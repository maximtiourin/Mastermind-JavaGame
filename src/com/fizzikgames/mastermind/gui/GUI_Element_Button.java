package com.fizzikgames.mastermind.gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import com.fizzikgames.mastermind.Settings;

/**
 * A GUI button is a image that is clickable
 * @author Maxim Tiourin
 */
public class GUI_Element_Button extends GUI_Element_Image {
	public GUI_Element_Button(GUI_Container container, String id, Image bgimage, Image highlightimage, Image iconimage, GUI_Tooltip tooltip, Sound clickSound, float x, float y, int width, int height, int renderPriority, boolean visible, boolean anchored) {
		super(container, id, bgimage, tooltip, x, y, renderPriority, visible, anchored);
		this.clickSound = clickSound;
		this.width = width;
		this.height = height;
		this.clicked = false;
		this.hold = false;
		this.cooldown = 0;
		this.animation = 0;
		this.highlight = highlightimage;
		this.icon = iconimage;
	}
	
	public void update(GameContainer gc) {
		if (!gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			hold = false;
		}
		
		if (visible()) {			
			if (Rectangle.contains(container.mousex(), container.mousey(), x(), y(), width, height)) {
				//Check for completely transparent pixels
				if (image.getColor((int) (container.mousex() - x()), (int) (container.mousey() - y())).getAlpha() == 0) {
					setFocus(false);
				}
				else {
					setFocus(true);
					
					if (gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !clicked && !hold && (cooldown <= 0)) {
						if (clickSound != null) clickSound.play(1.00f, Settings.sound_effect_volume);
						clicked = true;
						hold = true;
						cooldown = COOLDOWN;
						animation = ANIMATION;
					}
				}
			}
			else {
				if (gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !hold) {
					hold = true;
				}
				setFocus(false);
			}
		}
		
		if (cooldown > 0) {
			cooldown--;
		}
		if (animation > 0) {
			animation --;
		}
	}
	
	public void render(Graphics g) {
		if (visible()) {
			//Draw BG
			if (image != null) {
				if (animation > 0) {
					g.drawImage(image.getScaledCopy(width - (SHRINK * 2), height - (SHRINK * 2)), x + SHRINK, y + SHRINK);
				}
				else {
					g.drawImage(image.getScaledCopy(width, height), x(), y());
				}
			}
			
			//Draw Highlight
			if (animation > 0) {
				/*g.setColor(Color.yellow);
				g.setLineWidth(2);
				g.drawRect(x() + SHRINK, y() + SHRINK, width - (SHRINK * 2), height - (SHRINK * 2));*/
			}
			else if (hasFocus()) {
				/*g.setColor(Color.white);
				g.setLineWidth(2);
				g.drawRect(x(), y(), width, height);*/
				if (highlight != null) highlight.getScaledCopy(width - (SHRINK * 2), height - (SHRINK * 2)).draw(x + SHRINK, y + SHRINK);
			}
			
			//Draw Icon
			if (icon != null) {
				if (animation > 0) {
					g.drawImage(icon.getScaledCopy(width - (SHRINK * 2), height - (SHRINK * 2)), x + SHRINK, y + SHRINK);
				}
				else {
					g.drawImage(icon.getScaledCopy(width, height), x(), y());
				}
			}
			
			//Tooltip
			if ((tooltip != null) && hasFocus()) container.addTooltipToRenderQueue(tooltip);
		}
	}
	
	public boolean isClicked() {
		return clicked;
	}
	
	public boolean isClickedVolatile() {
		if (clicked) {
			clicked = false;
			return true;
		}
		
		return false;
	}
	
	public void setClicked(boolean b) {
		clicked = b;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	protected Sound clickSound;
	protected int width;
	protected int height;
	protected boolean clicked;
	protected boolean hold;
	protected int cooldown;
	protected int animation;
	protected Image highlight;
	protected Image icon;
	public static final int COOLDOWN = 15;
	public static final int ANIMATION = 15;
	public static final int SHRINK = 2;
}
