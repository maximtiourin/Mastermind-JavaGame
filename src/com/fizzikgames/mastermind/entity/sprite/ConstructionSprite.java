package com.fizzikgames.mastermind.entity.sprite;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

public class ConstructionSprite extends Sprite {
	public ConstructionSprite(String spritesheet) {
		super(spritesheet);
		
		rotation = 0;
		
		initAnimations();		
		current("A_r0c0");
	}
	
	public void initAnimations() {
		animationList = new ArrayList<AnimationReference>();
		Image imageList[];
		int durationList[];
		Point pos;
		Animation anim;
		
		//A_r0c0
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r0c0);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r0c0", anim, false));
		//A_r0c1
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r0c1);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r0c1", anim, false));
		//A_r0c2
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r0c2);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r0c2", anim, false));
		//A_r0c3
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r0c3);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r0c3", anim, false));
		//A_r1c0
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r1c0);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r1c0", anim, false));
		//A_r1c1
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r1c1);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r1c1", anim, false));
		//A_r1c2
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r1c2);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r1c2", anim, false));
		//A_r1c3
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r1c3);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r1c3", anim, false));
		//A_r2c0
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r2c0);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r2c0", anim, false));
		//A_r2c1
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r2c1);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r2c1", anim, false));
		//A_r2c2
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r2c2);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r2c2", anim, false));
		//A_r2c3
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r2c3);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r2c3", anim, false));
		//A_r3c0
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r3c0);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r3c0", anim, false));
		//A_r3c1
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r3c1);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r3c1", anim, false));
		//A_r3c2
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r3c2);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r3c2", anim, false));
		//A_r3c3
		imageList = new Image[1];
		durationList = new int[1];
		pos = getSpriteSheetPosition(A_r3c3);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 1;
		anim = new Animation(imageList, durationList);
		anim.setLooping(true);
		animationList.add(new AnimationReference("A_r3c3", anim, false));
		//A_r0c1 <-> A_r0c2
		imageList = new Image[2];
		durationList = new int[2];
		pos = getSpriteSheetPosition(A_r0c1);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 600;
		pos = getSpriteSheetPosition(A_r0c2);
		imageList[1] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[1] = 150;
		anim = new Animation(imageList, durationList);
		anim.setPingPong(true);
		animationList.add(new AnimationReference("A_r0c1<->A_r0c2", anim, false));
		//A_r0c3 <-> A_r1c3
		imageList = new Image[2];
		durationList = new int[2];
		pos = getSpriteSheetPosition(A_r0c3);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 600;
		pos = getSpriteSheetPosition(A_r1c3);
		imageList[1] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[1] = 150;
		anim = new Animation(imageList, durationList);
		anim.setPingPong(true);
		animationList.add(new AnimationReference("A_r0c3<->A_r1c3", anim, false));
		//A_r3c0 <-> A_r3c1
		imageList = new Image[2];
		durationList = new int[2];
		pos = getSpriteSheetPosition(A_r3c0);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 600;
		pos = getSpriteSheetPosition(A_r3c1);
		imageList[1] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[1] = 150;
		anim = new Animation(imageList, durationList);
		anim.setPingPong(true);
		animationList.add(new AnimationReference("A_r3c0<->A_r3c1", anim, false));
		//A_r3c2 <-> A_r3c3 <-> A_r2c2
		imageList = new Image[3];
		durationList = new int[3];
		pos = getSpriteSheetPosition(A_r3c2);
		imageList[0] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[0] = 300;
		pos = getSpriteSheetPosition(A_r3c3);
		imageList[1] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[1] = 150;
		pos = getSpriteSheetPosition(A_r2c2);
		imageList[2] = spritesheet.getSprite((int) pos.getX(), (int) pos.getY());
		durationList[2] = 600;
		anim = new Animation(imageList, durationList);
		anim.setPingPong(true);
		animationList.add(new AnimationReference("A_r3c2<->A_r3c3<->A_r2c2", anim, false));
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
	
	@Override
	public Image image() {
		Image img = currentAnimation.getCurrentFrame().getScaledCopy(1f);
		return img;
	}
	
	public double getRotation() {
		return rotation;
	}
	
	public void setRotation(double r) {
		rotation = r;
	}
	
	private double rotation;
	public static final int SPRITESHEET_WIDTH = 128;
	public static final int SPRITESHEET_HEIGHT = 128;
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	//Static Cell Ids
	public static final int A_r0c0 = 0;
	public static final int A_r0c1 = 1;
	public static final int A_r0c2 = 2;
	public static final int A_r0c3 = 3;
	public static final int A_r1c0 = 4;
	public static final int A_r1c1 = 5;
	public static final int A_r1c2 = 6;
	public static final int A_r1c3 = 7;
	public static final int A_r2c0 = 8;
	public static final int A_r2c1 = 9;
	public static final int A_r2c2 = 10;
	public static final int A_r2c3 = 11;
	public static final int A_r3c0 = 12;
	public static final int A_r3c1 = 13;
	public static final int A_r3c2 = 14;
	public static final int A_r3c3 = 15;
}
