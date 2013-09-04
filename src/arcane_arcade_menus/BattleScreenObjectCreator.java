package arcane_arcade_menus;

import common.GameObject;

import handlers.ActorHandler;
import handlers.DrawableHandler;
import handlers.MouseListenerHandler;
import worlds.Room;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.FieldSetting;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.RoomObjectCreator;

/**
 * BattleScreenObjectCreator creates the objects needed in the battle screen at 
 * the start of the room.
 * 
 * @author Unto Solala
 *			Created 4.9.2013
 */
public class BattleScreenObjectCreator extends GameObject implements RoomObjectCreator{
	
	// ATTRIBUTES-----------------------------------------------------------
	
	private DrawableHandler drawer;
	private ActorHandler actorhandler;
	private MouseListenerHandler mousehandler;
	private Navigator navigator;
	private AreaSetting fieldsetting;
	
	// CONSTRUCTOR---------------------------------------------------------
	/**
	 * Creates a new BattleScreenObjectCreator that will use the given handlers. 
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
	public BattleScreenObjectCreator(DrawableHandler drawer, 
			ActorHandler actorhandler, MouseListenerHandler mousehandler, 
			Navigator navigator)
	{
		// Initializes attributes
		this.drawer = drawer;
		this.actorhandler = actorhandler;
		this.mousehandler = mousehandler;
		this.navigator = navigator;
		this.fieldsetting = null;
	}

	// IMPLMENTED METHODS ---------------------------------------------

	@Override
	public void onRoomStart(Room room) {
		// Creates the objects
		new MenuBackgroundEffectCreator(this.drawer, this.actorhandler, room);
		new MenuCornerCreator(this.drawer, this.mousehandler, room,
				true);

	}

	@Override
	public void onRoomEnd(Room room) {
		// Does nothing
	}

	@Override
	public void setSettings(AreaSetting setting) {
		// Does nothing
	}

}
