package csse2002.security;

/**
 * This class provides the main method that runs the Spy Simulator.
 */
public class SpySimulator {

	/** Starts the GUI. */
	public static void main(String[] args) {
		SpyModel model = new SpyModel();
		SpyView view = new SpyView(model);
		new SpyController(model, view);
		view.setVisible(true);
	}
}
