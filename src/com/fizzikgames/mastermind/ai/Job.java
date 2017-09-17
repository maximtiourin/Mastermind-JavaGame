package com.fizzikgames.mastermind.ai;

import java.util.ArrayList;
import java.util.Comparator;

import com.fizzikgames.fizlib.pathfinding.Path;
import com.fizzikgames.mastermind.entity.AIControlledCharacter;
import com.fizzikgames.mastermind.entity.Item;

/**
 * A job is a set of tasks, and has its own priority
 * @author Maxim Tiourin
 */
public class Job {
	private ArrayList<Task> tasks;
	private int type;
	private int currentTask;
	private int priority;
	private boolean complete;
	private AIControlledCharacter worker;
	
	//Job Types (0 = all can perform)
	public static final int JOB_COUNT = 1;
	public static final int JOB_ANY = 0;
	
	public Job(int aType, int aPriority) {
		type = aType;
		priority = aPriority;
		tasks = new ArrayList<Task>();
		currentTask = 0;
		complete = false;
	}
	
	/**
	 * Goes through and adds the pre-computed paths to each appropriate movement task in the job
	 */
	public void addPathQueue(ArrayList<Path> pathQueue) {
		for (Task e : tasks) {
			if (e.getType() == Task.MOVEMENT) {
				e.setNeedUpdate(false);
				PathfindingQueue.assignPath(worker, pathQueue.remove(0));
			}
		}
	}
	
	public void addTask(Task t) {
		tasks.add(t);
	}
	
	public void clearItemsAfterBadPath() {
		for (Task e : tasks) {
			if ((e.getType() == Task.DROPOFF_ITEM) || (e.getType() == Task.PICKUP_ITEM)) {
				Item item = e.getItem();
				item.setTask(null);
				item.setJob(null);
			}
		}
	}
	
	public int getSize() {
		return tasks.size();
	}
	
	public Task getCurrentTask() {
		if (currentTask >= tasks.size()) {
			complete = true;
			return null;
		}
		
		return tasks.get(currentTask);
	}
	
	public void setCurrentTask(int t) {
		currentTask = t;
	}
	
	public void nextTask() {		
		currentTask++;	
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int t) {
		type = t;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int p) {
		priority = p;
	}
	
	public AIControlledCharacter getWorker() {
		return worker;
	}
	
	public void setWorker(AIControlledCharacter w) {
		worker = w;
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	public void setComplete(boolean b) {
		complete = b;
	}
	
	public ArrayList<Task> getTasks() {
		return tasks;
	}
	
	public static class JobPriorityComparator implements Comparator<Job> {
		private boolean increasing;
		
		public JobPriorityComparator(boolean increasing) {
			this.increasing = increasing;
		}
		
		@Override
		public int compare(Job a, Job b)
		{
			if (increasing) return a.getPriority() - b.getPriority();
			else return b.getPriority() - a.getPriority();
		}
	}
}
