package com.fizzikgames.mastermind.ai.strategy;

import org.newdawn.slick.GameContainer;

import com.fizzikgames.fizlib.pathfinding.Node;
import com.fizzikgames.mastermind.entity.AIControlledCharacter;
import com.fizzikgames.mastermind.world.World;

/**
 * An AI Strategy defines a class which operates on AIControlled characters.
 * @author Maxim Tiourin
 * @version 1.00
 */
public interface AIStrategy extends Runnable {
	@Override
	public void run();
	/**
	 * Initializes the Strategy, this should be called in the source character run() method.
	 */
	public void initialize();
	/**
	 * Updates a single tick of logic for the strategy
	 */
	public void tick(GameContainer gc, int delta);
	/**
	 * Sets the AI character the strategy is operating on.
	 */
	public void setSourceCharacter(AIControlledCharacter character);
	/**
	 * Sets the AI character's world the strategy is operating in.
	 */
	public void setSourceWorld(World world);
	/**
	 * Returns if the dest node is valid for the source character to move
	 */
	public boolean isValidNode(Node start, Node dest);
	/**
	 * Returns the cost of moving from start to dest
	 */
	public int getMoveCost(Node start, Node dest);
	/**
	 * Should return whether or not this strategy has been initialized, this
	 * should block any updates being done in the tick method until it is true.
	 */
	public boolean isInitialized();
}
