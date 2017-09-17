package com.fizzikgames.mastermind.asset;

import java.io.IOException;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

/**
 * An asset that contains an Image resource.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class AssetImage extends Asset {
	private Image image;
	
	public AssetImage(String id, String path) {
		super(id, path);
		image = null;
	}
	
	@Override
	public void load() throws IOException {
		try {
			image = new Image(path);
		} catch (SlickException e) {
			Log.error("Unable to load image asset: " + path, e);
		}
	}
	
	public Image getImage() {
		return image;
	}
}
