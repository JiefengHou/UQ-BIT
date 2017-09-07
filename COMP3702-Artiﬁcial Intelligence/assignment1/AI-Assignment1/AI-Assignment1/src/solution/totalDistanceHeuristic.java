package solution;

import problem.ASVConfig;
import algorithm.Heuristic;
import algorithm.State;


public  class totalDistanceHeuristic implements Heuristic {

	private ASVConfig goalState;

	
	public totalDistanceHeuristic(ASVConfig goalState) {
		this.goalState = goalState;
	}


	@Override
	public double estimate(State s2) {
		// TODO Auto-generated method stub
		State ras = (State)s2;
		return ((ASVConfig) ras).totalDistance(goalState);
	}

}
