package com.fizzikgames.mastermind.entity.sprite;

import java.awt.Point;

import org.newdawn.slick.Image;

/**
 * Implementation of a sprite sheet because slick's causes unacceptable artifacts.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class SpriteSheet {
	private Image[][] images;
	private int tw;
	private int th;
	
	public SpriteSheet(Image image, int atw, int ath) {
		tw = atw;
		th = ath;
		
		int c = image.getWidth() / tw;
		int r = image.getHeight() / th;
		
		images = new Image[r][c];
		
		//Initialize the sub images
		for (int rr = 0; rr < r; rr++) {
			for (int cc = 0; cc < c; cc++) {
				images[rr][cc] = image.getSubImage(cc * tw, rr * th, tw, th);
			}
		}
	}
	
	public Image getSprite(int x, int y) {
		return images[y][x];
	}
	
	public Image getSprite(Point point) {
		return getSprite((int) point.getX(), (int) point.getY());
	}
	
	public Image getScaledSprite(int x, int y, int atw, int ath) {
		if ((tw != atw) || (th != ath)) {
			return getSprite(x, y).getScaledCopy(atw, ath);
		} 
		else {
			return getSprite(x, y);
		}
	}
	
	public Image getScaledSprite(Point point, int atw, int ath) {
		return getScaledSprite((int) point.getX(), (int) point.getY(), atw, ath);
	}
}
