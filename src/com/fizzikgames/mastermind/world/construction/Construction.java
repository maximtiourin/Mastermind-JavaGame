package com.fizzikgames.mastermind.world.construction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

import com.fizzikgames.mastermind.ai.PathfindingQueue;
import com.fizzikgames.mastermind.entity.Entity;
import com.fizzikgames.mastermind.entity.sprite.ConstructionSprite;
import com.fizzikgames.mastermind.world.World;

public abstract class Construction extends Entity {
	public Construction(World world, double width, double height, int render) {
		super(world, width, height, render);
		
		rng = new Random(System.currentTimeMillis());
		
		w = World.convertToScreenPixels(width) / ConstructionSprite.WIDTH;
		h = World.convertToScreenPixels(height) / ConstructionSprite.HEIGHT;
		cells = new ConstructionSprite[h][w];
		collisionMatrix = new boolean[h][w];
		usePoints = new boolean[h][w];
		
		outlinePoly = null;		
		outlineColor = 1;
		outlineColorChange = OUTLINE_COLOR_CHANGE;
		outlinePivots = new ArrayList<Vector2f>();
		
		rotation = 0;
		placed = false;
		inUse = false;
	}
	
	/**
	 * Initializes the sprites in the cells
	 */
	public abstract void initCells();
	public abstract int getType();	
	public abstract Construction getNextType();
	
	public void update(GameContainer gc, int delta) {
		if (!world.isPaused()) {
			if (!placed) {			
				x = World.convertToEntityPos(world.getMousec());
				y = World.convertToEntityPos(world.getMouser());
				
				setRenderPriority(Entity.RENDER_ITEM);
			}
			else {
				setRenderPriority(Entity.RENDER_CONSTRUCTION);
			}
			
			for (ConstructionSprite[] e : cells) {
				for (ConstructionSprite c : e) {
					if (c != null) {
						c.step(delta);
					}
				}
			}
			
			if (outlineColor <= 0) outlineColorChange = -outlineColorChange;
			else if (outlineColor >= 255) outlineColorChange = -outlineColorChange;
			outlineColor += outlineColorChange;
		}
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) {
		
	}
	
	public Image getImage() {
		return null;
	}
	
	@Override
	public void draw(Image scaledImage, float x, float y) {
		scaledImage.draw((float) (x), (float) (y));
	}
	
	public void draw(ConstructionSprite cs, Image scaledImage, float x, float y) {
		scaledImage.setRotation((float) cs.getRotation());
		scaledImage.draw((float) (x), (float) (y));
	}
	
	/**
	 * Draws the entire construction, does this for the worldviewer since it might not have all of the information necessary, like if the construction is being placed.
	 */
	public void draw(Graphics g, double viewportx, double viewporty, int tw, int th, int div) {
		boolean obstructed = false;
		
		for (int r = 0; r < h; r++) {
			for (int c = 0; c < w; c++) {
				ConstructionSprite cell = cells[r][c];
				if (cell != null) {
					/*Image copy = cell.image().getScaledCopy((int) (((double) ConstructionSprite.WIDTH / (double) Tile.WIDTH) * (double) tw), 	 //Draws image with
							(int) (((double) ConstructionSprite.HEIGHT / (double) Tile.HEIGHT) * (double) th)); //correct scale*/
					
					//Image copy = cell.image().getScaledCopy(((ConstructionSprite.WIDTH / Tile.WIDTH) * tw), ((ConstructionSprite.HEIGHT / Tile.HEIGHT) * th));
					Image copy = cell.image().getScaledCopy((float) tw / (float) ConstructionSprite.WIDTH);
					
					if (!placed) {
						//Shade the images to a blueprint type hue since its in placement mode, also shade any invalid placements red						
						copy.setColor(0, 0, 255, 255, 1f);
						copy.setColor(1, 0, 255, 255, 1f);
						copy.setColor(2, 0, 255, 255, 1f);
						copy.setColor(3, 0, 255, 255, 1f);
						
						if (isCellObstructed(r, c)) {
							obstructed = true;
							copy.setColor(0, 255, 0, 0, .50f);
							copy.setColor(1, 255, 0, 0, .50f);
							copy.setColor(2, 255, 0, 0, .50f);
							copy.setColor(3, 255, 0, 0, .50f);
						}
					}
					
					DecimalFormat dcform = new DecimalFormat("##########."); //Rounds the float
					float cRound = Float.valueOf(dcform.format((float) (((x / div) - viewportx + c) * tw)));
					float rRound = Float.valueOf(dcform.format((float) (((y / div) - viewporty + r) * th)));
					
					draw(cell, copy, cRound, rRound); //Draw cell
				}
			}
		}
		
		//Draw outline polygon
		if (!placed) {
			float scalex = (float) tw / (float) div;
			float scaley = (float) th / (float) div;
			
			Shape temp = outlinePoly.transform(Transform.createScaleTransform(scalex, scaley));
			temp = temp.transform(Transform.createRotateTransform((float) ((-rotation * Math.PI) / 180), 0, 0));
			temp.setX((float) ((((x + outlinePivots.get(0).getX()) / div) - viewportx) * tw));
			temp.setY((float) ((((y + outlinePivots.get(0).getY()) / div) - viewporty) * th));
			g.setLineWidth(tw / div);
			if (!obstructed) g.setColor(new Color(outlineColor, 255, 255));
			else g.setColor(new Color(255, outlineColor, outlineColor));
			g.draw(temp);
		}
	}
	
