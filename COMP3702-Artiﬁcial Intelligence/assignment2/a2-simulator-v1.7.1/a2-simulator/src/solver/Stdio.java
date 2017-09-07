package solver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import problem.Action;
import problem.Cycle;
import problem.GridCell;
import problem.Player;
import problem.RaceSimTools;
import problem.RaceState;
import problem.Setup;
import problem.Tour;
import problem.Track;

public class Stdio {
	
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
		
		// Output file paths
		File f = new File(cycleFile);
		System.out.println("Cycle file: " + f.getAbsolutePath());
		f = new File(metaTrackFile);
		System.out.println("Meta track file: " + f.getAbsolutePath());
		
		// Purchase cycles
		Tour tour = new Tour(setup);
		List<Cycle> cycles = tour.getPurchasableCycles();
		System.out.println("Purchase cycles with e.g. \"buy " +
				cycles.get(0).getName() + "\". Enter \"next\" when done");
		String input = getInput();
		while (!input.equals("next")) {
			if (input.contains("buy")) {
				input = input.replace("buy", "");
				input = input.trim();
				for (Cycle c : cycles) {
					if (c.getName().equals(input)) {
						tour.buyCycle(c);
						break;
					}
				}
			}
			input = getInput();
		}
		
		// Register tracks
		List<Track> tracks = tour.getTracks();
		System.out.println("Register for tracks by reg track-name num-players,"
				+ " e.g. \"reg " + tracks.get(0).getFileNameNoPath() + 
				" 1\". Enter next when done");
		input = getInput();
		while (!input.equals("next")) {
			if (input.contains("reg")) {
				input = input.replace("reg", "");
				input = input.trim();
				String[] temp = input.split(" ");
				for (Track t : tracks) {
					if (t.getFileNameNoPath().equals(temp[0])) {
						int numPlayers = Integer.parseInt(temp[1]);
						tour.registerTrack(t, numPlayers);
						break;
					}
				}
			}
			input = getInput();
		}
		
		while (!tour.isFinished()) { 
			
			// Choose track
			List<Track> unraced = tour.getUnracedTracks();
			while (tour.isPreparing()) {
				System.out.println("Choose track with e.g. \"" + 
						unraced.get(0).getFileNameNoPath() +"\"." );
				input = getInput();
				for (Track t : unraced) {
					if (t.getFileNameNoPath().equals(input)) {
						
						// Choose player cycles
						List<Player> players = new ArrayList<Player>();
						Map<String, GridCell> sp = t.getStartingPositions();
						System.out.println("Enter player cycles, e.g. \"" +
								sp.keySet().iterator().next() + " " +
								tour.getPurchasedCycles().get(0).getName()
								+ "\". Enter \"start\" when done.");
						input = getInput();
						while (!input.equals("start")) {
							String[] temp = input.split(" ");
							for (Cycle c : cycles) {
								if (c.getName().equals(temp[1])) {
									players.add(new Player(temp[0], c, sp.get(temp[0])));
									break;
								}
							}
							input = getInput();
						}
						tour.startRace(t, players);	// tour.isPreparing -> false
						break;
					}
				}
			}
			
			// Race
			System.out.println("Enter actions, e.g. \"FS\". Separate actions "
					+ "for different cycles with a space");
			while (tour.isRacing()) {
				RaceState s = tour.getLatestRaceState();
				Track t = tour.getCurrentTrack();
				System.out.println(RaceSimTools.stateToString(s, t));
				input = getInput();
				String[] temp = input.split(" ");
				List<Action> actions = new ArrayList<Action>();
				for (int i = 0; i < temp.length; i++) {
					actions.add(Action.valueOf(input));
				}
				tour.stepTurn(actions);
			}
		}
		
		// Output results
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
	
	public static String getInput() {
		StringBuilder sb = new StringBuilder();
		char ch;
		try {
			ch = (char) System.in.read();
			while (ch != '\n') {
				sb.append(ch);
				ch = (char) System.in.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString().trim();
	}

}
