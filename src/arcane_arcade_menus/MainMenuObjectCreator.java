package arcane_arcade_menus;

import handlers.ActorHandler;
import handlers.DrawableHandler;
import worlds.Room;
import listeners.RoomListener;
import common.GameObject;

/**
 * MainMenuObjectCreator creates the objects needed in the main menu at the 
 * start of the room.
 *
 * @author Mikko Hilpinen.
 *         Created 1.9.2013.
 */
public class MainMenuObjectCreator extends GameObject implements RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private DrawableHandler drawer;
	private ActorHandler actorhandler;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new mainmenuobjectcreator that will use the given handlers. 
	 * The creator will create the objects when the room starts.
	 *
	 * @param drawer The drawer that will draw the created objects
	 * @param actorhandler The actorhandler that will inform the objects about 
	 * act events
	 * @param room The room where the objects will be created
	 */
	public MainMenuObjectCreator(DrawableHandler drawer, 
			ActorHandler actorhandler, Room room)
	{
		// Initializes attributes
		this.drawer = drawer;
		this.actorhandler = actorhandler;
		
		// Adds the object to the room
		if (room != null)
			room.addOnject(this);
	}
	
	
	// IMPLMENTED METHODS	---------------------------------------------
	
	@Override
	public void onRoomStart(Room room)
	{
		// Creates the objects
		// Creates the bakcgroundeffectcreator
		new MenuBackgroundEffectCreator(this.drawer, this.actorhandler, room);
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Does nothing
	}
}