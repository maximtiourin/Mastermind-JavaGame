package com.fizzikgames.mastermind.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

import com.fizzikgames.fizlib.string.StringUtil;
import com.fizzikgames.mastermind.util.Config;
import com.fizzikgames.mastermind.Controls;
import com.fizzikgames.mastermind.GameLogic;
import com.fizzikgames.mastermind.Settings;

/**
 * The Configuration class loads the configuration file and then offers static methods for getting its contents.
 * @author Maxim Tiourin
 */
public class Config {
	Config() {
		configString = new ArrayList<ArrayList<String>>();
		sectionLabels = new ArrayList<String>();
	}
	
	public void init() {
		File cfile = new File(path);
		File ocfile = new File(backup);
		try {
			if (cfile.exists()) {
				readConfig(cfile);
				if (!isSameVersion()) {
					if (ocfile.exists()) ocfile.delete();
					cfile.renameTo(ocfile);
					cfile = new File(path);
					
					//Create new config file
					createNewConfigFromBackup(cfile, ocfile);
					readConfig(cfile);
				}
			}
			else {
				createNewConfig(cfile);
				readConfig(cfile);
			}
		}
		catch (IOException e) {}
	}
	
	private void createNewConfig(File file) throws IOException {
		configString = new ArrayList<ArrayList<String>>();
		sectionLabels = new ArrayList<String>();
		
		if (file == null) {
			file = new File(path);
		}
		
		file.createNewFile();
		
		FileOutputStream fout = new FileOutputStream(file);
		PrintStream out = new PrintStream(fout);
		
		//Default Config file
		out.println("[Information]");
		out.println("version=" + GameLogic.VERSION);
		out.println("[Display Settings]");
		out.println("width=800");
		out.println("height=600");
		out.println("fullscreen=false");
		out.println("vsync=true");
		out.println("[Gameplay Settings]");
		out.println("toggle_gui=" + Settings.toggle_gui);
		out.println("sound_effect_volume=" + Settings.sound_effect_volume);
		out.println("[Controls]");
		out.println("scroll_up=" + Controls.scroll_up);
		out.println("scroll_down=" + Controls.scroll_down);
		out.println("scroll_left=" + Controls.scroll_left);
		out.println("scroll_right=" + Controls.scroll_right);
		out.println("num_1=" + Controls.num_1);
		out.println("num_2=" + Controls.num_2);
		out.println("num_3=" + Controls.num_3);
		out.println("num_4=" + Controls.num_4);
		out.println("num_5=" + Controls.num_5);
		out.println("num_6=" + Controls.num_6);
		out.println("num_7=" + Controls.num_7);
		out.println("num_8=" + Controls.num_8);
		out.println("num_9=" + Controls.num_9);
		out.println("num_0=" + Controls.num_0);
		
		out.close();
	}
	
	/**
	 * Creates a brand new config file while using some values from the backup
	 */
	public void createNewConfigFromBackup(File file, File ocfile) throws IOException {
		configString = new ArrayList<ArrayList<String>>();
		sectionLabels = new ArrayList<String>();
		
		readConfig(ocfile);
		
		if (file == null) {
			file = new File(path);
		}
		
		file.createNewFile();
		
		FileOutputStream fout = new FileOutputStream(file);
		PrintStream out = new PrintStream(fout);
		
		//Default Config file
		out.println("[Information]");
		out.println("version=" + GameLogic.VERSION);
		out.println("[Display Settings]");
		out.println("width=800");
		out.println("height=600");
		out.println("fullscreen=false");
		out.println("vsync=true");
		out.println("[Gameplay Settings]");
		out.println("toggle_gui=" + Settings.toggle_gui);
		out.println("sound_effect_volume=" + Settings.sound_effect_volume);
		out.println("[Controls]");
		out.println("scroll_up=" + Controls.scroll_up);
		out.println("scroll_down=" + Controls.scroll_down);
		out.println("scroll_left=" + Controls.scroll_left);
		out.println("scroll_right=" + Controls.scroll_right);
		out.println("move_up=" + Controls.move_up);
		out.println("move_down=" + Controls.move_down);
		out.println("move_left=" + Controls.move_left);
		out.println("move_right=" + Controls.move_right);
		out.println("num_1=" + Controls.num_1);
		out.println("num_2=" + Controls.num_2);
		out.println("num_3=" + Controls.num_3);
		out.println("num_4=" + Controls.num_4);
		out.println("num_5=" + Controls.num_5);
		out.println("num_6=" + Controls.num_6);
		out.println("num_7=" + Controls.num_7);
		out.println("num_8=" + Controls.num_8);
		out.println("num_9=" + Controls.num_9);
		out.println("num_0=" + Controls.num_0);
		
		out.close();
		
		ocfile.delete();
		configString = new ArrayList<ArrayList<String>>();
		sectionLabels = new ArrayList<String>();
	}
	
	private void readConfig(File file) throws IOException {
		FileInputStream fin = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fin);
		BufferedReader in = new BufferedReader(isr);
		
		String line = "";
		while ((line = in.readLine()) != null) {
			if (line.contains("[")) {
				configString.add(new ArrayList<String>());
				sectionLabels.add(StringUtil.substring(line, "[", "]", true));
			}
			else {
				configString.get(Math.max(configString.size() - 1, 0)).add(line);
			}
		}
		
		in.close();
	}
	
	public boolean isSameVersion() {
		String current = "" + GameLogic.VERSION;
		String old = valueAt("Information", "version");
		
		if (current.equals(old)) return true;
		
		return false;
	}
	
	public ArrayList<ArrayList<String>> getEntireStringConfig() {
		return configString;
	}
	
	private String valueAt(String section, String property) {
		for (int i = 0; i < configString.size(); i++) {
			if (sectionLabels.get(i).equals(section)) {
				for (String e : configString.get(i)) {
					if (e.contains(property)) {
						String cmp[] = e.split("=");
						if (cmp[0].equals(property)) {
							return StringUtil.substring(e, "=", true);
						}
					}
				}
			}
		}
		
		return null;
	}
	
	public String stringAt(String section, String property) {
		return valueAt(section, property);
	}
	
	public int integerAt(String section, String property) {
		return Integer.valueOf(valueAt(section, property));
	}
	
	public boolean booleanAt(String section, String property) {
		return Boolean.parseBoolean(valueAt(section, property));
	}
	
	public float floatAt(String section, String property) {
		return Float.valueOf(valueAt(section, property));
	}
	
	public static Config get() {
		return config;
	}
	
	private static final Config config = new Config();
	private ArrayList<ArrayList<String>> configString;
	private ArrayList<String> sectionLabels;
	private String path = "config.ini";
	private String backup = "config_old.ini";
}
