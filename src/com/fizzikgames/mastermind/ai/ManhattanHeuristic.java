package com.fizzikgames.mastermind.ai;

import com.fizzikgames.fizlib.pathfinding.AstarHeuristic;
import com.fizzikgames.fizlib.pathfinding.Mover;
import com.fizzikgames.fizlib.pathfinding.Node;
import com.fizzikgames.fizlib.pathfinding.TileMap;

public class ManhattanHeuristic implements AstarHeuristic {
	public ManhattanHeuristic() {
		
	}
	
	@Override
	public int getCost(TileMap map, Mover mover, Node srcNode, Node dstNode) {
		int cost = 0, sx, sy, sz, dx, dy, dz, xdist, ydist, zdist;
		dx = dstNode.getX();
		dy = dstNode.getY();
		dz = dstNode.getZ();
		sx = srcNode.getX();
		sy = srcNode.getY();
		sz = srcNode.getZ();
		xdist = Math.abs(sx - dx);
		ydist = Math.abs(sy - dy);
		zdist = Math.abs(sz - dz);
		cost = MOVECOST * ((xdist) + (ydist) + (zdist));
		return cost;
	}

	private final int MOVECOST = 10;
}
