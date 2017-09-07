package visualiser;

import java.io.File;

import javax.swing.JApplet;

public class VisApplet extends JApplet {
	/** UID, as required by Swing */
	private static final long serialVersionUID = 8479710856496756661L;

	public void init() {
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					new Visualiser(VisApplet.this);
				}
			});
		} catch (Exception e) {
			System.err.println("Could not create the visualizer.");
		}
		this.setSize(800, 600);
	}
}