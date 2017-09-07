package visualiser;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

import problem.Actor;
import problem.Cycle;
import problem.Distractor;
import problem.GridCell;
import problem.Opponent;
import problem.Player;
import problem.RaceSim;
import problem.RaceSimTools;
import problem.RaceState;
import problem.RaceState.Status;
import problem.Tour;
import problem.Track;

public class VisualisationPanel extends JComponent {
	private boolean autoscroll = false;
	private int topRow;
	private int bottomRow;
	private int leftCol;
	private int rightCol;

	private static int PIXEL_RADIUS = 10;

	private static int MIN_CELL_SIZE = 30;

	/** The scroll pane that owns this VP. */
	private JScrollPane owningScrollPane = null;

	/** UID, as required by Swing */
	private static final long serialVersionUID = -4286532773714402501L;

	/** Race state information */
	private Track track;
	private RaceState state;

	/** Images. Source image is rescaled in calculateTransform() */
	private Image imgFinishLineSource;
	private Image imgFinishLine;
	private Image imgDistractorActiveSource;
	private Image imgDistractorActive;
	private Image imgDistractorInactiveSource;
	private Image imgDistractorInactive;

	/** Height and width in pixels of track drawn */
	private int drawWidth;
	private int drawHeight;

	/** Height and width in pixels of the view area */
	private int viewWidth;
	private int viewHeight;

	/** Height and width in pixels of a track cell */
	private int cellWidth;
	private int cellHeight;

	/** max # of cells that fit on the display */
	private int numViewRows;
	private int numViewCols;

	/** Map a color to each letter of the alphabet for each player/opponent */
	private Map<Character, Color> letterColors;

	boolean showOpponentLines;

	private AffineTransform translation = AffineTransform.getTranslateInstance(
			0, -1);
	private AffineTransform transform = null;

