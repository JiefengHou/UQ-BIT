package solution;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import problem.ASVConfig;
import problem.Obstacle;
import problem.ProblemSpec;


import algorithm.*;
import tester.Tester;



public class solution {
			
	/** Holds the problem details */
	private static ProblemSpec problemSpec;
	
	/** The maximum distance any ASV can travel between two states */
	public static final double MAX_STEP = 0.001;
	public static String DEFAULT_OUTPUT = "solution.txt";
	public static final double BOOM_LENGTH = 0.05;
	
	/** The list of states in the sampled state space. */
	private static List<ASVConfig> asvConfigs = new ArrayList<ASVConfig>();	
	
	/** The random number generator */
	public static final Random random = new Random();
	
	private static Rectangle2D bounds = new Rectangle2D.Double(0, 0, 1, 1);
	
	static Tester test = new Tester();
	
	/** Sets the seed for RNG */
	public static void setSeed(long seed) {
		random.setSeed(seed);
	}
	
	public static double normalise(double angle) {
		if (angle >= 0*Math.PI && angle < 0.5*Math.PI) {
			angle = angle;
		} else if(angle >= 0.5*Math.PI && angle < Math.PI) {
			angle = Math.PI - angle;
		} else if(angle >= Math.PI && angle < 1.5*Math.PI) {
			angle = angle - Math.PI;
		} else {
			angle = 2*Math.PI - angle;
		}
		return angle;
	}
	

	public static int pointPosition(double angle) {
		if (angle >= 0*Math.PI && angle < 0.5*Math.PI) {
			return 1;
		} else if(angle >= 0.5*Math.PI && angle < Math.PI) {
			return 2;
		} else if(angle >= Math.PI && angle < 1.5*Math.PI) {
			return 3;
		} else {
			return 4;
		}
	}
	
	
	public static ASVConfig createRandomConfig(int asvNum) {
		double[] coords = new double[2*asvNum];
		double x = 0;
		double y = 0;
		double angle = 0;
		double randomAngle = 0;
		double totalTurned = 0;
		double lastAngle = 0;
		int position = 0;
		double range = 0;
		double range1 = 0;
		boolean side = true;
		Point2D p2 = new Point2D.Double(0,0);
		

		for(int i=0; i < asvNum*2; i=i+2) {
			if(i==0) {
				x = random.nextDouble();
				y = random.nextDouble();	
			/*
			} else {
				randomAngle = random.nextDouble()*Math.PI*2;
				x = x + BOOM_LENGTH * Math.cos(randomAngle);
				y = y + BOOM_LENGTH * Math.sin(randomAngle);
			}
			*/
			
			} else if(i==2) {
				randomAngle = random.nextDouble()*Math.PI*2;
				x = x + BOOM_LENGTH * Math.cos(randomAngle);
				y = y + BOOM_LENGTH * Math.sin(randomAngle);
				position = pointPosition(randomAngle);
				if(position == 1 || position == 3) {
					range = normalise(randomAngle);
					range1 = Math.PI + normalise(randomAngle);
				} else {
					range = Math.PI - normalise(randomAngle);
					range1 = - normalise(randomAngle);
				}
			} else if(i==4) {
				randomAngle = random.nextDouble()*Math.PI*2;
				x = x + BOOM_LENGTH * Math.cos(randomAngle);
				y = y + BOOM_LENGTH * Math.sin(randomAngle);
				if(randomAngle>range && randomAngle<range1) {
					side = true;
				} else {
					side = false;
				}
				position = pointPosition(randomAngle);	
				lastAngle = normalise(randomAngle);
			} else {	
				if(side) {
					if(position == 1 || position == 3) {
						randomAngle = lastAngle + random.nextDouble()*Math.PI;
					} else {
						randomAngle = random.nextDouble()*Math.PI - lastAngle;
					}
					//randomAngle = (random.nextDouble()*Math.PI - lastAngle)+Math.PI;
				} else {
					if(position == 1 || position == 3) {
						randomAngle = (random.nextDouble()*Math.PI + lastAngle)+Math.PI;
					} else {
						randomAngle = (random.nextDouble()*Math.PI - lastAngle)+Math.PI;
					}
					//randomAngle = random.nextDouble()*Math.PI - lastAngle;
				}
				x = x + BOOM_LENGTH * Math.cos(randomAngle);
				y = y + BOOM_LENGTH * Math.sin(randomAngle);
				position = pointPosition(randomAngle);
				lastAngle = normalise(randomAngle);
			}
			
			coords[i] = x;//new BigDecimal(x).setScale(3, RoundingMode.HALF_UP).doubleValue();
			coords[i+1] = y;//new BigDecimal(y).setScale(3, RoundingMode.HALF_UP).doubleValue();
		}				
		return new ASVConfig(coords);
	}
	
