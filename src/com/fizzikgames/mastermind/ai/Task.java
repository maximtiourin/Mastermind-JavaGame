package com.fizzikgames.mastermind.ai;

import com.fizzikgames.fizlib.pathfinding.Node;
import com.fizzikgames.mastermind.ai.strategy.WorkerStrategy;
import com.fizzikgames.mastermind.entity.Entity;
import com.fizzikgames.mastermind.entity.Item;
import com.fizzikgames.mastermind.world.World;

/**
 * A task is a set of instructions which must be completed in sequential order
 * @author Maxim Tiourin
 */
public class Task {
	private Job job;
	private int type;
	private World world;
	private boolean needUpdate;
	private boolean complete;	
	//MOVEMENT specific variables
	public static final int MOVEMENT = 0;
	private Node destNode;	
	//ITEM specific variables
	public static final int PICKUP_ITEM = 1;
	public static final int DROPOFF_ITEM = 2;
	private Item item;
	
	/**
	 * TASK_MOVEMENT
	 */
	public Task(World w, Job j, int aType, Node aDestNode) {
		world = w;
		job = j;
		type = aType;
		destNode = aDestNode;
		needUpdate = true;
		complete = false;
	}
	
	/**
	 * TASK_ITEM
	 */
	public Task(World w, Job j, int aType, Item aItem) {
		world = w;
		job = j;
		type = aType;
		item = aItem;
		needUpdate = true;
		complete = false;
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	public void update() {
		switch (type) {
			case MOVEMENT: {
				if (needUpdate) {
					//Update the pathfinding for movement job
					world.getPathfinder().addTask(this);
					needUpdate = false;
				}
				else if (Entity.entityPosEqualsNode(job.getWorker(), destNode)) {
					//Complete the job
					complete = true;
				}
				break;
			}
			case PICKUP_ITEM: {
				if (Entity.entityPosEqualsNode(job.getWorker(), Entity.getPositionAsNode(item))) {
					item.setTask(this); //Sets the items current task to this task
					((WorkerStrategy) job.getWorker().getAIStrategy()).setItem(item);
					world.removeItem(item);
					complete = true;
				}
				break;
			}
			case DROPOFF_ITEM: {
				item.setTask(this); // Sets the items current task to this task
				((WorkerStrategy) job.getWorker().getAIStrategy()).setItem(null);
				item.setTask(null);
				item.setX(job.getWorker().getX());
				item.setY(job.getWorker().getY());
				world.addItem(item);
				item.setJob(null);
				complete = true;
				break;
			}
		}
	}
	
	public void setNeedUpdate(boolean b) {
		needUpdate = b;
	}
	
	public Job getJob() {
		return job;
	}
	
	public Node getDestNode() {
		return destNode;
	}
	
	public int getType() {
		return type;
	}
	
	public Item getItem() {
		return item;
	}
}
