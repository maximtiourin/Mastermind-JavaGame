package com.fizzikgames.mastermind.ai;

import java.util.ArrayList;

import com.fizzikgames.fizlib.pathfinding.Astar;
import com.fizzikgames.fizlib.pathfinding.Node;
import com.fizzikgames.fizlib.pathfinding.Path;
import com.fizzikgames.mastermind.world.Level;
import com.fizzikgames.mastermind.world.Tile;
import com.fizzikgames.mastermind.world.World;
import com.fizzikgames.mastermind.ai.strategy.WorkerStrategy;
import com.fizzikgames.mastermind.entity.AIControlledCharacter;

public class PathfindingQueue implements Runnable {
	private Director director;
	private ArrayList<Task> tasks;
	private Astar astar;
	public static final int MOVEMENTCOST_HORIZONTAL = 1;
	public static final int MOVEMENTCOST_DIAGONAL = 4;
	public static final int MOVEMENTCOST_FRIENDLYIMPEDE = 25;
	
	public class Cell {
		private int row; //row the cell is in
		private int column; //column the cell is in
		private boolean end; //whether this cell is the end of the maze
		private boolean visited; //flag for whether this cell has been visited by a search algorithm
		private ArrayList<Cell> neighbors; //list of all neighbors of this cell
		private long identifier; //The index of this cell used in the adjacency list
		
		public Cell() 
		{
			row = -1;
			column = -1;
			end = false;
			visited = false;
			neighbors = new ArrayList<Cell>();
			identifier = -1;
		}
		
		/**
		 * Adds a neighbor to this cells list of neighbors
		 * @param neighbor the neighboring cell to add to the list
		 */
		public void addNeighbor(Cell neighbor) 
		{
			neighbors.add(neighbor);
		}
		
		/**
		 * Returns a list of this cells neighbors
		 * @return ArrayList<Cell>
		 */
		public ArrayList<Cell> getNeighbors() 
		{
			return neighbors;
		}
		
		/**
		 * Returns the cell's row
		 * @return int
		 */
		public int getRow() {
			return row;
		}
		
		/**
		 * Sets the cells' row
		 * @param row the row to set to
		 */
		public void setRow(int row) {
			this.row = row;
		}
		
		/**
		 * Returns the cell's column
		 * @return int
		 */
		public int getColumn() {
			return column;
		}
		
		/**
		 * Sets the cell's column
		 * @param column the column to set to
		 */
		public void setColumn(int column) {
			this.column = column;
		}
		
		/**
		 * Returns whether or not this cell is an ending cell
		 * @return boolean
		 */
		public boolean isEnd() 
		{
			return end;
		}
		
		/**
		 * Sets whether or not this cell is an ending cell
		 * @param b
		 */
		public void setEnd(boolean b) 
		{
			end = b;
		}
		
		/**
		 * Returns whether or not this cell has been visited
		 * @return boolean
		 */
		public boolean isVisited() 
		{
			return visited;
		}
		
		/**
		 * Sets whether or not this cell has been visited
		 * @param b
		 */
		public void setVisited(boolean b) 
		{
			visited = b;
		}
		
		/**
		 * Returns the cell identifier
		 * @return int
		 */
		public long getIdentifier() 
		{
			return identifier;
		}
		
		/**
		 * Sets the cell identifier
		 * @param id new identifier
		 */
		public void setIdentifier(long id) 
		{
			identifier = id;
		}
		
		/**
		 * Overridden equals method to check if two cells are equal to each other
		 * @param o the object to compare against
		 * @return boolean
		 */
		@Override
		public boolean equals(Object o) {
			if (o instanceof Cell) {
				if (((Cell) o).getIdentifier() == identifier) return true;
			}
			
			return false;
		}
	}
	
	public PathfindingQueue(Director director) {
		this.director = director;
		tasks = new ArrayList<Task>();
		astar = new Astar(new ManhattanHeuristic(), this.director.getWorld(), Level.WIDTH * Level.HEIGHT, 0, true, false, false);
		
		connectionCount = 0;
		connectedTiles = new Cell[Level.HEIGHT][Level.WIDTH];
		
		new Thread(astar).start();
	}
	
