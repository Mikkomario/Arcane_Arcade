package arcane_arcade_menus;

import worlds.Room;
import gameobjects.GameObject;
import handlers.ActorHandler;
import handlers.DrawableHandler;
import handlers.MouseListenerHandler;
import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.GamePhase;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.RoomObjectCreator;

/**
 * OptionScreenObjectCreator creates the objects needed in the options screen.
 * 
 * @author Mikko Hilpinen.
 * @since 3.3.2014
 */
public class OptionScreenObjectCreator extends GameObject implements
		RoomObjectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private DrawableHandler drawer;
	private MouseListenerHandler mousehandler;
	private ActorHandler actorhandler;
	private Navigator navigator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new objectCreator. The creator will create objects when the 
	 * room starts
	 * @param drawer The drawableHandler that will draw the contents of the room
	 * @param mousehandler The MouseListenerHandler that will inform the 
	 * objects about mouse events
	 * @param actorhandler The actorhandler that will inform the objects about 
	 * steps
	 * @param navigator The navigator that will handle the transitions between 
	 * rooms
	 */
	public OptionScreenObjectCreator(DrawableHandler drawer, 
			MouseListenerHandler mousehandler, ActorHandler actorhandler, 
			Navigator navigator)
	{
		// Initializes attributes
		this.drawer = drawer;
		this.mousehandler = mousehandler;
		this.actorhandler = actorhandler;
		this.navigator = navigator;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onRoomStart(Room room)
	{
		// Starts the music
		new MenuThemePlayer(room, 1);
		// Creates menucorners
		new MenuCornerCreator(this.drawer, this.mousehandler, room, false);
		// Creates background effects
		new MenuBackgroundEffectCreator(this.drawer, this.actorhandler, room);
		// Creates navigation button
		new SimplePhaseChangeButton(100, GameSettings.SCREENHEIGHT / 2, 
				GamePhase.MAINMENU, this.navigator, this.drawer, 
				this.actorhandler, this.mousehandler, room).setXScale(-1);
		
		// TODO create optionbuttons
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Does nothing
	}

	@Override
	public void setSettings(AreaSetting setting)
	{
		// Doesn't use settings
	}
}
