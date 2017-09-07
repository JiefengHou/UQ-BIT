package problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

/**
 * Contains the functions used to simulate a race.
 * @author Joshua Song
 *
 */
public class RaceSimTools {
	
	// Damage costs
	public final static double DISTRACTOR_COST_RELIABLE = 10.0;
	public final static double DISTRACTOR_COST_UNRELIABLE = 75.0;
	public final static double OBSTACLE_COST_WILD = 5.0;
	public final static double OBSTACLE_COST_DOMESTICATED = 50.0;
	
	// Probability for successful TO action
	public final static double TO_SUCCESS_PROBABILITY = 0.7;
	
	/**
	 * Checks if a GridCell is within the map borders and does not collide
	 * with an obstacle. If isWild == true, obstacle check is skipped
	 * @param g The GridCell to check
	 * @param track
	 * @param isWild
	 * @return true if g is valid
	 */
	public static boolean isValid(GridCell g, Track track, boolean isWild) {
		if (!withinBounds(g, track)) {
			return false;
		}
		if (!isWild) {
			if (isObstacle(g, track)) {
				return false;
			}
		}
		return true;		
	}
	
	public static boolean isValid(GridCell g, Track track, boolean isWild,
			List<GridCell> extraObstacles) {
		if (!withinBounds(g, track)) {
			return false;
		}
		if (!isWild) {
			if (isObstacle(g, track, extraObstacles)) {
				return false;
			}
		}
		return true;		
	}
	
	
	
