package visualiser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import problem.Action;
import problem.Player;
import problem.RaceSim;
import problem.RaceState;
import problem.Setup;
import problem.Tour;
import problem.Track;
import solver.Consultant;

public class Visualiser {
	private Container container;

	private Setup setup;
	private Tour tour;
	private int raceNumber;
	private int frameNumber;
	List<RaceState> stateHistory;
	List<ArrayList<Action>> actionHistory;
	private VisualisationPanel vp;

	/** A check box to control whether the game automatically scrolls. */
	private JCheckBox autoscrollCheckBox;

	private JPanel infoPanel;
	private JTable scoreTable;
	private JTextArea infoTextArea;

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem loadSetupItem, loadTourItem, writeOutputItem, solveItem,
			exitItem;
	private JMenu gameMenu;
	private JMenuItem playPauseItem, resetItem, backItem, forwardItem,
			stepItem, nextRaceItem, prevRaceItem, showLinesItem, hideLinesItem;

	private JPanel gameControls;
	private JSlider manualSlider;
	private JSlider framePeriodSlider;

	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			return new ImageIcon(path, description);
		}
	}

	private JButton playPauseButton, resetButton, backButton, forwardButton,
			stepButton, prevRaceButton, nextRaceButton;
	private ImageIcon rewindIcon = createImageIcon("bb.gif", "Rewind");
	private ImageIcon fastForwardIcon = createImageIcon("ff.gif",
			"Fast Forward");
	private ImageIcon playIcon = createImageIcon("play.gif", "Play");
	private ImageIcon pauseIcon = createImageIcon("pause.gif", "Pause");
	private ImageIcon resetIcon = createImageIcon("reset.gif", "Reset");
	private ImageIcon backIcon = createImageIcon("back.gif", "Back");
	private ImageIcon forwardIcon = createImageIcon("forward.gif", "Forward");
	private ImageIcon stepIcon = createImageIcon("step.gif", "Step");

	private boolean hasTour;
	private boolean playing;
	private boolean wasPlaying;

	private static final int FRAME_PERIOD_MIN = 50;
	private static final int FRAME_PERIOD_MAX = 2000;
	private static final int FRAME_PERIOD_INIT = 1000;

	private Timer animationTimer;

	private File defaultPath;

	private class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd.equals("Load setup")) {
				loadSetup();
			} else if (cmd.equals("Load tour result")) {
				loadTour();
			} else if (cmd.equals("Solve new tour result")) {
				solveTour();
			} else if (cmd.equals("Write output")) {
				writeOutput();
			} else if (cmd.equals("Play")) {
				// vp.playPauseAnimation();
			} else if (cmd.equals("Pause")) {
				// vp.playPauseAnimation();
			} else if (cmd.equals("Reset")) {
				// vp.resetGame();
			} else if (cmd.equals("Back one step")) {
				setFrameNumber(frameNumber - 1);
			} else if (cmd.equals("Forward one step")) {
				setFrameNumber(frameNumber + 1);
			} else if (cmd.equals("Simulate one step")) {
				// vp.stepRace();
			} else if (cmd.equals("Next race")) {
				setRaceNumber(raceNumber + 1);
			} else if (cmd.equals("Previous race")) {
				setRaceNumber(raceNumber - 1);
			} else if (cmd.equals("Show opponent policy lines")) {
				vp.showOpponentLines();
			} else if (cmd.equals("Hide opponent policy lines")) {
				vp.hideOpponentLines();
			}
		}
	}

	private class ResizeListener implements ComponentListener {
		@Override
		public void componentResized(ComponentEvent e) {
			updateTickSpacing();
		}

		@Override
		public void componentHidden(ComponentEvent e) {
		}

		@Override
		public void componentMoved(ComponentEvent e) {
		}

		@Override
		public void componentShown(ComponentEvent e) {
		}
	}

	private AbstractTableModel tableDataModel = new AbstractTableModel() {

		/** Required UID */
		private static final long serialVersionUID = 2401048818869542809L;

		public int getColumnCount() {
			if (!hasTour || stateHistory == null || stateHistory.isEmpty()) {
				return 0;
			}
			return stateHistory.get(0).getPlayers().size() + 1;
		}

		public int getRowCount() {
			return 6;
		}

		public Object getValueAt(int row, int col) {
			if (col == 0) {
				if (row == 0) {
					return "Player";
				} else if (row == 1) {
					return "Prev action";
				} else if (row == 2) {
					return "Next action";
				} else if (row == 3) {
					return "Cycle";
				} else if (row == 4) {
					return "Damage this turn";
				} else {
					return "Obstacle mode";
				}
			} else {
				RaceState state = stateHistory.get(frameNumber);
				int playerIndex = col - 1;
				Player player = state.getPlayers().get(playerIndex);
				if (row == 0) {
					return player.getId();
				} else if (row == 1) {
					if (frameNumber - 1 < 0) {
						return "-";
					} else {
						return actionHistory.get(frameNumber - 1)
								.get(playerIndex).toString();
					}
				} else if (row == 2) {
					if (frameNumber >= actionHistory.size()) {
						return "-";
					} else {
						return actionHistory.get(frameNumber).get(playerIndex)
								.toString();
					}
				} else if (row == 3) {
					return player.getCycle().toString();
				} else if (row == 4) {
					return "$" + player.getDamageCost();
				} else {
					return player.isObstacle();
				}
			}
		}
	};

	private ResizeListener resizeListener = new ResizeListener();
	private MenuListener menuListener = new MenuListener();

	private ChangeListener manualSliderListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			if (!manualSlider.getValueIsAdjusting() && wasPlaying) {
				wasPlaying = false;
				if (manualSlider.getValue() < manualSlider.getMaximum()) {
					playPauseAnimation();
				}
			}
			int value = manualSlider.getValue();
			if (value < 0) {
				value = 0;
			}
			setFrameNumber(value);
		}
	};

	private MouseListener manualSliderClickListener = new MouseListener() {
		@Override
		public void mousePressed(MouseEvent e) {
			if (playing) {
				wasPlaying = true;
				playPauseAnimation();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	};

	private ChangeListener framePeriodListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent e) {
			animationTimer.setDelay(framePeriodSlider.getValue());
		}
	};

	private ActionListener autoscrollListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			vp.setAutoscroll(autoscrollCheckBox.isSelected());
		}
	};

	private ActionListener playPauseListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			playPauseAnimation();
		}
	};

	private ActionListener prevRaceListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			setRaceNumber(raceNumber - 1);
		}
	};

	private ActionListener nextRaceListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			setRaceNumber(raceNumber + 1);
		}
	};

	private ActionListener forwardListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			setFrameNumber(frameNumber + 1);
		}
	};

	private ActionListener backListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			setFrameNumber(frameNumber - 1);
		}
	};

	private ActionListener stepListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// vp.stepRace();
		}
	};

	private ActionListener resetListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			setFrameNumber(0);
		}
	};

	public Visualiser(Container container, File defaultPath) {
		this.container = container;
		this.defaultPath = defaultPath;
		createComponents();
		setHasTour(false);
		animationTimer = new Timer(FRAME_PERIOD_INIT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setFrameNumber(frameNumber + 1);
			}
		});
	}

	public Visualiser(Container container) {
		this(container, null);
		try {
			this.defaultPath = new File(".").getCanonicalFile();
		} catch (IOException e) {
		}
	}

	private void createComponents() {
		vp = new VisualisationPanel();
		JPanel wp = new JPanel(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(vp);
		vp.setScrollPane(scrollPane);
		autoscrollCheckBox = new JCheckBox("Autoscroll");
		autoscrollCheckBox.setSelected(false);
		autoscrollCheckBox.addActionListener(autoscrollListener);

		wp.add(scrollPane, BorderLayout.CENTER);
		wp.add(autoscrollCheckBox, BorderLayout.SOUTH);
		container.setLayout(new BorderLayout());
		wp.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(5, 10, 10, 10),
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
		container.add(wp, BorderLayout.CENTER);

		infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		infoTextArea = new JTextArea();
		infoTextArea.setEditable(false);
		infoTextArea.setPreferredSize(new Dimension(250, 90));
		updateInfoText();
		// JScrollPane textScrollPane = new JScrollPane(infoTextArea);
		// JPanel p = new JPanel();
		// p.add(infoTextArea);
		infoPanel.add(infoTextArea, BorderLayout.WEST);
		scoreTable = new JTable(tableDataModel);
		// JScrollPane tableScrollPane = new JScrollPane(scoreTable);
		infoPanel.add(scoreTable, BorderLayout.CENTER);
		infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

		container.add(infoPanel, BorderLayout.NORTH);

		createMenus();
		createAnimationControls();
	}

	private void createMenus() {
		menuBar = new JMenuBar();
		createFileMenu();
		createGameMenu();
		if (container instanceof JFrame) {
			((JFrame) container).setJMenuBar(menuBar);
		} else if (container instanceof JApplet) {
			((JApplet) container).setJMenuBar(menuBar);
		}
	}

	private void createFileMenu() {
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.getAccessibleContext().setAccessibleDescription(
				"Load configs or close the app.");
		menuBar.add(fileMenu);

		loadSetupItem = new JMenuItem("Load setup");
		loadSetupItem.setMnemonic(KeyEvent.VK_S);
		loadSetupItem.addActionListener(menuListener);
		fileMenu.add(loadSetupItem);

		loadTourItem = new JMenuItem("Load tour result");
		loadTourItem.setMnemonic(KeyEvent.VK_T);
		loadTourItem.addActionListener(menuListener);
		fileMenu.add(loadTourItem);

		solveItem = new JMenuItem("Solve new tour result");
		solveItem.setMnemonic(KeyEvent.VK_N);
		solveItem.addActionListener(menuListener);
		fileMenu.add(solveItem);

		writeOutputItem = new JMenuItem("Write output");
		writeOutputItem.setMnemonic(KeyEvent.VK_W);
		writeOutputItem.addActionListener(menuListener);
		fileMenu.add(writeOutputItem);

		fileMenu.addSeparator();
		exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.addActionListener(menuListener);
		fileMenu.add(exitItem);
	}

	private void createGameMenu() {
		gameMenu = new JMenu("Game");
		gameMenu.setMnemonic(KeyEvent.VK_A);
		fileMenu.getAccessibleContext().setAccessibleDescription(
				"Manage the animation.");
		menuBar.add(gameMenu);
		gameMenu.setEnabled(false);

		stepItem = new JMenuItem("Simulate one step");
		stepItem.setMnemonic(KeyEvent.VK_S);
		stepItem.addActionListener(menuListener);
		gameMenu.add(stepItem);

		playPauseItem = new JMenuItem("Play");
		playPauseItem.setMnemonic(KeyEvent.VK_P);
		playPauseItem.addActionListener(menuListener);
		gameMenu.add(playPauseItem);

		resetItem = new JMenuItem("Reset");
		resetItem.setMnemonic(KeyEvent.VK_T);
		resetItem.addActionListener(menuListener);
		gameMenu.add(resetItem);

		backItem = new JMenuItem("Back one step");
		backItem.setMnemonic(KeyEvent.VK_B);
		backItem.addActionListener(menuListener);
		backItem.setEnabled(false);
		gameMenu.add(backItem);

		forwardItem = new JMenuItem("Forward one step");
		forwardItem.setMnemonic(KeyEvent.VK_F);
		forwardItem.addActionListener(menuListener);
		forwardItem.setEnabled(false);
		gameMenu.add(forwardItem);

		nextRaceItem = new JMenuItem("Next race");
		nextRaceItem.addActionListener(menuListener);
		nextRaceItem.setEnabled(false);
		gameMenu.add(nextRaceItem);

		prevRaceItem = new JMenuItem("Previous race");
		prevRaceItem.addActionListener(menuListener);
		prevRaceItem.setEnabled(false);
		gameMenu.add(prevRaceItem);

		showLinesItem = new JMenuItem("Show opponent policy lines");
		showLinesItem.addActionListener(menuListener);
		gameMenu.add(showLinesItem);

		hideLinesItem = new JMenuItem("Hide opponent policy lines");
		hideLinesItem.addActionListener(menuListener);
		gameMenu.add(hideLinesItem);
	}

	private void createAnimationControls() {
		Font sliderFont = new Font("Arial", Font.PLAIN, 12);

		gameControls = new JPanel();
		gameControls
				.setLayout(new BoxLayout(gameControls, BoxLayout.PAGE_AXIS));

		JLabel manualLabel = new JLabel("Frame #");
		manualLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		manualSlider = new JSlider(JSlider.HORIZONTAL);
		manualSlider.setPaintTicks(true);
		manualSlider.setPaintLabels(true);
		manualSlider.setFont(sliderFont);
		manualSlider.addChangeListener(manualSliderListener);
		manualSlider.addMouseListener(manualSliderClickListener);
		manualSlider.setMinorTickSpacing(1);
		manualSlider.addComponentListener(resizeListener);

		JLabel framePeriodLabel = new JLabel("Frame period (ms)");
		framePeriodLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		framePeriodSlider = new JSlider(JSlider.HORIZONTAL, FRAME_PERIOD_MIN,
				FRAME_PERIOD_MAX, FRAME_PERIOD_INIT);
		framePeriodSlider.setMajorTickSpacing(200);
		framePeriodSlider.setMinorTickSpacing(10);
		framePeriodSlider.setPaintTicks(true);
		framePeriodSlider.setPaintLabels(true);
		framePeriodSlider.setLabelTable(framePeriodSlider.createStandardLabels(
				200, 200));
		framePeriodSlider.setFont(sliderFont);
		framePeriodSlider.addChangeListener(framePeriodListener);
		JPanel framePeriodPanel = new JPanel();
		framePeriodPanel.setLayout(new BoxLayout(framePeriodPanel,
				BoxLayout.PAGE_AXIS));
		framePeriodPanel.add(framePeriodLabel);
		framePeriodPanel.add(Box.createRigidArea(new Dimension(0, 2)));
		framePeriodPanel.add(framePeriodSlider);

		stepButton = new JButton(stepIcon);
		stepButton.addActionListener(stepListener);
		backButton = new JButton(backIcon);
		backButton.addActionListener(backListener);
		backButton.setEnabled(false);
		forwardButton = new JButton(forwardIcon);
		forwardButton.addActionListener(forwardListener);
		forwardButton.setEnabled(false);
		playPauseButton = new JButton(playIcon);
		playPauseButton.addActionListener(playPauseListener);
		resetButton = new JButton(resetIcon);
		resetButton.addActionListener(resetListener);
		nextRaceButton = new JButton(fastForwardIcon);
		nextRaceButton.addActionListener(nextRaceListener);
		prevRaceButton = new JButton(rewindIcon);
		prevRaceButton.addActionListener(prevRaceListener);

		gameControls.add(new JSeparator(JSeparator.HORIZONTAL));
		gameControls.add(Box.createRigidArea(new Dimension(0, 2)));
		gameControls.add(manualLabel);
		gameControls.add(Box.createRigidArea(new Dimension(0, 2)));
		gameControls.add(manualSlider);
		gameControls.add(Box.createRigidArea(new Dimension(0, 5)));

		JPanel p2 = new JPanel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.LINE_AXIS));
		p2.add(backButton);
		p2.add(Box.createRigidArea(new Dimension(5, 0)));
		p2.add(forwardButton);
		p2.add(Box.createRigidArea(new Dimension(5, 0)));
		p2.add(stepButton);
		p2.add(Box.createRigidArea(new Dimension(5, 0)));
		p2.add(playPauseButton);
		p2.add(Box.createRigidArea(new Dimension(5, 0)));
		p2.add(resetButton);

		JPanel p3 = new JPanel();
		p3.setLayout(new BoxLayout(p3, BoxLayout.LINE_AXIS));
		p3.add(prevRaceButton);
		p3.add(Box.createRigidArea(new Dimension(5, 0)));
		p3.add(nextRaceButton);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		buttonPanel.add(p2);
		buttonPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		buttonPanel.add(p3);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(buttonPanel, BorderLayout.WEST);
		bottomPanel.add(framePeriodPanel, BorderLayout.CENTER);
		gameControls.add(bottomPanel);
		gameControls.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
		container.add(gameControls, BorderLayout.SOUTH);
	}

	private File askForFile() {
		JFileChooser fc = new JFileChooser(defaultPath);
		int returnVal = fc.showOpenDialog(container);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		try {
			defaultPath = fc.getSelectedFile().getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fc.getSelectedFile();
	}

	private void showFileError(File f) {
		JOptionPane.showMessageDialog(container,
				"Error loading " + f.getName(), "File I/O Error",
				JOptionPane.ERROR_MESSAGE);
	}

	private void writeOutput(File f) {
		try {
			tour.outputToFile(f.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeOutput() {
		File f = askForFile();
		if (f == null) {
			return;
		}
		writeOutput(f);
	}

	private void loadSetup() {
		JOptionPane.showMessageDialog(container, "Select cycle file",
				"Load setup", JOptionPane.INFORMATION_MESSAGE);
		File cycleFile = askForFile();
		if (cycleFile == null) {
			return;
		}
		JOptionPane.showMessageDialog(container, "Select meta-track file",
				"Load setup", JOptionPane.INFORMATION_MESSAGE);
		File metaTrackFile = askForFile();
		if (metaTrackFile == null) {
			return;
		}
		setSetup(new Setup(cycleFile.getPath(), metaTrackFile.getPath()));
	}

	public void setSetup(Setup setup) {
		this.setup = setup;
		setHasTour(false);
		updateInfoText();
	}

	public void loadTour() {
		if (setup == null) {
			JOptionPane.showMessageDialog(container, "Load setup first",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		File f = askForFile();
		if (f == null) {
			return;
		}
		Tour tempTour = new Tour(setup);
		try {
			boolean success = tempTour.loadFromFile(f.getPath());
			if (success) {
				setTour(tempTour);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void solveTour() {
		if (setup == null) {
			JOptionPane.showMessageDialog(container, "Load setup first",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		tour = new Tour(setup);
		Consultant consultant = new Consultant();
		consultant.solveTour(tour);
		setHasTour(true);
	}

	public void setTour(Tour tour) {
		this.tour = tour;
		setHasTour(true);
	}

	public void setHasTour(boolean hasTour) {
		this.hasTour = hasTour;
		if (hasTour) {
			setRaceNumber(0);
			vp.setVisible(true);
		} else {
			vp.setVisible(false);
		}
		tableDataModel.fireTableStructureChanged();
		updateControls();
		updateTable();
		updateInfoText();
		gameControls.setVisible(hasTour);
		gameMenu.setEnabled(hasTour);
		writeOutputItem.setEnabled(hasTour);

		vp.repaint();
	}

	public void setRaceNumber(int raceNumber) {
		if (raceNumber < 0 || raceNumber >= tour.getNumRaces()) {
			return;
		}
		this.raceNumber = raceNumber;
		stateHistory = tour.getStateHistory(raceNumber);
		actionHistory = tour.getActionHistory(raceNumber);
		vp.setTrack(tour.getTrack(raceNumber));
		setFrameNumber(0);
		updateMaximum();
	}

	public void setFrameNumber(int frameNumber) {
		if (frameNumber < 0 || stateHistory == null
				|| frameNumber >= stateHistory.size()) {
			return;
		}
		this.frameNumber = frameNumber;
		updateControls();
		updateTable();
		updateInfoText();
		manualSlider.setValue(frameNumber);
		vp.setState(stateHistory.get(frameNumber));
	}

	public void setPlaying(boolean playing) {
		if (this.playing == playing) {
			return;
		}
		this.playing = playing;
		if (playing) {
			playPauseItem.setText("Pause");
			playPauseButton.setIcon(pauseIcon);
		} else {
			playPauseItem.setText("Play");
			playPauseButton.setIcon(playIcon);
		}
		playPauseButton.repaint();

	}

	public void updateControls() {
		if (!hasTour) {

			return;
		}
		boolean canBack = frameNumber > 0;
		backButton.setEnabled(canBack);
		backItem.setEnabled(canBack);

		boolean canForward = stateHistory != null && frameNumber < stateHistory.size() - 1;
		forwardButton.setEnabled(canForward);
		forwardItem.setEnabled(canForward);

		boolean canStep = canForward;
		stepButton.setEnabled(canStep);
		stepItem.setEnabled(canStep);

		boolean canNextRace = raceNumber < tour.getNumRaces() - 1;
		nextRaceItem.setEnabled(canNextRace);
		nextRaceButton.setEnabled(canNextRace);

		boolean canPrevRace = raceNumber > 0;
		prevRaceItem.setEnabled(canPrevRace);
		prevRaceButton.setEnabled(canPrevRace);
	}

	public void updateInfoText() {
		StringBuilder sb = new StringBuilder();
		String ls = System.getProperty("line.separator");
		if (setup == null) {
			sb.append("Setup: not loaded" + ls + ls);
		} else {
			sb.append("Setup: " + setup.getCycleFileNoPath());
			sb.append(" " + setup.getMetaTrackFileNoPath() + ls);
		}

		if (!hasTour) {
			sb.append("Tour: not loaded" + ls);
		} else {
			sb.append("Tour: loaded" + ls);
			sb.append("Startup money: $" + setup.getStartupMoney() + ls);
			sb.append("Final money: $" + tour.getCurrentMoney() + ls);
			sb.append("Net profit: $" + (tour.getCurrentMoney() - setup.getStartupMoney()) + ls);
			if (tour.getNumRaces() > 0) {
				Track track = tour.getTrack(raceNumber);
				sb.append("Current track: "
						+ track.getFileNameNoPath().split("\\.")[0]);
			}
			/*
			 * sb.append("Total damage this race: $" + tour.getRaceDamageCost(
			 * raceNumber) + ls); sb.append("Total tour damage: $" +
			 * tour.getTotalDamageCost() + ls);
			 * sb.append("Money spent on cycles: $" + tour.getTotalCycleCost() +
			 * ls); sb.append("Total spent on registration: $" +
			 * tour.getTotalRegistrationCost() + ls);
			 */
		}
		infoTextArea.setText(sb.toString());
	}

	public void updateTable() {
		tableDataModel.fireTableDataChanged();
		if (tableDataModel.getColumnCount() > 0) {
			scoreTable.createDefaultColumnsFromModel();
			scoreTable.getColumnModel().getColumn(0).setMaxWidth(250);
			scoreTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		}
	}

	public void updateMaximum() {
		int maximum = stateHistory.size() - 1;
		manualSlider.setMaximum(maximum);
		updateTickSpacing();
		updateControls();
	}

	public void updateSliderSpacing(JSlider slider) {
		int width = slider.getBounds().width;
		int max = slider.getMaximum();
		int spacing = 1;
		int mode = 1;
		double pxPerLabel = (double) width * spacing / max;
		if (pxPerLabel <= 0) {
			return;
		}
		while (pxPerLabel <= 30) {
			if (mode == 1) {
				spacing *= 2;
				pxPerLabel *= 2;
				mode = 2;
			} else if (mode == 2) {
				spacing = spacing * 5 / 2;
				pxPerLabel *= 2.5;
				mode = 5;
			} else {
				spacing *= 2;
				pxPerLabel *= 2;
				mode = 1;
			}
		}
		slider.setMajorTickSpacing(spacing);
		int min = slider.getMinimum();
		if (min % spacing > 0) {
			min += (spacing - (min % spacing));
		}
		slider.setLabelTable(slider.createStandardLabels(spacing, min));
	}

	public void updateTickSpacing() {
		updateSliderSpacing(manualSlider);
		updateSliderSpacing(framePeriodSlider);
	}

	public void playPauseAnimation() {
		if (!hasTour) {
			return;
		}
		if (animationTimer.isRunning()) {
			animationTimer.stop();
			setPlaying(false);
		} else {
			animationTimer.start();
			setPlaying(true);
		}
	}

	static String defaultCycleFile = "testcases/example/cycle.txt";
	static String defaultMetaTrackFile = "testcases/example/meta-track.txt";
	static String defaultOutputFile = "result.txt";

	public static void main(String[] args) {
		JFrame frame = new JFrame("Assignment 2 visualiser");
		Visualiser vis = new Visualiser(frame);

		// Load input files
		String cycleFile = defaultCycleFile;
		String metaTrackFile = defaultMetaTrackFile;
		String outputFile = defaultOutputFile;
		if (args.length != 0 && args.length != 3) {
			System.out.println("Arguments: cycle-filename meta-track-filename "
					+ "result-filename");
			System.exit(1);
		} else if (args.length == 3) {
			cycleFile = args[0].trim();
			metaTrackFile = args[1].trim();
			outputFile = args[2].trim();
		}
		Setup setup = new Setup(cycleFile, metaTrackFile);

		// Create and solve tour
		Consultant consultant = new Consultant();
		Tour tour = new Tour(setup);
		consultant.solveTour(tour);
		try {
			tour.outputToFile(outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		vis.setSetup(setup);
		vis.setTour(tour);

		/*
		 * // Print times for (int i = 0; i < tour.getNumRaces(); i++) { double
		 * ptSeconds = tour.getPrepareTime(i)/1000.0; double stepTime = (double)
		 * tour.getRaceTime(i)/tour.getTurnNo(i); System.out.println("Race " + i
		 * + " prepare time: " + ptSeconds + " s, average time per step: " +
		 * stepTime + " ms."); }
		 */

		frame.setBounds(100, 100, 800, 600);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
	}
}
