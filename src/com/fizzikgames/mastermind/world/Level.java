package com.fizzikgames.mastermind.world;

import org.newdawn.slick.util.Log;

import com.fizzikgames.mastermind.asset.AssetLoader;
import com.fizzikgames.mastermind.entity.sprite.SpriteSheet;

public class Level {
	public Level(String tileset, String extras) {
		this.tileset = null;
		this.extras = null;
		
		SpriteSheet loadTileSet = AssetLoader.spritesheet(tileset);
		SpriteSheet loadExtraSet = AssetLoader.spritesheet(extras);
		if (loadTileSet != null) {
			this.tileset = loadTileSet;
		}
		else {
			Log.error("Unable to find tileset: " + tileset);
		}
		if (loadExtraSet != null) {
			this.extras = loadExtraSet;
		}
		else {
			Log.error("Unable to find extras tileset: " + tileset);
		}
		
		
		tiles = new short[HEIGHT][WIDTH];
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 0; c < WIDTH; c++) {
				tiles[r][c] = Tile.createTile((short) 1);
			}
		}
		for (int r = 40; r < 60; r++) {
			for (int c = 40; c < 60; c++) {
				tiles[r][c] = Tile.createTile((short) 0);
			}
		}
		tiles[45][45] = Tile.createTile((short) 1);
		tiles[46][46] = Tile.createTile((short) 1);
		tiles[45][47] = Tile.createTile((short) 1);
		tiles[44][46] = Tile.createTile((short) 1);
	}
	
	public short getTile(int r, int c) {
		return tiles[r][c];
	}
	
	public void setTile(short tile, int r, int c) {
		tiles[r][c] = tile;
	}
	
	public SpriteSheet getTileSet() {
		return tileset;
	}
	
	public SpriteSheet getExtras() {
		return extras;
	}
	
	public static final int WIDTH = 100;
	public static final int HEIGHT = 100;
	private short tiles[][];
	private SpriteSheet tileset;
	private SpriteSheet extras;
}