	/**
	 * Returns true if a GridCell is within the map borders
	 * @param g The GridCell to check
	 * @param track
	 * @return true if g is within track's borders
	 */
	public static boolean withinBounds(GridCell g, Track track) {
		if (g.getRow() < 0 || g.getRow() > track.getNumRows() - 1 ||
				g.getCol() < 0 || g.getCol() > track.getNumCols() - 1) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns true if a GridCell is an obstacle. Returns false otherwise or if
	 * the GridCell is out of map bounds
	 * @param g The GridCell to check
	 * @param track
	 * @return true if there is an obstacle at g or if g is out of bounds
	 */
	public static boolean isObstacle(GridCell g, Track track) {
		if (!withinBounds(g, track)) {
			return false;
		}
		return track.getCellType(g) == Track.CellType.OBSTACLE;
	}
	
	public static boolean isObstacle(GridCell g, Track track, 
			List<GridCell> extraObstacles) {
		if (!withinBounds(g, track)) {
			return false;
		}
		for (GridCell eo : extraObstacles) {
			if (eo.equals(g)) {
				return true;
			}
		}
		return track.getCellType(g) == Track.CellType.OBSTACLE;
	}
	
	/**
	 * Samples a resulting new state. Does not use function nextStates
	 * as doing so would require unnecessary computation
	 * @param state The current state
	 * @param actions
	 * @param track
	 * @param random
	 * @return
	 */
	public static RaceState sampleNextState(RaceState state, List<Action> actions,
			Track track, Random random) {

		// Check number of actions matches number of player cycles
		if (actions.size() != state.getPlayers().size()) {
			System.out.println("ERROR: Mismatch between number of actions and "
					+ "players.");
			return null;
		}
		
		// Generate next turn's distractor states
		List<Distractor> newDistractors = new ArrayList<Distractor>();
		for (Distractor d : state.getDistractors()) {
			newDistractors.add(sampleNextDistractor(d, random));
		}
		
		// Generate next turn's player states
		boolean adv1Plus = state.getPlayers().size() > 1;
		List<Player> newPlayers = new ArrayList<Player>();
		for (int i = 0; i < actions.size(); i++) {
			Player player = state.getPlayers().get(i);
			newPlayers.add(sampleNextPlayer(player, track,
					newDistractors, actions.get(i), random, adv1Plus));
		}
	
		// Generate next turn's opponent states
		List<Opponent> newOpponents = new ArrayList<Opponent>();
		for (Opponent o : state.getOpponents()) {
			newOpponents.add(sampleNextOpponent(o, newPlayers, track, random));
		}
			
		RaceState.Status status = getStatus(newPlayers, newOpponents, track,
				state.getTurnNo() + 1);
		return new RaceState(newPlayers, newOpponents, newDistractors, status,
				state.getTurnNo() + 1);
	}
	
	/**
	 * Samples a new distractor state. I.e. the distractor is returned with
	 * hasAppeared set randomly to either true or false
	 * @param distractor
	 * @param random
	 * @return new randomly selected distractor state
	 */
	public static Distractor sampleNextDistractor(Distractor distractor,
			Random random) {
		if (random.nextDouble() < distractor.getAppearProbability()) {
			return distractor.getAppeared(true);
		}
		return distractor.getAppeared(false);
	}
	
	/**
	 * Samples a new player state
	 * @param player
	 * @param track
	 * @param distractors List of distractors
	 * @param action
	 * @param random A random number generator
	 * @param adv1Plus If true, TO action is allowed. Should be true iff 
	 * number of cycles in the race > 1.
	 * @return new randomly selected player state
	 */
	public static Player sampleNextPlayer(Player player, Track track,
			List<Distractor> distractors, Action action, Random random, 
			boolean adv1Plus) {
		action = restrictToValidActions(player, action, adv1Plus);

		boolean isWild = player.getCycle().isWild();
		GridCell cell = player.getPosition();

		boolean willBeObstacle = player.isObstacle();
		GridCell nextCell = cell;
		double damageCost = 0;
		if (action == Action.TO) {
			willBeObstacle = (random.nextDouble() <= TO_SUCCESS_PROBABILITY);
		} else if (action == Action.TC) {
			willBeObstacle = false;
		} else if (action == Action.NE || action == Action.SE) {
			double r = random.nextDouble();
			if (r <= 0.7) { // Success
				nextCell = cell.shifted(action == Action.NE ? Direction.NE : Direction.SE);
			} else if (r <= 0.8) { // Fail and go N/S
				nextCell = cell.shifted(action == Action.NE ? Direction.N : Direction.S);
			} else if (r <= 0.9) { // Fail and go East
				nextCell = cell.shifted(Direction.E);
			} else {
				nextCell = cell;
			}
			if (!withinBounds(nextCell, track)) {
				nextCell = cell;
			} else if (isObstacle(nextCell, track)) {
				damageCost += getObstacleDamage(player.getCycle());
				if (!isWild) {
					nextCell = cell;
				}
			}
		} else if (action == Action.ST) {
			nextCell = cell;
		} else {
			// Handle FS, FM, FF
			int maxMoves;
			switch (action) {
			case FS:
				maxMoves = 1;
				break;
			case FM: 
				maxMoves = 2;
				break;
			case FF:
				maxMoves = 3;
				break;
			default:
				maxMoves = 0;
				break;
			}
			GridCell temp = cell;
			for (int i = 0; i < maxMoves; i++) {
				GridCell nextTemp = temp.shifted(Direction.E);
				if (!withinBounds(nextTemp, track)) {
					break;
				}
				if (isObstacle(nextTemp, track)) {
					damageCost += getObstacleDamage(player.getCycle());
					if (!isWild) {
						break;
					}
				}
				temp = nextTemp;
			}
			nextCell = temp;
		}
		if (isDistracted(nextCell, distractors)) {
			damageCost += getDistractorDamage(player.getCycle());
		}
		
		return new Player(player.getId(), player.getCycle(), nextCell, damageCost, willBeObstacle);
	}
	
	/**
	 * Samples a new opponent state
	 * @param opponent
	 * @param players List of players
	 * @param track
	 * @param random A random number generator
	 * @return new randomly selected opponent state
	 */
	public static Opponent sampleNextOpponent(Opponent opponent, 
			List <Player> players, Track track, Random random) {
		// Get extra obstacles due to player cycles being in obstacle mode
		List<GridCell> extraObstacles = new ArrayList<GridCell>();
		for (Player p : players) {
			if (p.isObstacle()) {
				extraObstacles.add(p.getPosition());
			}
		}
		
		GridCell cell = opponent.getPosition();
		Action desiredAction = chooseRandom(opponent.getPolicy().get(cell), random);
		GridCell nextCell = furthestMove(cell, desiredAction, track, extraObstacles, false);
		return new Opponent(opponent.getId(), opponent.getPolicy(), nextCell);
	}
	
	/**
	 * Returns a probability distribution over the next possible states.
	 * Essentially a transition probability function. WARNING: quite untested
	 * @param state The current RaceState
	 * @param actions List of actions (one for each player)
	 * @param track
	 * @param random A random number generator
	 * @return Map<RaceState, Double> where the Double is the probability
	 */
	public static Map<RaceState, Double> nextStates(RaceState state,
			List<Action> actions, Track track, Random random) {

		int numDistractors = state.getDistractors().size();
		int numPlayers = state.getPlayers().size();
		int numOpponents = state.getOpponents().size();
		boolean adv1Plus = numPlayers > 1;
		
		// Check number of actions matches number of player cycles
		if (actions.size() != numPlayers) {
			System.out.println("ERROR: Mismatch between number of actions and "
					+ "players.");
			return null;
		}

		// Build a tree to get all the possible combinations of next states
		Node<Actor> root = new Node<Actor>();
		ArrayList<Node<Actor>> leaves = new ArrayList<Node<Actor>>();
		leaves.add(root);
		ArrayList<Double> leafProb = new ArrayList<Double>();
		leafProb.add(1.0);
		
		// Probability distributions over next distractor states
		for (Distractor d : state.getDistractors()) {
			Map<Distractor, Double> nd = nextDistractors(d);
			ArrayList<Node<Actor>> newLeaves = new ArrayList<Node<Actor>>();
			ArrayList<Double> newLeafProb = new ArrayList<Double>();
			for (int i = 0; i < leaves.size(); i++) {
				Node<Actor> parent = leaves.get(i);
				for (Map.Entry<Distractor, Double> entry : nd.entrySet()) {
					Node<Actor> child = new Node<Actor>(entry.getKey());
					parent.addChild(child);
					newLeaves.add(child);
					newLeafProb.add(leafProb.get(i) * entry.getValue());
				}
			}
			leaves = newLeaves;	
			leafProb = newLeafProb;
		}
		
		// Probability distributions over next player states
		// Not independent to current distractor states
		for (int i = 0; i < numPlayers; i++) {
			Player currentPlayer = state.getPlayers().get(i);
			ArrayList<Node<Actor>> newLeaves = new ArrayList<Node<Actor>>();
			ArrayList<Double> newLeafProb = new ArrayList<Double>();
			for (int j = 0; j < leaves.size(); j++) {
				
				// Get distractor states for this leaf's ancestry
				Node<Actor> parent = leaves.get(j);
				List<Distractor> ancDistractors = new ArrayList<Distractor>();
				List<Actor> ancActors = parent.getDataAncestry();
				ListIterator<Actor> it = ancActors.listIterator();
				it.next();	// Ignore root dummy
				for (int k = 0; k < numDistractors; k++) {
					ancDistractors.add((Distractor) it.next());
				}
				
				// Generate probability distribution over next player states
				Map<Player, Double> np = nextPlayers(currentPlayer,
						actions.get(i), track, ancDistractors, adv1Plus);

				// Add a new child to this leaf for each possible next player state
				for (Map.Entry<Player, Double> entry : np.entrySet()) {
					Node<Actor> child = new Node<Actor>(entry.getKey());
					parent.addChild(child);
					newLeaves.add(child);
					newLeafProb.add(leafProb.get(j) * entry.getValue());
				}
			}
			leaves = newLeaves;			
			leafProb = newLeafProb;
		}
		
		// Probability distributions over next opponent states
		// Not independent to current player states
		for (Opponent o : state.getOpponents()) {
			ArrayList<Node<Actor>> newLeaves = new ArrayList<Node<Actor>>();
			ArrayList<Double> newLeafProb = new ArrayList<Double>();
			for (int i = 0; i < leaves.size(); i++) {
				
				// Get player states for this leaf's ancestry
				Node<Actor> parent = leaves.get(i);
				List<Player> ancPlayers = new ArrayList<Player>();
				List<Actor> ancActors = parent.getDataAncestry();
				ListIterator<Actor> it = ancActors.listIterator(
						1 + numDistractors);
				for (int j = 0; j < numPlayers; j++) {
					ancPlayers.add((Player) it.next());
				}
				Map<GridCell, Double> no = nextOpponentPositions(o, ancPlayers, track);
				for (Map.Entry<GridCell, Double> entry : no.entrySet()) {
					Node<Actor> child = new Node<Actor>(new Opponent(o.getId(), o.getPolicy(), entry.getKey()));
					parent.addChild(child);
					newLeaves.add(child);
					newLeafProb.add(leafProb.get(i) * entry.getValue());
				}
			}
			leaves = newLeaves;	
			leafProb = newLeafProb;
		}
		
		// Construct the result
		Map<RaceState, Double> result = new HashMap<RaceState, Double>();
		for (int i = 0; i < leaves.size(); i++) {
			Node<Actor> leaf = leaves.get(i);
			double probability = leafProb.get(i);
			List<Node<Actor>> ancestry = leaf.getAncestry();
			ListIterator<Node<Actor>> it = ancestry.listIterator();
			it.next();  // Ignore dummy root
			List<Distractor> stateDistractors = new ArrayList<Distractor>();
			List<Player> statePlayers = new ArrayList<Player>();
			List<Opponent> stateOpponents = new ArrayList<Opponent>();
			for (int j = 0; j < numDistractors; j++) {
				Distractor d = (Distractor) it.next().getData();
				stateDistractors.add(d);
			}
			for (int j = 0; j < numPlayers; j++) {
				Player p = (Player) it.next().getData();
				statePlayers.add(p);
			}
			for (int j = 0; j < numOpponents; j++) {
				Opponent o = (Opponent) it.next().getData();
				stateOpponents.add(o);
			}
			
			RaceState.Status status = getStatus(statePlayers, stateOpponents,
					track, state.getTurnNo() + 1);
			RaceState newState = new RaceState(statePlayers, stateOpponents,
					stateDistractors, status, state.getTurnNo() + 1);
			if (result.containsKey(newState)) {
				result.put(newState, result.get(newState) + probability);
			} else {
				result.put(newState, probability);				
			}
		}
		return result;
	}
	
	/**
	 * Returns the probability distribution over possible distractor states for
	 * the next turn
	 * @param distractor
	 * @return Map<Distractor, Double> where the Double is the probability
	 */
	public static Map<Distractor, Double> nextDistractors(Distractor distractor) {
		Map<Distractor, Double> result = new HashMap<Distractor, Double>();
		result.put(distractor.getAppeared(true),
				distractor.getAppearProbability());
		result.put(distractor.getAppeared(false),
				1.0 - distractor.getAppearProbability());	
		if (result.isEmpty()) {
			System.out.println("ERROR: No distractor states for the next turn");
		}
		return result;		
	}
	
	
	/**
	 * Restricts the action to make sure it is a valid action for the current player/track.
	 */
	public static Action restrictToValidActions(Player player, Action action, boolean adv1Plus) {
		// Ensure action is valid for this player's cycle
		Cycle.Speed speed = player.getCycle().getSpeed();
		if (action == Action.FM && speed == Cycle.Speed.SLOW) {
			action = Action.FS;
		} else if (action == Action.FF && speed == Cycle.Speed.MEDIUM) {
			action = Action.FM;
		} else if (action == Action.FF && speed == Cycle.Speed.SLOW) {
			action = Action.FS;
		} else if (player.isObstacle() && action != Action.TC) {
			action = Action.ST;
		} else if (action == Action.TO && !adv1Plus) {
			action = Action.ST;
		}
		return action;
	}
	
	/**
	 * Returns the probability distribution over possible player states for
	 * the next turn
	 * @param player
	 * @param action
	 * @param track
	 * @param distractors
	 * @param adv1Plus If true, TO action is allowed. Should be true iff 
	 * number of cycles in the race > 1.
	 * @return Map<Player, Double> where the Double is the probability
	 */
	public static Map<Player, Double> nextPlayers(Player player, Action action,
			Track track, List<Distractor> distractors, boolean adv1Plus) {
		// Restrict the speed
		action = restrictToValidActions(player, action, adv1Plus);

				
		boolean isWild = player.getCycle().isWild();
		Map<Player, Double> result = new HashMap<Player, Double>();
		
		// Temporary variables: probability, next grid cell, damage cost
		ArrayList<Double> nextProbs = new ArrayList<Double>();
		ArrayList<GridCell> nextCells = new ArrayList<GridCell>();
		ArrayList<Double> nextDamages = new ArrayList<Double>();
		ArrayList<Boolean> nextIsObs = new ArrayList<Boolean>();

		if (action == Action.TO) {
			
			// Turn into obstacle action. Slight chance of failure
			nextProbs.add(TO_SUCCESS_PROBABILITY);
			nextProbs.add(1 - TO_SUCCESS_PROBABILITY);
			nextCells.add(player.getPosition());
			nextCells.add(player.getPosition());
			nextDamages.add(0.0);
			nextDamages.add(0.0);
			nextIsObs.add(true);
			nextIsObs.add(false);
		} else if (action == Action.TC) {
			
			// Turn back into cycle action
			nextProbs.add(1.0);
			nextCells.add(player.getPosition());
			nextDamages.add(0.0);
			nextIsObs.add(false);			
		} else if (action == Action.NE || action == Action.SE) { 
			
			// Error exists for NE and SE moves. 4 possible results.
			nextProbs.add(0.1);
			nextProbs.add(0.1);
			nextProbs.add(0.1);
			nextProbs.add(0.7);
			for (int i = 0; i < 4; i++) {
				nextDamages.add(0.0);
				nextIsObs.add(false);				
			}
			if (action == Action.NE) {
				nextCells.add(player.getPosition().shifted(Direction.N));
				nextCells.add(player.getPosition().shifted(Direction.E));
				nextCells.add(player.getPosition());
				nextCells.add(player.getPosition().shifted(Direction.NE));
			} else if (action == Action.SE) {
				nextCells.add(player.getPosition().shifted(Direction.S));
				nextCells.add(player.getPosition().shifted(Direction.E));
				nextCells.add(player.getPosition());
				nextCells.add(player.getPosition().shifted(Direction.SE));
			}
			for (int i = 0; i < 4; i++) {
				if (!withinBounds(nextCells.get(i), track)) {
					nextCells.set(i, player.getPosition());
				} else if (isObstacle(nextCells.get(i), track)) {
					nextDamages.set(i, getObstacleDamage(player.getCycle()));
					if (!isWild) {
						nextCells.set(i, player.getPosition());
					}
				}
			}
		} else if (action == Action.ST){
			
			// Handle ST action
			nextProbs.add(1.0);
			nextCells.add(player.getPosition());
			nextDamages.add(0.0);
			nextIsObs.add(player.isObstacle());			
		} else {

			// Handle FS, FM, FF
			int maxMoves;
			switch (action) {
			case FS:
				maxMoves = 1;
				break;
			case FM: 
				maxMoves = 2;
				break;
			case FF:
				maxMoves = 3;
				break;
			default:
				maxMoves = 0;
				break;
			}
			double damageCost = 0.0;
			GridCell temp = player.getPosition();
			for (int i = 0; i < maxMoves; i++) {
				GridCell nextTemp = temp.shifted(Direction.E);
				if (!withinBounds(nextTemp, track)) {
					break;
				}
				if (isObstacle(nextTemp, track)) {
					damageCost += getObstacleDamage(player.getCycle());
					if (!isWild) {
						break;
					}
				}
				temp = nextTemp;
			}
			nextProbs.add(1.0);
			nextCells.add(temp);
			nextDamages.add(damageCost);
			nextIsObs.add(false);
		}

		// Damage from distractors
		for (int i = 0; i < nextCells.size(); i++) {
			if (isDistracted(nextCells.get(i), distractors)) {
				double d = nextDamages.get(i);
				nextDamages.set(i, d + getDistractorDamage(player.getCycle()));
			}
		}
		
		// Some results could be the same. Add up those probabilities
		for (int i = 0; i < nextCells.size(); i++) {
			Player p = new Player(player.getId(), player.getCycle(),
					nextCells.get(i), nextDamages.get(i), nextIsObs.get(i));
			if (result.containsKey(p)) {
				result.put(p, result.get(p) + nextProbs.get(i));
			} else {
				result.put(p, nextProbs.get(i));
			}
		}
		
		if (result.isEmpty()) {
			System.out.println("ERROR: No player states for the next turn");
		}
		return result;
	}
	
	/**
	 * Returns the probability distribution over possible opponent cells for
	 * the next turn
	 * @param opponent
	 * @param players List of players, to check if any are in obstacle mode
	 * @param track
	 * @param distractors
	 * @return Map<Opponent, Double> where the Double is the probability
	 */
	public static Map<GridCell, Double> nextOpponentPositions(Opponent opponent,
			List<Player> players, Track track) {

		// Get extra obstacles due to player cycles being in obstacle mode
		List<GridCell> extraObstacles = new ArrayList<GridCell>();
		for (Player p : players) {
			if (p.isObstacle()) {
				extraObstacles.add(p.getPosition());
			}
		}
		
		Map<GridCell, Double> cellDistribution = new HashMap<GridCell, Double>();
		RandomPolicy policy = opponent.getPolicy();
		GridCell currentPos = opponent.getPosition();
		
		// The actionMap is a probability distribution over actions
		Map<Action, Double> actionMap = policy.get(currentPos);
		for (Map.Entry<Action, Double> entry : actionMap.entrySet()) {
			Action action = entry.getKey();
			GridCell g = furthestMove(currentPos, action, track, extraObstacles,
					false);
			Opponent o = new Opponent(opponent.getId(), opponent.getPolicy(), g);
			if (cellDistribution.containsKey(g)) {
				cellDistribution.put(g, cellDistribution.get(g) + entry.getValue());
			} else {
				cellDistribution.put(g, entry.getValue());
			}
		}
		
		if (cellDistribution.isEmpty()) {
			System.out.println("ERROR: No opponent states for the next turn");
		}
		return cellDistribution;
	}
	
	/**
	 * Returns the resulting GridCell from taking an action from a certain
	 * GridCell. No error, so used only for the opponent.
	 * @param currentPos
	 * @param action
	 * @param track
	 * @param distractors
	 * @param extraObstacles List of obstacle grid cells in addition to those
	 * listed in the track's map. Used for player cycles in obstacle mode
	 * @param isWild
	 * @return
	 */
	public static GridCell furthestMove(GridCell currentPos, Action action,
			Track track, List<GridCell> extraObstacles, boolean isWild) {
		
		// If currently on top of an obstacle, can't move
		if (!isValid(currentPos, track, isWild, extraObstacles)) {
			return currentPos;
		}
		
		if (action == Action.NE) {
			GridCell temp = currentPos.shifted(Direction.NE);
			if (isValid(temp, track, isWild, extraObstacles)) {
				return temp;
			}
			return currentPos;
		} else if (action == Action.SE) {
			GridCell temp = currentPos.shifted(Direction.SE);
			if (isValid(temp, track, isWild, extraObstacles)) {
				return temp;
			}
			return currentPos;
		} else {
			int maxMoves;
			switch (action) {
			case FS:
				maxMoves = 1;
				break;
			case FM: 
				maxMoves = 2;
				break;
			case FF:
				maxMoves = 3;
				break;
			default:
				maxMoves = 0;
				break;
			}
			GridCell temp = currentPos;
			for (int i = 0; i < maxMoves; i++) {
				GridCell next = temp.shifted(Direction.E);
				if (!isValid(next, track, isWild, extraObstacles)) {
					break;
				}
				temp = next;
			}
			return temp;
		}
	}
	
	/**
	 * Returns true if a distractor is on GridCell pos and hasAppeared == true
	 * @param pos
	 * @param distractors
	 * @return true if a distractor is on GridCell pos and hasAppeared == true
	 */
	public static boolean isDistracted(GridCell pos, List<Distractor> distractors) {
		for (Distractor d : distractors) {
			if (d.getPosition().equals(pos) && d.hasAppeared()) {
				return true;
			}
		}
		return false;
	}	
	
	/**
	 * Choose a random key from a Map<K, Double> where the probability of 
	 * choosing key K is given by the corresponding Double
	 * @param input Map<K, Double>, where Double is probability of choosing K
	 * @param random Random number generator
	 * @return randomly chosen K. May return null if probabilities do not sum 
	 * to 1
	 */	
	public static <K> K chooseRandom(Map<K, Double> input, Random random) {
		double sum = 0;
		double d = random.nextDouble();
		for (Map.Entry<K, Double> entry : input.entrySet()) {
			sum += entry.getValue();
			if (d <= sum) {
				return entry.getKey();
			}
		}
		return null;		
	}
	
	/**
	 * Returns the RaceState Status
	 * @param players
	 * @param opponents
	 * @param track
	 * @param turnNo
	 * @return RaceState.Status, either WON, LOST, or RACING
	 */
	public static RaceState.Status getStatus(List<Player> players, 
			List<Opponent> opponents, Track track, int turnNo) {
		for (Player p : players) {
			if (track.getCellType(p.getPosition()) == Track.CellType.GOAL) {
				return RaceState.Status.WON;
			}
		}
		for (Opponent o : opponents) {
			if (track.getCellType(o.getPosition()) == Track.CellType.GOAL) {
				return RaceState.Status.LOST;
			}
		}
		
		// If there are no opponents, must win within 2n steps, where
		// n is the number of columns
		if (opponents.size() == 0) {
			if (turnNo > 2 * track.getNumCols()) {
				return RaceState.Status.LOST;
			}
		} else {
			
			// If there are opponents, there is still a 100n time limit
			if (turnNo > 100 * track.getNumCols()) {
				return RaceState.Status.LOST;
			}
		}
		
		return RaceState.Status.RACING;
	}
	
	public static String stateToString(RaceState state, Track track) {
		StringBuilder sb = new StringBuilder();
		String ls = System.getProperty("line.separator");
		HashMap<GridCell, ArrayList<String>> things = 
				new HashMap<GridCell, ArrayList<String>>();
		for (Player a : state.getPlayers()) {
			if (!things.containsKey(a.getPosition())) {
				things.put(a.getPosition(), new ArrayList<String>());
			}
			things.get(a.getPosition()).add(a.getId());
		}
		for (Opponent a : state.getOpponents()) {
			if (!things.containsKey(a.getPosition())) {
				things.put(a.getPosition(), new ArrayList<String>());
			}
			things.get(a.getPosition()).add(a.getId());
		}
		for (Distractor a : state.getDistractors()) {
			if (!a.hasAppeared()) {
				continue;
			}
			if (!things.containsKey(a.getPosition())) {
				things.put(a.getPosition(), new ArrayList<String>());
			}
			things.get(a.getPosition()).add(a.getId());
		}
		for (int row = 0; row < track.getNumRows(); row++) {
			for (int col = 0; col < track.getNumCols(); col++) {
				GridCell g = new GridCell(row, col);
				if (things.containsKey(g)) {
					ArrayList<String> temp = things.get(g);
					if (temp.size() > 1) {
						sb.append("[");
						for (int i = 0 ; i < temp.size() - 1; i++) {
							sb.append(temp.get(i));
							sb.append("-");
						}
						sb.append(temp.get(temp.size() - 1));
						sb.append("]");
					} else {
						sb.append(temp.get(0));
					}
				} else {
					if (track.getCellType(g) == Track.CellType.OBSTACLE){
						sb.append("1");
					} else {
						sb.append("0");
					}
				}
			}
			sb.append(ls);
		}
		return sb.toString();
	}
	
	/**
	 * Returns the monetary damage cost for collision with distractor
	 * @param cycle
	 * @return damage cost
	 */
	public static double getDistractorDamage(Cycle cycle) {
		if (cycle.isReliable()) {
			return DISTRACTOR_COST_RELIABLE;
		} else {
			return DISTRACTOR_COST_UNRELIABLE;
		}
	}
	
	/**
	 * Returns the monetary damage cost for collision with an obstacle
	 * @param cycle
	 * @return damage cost
	 */
	public static double getObstacleDamage(Cycle cycle) {
		if (cycle.isWild()) {
			return OBSTACLE_COST_WILD;
		} else {
			return OBSTACLE_COST_DOMESTICATED;
		}
	}
}
