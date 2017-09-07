package csse2002.security.test;

import junit.framework.Assert;

import org.junit.Test;
import csse2002.math.*;
import csse2002.security.*;

public class SpySimulatorTest {

	/**
	 * The line separator to be used to separate lines in JTextField text.
	 */
	private final String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * Test that the Simulator has been properly initialised.
	 */
	@Test
	public void testInitialState() {
		BigFraction ks = new BigFraction(1, 2);
		SpyModel model = new SpyModel();
		SpyView view = new SpyView(model);
		new SpyController(model, view);
		// check that cmdRead button is correctly labeled.
		Assert.assertEquals("Read Informants", view.cmdRead.getText());
		// check that cmdMeet button is correctly labeled.
		Assert.assertEquals("Meet Informant", view.cmdMeet.getText());
		// check that txtSpy is correct.
		Assert.assertEquals(getSpyString(ks), view.txtSpy.getText());
		// check that txtInfo is correct.
		Assert.assertEquals("", view.txtInfo.getText());
		// check that txtInformantsFile is correct.
		Assert.assertEquals("", view.txtInformantsFile.getText());
	}

	/**
	 * Test behaviour when file cannot be read.
	 */
	@Test
	public void testFileReadError() {
		BigFraction ks = new BigFraction(1, 2);
		SpyModel model = new SpyModel();
		SpyView view = new SpyView(model);
		new SpyController(model, view);

		// press Read Informants button when no file is given
		view.cmdRead.doClick();

		// check that txtSpy is correct.
		Assert.assertEquals(getSpyString(ks), view.txtSpy.getText());
		// check that txtInfo is correct.
		Assert.assertEquals(getReadError(), view.txtInfo.getText());
	}

	/**
	 * Test behaviour when there is no informant to meet.
	 */
	@Test
	public void testMeetError() {
		BigFraction ks = new BigFraction(1, 2);
		SpyModel model = new SpyModel();
		SpyView view = new SpyView(model);
		new SpyController(model, view);

		// press meet when no informants have been loaded.
		view.cmdMeet.doClick();

		// check that txtSpy is correct.
		Assert.assertEquals(getSpyString(ks), view.txtSpy.getText());
		// check that txtInfo is correct.
		Assert.assertEquals(getMeetError(), view.txtInfo.getText());
	}

	/**
	 * Test behaviour when files are read and informants are met
	 */
	@Test
	public void testFileReadAndMeet() {
		BigFraction ks = new BigFraction(1, 2);
		SpyModel model = new SpyModel();
		SpyView view = new SpyView(model);
		new SpyController(model, view);

		// read in informants (1/3, 1/6), (1/2, 2/3)
		view.txtInformantsFile.setText("informants1.txt");
		view.cmdRead.doClick();
		// read in more informants (1/2. 1/3)
		view.txtInformantsFile.setText("informants2.txt");
		view.cmdRead.doClick();

		// check that txtSpy is correct.
		Assert.assertEquals(getSpyString(ks), view.txtSpy.getText());
		// check that txtInfo is correct.
		Assert.assertEquals(getReadMessage(), view.txtInfo.getText());

		// meet informant (1/3, 1/6)
		view.cmdMeet.doClick();
		TwoCoinChannel informant =
				new TwoCoinChannel(new BigFraction(1, 3), new BigFraction(1, 6));
		ks = this.checkMeetState(ks, informant, view);

		// meet informant (1/2, 2/3)
		view.cmdMeet.doClick();
		informant =
				new TwoCoinChannel(new BigFraction(1, 2), new BigFraction(2, 3));
		ks = this.checkMeetState(ks, informant, view);

		// meet informant (1/2, 1/3)
		view.cmdMeet.doClick();
		informant =
				new TwoCoinChannel(new BigFraction(1, 2), new BigFraction(1, 3));
		ks = this.checkMeetState(ks, informant, view);
	}

	/**
	 * Test spy guessing.
	 */
	@Test
	public void testGuess() {
		BigFraction ks = new BigFraction(1, 2);
		SpyModel model = new SpyModel();
		SpyView view = new SpyView(model);
		new SpyController(model, view);

		// make a guess
		view.cmdGuess.doClick();
		ks = this.checkGuessState(ks, view);
	}

	// HELPER METHODS

	/**
	 * Checks the state is correct after a meet, and returns new knowledge state
	 * of spy after meet.
	 */
	private BigFraction checkMeetState(BigFraction ks,
			TwoCoinChannel informant, SpyView view) {
		String actualTxtInfo = view.txtInfo.getText();
		Boolean outcome;
		if (actualTxtInfo.equals(getInfoString(informant, true))) {
			outcome = true;
		} else {
			Assert.assertEquals(getInfoString(informant, false), actualTxtInfo);
			outcome = false;
		}
		ks = informant.aPosteriori(ks, outcome);
		Assert.assertEquals(getSpyString(ks), view.txtSpy.getText());
		return ks;
	}

	/**
	 * Checks the state is correct after a guess, and returns new knowledge state
	 * of spy after guess.
	 */
	private BigFraction checkGuessState(BigFraction ks, SpyView view) {
		String actualTxtInfo = view.txtInfo.getText();
		Boolean secret;
		Boolean guess =
				((ks.compareTo(new BigFraction(1, 2)) >= 0) ? true : false);
		if (actualTxtInfo.equals(getInfoString(true, guess))) {
			secret = true;
		} else {
			Assert.assertEquals(getInfoString(false, guess), actualTxtInfo);
			secret = false;
		}
		ks = (secret ? BigFraction.ONE : BigFraction.ZERO);
		Assert.assertEquals(getSpyString(ks), view.txtSpy.getText());
		return ks;
	}

	// HELPER METHODS FOR STRINGS

	private String getSpyString(BigFraction p) {
		return "Spy thinks ..." + LINE_SEPARATOR
				+ "Secret is true with probability " + p;
	}

	private String getReadError() {
		return "Error reading from file.";
	}

	private String getReadMessage() {
		return "Informants added from file.";
	}

	private String getMeetError() {
		return "There is no informant to meet.";
	}

	private String getInfoString(TwoCoinChannel informant, boolean outcome) {
		return "Informant says ..." + LINE_SEPARATOR + "Heads-bias if true: "
				+ informant.getCoinBias(true) + LINE_SEPARATOR
				+ "Heads-bias if false: " + informant.getCoinBias(false)
				+ LINE_SEPARATOR + "Result is ... "
				+ (outcome ? "heads" : "tails") + "!";
	}

	private String getInfoString(Boolean secret, Boolean guess) {
		return "Spy guesses that secret is " + guess + LINE_SEPARATOR
				+ "and is ... " + (secret == guess ? "correct" : "incorrect")
				+ "!";
	}
}
