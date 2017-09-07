package csse2002.security;

import javax.swing.*;

/**
 * The view for the Spy Simulator.
 */
@SuppressWarnings("serial")
public class SpyView extends JFrame {

	// the model of the Spy Simulator
	private SpyModel model;

	/** THESE FIELDS USED FOR TESTING: DO NOT CHANGE DECLARATION! */
	// text area for displaying the current knowledge state of the spy
	public JTextArea txtSpy;
	// scroll panel for containing txtSpy
	private JScrollPane scpSpy;
	// text area for displaying informant interactions and errors
	public JTextArea txtInfo;
	// scroll panel for containing txtInfo
	private JScrollPane scpInfo;
	// informants field: for entering the name of informants to be read
	public JTextField txtInformantsFile;
	// read button: for reading the informants in txtInformantsFile
	public JButton cmdRead;
	// meet informants button: for updating knowledge state of spy after
	// encounter with next informant
	public JButton cmdMeet;
	// guess button: for making the spy guess the secret
	public JButton cmdGuess;

	/** END DECLARATION OF TESTING FIELDS */

	/**
	 * Creates a new Spy Simulator window.
	 */
	public SpyView(SpyModel model) {
		this.model = model;
		// REMOVE THIS LINE AND COMPLETE THIS METHOD
	}

	// REMOVE THIS LINE AND ADD YOUR OWN METHODS ETC HERE

}
