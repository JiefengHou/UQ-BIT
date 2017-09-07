package algorithm;

import java.util.List;

import problem.ASVConfig;
import algorithm.Heuristic;
import algorithm.ZeroHeuristic;


public abstract class AbstractSearchAlgorithm {
	/** The root state of the search. */
	private ASVConfig root;
	/**  The goal state of the search. */
	private ASVConfig goal;
	/** A heuristic; estimates cost to reach the goal (if appropriate). */
	private Heuristic heuristic;
	
	/**
	 * Constructor; stores the inputs for the search.
	 * @param root the root state of the search.
	 * @param goal the goal state of the search.
	 * @param heuristic a heuristic estimate function.
	 */
	public AbstractSearchAlgorithm(ASVConfig root, 
			ASVConfig goal, Heuristic heuristic) {
		this.root = root;
		this.goal = goal;
		this.heuristic = heuristic;
	}
	
	/**
	 * Constructor with no heuristic; stores the inputs, and uses an
	 * always-zero heuristic as an equivalent to no heuristic at all.
	 * @param root the root state of the search.
	 * @param goal the goal state of the search.
	 * @param sf a successor function that returns the successors of a state.
	 */
	public AbstractSearchAlgorithm(ASVConfig root, ASVConfig goal) {
		this(root, goal, new ZeroHeuristic());
	}
	
	/**
	 * Returns the root state of the search.
	 * @return the root state of the search.
	 */
	public ASVConfig getRoot() {
		return root;
	}
	
	/**
	 * Returns the goal state of the search.
	 * @return the goal state of the search.
	 */
	public ASVConfig getGoal() {
		return goal;
	}
	
	/**
	 * Returns the current heuristic function.
	 * @return the current heuristic function.
	 */
	public Heuristic getHeuristic() {
		return heuristic;
	}
	
	/**
	 * The core method of the search; this should run whatever search
	 * algorithm is used until it concludes.
	 * The results of the search will then be available via
	 * the goalFound() method, as well as the
	 * getFinalState(), getFinalDepth(), getFinalCost(), and
	 * getFinalPath() methods.
	 * Note that if goalFound() returns false, or no search has been run yet,
	 * the results of the other 
	 */
	public abstract void search();
	
	/**
	 * Conducts a search, displaying the results of the search and the time
	 * taken to do it to standard output.
	 */
	public void verboseSearch() {
		long startTime = System.currentTimeMillis();
		this.search();
		System.out.println("Time taken: " + (System.currentTimeMillis()
				- startTime) + "ms");
		
		if (this.goalFound()) {
			double finalCost = this.getGoalCost();
			int finalDepth = this.getGoalDepth();
			System.out.println(String.format("Arrived at %s for "
					+ "cost %.2f at depth %d", goal, finalCost, finalDepth));
			List<ASVConfig> path = this.getGoalPath();
			if (path.size() < 100) {
				System.out.println("Path taken:" + path);
			}
		} else {
			System.out.println("Failed to find the goal!");
		}
	}
	
	/**
	 * Returns whether a goal state has been reached.
	 * @return true if a search has been successful, and false otherwise.
	 */
	public abstract boolean goalFound();
	
	/**
	 * Returns the depth at which the goal state was found; 
	 * behaviour undefined if no goal has been found.
	 * @return the goal state of the search.
	 */
	public abstract int getGoalDepth();
	
	/**
	 * Returns the cost of the path taken to the goal; 
	 * behaviour undefined if no goal has been found.
	 * @return the goal state of the search.
	 */
	public abstract double getGoalCost();
	
	/**
	 * Returns the path taken to reach the goal.
	 * @return the path to the goal as a list, where the first element is
	 * the initial state and the last is the goal.
	 */
	public abstract List<ASVConfig> getGoalPath();
}
