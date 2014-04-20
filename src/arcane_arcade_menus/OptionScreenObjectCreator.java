package arcane_arcade_menus;

import utopia_worlds.Area;
import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.SettingUsingArea;
import arcane_arcade_worlds.SettingUsingAreaObjectCreator;

/**
 * OptionScreenObjectCreator creates the objects needed in the options screen.
 * 
 * @author Mikko Hilpinen
 * @since 3.3.2014
 */
public class OptionScreenObjectCreator extends SettingUsingAreaObjectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Navigator navigator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new objectCreator. The creator will create objects when the 
	 * room starts
	 * @param optionScreen The option screen where the objects will be created at
	 * @param navigator The navigator that will handle the transitions between 
	 * rooms
	 */
	public OptionScreenObjectCreator(SettingUsingArea optionScreen, 
			Navigator navigator)
	{
		super(optionScreen, "space", "background", GameSettings.SCREENWIDTH, 
				GameSettings.SCREENHEIGHT, null);
		
		// Initializes attributes
		this.navigator = navigator;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected void onSettingsChange(AreaSetting newSettings)
	{
		// Does nothing
	}

	@Override
	protected void createObjects(Area area)
	{
		// Starts the music
		new MenuThemePlayer(area, 1);
		// Creates menucorners
		new MenuCornerCreator(area, false);
		// Creates background effects
		new MenuBackgroundEffectCreator(area);
		// Creates navigation button
		new SimplePhaseChangeButton(100, GameSettings.SCREENHEIGHT / 2, 
				"mainmenu", this.navigator, area).setXScale(-1);
		
		new OptionsInterface(area);
	}
}
