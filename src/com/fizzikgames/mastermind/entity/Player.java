package com.fizzikgames.mastermind.entity;

import com.fizzikgames.mastermind.world.World;

public class Player {
	public Player(World world) {
		this.world = world;
		this.funds = 0;
	}
	
	public long getFunds() {
		return funds;
	}
	
	public void setFunds(long f) {
		funds = f;
	}
	
	private World world;
	private long funds;
}
