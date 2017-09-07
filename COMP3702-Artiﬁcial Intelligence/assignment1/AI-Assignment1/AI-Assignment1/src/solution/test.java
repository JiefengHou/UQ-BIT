package solution;

import java.util.ArrayList;
import java.util.List;

import algorithm.AStarSearch;
import algorithm.AbstractSearchAlgorithm;
import problem.ASVConfig;

public class test {

	static double[] a = {0.150,0.200,0.150,0.150,0.200,0.150};
	static ASVConfig asv1 = new ASVConfig(a);
	static double[] b = {0.150,0.200,0.200,0.150,0.200,0.150};
	static ASVConfig asv2 = new ASVConfig(b);
	static double[] c = {0.150,0.200,0.250,0.150,0.200,0.150};
	static ASVConfig asv3 = new ASVConfig(c);
	static double[] d = {0.150,0.200,0.250,0.150,0.200,0.150};
	static ASVConfig asv4 = new ASVConfig(d);
	static double[] e = {0.150,0.200,0.250,0.150,0.200,0.150};
	static ASVConfig asv5 = new ASVConfig(e);
	static double[] f = {0.150,0.200,0.250,0.150,0.200,0.150};
	static ASVConfig asv6 = new ASVConfig(f);
	static double[] g = {0.150,0.200,0.250,0.150,0.200,0.150};
	static ASVConfig asv7 = new ASVConfig(g);
	
	static double route1 = 0.1;
	static double route2 = 0.2;
	static double route3 = 0.2;
	static double route4 = 0.1;
	static double route5 = 0.2;
	static double route6 = 0.2;
	static double route7 = 0.1;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<ASVConfig> path = new ArrayList<ASVConfig>();
		try{
			asv1.addSuccessor(asv2, route1);
			asv2.addSuccessor(asv1, route2);
			asv2.addSuccessor(asv3, route3);
			asv3.addSuccessor(asv2, route3);
			asv1.addSuccessor(asv4, route4);
			asv4.addSuccessor(asv1, route4);
			asv2.addSuccessor(asv5, route2);
			asv5.addSuccessor(asv2, route2);
			asv5.addSuccessor(asv6, route5);
			asv6.addSuccessor(asv5, route5);
			asv5.addSuccessor(asv7, route6);
			asv7.addSuccessor(asv5, route5);
//			System.out.println(asv1.getSuccessors());
//			System.out.println(asv2.getSuccessors());
			AbstractSearchAlgorithm algo = null;
			totalDistanceHeuristic heuristic = new totalDistanceHeuristic(asv7);
			algo = new AStarSearch(asv1, asv7, heuristic);
			algo.verboseSearch();
			if (algo.goalFound()) {
				for (ASVConfig s : algo.getGoalPath()) {
					path.add((ASVConfig) s);
				}
			}
			
		}catch(Exception e){
			System.out.println("Sorry, please "
					+ "enter your full name separated by a space.");
		}
	}
}
