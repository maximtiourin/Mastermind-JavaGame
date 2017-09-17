package com.fizzikgames.mastermind.world;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.ListIterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Rectangle;

import com.fizzikgames.fizlib.pathfinding.Mover;
import com.fizzikgames.fizlib.pathfinding.Node;
import com.fizzikgames.fizlib.pathfinding.TileMap;

import com.fizzikgames.mastermind.Controls;
import com.fizzikgames.mastermind.Event;
import com.fizzikgames.mastermind.GameLogic;
import com.fizzikgames.mastermind.Renderable;
import com.fizzikgames.mastermind.ai.Director;
import com.fizzikgames.mastermind.ai.Job;
import com.fizzikgames.mastermind.ai.PathfindingQueue;
import com.fizzikgames.mastermind.ai.Task;
import com.fizzikgames.mastermind.ai.strategy.FollowStrategy;
import com.fizzikgames.mastermind.ai.strategy.PathfindStrategy;
import com.fizzikgames.mastermind.ai.strategy.WorkerStrategy;
import com.fizzikgames.mastermind.ai.strategy.ZombieStrategy;
import com.fizzikgames.mastermind.entity.AIControlledCharacter;
import com.fizzikgames.mastermind.entity.Character;
import com.fizzikgames.mastermind.entity.ControlledEntity;
import com.fizzikgames.mastermind.entity.Entity;
import com.fizzikgames.mastermind.entity.Item;
import com.fizzikgames.mastermind.entity.Player;
import com.fizzikgames.mastermind.entity.PlayerCharacter;
import com.fizzikgames.mastermind.world.construction.CStruct_Desk01;
import com.fizzikgames.mastermind.world.construction.CStruct_ExecutiveDesk01;
import com.fizzikgames.mastermind.world.construction.Construction;

public class World extends Event implements Runnable, Renderable, TileMap {
	private Character testCharacter;
	private AIControlledCharacter pathfindtestchar;
	
	private WorldViewer viewer;
	private Level level;
	private Player player;
	private Construction placement;
	private int constructionMatrix[][];
	private ArrayList<Character> characters;
	private ArrayList<Item> items;
	private ArrayList<Construction> constructions;
	private ArrayList<Entity> entities;
	private boolean runOnce;
	private Director director;
	//pause control
	private boolean paused;
	//player vars
	private Character currentPlayerInputFocus;
	//ui
	private boolean showGui;
	//statics
	public static final int ConstructionEmptyPoint = 0;
	public static final int ConstructionUsePoint = 1;
	public static final int ConstructionBlockPoint = 2;
	
	public World() {		
		viewer = new WorldViewer(this, 50, 50);
		level = new Level("tileset_001", "tileset_extras");
		characters = new ArrayList<Character>();
		items = new ArrayList<Item>();
		constructions = new ArrayList<Construction>();
		entities = new ArrayList<Entity>();
		runOnce = true;
		director = new Director(this);
		currentPlayerInputFocus = null;
		paused = false;
		
		player = new Player(this);
		player.setFunds(0);
		
		placement = null;
		constructionMatrix = new int[Level.HEIGHT][Level.WIDTH];
		
		/*item = new Item(this, "testcase", convertToEntityPixels(26), convertToEntityPixels(26), false);
		item.setY(convertToEntityPos(43));
		item.setX(convertToEntityPos(43));*/
		
		testCharacter = new PlayerCharacter(this, "character_scientist", convertToEntityPixels(32), convertToEntityPixels(32), Character.BASE_MOVEMENTSPEED);
		testCharacter.setX(convertToEntityPos(50));
		testCharacter.setY(convertToEntityPos(50));
		addCharacter(testCharacter);
		viewer.setFollowTarget(testCharacter);
		setCurrentPlayerInputFocus(testCharacter);
		
		pathfindtestchar = new AIControlledCharacter(this, "character_scientist", convertToEntityPixels(32), convertToEntityPixels(32), Character.BASE_MOVEMENTSPEED / 2, ControlledEntity.ZOMBIE, new PathfindStrategy(20));
		pathfindtestchar.setX(convertToEntityPos(55));
		pathfindtestchar.setY(convertToEntityPos(55));
		addCharacter(pathfindtestchar);
		new Thread(pathfindtestchar).start();
		
		showGui = false;
	}
	
	public void run() {		
		Thread viewerThread = new Thread(viewer);
		Thread directThread = new Thread(director);
		directThread.start();
		viewerThread.start();
	}
	
