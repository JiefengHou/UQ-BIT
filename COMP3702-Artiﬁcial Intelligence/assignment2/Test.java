package solver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import problem.Cycle;
import problem.Setup;
import problem.Tour;
import problem.Track;

public class Test {

	
	static String defaultCycleFile = "testcases/novice/cycle.txt";
	static String defaultMetaTrackFile = "testcases/novice/meta-track.txt";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		HashMap map = new HashMap();
//		HashSet set = new HashSet();
//		int e = 1;
//		String e1 = "d";
//		set.add(e);
//		set.add(e1);
//		String key = "id";
//		String value = "sth";
//		int key1 = 1;
//		int value1 = 2;
//		map.put(key, value);
//		map.put(key1, value1);
//		System.out.println(map);
//		System.out.println(set);
		
		String cycleFile = defaultCycleFile;
		String metaTrackFile = defaultMetaTrackFile;
		if (args.length != 0 && args.length != 2) {
			System.out.println("Arguments: cycle-filename meta-track-filename "
					+ "result-filename");
			System.exit(1);
		} else if (args.length == 2) {
			cycleFile = args[0].trim();
			metaTrackFile = args[1].trim();
		}
		Setup setup = new Setup(cycleFile, metaTrackFile);
		Tour tour = new Tour(setup);
		Simulation simulation = new Simulation();
		List<Map<Double, Map<Track, Cycle>>> res = simulation.calculator(tour);
		
		System.out.println(res.size());
		//System.out.println(res);
		
//		for(Map<Double, Map<Track, Cycle>> e : res){
//			System.out.println(e);
//		}
		
		res = simulation.sortPositive(res);
		for(Map<Double, Map<Track, Cycle>> e : res){
			System.out.println(e);
		}
		
		simulation.sortingTwo(res);
	}

}