	/*
	public static ASVConfig createWallConfig(int asvNum,Obstacle obstacles) {
		
	}
	*/
	
	public static void generateConfigs(int numberToGenerate) {
		for (int i = 0; i < numberToGenerate; i++) {
			while(true) {
				ASVConfig asvConfig = createRandomConfig(problemSpec.getASVCount());
				//System.out.println(asvConfig.toString());
				if (!test.hasCollision(asvConfig, problemSpec.getObstacles()) &&
					test.hasEnoughArea(asvConfig) && test.fitsBounds(asvConfig)
					&& test.isConvex(asvConfig)) {
					asvConfigs.add(asvConfig);
					//System.out.println(asvConfig);
					break;
				}
			}
		}
	}
	
	public static double[] maxDistanceIndex(ASVConfig asv,ASVConfig asv1) {
		double[] result = new double[2];
		double maxDistance = 0;
		int index = 0;
		for (int i = 0; i < asv.getASVCount(); i++) {
			double distance = asv.getPosition(i).distance(
					asv1.getPosition(i));
			if (distance > maxDistance) {
				maxDistance = distance;
				index = i;
			}
		}
		result[0]=maxDistance;
		result[1]=index;
		return result;
	}
	
	public static List<Double> ASVtoAngle(ASVConfig asvConfig) {
		int numASV = asvConfig.getASVCount();
		List<Double> asvAngle = new ArrayList<Double>();
		asvAngle.add(asvConfig.getPosition(0).getX());
		asvAngle.add(asvConfig.getPosition(0).getY());
		for(int i=1;i<numASV;i++) {
			double lastPointX = asvConfig.getPosition(i-1).getX();
			double lastPointY = asvConfig.getPosition(i-1).getY();
			double currentPointX = asvConfig.getPosition(i).getX();
			double currentPointY = asvConfig.getPosition(i).getY();
//			System.out.println("current point: "+currentPointX+" "+currentPointY);
//			System.out.println("last point: "+lastPointX+" "+lastPointY);
			double angle = Math.atan2(lastPointY-currentPointY,lastPointX-currentPointX);
			asvAngle.add(angle);
		}
		return asvAngle;
	}
	
	
	public static ASVConfig AngletoASV(List<Double> asvAngle) {
		double currentPointX = 0;
		double currentPointY = 0;
		double[] coords = new double[(asvAngle.size()-1)*2];
		coords[0] =  asvAngle.get(0);//new BigDecimal(asvAngle.get(0)).setScale(3, RoundingMode.HALF_UP).doubleValue();
		coords[1] =  asvAngle.get(1);//new BigDecimal(asvAngle.get(1)).setScale(3, RoundingMode.HALF_UP).doubleValue();
		
		for(int i=2;i<asvAngle.size();i++) {
			double lastPointX = coords[2*i-4];
			double lastPointY = coords[2*i-3];
			double angle = asvAngle.get(i);
						
			currentPointX = lastPointX - 0.05*Math.cos(angle);
			currentPointY = lastPointY - 0.05*Math.sin(angle);	
			//System.out.println("current point: "+currentPointX+" "+currentPointY);
			//System.out.println("last point: "+lastPointX+" "+lastPointY);
			if(i>2) {
				double lastAngle = asvAngle.get(i-1);
				//System.out.println(lastAngle+", "+Math.toDegrees(lastAngle)+'\n');
				if(lastAngle > angle) {
					//System.out.println("asdasd");
					currentPointX = lastPointX - 0.05*Math.cos(-angle);
					currentPointY = lastPointY + 0.05*Math.sin(-angle);
				}
			} 
			
			coords[2*i-2] = currentPointX;//new BigDecimal(currentPointX).setScale(3, RoundingMode.HALF_UP).doubleValue();
			coords[2*i-1] = currentPointY;//new BigDecimal(currentPointY).setScale(3, RoundingMode.HALF_UP).doubleValue();
		}
		return new ASVConfig(coords);	
	}
	
