package com.fizzikgames.mastermind.world;

import java.awt.Point;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

import com.fizzikgames.fizlib.pathfinding.Node;
import com.fizzikgames.mastermind.GameLogic;
import com.fizzikgames.mastermind.ai.PathfindingQueue.Cell;
import com.fizzikgames.mastermind.asset.AssetLoader;
import com.fizzikgames.mastermind.entity.AIControlledCharacter;
import com.fizzikgames.mastermind.entity.Character;
import com.fizzikgames.mastermind.entity.Entity;
import com.fizzikgames.mastermind.entity.sprite.ConstructionSprite;
import com.fizzikgames.mastermind.world.construction.Construction;

@SuppressWarnings("unused")
public class WorldViewer implements Runnable {
	//Main
	private Random rng;
	private World world;
	private double row;
	private double column;
	//Matrices
	private short rotMatrix[][];
	//Helpful Flags
	private boolean init;
	//Mouse variables
	private int mousex;
	private int mousey;
	private int mouser;
	private int mousec;
	//Zooming Variables
	private static final int ZOOMS = 3;
	private static final int ZOOM_0 = 8; //Smallest Zoom
	private static final int ZOOM_1 = 16; //Default
	private static final int ZOOM_2 = 32; //Default
	private int zoom_state[];
	private int zoom_level;
	//Panning Variables
	private boolean panning;
	private double pan_destr;
	private double pan_destc;
	private double pan_speed;
	//Follow Variables
	private boolean followLock;
	private Entity followEntity;
	
	public WorldViewer(World world, int r, int c) {
		rng = new Random(System.currentTimeMillis()); //Seed the random number generator
		this.world = world;
		row = r;
		column = c;
		//Matrices
		rotMatrix = new short[Level.HEIGHT][Level.WIDTH];
		//Helpful Flags
		init = true;
		//Mouse Variables
		mousex = 0;
		mousey = 0;
		mouser = 0;
		mousec = 0;
		//Zooming Variables
		zoom_state = new int[ZOOMS];
		zoom_state[0] = ZOOM_0;
		zoom_state[1] = ZOOM_1;
		zoom_state[2] = ZOOM_2;
		zoom_level = 2;
		//Panning Variables
		panning = false;
		//Follow Variables
		followLock = false;
		followEntity = null;
	}
	
	public void run() {
		
	}
	
	public void tick(GameContainer gc, int delta) {
		//Helper Variables/////////////////////////////////////////////////////////
		int tw = zoom_state[zoom_level];
		int th = zoom_state[zoom_level];
		int div = Tile.DIVISIONS;
		int halfWidth = (int) Math.floor((GameLogic.WINDOWED_WIDTH / 2) / tw);
		int halfHeight = (int) Math.floor((GameLogic.WINDOWED_HEIGHT / 2) / th);
		int extraR = 2; //extra row flag
		int extraC = 2; //extra column flag
		//if (row % 1 > 0) extraR++; //Checks if we need an extra row
		//if (column % 1 > 0) extraC++; //Check if we need an extra column
		//if ((row % 2 > 0) && (extraR > 0)) extraR++; //Checks if we need an extra row
		//if ((column % 2 > 0) && (extraC > 0)) extraC++; //Check if we need an extra column
		double viewportx = Math.max(column - halfWidth, 0); //Constrains viewportx to not go below 0
		double viewporty = Math.max(row - halfHeight, 0); //Constrains viewporty to not go below 0
		double viewportw = Math.min((column + halfWidth + extraC) - viewportx, Level.WIDTH - viewportx - 1); //Constrains viewportw to not go past level width - viewportx
		double viewporth = Math.min((row + halfHeight + extraR) - viewporty, Level.HEIGHT - viewporty - 1); //Constrains viewporth to not go past level height - viewporty
		//////////////////////////////////////////////////////////////////////////////
		//Rotation Matrix/////////////////////////////////////////////////////////////
		if (init) {
			//Determine Rotation Matrix	
			for (int r = 0; r < Level.HEIGHT; r++) {
				for (int c = 0; c < Level.WIDTH; c++) {
					if (Tile.shouldRotate(world.getLevel().getTile(r, c))) {
						rotMatrix[r][c] = (short) (rng.nextInt(4) * 90);
					} else {
						rotMatrix[r][c] = (short) 0;
					}
				}
			}
			
			init = false;
		}
		//////////////////////////////////////////////////////////////////////////////
		
		if (panning) {
			pan(); //Pan the camera a step if we are currently panning
		}
		else if (followLock) {
			follow();
		}
		constrain(); //Constrains the viewer position to stay within the level
		
		//Update Mouse
		mousex = gc.getInput().getMouseX();
		mousey = gc.getInput().getMouseY();
		mouser = (int) Math.floor((double) viewporty + (mousey / (double) th));
		mousec = (int) Math.floor((double) viewportx + (mousex / (double) tw));
		mouser = Math.min(mouser, Level.HEIGHT - 1);
		mouser = Math.max(mouser, 0);
		mousec = Math.min(mousec, Level.WIDTH - 1);
		mousec = Math.max(mousec, 0);
	}
	
