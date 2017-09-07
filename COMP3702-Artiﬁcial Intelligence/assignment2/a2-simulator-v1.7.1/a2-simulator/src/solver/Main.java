package solver;

import java.io.IOException;

import problem.Setup;
import problem.Tour;

public class Main {
	
	static String defaultCycleFile = "testcases/example/cycle.txt";
	static String defaultMetaTrackFile = "testcases/example/meta-track.txt";
	static String defaultOutputFile = "result.txt";

	public static void main(String[] args) {
		
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
		
		// Print times
		for (int i = 0; i < tour.getNumRaces(); i++) {
			double ptSeconds = tour.getPrepareTime(i)/1000.0;
			double stepTime = (double) tour.getRaceTime(i)/tour.getTurnNo(i);
			System.out.println("Race " + i + " prepare time: " + ptSeconds +
					" s, average time per step: " + stepTime + " ms.");
		}
	}

}
