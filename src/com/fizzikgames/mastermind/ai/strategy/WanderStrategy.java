package com.fizzikgames.mastermind.ai.strategy;

import org.newdawn.slick.GameContainer;

import com.fizzikgames.fizlib.pathfinding.Node;
import com.fizzikgames.mastermind.ai.PathfindingQueue;
import com.fizzikgames.mastermind.entity.AIControlledCharacter;
import com.fizzikgames.mastermind.world.World;

/**
 * The wander strategy defines a strategy which makes the source entity
 * wander off in random directions, and stand still for random periods of time.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class WanderStrategy implements AIStrategy, Runnable {
	private AIControlledCharacter source;
	private World world;
	private boolean initialized;
	
	public WanderStrategy() {
		initialized = false;
	}

	@Override
	public void run() {
		initialize();
	}

	@Override
	public void initialize() {		
		initialized = true;
	}

	@Override
	public void tick(GameContainer gc, int delta) {
		if (isInitialized()) {
			source.getSprite().current("stand_still");
			//Do nothing for now
			source.updateRotation(1);
		}
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
