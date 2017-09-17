package com.fizzikgames.mastermind.entity;

import java.util.Comparator;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;

import com.fizzikgames.fizlib.pathfinding.Node;
import com.fizzikgames.mastermind.world.World;

/**
 * An entity is the base class for all objects existing in the game world.
 * @author Maxim Tiourin
 * @version 1.00
 */
public abstract class Entity extends RenderableGameObject {
	protected World world;
	protected Image image;
	protected double x;
	protected double y;
	protected double width;
	protected double height;
	public static final float STACK_TRANSPARENCY = .75f;
	
	public Entity(World world, double width, double height, int renderPriority) {
		super(renderPriority);
		
		this.world = world;
		this.image = null;
		this.width = width;
		this.height = height;
	}
	
	public abstract void update(GameContainer gc, int delta);
	public abstract void draw(Image scaledImage, float x, float y);
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getWidth() {
		return width;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	/**
	 * Checks if the entities coordinates are equal to the nodes coordinates, using the entity coordinate system
	 */
	public static boolean entityPosEqualsNode(Entity e, Node comp) {
		if (e.getX() == World.convertToEntityPos(comp.getX())) {
			if (e.getY() == World.convertToEntityPos(comp.getY())) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns the distance between the two entities
	 */
	public static double getDistanceBetweenEntities(Entity a, Entity b) {
		return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
	}
	
	/**
	 * Returns this entities position as a node
	 */
	public static Node getPositionAsNode(Entity e) {
		return new Node(null, World.convertToWorldPos(e.getX()), World.convertToWorldPos(e.getY()));
	}
	
	/**
	 * Compares two entities based on their render priority
	 * @author Maxim Tiourin
	 * @version 1.00
	 */
	public static class RenderPriorityComparator implements Comparator<Entity>{
		private boolean increasing;
		
		public RenderPriorityComparator(boolean increasing)
		{
			this.increasing = increasing; 
		}
		
		@Override
		/**
		 * Compares entities based on their render priority, either in increasing, or decreasing order
		 */
		public int compare(Entity a, Entity b) {
			if (increasing) return a.getRenderPriority() - b.getRenderPriority();
			else return b.getRenderPriority() - a.getRenderPriority();
		}		
	}
}
