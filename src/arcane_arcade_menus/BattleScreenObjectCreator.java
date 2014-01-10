package arcane_arcade_menus;


import java.awt.geom.Point2D;

import gameobjects.GameObject;
import handlers.ActorHandler;
import handlers.DrawableHandler;
import handlers.MouseListenerHandler;
import worlds.Room;
import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.GamePhase;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.RoomObjectCreator;

/**
 * BattleScreenObjectCreator creates the objects needed in the battle screen at 
 * the start of the room.
 * 
 * @author Unto Solala
 *			Created 4.9.2013
 */
public class BattleScreenObjectCreator extends GameObject implements RoomObjectCreator
{
	// ATTRIBUTES-----------------------------------------------------------
	
	private DrawableHandler drawer;
	private ActorHandler actorhandler;
	private MouseListenerHandler mousehandler;
	private Navigator navigator;
	private BattleSettingScreenInterface barhandler;
	
	
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
		this.barhandler = null;
	}

	
	// IMPLMENTED METHODS ---------------------------------------------

	@Override
	public void onRoomStart(Room room)
	{
		//System.out.println("battlescreen object creator starts creating objects");
		
		// Creates the objects
		new MenuBackgroundEffectCreator(this.drawer, this.actorhandler, room);
		new MenuCornerCreator(this.drawer, this.mousehandler, room,
				true);
		this.barhandler = new BattleSettingScreenInterface(this.drawer, 
				this.mousehandler, room);
		new ToElementScreenButton(room);
		new SimplePhaseChangeButton(100, GameSettings.SCREENHEIGHT - 100, 
				GamePhase.MAINMENU, this.navigator, this.drawer, 
				this.actorhandler, this.mousehandler, room).setXScale(-1);
		
		//System.out.println("battlescreen object creator completed");
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Does nothing
	}

	@Override
	public void setSettings(AreaSetting setting)
	{
		// Does nothing
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	/**
	 * This button changes the game phase to element selection screen when its 
	 * pressed.
	 *
	 * @author Mikko Hilpinen.
	 *         Created 1.12.2013.
	 */
	private class ToElementScreenButton extends MenuButton
	{
		// CONSTRUCTOR	--------------------------------------------------
		
		/**
		 * Creates a new button that will take the user to element screen upon 
		 * click.
		 * 
		 * @param room The room to which the object is created
		 */
		public ToElementScreenButton(Room room)
		{
			super(GameSettings.SCREENWIDTH - 100, 
					GameSettings.SCREENHEIGHT - 100, 
					BattleScreenObjectCreator.this.drawer, 
					BattleScreenObjectCreator.this.actorhandler, 
					BattleScreenObjectCreator.this.mousehandler,
					room, "To " + GamePhase.ELEMENTMENU.toString());
		}
		
		
		// IMPLEMENTED METHODS	------------------------------------------

		@Override
		public void onMouseButtonEvent(MouseButton button,
				MouseButtonEventType eventType, Point2D mousePosition,
				double eventStepTime)
		{
			// On left pressed goes to the element selection screen
			if (button == MouseButton.LEFT && 
					eventType == MouseButtonEventType.PRESSED)
				BattleScreenObjectCreator.this.navigator.startPhase(
						GamePhase.ELEMENTMENU, 
						BattleScreenObjectCreator.this.barhandler.createFieldSetting());
		}
	}
}
