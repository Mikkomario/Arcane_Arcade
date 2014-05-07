package arcane_arcade_field;

import utopia_worlds.Area;
import arcane_arcade_main.GameSettings;
import arcane_arcade_main.Options;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.FieldSetting;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.SettingUsingArea;
import arcane_arcade_worlds.SettingUsingAreaObjectCreator;

/**
 * FieldObjectCreator creates all the objects in the field at the beginning of the 
 * room using the given settings.
 *
 * @author Mikko Hilpinen.
 * @since 27.8.2013.
 */
public class FieldObjectCreator extends SettingUsingAreaObjectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private FieldSetting currentsetting;
	
	
	// CONSTRCUTOR	-----------------------------------------------------
	
	/**
	 * Creates a new fieldobjectcreator. The creator has its own handlers so 
	 * that all the objects created will have the same handlers
	 * @param field The field to which the objects are created
	 * @param navigator The navigator that will handle the phase changing
	 */
	public FieldObjectCreator(SettingUsingArea field, Navigator navigator)
	{
		super(field, "mountains", "background", GameSettings.SCREENWIDTH, 
				GameSettings.SCREENHEIGHT, FieldSetting.class, navigator);
		
		// Initializes attributes
		this.currentsetting = null;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected void onSettingsChange(AreaSetting newSettings)
	{
		this.currentsetting = (FieldSetting) newSettings;
		
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
		
		// Creates the server
		Server server = new Server(GameSettings.SCREENWIDTH / 2, 
				GameSettings.SCREENHEIGHT / 2, ballrelay, wizardrelay, area);
		// Creates the scorekeeper
		ScoreKeeper scorekeeper = new ScoreKeeper(area, server, wizardrelay, 
				getNavigator(), this.currentsetting.getVictoryPoints());
		// Creates wizard(s)
		wizardrelay.addWizard(new Wizard(area, scorekeeper, ballrelay, 
				ScreenSide.LEFT, Options.leftwizardbuttons, 
				this.currentsetting.getElementsOnSide(ScreenSide.LEFT), 
				this.currentsetting.getManaRegenerationModifier(), 
				this.currentsetting.getSpellDelayModifier(), Avatar.GANDALF, 
				wizardvoiceplayer));
		wizardrelay.addWizard(new Wizard(area, scorekeeper, ballrelay, 
				ScreenSide.RIGHT, Options.rightwizardbuttons, 
				this.currentsetting.getElementsOnSide(ScreenSide.RIGHT), 
				this.currentsetting.getManaRegenerationModifier(), 
				this.currentsetting.getSpellDelayModifier(), Avatar.WHITEWIZARD, 
				wizardvoiceplayer));
	}
}
