package com.fizzikgames.mastermind.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public abstract class GUI {
	public GUI() {
		container = null;
		
		init();
	}
	
	protected abstract void init();
	
	public void update(GameContainer gc) {
		container.update(gc);
	}
	
	public void render(Graphics g) {
		container.render(g);
		
		for (GUI_Tooltip e : container.getTooltipRenderQueue()) {
			e.render(g, e.parent.x(), e.parent.y(), e.parent.getWidth(), e.parent.getHeight());
		}
		
		container.getTooltipRenderQueue().clear();
	}
	
	public GUI_Container getContainer() {
		return container;
	}
	
	public boolean hasFocus() {
		if (container.isVisible() && container.hasFocus()) return true;
		return false;
	}
	
	public boolean hasKeyFocus() {
		return container.hasKeyFocus();
	}
	
	public void setMouseWheelMoved(int change) {
		container.setMouseWheelMoved(change);
	}
	
	protected GUI_Container container;
}
