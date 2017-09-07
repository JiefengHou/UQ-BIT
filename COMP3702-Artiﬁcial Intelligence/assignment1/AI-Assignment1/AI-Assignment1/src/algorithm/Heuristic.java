package algorithm;


public interface Heuristic {
	/**
	 * Returns an estimate of the cost of reaching the goal state from the given state.
	 * @param s2 the state.
	 * @return an estimate of the cost to the goal.
	 */
	public double estimate(State s2);
}
