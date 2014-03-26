package arcane_arcade_field;

import utopia_gameobjects.GameObject;
import utopia_handlers.ActorHandler;
import utopia_handlers.CollisionHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.KeyListenerHandler;
import utopia_helpAndEnums.DepthConstants;
import utopia_worlds.Room;
import arcane_arcade_main.GameSettings;
import arcane_arcade_main.Options;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.FieldSetting;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.RoomObjectCreator;

/**
 * Objectcreator creates all the objects in the field at the beginning of the 
 * room using the given settings. The FieldObjectCreator also creates the 
 * collision handling environment to the field
 *
 * @author Mikko Hilpinen.
 *         Created 27.8.2013.
 */
public class FieldObjectCreator extends GameObject implements RoomObjectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private DrawableHandler fielddrawer;
	private ActorHandler fieldactorhandler;
	private KeyListenerHandler fieldkeylistenerhandler;
	private CollisionHandler fieldcollisionhandler;
	private FieldSetting currentsetting;
	private Navigator navigator;
	
	
	// CONSTRCUTOR	-----------------------------------------------------
	
	/**
	 * Creates a new fieldobjectcreator. The creator has its own handlers so 
	 * that all the objects created will have the same handlers
	 *
	 * @param superdrawer The drawablehandler that will draw each drawable created
	 * @param superactorhandler The actorhandler that will inform created 
	 * actor about the act event
	 * @param superkeyhandler The keylistenerhandler that will inform created 
	 * objects about the key events
	 * @param navigator The navigator that will handle the phase changing
	 */
	public FieldObjectCreator(DrawableHandler superdrawer, 
			ActorHandler superactorhandler, KeyListenerHandler superkeyhandler, 
			Navigator navigator)
	{
		// Initializes attributes
		this.fielddrawer = new DrawableHandler(false, true, DepthConstants.NORMAL, 5, 
				superdrawer);
		this.fieldactorhandler = new ActorHandler(false, superactorhandler);
		this.fieldkeylistenerhandler = new KeyListenerHandler(false, superkeyhandler);
		this.fieldcollisionhandler = new CollisionHandler(false, this.fieldactorhandler);
		this.currentsetting = null;
		this.navigator = navigator;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onRoomStart(Room room)
	{
		// Creates the objects needed

		// Creates the music player
		new FieldMusicPlayer(room);
		// Creates a ballrelay
		BallRelay ballrelay = new BallRelay(room);
		// And the wizardrelay
		WizardRelay wizardrelay = new WizardRelay(room);
		// Creates a wizardsoundqueueplayer
		WizardSoundQueuePlayer wizardvoiceplayer = 
				new WizardSoundQueuePlayer(wizardrelay);
		// Creates the server
		Server server = new Server(GameSettings.SCREENWIDTH / 2, 
				GameSettings.SCREENHEIGHT / 2, this.fielddrawer, this.fieldactorhandler, 
				this.fieldcollisionhandler.getCollidableHandler(), 
				this.fieldcollisionhandler, room, ballrelay, wizardrelay);
		// Creates the scorekeeper
		ScoreKeeper scorekeeper = new ScoreKeeper(this.fielddrawer, 
				this.fieldactorhandler, room, server, wizardrelay, this.navigator, 
				this.currentsetting.getVictoryPoints());
		// Creates wizard(s)
		wizardrelay.addWizard(new Wizard(this.fielddrawer, 
				this.fieldcollisionhandler.getCollidableHandler(), 
				this.fieldcollisionhandler, this.fieldactorhandler, 
				this.fieldkeylistenerhandler, room, scorekeeper, ballrelay, 
				ScreenSide.LEFT, Options.leftwizardbuttons, 
				this.currentsetting.getElementsOnSide(ScreenSide.LEFT), 
				this.currentsetting.getManaRegenerationModifier(), 
				this.currentsetting.getSpellDelayModifier(), Avatar.GANDALF, 
				wizardvoiceplayer));
		wizardrelay.addWizard(new Wizard(this.fielddrawer, 
				this.fieldcollisionhandler.getCollidableHandler(), 
				this.fieldcollisionhandler, this.fieldactorhandler, 
				this.fieldkeylistenerhandler, room, scorekeeper, ballrelay, 
				ScreenSide.RIGHT, Options.rightwizardbuttons, 
				this.currentsetting.getElementsOnSide(ScreenSide.RIGHT), 
				this.currentsetting.getManaRegenerationModifier(), 
				this.currentsetting.getSpellDelayModifier(), Avatar.WHITEWIZARD, 
				wizardvoiceplayer));
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Does nothing
	}

	@Override
	public void setSettings(AreaSetting setting)
	{
		// Checks that the setting is the right type
		if (setting instanceof FieldSetting)
			this.currentsetting = (FieldSetting) setting;
		else
			System.err.println("FieldObjectCreator requires an " +
					"FieldSetting -setting and will not function otherwise!");
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Pauses all the objects (that can be inactivated) in the field
	 */
	public void pauseField()
	{
		// Inactivates the field handlers
		this.fieldactorhandler.inactivate();
		this.fieldcollisionhandler.inactivate();
		this.fieldkeylistenerhandler.inactivate();
	}
	
	/**
	 * Reactivates all the objects in the field
	 */
	public void unpause()
	{
		// (Re)activates the field handlers
		this.fieldactorhandler.activate();
		this.fieldcollisionhandler.activate();
		this.fieldkeylistenerhandler.activate();
	}
}
