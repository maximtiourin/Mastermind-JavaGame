package com.fizzikgames.mastermind.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

/**
 * A GUI Label is a dynamic or static text element.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class GUI_Element_Label extends GUI_Element {
	public GUI_Element_Label(GUI_Container container, String id, String label, UnicodeFont font, Color color, GUI_Tooltip tooltip, float x, float y, int renderPriority, boolean visible, boolean anchored) {
		super(container, id, x, y, renderPriority, visible, anchored);
		this.label = label;
		this.font = font;
		this.color = color;
		this.tooltip = tooltip;
	}
	
	@Override
	public void update(GameContainer gc) {
		if (visible()) {
			if (Rectangle.contains(container.mousex(), container.mousey(), x(), y(), font.getWidth(label), font.getHeight(label))) {
				setFocus(true);
			}
			else {
				setFocus(false);
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		if (visible()) {
			//Label
			g.setFont(font);
			g.setColor(color);
			g.drawString(label, x(), y());
			//font.drawString(x(), y(), label, color);
			
			if ((tooltip != null) && hasFocus()) container.addTooltipToRenderQueue(tooltip);
		}
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public UnicodeFont getFont() {
		return font;
	}
	
	public void setFont(UnicodeFont font) {
		this.font = font;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String s) {
		label = s;
	}
	
	public GUI_Tooltip getTooltip() {
		return tooltip;
	}
	
	public void setTooltip(GUI_Tooltip t) {
		tooltip = t;
	}
	
	public int getWidth() {
		return font.getWidth(label);
	}
	
	public int getHeight() {
		return font.getHeight(label);
	}
	
	protected String label;
	protected UnicodeFont font;
	protected Color color;
}
