package com.fizzikgames.mastermind.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * A GUI_Element is the abstract base for any element of the GUI that is displayed or interactive with the user.
 * @author Maxim Tiourin
 */
public abstract class GUI_Element implements Comparable<GUI_Element> {
	public GUI_Element(GUI_Container container, String id, float x, float y, int renderPriority, boolean visible, boolean anchored) {
		this.container = container;
		this.visible = visible;
		this.focus = false;
		this.id = id;
		this.renderPriority = renderPriority;
		this.anchored = anchored;
		this.x = x;
		this.y = y;
		this.xprev = x;
		this.yprev = y;
	}
	
	public abstract void update(GameContainer gc);
	public abstract void render(Graphics g);
	public abstract int getWidth();
	public abstract int getHeight();
	
	public int compareTo(GUI_Element e) {
		if (renderPriority < e.getRenderPriority()) return -1;
		else if (renderPriority == e.getRenderPriority()) return 0;
		return 1;
	}
	
	public GUI_Container getContainer() {
		return container;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean b) {
		visible = b;
	}
	
	public int getRenderPriority() {
		return renderPriority;
	}
	
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the true x position of this element based on whether or not it is anchored.
	 */
	protected float x() {
		if (anchored) return (container.getX() + x);
		return x;
	}
	
	/**
	 * Returns the true y position of this element based on whether or not it is anchored.
	 */
	protected float y() {
		if (anchored) return (container.getY() + y);
		return y;
	}
	
	protected void addX(float x) {
		this.xprev = this.x;
		this.x += x;
	}
	
	protected void addY(float y) {
		this.yprev = this.y;
		this.y += y;
	}
	
	protected float xprev() {
		return xprev;
	}
	
	protected float yprev() {
		return yprev;
	}
	
	protected boolean visible() {
		if (container.isVisible() && visible) return true;
		return false;
	}
	
	public boolean hasFocus() {
		return focus;
	}
	
	public void setFocus(boolean f) {
		focus = f;
	}
	
	public static boolean inBounds(GUI_Element e, Rectangle bounds) {
		if (bounds.intersects(new Rectangle(e.x(), e.y(), e.getWidth(), e.getHeight()))) {
			return true;
		}
		
		return false;
	}
	
	protected GUI_Container container;
	protected boolean focus;
	protected boolean visible;
	protected String id;
	protected int renderPriority;
	protected boolean anchored;
	protected float x;
	protected float y;
	protected float xprev;
	protected float yprev;
	protected GUI_Tooltip tooltip;
	public static final int RENDER_IMAGE = 1000;
	public static final int RENDER_LABEL = 2000;
	public static final int RENDER_BUTTON = 3000;
	public static final int RENDER_TEXTBOX = 4000;
	public static final int RENDER_FOCUS = 9999;
}