	public static boolean hasDirectPath(ASVConfig startASV, ASVConfig endASV) {
		if(startASV.getASVCount() != endASV.getASVCount()) {
			return false;
		}
		ASVConfig thisASV = startASV;
		double maxDistance = endASV.maxDistance(startASV);
		
		if(maxDistance > 0.001) {
			
			double distance = endASV.getPosition(0).distance(thisASV.getPosition(0));
			int maxStep = (int)(distance/0.001);
			
			double deltaX = endASV.getPosition(0).getX()-thisASV.getPosition(0).getX();
			double deltaY = endASV.getPosition(0).getY()-thisASV.getPosition(0).getY();
			double angle = Math.atan(deltaY/deltaX);

			List<Double> startList = ASVtoAngle(thisASV);
			List<Double> endList = ASVtoAngle(endASV);
			List<Double> deltaList = new ArrayList<Double>();
			
			deltaList.add(deltaX/maxStep);
			deltaList.add(deltaY/maxStep);
			
			for(int i=2;i<startList.size();i++) {
				double deltaAngle =  endList.get(i) - startList.get(i);
				deltaList.add(deltaAngle/maxStep);
			}
			//System.out.println("maxstep:"+maxStep+"\n");
			for(int ii=0;ii<maxStep;ii++) {
				List<Double> nextList = new ArrayList<Double>();
				startList = ASVtoAngle(startASV);
				double nextX = startASV.getPosition(0).getX() + deltaList.get(0);
				double nextY = startASV.getPosition(0).getY() + deltaList.get(1);
				
				nextList.add(nextX);
				nextList.add(nextY);
				double nextAngle;
				for(int i=2;i<startList.size();i++) {
					nextAngle = startList.get(i)+deltaList.get(i);
					nextList.add(nextAngle);	
				}
				ASVConfig nextASVConfig = AngletoASV(nextList);
				if (test.hasCollision(nextASVConfig, problemSpec.getObstacles())) {
					//System.out.println("1");
					return false;
				} 
				if(!test.hasEnoughArea(nextASVConfig)) {
					//System.out.println("2");
					return false;
				}
				if(!test.fitsBounds(nextASVConfig)) {
					//System.out.println("3");
					return false;
				}
				if(!test.isValidStep(startASV,nextASVConfig)) {


				}
				startASV = nextASVConfig;
			}

		} else return true;
		
		return true;			
	}

