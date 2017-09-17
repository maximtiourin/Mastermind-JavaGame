package com.fizzikgames.mastermind.entity;

import com.fizzikgames.mastermind.world.World;

/**
 * A controlled entity has an alliance associated with it such that
 * it can tell if another entity is allied, neutral, or hostile to it.
 * @author Maxim Tiourin
 * @version 1.00
 */
public abstract class ControlledEntity extends Entity {
	public static final int PLAYER_NEUTRAL = 0;
	public static final int PLAYER_FRIENDLY = 1;
	public static final int PLAYER_HOSTILE = 2;
	public static final int ZOMBIE = 100;
	private int alliance;
	
	public ControlledEntity(World world, double width, double height, int renderPriority, int alliance) {
		super(world, width, height, renderPriority);
		
		this.alliance = alliance;
	}
	
	/**
	 * Returns the alliance, use static members to determine what kind of alliance it is.
	 */
	public int getAlliance() {
		return alliance;
	}
	
	/**
	 * Returns true if this entity is hostile to the controlled entity
	 */
	public boolean isHostileTo(ControlledEntity e) {
		if (alliance > e.getAlliance()) {
			return true;
		}
		
		return false;
	}
}
