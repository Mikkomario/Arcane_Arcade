package arcane_arcade_menus;

import utopia_worlds.Area;
import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.SettingUsingArea;
import arcane_arcade_worlds.SettingUsingAreaObjectCreator;

/**
 * TutorialScreenObjectCreator creates the objects in the tutorial selection 
 * screen
 * 
 * @author Mikko Hilpinen
 * @since 7.5.2014
 */
public class TutorialScreenObjectCreator extends SettingUsingAreaObjectCreator
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new TutorialScreenObjectCreator. The objects will be created 
	 * once the room starts
	 * 
	 * @param area The area where the objects will be created
	 * @param navigator The navigator that handles the transitions between the areas
	 */
	public TutorialScreenObjectCreator(SettingUsingArea area, Navigator navigator)
	{
		super(area, "space", "background", GameSettings.SCREENWIDTH, 
				GameSettings.SCREENHEIGHT, null, navigator);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	protected void onSettingsChange(AreaSetting newSettings)
	{
		// Does nothing
	}

	@Override
	protected void createObjects(Area area)
	{
		// Playes the music
		new MenuThemePlayer(area, 1);
		
		// Creates the basic objects
		// Creates menucorners
		new MenuCornerCreator(area, false);
		// Creates background effects
		new MenuBackgroundEffectCreator(area);
		// Creates navigation button
		new SimplePhaseChangeButton(GameSettings.SCREENWIDTH - 100, 
				GameSettings.SCREENHEIGHT / 2, "mainmenu", getNavigator(), area);
	}

}
