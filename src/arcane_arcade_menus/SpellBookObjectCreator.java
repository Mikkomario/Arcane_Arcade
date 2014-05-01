package arcane_arcade_menus;

import utopia_worlds.Area;
import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.SettingUsingArea;
import arcane_arcade_worlds.SettingUsingAreaObjectCreator;

/**
 * This objectCreator creates the objects used in the spellBook area
 * 
 * @author Mikko Hilpinen
 * @since 28.4.2014
 */
public class SpellBookObjectCreator extends SettingUsingAreaObjectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Navigator navigator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new spellBookObjectCreator, the objects will be created once 
	 * the area starts
	 * @param area The area where the objects will be created
	 * @param navigator The navigator that handles the transition between different areas
	 */
	public SpellBookObjectCreator(SettingUsingArea area, Navigator navigator)
	{
		super(area, "space", "background", GameSettings.SCREENWIDTH, 
				GameSettings.SCREENHEIGHT, null);
		
		// Initializes attributes
		this.navigator = navigator;
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	protected void onSettingsChange(AreaSetting newSettings)
	{
		// Does nothing
	}

	@Override
	protected void createObjects(Area area)
	{
		// Creates the basic menu elements
		// Starts the music
		new MenuThemePlayer(area, 1);
		// Creates menucorners
		new MenuCornerCreator(area, false);
		// Creates background effects
		new MenuBackgroundEffectCreator(area);
		// Creates navigation button
		new SimplePhaseChangeButton(100, GameSettings.SCREENHEIGHT / 2, 
				"mainmenu", this.navigator, area).setXScale(-1);
		
		// Creates the spellBookInterface
		new SpellBookInterface(area);
	}
}
