package com.fizzikgames.mastermind.asset;

import java.io.IOException;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.fizzikgames.mastermind.entity.sprite.SpriteSheet;
import com.fizzikgames.mastermind.world.Tile;

/**
 * An asset that contains a SpriteSheet resource.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class AssetSpriteSheet extends Asset {
	private SpriteSheet spritesheet;
	
	public AssetSpriteSheet(String id, String path) {
		super(id, path);
		spritesheet = null;
	}
	
	@Override
	public void load() throws IOException {
		try {
			spritesheet = new SpriteSheet(new Image(path), Tile.WIDTH, Tile.HEIGHT);
		} catch (SlickException e) {
			Log.error("Unable to load spritesheet asset: " + path, e);
		}
	}
	
	public SpriteSheet getSpriteSheet() {
		return spritesheet;
	}
}
