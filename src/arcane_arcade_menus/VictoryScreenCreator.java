package arcane_arcade_menus;

import handlers.ActorHandler;
import handlers.DrawableHandler;
import handlers.MouseListenerHandler;
import worlds.Room;
import common.GameObject;

import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.RoomObjectCreator;
import arcane_arcade_worlds.VictorySetting;

/**
 * VictoryScreenCreator creates the objects needed in the victory screen at the 
 * start of the room.
 * 
 * @author Unto Solala
 *			Created 4.9.2013
 */
public class VictoryScreenCreator extends GameObject implements RoomObjectCreator{
	
	// ATTRIBUTES	-----------------------------------------------------
	
	private DrawableHandler drawer;
	private ActorHandler actorhandler;
	private MouseListenerHandler mouselistenerhandler;
	private Navigator navigator;
	private VictorySetting victorysetting;
		
	// CONSTRUCTOR	-----------------------------------------------------
	/**
	 * Creates a new VictoryScreenCreator that will use the given handlers. 
	 * The creator will create the objects when the room starts.
	 *
	 * @param drawer The drawer that will draw the created objects
	 * @param actorhandler The actorhandler that will inform the objects about 
	 * act events
	 * @param room The room where the objects will be created
	 * @param mousehandler The mouselistenerhandler that will inform the objects 
	 * about mouse events
	 * @param navigator The navigator that handles the transition between the 
	 * gamephases
	 */
	public VictoryScreenCreator(DrawableHandler drawer, 
			ActorHandler actorhandler, MouseListenerHandler mousehandler, 
			Navigator navigator)
	{
		// Initializes attributes
		this.drawer = drawer;
		this.actorhandler = actorhandler;
		this.mouselistenerhandler = mousehandler;
		this.navigator = navigator;
	}
	
	// IMPLMENTED METHODS ---------------------------------------------

	@Override
	public void onRoomStart(Room room) {
		// Creates the objects
		
	}

	@Override
	public void onRoomEnd(Room room) {
		// Does nothing
	}

	@Override
	public void setSettings(AreaSetting setting) {
		//TODO: Coming soon...
	}
}
