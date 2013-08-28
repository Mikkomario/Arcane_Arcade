package arcane_arcade_field;

import arcane_arcade_main.GameSettings;
import handlers.ActorHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import handlers.KeyListenerHandler;
import helpAndEnums.DepthConstants;
import common.GameObject;

import worlds.Room;
import listeners.RoomListener;

/**
 * Objectcreator creates all the objects in the field at the beginning of the 
 * room using the given settings. The FieldObjectCreator also creates the 
 * collision handling environment to the field
 *
 * @author Mikko Hilpinen.
 *         Created 27.8.2013.
 */
public class FieldObjectCreator extends GameObject implements RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private DrawableHandler drawer;
	private ActorHandler actorhandler;
	private KeyListenerHandler keylistenerhandler;
	private CollisionHandler collisionhandler;
	
	
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
	 */
	public FieldObjectCreator(DrawableHandler superdrawer, 
			ActorHandler superactorhandler, KeyListenerHandler superkeyhandler)
	{
		// Initializes attributes
		this.drawer = new DrawableHandler(false, true, DepthConstants.NORMAL, 
				superdrawer);
		this.actorhandler = new ActorHandler(false, superactorhandler);
		this.keylistenerhandler = new KeyListenerHandler(false, superkeyhandler);
		this.collisionhandler = new CollisionHandler(false, this.actorhandler);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onRoomStart(Room room)
	{
		// Creates the objects needed

		// Creates a ballrelay
		BallRelay ballrelay = new BallRelay(room);
		// Creates wizard(s)
		new Wizard(this.drawer, this.collisionhandler.getCollidableHandler(), 
				this.collisionhandler, this.actorhandler, 
				this.keylistenerhandler, room, ballrelay);
		// Creates the server
		new Server(GameSettings.SCREENWIDTH / 2, GameSettings.SCREENHEIGHT / 2, 
				this.drawer, this.actorhandler, 
				this.collisionhandler.getCollidableHandler(), 
				this.collisionhandler, room, ballrelay);
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Does nothing
	}
}
