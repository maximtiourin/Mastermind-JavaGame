package com.fizzikgames.mastermind.world.construction;

import com.fizzikgames.mastermind.entity.Entity;
import com.fizzikgames.mastermind.world.World;

public abstract class CAbstruct_ExecutiveDesk extends Construction {
	public CAbstruct_ExecutiveDesk(World world, double width, double height) {
		super(world, width, height, Entity.RENDER_CONSTRUCTION);
	}
	
	@Override
	public abstract void initCells();
	@Override
	public abstract int getType();
	
	@Override
	public Construction getNextType() {
		return CType_ExecutiveDesk.get(world, getType() + 1);
	}
}
