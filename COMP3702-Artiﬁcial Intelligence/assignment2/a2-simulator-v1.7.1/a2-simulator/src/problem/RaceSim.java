package problem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * Simulator for a race
 * @author Joshua Song
 *
 */
public class RaceSim {
	
	private Random random;
	private List<RaceState> stateHistory;
	private List<ArrayList<Action>> actionHistory;
	private Track track;
	private double totalDamageCost;
	
	public RaceSim(RaceState startState, Track track, Random random) {
		this.random = random;
		stateHistory = new ArrayList<RaceState>();
		stateHistory.add(startState);
		actionHistory = new ArrayList<ArrayList<Action>>();
		this.track = track;
		totalDamageCost = 0;
	}
	
	public RaceSim(List<RaceState> stateHistory,
			List<ArrayList<Action>> actionHistory, Track track,
			Random random) {
		this.random = random;
		this.stateHistory = stateHistory;
		this.actionHistory = actionHistory;
		this.track = track;
		random = new Random();
		totalDamageCost = 0;
		for (RaceState r : stateHistory) {
			totalDamageCost += r.getTotalDamageCost();
		}
	}
	
	/**
	 * Samples a new RaceState and adds it to the stateHistory
	 * @param actions
	 */
	public void stepTurn(List<Action> actions) {
		if (getCurrentStatus() != RaceState.Status.RACING) {
			System.out.println("ERROR: Cannot step as race is over.");
		}
		stateHistory.add(RaceSimTools.sampleNextState(
				getCurrentState(), actions, track, random));
		ArrayList<Action> copy = new ArrayList<Action>();
		copy.addAll(actions);
		actionHistory.add(copy);
		for (Player p : getCurrentState().getPlayers()) {
			totalDamageCost += p.getDamageCost();
		}
	}
	
	/**
	 * Returns the current RaceState
	 * @return current RaceState
	 */
	public RaceState getCurrentState() {
		return stateHistory.get(stateHistory.size() - 1);
	}
	
	/**
	 * Returns the status of the current race state, RACING, WON or LOST
	 * @return RaceState.Status of current RaceState
	 */
	public RaceState.Status getCurrentStatus() {
		return getCurrentState().getStatus();
	}
	
	/**
	 * Returns the current turn number (number of states in stateHistory)
	 * @return turn number
	 */
	public int getTurnNo() {
		return stateHistory.size();
	}
	
	/**
	 * Returns read-only stateHistory of states
	 * @return this stateHistory (list of RaceState)
	 */
	public List<RaceState> getStateHistory() {
		return Collections.unmodifiableList(stateHistory);
	}
	
	public List<ArrayList<Action>> getActionHistory() {
		return Collections.unmodifiableList(actionHistory);
	}
	
	/**
	 * Returns true if the race has finished
	 * @return true if the race has finished
	 */
	public boolean isFinished() {
		return getCurrentStatus() != RaceState.Status.RACING;
	}
	
	/**
	 * Returns the track currently in use by the sim
	 * @return current Track
	 */
	public Track getTrack() {
		return track;
	}
	
	/**
	 * Returns the damage costs received so far
	 * @return damage cost
	 */
	public double getTotalDamageCost() {
		return totalDamageCost;
	}
}