	public void drawScene(Graphics g) {		
		int tw = zoom_state[zoom_level];
		int th = zoom_state[zoom_level];
		int div = Tile.DIVISIONS;
		int halfWidth = (int) Math.floor((GameLogic.WINDOWED_WIDTH / 2) / tw);
		int halfHeight = (int) Math.floor((GameLogic.WINDOWED_HEIGHT / 2) / th);
		int extraR = 3; //extra row flag
		int extraC = 2; //extra column flag
		double viewportx = Math.max(column - halfWidth, 0); //Constrains viewportx to not go below 0
		double viewporty = Math.max(row - halfHeight, 0); //Constrains viewporty to not go below 0
		double viewportw = Math.min((column + halfWidth + extraC) - viewportx, Level.WIDTH - viewportx - 1); //Constrains viewportw to not go past level width - viewportx
		double viewporth = Math.min((row + halfHeight + extraR) - viewporty, Level.HEIGHT - viewporty - 1); //Constrains viewporth to not go past level height - viewporty
		
		Level level = world.getLevel();
		//Draw All Tile Images
		int vpr = (int) Math.floor(viewporty);
		int vpc = (int) Math.floor(viewportx);
		int vpw = (int) Math.floor(viewportw);
		int vph = (int) Math.floor(viewporth);
		g.setColor(Color.white);
		
		for (int r = vpr; r <= vpr + vph; r++) {
			for (int c = vpc; c <= vpc + vpw; c++) {
				short tileid = Tile.getTileId(level.getTile(r, c));
				Point tilePos = Tile.getSpriteSheetPosition(tileid);
				//Image scaledTile = level.getTileSet().getSprite((int) tilePos.getX(), (int) tilePos.getY()).getScaledCopy(tw, th);
				Image scaledTile = level.getTileSet().getScaledSprite(tilePos, tw, th);
				//Tiles
				scaledTile.setRotation((float) rotMatrix[r][c]);
				
				DecimalFormat dcform = new DecimalFormat("##########."); //Rounds the float
				float cRound = Float.valueOf(dcform.format((float) ((c - viewportx) * tw)));
				float rRound = Float.valueOf(dcform.format((float) ((r - viewporty) * th)));
				
				scaledTile.draw(cRound, rRound);
				//System.out.println("Draw Coords: C=[" + (float) ((c - viewportx) * tw) + "] R=[" + (float) ((r - viewporty) * th) + "]");
				//Tile Walls
				if (Tile.isWall(tileid)) {
					//Create Wall matrix
					int[][] nearbyWalls = new int[3][3];
					nearbyWalls[1][1] = 1;
					int nwoffset = 1;
					for (int wr = -1; wr <= 1; wr++) {
						for (int wc = -1; wc <= 1; wc++) {
							int newr = r + wr;
							int newc = c + wc;
							//if (!((wr == 0) && (wr == 0))) {
								//Check Bounds
								if (((newr >= 0) && (newr < Level.HEIGHT)) && ((newc >= 0) && (newc < Level.WIDTH))) {
									if (Tile.isWall(Tile.getTileId(level.getTile(newr, newc)))) {
										nearbyWalls[nwoffset + wr][nwoffset + wc] = 1;
									}
									else {
										nearbyWalls[nwoffset + wr][nwoffset + wc] = 0;
									}
								}
							//}
						}
					}
					
					//Check Wall Type
					int wallType = Tile.getWallType(nearbyWalls);
					if (wallType > 0) {
						tilePos = Tile.getSpriteSheetPosition(wallType);
						Image wallImage = level.getExtras().getSprite((int) tilePos.getX(), (int) tilePos.getY()).getScaledCopy(tw, th);
						wallImage.draw(cRound, rRound);
					}
				}
				
				//Debug draw connected tiles
				/*Cell dtest[][] = world.getPathfinder().DEBUG();
				g.setColor(Color.white);
				if (dtest[r][c] != null)
				g.drawString(dtest[r][c].getIdentifier() + "", (float) ((c - viewportx) * tw), (float) ((r - viewporty) * th));*/
			}
		}
		
		//Draw All Entities
		ArrayList<Entity> entities = world.getEntitiesInArea(World.convertToEntityPos(viewporty - 2), World.convertToEntityPos(viewportx - 2), World.convertToEntityPos(viewportw + 4), World.convertToEntityPos(viewporth + 4));
		Collections.sort(entities, new Entity.RenderPriorityComparator(false)); //Sorts the entities based on their render priorities
		for (Entity e : entities) {
			if (e instanceof Construction) {
				((Construction) e).draw(g, viewportx, viewporty, tw, th, div);
			}
			else {
				Image copy = e.getImage().getScaledCopy((int) (((double) World.convertToScreenPixels(e.getWidth()) / (double) Tile.WIDTH) * (double) tw), 	 //Draws image with
														(int) (((double) World.convertToScreenPixels(e.getHeight()) / (double) Tile.HEIGHT) * (double) th)); //correct scale
				copy.setAlpha(e.getImage().getAlpha());
				e.draw(copy, (float) (((e.getX() / div) - viewportx) * tw), (float) (((e.getY() / div) - viewporty) * th));
			}
			
			//Debug draw entity pos
			//g.setColor(Color.red);
			//g.fillOval((float) ((((e.getX() / div) - viewportx) * tw) - 1), (float) ((((e.getY() / div) - viewporty) * th) - 1), 2, 2);
			
			//Debug line of sight
			//Test Line of Sight
			/*if (e instanceof Character) {
				if (e != world.getCurrentPlayerInputFocus()) {
					Entity src = e;
					Entity dest = world.getCurrentPlayerInputFocus();
					if (world.hasLineOfSight(src, dest, 15)) {
						g.drawLine((float) ((((src.getX() + (src.getWidth() / 2)) / div) - viewportx) * tw), ((float) (((src.getY() + (src.getHeight() / 2)) / div) - viewporty) * th), (float) ((((dest.getX() + (dest.getWidth() / 2)) / div) - viewportx) * tw), (float) ((((dest.getY() + (dest.getHeight() / 2)) / div) - viewporty) * th));
					}
					
					double x0 = 0, y0 = 0, x1 = 0, y1 = 0;
					if (src.getX() < dest.getX()) {
						x0 = src.getX();
						x1 = dest.getX() + dest.getWidth();
					}
					else {
						x0 = dest.getX();
						x1 = src.getX() + src.getWidth();
					}
					if (src.getY() < dest.getY()) {
						y0 = src.getY();
						y1 = dest.getY() + dest.getHeight();
					}
					else {
						y0 = dest.getY();
						y1 = src.getY() + src.getHeight();
					}
					
					g.setColor(Color.blue);
					//g.drawRect((float) ((((x0) / div) - viewportx) * tw), (float) ((((y0) / div) - viewporty) * th), (float) (((x1 - x0) / div) * tw), (((float) (y1 - y0) / div) * th));
				}
				
			}*/
		}
		
		if (world.worldHasFocus()) {
			g.setColor(Color.red);
			g.setLineWidth(1);
			g.drawRect((float) ((mousec - viewportx) * tw), (float) ((mouser - viewporty) * th), (float) (tw), (float) (th));
		}
		
		//Draw pause test
		if (world.isPaused()) {
			g.setColor(Color.black);
			g.drawString("Paused", (GameLogic.WINDOWED_WIDTH / 2), (GameLogic.WINDOWED_HEIGHT / 2));
			g.drawString("Paused", (GameLogic.WINDOWED_WIDTH / 2) + 2, (GameLogic.WINDOWED_HEIGHT / 2) + 2);
			g.setColor(Color.white);
			g.drawString("Paused", (GameLogic.WINDOWED_WIDTH / 2) + 1, (GameLogic.WINDOWED_HEIGHT / 2) + 1);
		}
	}
	
