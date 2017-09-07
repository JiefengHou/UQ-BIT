package problem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Use this class to load the cycle and track information from file
 * @author Joshua Song
 *
 */
public class Setup {
	private List<Cycle> cycles;
	private List<Track> tracks;
	private double startupMoney;
	private String cycleFile;
	private String metaTrackFile;
	private String cycleFileNoPath;
	private String metaTrackFileNoPath;
	
	public Setup(List<Cycle> cycles, List<Track> tracks, double startupMoney) {
		this.cycles = cycles;
		this.tracks = tracks;
		this.startupMoney = startupMoney;
		cycleFile = "None";
		metaTrackFile = "None";
		cycleFileNoPath = "None";
		metaTrackFileNoPath = "None";
	}
	
	public Setup(String cycleFile, String metaTrackFile) {
		this.cycleFile = cycleFile;
		this.metaTrackFile = metaTrackFile;
		try {
			cycles = loadCycles(cycleFile);
			tracks = loadTracks(metaTrackFile);
			
			// Get file names excluding the path to get there
			File f = new File(cycleFile);
			cycleFileNoPath = f.getName();
			f = new File(metaTrackFile);
			metaTrackFileNoPath = f.getName();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<Cycle> getCycles() {
		return cycles;
	}
	
	public List<Track> getTracks() {
		return tracks;
	}
	
	public double getStartupMoney() {
		return startupMoney;
	}
	
	public String getCycleFileNoPath() {
		return cycleFileNoPath;
	}
	
	public String getMetaTrackFileNoPath() {
		return metaTrackFileNoPath;
	}
	
	public String getCycleFile() {
		return cycleFile;
	}
	
	public String getMetaTrackFile() {
		return metaTrackFile;
	}
	
	private List<Cycle> loadCycles(String filename) throws IOException {
		System.out.print("Loading " + filename + "...  ");
		List<Cycle> result = new ArrayList<Cycle>();
		BufferedReader input = new BufferedReader(new FileReader(filename));
		String line;
		int lineNo = 0;
		Scanner s;
		try {
			line = input.readLine();
			lineNo++;
			s = new Scanner(line);
			int numCycles = s.nextInt();
			s.close();

			for (int i = 0; i < numCycles; i++) {
				line = input.readLine();
				lineNo++;
				String name;
				Cycle.Speed speed = null;
				boolean reliable;
				boolean wild;
				double price;
				s = new Scanner(line);
				name = s.next();
				String temp = s.next();
				if (temp.equals("Slow")) {
					speed = Cycle.Speed.SLOW;
				} else if (temp.equals("Medium")) {
					speed = Cycle.Speed.MEDIUM;
				} else if (temp.equals("Fast")) {
					speed = Cycle.Speed.FAST;
				}
				reliable = s.next().equals("Reliable");
				wild = s.next().equals("Wild");
				price = s.nextDouble();
				result.add(new Cycle(name, speed, reliable, wild, price));
			}
		} catch (InputMismatchException e) {
			throw new IOException(String.format(
					"Invalid number format on line %d: %s", lineNo,
					e.getMessage()));
		} catch (NoSuchElementException e) {
			throw new IOException(String.format("Not enough tokens on line %d",
					lineNo));
		} catch (NullPointerException e) {
			throw new IOException(String.format(
					"Line %d expected, but file ended.", lineNo));
		} finally {
			input.close();
		}
		System.out.println("Done");
		return result;
	}
	
	private List<Track> loadTracks(String filename) throws IOException {
		System.out.println("Loading " + filename + "... ... ...");
		List<Track> result = new ArrayList<Track>();
		File meta = new File(filename);
		BufferedReader input = new BufferedReader(new FileReader(meta));
		String line;
		int lineNo = 0;
		Scanner s;
		try {
			line = input.readLine();
			lineNo++;
			s = new Scanner(line);
			int numTracks = s.nextInt();
			startupMoney = s.nextDouble();
			s.close();

			for (int i = 0; i < numTracks; i++) {
				line = input.readLine();
				lineNo++;
				String path = meta.getParent() +
						System.getProperty("file.separator") + line;
				result.add(new Track(path));
			}
		} catch (InputMismatchException e) {
			throw new IOException(String.format(
					"Invalid number format on line %d: %s", lineNo,
					e.getMessage()));
		} catch (NoSuchElementException e) {
			throw new IOException(String.format("Not enough tokens on line %d",
					lineNo));
		} catch (NullPointerException e) {
			throw new IOException(String.format(
					"Line %d expected, but file ended.", lineNo));
		} finally {
			input.close();
		}
		System.out.println("Meta-track loading complete!");
		return result;
	}
}
