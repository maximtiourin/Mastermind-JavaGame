package com.fizzikgames.mastermind.gui;

import java.util.Collections;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import com.fizzikgames.mastermind.asset.AssetLoader;

/**
 * A GUI Vertical Scroll Area is similar to an ImageLabel except it has a maximum width and height, and will clip everything outside of
 * those dimensions, but also allowing the user to scroll the clipping vertically.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class GUI_Element_VerticalScrollArea extends GUI_Element_ImageLabel {
	protected Image bg;
	protected int maxHeight;
	protected float yoffset;
	protected GUI_Element_Button scrollup;
	protected GUI_Element_Button scrolldown;
	protected int sbd; //Scroll button dimensions
	protected float scrollbarHeight;
	protected float scrollbarOffset;
	protected boolean scrollbarDrag;
	private float prevdragy;
	private static final int HEIGHT_PADDING = 15;
	private static final int SCROLL_AMOUNT = 20;
	private static final int SBD_PADDING = 4;
	
	public GUI_Element_VerticalScrollArea(GUI_Container container, String id, Image bg, float x, float y, int width, int height, int scrollButtonDimension, int renderPriority, boolean visible, boolean anchored) {
		super(container, id, null, x, y, renderPriority, visible, anchored);
		this.width = width;
		this.height = height;
		this.bg = bg;
		this.maxHeight = 0;
		this.yoffset = 0;
		this.sbd = scrollButtonDimension;
		this.scrollbarHeight = 0;
		this.scrollbarOffset = 0;
		this.scrollbarDrag = false;
		this.prevdragy = 0;
		this.scrollup = new GUI_Element_Button(container, id + "_button_scrollup", AssetLoader.image("ui_gui_button01"), AssetLoader.image("ui_gui_buttonhighlight"), null, null, null, x() + this.width - sbd, y(), sbd, sbd, GUI_Element.RENDER_BUTTON, true, true);
		this.scrolldown = new GUI_Element_Button(container, id + "_button_scrolldown", AssetLoader.image("ui_gui_button01"), AssetLoader.image("ui_gui_buttonhighlight"), null, null, null, x() + this.width - sbd, y() + this.height - sbd, sbd, sbd, GUI_Element.RENDER_BUTTON, true, true);
	}
	
	@Override
	public void update(GameContainer gc) {
		if (visible()) {
			float oldyoffset = yoffset;
			
			//Update scroll buttons
			scrollup.update(gc);
			scrolldown.update(gc);
			
			//Update Bar height
			scrollbarHeight = ((float) height / (float) maxHeight) * (height - (sbd * 2));
			scrollbarOffset = ((float) yoffset / (float) maxHeight) * (height - (sbd * 2));
			
			if (Rectangle.contains(container.mousex(), container.mousey(), x(), y(), width, height)) {
				setFocus(true);
				
				//Check mousewheel
				int checkWheel = container.getMouseWheelState();
				
				if (checkWheel < 0) {
					if ((maxHeight - yoffset > height)) {
						//Allow scrolling down
						yoffset += Math.min(SCROLL_AMOUNT, maxHeight - (yoffset + height));
					}
				}
				else if (checkWheel > 0) {
					if (yoffset > 0) {
						//Allow scrolling up
						yoffset -= Math.min(SCROLL_AMOUNT, yoffset);
					}
				}
				
				//Check scroll clicks
				if (scrollup.isClickedVolatile() && yoffset > 0) yoffset -= Math.min(SCROLL_AMOUNT, yoffset);
				if (scrolldown.isClickedVolatile() && (maxHeight - yoffset > height)) yoffset += Math.min(SCROLL_AMOUNT, maxHeight - (yoffset + height));
				
				//Check scroll drag
				if (gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) 
						&& Rectangle.contains(container.mousex(), container.mousey(), x() + width - sbd + SBD_PADDING, y() + sbd + scrollbarOffset, sbd - (SBD_PADDING * 2), scrollbarHeight) 
						&& !scrollbarDrag) {
					scrollbarDrag = true;
					
					prevdragy = container.mousey();
				}
				else if (!gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && scrollbarDrag) {
					scrollbarDrag = false;
				}
				
				//Update scrollbar drag position
				if (scrollbarDrag) {
					float soff = (container.mousey() - prevdragy);
					
					float scrollAmount = (int) (Math.abs(soff) / ((float) height / (float) maxHeight));
					
					soff = Math.abs(soff) / soff; //Get unit velocity
					
					if (soff > 0) {						
						if ((maxHeight - yoffset > height)) {
							//Allow scrolling down
							yoffset += Math.min(scrollAmount, maxHeight - (yoffset + height));
						}
					}
					else if (soff < 0) {
						if (yoffset > 0) {
							//Allow scrolling up
							yoffset -= Math.min(scrollAmount, yoffset);
						}
					}
					
					prevdragy = container.mousey();
				}
				
				//Update Elements
				if ((Math.abs(oldyoffset) - Math.abs(yoffset)) != 0) {
					for (GUI_Element e : elements) {
						e.addY(-(yoffset - oldyoffset));
					}
				}
				
				for (GUI_Element e : elements) {
					//Update elements in bounds when area has focus
					if (GUI_Element.inBounds(e, new Rectangle(x(), y(), width, height))) e.update(gc);
				}
			}
			else {				
				setFocus(false);				
				for (GUI_Element e : elements) {
					//Clear focus for elements in bounds when the area doesnt have focus
					if (GUI_Element.inBounds(e, new Rectangle(x(), y(), width, height))) {
						if (!(e instanceof GUI_Element_TextBox)) e.setFocus(false);
						else e.update(gc);
					}
				}
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		if (visible()) {
			g.setColor(Color.white);
			
			if (bg != null) bg.getScaledCopy(width, height).draw(x(), y());
			
			g.setClip((int) x(), (int) y(), width, height);
			for (GUI_Element e : elements) {
				if (GUI_Element.inBounds(e, new Rectangle(x(), y(), width, height))) e.render(g);
			}
			g.clearClip();
			
			scrollup.render(g);
			scrolldown.render(g);
			
			//Draw temporary scroll bar
			g.setColor(Color.blue);
			g.fillRect(x() + width - sbd + SBD_PADDING, y() + sbd, sbd - (SBD_PADDING * 2), height - (sbd * 2));
			g.setColor(Color.green);
			g.fillRect(x() + width - sbd + SBD_PADDING, y() + sbd + scrollbarOffset, sbd - (SBD_PADDING * 2), scrollbarHeight);
			
			//Draw Debug rectangle
			/*g.setColor(Color.white);
			g.drawRect(x(), y(), width, maxHeight);
			g.setColor(Color.yellow);
			g.drawRect(x(), y() + yoffset, width, height);*/
		}
	}
	
	@Override
	public void addElement(GUI_Element e) {
		elements.add(e);
		Collections.sort(elements);
		
		int th = (int) (e.y() + e.getHeight() - y());
		maxHeight = Math.max(maxHeight, th) + HEIGHT_PADDING;
	}
}
