package problem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * A class for managing tours
 * @author Joshua Song
 *
 */
public class Tour {
	
	public enum Status {
		PREPARING, RACING, FINISHED
	}
	
	private Setup setup;
	/** The current race number. */
	private int raceNo;
	/** Maximum number of races in the tour */
	private int maxRaces;
	
	/** The current amount of money */
	private double money;
	/** List of cycles purchased */
	private ArrayList<Cycle> purchasedCycles;
	/** Tracks registered, and number of players registered for it */
	private Map<Track, Integer> registeredTracks;
	/** List of RaceSims. For runnning simulations and for recording history */
	private List<RaceSim> raceSims;
	/** History of tracks raced */
	private List<Track> trackHistory;
	/** Time spent preparing on each race (milliseconds) */
	private List<Long> prepareTimes;
	/** Time spent on each race (milliseconds) */
	private List<Long> raceTimes;
	private Long timeStamp;
	
	private Random random;

	/** 
	 * The current status. Status is PREPARING before each race, RACING during
	 * the race, and FINISHED after the final race.
	 */
	private Status status;
	
	/**
	 * Construct a tour from a setup
	 * @param setup
	 */
	public Tour(Setup setup) {
		this.setup = setup;
		raceNo = 0;
		maxRaces = 3;
		money = setup.getStartupMoney();
		purchasedCycles = new ArrayList<Cycle>();
		registeredTracks = new HashMap<Track, Integer>();
		status = Status.PREPARING;
		raceSims = new ArrayList<RaceSim>();
		trackHistory = new ArrayList<Track>();
		prepareTimes = new ArrayList<Long>();
		raceTimes = new ArrayList<Long>();
		timeStamp = System.currentTimeMillis();
		System.out.printf("\nTour created. Starting money: $%.2f\n", money);
		random = new Random();
	}
	
	/**
	 * Finish the preparation phase and complete the Tour without entering
	 * any races at all
	 */
	public void skipTour() {
		if (status != Status.PREPARING) {
			System.out.println("ERROR: You can't skip the tour after already starting a race!");
			return;
		}
		System.out.println("Skipping the Tour entirely!");
		status = Status.FINISHED;
		finishTour();
	}
	
	/**
	 * Wraps up the Tour upon completion.
	 */
	private void finishTour() {
		System.out.println();
		System.out.println("Tour completed!");
		System.out.printf("Profit: $%.2f\n", money - setup.getStartupMoney());
	}
	
	/**
	 * Get a read-only list of cycles that can be purchased
	 * @return list of cycles that can be purchased
	 */
	public List<Cycle> getPurchasableCycles() {
		List<Cycle> purchasable = new ArrayList<Cycle>();
		for (Cycle c : setup.getCycles()) {
			if (c.getPrice() <= money) {
				purchasable.add(c);
			}
		}
		return Collections.unmodifiableList(purchasable);
	}
	
	/**
	 * Buy a cycle. Can only be done before any race.
	 * @param cycle The cycle to buy
	 * @return true iff success
	 */
	public boolean buyCycle(Cycle cycle) {
		if (raceNo != 0 || status != Status.PREPARING) {
			System.out.println("Error: You can only buy cycles at the start of the tour");
			return false;
		} else if (cycle.getPrice() > money) {
			System.out.println("Error: Insufficient money to buy this cycle");
			return false;
		} else if (!getPurchasableCycles().contains(cycle)) {
			System.out.println("Error: Cycle is not available for purchase");
			return false;
		}
		purchasedCycles.add(cycle);
		money -= cycle.getPrice();
		System.out.printf("Successfully purchased %s.  $%.2f remaining\n", cycle.getName(), money); 
		return true;
	}
	
