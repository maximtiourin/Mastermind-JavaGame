package com.fizzikgames.mastermind.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.fizzikgames.mastermind.Renderable;

public abstract class RenderableGameObject implements Renderable {
	protected int renderPriority;
	public static final int RENDER_PLAYERCHARACTER = 9;
	public static final int RENDER_CHARACTER = 10;
	public static final int RENDER_ITEM = 50;
	public static final int RENDER_CONSTRUCTION = 250;
	
	public RenderableGameObject(int renderPriority) {
		this.renderPriority = renderPriority;
	}
	
	public abstract void render(GameContainer gc, Graphics g);
	public abstract Image getImage();
	
	public int getRenderPriority() {
		return renderPriority;
	}
	
	public void setRenderPriority(int p) {
		renderPriority = p;
	}
}