	public static ASVConfig createWallConfig(int asvNum) {
		double[] coords = new double[2*asvNum];
		double x = 0;
		double y = 0;
		double angle = 0;
		double randomAngle = 0;
		double totalTurned = 0;
		double lastAngle = 0;
		int position = 0;
		double range = 0;
		double range1 = 0;
		boolean side = true;
		Point2D p2 = new Point2D.Double(0,0);
		List<Obstacle> obstacles = problemSpec.getObstacles();
		List<Double> obstacleList = getObstacle(obstacles);
		double pad = 0.1;
		int noOfObstacles = obstacleList.size()/4;
		for (int i = 0; i < noOfObstacles; i++) {
			double max_X = obstacleList.get(4*i) + pad;
			double max_Y = obstacleList.get(4*i+1) + pad;
			double min_X = obstacleList.get(4*i+2) + pad;
			double min_Y = obstacleList.get(4*i+3) + pad;
			for(int j=0; j < asvNum*2; j=j+2) {
				if(j==0) {
					x = random.nextDouble()*(max_X-min_X) + min_X;
					y = random.nextDouble()*(max_Y-min_Y) + min_Y;	
				} else if(j==2) {
					randomAngle = random.nextDouble()*Math.PI*2;
					x = x + BOOM_LENGTH * Math.cos(randomAngle);
					y = y + BOOM_LENGTH * Math.sin(randomAngle);
					position = pointPosition(randomAngle);
					if(position == 1 || position == 3) {
						range = normalise(randomAngle);
						range1 = Math.PI + normalise(randomAngle);
					} else {
						range = Math.PI - normalise(randomAngle);
						range1 = - normalise(randomAngle);
					}
				} else if(j==4) {
					randomAngle = random.nextDouble()*Math.PI*2;
					x = x + BOOM_LENGTH * Math.cos(randomAngle);
					y = y + BOOM_LENGTH * Math.sin(randomAngle);
					if(randomAngle>range && randomAngle<range1) {
						side = true;
					} else {
						side = false;
					}
					position = pointPosition(randomAngle);
					if(position == 1 || position == 3) {
						range = normalise(randomAngle);
						range1 = Math.PI + normalise(randomAngle);
					} else {
						range = Math.PI - normalise(randomAngle);
						range1 = - normalise(randomAngle);
					}	
					lastAngle = normalise(randomAngle);
				} else {	
					if(side) {
						randomAngle = (random.nextDouble()*Math.PI - lastAngle)+Math.PI;
					} else {
						randomAngle = random.nextDouble()*Math.PI - lastAngle;
					}
					x = x + BOOM_LENGTH * Math.cos(randomAngle);
					y = y + BOOM_LENGTH * Math.sin(randomAngle);
					lastAngle = normalise(randomAngle);
				}
				
				coords[j] = x;//new BigDecimal(x).setScale(3, RoundingMode.HALF_UP).doubleValue();
				coords[j+1] = y;//new BigDecimal(y).setScale(3, RoundingMode.HALF_UP).doubleValue();
			}
		}
		return new ASVConfig(coords);
	}
	
	public static void generateWallConfigs(int numberToGenerate) {
		for (int i = 0; i < numberToGenerate; i++) {
			while(true) {
				ASVConfig asvConfig = createWallConfig(problemSpec.getASVCount());
				//System.out.println(asvConfig.toString());
				if (!test.hasCollision(asvConfig, problemSpec.getObstacles()) &&
					test.hasEnoughArea(asvConfig) && test.fitsBounds(asvConfig)
					&& test.isConvex(asvConfig)) {
					asvConfigs.add(asvConfig);
					break;
				}
			}
		}
	}
	
	public static ASVConfig createLineConfig(int asvNum) {
		
		double[] coords = new double[2*asvNum];
		double x = 0;
		double y = 0;
		double angle = 0;
		double randomAngle = 0;
		double lastAngle = 0;
		int position = 0;
		double range = 0;
		double range1 = 0;
		boolean side = true;

		for(int i=0; i < asvNum*2; i=i+2) {
			if(i==0) {
				x = random.nextDouble()*problemSpec.getInitialState().getPosition(0).distance(problemSpec.getGoalState().getPosition(0))
						+problemSpec.getInitialState().getPosition(0).getX();
				y = random.nextDouble()*problemSpec.getInitialState().getPosition(0).distance(problemSpec.getGoalState().getPosition(0))
						+problemSpec.getInitialState().getPosition(0).getY();
			} else if(i==2) {
				randomAngle = random.nextDouble()*Math.PI*2;
				x = x + BOOM_LENGTH * Math.cos(randomAngle);
				y = y + BOOM_LENGTH * Math.sin(randomAngle);
				position = pointPosition(randomAngle);
				if(position == 1 || position == 3) {
					range = normalise(randomAngle);
					range1 = Math.PI + normalise(randomAngle);
				} else {
					range = Math.PI - normalise(randomAngle);
					range1 = - normalise(randomAngle);
				}
			} else if(i==4) {
				randomAngle = random.nextDouble()*Math.PI*2;
				x = x + BOOM_LENGTH * Math.cos(randomAngle);
				y = y + BOOM_LENGTH * Math.sin(randomAngle);
				if(randomAngle>range && randomAngle<range1) {
					side = true;
				} else {
					side = false;
				}
				position = pointPosition(randomAngle);	
				lastAngle = normalise(randomAngle);
			} else {	
				if(side) {
					if(position == 1 || position == 3) {
						randomAngle = lastAngle + random.nextDouble()*Math.PI;
					} else {
						randomAngle = random.nextDouble()*Math.PI - lastAngle;
					}
				} else {
					if(position == 1 || position == 3) {
						randomAngle = (random.nextDouble()*Math.PI + lastAngle)+Math.PI;
					} else {
						randomAngle = (random.nextDouble()*Math.PI - lastAngle)+Math.PI;
					}
				}
				x = x + BOOM_LENGTH * Math.cos(randomAngle);
				y = y + BOOM_LENGTH * Math.sin(randomAngle);
				position = pointPosition(randomAngle);
				lastAngle = normalise(randomAngle);
			}
			
			coords[i] = x;
			coords[i+1] = y;
		}				
		return new ASVConfig(coords);
	}
	
