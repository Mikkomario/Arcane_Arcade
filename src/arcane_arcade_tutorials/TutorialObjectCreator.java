package arcane_arcade_tutorials;

import utopia_worlds.Area;
import arcane_arcade_field.Avatar;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.FieldMusicPlayer;
import arcane_arcade_field.ScreenSide;
import arcane_arcade_field.Wizard;
import arcane_arcade_field.WizardRelay;
import arcane_arcade_field.WizardSoundQueuePlayer;
import arcane_arcade_main.GameSettings;
import arcane_arcade_main.Options;
import arcane_arcade_status.Element;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.SettingUsingArea;
import arcane_arcade_worlds.SettingUsingAreaObjectCreator;

/**
 * TutorialObjectCreator can create the objects needed in any tutorial.
 * 
 * @author Mikko Hilpinen
 * @since 8.5.2014
 */
public class TutorialObjectCreator extends SettingUsingAreaObjectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private int tutorial;
	
	/**
	 * Moving tutorial teaches the basics of moving around
	 */
	public static final int MOVING = 0;
	/**
	 * Casting tutorial teaches the basic casting and the use of different 
	 * elements
	 */
	public static final int CASTING = 1;
	/**
	 * Colour tutorial teaches about life and colour
	 */
	public static final int COLOUR= 2;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new tutorialObjectCreator. The objects will be created once 
	 * the room starts. There are different tutorial types to choose from.
	 * 
	 * @param area The area where the objects will be placed to
	 * @param navigator The navigator that handles the transitions between 
	 * the different areas
	 * @param tutorialType The type of the tutorial held in this area 
	 */
	public TutorialObjectCreator(SettingUsingArea area, Navigator navigator, 
			int tutorialType)
	{
		super(area, "mountains", "background", GameSettings.SCREENWIDTH, 
				GameSettings.SCREENHEIGHT, null, navigator);
		// Initializes attributes
		this.tutorial = tutorialType;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	protected void onSettingsChange(AreaSetting newSettings)
	{
		// Doesn't use settings
	}

	@Override
	protected void createObjects(Area area)
	{
		// Creates the objects needed

		// Creates the music player
		new FieldMusicPlayer(area);
		
		// Creates a ballrelay
		BallRelay ballrelay = new BallRelay(area);
		// And the wizardrelay
		WizardRelay wizardrelay = new WizardRelay(area);
		
		// Creates a wizardsoundqueueplayer
		WizardSoundQueuePlayer wizardvoiceplayer = 
				new WizardSoundQueuePlayer(wizardrelay);
		
		Element[] onlyBlaze = {Element.BLAZE};
		Element[] usableElements = {Element.BLAZE, Element.TIDE, Element.FROST};
		
		if (this.tutorial == MOVING)
			usableElements = onlyBlaze;
		
		// Creates wizard(s)
		wizardrelay.addWizard(new Wizard(area, null, ballrelay, 
				ScreenSide.LEFT, Options.leftwizardbuttons, usableElements, 
				1, 1, Avatar.GANDALF, wizardvoiceplayer, (this.tutorial != MOVING)));
		
		// TODO: Create the tutorial objects
	}
}
