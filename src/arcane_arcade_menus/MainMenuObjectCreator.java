package arcane_arcade_menus;

import arcane_arcade_worlds.Navigator;
import handlers.ActorHandler;
import handlers.DrawableHandler;
import handlers.MouseListenerHandler;
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
	private MouseListenerHandler mouselistenerhandler;
	private Navigator navigator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new mainmenuobjectcreator that will use the given handlers. 
	 * The creator will create the objects when the room starts.
	 *
	 * @param drawer The drawer that will draw the created objects
	 * @param actorhandler The actorhandler that will inform the objects about 
	 * act events
	 * @param room The room where the objects will be created
	 * @param mousehandler The mouselistenerhandler that will inform the objects 
	 * about mouse events
	 */
	public MainMenuObjectCreator(DrawableHandler drawer, 
			ActorHandler actorhandler, Room room, 
			MouseListenerHandler mousehandler, Navigator navigator)
	{
		// Initializes attributes
		this.drawer = drawer;
		this.actorhandler = actorhandler;
		this.mouselistenerhandler = mousehandler;
		this.navigator = navigator;
		
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
		// Creates the menucorners
		new MenuCornerCreator(this.drawer, this.mouselistenerhandler, room, false);
		// Creates the MainMenuElements
		new MainMenuMenuCreator(drawer, this.mouselistenerhandler, room, this.navigator);
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Does nothing
	}
}
