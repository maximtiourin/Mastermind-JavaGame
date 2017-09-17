package com.fizzikgames.mastermind.world.construction;

import com.fizzikgames.mastermind.world.World;

public class CType_ExecutiveDesk {
	public CType_ExecutiveDesk() {
		
	}
	
	public static Construction get(World world, int type) {
		if (type > types - 1) type = 0;
		
		switch (type) {
			case EXECDESK01: {
				return new CStruct_ExecutiveDesk01(world);
			}
		}
		
		return new CStruct_ExecutiveDesk01(world);
	}
	
	private static final int types = 1;
	private static final int EXECDESK01 = 0;
}
