package com.fizzikgames.mastermind;

import com.fizzikgames.mastermind.util.Config;

/**
 * Holds Gameplay Settings for the user
 * @author Maxim Tiourin
 */
public class Settings {
	public Settings() {
		
	}
	
	public void init() {
		toggle_gui = Config.get().booleanAt("Gameplay Settings", "toggle_gui");
		sound_effect_volume = Config.get().floatAt("Gameplay Settings", "sound_effect_volume");
	}
	
	public static Settings get() {
		return settings;
	}
	
	private static final Settings settings = new Settings();
	
	public static boolean toggle_gui = true;
	public static float sound_effect_volume = 1.00f;
}
