package problem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * Represents a race track
 * @author Joshua Song
 *
 */
public class Track {
	
	/**
	 * Enumeration of the possible grid cell types. Distractor means the cell
	 * is a location for the possible occurance of a distractor.
	 */
	public enum CellType {
		EMPTY, OBSTACLE, GOAL
	}
	
	private String fileName;
	private String fileNameNoPath;
	private int numRows;
	private int numCols;
	private int numOpponents;
	private double registrationFee;
	private double prize;
	
	/** The map, which is a matrix of CellTypes */
	private ArrayList<ArrayList<CellType>> map;
	/** Starting locations for the players. Maps string id to GridCell */
	private Map<String, GridCell> playerStarts;
	/** Opponents, set to their starting positions */
	private List<Opponent> opponents;
	/** Distractors, set to their starting positions */
	private List<Distractor> distractors;
	
	public Track(ArrayList<ArrayList<CellType>> map,
			Map<String, GridCell> playerStarts, List<Opponent> opponents,
			List<Distractor> distractors) {
		this.map = map;
		this.playerStarts = playerStarts;
		this.opponents = opponents;
		this.distractors = distractors;
	}

	/**
	 * Load the track from a text file
	 * @param fileName The path of the text file to load.
	 * @throws IOException 
	 */
	public Track(String fileName) throws IOException {
		this.fileName = fileName;
		File file = new File(fileName);
		fileNameNoPath = file.getName();
		System.out.print("Loading " + fileName + "...  ");
		BufferedReader input = new BufferedReader(new FileReader(fileName));
		String line = "";
		int lineNo = 0;
		Scanner s;
		try {
			line = input.readLine();
			lineNo++;
			s = new Scanner(line);
			s.useLocale(Locale.ENGLISH);
			numRows = s.nextInt();
			numCols = s.nextInt();
			numOpponents = s.nextInt();
			registrationFee = s.nextDouble();
			prize = s.nextDouble();
			s.close();

			// Load map and starting positions
			map = new ArrayList<ArrayList<CellType>>();
			playerStarts = new HashMap<String, GridCell>();
			Map<String, GridCell> opponentStarts = new HashMap<String, GridCell>();
			Map<String, ArrayList<GridCell>> distractorStarts = 
					new HashMap<String, ArrayList<GridCell>>();
			for (int row = 0; row < numRows; row++) {
				line = input.readLine();
				lineNo++;
				ArrayList<CellType> mapRow = new ArrayList<CellType>();
				for (int col = 0; col < numCols; col++) {
					GridCell cell = new GridCell(row, col);
					char c = line.charAt(col);
					if (c == '1') {
						mapRow.add(CellType.OBSTACLE);
					} else if (col == numCols - 1) {
						mapRow.add(CellType.GOAL);
					} else {
						mapRow.add(CellType.EMPTY);
					}
					
					if (c >= 'K' && c <= 'Z') {
						playerStarts.put(Character.toString(c), cell);
					} else if (c >= 'A' && c <= 'J') {
						opponentStarts.put(Character.toString(c), cell);
					} else if (Character.isLowerCase(c)) {
						String cs = Character.toString(c);
						if (!distractorStarts.containsKey(cs)) {
							distractorStarts.put(cs, new ArrayList<GridCell>());
						}
						distractorStarts.get(cs).add(cell);
					}
				}
				map.add(mapRow);
			}
			
			// Load opponent policies and create opponents
			opponents = new ArrayList<Opponent>();
			for (int i = 0; i < numOpponents; i++) {
				String opponentId = input.readLine().trim();
				lineNo++;
				RandomPolicy policy = new RandomPolicy();
				for (int row = 0; row < numRows; row++) {
					for (int col = 0; col < numCols; col++) {
						line = input.readLine();
						lineNo++;
						GridCell cell = new GridCell(row, col);
						s = new Scanner(line);
						s.useLocale(Locale.ENGLISH);
						EnumMap<Action, Double> actionMap = 
								new EnumMap<Action, Double>(Action.class);
						actionMap.put(Action.FS, s.nextDouble());
						actionMap.put(Action.FM, s.nextDouble());
						actionMap.put(Action.FF, s.nextDouble());
						actionMap.put(Action.NE, s.nextDouble());
						actionMap.put(Action.SE, s.nextDouble());
						actionMap.put(Action.ST, s.nextDouble());
						policy.put(cell, actionMap);
					}
				}
				opponents.add(new Opponent(opponentId, policy,
						opponentStarts.get(opponentId)));
			}
			
			// Load distractor behaviour and create distractors
			distractors = new ArrayList<Distractor>();
			for (int i = 0; i < distractorStarts.size(); i++) {
				String id = input.readLine().trim();
				lineNo++;
				line = input.readLine();
				lineNo++;
				s = new Scanner(line);
				s.useLocale(Locale.ENGLISH);
				double appearProb = s.nextDouble();
				s.close();
				for (GridCell g : distractorStarts.get(id)) {
					distractors.add(new Distractor(id, appearProb, false, g));	
				}			
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
	}
	
	/**
	 * Returns true if position is within the map
	 * @param pos GridCell position
	 * @return true iff pos is within map bounds
	 */
	public boolean withinBorder(GridCell pos) {
		if (pos.getRow() < 0 || pos.getRow() > numRows - 1 ||
				pos.getCol() < 0 || pos.getCol() > numCols - 1) {
			return false;
		}
		return true;
	}
	
	public CellType getCellType(GridCell pos) {
		return map.get(pos.getRow()).get(pos.getCol());
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumCols() {
		return numCols;
	}
	
	public double getRegistrationFee() {
		return registrationFee;
	}
	
	public double getPrize() {
		return prize;
	}
	
	public int getNumOpponents() {
		return numOpponents;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Returns the name of this track, i.e. the last bit of the file path
	 * @return name of track
	 */
	public String getFileNameNoPath() {
		return fileNameNoPath;
	}

	/**
	 * Returns the name of this track, (no path / extension)
	 * @return name of track
	 */
	public String getName() {
		return getFileNameNoPath().split("\\.(?=[^\\.]+$)")[0];
	}

	/**
	 * Return the list of opponents. Each opponent is set to its starting
	 * position.
	 * @return list of opponnents
	 */
	public List<Opponent> getOpponents() {
		return Collections.unmodifiableList(opponents);
	}
	
	/**
	 * Returns the list of distractors. Each distractor is set to its starting
	 * position, and starts with hasAppeared set to false.
	 * @return list of distractors
	 */
	public List<Distractor> getDistractors() {
		return Collections.unmodifiableList(distractors);
	}
	
	/**
	 * Returns read-only Map of starting locations for the players. 
	 * Maps string id to GridCell
	 * @return
	 */
	public Map<String, GridCell> getStartingPositions() {
		return Collections.unmodifiableMap(playerStarts);
	}
	
	/**
	 * Returns a read-only map
	 * @return The track's map
	 */
	public List<ArrayList<CellType>> getMap() {
		return Collections.unmodifiableList(map);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((distractors == null) ? 0 : distractors.hashCode());
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result
				+ ((fileNameNoPath == null) ? 0 : fileNameNoPath.hashCode());
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		result = prime * result + numCols;
		result = prime * result + numOpponents;
		result = prime * result + numRows;
		result = prime * result
				+ ((opponents == null) ? 0 : opponents.hashCode());
		result = prime * result
				+ ((playerStarts == null) ? 0 : playerStarts.hashCode());
		long temp;
		temp = Double.doubleToLongBits(prize);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(registrationFee);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Track other = (Track) obj;
		if (distractors == null) {
			if (other.distractors != null)
				return false;
		} else if (!distractors.equals(other.distractors))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (fileNameNoPath == null) {
			if (other.fileNameNoPath != null)
				return false;
		} else if (!fileNameNoPath.equals(other.fileNameNoPath))
			return false;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		if (numCols != other.numCols)
			return false;
		if (numOpponents != other.numOpponents)
			return false;
		if (numRows != other.numRows)
			return false;
		if (opponents == null) {
			if (other.opponents != null)
				return false;
		} else if (!opponents.equals(other.opponents))
			return false;
		if (playerStarts == null) {
			if (other.playerStarts != null)
				return false;
		} else if (!playerStarts.equals(other.playerStarts))
			return false;
		if (Double.doubleToLongBits(prize) != Double
				.doubleToLongBits(other.prize))
			return false;
		if (Double.doubleToLongBits(registrationFee) != Double
				.doubleToLongBits(other.registrationFee))
			return false;
		return true;
	}
	
	
}
