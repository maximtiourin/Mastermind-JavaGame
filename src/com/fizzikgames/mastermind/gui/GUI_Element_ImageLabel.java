package com.fizzikgames.mastermind.gui;

import java.util.ArrayList;
import java.util.Collections;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * A GUI Image Label is a combination of elements layered on one another to produce a dynamic element.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class GUI_Element_ImageLabel extends GUI_Element {
	public GUI_Element_ImageLabel(GUI_Container container, String id, GUI_Tooltip tooltip, float x, float y, int renderPriority, boolean visible, boolean anchored) {
		super(container, id, x, y, renderPriority, visible, anchored);
		this.elements = new ArrayList<GUI_Element>();
		this.tooltip = tooltip;
		this.width = 0;
		this.height = 0;
	}
	
	@Override
	public void update(GameContainer gc) {
		if (visible()) {
			setFocus(false);
			for (GUI_Element e : elements) {
				e.update(gc);
				if (e.hasFocus()) setFocus(true);
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		if (visible()) {			
			for (GUI_Element e : elements) {
				e.render(g);
			}
			
			if ((tooltip != null) && hasFocus()) container.addTooltipToRenderQueue(tooltip);
		}
	}
	
	public void addElement(GUI_Element e) {
		elements.add(e);
		Collections.sort(elements);
		
		//Calculate width and height
		int tw = (int) (e.x() + e.getWidth() - x());
		width = Math.max(width, tw);
		int th = (int) (e.y() + e.getHeight() - y());
		height = Math.max(height, th);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	protected ArrayList<GUI_Element> elements;
	protected int width;
	protected int height;
}