	/**
	 * Register for a track. Can only be done before any race.
	 * @param track The track to register for. 
	 * @param numPlayers The number of players to register for
	 * @return true iff success
	 */
	public boolean registerTrack(Track track, int numPlayers) {
		if (raceNo != 0 || status != Status.PREPARING) {
			System.out.println("Error: You can only register at the start of the tour");
			return false;
		} else if (!setup.getTracks().contains(track)) {
			System.out.println("Error: Invalid track");
			return false;
		} else if (registeredTracks.size() >= maxRaces) {
			System.out.println("Error: Already registered for max number of races");
			return false;
		} else if (registeredTracks.containsKey(track)) {
			System.out.println("Error: Already registered this track.");
			return false;
		}
		int maxPlayers = track.getStartingPositions().size();
		if (numPlayers < 1 || numPlayers > maxPlayers) {
			System.out.println("Error: Invalid number of players for registration");
			return false;
		}
		double cost = track.getRegistrationFee() * numPlayers;
		if (cost > money) {
			System.out.println("Error: Insufficient money to register");
			return false;
		}
		
		registeredTracks.put(track, numPlayers);
		money -= cost;
		System.out.printf("Successfully registered for %d player(s) in track %s.  $%.2f remaining.\n",
				numPlayers, track.getFileNameNoPath(), money); 
		return true;
	}
	
	public List<Cycle> getPurchasedCycles() {
		return Collections.unmodifiableList(purchasedCycles);
	}
	
	public double getCurrentMoney() {
		return money;
	}
	
	/** 
	 * Returns list of tracks that can be registered for.
	 * @return List<Track>
	 */
	public List<Track> getTracks() {
		return Collections.unmodifiableList(setup.getTracks());
	}
	
	/**
	 * Returns list of tracks that have been registered for but not raced on
	 * @return List<Track>
	 */
	public List<Track> getUnracedTracks() {
		List<Track> unraced = new ArrayList<Track>();
		for (Track t : registeredTracks.keySet()) {
			if (!trackHistory.contains(t)) {
				unraced.add(t);
			}
		}
		return unraced;
	}
	
	/**
	 * Returns registered tracks and number of players registered for each track
	 * @return Map<Track, Integer>, where the integer is the number of players
	 * registered for the track
	 */
	public Map<Track, Integer> getRegisteredTracks() {
		return Collections.unmodifiableMap(registeredTracks);
	}
	
	/**
	 * Start a race
	 * @param track The chosen track. Must be registered
	 * @param players The players, with the cycles and start positions set
	 * @return true iff race was started successfully
	 */
	public boolean startRace(Track track, List<Player> players) {
		if (status != Status.PREPARING) {
			System.out.println("Error: Not in PREPARING state.");
			return false;
		} else if (!registeredTracks.containsKey(track)) {
			System.out.println("Error: Track not registered.");
			return false;
		} else if (trackHistory.contains(track)) {
			System.out.println("Error: This track has already been raced.");
			return false;
		} else if (players.isEmpty()) {
			System.out.println("Error: Need at least one player");
			return false;
		} else if (players.size() > registeredTracks.get(track)) {
			System.out.println("Error: Too many players to start");
			return false;
		}

		@SuppressWarnings("unchecked")
		ArrayList<Cycle> temp = (ArrayList<Cycle>) purchasedCycles.clone();
		ArrayList<String> ids = new ArrayList<String>();
		for (Player p : players) {

			// Ensure that no two players with the same id exists.
			if (ids.contains(p.getId())) {
				System.out.println("Error: Multiple players with same id.");
				return false;
			} else {
				ids.add(p.getId());
			}
			
			// Ensure starting positions is set correctly w.r.t. id
			if (!p.getPosition().equals(
					track.getStartingPositions().get(p.getId()))) {
				System.out.println("Error: Invalid starting position or id.");
				return false;
			}
			
			// Ensure the cycle has been purchased
			if (!temp.contains(p.getCycle())) {
				System.out.println("Error: Cycle not available.");
				return false;
			} else {
				temp.remove(p.getCycle());
			}
			
			// damageCost must be set to 0
			if (p.getDamageCost() != 0) {
				System.out.println("Error: Damage cost must be 0");
				return false;
			}
			
			// Cannot start in obstacle mode
			if (p.isObstacle()) {
				System.out.println("Error: Cannot start in obstacle mode");
				return false;
			}
		}
		
		// Create initial RaceState
		RaceState startState = new RaceState(players, track.getOpponents(),
				track.getDistractors());
		
		// Create a new RaceSim for this race
		raceSims.add(new RaceSim(startState, track, random));
		
		raceNo++;
		trackHistory.add(track);
		status = Status.RACING;
		System.out.println("Started race on track " + track.getFileNameNoPath());
		
		// Calculate time taken to prepare
		prepareTimes.add(System.currentTimeMillis() - timeStamp);
		timeStamp = System.currentTimeMillis();
		
		return true;
	}
	
