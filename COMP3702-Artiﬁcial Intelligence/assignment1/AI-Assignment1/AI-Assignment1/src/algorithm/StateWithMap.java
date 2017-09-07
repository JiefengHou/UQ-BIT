package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class StateWithMap implements State {
	/** A mapping holding the successors and their costs */
	Map<StateWithMap, Double> succMap;

	/**
	 * Constructor; initialises the mapping.
	 */
	public StateWithMap() {
		succMap = new HashMap<StateWithMap, Double>();
	}

	/**
	 * Adds the given state as a successor of the current state, with the given
	 * cost.
	 * 
	 * @param succ
	 *            the successor state.
	 * @param cost
	 *            the edge cost to the successor state.
	 */
	public void addSuccessor(StateWithMap succ, double cost) {
		this.succMap.put(succ, cost);
	}

	@Override
	public List<State> getSuccessors() {
		return new ArrayList<State>(succMap.keySet());
	}

	@Override
	public double getCost(State successor) {
		return succMap.get(successor);
	}
}
