package arcane_arcade_menus;

import java.awt.geom.Point2D;

import utopia_gameobjects.GameObject;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.KeyListenerHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Room;
import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.FieldSetting;
import arcane_arcade_worlds.GamePhase;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.RoomObjectCreator;

/**
 * ElementScreen Object creator creates the necessary interface elements to 
 * the element screen in which the users will pick the elements they use in 
 * the following battle.
 *
 * @author Mikko Hilpinen.
 *         Created 30.11.2013.
 */
public class ElementScreenObjectCreator extends GameObject implements 
		RoomObjectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private FieldSetting settings;
	private DrawableHandler drawer;
	private ActorHandler actorhandler;
	private MouseListenerHandler mousehandler;
	private KeyListenerHandler keyhandler;
	private Navigator navigator;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new ElementScreenObjectCreator
	 * @param navigator The navigator used to change to different game phases
	 * @param drawer The drawer that will draw the contents of the room
	 * @param actorhandler The actorhandler that will inform the objects about 
	 * steps
	 * @param mousehandler The mouseListenerHandler that will inform the objects 
	 * about mouse events
	 * @param keyhandler The keylistenerhandler that will inform the objects 
	 * about key events
	 */
	public ElementScreenObjectCreator(Navigator navigator, 
			DrawableHandler drawer, ActorHandler actorhandler, 
			MouseListenerHandler mousehandler, KeyListenerHandler keyhandler)
	{
		// Initializes attributes
		this.settings = null;
		this.drawer = drawer;
		this.actorhandler = actorhandler;
		this.mousehandler = mousehandler;
		this.keyhandler = keyhandler;
		this.navigator = navigator;
	}
		
	@Override
	public void onRoomStart(Room room)
	{
		// Creates the objects
		// Starts the music
		new MenuThemePlayer(room, 2);
		
		new MenuBackgroundEffectCreator(this.drawer, this.actorhandler, room);
		new MenuCornerCreator(this.drawer, this.mousehandler, room,
				true);
		new SimplePhaseChangeButton(100, GameSettings.SCREENHEIGHT - 100, 
				GamePhase.BATTLESETTINGMENU, this.navigator, this.drawer, 
				this.actorhandler, this.mousehandler, room).setXScale(-1);
		new ToFieldButton(room);
		new ElementSelectionInterface(this, this.drawer, this.keyhandler, room);
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Does nothing
	}

	@Override
	public void setSettings(AreaSetting setting)
	{
		// Only works with fieldsettings
		if (setting instanceof FieldSetting)
			this.settings = (FieldSetting) setting;
		else
			System.err.println("ElementScreenObjectCreator only works with " +
					"FieldSetting type settings");
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The settings used and modified in this room
	 */
	public FieldSetting getSettings()
	{
		return this.settings;
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	private class ToFieldButton extends MenuButton
	{
		// CONSTRUCTOR	--------------------------------------------------
		
		/**
		 * Creates a new tofieldbutton to the given room
		 *
		 * @param room The room where the button is created at
		 */
		public ToFieldButton(Room room)
		{
			super(GameSettings.SCREENWIDTH - 100, 
					GameSettings.SCREENHEIGHT - 100, 
					ElementScreenObjectCreator.this.drawer, 
					ElementScreenObjectCreator.this.actorhandler,
					ElementScreenObjectCreator.this.mousehandler, room, 
					"To " + GamePhase.FIELD.toString());
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------

		@Override
		public void onMouseButtonEvent(MouseButton button,
				MouseButtonEventType eventType, Point2D.Double mousePosition,
				double eventStepTime)
		{
			// On left pressed goes to the element selection screen
			if (button == MouseButton.LEFT && 
					eventType == MouseButtonEventType.PRESSED && isVisible())
			{
				// Also stops the menumusic
				MultiMediaHolder.getMidiTrackBank("menu").getTrack(
						"maintheme").stop();
				
				ElementScreenObjectCreator.this.navigator.startPhase(
						GamePhase.FIELD, 
						ElementScreenObjectCreator.this.settings);
			}
		}
		
		@Override
		public boolean isVisible()
		{
			// Overrides the isVisible method so that the button only comes 
			// visible when the next section can be accessed
			
			return super.isVisible() && 
					ElementScreenObjectCreator.this.settings.elementsAreReady();
		}
	}
}
