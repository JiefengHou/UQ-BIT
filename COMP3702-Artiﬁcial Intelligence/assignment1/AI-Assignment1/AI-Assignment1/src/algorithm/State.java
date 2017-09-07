package algorithm;

import java.util.List;


public interface State {
	/**
	 * Returns the successors of this state.
	 * 
	 * @return the successors of this state.
	 */
	public List<State> getSuccessors();

	/**
	 * Returns the cost of moving directly from this state to the given one. The
	 * behaviour is undefined if there is no edge to move along.
	 * 
	 * @param successor
	 *            the successor state
	 * @return the cost of moving directly to the given state.
	 */
	public double getCost(State successor);
}