	public void run() {
		//Initialize Connection Cells
		for (int r = 0; r < Level.HEIGHT; r++) {
			for (int c = 0; c < Level.WIDTH; c++) {
				Cell cell = new Cell();
				cell.setIdentifier(connectionCount);
				cell.setRow(r);
				cell.setColumn(c);
				connectedTiles[r][c] = cell;				
			}
		}
		connectionCount++;
		
		//Generate neighbors for each cell in a clockwise manner
		for (int r = 0; r < Level.HEIGHT; r++) {
			for (int c = 0; c < Level.WIDTH; c++) {
				// Top
				if (r - 1 >= 0)
					connectedTiles[r][c].addNeighbor(connectedTiles[r - 1][c]);
				// Right
				if (c + 1 < Level.WIDTH)
					connectedTiles[r][c].addNeighbor(connectedTiles[r][c + 1]);
				// Bottom
				if (r + 1 < Level.HEIGHT)
					connectedTiles[r][c].addNeighbor(connectedTiles[r + 1][c]);
				// Left
				if (c - 1 >= 0)
					connectedTiles[r][c].addNeighbor(connectedTiles[r][c - 1]);
			}
		}
	}
	
	public void update() {
		if (tasks.size() > 0) {
			Task task = tasks.get(0);
			
			Node dest = task.getDestNode();
			int r = dest.getRow();
			int c = dest.getColumn();
			findPath(task.getJob().getWorker(), r, c);
			tasks.remove(0);
		}
	}
	
	public void addTask(Task t) {
		tasks.add(t);
	}
	
	/**
	 * Assigns a pre-computed path to the worker character
	 */
	public static void assignPath(AIControlledCharacter ch, Path path) {
		if (path.getSize() > 0) {
			path.getNextNode(); //Pops off start node, which is the position of the character
			((WorkerStrategy) ch.getAIStrategy()).setPath(path);
		}
	}
	
	/**
	 * Finds a valid path for the worker character towards the destination, if none exist, cancel the job
	 */
	public void findPath(AIControlledCharacter ch, int r, int c) {
		Path path = astar.findPath(ch, new Node(null, World.convertToWorldPos(ch.getX()), World.convertToWorldPos(ch.getY())), new Node(null, c, r));
		if (path.getSize() > 0) {
			path.getNextNode(); //Pops off start node, which is the position of the character
			((WorkerStrategy) ch.getAIStrategy()).setPath(path);
		}
		else if ((World.convertToWorldPos(ch.getX()) == c) && (World.convertToWorldPos(ch.getY()) == r)) {
			//Character is already there do nothing
		}
		else {
			System.out.println("No path found! Job cancelled.");
			((WorkerStrategy) ch.getAIStrategy()).getJob().clearItemsAfterBadPath();
			((WorkerStrategy) ch.getAIStrategy()).getJob().setComplete(true);
			((WorkerStrategy) ch.getAIStrategy()).setJob(null);
		}
	}
	