	public static void generateLineConfigs(int numberToGenerate) {
		for (int i = 0; i < numberToGenerate; i++) {
			while(true) {
				ASVConfig asvConfig = createLineConfig(problemSpec.getASVCount());
				//System.out.println(asvConfig.toString());
				if (!test.hasCollision(asvConfig, problemSpec.getObstacles()) &&
					test.hasEnoughArea(asvConfig) && test.fitsBounds(asvConfig)
					&& test.isConvex(asvConfig)) {
					asvConfigs.add(asvConfig);
					//System.out.println(asvConfigs.size());
					break;
				}
			}
		}
	}
	
	public static void connectConfigs() {
		//System.out.println("asvConfig size: "+asvConfigs.size());
		for (int i = 0; i < asvConfigs.size(); i++) {
			ASVConfig asv1 = asvConfigs.get(i);
			for (int j = i + 1; j < asvConfigs.size(); j++) {
				ASVConfig asv2 = asvConfigs.get(j);
				double route = asv1.totalDistance(asv2);
				if (!hasDirectPath(asv1, asv2)) {
					continue;
				}
//				System.out.println(asv1);
//				System.out.println(asv2);
				asv1.addSuccessor(asv2, route);
				asv2.addSuccessor(asv1, route);
			}
		}
	}
	
	/**
	 * Writes the given solution path to a text file.
	 * 
	 * @param path
	 *            the path taken from the initial state to the goal state.
	 * @param outputPath
	 *            the path of the output file to write to.
	 * @throws IOException
	 *             if there is an error writing to the output file.
	 */
	public static void writeOutput(List<ASVConfig> path, String outputPath, double cost)
			throws IOException {

		BufferedWriter out = new BufferedWriter(new FileWriter(outputPath));
		String step = Integer.toString(path.size());
		String c = Double.toString(cost);
		//new BigDecimal(cost).setScale(3, RoundingMode.HALF_UP).doubleValue()
		out.write(step);
		out.write(" ");
		out.write(c);
		for(ASVConfig s :path){
			out.write("\n");
			String toWrite = s.toString();
			out.write(toWrite);
			
		}
		out.close();
	}
	public static List<ASVConfig> fillPath(List<ASVConfig> path) {
		List<ASVConfig> result = new ArrayList<ASVConfig>();
		for(int p=1;p<path.size();p++) {

			ASVConfig startASV = path.get(p-1);
			ASVConfig endASV = path.get(p);
			if(p==1) {
				result.add(startASV);
			}
			double maxDistance = endASV.maxDistance(startASV);
			double distance = endASV.getPosition(0).distance(startASV.getPosition(0));
			int maxStep = (int)(distance/0.001);
			//System.out.println("maxStep: "+maxStep);
			double deltaX = endASV.getPosition(0).getX()-startASV.getPosition(0).getX();
			double deltaY = endASV.getPosition(0).getY()-startASV.getPosition(0).getY();
			double angle = Math.atan(deltaY/deltaX);
			//System.out.println("deltaX:"+deltaX+"...."+"deltaY"+deltaY);
			List<Double> startList = ASVtoAngle(startASV);
			//System.out.println("startList:" + startList);
			List<Double> endList = ASVtoAngle(endASV);
			//System.out.println("endList:" + endList);
			List<Double> deltaList = new ArrayList<Double>();
			
			deltaList.add(deltaX/maxStep);
			deltaList.add(deltaY/maxStep);
			
			for(int i=2;i<startList.size();i++) {
				double deltaAngle =  endList.get(i) -  startList.get(i);
				deltaList.add(deltaAngle/maxStep);
			}
			//System.out.println("deltaList:" + deltaList);
			//System.out.println("start looping");
			for(int ii=0;ii<maxStep;ii++) {
				List<Double> nextList = new ArrayList<Double>();
				startList = ASVtoAngle(startASV);
				//System.out.println("ii:"+ii);
				double nextX = startASV.getPosition(0).getX() + deltaList.get(0);
				double nextY = startASV.getPosition(0).getY() + deltaList.get(1);
				
				nextList.add(nextX);
				nextList.add(nextY);
				
				for(int i=2;i<startList.size();i++) {
					double nextAngle = startList.get(i) + deltaList.get(i);
					nextList.add(nextAngle);	
				}
				//System.out.println("startList:"+startList.toString());
				//System.out.println("deltaList:"+deltaList.toString());
				//System.out.println("nextList:"+nextList.toString());
				ASVConfig nextASVConfig = AngletoASV(nextList);
				result.add(nextASVConfig);
				startASV = nextASVConfig;
			}
			result.add(endASV);
		}
		return result;
	}
	
