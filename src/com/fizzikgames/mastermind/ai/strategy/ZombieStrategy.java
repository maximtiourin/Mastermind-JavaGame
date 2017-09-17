package com.fizzikgames.mastermind.ai.strategy;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;

import com.fizzikgames.fizlib.pathfinding.Node;
import com.fizzikgames.mastermind.entity.AIControlledCharacter;
import com.fizzikgames.mastermind.entity.Entity;
import com.fizzikgames.mastermind.world.World;

/**
 * This is a super strategy that defines a zombie follow strategy which will wander aimlessly,
 * if it attains line of sight of a player within a certain range it will use 
 * a stupid follow strategy towards the player, if line of sight breaks it will use
 * an intelligent pathfinding strategy towards the players last known location, if it never regains
 * line of sight after the pathfind, it reverts to a wander strategy.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class ZombieStrategy implements AIStrategy, Runnable {
	private static final double FOLLOW_BREAKOFF_DISTANCE = 30; //How much distance is allowed to be made before reverting to wander instead of stupidfollow.
	private AIControlledCharacter source;
	private Entity target;
	private World world;
	private AIStrategy strat_wander;
	private AIStrategy strat_stupidFollow;
	private AIStrategy strat_pathfind;
	private AIStrategy currentStrategy;
	private Node lastKnownTargetLocation;
	private double sightRadius;
	private boolean initialized;
	
	public ZombieStrategy(double sightRadius) {
		this.sightRadius = sightRadius;
		
		initialized = false;
	}
	
	@Override
	public void run() {		
		initialize();
	}

	@Override
	public void initialize() {
		lastKnownTargetLocation = null;
		
		strat_wander = new WanderStrategy();
		strat_wander.setSourceCharacter(source);
		strat_wander.setSourceWorld(world);
		strat_stupidFollow = new FollowStrategy();
		strat_stupidFollow.setSourceCharacter(source);
		strat_stupidFollow.setSourceWorld(world);
		strat_pathfind = new PathfindStrategy((int) Math.floor(sightRadius));
		strat_pathfind.setSourceCharacter(source);
		strat_pathfind.setSourceWorld(world);
		
		new Thread(strat_wander).start();
		new Thread(strat_stupidFollow).start();
		new Thread(strat_pathfind).start();
		
		currentStrategy = strat_wander;
		
		initialized = true;
	}

	@Override
	public void tick(GameContainer gc, int delta) {
		if (isInitialized()) {			
			if (currentStrategy == strat_wander) {
				wander(gc, delta);
			}
			else if (currentStrategy == strat_stupidFollow) {
				stupidFollow(gc, delta);
			}
			else if (currentStrategy == strat_pathfind) {
				pathfind(gc, delta);
			}
		}
	}
	
	/**
	 * Returns the first entity in a list of available entities that is a valid target for this strategy.
	 */
	public Entity getValidTargetInRange() {
		ArrayList<Entity> targets = world.getEntitiesInRadiusWithHostileAlliance(source, source.getY(), source.getX(), World.convertToEntityPos(sightRadius), World.convertToEntityPos(sightRadius));
		
		if (targets.size() > 0)	return targets.get(0);
		return null;
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
	public boolean isValidNode(Node start, Node dest) {
		return currentStrategy.isValidNode(start, dest);
	}

	@Override
	public int getMoveCost(Node start, Node dest) {
		return currentStrategy.getMoveCost(start, dest);
	}

	@Override
	public boolean isInitialized() {
		return initialized;
	}
	
	/**
	 * Do the strat_wander logic
	 */
	private void wander(GameContainer gc, int delta) {
		boolean doWander = true;
		
		//Check if a valid target is in range
		target = getValidTargetInRange();
		if (target != null) {
			//Have target in range, check if we have line of sight
			if (world.hasLineOfSight(source, target, sightRadius)) {
				//Have line of sight, start stupid follow
				currentStrategy = strat_stupidFollow;
				
				((FollowStrategy) strat_stupidFollow).setTarget(target);
				
				lastKnownTargetLocation = Entity.getPositionAsNode(target);
				
				doWander = false;
			}
		}
		
		//Do wandering
		if (doWander) strat_wander.tick(gc, delta);
	}
	
	/**
	 * Do the strat_stupidFollow logic
	 */
	private void stupidFollow(GameContainer gc, int delta) {
		boolean doStupidFollow = true;
		
		//Check if have target (error check)
		if (target != null) {
			//Check if we lost line of sight
			if (!world.hasLineOfSight(source, target, sightRadius)) {
				//Lost line of sight, start pathfind
				currentStrategy = strat_pathfind;
				
				//Set pathfinding and cleanup
				((PathfindStrategy) strat_pathfind).pathfind(lastKnownTargetLocation);
				
				((FollowStrategy) strat_stupidFollow).setTarget(null);
				lastKnownTargetLocation = null;
				
				doStupidFollow = false;
			}
			else {
				//Update last known location
				lastKnownTargetLocation = Entity.getPositionAsNode(target);
			}
		}
		
		//Do stupidFollow
		if (doStupidFollow) strat_stupidFollow.tick(gc, delta);
	}
	
	/**
	 * Do the strat_pathfind logic
	 */
	private void pathfind(GameContainer gc, int delta) {
		boolean doPathfind = true;
		
		//Check if we have spotted the target yet
		if (world.hasLineOfSight(source, target, sightRadius)) {
			//Have line of sight, start stupid follow
			currentStrategy = strat_stupidFollow;
			
			((FollowStrategy) strat_stupidFollow).setTarget(target);
			
			lastKnownTargetLocation = Entity.getPositionAsNode(target);
			
			//End pathfinding
			((PathfindStrategy) strat_pathfind).endPathfinding();
			
			doPathfind = false;
		}
		else {
			//Not spotted target yet, check if pathfinding is done.
			if (!((PathfindStrategy) strat_pathfind).isPathfinding()) {
				if (Entity.getDistanceBetweenEntities(source, target) > FOLLOW_BREAKOFF_DISTANCE) {
					//Pathfinding is done, still havent found target, revert to wander
					currentStrategy = strat_wander;
				
					target = null;				
				}
				else {
					//Pathfinding is done, target not in line of sight, but so close that wandering doesnt make sense
					currentStrategy = strat_stupidFollow;
					
					((FollowStrategy) strat_stupidFollow).setTarget(target);
					
					lastKnownTargetLocation = Entity.getPositionAsNode(target);
				}
				
				doPathfind = false;				
			}
		}
		
		if (doPathfind) strat_pathfind.tick(gc, delta);
	}
}
