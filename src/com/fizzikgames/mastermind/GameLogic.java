package com.fizzikgames.mastermind;

import java.util.ArrayList;

import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;
import org.newdawn.slick.util.Log;

import com.fizzikgames.mastermind.asset.AssetLoader;
import com.fizzikgames.mastermind.util.Config;
import com.fizzikgames.mastermind.world.World;

/**
 * The main game logic that initializes the game and then handles primary game loops as well
 * as initialization for utility classes.
 * @author Maxim Tiourin
 * @version 1.00
 */
public class GameLogic implements Game {
	////////////////////////////////////////////////////////////
	public static final String TITLE = "Mastermind";
	public static final String AUTHOR = "Maxim Tiourin";
	public static final String VERSION = "0.0.0.12";
	public static int WINDOWED_WIDTH = 800; //1920
	public static int WINDOWED_HEIGHT = 600; //1080
	public static boolean FULLSCREEN = false;
	public static boolean VSYNC = false;
	public static AppGameContainer GUI_CONTEXT;
	private static final int MIN_LOGIC = 10;
	private static final int MAX_LOGIC = 10;
	//private static final int FPS = 100; //200
	protected AppGameContainer gameContainer;
	////////////////////////////////////////////////////////////
	private int gameState;
	private World world;
	private ArrayList<Renderable> renderables;
	//GameStates////////////////////////////////////////////////
	private static final int STATE_LOAD = 0;
	//private static final int STATE_SPLASH = 1;
	private static final int STATE_START_PLAY = 2;
	private static final int STATE_PLAY = 3;
	////////////////////////////////////////////////////////////
	
	public GameLogic() {
		renderables = new ArrayList<Renderable>();
		Config.get().init();
		WINDOWED_WIDTH = Config.get().integerAt("Display Settings", "width");
		WINDOWED_HEIGHT = Config.get().integerAt("Display Settings", "height");
		FULLSCREEN = Config.get().booleanAt("Display Settings", "fullscreen");
		VSYNC = Config.get().booleanAt("Display Settings", "vsync");
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		Settings.get().init();
		Controls.get().init();
		
		gameState = STATE_LOAD;
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {			
		switch (gameState) {
			case STATE_LOAD: {
				//Load all assets
				if (!AssetLoader.isReady()) {
					AssetLoader.load();
					renderables.add(AssetLoader.get());
				}				
				
				AssetLoader.update(gc);
				
				if (AssetLoader.isLoaded()) {
					renderables.remove(AssetLoader.get());
					gameState = STATE_START_PLAY;
				}
				
				break;
			}
			case STATE_START_PLAY: {
				//Set up World
				world = new World();
				Thread worldThread = new Thread(world);				
				renderables.add(world);
				worldThread.start();
				
				gameState = STATE_PLAY;
				
				break;
			}
			case STATE_PLAY: {
				//Update World
				world.update(gc, delta);
				
				break;
			}
		}
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setWorldClip(0, 0, WINDOWED_WIDTH, WINDOWED_HEIGHT);
		
		for (Renderable e : renderables) {
			e.render(gc, g);
		}
	}
	
	@Override
	public boolean closeRequested() {
		gameContainer.exit();
		return true;
	}
	
	@Override
	public String getTitle() {
		return TITLE;
	}
	
	public static void main(String[] args) throws SlickException {
		//Initialize Game Container
		GameLogic gl = new GameLogic();
		Renderer.setRenderer(Renderer.VERTEX_ARRAY_RENDERER);
		AppGameContainer window = new AppGameContainer(gl);
		gl.gameContainer = window;
		GameLogic.GUI_CONTEXT = window;
		window.setDisplayMode(WINDOWED_WIDTH, WINDOWED_HEIGHT, true);
		//window.setVSync(VSYNC);
		//window.setTargetFrameRate(200);
		window.setMinimumLogicUpdateInterval(MIN_LOGIC);
		window.setMaximumLogicUpdateInterval(MAX_LOGIC);
		window.setSoundOn(true);
		window.setMusicOn(true);
		//window.setMusicVolume(0f);
        window.start();
        
        Log.setVerbose(true);
	}
}
