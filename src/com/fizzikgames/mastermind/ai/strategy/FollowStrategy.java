package com.fizzikgames.mastermind.ai.strategy;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;

import com.fizzikgames.fizlib.pathfinding.Node;
import com.fizzikgames.mastermind.ai.PathfindingQueue;
import com.fizzikgames.mastermind.entity.AIControlledCharacter;
import com.fizzikgames.mastermind.entity.Entity;
import com.fizzikgames.mastermind.world.World;

/**
 * The follow strategy defines a strategy for a character that stupidly follows another entity with no intelligent pathfinding.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class FollowStrategy implements AIStrategy, Runnable {
	private AIControlledCharacter source;
	private Entity target;
	private World world;
	private boolean initialized;
	
	public FollowStrategy() {
		initialized = false;
	}
	
	@Override
	public void run() {
		initialize();
	}
	
	public void initialize() {		
		initialized = true;
	}
	
	public void tick(GameContainer gc, int delta) {
		if (isInitialized()) {
			source.getSprite().current("stand_still");
			
			if (hasTarget()) {
				Node destnode = Entity.getPositionAsNode(target);
				
				double oldx = source.getX();
				double oldy = source.getY();
				double newx = World.convertToEntityPos(destnode.getX());
				double newy = World.convertToEntityPos(destnode.getY());
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
				
				source.updateRotation(oldx, oldy, target.getX(), target.getY());
			}
		}
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
	
	public boolean hasTarget() {
		return target != null;
	}
	
	public void setTarget(Entity e) {
		target = e;
	}
}