	public boolean isPreparing() {
		return status == Status.PREPARING;
	}
	
	public boolean isRacing() {
		return status == Status.RACING;
	}
	
	public boolean isFinished() {
		return status == Status.FINISHED;
	}
	
	/**
	 * Step a turn in the current race.
	 * @param actions List of actions. Size must match number of players.
	 */
	public void stepTurn(List<Action> actions) {
		if (!isRacing()) {
			System.out.println("Error: Not currently racing.");
			return;
		}
		RaceSim currentSim = raceSims.get(raceSims.size() - 1);
		currentSim.stepTurn(actions);
		if (currentSim.isFinished()) {
			if (raceNo >= registeredTracks.size()) {
				status = Status.FINISHED;
			} else {
				status = Status.PREPARING;
			}
			money -= currentSim.getTotalDamageCost();
			if (currentSim.getCurrentStatus() == RaceState.Status.WON) {
				money += currentSim.getTrack().getPrize();
				System.out.println("Race finished. You won!");
				System.out.printf("Damage costs: $%.2f  Prize money: $%.2f  Current money: $%.2f\n",
						currentSim.getTotalDamageCost(), currentSim.getTrack().getPrize(), money);
			} else {
				System.out.println("Race finished. You lost.");
				System.out.printf("Damage costs: $%.2f  Current money: $%.2f\n", currentSim.getTotalDamageCost(), money);
			}
			
			// Calculate time taken for race
			raceTimes.add(System.currentTimeMillis() - timeStamp);
			timeStamp = System.currentTimeMillis();			
		}
		if (isFinished()) {
			finishTour();
		}
	}
	
	/**
	 * Return the latest race state. Returns null if no race state exists 
	 * in history.
	 * @return latest RaceState. 
	 */
	public RaceState getLatestRaceState() {
		if (raceSims.isEmpty()) {
			System.out.println("Error: no race in history.");
			return null;
		}
		return getCurrentSim().getCurrentState();
	}
	
	/**
	 * Returns the RaceState history for a RaceSim. 
	 * @param raceIndex index of RaceSim to get history from.
	 * Should be < getNumRaces()
	 * @return List<RaceState>, the state history. Null if error occurred
	 */
	public List<RaceState> getStateHistory(int raceIndex) {
		if (raceIndex < 0 || raceIndex > raceSims.size()) {
			System.out.println("Error: cannot get history; race does not exist");
			return null;
		}
		return raceSims.get(raceIndex).getStateHistory();
	}
	
	/**
	 * Returns the action history for a RaceSim
	 * @param raceIndex index of RaceSim to get history from.
	 * Should be < getNumRaces()
	 * @return List<ArrayList<Action>>. 
	 */
	public List<ArrayList<Action>> getActionHistory(int raceIndex) {
		if (raceIndex < 0 || raceIndex > raceSims.size()) {
			System.out.println("Error: cannot get history; race does not exist");
			return null;
		}
		return raceSims.get(raceIndex).getActionHistory();
	}
	
	public double getRaceDamageCost(int raceIndex) {
		if (raceIndex < 0 || raceIndex > raceSims.size()) {
			System.out.println("Error: cannot get damage; race does not exist");
			return 0.0;
		}
		return raceSims.get(raceIndex).getTotalDamageCost();
	}
	
	/**
	 * @return double Total money spent on repairs
	 */
	public double getTotalDamageCost() {
		double sum = 0;
		for (RaceSim r : raceSims) {
			sum += r.getTotalDamageCost();
		}
		return sum;
	}
	
