package arcane_arcade_menus;

import java.awt.geom.Point2D.Double;

import utopia_interfaceElements.AbstractButton;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_utility.DepthConstants;
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
		
		// Creates the tutorial selection buttons
		for (int i = 0; i < 3; i++)
		{
			new TutorialSelectionButton(i, area);
		}
	}

	
	// SUBCLASSES	-----------------------------------------------------
	
	private class TutorialSelectionButton extends AbstractButton
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private String tutorialAreaName;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		// The index goes from 0 to 2
		public TutorialSelectionButton(int index, Area area)
		{
			super((GameSettings.SCREENWIDTH / 4) * (index + 1) - 50, 
					GameSettings.SCREENHEIGHT / 2, DepthConstants.NORMAL, 
					MultiMediaHolder.getSprite("menu", "tutorials"), area);
			
			// Initializes attributes
			if (index == 0)
				this.tutorialAreaName = "movingtutorial";
			else if (index == 1)
				this.tutorialAreaName = "castingtutorial";
			else
				this.tutorialAreaName = "colourtutorial";
			
			// Changes the visual look
			getSpriteDrawer().setImageSpeed(0);
			getSpriteDrawer().setImageIndex(index);
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------

		@Override
		public void onMouseButtonEvent(MouseButton button,
				MouseButtonEventType eventType, Double mousePosition,
				double eventStepTime)
		{
			// On left click, goes to a tutorial
			if (button == MouseButton.LEFT && eventType == MouseButtonEventType.PRESSED)
			{
				// Also stops the menumusic
				MultiMediaHolder.getMidiTrackBank("menu").getTrack(
						"maintheme").stop();
				
				getNavigator().startPhase(this.tutorialAreaName, null);
			}
		}

		@Override
		public boolean listensMouseEnterExit()
		{
			return true;
		}

		@Override
		public void onMousePositionEvent(MousePositionEventType eventType,
				Double mousePosition, double eventStepTime)
		{
			// Scales
			if (eventType == MousePositionEventType.ENTER)
				setScale(1.2, 1.2);
			else if (eventType == MousePositionEventType.EXIT)
				setScale(1, 1);
		}
	}
}
