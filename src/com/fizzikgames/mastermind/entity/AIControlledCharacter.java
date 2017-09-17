package com.fizzikgames.mastermind.entity;

import org.newdawn.slick.GameContainer;

import com.fizzikgames.fizlib.pathfinding.Mover;
import com.fizzikgames.fizlib.pathfinding.Node;
import com.fizzikgames.mastermind.ai.strategy.AIStrategy;
import com.fizzikgames.mastermind.world.World;

/**
 * An ai controlled character is a character that is manipulated by an AIStrategy.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class AIControlledCharacter extends Character implements Runnable, Mover {
	private AIStrategy ai;
	
	public AIControlledCharacter(World world, String spritesheet, double width, double height, double moves, int alliance, AIStrategy ai) {
		super(world, spritesheet, width, height, moves, RenderableGameObject.RENDER_CHARACTER, alliance);
		this.ai = ai;
	}
	
	//Initialize AI Strategy in thread
	public void run() {
		ai.setSourceWorld(world);
		ai.setSourceCharacter(this);
		new Thread(ai).start();
	}
	
	@Override
	public void update(GameContainer gc, int delta) {
		if (!world.isPaused()) {
			super.update(gc, delta);
			
			ai.tick(gc, delta);
			
			sprite.step(delta);
		}
	}
	
	/**
	 * Returns if the dest node is valid for the character to move
	 */
	public boolean isValidNode(Node start, Node dest) {
		return ai.isValidNode(start, dest);
	}
	
	/**
	 * Returns the cost of moving from start to dest
	 */
	public int getMoveCost(Node start, Node dest) {
		return ai.getMoveCost(start, dest);
	}
	
	/**
	 * Returns the ai strategy being used to control this character.
	 */
	public AIStrategy getAIStrategy() {
		return ai;
	}
}