	/**
	 * @return double Total money spent on purchasing cycles
	 */
	public double getTotalCycleCost() {
		double sum = 0;
		for (Cycle c : purchasedCycles) {
			sum += c.getPrice();
		}
		return sum;
	}

	/**
	 * @return double Total money spent on registration for tracks
	 */
	public double getTotalRegistrationCost() {
		double sum = 0;
		for (Entry<Track, Integer> entry : registeredTracks.entrySet()) {
			sum += entry.getKey().getRegistrationFee() * entry.getValue();
		}
		return sum;
	}
	
	/**
	 * Returns the track for a RaceSim
	 * @param raceIndex index of RaceSim to get track from.
	 * @return Track
	 */
	public Track getTrack(int raceIndex) {
		if (raceIndex < 0 || raceIndex >= raceSims.size()) {
			System.out.println("Error: cannot get track; race does not exist");
			return null;
		}
		return raceSims.get(raceIndex).getTrack();
	}
	
	public Track getCurrentTrack() {
		if (!isRacing()) {
			System.out.println("Error: no race in progress.");
			return null;
		}
		return getCurrentSim().getTrack();
	}
	
	/**
	 * Returns the processing time in milliseconds taken to prepare for a race
	 * that has started or already ended.
	 * @param raceIndex Index of race
	 * @return race prepare time in milliseconds. Returns -1 if race does not
	 * exist
	 */
	public long getPrepareTime(int raceIndex) {
		if (prepareTimes.size() <= raceIndex) {
			System.out.println("Error: can't get time, race does not exist");
			return -1;
		} else {
			return prepareTimes.get(raceIndex);
		}
	}
	
	/**
	 * Returns the total processing time in milliseconds taken to run a race
	 * @param raceIndex Index of race
	 * @return race prepare time in milliseconds. Returns -1 if race does not
	 * exist
	 */
	public long getRaceTime(int raceIndex) {
		if (raceTimes.size() <= raceIndex) {
			System.out.println("Error: can't get time, race does not exist");
			return -1;
		} else {
			return raceTimes.get(raceIndex);
		}
	}
	
	/**
	 * Returns the maximum number of races allowed
	 * @return maximum number of races allowed
	 */
	public int getMaxRaces() {
		return maxRaces;
	}
	
	/**
	 * Returns the number of races completed or in progress
	 * @return the number of races completed or in progress
	 */
	public int getNumRaces() {
		return raceSims.size();
	}
	
	/**
	 * Returns the number of turns in a race
	 * @param raceIndex
	 * @return the number of turns in a race. -1 if race does not exist
	 */
	public int getTurnNo(int raceIndex) {
		if (raceSims.size() <= raceIndex) {
			System.out.println("Error: can't get no. of turns, race doesn't exist");
			return -1;			
		} else {
			return raceSims.get(raceIndex).getTurnNo();
		}
	}
	
