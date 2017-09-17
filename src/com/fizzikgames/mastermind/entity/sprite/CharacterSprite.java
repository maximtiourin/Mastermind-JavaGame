package com.fizzikgames.mastermind.entity.sprite;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

public class CharacterSprite extends Sprite {
	public CharacterSprite(String spritesheet) {
		super(spritesheet);
		
		initAnimations();		
		current("stand_still");
	}
	
	public void initAnimations() {
		animationList = new ArrayList<AnimationReference>();
		Image imageList[];
		int durationList[];
		Point pos;
		Animation anim;
		
		//Standing Still
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(STAND_STILL);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("stand_still", anim, false));
		//Walking
		imageList = new Image[3];
		durationList = new int[3];
		pos = getSpriteSheetPosition(WALK_1);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 150;
		pos = getSpriteSheetPosition(STAND_STILL);
		imageList[1] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[1] = 300;
		pos = getSpriteSheetPosition(WALK_2);
		imageList[2] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[2] = 150;
		anim = new Animation(imageList, durationList);
		anim.setPingPong(true);
		animationList.add(new AnimationReference("walk_normal", anim, true));
	}
	
	/**
	 * Returns the x and y position of the image based on its cell id in the spritesheet
	 */
	public static Point getSpriteSheetPosition(int cellid) {
		//Quick case
		if (cellid == 0) return new Point(0, 0);
		
		int left = cellid;
		int x = 0, y = 0;
		
		while ((left > 0) && (Math.floor(SPRITESHEET_WIDTH / WIDTH) <= left)) {
			left -= Math.floor(SPRITESHEET_WIDTH / WIDTH);
			y++;
		}
		x = left;
		
		return new Point(x, y);
	}
	
	public static final int SPRITESHEET_WIDTH = 512;
	public static final int SPRITESHEET_HEIGHT = 512;
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	//Static Cell Ids
	public static final int STAND_STILL = 0;
	public static final int WALK_1 = 1;
	public static final int WALK_2 = 2;
}
