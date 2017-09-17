package com.fizzikgames.mastermind.ai.strategy;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import com.fizzikgames.fizlib.pathfinding.Astar;
import com.fizzikgames.fizlib.pathfinding.Node;
import com.fizzikgames.fizlib.pathfinding.Path;
import com.fizzikgames.mastermind.ai.ManhattanHeuristic;
import com.fizzikgames.mastermind.ai.PathfindingQueue;
import com.fizzikgames.mastermind.entity.AIControlledCharacter;
import com.fizzikgames.mastermind.entity.Entity;
import com.fizzikgames.mastermind.world.World;

/**
 * The pathfind strategy defines a strategy where it is given a location to pathfind to, and
 * it moves the source entity towards that location using a pathfinding algorithm.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class PathfindStrategy implements AIStrategy, Runnable {
	private static final int MAX_PATH_TRIES = 100; //Amount of tries to find a viable path before quitting.
	private AIControlledCharacter source;
	private World world;
	private Astar pathfinder;
	private Path path;
	private Node destNode;
	private Node nextNode;
	private int searchRange;
	private int currentTries;
	private boolean onPath;
	private boolean initialized;
	
	public PathfindStrategy(int searchRange) {
		this.searchRange = searchRange;
		
		initialized = false;
	}
	
	@Override
	public void run() {
		initialize();
	}
	
	@Override
	public void initialize() {
		pathfinder = new Astar(new ManhattanHeuristic(), world, searchRange * searchRange, searchRange, true, false, false);
		path = null;
		destNode = null;
		nextNode = null;
		currentTries = 0;
		onPath = false;
		
		new Thread(pathfinder).start();
		
		initialized = true;
	}
	
	@Override
	public void tick(GameContainer gc, int delta) {
		if (isInitialized()) {
			source.getSprite().current("stand_still");
			
			if (onPath) {
				if (currentTries > MAX_PATH_TRIES) {
					//Exceeded maximum amount of tries, end all pathfinding
					endPathfinding();
				}
				
				if (nextNode != null) {
					if (Entity.getPositionAsNode(source).equals(nextNode)) {
						//Reached next node, find new "next node"
						if (path.getSize() > 0) {
							nextNode = path.getNextNode();
						}
						else {
							//Reached end of path
							endPathfinding();
						}						
					}
					
					if (nextNode != null) {
						if (isValidNode(Entity.getPositionAsNode(source), nextNode) && isValidNode(Entity.getPositionAsNode(source), Entity.getPositionAsNode(source))) {
							//Move towards next node
							double oldx = source.getX();
							double oldy = source.getY();
							double newx = World.convertToEntityPos(nextNode.getX());
							double newy = World.convertToEntityPos(nextNode.getY());
							double dx = source.getX() - newx;
							double dy = source.getY() - newy;

							double cx = 0;
							double cy = 0;

							// Calculate the appropriate change in x and y
							if (dx < 0)
								cx = 1;
							else if (dx > 0)
								cx = -1;
							if (dy < 0)
								cy = 1;
							else if (dy > 0)
								cy = -1;
							if ((Math.abs(cx) == 1) && (Math.abs(cy) == 1)) {
								cx *= AIControlledCharacter.DIAGONAL;
								cy *= AIControlledCharacter.DIAGONAL;
							}
							
							source.doMovesStrictImpede(cx, cy); // Do a move using the previous change in x and y

							Vector2f current = new Vector2f((float) source.getX(), (float) source.getY());
							Vector2f dest = new Vector2f((float) newx, (float) newy);

							if (current.distance(dest) <= source.getMoves()) {
								source.setX(newx);
								source.setY(newy);
							}
							
							if (!((oldx == source.getX()) && (oldy == source.getY()))) {
								source.getSprite().current("walk_normal");
							}
							
							source.updateRotation(oldx, oldy, newx, newy); //Always be facing next node, regardless of movement
						}
						else {
							//Path interrupted, try again, but increment fail safe of tries
							pathfind(destNode);
							currentTries++;
						}
					}
					
				}
			}
		}
	}
	
	/**
	 * Sets pathfinding to the destination node.
	 */
	public void pathfind(Node dest) {
		boolean hasPath = false;
		
		if (source != null && dest != null) {
			Node src = Entity.getPositionAsNode(source);
			
			//First check if source node and dest node are connected
			if (world.getPathfinder().hasPath(source, dest.getRow(), dest.getColumn())) {
				Path checkPath = pathfinder.findPath(source, src, dest);
				if (checkPath.getSize() > 1) {
					//Have path that is atleast one node away (first node is always the starting node)
					checkPath.getNextNode(); //Pop off starting node
					path = checkPath; //Assign as new path
					nextNode = path.getNextNode();//Assign next node
					
					hasPath = true;
				}
			}
		}
		
		if (!hasPath) {
			//No immediate path
			endPathfinding();
		}
		else {
			destNode = dest;
			onPath = true;
		}
	}
	
	/**
	 * Returns if the strategy is currently pathfinding.
	 */
	public boolean isPathfinding() {
		return onPath;
	}
	
	/**
	 * Stops all pathfinding;
	 */
	public void endPathfinding() {
		path = null;
		destNode = null;
		nextNode = null;
		onPath = false;
		currentTries = 0;
	}
	
	@Override
	public void setSourceCharacter(AIControlledCharacter character) {
		source = character;
	}
	
	@Override
	public void setSourceWorld(World world) {
		this.world = world;
	}
	
	@Override
	/**
	 * Returns if the dest node is valid for the character to move
	 */
	public boolean isValidNode(Node start, Node dest) {
		return !world.movementBlockedAt(World.convertToEntityPos(dest.getX()), World.convertToEntityPos(dest.getY()), source.getWidth(), source.getHeight()) 
				&& !world.movementImpededAt(source, World.convertToEntityPos(dest.getX()), World.convertToEntityPos(dest.getY()), World.convertToEntityPixels(6), World.convertToEntityPixels(6));
	}
	
	@Override
	/**
	 * Returns the cost of moving from start to dest
	 */
	public int getMoveCost(Node start, Node dest) {
		int cost = 0;
		
		//Check for diagonal
		int dx = Math.abs(dest.getX() - start.getX());
		int dy = Math.abs(dest.getY() - start.getY());
		if ((dx == 1) && (dy == 1)) {
			cost += PathfindingQueue.MOVEMENTCOST_DIAGONAL;
		}
		else {
			cost += PathfindingQueue.MOVEMENTCOST_HORIZONTAL;
		}
		
		return cost;
	}
	
	@Override
	public boolean isInitialized() {
		return initialized;
	}
}
