package com.fizzikgames.mastermind.world.construction;

import com.fizzikgames.mastermind.entity.Entity;
import com.fizzikgames.mastermind.world.World;

public abstract class CAbstruct_Desk extends Construction {
	public CAbstruct_Desk(World world, double width, double height) {
		super(world, width, height, Entity.RENDER_CONSTRUCTION);
	}
	
	@Override
	public abstract void initCells();
	@Override
	public abstract int getType();
	
	@Override
	public Construction getNextType() {
		return CType_Desk.get(world, getType() + 1);
	}
}
