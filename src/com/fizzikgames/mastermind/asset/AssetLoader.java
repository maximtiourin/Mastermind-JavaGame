package com.fizzikgames.mastermind.asset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.util.Log;

import com.fizzikgames.mastermind.GameLogic;
import com.fizzikgames.mastermind.Renderable;
import com.fizzikgames.mastermind.entity.sprite.SpriteSheet;

/**
 * The asset loader is in charge of loading every resource that the game intends to use,
 * such as images, spritesheets, sounds, fonts, interface files, scripts, etc.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class AssetLoader implements Renderable {
	private static final AssetLoader AL = new AssetLoader();
	private static HashMap<String, Asset> assets;
	private static ArrayList<DeferredResource> loadList;
	private static int totalToLoad = 0;
	private static boolean ready = false;
	private static boolean loading = false;
	private static boolean loaded = false;
	private static String description = "";
	
	public AssetLoader() {
		assets = new HashMap<String, Asset>();
		loadList = new ArrayList<DeferredResource>();
	}
	
	public static void load() {
		ArrayList<Asset> assetList=  new ArrayList<Asset>();
		
			//Fonts
		assetList.add(new AssetFont("ui_font_data", "assets/fonts/Data.ttf"));
		assetList.add(new AssetFont("ui_font_dejavuserif", "assets/fonts/DejaVuSerif.ttf"));
		
			//GUI
		assetList.add(new AssetImage("ui_gui_longbar", "assets/images/ui/gui/longbar.png"));
		assetList.add(new AssetImage("ui_gui_cornertab", "assets/images/ui/gui/cornertab.png"));
		assetList.add(new AssetImage("ui_gui_seperator", "assets/images/ui/gui/seperator.png"));
		assetList.add(new AssetImage("ui_gui_button01", "assets/images/ui/gui/button01.png"));
		assetList.add(new AssetImage("ui_gui_buttonhighlight", "assets/images/ui/gui/buttonhighlight.png"));
		
		// Load Tilesets
		assetList.add(new AssetSpriteSheet("tileset_001", "assets/images/tileset/tileset001.png"));
		assetList.add(new AssetSpriteSheet("tileset_extras", "assets/images/tileset/tilesetextras001.png"));

		// Load Images
			//Characters
		assetList.add(new AssetSpriteSheet("character_scientist", "assets/images/character/scientist.png"));
		assetList.add(new AssetImage("testabc", "assets/images/character/testchar.png"));
			//Objects
		assetList.add(new AssetImage("testcase", "assets/images/object/testcase.png"));
			//Constructions
		assetList.add(new AssetSpriteSheet("cstructp_desk001", "assets/images/construction/desk001.png"));
		assetList.add(new AssetSpriteSheet("cstructp_desk002", "assets/images/construction/desk002.png"));
		
		// Load Sounds
			//Effects
				//UI
					assetList.add(new AssetSound("sound_effect_ui_buttonclick", "assets/sounds/effects/ui/buttonclick.wav"));
					
		/* Convert Asset List to Hash Map using asset ids as keys*/
		for (Asset e : assetList) {
			assets.put(e.getId(), e);
		}
					
		totalToLoad = loadList.size();

		ready = true;
	}
	
	public static void update(GameContainer gc) {
		if (ready) {			
			if (loadList.size() > 0) {
				loading = true;
				DeferredResource next = loadList.remove(0);
				description = next.getDescription();
				//System.out.println("Loading Resource out of " + totalToLoad + "! " + description);
				try {
					next.load();
				} catch (IOException e) {
					Log.error("Unable to defer load Asset: " + description, e);
				}
			}
			else {
				loading = false;
				loaded = true;
			}
		}
	}
	
	public void render(GameContainer gc, Graphics g) {
		if (ready) {
			if (loading) {
				//Draw Background
				g.setColor(Color.black);
				g.fillRect(0, 0, GameLogic.WINDOWED_WIDTH, GameLogic.WINDOWED_HEIGHT);
				
				//Draw Loading info
				int total = totalToLoad;
				int left = total - loadList.size();
				int percent = (int) (((double) left / (double) total) * 100.00);
				
				g.setColor(Color.white);
				g.drawString("Loading: " + description, 25, 25);
				g.drawString(left + "/" + total + " (" + percent + "%)", 25, 40);
			}
		}
	}
	
	public static AssetLoader get() {
		return AL;
	}
	
	public static ArrayList<DeferredResource> getLoadingList() {
		return loadList;
	}
	
	public static Image image(String id) {
		return ((AssetImage) assets.get(id)).getImage();
	}
	
	public static SpriteSheet spritesheet(String id) {
		return ((AssetSpriteSheet) assets.get(id)).getSpriteSheet();
	}
	
	public static UnicodeFont font(String id, float size) {
		return ((AssetFont) assets.get(id)).getFont(size);
	}
	
	public static Sound sound(String id) {
		return ((AssetSound) assets.get(id)).getSound();
	}
	
	public static boolean isReady() {
		return ready;
	}
	
	public static boolean isLoaded() {
		return loaded;
	}
}
