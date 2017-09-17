package com.fizzikgames.mastermind.ai.strategy;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import com.fizzikgames.fizlib.pathfinding.Node;
import com.fizzikgames.fizlib.pathfinding.Path;
import com.fizzikgames.mastermind.ai.Job;
import com.fizzikgames.mastermind.ai.PathfindingQueue;
import com.fizzikgames.mastermind.entity.AIControlledCharacter;
import com.fizzikgames.mastermind.entity.Entity;
import com.fizzikgames.mastermind.entity.Item;
import com.fizzikgames.mastermind.world.World;

/**
 * The worker strategy defines a strategy for a worker character who is assigned a completed path and attempts to
 * traverse it's entirety while also completing jobs assigned from an outside JobQueue and PathfindingQueue instance.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class WorkerStrategy implements AIStrategy {
	private AIControlledCharacter source;
	private World world;
	private boolean initialized;
	//Path-finding and jobs
	private Item item; //An item the character might be holding
	private Path path;
	private Node nextNodeOnPath;
	private Job job;
	private int labors[];
	
	public WorkerStrategy() {
		initialized = false;
	}
	
	@Override
	public void run() {
		initialize();
	}
	
	public void initialize() {
		//Job vars
		item = null;
		path = null;
		nextNodeOnPath = null;
		job = null;
		//Initialize Labors
		labors = new int[Job.JOB_COUNT];
		labors[Job.JOB_ANY] = 1;
		
		initialized = true;
	}
	
	@Override
	public void tick(GameContainer gc, int delta) {
		if (isInitialized()) {
			if ((job != null) && !world.isPaused()) {
				if (path != null) {				
					if ((nextNodeOnPath == null) && (path.getSize() > 0)) {
						nextNodeOnPath = path.getNextNode();
					} 
					else if (nextNodeOnPath != null) {
						if (!isValidNode(Entity.getPositionAsNode(source), nextNodeOnPath)) {
							//Reset pathfinding
							path = null;
							nextNodeOnPath = null;
							job.getCurrentTask().setNeedUpdate(true);
						}
						else if (Entity.entityPosEqualsNode(source, nextNodeOnPath)) {
							nextNodeOnPath = null;
						}
						else {
							source.getSprite().current("walk_normal");
							
							double newx = World.convertToEntityPos(nextNodeOnPath.getX());
							double newy = World.convertToEntityPos(nextNodeOnPath.getY());
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
	
							source.doMoves(cx, cy); // Do a move using the previous change in x and y
	
							Vector2f current = new Vector2f((float) source.getX(), (float) source.getY());
							Vector2f dest = new Vector2f((float) newx, (float) newy);
	
							if (current.distance(dest) <= source.getMoves()) {
								source.setX(newx);
								source.setY(newy);
							}
						}
					}
				}
				else {
					source.getSprite().current("stand_still");
				}
				
				if (job != null) {
					if (job.getCurrentTask() != null) job.getCurrentTask().update(); //Updates the task to see if movement is finished
				}
			}
			else {
				source.getSprite().current("stand_still");
			}
		}
	}
	
	/**
	 * Returns if the dest node is valid for the character to move
	 */
	public boolean isValidNode(Node start, Node dest) {
		return !world.movementBlockedAt(World.convertToEntityPos(dest.getX()), World.convertToEntityPos(dest.getY()), source.getWidth(), source.getHeight());
	}
	
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
		
		//Add cost if node is occupied by a friendly character
		if (world.movementImpededAt(source, World.convertToEntityPos(dest.getX()), World.convertToEntityPos(dest.getY()), source.getWidth(), source.getHeight())) {
			cost += PathfindingQueue.MOVEMENTCOST_FRIENDLYIMPEDE;
		}
		
		return cost;
	}

	@Override
	public void setSourceCharacter(AIControlledCharacter character) {
		source = character;
	}
	
	@Override
	public void setSourceWorld(World world) {
		this.world = world;
	}
	
	@Override public boolean isInitialized() {
		return initialized;
	}
	
	public Path getPath() {
		return path;
	}
	
	public void setPath(Path p) {
		path = p;
	}
	
	public Job getJob() {
		return job;
	}
	
	public void setJob(Job j) {
		job = j;
	}
	
	public int[] getLabors() {
		return labors;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setItem(Item i) {
		item = i;
	}
}