	/**
	 * Checks to see if there is a literal valid path for the character at the destination r and c, and also adds the path to the path queue if there is one.
	 */
	public boolean hasPath(ArrayList<Path> pathQueue, AIControlledCharacter ch, int r, int c) {
		Path path = astar.findPath(ch, new Node(null, World.convertToWorldPos(ch.getX()), World.convertToWorldPos(ch.getY())), new Node(null, c, r));
		if (path.getSize() > 0) {
			if (pathQueue != null) pathQueue.add(path);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks the connection matrix to see if the character should be able to reach the destination
	 */
	public boolean hasPath(AIControlledCharacter ch, int r, int c) {
		int chx = World.convertToWorldPos(ch.getX());
		int chy = World.convertToWorldPos(ch.getY());
		if (connectedTiles[chy][chx].equals(connectedTiles[r][c])) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Opens a new floor connection in the connection matrix
	 */
	public void openNewConnection(int r, int c) {
		int newr = 0;
		int newc = 0;
		long count1 = 0;
		boolean firstCount = true;
		boolean isCase = true; //flag on whether or not this is the right case
		/*Case 0 - No open floors in 4 cardinal directions*/
		newr = r - 1; newc = c;
		if (inLevelBounds(newr, newc)) {
			if (!isWall(newr, newc)) isCase = false;
		}
		newr = r + 1; newc = c;
		if (inLevelBounds(newr, newc)) {
			if (!isWall(newr, newc)) isCase = false;
		}
		newr = r; newc = c - 1;
		if (inLevelBounds(newr, newc)) {
			if (!isWall(newr, newc)) isCase = false;
		}
		newr = r; newc = c + 1;
		if (inLevelBounds(newr, newc)) {
			if (!isWall(newr, newc)) isCase = false;
		}
		if (isCase) {
			//Do Case 0
			connectedTiles[r][c].setIdentifier(connectionCount++);
		}
		else {
			/*Case 1 - Is open floors, but they are all the same count*/
			isCase = true;
			firstCount = true;
			newr = r - 1; newc = c;
			if (inLevelBounds(newr, newc)) {
				if (!isWall(newr, newc)) {
					if (firstCount) {
						count1 = connectedTiles[newr][newc].getIdentifier();
						firstCount = false;
					}
					else if (count1 != connectedTiles[newr][newc].getIdentifier()) {
						isCase = false;
					}
				}
			}
			newr = r + 1; newc = c;
			if (inLevelBounds(newr, newc)) {
				if (!isWall(newr, newc)) {
					if (firstCount) {
						count1 = connectedTiles[newr][newc].getIdentifier();
						firstCount = false;
					}
					else if (count1 != connectedTiles[newr][newc].getIdentifier()) {
						isCase = false;
					}
				}
			}
			newr = r; newc = c - 1;
			if (inLevelBounds(newr, newc)) {
				if (!isWall(newr, newc)) {
					if (firstCount) {
						count1 = connectedTiles[newr][newc].getIdentifier();
						firstCount = false;
					}
					else if (count1 != connectedTiles[newr][newc].getIdentifier()) {
						isCase = false;
					}
				}
			}
			newr = r; newc = c + 1;
			if (inLevelBounds(newr, newc)) {
				if (!isWall(newr, newc)) {
					if (firstCount) {
						count1 = connectedTiles[newr][newc].getIdentifier();
						firstCount = false;
					}
					else if (count1 != connectedTiles[newr][newc].getIdentifier()) {
						isCase = false;
					}
				}
			}
			if (isCase) {
				//Do Case 1
				connectedTiles[r][c].setIdentifier(count1);
			}
			else {
				/* Case 2 - Is open floors, but of different counts */
				firstCount = true;
				count1 = 0;
				newr = r - 1; newc = c;
				if (inLevelBounds(newr, newc)) {
					if (!isWall(newr, newc)) {
						if (firstCount) {
							count1 = connectedTiles[newr][newc].getIdentifier();
							firstCount = false;
						}
						else if (count1 > connectedTiles[newr][newc].getIdentifier()) {
							count1 = connectedTiles[newr][newc].getIdentifier();
						}
					}
				}
				newr = r + 1; newc = c;
				if (inLevelBounds(newr, newc)) {
					if (!isWall(newr, newc)) {
						if (firstCount) {
							count1 = connectedTiles[newr][newc].getIdentifier();
							firstCount = false;
						}
						else if (count1 > connectedTiles[newr][newc].getIdentifier()) {
							count1 = connectedTiles[newr][newc].getIdentifier();
						}
					}
				}
				newr = r; newc = c - 1;
				if (inLevelBounds(newr, newc)) {
					if (!isWall(newr, newc)) {
						if (firstCount) {
							count1 = connectedTiles[newr][newc].getIdentifier();
							firstCount = false;
						}
						else if (count1 > connectedTiles[newr][newc].getIdentifier()) {
							count1 = connectedTiles[newr][newc].getIdentifier();
						}
					}
				}
				newr = r; newc = c + 1;
				if (inLevelBounds(newr, newc)) {
					if (!isWall(newr, newc)) {
						if (firstCount) {
							count1 = connectedTiles[newr][newc].getIdentifier();
							firstCount = false;
						}
						else if (count1 > connectedTiles[newr][newc].getIdentifier()) {
							count1 = connectedTiles[newr][newc].getIdentifier();
						}
					}
				}
				
				//Do Case 2
				dfsFloodFill(count1, r, c);
			}
		}
	}
	
	/**
	 * Closes the floor connection, changing the count of an adjacent rooms
	 */
	public void closeConnection(int r, int c) {
		int newr = 0;
		int newc = 0;
		
		newr = r - 1; newc = c;
		if (inLevelBounds(newr, newc)) {
			if (!isWall(newr, newc)) {
				dfsFloodFill(connectionCount++, newr, newc);
			}
		}
		newr = r + 1; newc = c;
		if (inLevelBounds(newr, newc)) {
			if (!isWall(newr, newc)) {
				dfsFloodFill(connectionCount++, newr, newc);
			}
		}
		newr = r; newc = c - 1;
		if (inLevelBounds(newr, newc)) {
			if (!isWall(newr, newc)) {
				dfsFloodFill(connectionCount++, newr, newc);
			}
		}
		newr = r; newc = c + 1;
		if (inLevelBounds(newr, newc)) {
			if (!isWall(newr, newc)) {
				dfsFloodFill(connectionCount++, newr, newc);
			}
		}
	}
	
	private void dfsFloodFill(long value, int r, int c) {
		clearVisited();
		floodFill(value, r, c);
	}
	
	/**
	 * [DFS based] Flood Fills the connectedTiles with the given value if they don't already have it, starting at r,c
	 */
	private void floodFill(long value, int r, int c) {
		Cell currentCell = connectedTiles[r][c];
		currentCell.setVisited(true);
		currentCell.setIdentifier(value); //Fill the value!
		
		ArrayList<Cell> neighbors = currentCell.getNeighbors();
		ArrayList<Cell> unvisited = new ArrayList<Cell>();

		// Add proper unvisited
		for (Cell e : neighbors) {
			// Check that it is a different value than param value
			if (e.getIdentifier() != value) {
				// Check that it isn't a wall
				if (!isWall(e.getRow(), e.getColumn())) {
					// Check that it hasn't been visited
					if (!e.isVisited()) {
						unvisited.add(e);
					}
				}
			}
		}

		for (Cell e : unvisited) {
			floodFill(value, e.getRow(), e.getColumn());
		}
	}
	
	/**
	 * Clears the visited flag off of all cells in the connectedTiles matrix
	 */
	public void clearVisited() {
		for (int r = 0; r < Level.HEIGHT; r++) {
			for (int c = 0; c < Level.WIDTH; c++) {
				connectedTiles[r][c].setVisited(false);
			}
		}
	}
	
	/**
	 * Returns true if the tile is a wall
	 */
	private boolean isWall(int r, int c) {
		Level level = director.getWorld().getLevel();
		int constructionMatrix[][] = director.getWorld().getConstructionMatrix();
		
		boolean returnval = false;
		if (Tile.isWall(Tile.getTileId(level.getTile(r, c)))) {
			returnval = true;
		}
		else if (constructionMatrix[r][c] == World.ConstructionBlockPoint) {
			returnval = true;
		}
		
		return returnval;
	}
	
	/**
	 * Returns true if the coordinates are within the level's bounds
	 */
	private boolean inLevelBounds(int r, int c) {
		if (!((r >= 0) && (r < Level.HEIGHT))) return false;
		if (!((c >= 0) && (c < Level.WIDTH))) return false;
		
		return true;
	}
	
	public Cell[][] DEBUG() {
		return connectedTiles;
	}
	
	private long connectionCount; //connection value, default 0, increase every time a new unconnected room is found
	private Cell connectedTiles[][]; //stores a connection value for each tile default 0 (walls), use these values to see quickly see if a worker can path to a destination.
									//When a floor is opened up for the first time it gets a value of i, and all connected floors receive i. When another floor is opened up
									//, if it has a connected floor adjacent it recieves i, otherwise it recieves i+1, where i is connection Count. When a floor is opened
									// up and it is adjacent to two or more floors, it recieves the lowest count of them all, and then flood fills the rest to that count.
}