	public boolean isValidPlacement() {
		for (int r = 0; r < h; r++) {
			for (int c = 0; c < w; c++) {
				if (cells[r][c] != null) {
					if (isCellObstructed(r, c)) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if the cell at r,c is obstructed by something (primarily used for placement checking)
	 */
	public boolean isCellObstructed(int r, int c) {
		double w = World.convertToEntityPixels(ConstructionSprite.WIDTH);
		double h = World.convertToEntityPixels(ConstructionSprite.HEIGHT);
		
		if (world.movementBlockedAt(x + World.convertToEntityPos(c), y + World.convertToEntityPos(r), w, h)) {
			return true;
		}
		else if (world.getConstructionMatrix()[World.convertToWorldPos(y) + r][World.convertToWorldPos(x) + c] == World.ConstructionUsePoint) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Rotates the cell matrix in 90 degree increments, ex- increment of 2 rotates 180 degrees
	 */
	public void rotate(int increment) {
		int inc = increment % 4;
		
		for (int i = 0; i < inc; i++) {
			rotateNinetyDegrees();
			rotateOutlinePivots();
		}
		
		for (ConstructionSprite[] e : cells) {
			for (ConstructionSprite c : e) {
				if (c != null) {
					c.setRotation(-rotation);
				}
			}
		}
	}
	
	private void rotateNinetyDegrees() {
		int ww = w;
		int hh = h;
		w = hh;
		h = ww;
		width = World.convertToEntityPos(w);
		height = World.convertToEntityPos(h);
		
		ConstructionSprite newcells[][] = new ConstructionSprite[ww][hh]; //Interchange width and height
		boolean newcollision[][] = new boolean[ww][hh];
		boolean newusepoints[][] = new boolean[ww][hh];
		int rr = 0;
		for (int c = ww - 1, cc = 0; c >= 0; c--, cc = 0) {
			for (int r = 0; r < hh; r++) {
				newcells[rr][cc] = cells[r][c];
				newcollision[rr][cc] = collisionMatrix[r][c];
				newusepoints[rr][cc] = usePoints[r][c];
				cc++;
			}
			rr++;
		}
		
		cells = newcells;
		collisionMatrix = newcollision;
		usePoints = newusepoints;
		rotation += 90;
		if (rotation >= 360) rotation = rotation % 360;
	}
	
	private void rotateOutlinePivots() {
		int pivotPos = outlinePivots.size() - 1;
		Vector2f pivot = outlinePivots.get(pivotPos);
		outlinePivots.remove(pivotPos);
		outlinePivots.add(0, pivot);
	}
	
	/**
	 * Opens the path-finding connection for all collision tiles in this construction (used when construction is removed)
	 */
	public void openPathfinderConnection(PathfindingQueue pf) {
		for (int r = 0; r < h; r++) {
			for (int c = 0; c < w; c++) {
				if (collisionMatrix[r][c]) {
					pf.openNewConnection(World.convertToWorldPos(y) + r, World.convertToWorldPos(x) + c);
				}
			}
		}
	}
	
	/**
	 * Closes the path-finding connection for all collision tiles in this construction (used when construction is placed)
	 */
	public void closePathfinderConnection(PathfindingQueue pf) {
		for (int r = 0; r < h; r++) {
			for (int c = 0; c < w; c++) {
				if (collisionMatrix[r][c]) {
					pf.closeConnection(World.convertToWorldPos(y) + r, World.convertToWorldPos(x) + c);
				}
			}
		}
	}
	
	public ConstructionSprite[][] getCells() {
		return cells;
	}
	
	public boolean[][] getCollisionMatrix() {
		return collisionMatrix;
	}
	
	public ConstructionSprite getCell(int r, int c) {
		return cells[r][c];
	}
	
	public double getRotation() {
		return rotation;
	}
	
	public int w() {
		return w;
	}
	
	public int h() {
		return h;
	}
	
	public boolean[][] getUsePoints() {
		return usePoints;
	}
	
	public boolean isPlaced() {
		return placed;
	}
	
	public void setPlaced(boolean b) {		
		placed = b;
	}
	
	protected Random rng;
	protected ConstructionSprite cells[][];
	protected boolean collisionMatrix[][];
	protected boolean usePoints[][];
	protected Polygon outlinePoly;
	protected ArrayList<Vector2f> outlinePivots; //How much to shift the outline by based on the rotation
	protected int outlineColor;
	protected int outlineColorChange;
	protected double rotation;
	protected int w;
	protected int h;
	protected boolean placed;
	protected boolean inUse;
	private static final int OUTLINE_COLOR_CHANGE = 4;
}