	/**
	 * Pans the viewer to the position at speed
	 */
	public void panView(double r, double c, double speed) {
		pan_destr = r;
		pan_destc = c;
		pan_speed = speed;
		panning = true;
	}
	
	/**
	 * Do the actual pan
	 */
	public void pan() {
		
	}
	
	/**
	 * Do the follow lock operations
	 */
	public void follow() {
		row = World.convertToPreciseWorldPos(followEntity.getY());
		column = World.convertToPreciseWorldPos(followEntity.getX());		
	}
	
	/**
	 * Sets the follow target and enables camera locked following.
	 * @param e the entity to follow, null will cancel following
	 */
	public void setFollowTarget(Entity e)
	{
		if (e != null) {
			followEntity = e;
			followLock = true;
		}
		else {
			followEntity = null;
			followLock = false;
		}
	}
	
	/**
	 * Constrains the cameras position so it displays the tiles properly
	 */
	public void constrain() {
		int tw = zoom_state[zoom_level];
		int th = zoom_state[zoom_level];
		int halfWidth = (int) Math.floor((GameLogic.WINDOWED_WIDTH / 2) / tw);
		int halfHeight = (int) Math.floor((GameLogic.WINDOWED_HEIGHT / 2) / th);
		
		row = Math.max(halfHeight, row);
		row = Math.min(row, Level.HEIGHT - 1 - halfHeight);
		column = Math.max(halfWidth, column);
		column = Math.min(column, Level.WIDTH - 1 - halfWidth);
	}
	
	/**
	 * Zooms the view finder in our out, effectively changing tile width and height when drawing
	 */
	public void zoom(int sign) {
		int add = sign / Math.abs(sign);
		zoom_level += add;
		zoom_level = Math.max(0, zoom_level);
		zoom_level = Math.min(zoom_level, ZOOMS - 1);
	}
	
	/**
	 * Recalculates the random rotation value at r,c in the matrix, also checking if the tile should be rotated at all.
	 */
	public void recalcRotMatrixAt(int r, int c) {
		if (Tile.shouldRotate(world.getLevel().getTile(r, c))) {
			rotMatrix[r][c] = (short) (rng.nextInt(4) * 90);
		} else {
			rotMatrix[r][c] = (short) 0;
		}
	}
	
	public void adjustR(double amount) {
		row += amount;
	}
	
	public void adjustC(double amount) {
		column += amount;
	}
	
	public double getRow() {
		return row;
	}
	
	public double getColumn() {
		return column;
	}
	
	public int getMouser() {
		return mouser;
	}
	
	public int getMousec() {
		return mousec;
	}
	
	public int getMousex() {
		return mousex;
	}
	
	public int getMousey() {
		return mousey;
	}
}
