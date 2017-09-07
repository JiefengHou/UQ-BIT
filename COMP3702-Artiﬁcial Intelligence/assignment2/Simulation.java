package solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import problem.Action;
import problem.Cycle;
import problem.Direction;
import problem.Distractor;
import problem.GridCell;
import problem.Node;
import problem.Opponent;
import problem.Player;
import problem.RaceSim;
import problem.RaceSimTools;
import problem.RaceState;
import problem.Setup;
import problem.Tour;
import problem.Track;
import problem.Cycle.Speed;

public class Simulation {

    private static Random random = new Random();
	
	public static List<Map<Double, Map<Track, Cycle>>> calculator(Tour tour){
		List<Map<Double, Map<Track, Cycle>>> res = 
				new ArrayList<Map<Double, Map<Track, Cycle>>>();
		List<Cycle> purchasableCycles = tour.getPurchasableCycles();
		List<Track> allTracks = tour.getTracks();
		for(int i = 0; i < purchasableCycles.size(); i++){
			for (int j = 0; j < allTracks.size(); j++){
				double money = 0;
				//System.out.println(allTracks.size());
				Cycle cycle = purchasableCycles.get(i);
				//System.out.println(cycle);
				Track track = allTracks.get(j);
				
				Map<String, GridCell> startingPositions = 
						track.getStartingPositions();
				String id = "";
				GridCell startPosition = null;				
				for (Map.Entry<String, GridCell> entry : startingPositions.entrySet()) {
					id = entry.getKey();
					startPosition = entry.getValue();
					break;
				}
				Player player = new Player(id, cycle, startPosition);

				List<Player> players = new ArrayList<Player>();
				players.add(new Player(id, cycle, startPosition));
				//System.out.println(players);
				List<Opponent> opponents = track.getOpponents();
				
				List<Distractor> distractors = track.getDistractors();
				Random random = new Random();
				RaceState raceState = new RaceState(players, opponents, distractors);
				RaceSim simulator = new RaceSim(raceState, track, random);
				
				while(!simulator.isFinished()){
					RaceState state = simulator.getCurrentState();
					System.out.println();
					System.out.println(track.getName());
					System.out.println("Player position: " + 
							state.getPlayers().get(0).getPosition());
					
					if(state.getOpponents().size()>0) {
						System.out.println("Opponent position: " + 
								state.getOpponents().get(0).getPosition());	
					}
					System.out.println(RaceSimTools.stateToString(state, track));
					
					Node<RaceState> root = new Node<RaceState>(state);
					List<Node<RaceState>> nodeList = new ArrayList<Node<RaceState>>();
					nodeList.add(root);
					
					nextSimulate(nodeList,track);
					
					
					List<Action> actions = new ArrayList<Action>();
					Node<RaceState> selectNode = select(root,track);
					actions.add(selectNode.getAction());
					System.out.println(actions);
					if(actions.get(0) == null){
						break;
					}
					simulator.stepTurn(actions);
				}
				if (simulator.isFinished()) {
					money -= simulator.getTotalDamageCost();
				}
				money -= track.getRegistrationFee();
				if(simulator.getCurrentStatus() == RaceState.Status.WON){
					money += track.getPrize();
				}
				money -= cycle.getPrice();	
				HashMap<Track, Cycle> value = new HashMap<Track, Cycle>();
				HashMap<Double, Map<Track, Cycle>> map = new HashMap<Double, Map<Track, Cycle>>();

				value.put(track, cycle);
				map.put(money, value);
				res.add(map);
			}
		}
		return res;
	}
	private static Node<RaceState> select(Node<RaceState> root, Track track) {
		double maxValue = 0;
		Node<RaceState> selectNode = new Node<RaceState>();
		for(Node<RaceState> node : root.getChildren()) {
			double value = node.getValue()+track.getPrize()
					*Math.sqrt(Math.log(node.getParent().getVisit())/node.getVisit());
			if(value>=maxValue) {
				maxValue = value;
				selectNode = node;
			}
		}
		return selectNode;
	}
	
	//expand the node, add children
	private static void expand(Node<RaceState> root, Track track) {
		Player player = root.getData().getPlayers().get(0);
		GridCell playerCurrentPos = player.getPosition();
		Cycle cycle = player.getCycle();
		List<Action> actions = getNextActionList(playerCurrentPos,
				track,cycle);
		for(Action action : actions) {
			List<Action> nextAction = new ArrayList<Action>();
			nextAction.add(action);
			Node<RaceState> child = new Node<RaceState>(RaceSimTools.sampleNextState(
					root.getData(), nextAction, track, random), action, root);
		}
	}
	
