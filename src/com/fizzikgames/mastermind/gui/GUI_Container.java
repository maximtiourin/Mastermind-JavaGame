package com.fizzikgames.mastermind.gui;

import java.util.ArrayList;
import java.util.Collections;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * A GUI_Container holds a list of elements, and does operations on them to ease with rendering and updating.
 * @author Maxim Tiourin
 */
public class GUI_Container {
	public GUI_Container(float x, float y) {
		this.x = x;
		this.y = y;
		elements = new ArrayList<GUI_Element>();
		tooltipRenderQueue = new ArrayList<GUI_Tooltip>();
		mouseWheelMoved = 0;
		visible = false;
		keyFocus = null;
	}
	
	public void update(GameContainer gc) {
		if (visible) {
			mousex = gc.getInput().getMouseX();
			mousey = gc.getInput().getMouseY();
			
			for (GUI_Element e : elements) {
				e.update(gc);
			}
		}
		
		mouseWheelMoved = 0;
	}
	
	public void render(Graphics g) {
		if (visible) {
			for (GUI_Element e : elements) {
				e.render(g);
			}
		}
	}
	
	/**
	 * Adds the element into the container, and keeps a sorted list of element render priorities.
	 */
	public void addElement(GUI_Element e) {
		elements.add(e);
		Collections.sort(elements);
	}
	
	/**
	 * Returns the element with the given id, or null if it wasn't found.
	 */
	public GUI_Element getElement(String id) {
		if (elements.size() <= 0) return null;
		for (GUI_Element e : elements) {
			if (id.equals(e.getId())) {
				return e;
			}
		}
		
		return null;
	}
	
	/**
	 * Removes the element with the given id, or returns false if no element found.
	 */
	public boolean removeElement(String id) {
		if (elements.size() <= 0) return false;
		for (GUI_Element e : elements) {
			if (id.equals(e.getId())) {
				elements.remove(e);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Sets the visibility of the container, and consequently all of its elements, does not modify the element's visibility, simply never renders it if not visible.
	 */
	public void setVisible(boolean b) {
		visible = b;
	}
	
	/**
	 * Sets the element with the id to the given visibility, returns false if no element with the id is found.
	 */
	public boolean setElementVisible(String id, boolean visibility) {
		for (GUI_Element e : elements) {
			if (id.equals(e.getId())) {
				e.setVisible(visibility);
				return true;
			}
		}
		
		return false;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float mousex() {
		return mousex;
	}
	
	public float mousey() {
		return mousey;
	}
	
	public boolean hasFocus() {
		for (GUI_Element e : elements) {
			if (e.hasFocus()) return true;
		}
		
		return false;
	}
	
	public boolean hasKeyFocus() {
		if (keyFocus == null) return false;
		return true;
	}
	
	public GUI_Element getKeyFocus() {
		return keyFocus;
	}
	
	public void changeKeyFocus(GUI_Element e) {
		if (keyFocus != null) {
			if (keyFocus instanceof GUI_Element_TextBox) {
				((GUI_Element_TextBox) keyFocus).removeKeyFocus();
			}
		}
		
		keyFocus = e;
	}
	
	public ArrayList<GUI_Tooltip> getTooltipRenderQueue() {
		return tooltipRenderQueue;
	}
	
	public void addTooltipToRenderQueue(GUI_Tooltip t) {
		tooltipRenderQueue.add(t);
	}
	
	public void setMouseWheelMoved(int change) {
		mouseWheelMoved = change;
	}
	
	public int getMouseWheelState() {
		return mouseWheelMoved;
	}
	
	protected ArrayList<GUI_Element> elements;
	protected ArrayList<GUI_Tooltip> tooltipRenderQueue;
	protected GUI_Element keyFocus;
	protected int mouseWheelMoved;
	protected boolean visible;
	protected float x;
	protected float y;
	protected float mousex;
	protected float mousey;
}
