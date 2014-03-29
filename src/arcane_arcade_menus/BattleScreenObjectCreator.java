package arcane_arcade_menus;

import java.awt.geom.Point2D;

import utopia_worlds.Area;
import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.SettingUsingArea;
import arcane_arcade_worlds.SettingUsingAreaObjectCreator;

/**
 * BattleScreenObjectCreator creates the objects needed in the battle screen at 
 * the start of the room.
 * 
 * @author Unto Solala & Mikko Hilpinen
 * @since 4.9.2013
 */
public class BattleScreenObjectCreator extends SettingUsingAreaObjectCreator
{
	// ATTRIBUTES-----------------------------------------------------------
	
	private Navigator navigator;
	private BattleSettingScreenInterface barhandler;
	
	
	// CONSTRUCTOR---------------------------------------------------------
	
	/**
	 * Creates a new BattleScreenObjectCreator.  
	 * The creator will create the objects when the room starts.
	 * @param battleScreen The battleScreen where the objects will be created at
	 * @param navigator The navigator that will handle area transitions
	 */
	public BattleScreenObjectCreator(SettingUsingArea battleScreen, 
			Navigator navigator)
	{
		super(battleScreen, "space", "background", GameSettings.SCREENWIDTH, 
				GameSettings.SCREENHEIGHT, null);
		
		// Initializes attributes
		this.navigator = navigator;
		this.barhandler = null;
	}

	
	// IMPLMENTED METHODS ---------------------------------------------
	
	@Override
	protected void onSettingsChange(AreaSetting newSettings)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createObjects(Area area)
	{
		new MenuThemePlayer(area, 1);
		new MenuBackgroundEffectCreator(area);
		new MenuCornerCreator(area, true);
		this.barhandler = new BattleSettingScreenInterface(area);
		new ToElementScreenButton(area);
		new SimplePhaseChangeButton(100, GameSettings.SCREENHEIGHT - 100, 
				"mainmenu", this.navigator, area).setXScale(-1);
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	/**
	 * This button changes the game phase to element selection screen when its 
	 * pressed.
	 *
	 * @author Mikko Hilpinen.
	 *         Created 1.12.2013.
	 */
	private class ToElementScreenButton extends MenuButton
	{
		// CONSTRUCTOR	--------------------------------------------------
		
		/**
		 * Creates a new button that will take the user to element screen upon 
		 * click.
		 * 
		 * @param area The room to which the object is created
		 */
		public ToElementScreenButton(Area area)
		{
			super(GameSettings.SCREENWIDTH - 100, 
					GameSettings.SCREENHEIGHT - 100, "To element selection", area);
		}
		
		
		// IMPLEMENTED METHODS	------------------------------------------

		@Override
		public void onMouseButtonEvent(MouseButton button,
				MouseButtonEventType eventType, Point2D.Double mousePosition,
				double eventStepTime)
		{
			// On left pressed goes to the element selection screen
			if (button == MouseButton.LEFT && 
					eventType == MouseButtonEventType.PRESSED)
				BattleScreenObjectCreator.this.navigator.startPhase(
						"elementmenu", 
						BattleScreenObjectCreator.this.barhandler.createFieldSetting());
		}
	}
}