	//calculate the value
	private static Double rollOut(Node<RaceState> startNode, Track track) {
		double money = 0;
		RaceState.Status currentStatus = startNode.getData().getStatus();
		if(currentStatus!=RaceState.Status.RACING) {
			money -= startNode.getData().getTotalDamageCost();
			if(currentStatus==RaceState.Status.WON) {
				money += track.getPrize();
			}
		} else {
			Player player = startNode.getData().getPlayers().get(0);
			GridCell playerCurrentPos = player.getPosition();
			Cycle cycle = player.getCycle();
			//get possible actions
			List<Action> actions = getNextActionList(playerCurrentPos,
					track,cycle);	
			List<Action> nextAction = new ArrayList<Action>();
			//get a random action from action list	
			nextAction.add(actions.get(random.nextInt(actions.size())));
			//sample next state using random action
			Node<RaceState> child = new Node<RaceState>(RaceSimTools.sampleNextState(
					startNode.getData(), nextAction, track, random), startNode);
			money -= child.getData().getTotalDamageCost();
			//continue to calculate money until racing is finished
			money += rollOut(child,track);
		}
		return money;
	}
	
	//update the value and visit times
	private static void update(Node<RaceState> node, Double value) {
		List<Node<RaceState>> nodeAncestry = node.getAncestry();
		for(Node<RaceState> p : nodeAncestry) {
			p.addVisit(1);
		}
		
		if(nodeAncestry.size()>=2) {
			Node<RaceState> targetNode = nodeAncestry.get(1);
			targetNode.setValue((value+targetNode.getValue())/targetNode.getVisit());
		}
	}
	
	//check the current state is finished or not
	private static boolean checkRacing(Node<RaceState> node) {
		RaceState.Status currentStatus = node.getData().getStatus();
		return currentStatus==RaceState.Status.RACING;
	}
	
	//simulate nodes of each breadth
	private static void nextSimulate(List<Node<RaceState>> nodeList, Track track) {
		List<Node<RaceState>> nextList = new ArrayList<Node<RaceState>>();
		for(Node<RaceState> node : nodeList) {
			if(checkRacing(node)) {
				//expand the node
				expand(node,track);
				for(Node<RaceState> childNode : node.getChildren()) {
					//calculate money(value) by simulation
					double value = rollOut(childNode,track);
					//update value to this node
					update(childNode,value);
					nextList.add(childNode);
				}
			}
		}
	}
	
	//return the valid action list
	private static List<Action> getNextActionList(GridCell currentPosition, 
			Track track, Cycle cycle) {
		List<Action> actions = new ArrayList<Action>();
		
		GridCell nextPos = currentPosition;
		nextPos = nextPos.shifted(Direction.E);
		if(nextPos != null && RaceSimTools.withinBounds(nextPos, track)) {
			actions.add(Action.FS);
		}
		nextPos = nextPos.shifted(Direction.E);
		if(nextPos != null && RaceSimTools.withinBounds(nextPos, track) 
				&& cycle.getSpeed() != Speed.SLOW) {
			actions.add(Action.FM);
		}
		nextPos = nextPos.shifted(Direction.E);
		if(nextPos != null && RaceSimTools.withinBounds(nextPos, track) 
				&& cycle.getSpeed()!=Speed.SLOW && cycle.getSpeed()!=Speed.MEDIUM) {
			actions.add(Action.FF);
		}
		
		if(currentPosition.shifted(Direction.NE) != null 
				&& RaceSimTools.withinBounds(currentPosition.shifted(Direction.NE), track)) {
			actions.add(Action.NE);
		}	
		if(currentPosition.shifted(Direction.SE) != null 
				&& RaceSimTools.withinBounds(currentPosition.shifted(Direction.SE), track)) {
			actions.add(Action.SE);
		}	
		return actions;
	}
	
	
	public static List<Map<Double, Map<Track, Cycle>>>sortPositive
	(List<Map<Double, Map<Track, Cycle>>> res){
		List<Map<Double, Map<Track, Cycle>>> out = 
				new ArrayList<Map<Double, Map<Track, Cycle>>>();
		
		for(Map<Double, Map<Track, Cycle>> e : res){
			for(Iterator<Double> itr = e.keySet().iterator(); itr.hasNext();){
				if(itr.next() > 0){
					out.add(e);
				}
			}
		}
//		TreeMap <Double, Map<Track,Cycle>> outMap =
//				new TreeMap<Double, Map<Track,Cycle>>(
//						new Comparator<Double>(){	
//						@Override
//						public int compare(Double o1, Double o2){
//							return o2.compareTo(o1);
//						}
//					}	
//			);
//		for(Map<Double, Map<Track, Cycle>> e : res){
//			outMap.putAll(e);
//		}
//		System.out.println(outMap.size());
//		
//		out.add(outMap);
		return out;
	}
	
	public static List<Map<Double, Map<Track, Cycle>>>sortingOne
	(List<Map<Double, Map<Track, Cycle>>> res){
		List<Map<Double, Map<Track, Cycle>>> out = 
				new ArrayList<Map<Double, Map<Track, Cycle>>>();
		for(Map<Double, Map<Track, Cycle>> e : res){
			
		}
		return out;
		
	}
	public static List<Map<Double, Map<Track, Cycle>>>sortingTwo
	(List<Map<Double, Map<Track, Cycle>>> res){
		List<Map<Double, Map<Track, Cycle>>> out = 
				new ArrayList<Map<Double, Map<Track, Cycle>>>();
		for(Map<Double, Map<Track, Cycle>> e : res){
			
		}
		return out;
	}
	
}
