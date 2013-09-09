package arcane_arcade_field;

import arcane_arcade_main.GameSettings;
import arcane_arcade_main.Options;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.FieldSetting;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.RoomObjectCreator;
import handlers.ActorHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import handlers.KeyListenerHandler;
import helpAndEnums.DepthConstants;
import common.GameObject;

import worlds.Room;

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
	
	private DrawableHandler drawer;
	private ActorHandler actorhandler;
	private KeyListenerHandler keylistenerhandler;
	private CollisionHandler collisionhandler;
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
		this.drawer = new DrawableHandler(false, true, DepthConstants.NORMAL, 
				superdrawer);
		this.actorhandler = new ActorHandler(false, superactorhandler);
		this.keylistenerhandler = new KeyListenerHandler(false, superkeyhandler);
		this.collisionhandler = new CollisionHandler(false, this.actorhandler);
		this.currentsetting = null;
		this.navigator = navigator;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onRoomStart(Room room)
	{
		// Creates the objects needed

		// Creates a ballrelay
		BallRelay ballrelay = new BallRelay(room);
		// And the wizardrelay
		WizardRelay wizardrelay = new WizardRelay(room);
		// Creates a wizardsoundqueueplayer
		WizardSoundQueuePlayer wizardvoiceplayer = 
				new WizardSoundQueuePlayer(wizardrelay);
		// Creates the server
		Server server = new Server(GameSettings.SCREENWIDTH / 2, 
				GameSettings.SCREENHEIGHT / 2, this.drawer, this.actorhandler, 
				this.collisionhandler.getCollidableHandler(), 
				this.collisionhandler, room, ballrelay, wizardrelay);
		// Creates the scorekeeper
		ScoreKeeper scorekeeper = new ScoreKeeper(this.drawer, 
				this.actorhandler, room, server, wizardrelay, this.navigator, 
				this.currentsetting.getVictoryPoints());
		// Creates wizard(s)
		wizardrelay.addWizard(new Wizard(this.drawer, 
				this.collisionhandler.getCollidableHandler(), 
				this.collisionhandler, this.actorhandler, 
				this.keylistenerhandler, room, scorekeeper, ballrelay, 
				ScreenSide.LEFT, Options.leftwizardbuttons, 
				this.currentsetting.getElementsOnSide(ScreenSide.LEFT), 
				this.currentsetting.getManaRegenerationModifier(), 
				this.currentsetting.getSpellDelayModifier(), Avatar.GANDALF, 
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
}
