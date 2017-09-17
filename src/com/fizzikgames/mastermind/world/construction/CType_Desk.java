package com.fizzikgames.mastermind.world.construction;

import com.fizzikgames.mastermind.world.World;

public class CType_Desk {
	public CType_Desk() {
		
	}
	
	public static Construction get(World world, int type) {
		if (type > types - 1) type = 0;
		
		switch (type) {
			case DESK01: {
				return new CStruct_Desk01(world);
			}
			case DESK02: {
				return new CStruct_Desk02(world);
			}
		}
		
		return new CStruct_Desk01(world);
	}
	
	private static final int types = 2;
	private static final int DESK01 = 0;
	private static final int DESK02 = 1;
}
