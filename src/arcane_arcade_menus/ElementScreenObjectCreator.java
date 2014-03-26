package arcane_arcade_menus;

import java.awt.geom.Point2D;

import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.FieldSetting;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.SettingUsingArea;
import arcane_arcade_worlds.SettingUsingAreaObjectCreator;

/**
 * ElementScreen Object creator creates the necessary interface elements to 
 * the element screen in which the users will pick the elements they use in 
 * the following battle.
 *
 * @author Mikko Hilpinen.
 * @since 30.11.2013.
 */
public class ElementScreenObjectCreator extends SettingUsingAreaObjectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private FieldSetting settings;
	private Navigator navigator;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new ElementScreenObjectCreator
	 * @param elementScreen The elementScreen where the objects will be created
	 * @param navigator The navigator used to change to different game phases
	 */
	public ElementScreenObjectCreator(SettingUsingArea elementScreen, 
			Navigator navigator)
	{
		super(elementScreen, "space", "background", GameSettings.SCREENWIDTH, 
				GameSettings.SCREENHEIGHT, FieldSetting.class);
		
		// Initializes attributes
		this.settings = null;
		this.navigator = navigator;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected void onSettingsChange(AreaSetting newSettings)
	{
		this.settings = (FieldSetting) newSettings;
	}

	@Override
	protected void createObjects(Area area)
	{
		// Creates the objects
		// Starts the music
		new MenuThemePlayer(area, 2);
		
		new MenuBackgroundEffectCreator(area.getDrawer(), 
				area.getActorHandler(), area);
		new MenuCornerCreator(area.getDrawer(), area.getMouseHandler(), area,
				true);
		new SimplePhaseChangeButton(100, GameSettings.SCREENHEIGHT - 100, 
				"battlesettingmenu", this.navigator, area.getDrawer(), 
				area.getActorHandler(), area.getMouseHandler(), area).setXScale(-1);
		new ToFieldButton(area);
		new ElementSelectionInterface(this, area.getDrawer(), area.getKeyHandler(), area);
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The settings used and modified in this room
	 */
	public FieldSetting getSettings()
	{
		return this.settings;
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	private class ToFieldButton extends MenuButton
	{
		// CONSTRUCTOR	--------------------------------------------------
		
		/**
		 * Creates a new tofieldbutton to the given room
		 *
		 * @param area The room where the button is created at
		 */
		public ToFieldButton(Area area)
		{
			super(GameSettings.SCREENWIDTH - 100, 
					GameSettings.SCREENHEIGHT - 100, 
					area.getDrawer(), 
					area.getActorHandler(),
					area.getMouseHandler(), area, 
					"To field");
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------

		@Override
		public void onMouseButtonEvent(MouseButton button,
				MouseButtonEventType eventType, Point2D.Double mousePosition,
				double eventStepTime)
		{
			// On left pressed goes to the element selection screen
			if (button == MouseButton.LEFT && 
					eventType == MouseButtonEventType.PRESSED && isVisible())
			{
				// Also stops the menumusic
				MultiMediaHolder.getMidiTrackBank("menu").getTrack(
						"maintheme").stop();
				
				ElementScreenObjectCreator.this.navigator.startPhase(
						"field", ElementScreenObjectCreator.this.settings);
			}
		}
		
		@Override
		public boolean isVisible()
		{
			// Overrides the isVisible method so that the button only comes 
			// visible when the next section can be accessed
			
			return super.isVisible() && 
					ElementScreenObjectCreator.this.settings.elementsAreReady();
		}
	}
}
