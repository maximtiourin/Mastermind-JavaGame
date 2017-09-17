package com.fizzikgames.mastermind.ai;

import java.util.ArrayList;
import java.util.PriorityQueue;

import com.fizzikgames.fizlib.pathfinding.Path;
import com.fizzikgames.mastermind.ai.strategy.WorkerStrategy;
import com.fizzikgames.mastermind.entity.AIControlledCharacter;
import com.fizzikgames.mastermind.entity.Character;
import com.fizzikgames.mastermind.world.World;

public class JobQueue implements Runnable {
	private Director director;
	private ArrayList<Job> activeList;
	private PriorityQueue<Job> jobQueue;
	private ArrayList<AIControlledCharacter> workerList;
	
	public JobQueue(Director director) {
		this.director = director;
		activeList = new ArrayList<Job>();
		jobQueue = new PriorityQueue<Job>(100, new Job.JobPriorityComparator(true));
		workerList = new ArrayList<AIControlledCharacter>();
	}
	
	public void run() {
		
	}
	
	public void update() {
		//Find any workers with no job and add them to the worker list **EDIT 5-1-2012** Find all possible workers, this helps with impossible job queueiong
		workerList.clear();
		for (Character e : director.getWorld().getCharacters()) {
			if (e instanceof AIControlledCharacter) {
				if (((AIControlledCharacter) e).getAIStrategy() instanceof WorkerStrategy) {
					workerList.add((AIControlledCharacter) e);
				}				
			}
		}
		
		//Look through active list, check if current task is complete of selected job
		//If it is move to next task, if no new task complete the job.
		ArrayList<Job> popList = new ArrayList<Job>();
		for (Job e : activeList) {
			Task currentTask = e.getCurrentTask();
			if (currentTask != null) {
				if (currentTask.isComplete()) {
					e.nextTask();
				}
			}
			else {
				popList.add(e);
			}
		}
		//Remove all jobs from active that are on pop
		for (Job e : popList) {
			System.out.println("Job is Done!");
			((WorkerStrategy) e.getWorker().getAIStrategy()).setJob(null);
			activeList.remove(e);
		}
		
		//Try to assign queued jobs to workers on the worker list
		popList.clear();
		ArrayList<AIControlledCharacter> popList2 = new ArrayList<AIControlledCharacter>();
		for (Job j : jobQueue) {
			ArrayList<Path> pathQueue = new ArrayList<Path>();
			int jReachError = 0; //Counter to keep track of how many workers can't reach a task in this job
			popList2.clear();
			for (AIControlledCharacter e : workerList) {
				if (((WorkerStrategy) e.getAIStrategy()).getLabors()[j.getType()] == 1) {
					if (noTaskMovementsBlocked(pathQueue, e, j)) {
						if (((WorkerStrategy) e.getAIStrategy()).getJob() == null) {
							System.out.println("Worker Assigned!");
							j.setCurrentTask(0);
							j.setWorker(e);
							if (pathQueue.size() > 0) j.addPathQueue(pathQueue);
							((WorkerStrategy) e.getAIStrategy()).setJob(j);
							activeList.add(j);
							popList.add(j);
							//popList2.add(e);
							break;
						}
					}
					else {
						jReachError++;
					}
				}
			}
			if (jReachError == workerList.size()) {
				System.out.println("Job Cancelled. One or more tasks is unreachable.");
				j.clearItemsAfterBadPath();//Clear items
				popList.add(j); //Remove this job because one or more tasks cant be reached by anyone
			}
			//Remove popped workers
			//for (Character e : popList2) {
			//	workerList.remove(e);
			//}
		}
		
		//Remove jobs queued and added to pop list
		for (Job e : popList) {
			jobQueue.remove(e);
		}
	}
	
	public void addJob(Job j) {
		jobQueue.add(j);
		System.out.println("Adding Job to Job Queue");
	}
	
	/**
	 * Returns true if no invalid movement tasks in the job for this character
	 * Also, for optimization, it sends the reference path queue to the pathfinder which gets populated with paths
	 * if they are valid for each task, if they all are, the job gets created with those paths already precalculated.
	 */
	public boolean noTaskMovementsBlocked(ArrayList<Path> pathQueue, AIControlledCharacter e, Job j) {
		for (Task t : j.getTasks()) {
			if (t.getType() == Task.MOVEMENT) {
				if (director.getWorld().movementBlockedAt(World.convertToEntityPos(t.getDestNode().getX()), 
														  World.convertToEntityPos(t.getDestNode().getY()), 
														  e.getWidth(), e.getHeight())) {
					return false;
				}
				if (!director.getPathfinder().hasPath(e, t.getDestNode().getRow(), t.getDestNode().getColumn())) {
					return false;
				}
			}
		}
		
		return true;
	}
}