	/**
	 * Outputs the tour data to file in the format described in the task sheet
	 * @param filename Name of file to save to
	 * @throws IOException 
	 */
	public void outputToFile(String filename) throws IOException {
		String ls = System.getProperty("line.separator");
		FileWriter output = new FileWriter(filename);
		output.write(setup.getCycleFileNoPath() + " " +
				setup.getMetaTrackFileNoPath() + ls);
		
		// Purchased cycles
		StringBuilder sb = new StringBuilder();
		for (Cycle c : purchasedCycles) {
			sb.append(c.getName() + " ");
		}
		sb.append(ls);
		
		// Registered tracks
		for (Map.Entry<Track, Integer> entry : registeredTracks.entrySet()) {
			sb.append(entry.getKey().getFileNameNoPath() + " " +
					entry.getValue() + " ");
		}
		output.write(sb.toString().trim() + ls);
		
		// Write races
		for (RaceSim sim : raceSims) {
			List<RaceState> history = sim.getStateHistory();
			
			// Write track name, number of states, player ids and cycles
			sb = new StringBuilder();
			sb.append(sim.getTrack().getFileNameNoPath() + " " + history.size()
					+ " ");
			for (Player p : sim.getStateHistory().get(0).getPlayers()) {
				sb.append(p.getId() + " " + p.getCycle().getName());
			}
			sb.append(ls);
			output.write(sb.toString());
			
			// Write each state and action
			for (int i = 0; i < history.size(); i++) {
				RaceState state = history.get(i);
				output.write(RaceSimTools.stateToString(state, sim.getTrack()));
				
				// Write player damage, isObstacle, actions
				List<Player> players = state.getPlayers();
				if (i < history.size() - 1) {
					ArrayList<Action> actions = sim.getActionHistory().get(i);
					for (int j = 0; j < players.size(); j++) {
						output.write(players.get(j).getId() + "-" +
								players.get(j).getDamageCost() + "-" +
								players.get(j).isObstacle() + "-" +
								actions.get(j).toString() + " ");
					}
				} else if (i == history.size() - 1) {
					for (int j = 0; j < players.size(); j++) {
						output.write(players.get(j).getId() + "-" +
								players.get(j).getDamageCost() + "-" +
								players.get(j).isObstacle() + " ");
					}
				}
				output.write(ls);
			}
			
			// Write money gained/lost from this race
			double moneyChange = sim.getTrack().getRegistrationFee() *
					sim.getCurrentState().getPlayers().size();
			if (sim.getCurrentStatus() == RaceState.Status.WON) {
				moneyChange += sim.getTrack().getPrize();
			}
			output.write(Double.toString(moneyChange));
			output.write(ls);
		}
		
		// Write money at end of tour
		output.write(Double.toString(money));
		output.close();
	}
	
