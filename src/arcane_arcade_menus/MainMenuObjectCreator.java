package arcane_arcade_menus;

import utopia_worlds.Area;
import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.SettingUsingArea;
import arcane_arcade_worlds.SettingUsingAreaObjectCreator;

/**
 * MainMenuObjectCreator creates the objects needed in the main menu at the 
 * start of the room.
 *
 * @author Mikko Hilpinen.
 * @since 1.9.2013.
 */
public class MainMenuObjectCreator extends SettingUsingAreaObjectCreator
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new mainmenuobjectcreator that will use the given handlers. 
	 * The creator will create the objects when the room starts.
	 * @param mainMenu The main menu where the objects will be created
	 * @param navigator The navigator that handles the transion between the 
	 * gamephases
	 */
	public MainMenuObjectCreator(SettingUsingArea mainMenu, Navigator navigator)
	{
		super(mainMenu, "space", "background", GameSettings.SCREENWIDTH, 
				GameSettings.SCREENHEIGHT, null, navigator);
	}
	
	
	// IMPLMENTED METHODS	---------------------------------------------

	@Override
	protected void onSettingsChange(AreaSetting newSettings)
	{
		// Does nothing
	}

	@Override
	protected void createObjects(Area area)
	{
		// Creates the objects
		// Creates the musicplayer
		new MenuThemePlayer(area, 0);
		// Creates the bakcgroundeffectcreator
		new MenuBackgroundEffectCreator(area);
		// Creates the menucorners
		new MenuCornerCreator(area, false);
		// Creates the MainMenuElements
		new MainMenuMenuCreator(getNavigator());
	}
}
