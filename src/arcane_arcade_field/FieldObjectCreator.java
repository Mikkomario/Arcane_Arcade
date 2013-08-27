package arcane_arcade_field;

import handlers.ActorHandler;
import handlers.DrawableHandler;
import handlers.KeyListenerHandler;
import helpAndEnums.DepthConstants;
import common.GameObject;

import worlds.Room;
import listeners.RoomListener;

/**
 * Objectcreator creates all the objects in the field at the beginning of the 
 * room using the given settings
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
	// TODO: Add collidablehandler and collisionhandlers somewhere
	
	
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
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public boolean isActive()
	{
		// Objectcreators are always active
		return true;
	}

	@Override
	public boolean activate()
	{
		// Objectcreators are always active
		return true;
	}

	@Override
	public boolean inactivate()
	{
		// Objectcreators are always active
		return false;
	}

	@Override
	public void onRoomStart(Room room)
	{
		// Creates the objects needed

		// Creates wizard(s)
		// TODO: Add collidablehandler & collisionhandler
		room.addOnject(new Wizard(this.drawer, null, null, this.actorhandler, 
				this.keylistenerhandler));
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Does nothing
	}
}
