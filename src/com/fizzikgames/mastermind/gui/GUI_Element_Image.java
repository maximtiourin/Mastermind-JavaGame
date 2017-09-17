package com.fizzikgames.mastermind.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

/**
 * A GUI Image
 * @author Maxim Tiourin
 */
public class GUI_Element_Image extends GUI_Element {
	public GUI_Element_Image(GUI_Container container, String id, Image image, GUI_Tooltip tooltip, float x, float y, int renderPriority, boolean visible, boolean anchored) {
		super(container, id, x, y, renderPriority, visible, anchored);
		this.image = image;
		this.tooltip = tooltip;
	}
	
	@Override
	public void update(GameContainer gc) {
		if (visible()) {
			if (Rectangle.contains(container.mousex(), container.mousey(), x(), y(), image.getWidth(), image.getHeight())) {
				//Check for completely transparent pixels
				if (image.getColor((int) (container.mousex() - x()), (int) (container.mousey() - y())).getAlpha() == 0) {
					setFocus(false);
				}
				else {
					setFocus(true);
				}
			}
			else {
				setFocus(false);
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		if (visible()) {
			g.drawImage(image, x(), y());
			
			if ((tooltip != null) && hasFocus()) container.addTooltipToRenderQueue(tooltip);
		}
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	protected Image image;
}
