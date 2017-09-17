package com.fizzikgames.mastermind.ai;

import com.fizzikgames.mastermind.world.World;

/**
 * The director handles big picture ai operations.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class Director implements Runnable {
	private World world;
	private PathfindingQueue pathfinder;
	private JobQueue jobQueue;
	
	public Director(World world) {
		this.world = world;
		pathfinder = new PathfindingQueue(this);
		jobQueue = new JobQueue(this);
	}	
	
	@Override
	public void run() {
		new Thread(pathfinder).start();
		new Thread(jobQueue).start();
	}
	
	public void update() {
		pathfinder.update();
		jobQueue.update();
	}
	
	public void addJob(Job j) {
		jobQueue.addJob(j);
	}
	
	public PathfindingQueue getPathfinder() {
		return pathfinder;
	}
	
	public JobQueue getJobQueue() {
		return jobQueue;
	}
	
	public World getWorld() {
		return world;
	}
}