	public void update(GameContainer gc, int delta) {
		//Test pausing
		if (gc.getInput().isKeyPressed(Input.KEY_SPACE)) {
			paused = !paused;
		}
		//Test Item hauling job
		/*
		 * if (gc.getInput().isKeyPressed(Input.KEY_T)) { if (item.getJob() ==
		 * null) { Job itemJob = new Job(Job.JOB_ANY, 0); itemJob.addTask(new
		 * Task(this, itemJob, Task.MOVEMENT, new Node(null,
		 * World.convertToWorldPos(item.getX()),
		 * World.convertToWorldPos(item.getY())))); itemJob.addTask(new
		 * Task(this, itemJob, Task.PICKUP_ITEM, item)); itemJob.addTask(new
		 * Task(this, itemJob, Task.MOVEMENT, new Node(null, viewer.getMousec(),
		 * viewer.getMouser()))); itemJob.addTask(new Task(this, itemJob,
		 * Task.DROPOFF_ITEM, item)); item.setJob(itemJob);
		 * director.getJobQueue().addJob(itemJob); } }
		 */

		//Test Camera movement
		//double speed = 1;
		double change = .15;
		/*
		 * if (gc.getInput().isKeyPressed(Input.KEY_A))
		 * viewer.panView(viewer.getRow(), viewer.getColumn() - .25, speed,
		 * .25); if (gc.getInput().isKeyPressed(Input.KEY_D))
		 * viewer.panView(viewer.getRow(), viewer.getColumn() + .25, speed,
		 * .25); if (gc.getInput().isKeyPressed(Input.KEY_W))
		 * viewer.panView(viewer.getRow() - .25, viewer.getColumn(), speed,
		 * .25); if (gc.getInput().isKeyPressed(Input.KEY_S))
		 * viewer.panView(viewer.getRow() + .25, viewer.getColumn(), speed,
		 * .25);
		 */
		if (gc.getInput().isKeyDown(Controls.scroll_left))
			viewer.adjustC(-change);
		if (gc.getInput().isKeyDown(Controls.scroll_right))
			viewer.adjustC(change);
		if (gc.getInput().isKeyDown(Controls.scroll_up))
			viewer.adjustR(-change);
		if (gc.getInput().isKeyDown(Controls.scroll_down))
			viewer.adjustR(change);

		/* Primary World Game logic branch */
		//Create a char for debug
		if (gc.getInput().isKeyPressed(Controls.num_1)) {
			if (placement == null) {
				AIControlledCharacter newchar = new AIControlledCharacter(this, "character_scientist", convertToEntityPixels(32), convertToEntityPixels(32), Character.BASE_MOVEMENTSPEED, ControlledEntity.PLAYER_FRIENDLY, new WorkerStrategy());
				newchar.setX(convertToEntityPos(viewer.getMousec()));
				newchar.setY(convertToEntityPos(viewer.getMouser()));
				addCharacter(newchar);
				new Thread(newchar).start();
			}
		}
		//Create a reg desk for debug
		if (gc.getInput().isKeyPressed(Controls.num_2)) {
			if (placement == null) {
				//Start placing
				Construction newconst = new CStruct_Desk01(this);
				newconst.setX(convertToEntityPos(viewer.getMousec()));
				newconst.setY(convertToEntityPos(viewer.getMouser()));
				addConstruction(newconst);
				placement = newconst;
			}
		}
		//Create a exec desk for debug
		if (gc.getInput().isKeyPressed(Controls.num_3)) {
			if (placement == null) {
				//Start placing
				Construction newconst = new CStruct_ExecutiveDesk01(this);
				newconst.setX(convertToEntityPos(viewer.getMousec()));
				newconst.setY(convertToEntityPos(viewer.getMouser()));
				addConstruction(newconst);
				placement = newconst;
			}
		}
		//Create a following char for debug
		if (gc.getInput().isKeyPressed(Controls.num_4)) {
			if (placement == null) {
				AIControlledCharacter newchar = new AIControlledCharacter(this, "character_scientist", convertToEntityPixels(32), convertToEntityPixels(32), Character.BASE_MOVEMENTSPEED / 4, ControlledEntity.ZOMBIE, new FollowStrategy());
				((FollowStrategy) newchar.getAIStrategy()).setTarget(testCharacter);
				newchar.setX(convertToEntityPos(viewer.getMousec()));
				newchar.setY(convertToEntityPos(viewer.getMouser()));
				addCharacter(newchar);
				new Thread(newchar).start();
			}
		}
		//Create a pathfind char for debug
		if (gc.getInput().isKeyPressed(Controls.num_5)) {
			if (placement == null) {
				((PathfindStrategy) pathfindtestchar.getAIStrategy()).pathfind(Entity.getPositionAsNode(testCharacter));
			}
		}
		//Create a following char for debug
		if (gc.getInput().isKeyPressed(Controls.num_6)) {
			if (placement == null) {
				AIControlledCharacter newchar = new AIControlledCharacter(this, "character_scientist", convertToEntityPixels(32), convertToEntityPixels(32), Character.BASE_MOVEMENTSPEED / 4, ControlledEntity.ZOMBIE, new ZombieStrategy(15.00));
				newchar.setX(convertToEntityPos(viewer.getMousec()));
				newchar.setY(convertToEntityPos(viewer.getMouser()));
				addCharacter(newchar);
				new Thread(newchar).start();
			}
		}
		//Test Job Movement
		if (gc.getInput().isMousePressed(Input.MOUSE_MIDDLE_BUTTON)) {
			if (worldHasFocus()) {
				if (placement == null) {
					Job walkingJob = new Job(Job.JOB_ANY, 0);
					walkingJob.addTask(new Task(this, walkingJob, Task.MOVEMENT, new Node(null, viewer.getMousec(), viewer.getMouser())));
					director.getJobQueue().addJob(walkingJob);
				}
				else {
					//Change placement type
					Construction newconst = placement.getNextType();
					newconst.setX(convertToEntityPos(viewer.getMousec()));
					newconst.setY(convertToEntityPos(viewer.getMouser()));
					newconst.rotate((int) (placement.getRotation() / 90));
					removeConstruction(placement);
					addConstruction(newconst);
					placement = newconst;
				}
			}

		}
		//Test wall creation and deletion (using temp values)
		if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			if (worldHasFocus()) {
				if (placement == null) {
					//Check if not placing anything
					if (Tile.isWall(Tile.getTileId(level.getTile(viewer.getMouser(), viewer.getMousec())))) {
						level.setTile(Tile.createTile((short) 0), viewer.getMouser(), viewer.getMousec());
						viewer.recalcRotMatrixAt(viewer.getMouser(), viewer.getMousec());
						director.getPathfinder().openNewConnection(viewer.getMouser(), viewer.getMousec());
					}
				}
				else {
					//Place the placement
					if (placement.isValidPlacement()) {
						updateConstructionMatrix(placement, true);
						placement.closePathfinderConnection(getPathfinder());
						placement.setPlaced(true);
						placement = null;
					}
				}
			}
		}
		else if (gc.getInput().isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
			if (worldHasFocus()) {
				if (placement == null) {
					if (!Tile.isWall(Tile.getTileId(level.getTile(viewer.getMouser(), viewer.getMousec())))) {
						level.setTile(Tile.createTile((short) 1), viewer.getMouser(), viewer.getMousec());
						viewer.recalcRotMatrixAt(viewer.getMouser(), viewer.getMousec());
						director.getPathfinder().closeConnection(viewer.getMouser(), viewer.getMousec());
					}
				}
				else {
					//Cancel placement
					removeConstruction(placement);
					placement = null;
				}
			}
		}	
		
