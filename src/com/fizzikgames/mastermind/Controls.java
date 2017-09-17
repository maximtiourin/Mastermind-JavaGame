package com.fizzikgames.mastermind;

import org.newdawn.slick.Input;

import com.fizzikgames.mastermind.util.Config;

/**
 * Static class to hold the user's control settings
 * @author Maxim Tiourin
 */
public class Controls {
	public Controls() {
		
	}
	
	public void init() {
		scroll_up = Config.get().integerAt("Controls", "scroll_up");
		scroll_down = Config.get().integerAt("Controls", "scroll_down");
		scroll_left = Config.get().integerAt("Controls", "scroll_left");
		scroll_right = Config.get().integerAt("Controls", "scroll_right");
		move_up = Config.get().integerAt("Controls", "move_up");
		move_down = Config.get().integerAt("Controls", "move_down");
		move_left = Config.get().integerAt("Controls", "move_left");
		move_right = Config.get().integerAt("Controls", "move_right");
		num_1 = Config.get().integerAt("Controls", "num_1");
		num_2 = Config.get().integerAt("Controls", "num_2");
		num_3 = Config.get().integerAt("Controls", "num_3");
		num_4 = Config.get().integerAt("Controls", "num_4");
		num_5 = Config.get().integerAt("Controls", "num_5");
		num_6 = Config.get().integerAt("Controls", "num_6");
		num_7 = Config.get().integerAt("Controls", "num_7");
		num_8 = Config.get().integerAt("Controls", "num_8");
		num_9 = Config.get().integerAt("Controls", "num_9");
		num_0 = Config.get().integerAt("Controls", "num_0");
	}
	
	public static Controls get() {
		return controls;
	}
	
	public static final Controls controls = new Controls();
	public static int scroll_up = Input.KEY_UP;
	public static int scroll_down = Input.KEY_DOWN;
	public static int scroll_left = Input.KEY_LEFT;
	public static int scroll_right = Input.KEY_RIGHT;
	public static int move_up = Input.KEY_W;
	public static int move_down = Input.KEY_S;
	public static int move_left = Input.KEY_A;
	public static int move_right = Input.KEY_D;
	public static int show_gui = Input.KEY_E;
	public static int num_1 = Input.KEY_1;
	public static int num_2 = Input.KEY_2;
	public static int num_3 = Input.KEY_3;
	public static int num_4 = Input.KEY_4;
	public static int num_5 = Input.KEY_5;
	public static int num_6 = Input.KEY_6;
	public static int num_7 = Input.KEY_7;
	public static int num_8 = Input.KEY_8;
	public static int num_9 = Input.KEY_9;
	public static int num_0 = Input.KEY_0;
}
