package com.fizzikgames.mastermind.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.fizzikgames.mastermind.entity.sprite.CharacterSprite;
import com.fizzikgames.mastermind.world.World;

public abstract class Character extends ControlledEntity {
	protected CharacterSprite sprite;
	protected double rotation;
	protected double baseSpeed;
	protected double moves;
	//Static
	public static final int SECOND = 1000; //milliseconds in second
	public static final double BASE_MOVEMENTSPEED = World.convertToEntityPos(2.5); //Move 2.5 tiles a second
	public static final double DIAGONAL = 0.707106781; //How much to adjust position if moving diagonally
	public static final int IMAGE_ROTATION_OFFSET = -90; //offset of character images initial rotation, which will be at 90 degrees
	
	public Character(World world, String spritesheet, double width, double height, double baseSpeed, int renderPriority, int alliance) {
		super(world, width, height, renderPriority, alliance);
		
		this.sprite = new CharacterSprite(spritesheet);
		this.rotation = 0;
		this.baseSpeed = baseSpeed;
	}
	
	public void update(GameContainer gc, int delta) {
		//Calculate movespeed based on delta
		setMoves(baseSpeed, delta);
	}
	
	public void render(GameContainer gc, Graphics g) {
		
	}
	
	/**
	 * Does movement unless blocked, also updates rotations
	 */
	public void doMoves(double x, double y) {
		double ax = x;
		double ay = y;
		double moves = this.moves;
		double oldx = this.x;
		double oldy = this.y;
		double newx;
		double newy;
		
		//Test for wall while decrementing moves until we have a snug fit
		while (moves > 0) {
			boolean didMove = false;
			newx = this.x + (ax * moves);
			newy = this.y + (ay * moves);
			if (!world.movementBlockedAt(newx, oldy, width, height)) {
				this.x = newx;				
				didMove = true;
			}
			if (!world.movementBlockedAt(oldx, newy, width, height)) {
				this.y = newy;
				didMove = true;
			}

			if (didMove) moves = 0;
			else moves -= 0.01;
		}
		
		updateRotation(oldx, oldy, this.x, this.y);
	}
	
	/**
	 * Does movement unless blocked or impeded, but has a special case where being impeded is allowed, does not update rotation
	 */
	public void doMovesImpede(double x, double y) {
		if (!((x == 0) && (y == 0))) {			
			double ax = x;
			double ay = y;		
			double moves = this.moves;
			double oldx = this.x;
			double oldy = this.y;
			double newx;
			double newy;
			
			//Test for wall while decrementing moves until we have a snug fit
			while (moves > 0) {			
				boolean didMove = false;
				newx = this.x + (ax * moves);
				newy = this.y + (ay * moves);
				if (!world.movementBlockedAt(newx, oldy, width, height) && (!world.movementImpededAt(this, newx, oldy, width, height) || world.movementImpededAt(this, oldx, oldy, width, height))) {
					this.x = newx;	
					didMove = true;
				}
				if (!world.movementBlockedAt(oldx, newy, width, height) && (!world.movementImpededAt(this, oldx, newy, width, height) || world.movementImpededAt(this, oldx, oldy, width, height))) {
					this.y = newy;
					didMove = true;
				}
	
				if (didMove) moves = 0;
				else moves -= 0.01;
			}
		}
	}
	
	/**
	 * Does movement unless blocked or impeded, does not update rotation
	 */
	public void doMovesStrictImpede(double x, double y) {
		if (!((x == 0) && (y == 0))) {			
			double ax = x;
			double ay = y;		
			double moves = this.moves;
			double oldx = this.x;
			double oldy = this.y;
			double newx;
			double newy;
			
			//Test for wall while decrementing moves until we have a snug fit
			while (moves > 0) {			
				boolean didMove = false;
				newx = this.x + (ax * moves);
				newy = this.y + (ay * moves);
				if (!world.movementBlockedAt(newx, oldy, width, height) && (!world.movementImpededAt(this, newx, oldy, width, height))) {
					this.x = newx;	
					didMove = true;
				}
				if (!world.movementBlockedAt(oldx, newy, width, height) && (!world.movementImpededAt(this, oldx, newy, width, height))) {
					this.y = newy;
					didMove = true;
				}
	
				if (didMove) moves = 0;
				else moves -= 0.01;
			}
		}
	}
	
	/**
	 * Changes the character's rotation to the angle between the two sets of points if they are not both equal.
	 */
	public void updateRotation(double oldx, double oldy, double newx, double newy) {
		if (!((oldx == newx) && (oldy == newy))) {
			double dx = oldx - newx;
			double dy = oldy - newy;
			rotation = (Math.atan2(dy, dx) * (180 / Math.PI)) + IMAGE_ROTATION_OFFSET;
		}
	}
	
	/**
	 * Increases current rotation by the given amount
	 */
	public void updateRotation(double amount) {
		rotation += amount;
	}
	
	@Override
	public void draw(Image scaledImage, float x, float y) {
		scaledImage.setRotation((float) rotation);
		scaledImage.draw((float) (x), (float) (y));
	}
	
	@Override
	/**
	 * Returns the currently displayed image in this character's sprite.
	 */
	public Image getImage() {
		return sprite.image();
	}
	
	/**
	 * Returns the sprite associated with this character.
	 */
	public CharacterSprite getSprite() {
		return sprite;
	}
	
	public double getMoves() {
		return moves;
	}
	
	/**
	 * Sets the current movement amount based on the delta, this will also
	 * adjust how fast a sprite animation plays.
	 */
	public void setMoves(double moves, int delta) {
		this.moves = moves * ((double) delta / (double) SECOND);
		sprite.updateSpeed((this.moves * (SECOND / delta)) / BASE_MOVEMENTSPEED);
	}
}
