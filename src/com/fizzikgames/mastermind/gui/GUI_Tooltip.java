package com.fizzikgames.mastermind.gui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

import com.fizzikgames.fizlib.string.StringUtil;
import com.fizzikgames.mastermind.GameLogic;

/**
 * A GUI Tooltip is textual information that can be displayed when an element is hovered over.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class GUI_Tooltip {
	public GUI_Tooltip(String text, UnicodeFont font, int maxWidth, Color bgColor, Color borderColor, Color textColor) {
		this.text = text;
		this.font = font;
		this.maxWidth = maxWidth;
		this.bgColor = bgColor;
		this.borderColor = borderColor;
		this.textColor = textColor;
		wraptext = new ArrayList<String>();
		setWrapping(true);
	}
	
	/**
	 * Renders the tooltip.
	 * @param g the graphics context
	 * @param x the x position of the parent
	 * @param y the y position of the parent
	 * @param w the width of the parent
	 * @param h the height of the parent
	 */
	public void render(Graphics g, float x, float y, int w, int h) {
		if (!text.equals("")) {
			int wwidth = GameLogic.WINDOWED_WIDTH;
			int wheight = GameLogic.WINDOWED_HEIGHT;
			float mousex = parent.getContainer().mousex();
			float mousey = parent.getContainer().mousey();
			int lwidth = w;
			int lheight = h;
			
			if (Rectangle.contains(mousex, mousey, x - PAD, y - PAD, lwidth + (2 * PAD), lheight + (2 * PAD))) {
				int twidth = highestWrapWidth() + (2 * OFFSET);
				int theight = (wraptext.size() * font.getHeight(text)) + (2 * OFFSET);
				float txoff;
				float tyoff;
				
				if ((mousex + twidth) < wwidth) {
					txoff = mousex;
				}
				else {
					txoff = wwidth - twidth;
				}
				
				if ((mousey + theight) < wheight) {
					tyoff = mousey + MOUSEYOFF;
				}
				else {
					tyoff = wheight - theight - MOUSEYOFF;
				}
				
				g.setLineWidth(1);
				g.setColor(bgColor);
				g.fillRect(txoff, tyoff, twidth, theight);
				g.setColor(borderColor);
				g.drawRect(txoff, tyoff, twidth, theight);
				
				//Draw text
				int i = 0;
				for (String e : wraptext) {
					font.drawString(txoff + OFFSET, tyoff + i + OFFSET, e, textColor);
					
					i += font.getHeight(text);
				}
			}
		}
	}
	
	public void setWrapping(boolean b) {
		final String token = " ";
		
		wraps = b;
		
		wraptext.clear();
		if (wraps) {
			//Tokenize lines so that each line fits the maximum width;
			String str = text;
			boolean fail = false;
			
			while ((font.getWidth(str) > maxWidth) && (!fail)) {
				int i = str.length() - 1;
				
				while ((i > 0) && (font.getWidth(StringUtil.substring(str, 0, i, true)) > maxWidth)) {
					i--;
				}
				
				if (i == 0) {
					undoWrapping();
					fail = true;
				}
				else {
					int pos = StringUtil.firstOccurenceBeforePos(str, token, i);
					
					wraptext.add(StringUtil.substring(str, 0, pos, true));
					str = StringUtil.substring(str, pos + 1, str.length(), true);
				}
			}
			wraptext.add(str);
		}
		else {
			undoWrapping();
		}
	}
	
	private void undoWrapping() {
		wraptext.clear();
		wraptext.add(text);
	}
	
	private int highestWrapWidth() {
		int width = 0;
		
		for (String e : wraptext) {
			int w = font.getWidth(e);
			if (w > width) width = w;
		}
		
		return width;
	}
	
	public void setParent(GUI_Element parent) {
		this.parent = parent;
	}
	
	protected GUI_Element parent;
	protected String text;
	protected ArrayList<String> wraptext;
	protected UnicodeFont font;
	protected int maxWidth;
	protected Color bgColor;
	protected Color borderColor;
	protected Color textColor;
	protected boolean wraps;
	private static final int MOUSEYOFF = 25;
	private static final int OFFSET = 5;
	private static final int PAD = 0;
}
