package com.fizzikgames.mastermind.gui;

import org.newdawn.slick.Color;

import com.fizzikgames.mastermind.GameLogic;
import com.fizzikgames.mastermind.asset.AssetLoader;

public class GameplayGUI extends GUI {
	public GameplayGUI() {
		super();
	}
	
	@Override
	protected void init() {
		System.out.println("GameplayGUI Initialized!");
		
		//Helper Vars
		final int window_w = GameLogic.WINDOWED_WIDTH;
		final int window_h = GameLogic.WINDOWED_HEIGHT;
		GUI_Tooltip ttip = null;
		GUI_Element element = null;
		GUI_Element element2 = null;
		
		//Setup Container
		container = new GUI_Container(0, 0);
		
		/*Add elements*/
			//Primary UI pane
		//container.addElement(new GUI_Element_Image(container, "gui_image_gameplayPane", AssetLoader.image("ui_gui_gameplaypane"), 0f, 0f, 1, true, true));
			//Top gui bar
		container.addElement(new GUI_Element_Image(container, "gui_image_topguibar", AssetLoader.image("ui_gui_longbar"), null, 0f, 0f, GUI_Element.RENDER_IMAGE, true, true));
			//Top Right corner
		container.addElement(new GUI_Element_Image(container, "gui_image_rightcornertab", AssetLoader.image("ui_gui_cornertab"), null, (float) window_w - 200f, 0f, GUI_Element.RENDER_IMAGE, true, true));
			//Seperator between top gui bar and top right corner
		container.addElement(new GUI_Element_Image(container, "gui_image_toprightseperator", AssetLoader.image("ui_gui_seperator"), null, (float) window_w - 208f, 0f, GUI_Element.RENDER_IMAGE, true, true));
			//Currency at top right of UI
		ttip = new GUI_Tooltip("Amount of funds you currently have. Get more funds by selling research or running operations.", AssetLoader.font("ui_font_dejavuserif", 14), 250, Color.black, Color.white, Color.white);
		currencyAmountLabel = new GUI_Element_Label(container, "gui_label_currencyAmount", "", AssetLoader.font("ui_font_data", 18), Color.white, ttip, (float) window_w - 190f, 4f, GUI_Element.RENDER_LABEL, true, true);
		ttip.setParent(currencyAmountLabel);
		container.addElement(currencyAmountLabel);		
			//Options Menu Button at top right of UI
		ttip = new GUI_Tooltip("Options Menu", AssetLoader.font("ui_font_dejavuserif", 14), 250, Color.black, Color.white, Color.white);
		optionsMenuButton = new GUI_Element_Button(container, "gui_button_optionsmenu", AssetLoader.image("ui_gui_button01"), AssetLoader.image("ui_gui_buttonhighlight"), null, ttip, AssetLoader.sound("sound_effect_ui_buttonclick"), window_w - 150f, 75f, 15, 15, GUI_Element.RENDER_BUTTON, true, true);
		ttip.setParent(optionsMenuButton);
		container.addElement(optionsMenuButton);		
			//Test ImageLabel
		/*ttip = new GUI_Tooltip("This is a test Image Label Tooltip.", AssetLoader.font("ui_font_dejavuserif", 14), 250, Color.black, Color.white, Color.white);
		element = new GUI_Element_ImageLabel(container, "gui_imagelabel_test", ttip, 300f, 300f, GUI_Element.RENDER_IMAGE, true, true);
		ttip.setParent(element);
		((GUI_Element_ImageLabel) element).addElement(new GUI_Element_Image(container, "123", AssetLoader.image("ui_gui_cornertab"), null, (float) 300f, 300f, 1, true, true));
		((GUI_Element_ImageLabel) element).addElement(new GUI_Element_Label(container, "456", "This is a test label for the imagelabel element.", AssetLoader.font("ui_font_dejavuserif", 16), Color.white, null, 400f, 330f, 2, true, true));
		((GUI_Element_ImageLabel) element).addElement(new GUI_Element_Button(container, "789", AssetLoader.image("ui_gui_button01"), AssetLoader.image("ui_gui_buttonhighlight"), null, null, AssetLoader.sound("sound_effect_ui_buttonclick"), 325f, 315f, 75, 25, 3, true, true));
		container.addElement(element);*/
			//Test Vertical scrollarea
		element = new GUI_Element_VerticalScrollArea(container, "gui_verticalscroll_test", AssetLoader.image("ui_gui_longbar"), 300f, 300f, 300, 125, 25, GUI_Element.RENDER_IMAGE, true, true);
		element2 = new GUI_Element_Image(container, "123", AssetLoader.image("ui_gui_cornertab"), null, (float) 300f, 300f, 1, true, true);
		((GUI_Element_VerticalScrollArea) element).addElement(element2);
		ttip = new GUI_Tooltip("label 0", AssetLoader.font("ui_font_dejavuserif", 14), 250, Color.black, Color.white, Color.white);
		element2 = new GUI_Element_Label(container, "456", "This is a test label for the scroll area element.", AssetLoader.font("ui_font_dejavuserif", 16), Color.white, ttip, 400f, 330f, 2, true, true);
		ttip.setParent(element2);
		((GUI_Element_VerticalScrollArea) element).addElement(element2);
		ttip = new GUI_Tooltip("button 0", AssetLoader.font("ui_font_dejavuserif", 14), 250, Color.black, Color.white, Color.white);
		element2 = new GUI_Element_Button(container, "789", AssetLoader.image("ui_gui_button01"), AssetLoader.image("ui_gui_buttonhighlight"), null, ttip, AssetLoader.sound("sound_effect_ui_buttonclick"), 325f, 315f, 75, 25, 3, true, true);
		ttip.setParent(element2);
		((GUI_Element_VerticalScrollArea) element).addElement(element2);
		ttip = new GUI_Tooltip("button 1", AssetLoader.font("ui_font_dejavuserif", 14), 250, Color.black, Color.white, Color.white);
		element2 = new GUI_Element_Button(container, "78912", AssetLoader.image("ui_gui_button01"), AssetLoader.image("ui_gui_buttonhighlight"), null, ttip, AssetLoader.sound("sound_effect_ui_buttonclick"), 325f, 375f, 75, 25, 3, true, true);
		ttip.setParent(element2);
		((GUI_Element_VerticalScrollArea) element).addElement(element2);
		ttip = new GUI_Tooltip("button 2", AssetLoader.font("ui_font_dejavuserif", 14), 250, Color.black, Color.white, Color.white);
		element2 = new GUI_Element_Button(container, "7891212", AssetLoader.image("ui_gui_button01"), AssetLoader.image("ui_gui_buttonhighlight"), null, ttip, AssetLoader.sound("sound_effect_ui_buttonclick"), 325f, 435f, 75, 25, 3, true, true);
		ttip.setParent(element2);
		((GUI_Element_VerticalScrollArea) element).addElement(element2);
		ttip = new GUI_Tooltip("button 3", AssetLoader.font("ui_font_dejavuserif", 14), 250, Color.black, Color.white, Color.white);
		element2 = new GUI_Element_Button(container, "789121212", AssetLoader.image("ui_gui_button01"), AssetLoader.image("ui_gui_buttonhighlight"), null, ttip, AssetLoader.sound("sound_effect_ui_buttonclick"), 325f, 500f, 75, 25, 3, true, true);
		ttip.setParent(element2);
		((GUI_Element_VerticalScrollArea) element).addElement(element2);
		ttip = new GUI_Tooltip("Enter some text in this textbox element!", AssetLoader.font("ui_font_dejavuserif", 14), 250, Color.black, Color.white, Color.white);
		element2 = new GUI_Element_TextBox(container, "textboxtest", "Username", AssetLoader.font("ui_font_dejavuserif", 14), 20, Color.white, Color.black, Color.black, ttip, 325f, 545f, 100, 25, 4, true, true, true);
		((GUI_Element_TextBox) element2).setEnterButton(optionsMenuButton);
		ttip.setParent(element2);
		((GUI_Element_VerticalScrollArea) element).addElement(element2);
		container.addElement(element);
	}
	
	public GUI_Element_Label getCurrencyAmountLabel() {
		return currencyAmountLabel;
	}
	
	public GUI_Element_Button getOptionsMenuButton() {
		return optionsMenuButton;
	}
	
	/* Dynamic Labels */
	private GUI_Element_Label currencyAmountLabel;
	private GUI_Element_Button optionsMenuButton;
}