	public VisualisationPanel() {
		super();
		this.setBackground(Color.WHITE);
		this.setOpaque(true);

		// Import source images
		try {
			imgFinishLineSource = ImageIO.read(getClass().getResource(
					"finish_line.jpg"));
			imgDistractorActiveSource = ImageIO.read(getClass().getResource(
					"distractor_active.png"));
			imgDistractorInactiveSource = ImageIO.read(getClass().getResource(
					"distractor_inactive.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Set player/opponent colors
		letterColors = new HashMap<Character, Color>();
		for (char c = 'A'; c <= 'Z'; c++) {
			int r = (57 * c) % 220;
			int g = (89 * c) % 220;
			int b = (31 * c) % 220;
			int alpha = 255;
			letterColors.put(c, new Color(r, g, b, alpha));
		}

		showOpponentLines = false;
	}

	public void setAutoscroll(boolean autoscroll) {
		this.autoscroll = autoscroll;
		if (autoscroll) {
			updateViewPosition();
		}
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.owningScrollPane = scrollPane;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public void setState(RaceState state) {
		this.state = state;
		if (autoscroll) {
			updateViewPosition();
		}
	}

	public Track getTrack() {
		return track;
	}

	public RaceState getState() {
		return state;
	}

	public void showOpponentLines() {
		showOpponentLines = true;
		repaint();
	}

	public void hideOpponentLines() {
		showOpponentLines = false;
		repaint();
	}

	public void calculateCellSizes() {
		cellWidth = (int) ((1.0 / track.getNumCols()) * drawWidth);
		cellHeight = (int) ((1.0 / track.getNumRows()) * drawHeight);
	}

	public void calculateTransform() {
		// Grow the display area to handle the
		drawWidth = viewWidth = owningScrollPane.getWidth() - 5;
		drawHeight = viewHeight = owningScrollPane.getHeight() - 5;
		calculateCellSizes();
		if (cellWidth < MIN_CELL_SIZE && cellHeight < MIN_CELL_SIZE) {
			drawWidth = MIN_CELL_SIZE * track.getNumCols();
			drawHeight = MIN_CELL_SIZE * track.getNumRows();
		} else if (cellWidth < MIN_CELL_SIZE) {
			drawWidth = MIN_CELL_SIZE * track.getNumCols();
			drawHeight = viewHeight = owningScrollPane.getHeight() - 20;
		} else if (cellHeight < MIN_CELL_SIZE) {
			drawHeight = MIN_CELL_SIZE * track.getNumRows();
			drawWidth = viewWidth = owningScrollPane.getWidth() - 20;
		}
		calculateCellSizes();

		// Make the width and height the same again.
		cellWidth = cellHeight = Math.min(cellHeight, cellWidth);
		drawWidth = cellWidth * track.getNumCols();
		drawHeight = cellHeight * track.getNumRows();
		calculateCellSizes();

		numViewCols = (int) ((1.0 * viewWidth) / cellWidth);
		numViewRows = (int) ((1.0 * viewHeight) / cellHeight);

		// Set the new sizing and repaint
		this.setPreferredSize(new Dimension(drawWidth, drawHeight));
		owningScrollPane.revalidate();
		owningScrollPane.repaint();

		transform = AffineTransform.getScaleInstance(drawWidth, -drawHeight);
		transform.concatenate(translation);

		imgFinishLine = imgFinishLineSource.getScaledInstance(cellWidth,
				cellHeight, Image.SCALE_FAST);
		imgDistractorActive = imgDistractorActiveSource.getScaledInstance(
				(int) (cellWidth / 2.5), (int) (cellHeight / 2.5),
				Image.SCALE_SMOOTH);
		imgDistractorInactive = imgDistractorInactiveSource.getScaledInstance(
				(int) (cellWidth / 2.5), (int) (cellHeight / 2.5),
				Image.SCALE_SMOOTH);
	}

	private void paintTrack(Graphics2D g2) {
		int numCols = track.getNumCols();
		int numRows = track.getNumRows();

		for (int col = 0; col < numCols; col++) {
			for (int row = 0; row < numRows; row++) {

				GridCell cell = new GridCell(row, col);
				int x = (int) (((double) col / numCols) * drawWidth);
				int y = (int) (((double) row / numRows) * drawHeight);

				switch (track.getCellType(cell)) {
				case EMPTY:
					break;
				case OBSTACLE:
					g2.setColor(Color.RED);
					g2.fillRect(x, y, cellWidth, cellHeight);
					break;
				case GOAL:
					g2.drawImage(imgFinishLine, x, y, this);
				default:
					break;
				}
			}
		}

		// Draw track grid
		g2.setColor(new Color(1.0f, 0.0f, 0.0f, 0.3f));
		g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, new float[] { 5.0f }, 0.0f));
		for (int i = 0; i <= numCols; i++) {
			double v = (double) i / numCols;
			if (v == 1) {
				v = 0.999;
			}
			Line2D line = new Line2D.Double(v, 0, v, 1);
			g2.draw(transform.createTransformedShape(line));

		}
		for (int i = 0; i <= numRows; i++) {
			double v = (double) i / numRows;
			Line2D line = new Line2D.Double(0, v, 1, v);
			g2.draw(transform.createTransformedShape(line));

		}
	}

	private void paintState(Graphics2D g2) {
		FontMetrics fm = g2.getFontMetrics();
		int numRows = track.getNumRows();
		int numCols = track.getNumCols();

		// Draw distractors
		for (Distractor d : state.getDistractors()) {
			int row = d.getPosition().getRow();
			int col = d.getPosition().getCol();
			int x = (int) ((((double) col + 0.6) / numCols) * drawWidth);
			int y = (int) ((((double) row + 0.375) / numRows) * drawHeight);
			if (d.hasAppeared()) {
				g2.drawImage(imgDistractorActive, x, y, this);
			} else {
				g2.drawImage(imgDistractorInactive, x, y, this);
			}

			// Draw string representing probability of appearing
			String s = String.valueOf(d.getAppearProbability());
			int imgWidth = imgDistractorActive.getWidth(this);
			g2.setColor(Color.BLACK);
			g2.drawString(s, x + (imgWidth - fm.stringWidth(s)) / 2, y);
		}

		// Draw the players
		for (int i = 0; i < state.getPlayers().size(); i++) {
			Player p = state.getPlayers().get(i);
			GridCell cell = p.getPosition();

			// Get number of players in the same cell
			int above = 0;
			int below = 0;
			for (int j = 0; j < state.getPlayers().size(); j++) {
				Player p2 = state.getPlayers().get(j);
				if (p2.getPosition().equals(cell)) {
					if (j < i) {
						above++;
					} else if (j > i) {
						below++;
					}
				}
			}

			int spacing = cellHeight / (above + below + 2);
			int x = (int) (((cell.getCol() + 0.5) / numCols) * drawWidth);
			int y = (int) (((double) cell.getRow() / numRows) * drawHeight)
					+ spacing * (1 + above);
			Color color = letterColors.get(p.getId().charAt(0));
			g2.setColor(color);
			g2.fillOval(x - PIXEL_RADIUS, y - PIXEL_RADIUS, PIXEL_RADIUS * 2,
					PIXEL_RADIUS * 2);
			g2.setColor(Color.WHITE);
			g2.drawString(p.getId(), x - fm.stringWidth(p.getId()) / 2,
					y + fm.getAscent() / 2);

		}

		// Draw the opponents
		for (int i = 0; i < state.getOpponents().size(); i++) {
			Opponent o = state.getOpponents().get(i);
			GridCell cell = o.getPosition();

			// Get number of opponents in the same cell
			int above = 0;
			int below = 0;
			for (int j = 0; j < state.getOpponents().size(); j++) {
				Opponent o2 = state.getOpponents().get(j);
				if (o2.getPosition().equals(cell)) {
					if (j < i) {
						above++;
					} else if (j > i) {
						below++;
					}
				}
			}

			// Draw circle
			int spacing = cellHeight / (above + below + 2);
			int x = (int) (((cell.getCol() + 0.25) / numCols) * drawWidth);
			int y = (int) (((double) cell.getRow() / numRows) * drawHeight)
					+ spacing * (1 + above);
			Color color = letterColors.get(o.getId().charAt(0));
			g2.setColor(color);
			g2.fillOval(x - PIXEL_RADIUS, y - PIXEL_RADIUS, PIXEL_RADIUS * 2,
					PIXEL_RADIUS * 2);

			// Draw lines showing next opponent states (not taking players
			// into account)
			if (showOpponentLines) {
				List<Player> empty = new ArrayList<Player>();
				Map<GridCell, Double> next = RaceSimTools.nextOpponentPositions(o,
						empty, track);
				for (Map.Entry<GridCell, Double> entry : next.entrySet()) {
					GridCell nextCell = entry.getKey();
					int nextX = (int) (((nextCell.getCol() + 0.25) / numCols) * drawWidth);
					int nextY = (int) (((nextCell.getRow() + 0.5) / numRows) * drawHeight);
					int r = color.getRed();
					int g = color.getGreen();
					int b = color.getBlue();
					int a = (int) (entry.getValue() * 255);
					g2.setColor(new Color(r, g, b, a));
					g2.setStroke(new BasicStroke(2.f));
					if (!nextCell.equals(cell)) {
						g2.drawLine(x, y, nextX, nextY);
					} else {
						g2.drawOval(x - PIXEL_RADIUS - 2, y - PIXEL_RADIUS - 2,
								2 * PIXEL_RADIUS + 4, 2 * PIXEL_RADIUS + 4);
					}
				}
			}

			// Write opponent's id
			g2.setColor(Color.WHITE);
			g2.drawString(o.getId(), x - fm.stringWidth(o.getId()) / 2,
					y + fm.getAscent() / 2);

		}

	}

	/**
	 * Updates the track view to center it on the player's leading bike.
	 * 
	 * @author Dimitri Klimenko
	 */
	public void updateViewPosition() {
		topRow = leftCol = Integer.MAX_VALUE;
		bottomRow = rightCol = Integer.MIN_VALUE;
		for (Player player : state.getPlayers()) {
			addPosition(player.getPosition());
		}
		for (Opponent opponent : state.getOpponents()) {
			addPosition(opponent.getPosition());
		}

		int row = 0;
		int col = 0;

		int rowDiff = bottomRow - topRow;
		if (rowDiff > numViewRows - 2) {
			row = bottomRow - numViewRows + 5;
		} else {
			row = (bottomRow + topRow) / 2 - numViewRows / 2;
		}
		int colDiff = rightCol - leftCol;
		if (colDiff > numViewCols - 2) {
			col = rightCol - numViewCols + 5;
		} else {
			col = (rightCol + leftCol) / 2 - numViewCols / 2;
		}

		if (row >= track.getNumRows() - numViewRows) {
			row = track.getNumRows() - numViewRows;
		}
		if (row < 0) {
			row = 0;
		}

		if (col >= track.getNumCols() - numViewCols) {
			col = track.getNumCols() - numViewCols;
		}
		if (col < 0) {
			col = 0;
		}

		int x = (int) (((double) col / track.getNumCols()) * drawWidth);
		int y = (int) (((double) row / track.getNumRows()) * drawHeight);
		owningScrollPane.getViewport().setViewPosition(new Point(x, y));
	}

	public void addPosition(GridCell cell) {
		int row = cell.getRow();
		int col = cell.getCol();
		if (row < topRow) {
			topRow = row;
		}
		if (row > bottomRow) {
			bottomRow = row;
		}
		if (col < leftCol) {
			leftCol = col;
		}
		if (col > rightCol) {
			rightCol = col;
		}
	}

	/**
	 * @author Peter(modify) Should paint all components based on 'current'
	 *         values
	 */
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		if (state == null || track == null) {
			return;
		}

		calculateTransform();
		Graphics2D g2 = (Graphics2D) graphics;
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, getWidth(), getHeight());
		paintTrack(g2);
		paintState(g2);
	}
}