	public static List<Double> getObstacle(List<Obstacle> obstacles) {
		List<Double> obstacleList = new ArrayList<Double>();
		for(Obstacle o : obstacles) {
			obstacleList.add(o.getRect().getMaxX());
			obstacleList.add(o.getRect().getMaxY());
			obstacleList.add(o.getRect().getMinX());
			obstacleList.add(o.getRect().getMinY());
		}
		return obstacleList;
	}

	public static void main(String[] args) throws IOException {
		long seed = (new Random()).nextLong();
		System.out.println("Seed: " + seed);
		setSeed(seed);
		
		problemSpec = new ProblemSpec();
		problemSpec.loadProblem("3ASV-c4.txt");
		String outputPath = DEFAULT_OUTPUT;
			
		int numASV = problemSpec.getASVCount();
		ASVConfig initialState = problemSpec.getInitialState();
		ASVConfig goalState = problemSpec.getGoalState();	
		List<Obstacle> obstacles = problemSpec.getObstacles();
		
		System.out.println("ASV number:	 "+numASV);	
		System.out.println("Initial State: "+initialState);	
		System.out.println("Goal State: "+goalState);	
		System.out.println("Obstacle: "+obstacles);	
		
		/*
		List<Double> obstacleList = new ArrayList<Double>();
		for(Obstacle o : obstacles) {
			obstacleList.add(o.getRect().getMaxX());
			obstacleList.add(o.getRect().getMaxY());
			obstacleList.add(o.getRect().getMinX());
			obstacleList.add(o.getRect().getMinY());
		}
		System.out.println(obstacleList);
		*/
		
		asvConfigs.add(initialState);
		asvConfigs.add(goalState);
		System.out.println("Generating ASV configurations!");
		long startTime = System.currentTimeMillis();
		generateConfigs(500);
		//generateWallConfigs(100);
		//generateLineConfigs(200);
		System.out.println("Time taken: " + (System.currentTimeMillis()
				- startTime) + "ms");
		
		System.out.println(asvConfigs.toString());
		System.out.println("Connecting graph!");
		long startTime1 = System.currentTimeMillis();
		connectConfigs();
		System.out.println("Time taken: " + (System.currentTimeMillis()
				- startTime1) + "ms");
		Heuristic heuristic;
		heuristic = new totalDistanceHeuristic(problemSpec.getGoalState());
		AbstractSearchAlgorithm algo;
		algo = new AStarSearch(initialState, goalState, heuristic);
		System.out.println("Searching!");
		System.out.println();
		algo.verboseSearch();
		double cost = algo.getGoalCost();
		if (algo.goalFound()) {
			List<ASVConfig> path = new ArrayList<ASVConfig>();
			for (ASVConfig s : algo.getGoalPath()) {
				path.add((ASVConfig) s);
			}
			path = fillPath(path);
			try {
				writeOutput(path, outputPath, cost);
				System.out.println("Done!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
