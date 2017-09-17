package com.fizzikgames.mastermind.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.fizzikgames.mastermind.ai.Job;
import com.fizzikgames.mastermind.ai.Task;
import com.fizzikgames.mastermind.asset.AssetLoader;
import com.fizzikgames.mastermind.world.World;

public class Item extends Entity {
	private Image image;
	private Job job; //Sets this to the job that is using the item, so no other jobs can use it
	private Task task; //Sets this to the task that is using the item, so no other tasks can use it until this is null
	private boolean blocking; //Whether or not this item blocks movement
	
	public Item(World world, String imageAssetPath, double width, double height, boolean blocking) {
		super(world, width, height, Entity.RENDER_ITEM);
		
		this.image = null;
		this.job = null;
		this.task = null;
		this.blocking = blocking;		
		this.image = AssetLoader.image(imageAssetPath);
	}
	
	public void update(GameContainer gc, int delta) {
		
	}
	
	public void render(GameContainer gc, Graphics g) {
		
	}
	
	public void draw(Image scaledImage, float x, float y) {
		
	}
	
	public Image getImage() {
		return image;
	}
	
	public boolean isBlocking() {
		return blocking;
	}
	
	public void setBlocking(boolean b) {
		blocking = b;
	}
	
	public Job getJob() {
		return job;
	}
	
	public void setJob(Job j) {
		job = j;
	}
	
	public Task getTask() {
		return task;
	}
	
	public void setTask(Task t) {
		task = t;
	}
}