		//Update Camera
		viewer.tick(gc, delta);
		
		//Update entities
		try {
			ListIterator<Entity> it = entities.listIterator();
			while (it.hasNext()) {
				it.next().update(gc, delta);
			}
		} catch (ConcurrentModificationException e) {}
		
		//Update Director
		director.update();
		
		if (runOnce) {
			gc.getInput().addMouseListener(this);
			runOnce = false;
		}
		
		gc.getInput().clearKeyPressedRecord();
	}
	
	public void render(GameContainer gc, Graphics g) {
		viewer.drawScene(g);
		
		//Draw additional Debug strings
		g.setColor(Color.red);
		g.setFont(GameLogic.GUI_CONTEXT.getDefaultFont());
		g.drawString("MOUSE ROW: " + viewer.getMouser(), 25, 25);
		g.drawString("MOUSE COLUMN: " + viewer.getMousec(), 25, 50);
	}
	
	@Override
	public void mouseWheelMoved(int change) {
		if (worldHasFocus()) {
			if (placement == null) {
				viewer.zoom(change);
			}
			else {
				if (change > 0)
					placement.rotate(1);
				else if (change < 0)
					placement.rotate(3);
			}
		}
	}
	
	/**
	 * Returns true if movement is obstructed for the entity at position with width and height
	 */
	public boolean movementBlockedAt(double x, double y, double width, double height) {
		for (double r = y; r < y + height; r++) {
			for (double c = x; c < x + width; c++) {
				if (Tile.isWall(Tile.getTileId(level.getTile(convertToWorldPos(r), convertToWorldPos(c))))) {
					//Obstructed by wall
					return true;
				}
				if (constructionMatrix[convertToWorldPos(r)][convertToWorldPos(c)] == ConstructionBlockPoint) {
					//Obstructed by construction
					return true;
				}
			}
		}	
		
		return false;
	}
	
	/**
	 * Returns true if movement is impeded for the entity at position with width and height
	 */
	public boolean movementImpededAt(Character movechar, double destx, double desty, double srcwidth, double srcheight) {
		final int collisionPadding = 3;
		
		for (Character c : characters) {
			if (!c.equals(movechar)) {
				Rectangle src = new Rectangle((float) destx, (float) desty, (float) srcwidth, (float) srcheight);
				Rectangle comp = new Rectangle((float) c.getX() + collisionPadding, (float) c.getY() + collisionPadding, (float) c.getWidth() - (2 * collisionPadding), (float) c.getHeight() - (2 * collisionPadding));
				if (src.intersects(comp)) return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns true if the src entity has line of sight of the dest entity within the sight radius
	 */
	public boolean hasLineOfSight(Entity src, Entity dest, double sightRadius) {		
		double convertedSightRadius = World.convertToEntityPos(sightRadius);
		
		//First check easy case if dest is within sightradius of src
		double distance = Math.sqrt(Math.pow(src.getX() - dest.getX(), 2) + Math.pow(src.getY() - dest.getY(), 2)); //Distance formula
		if (distance > convertedSightRadius) {
			//System.out.println("No Line of Sight: SoftCase");
			return false;
		}
		
		//Trace
		int x0 = World.convertToWorldPos(src.getX() + (src.getWidth() / 2));
		int y0 = World.convertToWorldPos(src.getY() + (src.getHeight() / 2));
		int x1 = World.convertToWorldPos(dest.getX() + (dest.getWidth() / 2));
		int y1 = World.convertToWorldPos(dest.getY() + (dest.getHeight() / 2));		
		
		int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int x = x0;
        int y = y0;
        int n = 1 + dx + dy;
        int x_inc = (x1 > x0) ? 1 : -1;
        int y_inc = (y1 > y0) ? 1 : -1;
        int error = dx - dy;
        dx *= 2;
        dy *= 2;

        for (; n > 0; --n) {
            if (movementBlockedAt(World.convertToEntityPos(x), World.convertToEntityPos(y), World.convertToEntityPixels(Tile.WIDTH), World.convertToEntityPixels(Tile.HEIGHT))) {
            	//System.out.println("No Line of Sight: HardCase [" + y + ", " + x + "]");
            	return false;
            }

            if (error > 0) {
                x += x_inc;
                error -= dy;
            }
            else {
                y += y_inc;
                error += dx;
            }
        }
		
		return true;
	}
	
	/**
	 * Returns a subset of all entities within the given rectangle
	 * @param r y pos
	 * @param c x pos
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 * @return ArrayList of Entities in the rectangle
	 */
	public ArrayList<Entity> getEntitiesInArea(double r, double c, double width, double height) {
		ArrayList<Entity> entities = new ArrayList<Entity>();
		
		Rectangle rect = new Rectangle((float) c, (float) r, (float) width, (float) height);

		for (Entity e : this.entities) {
			Rectangle erect = new Rectangle((float) e.getX(), (float) e.getY(), (float) e.getWidth(), (float) e.getHeight());
			
			if (rect.intersects(erect) || rect.contains(erect)) {
				entities.add(e);
			}
		}
	
		return entities;
	}
	
	/**
	 * Returns a subset of all entities with the given radius from the center position
	 */
	public ArrayList<Entity> getEntitiesInRadius(double r, double c, double hradius, double vradius) {
		ArrayList<Entity> entities = new ArrayList<Entity>();
		
		Ellipse ellipse = new Ellipse((float) c, (float) r, (float) hradius, (float) vradius);
		
		for (Entity e : this.entities) {
			Rectangle hitbox = new Rectangle((float) e.getX(), (float) e.getY(), (float) e.getWidth(), (float) e.getHeight());
			
			if (ellipse.contains(hitbox) || ellipse.intersects(hitbox)) {
				entities.add(e);
			}
		}
		
		return entities;
	}
	
	/**
	 * Returns a subset of all entities within the given radius from center position, with a hostile alliance to the controlled entity
	 */
	public ArrayList<Entity> getEntitiesInRadiusWithHostileAlliance(ControlledEntity compare, double r, double c, double hradius, double vradius) {
		ArrayList<Entity> entities = new ArrayList<Entity>();
		
		Ellipse ellipse = new Ellipse((float) c, (float) r, (float) hradius, (float) vradius);
		
		for (Entity e : this.entities) {
			if (e instanceof ControlledEntity) {
				if (compare.isHostileTo((ControlledEntity) e)) {
					Rectangle hitbox = new Rectangle((float) e.getX(), (float) e.getY(), (float) e.getWidth(), (float) e.getHeight());

					if (ellipse.contains(hitbox) || ellipse.intersects(hitbox)) {
						entities.add(e);
					}
				}
				
			}
			
		}
		
		return entities;
	}
	
	/**
	 * Updates the construction matrix which is used for optimized collision detection with constructions
	 */
	public void updateConstructionMatrix(Construction cs, boolean adding) {		
		boolean matrix[][] = cs.getCollisionMatrix();
		boolean points[][] = cs.getUsePoints();
		for (int r = 0; r < cs.h(); r++) {
			for (int c = 0; c < cs.w(); c++) {				
				constructionMatrix[World.convertToWorldPos(cs.getY()) + r][World.convertToWorldPos(cs.getX()) + c] = ConstructionEmptyPoint;
				if (adding) {
					if (matrix[r][c] == true) {
						constructionMatrix[World.convertToWorldPos(cs.getY()) + r][World.convertToWorldPos(cs.getX()) + c] = ConstructionBlockPoint;
					}
					else if (points[r][c] == true) {
						constructionMatrix[World.convertToWorldPos(cs.getY()) + r][World.convertToWorldPos(cs.getX()) + c] = ConstructionUsePoint;
					}
				}
			}
		}
	}
	
	public int[][] getConstructionMatrix() {
		return constructionMatrix;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public boolean isWalkableTile(Mover mover, Node start, Node dest) {
		return mover.isValidNode(start, dest);
	}
	
	public int getCost(Mover mover, Node srcNode, Node dstNode) {
		return mover.getMoveCost(srcNode, dstNode);
	}
	
	public PathfindingQueue getPathfinder() {
		return director.getPathfinder();
	}
	
	public int getWidth() {
		return Level.WIDTH;
	}
	
	public int getHeight() {
		return Level.HEIGHT;
	}
	
	public int getDepth() {
		return 1;
	}
	
	public int getMouser() {
		return viewer.getMouser();
	}
	
	public int getMousec() {
		return viewer.getMousec();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void addCharacter(Character c) {
		characters.add(c);
		entities.add(c);
	}
	
	public void removeCharacter(Character c) {
		characters.remove(c);
		entities.remove(c);
	}
	
	public void addItem(Item i) {
		items.add(i);
		entities.add(i);
	}
	
	public void removeItem(Item i) {
		items.remove(i);
		entities.remove(i);
	}
	
	public void addConstruction(Construction c) {
		constructions.add(c);
		entities.add(c);
	}
	
	public void removeConstruction(Construction c) {
		constructions.remove(c);
		entities.remove(c);
	}
	
	public ArrayList<Character> getCharacters() {
		return characters;
	}
	
	public boolean isShowingGui() {
		return showGui;
	}
	
	public boolean worldHasFocus() {		
		return true;
	}
	
	public Character getCurrentPlayerInputFocus() {
		return currentPlayerInputFocus;
	}
	
	public void setCurrentPlayerInputFocus(Character c) {
		currentPlayerInputFocus = c;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public void setPaused(boolean b) {
		paused = b;
	}
	
	/**
	 * Converts the Screen pixel size to entity pixel size
	 */
	public static double convertToEntityPixels(int pixels) {
		return (double) pixels / ((double) Tile.WIDTH / (double) Tile.DIVISIONS);
	}
	
	/**
	 * Converts the entity pixel size to Screen pixel size
	 */
	public static int convertToScreenPixels(double pixels) {
		return (int) (pixels * ((double) Tile.WIDTH / (double) Tile.DIVISIONS));
	}
	
	/**
	 * Converts the coordinate to a World Coordinate
	 * @param coordinate
	 * @return world coordinate
	 */
	public static int convertToWorldPos(double coordinate) {
		return (int) Math.floor(coordinate / Tile.DIVISIONS);
	}
	
	/**
	 * Converts the coordinate to a precise World Coordinate
	 * @param coordinate the coordinate to convert
	 * @return double the precise world position
	 */
	public static double convertToPreciseWorldPos(double coordinate)
	{
		return coordinate / Tile.DIVISIONS;
	}
	
	/**
	 * Converts the coordinate to a Entity Coordinate
	 * @param coordinate
	 * @return world coordinate
	 */
	public static double convertToEntityPos(double coordinate) {
		return (coordinate) * ((double) Tile.DIVISIONS);
	}
}