	public boolean loadFromFile(String filename) throws IOException {
		System.out.println("Loading " + filename);
		BufferedReader input = new BufferedReader(new FileReader(filename));
		String line;
		int lineNo = 0;
		Scanner s;
		NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
		try {
			
			// Cycle and meta track file
			line = input.readLine().trim();
			lineNo++;
			s = new Scanner(line);
			s.useLocale(Locale.ENGLISH);
			String cycleFile = s.next();
			String metaTrackFile = s.next();
			if (!cycleFile.equals(setup.getCycleFileNoPath())) {
				System.out.println("Error: cycle file mismatch");
				return false;
			}
			if (!metaTrackFile.equals(setup.getMetaTrackFileNoPath())) {
				System.out.println("Error: meta track file mismatch");
				return false;
			}
			s.close();
			
			// Purchased cycles
			purchasedCycles.clear();
			line = input.readLine().trim();
			lineNo++;
			s = new Scanner(line);
			s.useLocale(Locale.ENGLISH);
			while (s.hasNext()) {
				String cycleName = s.next();
				for (Cycle c : setup.getCycles()) {
					if (c.getName().equals(cycleName)) {
						purchasedCycles.add(c);
						break;
					}
				}
			}
			s.close();
			
			// Registered tracks
			registeredTracks.clear();
			line = input.readLine().trim();
			lineNo++;
			s = new Scanner(line);
			s.useLocale(Locale.ENGLISH);
			while (s.hasNext()) {
				String trackName = s.next();
				int numPlayers = s.nextInt();
				for (Track t : setup.getTracks()) {
					if (t.getFileNameNoPath().equals(trackName)) {
						registeredTracks.put(t, numPlayers);
						break;
					}
				}
			}
			s.close();
			
			// Race history
			trackHistory.clear();
			raceSims.clear();
			status = Status.FINISHED;
			raceNo = 0;
			while (true) {
				line = input.readLine().trim();
				lineNo++;
				
				// Track 
				String[] temp = line.split(" ");
				String trackName = temp[0];
				Track track = null;
				for (Track t : setup.getTracks()) {
					if (t.getFileNameNoPath().equals(trackName)) {
						track = t;
						break;
					}
				}
				if (track == null) {
					
					// Money at end of tour
					Number number = format.parse(line);
					money = number.doubleValue();
					status = Status.FINISHED;
					break;
				}
				
				// Number of states and player cycles
				int numStates = Integer.parseInt(temp[1]);
				Map<String, Cycle> playerCycles = new HashMap<String, Cycle>();
				for (int i = 2; i < temp.length; i += 2) {
					String id = temp[i];
					for (Cycle c : setup.getCycles()) {
						if (c.getName().equals(temp[i + 1])) {
							playerCycles.put(id, c);
							break;
						}
					}
				}
				
				// Race states. First put track opponent policies into hashmap
				// with id as key for easy access
				HashMap<Character, RandomPolicy> policies = 
						new HashMap<Character, RandomPolicy>();
				for (Opponent o : track.getOpponents()) {
					policies.put(o.getId().charAt(0), o.getPolicy());
				}
				List<RaceState> stateHistory = new ArrayList<RaceState>();
				List<ArrayList<Action>> actionHistory = 
						new ArrayList<ArrayList<Action>>();
				for (int turnNo = 0; turnNo < numStates; turnNo++) {
					List<Opponent> opponents = new ArrayList<Opponent>();
					List<Distractor> distractors = new ArrayList<Distractor>();
					Map<String, GridCell> playerPos = 
							new HashMap<String, GridCell>();
					for (int row = 0; row < track.getNumRows(); row++) {
						line = input.readLine().trim();
						lineNo++;
						int col = 0;
						char[] chars = line.toCharArray();
						boolean inBracket = false;
						for (int j = 0; j < chars.length; j++) {
							char c = chars[j];
							GridCell cell = new GridCell(row, col);
							if (c == '[') {
								inBracket = true;
							} else if (c == ']') {
								inBracket = false;
							} else if (c >= 'A' && c <= 'J') {
								opponents.add(new Opponent(String.valueOf(c),
										policies.get(c), cell));
							} else if (c >= 'K' && c <= 'Z') {
								playerPos.put(String.valueOf(c), cell);										
							}
							for (Distractor d : track.getDistractors()) {
								if (c == d.getId().charAt(0) && 
										cell.equals(d.getPosition())) {
									distractors.add(d.getAppeared(true));
								}
							}
							
							if (!inBracket) {
								for (Distractor d : track.getDistractors()) {
									boolean alreadyAdded = false;
									for (Distractor d2 : distractors) {
										if (d2.getId().equals(d.getId()) &&
												d2.getPosition().equals(d.getPosition())) {
											alreadyAdded = true;
											break;
										}
									}
									if (!alreadyAdded) {
										distractors.add(d.getAppeared(false));
									}
								}
								col++;
							}
						}
					}
					
					// Player damage, isObstacle, action
					List<Player> players = new ArrayList<Player>();
					ArrayList<Action> actions = new ArrayList<Action>();
					line = input.readLine().trim();
					lineNo++;
					String[] temp2 = line.split(" ");
					for (String s2 : temp2) {
						String[] temp3 = s2.split("-");
						String id = temp3[0];
						Number number = format.parse(temp3[1]);
						double damage = number.doubleValue();
						boolean isObstacle = Boolean.parseBoolean(temp3[2]);
						Cycle cycle = playerCycles.get(id);
						GridCell pos = playerPos.get(id);
						players.add(new Player(id, cycle, pos, damage, 
								isObstacle));
						if (temp3.length >= 4) {
							Action action = Action.valueOf(temp3[3]);
							actions.add(action);
						}
						
					}
					
					// Record to state and action history
					RaceState.Status status = RaceSimTools.getStatus(players,
							opponents, track, turnNo);
					stateHistory.add(new RaceState(players, opponents,
							distractors, status, turnNo));
					if (!actions.isEmpty()) {
						actionHistory.add(actions);
					}
				}
				
				// Add the race
				raceSims.add(new RaceSim(stateHistory, actionHistory, track, random));
				
				// Next line is unnecessary
				line = input.readLine();
				lineNo++;
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
		} catch (ParseException e) {
			throw new IOException(String.format(
					"Double parsing error on line %d .", lineNo));
		} finally {
			input.close();
		}
		
		System.out.println("Done");
		return true;
		
	}
	
	private RaceSim getCurrentSim() {
		return raceSims.get(raceSims.size() - 1);
	}
	
	

}
