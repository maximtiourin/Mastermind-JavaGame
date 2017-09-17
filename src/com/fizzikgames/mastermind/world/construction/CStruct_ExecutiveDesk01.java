package com.fizzikgames.mastermind.world.construction;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

import com.fizzikgames.mastermind.world.World;
import com.fizzikgames.mastermind.entity.sprite.ConstructionSprite;

public class CStruct_ExecutiveDesk01 extends CAbstruct_ExecutiveDesk {
	public CStruct_ExecutiveDesk01(World world) {
		super(world, World.convertToEntityPixels(WIDTH), World.convertToEntityPixels(HEIGHT));
		
		initCells();
	}
	
	@Override
	public void initCells() {
		cells[0][0] = new ConstructionSprite("cstructp_desk002");
		cells[0][1] = new ConstructionSprite("cstructp_desk002");
		cells[0][2] = new ConstructionSprite("cstructp_desk002");
		cells[0][3] = new ConstructionSprite("cstructp_desk002");
		cells[0][4] = new ConstructionSprite("cstructp_desk002");
		cells[1][0] = new ConstructionSprite("cstructp_desk002");
		cells[1][1] = new ConstructionSprite("cstructp_desk002");
		cells[1][2] = new ConstructionSprite("cstructp_desk002");
		cells[1][3] = new ConstructionSprite("cstructp_desk002");
		cells[1][4] = new ConstructionSprite("cstructp_desk002");
		
		//Set correct animations
		cells[0][0].current("A_r0c0");
		int randInt = rng.nextInt(2);
		if (randInt == 0) cells[0][1].current("A_r0c1");
		else cells[0][1].current("A_r3c0<->A_r3c1");
		cells[0][2].current("A_r2c0");
		randInt = rng.nextInt(2);
		if (randInt == 0) cells[0][3].current("A_r0c2");
		else cells[0][3].current("A_r3c2<->A_r3c3<->A_r2c2");
		cells[0][4].current("A_r0c3");
		cells[1][0].current("A_r1c0");
		cells[1][1].current("A_r1c1");
		cells[1][2].current("A_r2c1");
		cells[1][3].current("A_r1c2");
		randInt = rng.nextInt(2);
		if (randInt == 0) cells[1][4].current("A_r1c3");
		else cells[1][4].current("A_r2c3");
		
		//Set collision matrix and use points
		for (int r = 0; r < h; r++) {
			for (int c = 0; c < w; c++) {
				collisionMatrix[r][c] = false;
				usePoints[r][c] = false;
			}
		}
		collisionMatrix[0][1] = true;
		collisionMatrix[0][2] = true;
		collisionMatrix[0][3] = true;
		collisionMatrix[1][0] = true;
		collisionMatrix[1][4] = true;
		
		usePoints[1][2] = true;
		
		
		//Set outlinePolgyon
		int tw = ConstructionSprite.WIDTH;
		int th = ConstructionSprite.HEIGHT;
		
		outlinePoly = new Polygon();
		outlinePoly.setAllowDuplicatePoints(false);
		
		outlinePoly.addPoint((float) 0, (float) (World.convertToEntityPixels(1 * th)));
		outlinePoly.addPoint((float) (World.convertToEntityPixels(1 * tw)), (float) 0);
		outlinePoly.addPoint((float) (World.convertToEntityPixels(4 * tw)), (float) 0);
		outlinePoly.addPoint((float) (World.convertToEntityPixels(5 * tw)), (float) (World.convertToEntityPixels(1 * th)));
		outlinePoly.addPoint((float) (World.convertToEntityPixels(5 * tw)), (float) (World.convertToEntityPixels(2 * th)));
		outlinePoly.addPoint((float) 0, (float) (World.convertToEntityPixels(2 * th)));
		
		outlinePivots.add(new Vector2f(0, 0));
		outlinePivots.add(new Vector2f((float) World.convertToEntityPixels((w - 3) * tw), 0));
		outlinePivots.add(new Vector2f((float) World.convertToEntityPixels(w * tw), (float) (World.convertToEntityPixels(h * th))));
		outlinePivots.add(new Vector2f(0, (float) (World.convertToEntityPixels((h + 3) * th))));
		
		outlinePoly.setX(0);
		outlinePoly.setY(0);
		
		outlinePoly.setClosed(true);
	}
	
	@Override
	public int getType() {
		return TYPE;
	}
	
	public static final int WIDTH = 160;
	public static final int HEIGHT = 64;
	private static final int TYPE = 0;
}
