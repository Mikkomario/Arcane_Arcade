package arcane_arcade_field;

import java.awt.event.KeyEvent;

import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.Navigator;
import utopia_gameobjects.GameObject;
import utopia_interfaceElements.OptionMessageBox;
import utopia_listeners.AdvancedKeyListener;
import utopia_listeners.OptionMessageBoxListener;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_utility.DepthConstants;
import utopia_worlds.Area;
import utopia_worlds.Room;

/**
 * EscapeToMenu listens user key-input and checks if they want to return to the 
 * main menu by pressing esc
 * 
 * @author Mikko Hilpinen
 * @since 18.5.2014
 */
public class EscapeToMenu extends GameObject implements AdvancedKeyListener,
		RoomListener, OptionMessageBoxListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Navigator navigator;
	private Area area;
	private boolean active;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new EscapeToMenu. The object will die once the room ends.
	 * 
	 * @param navigator The navigator that will handle transitions between 
	 * areas
	 * @param area The area where the listener resides
	 */
	public EscapeToMenu(Navigator navigator, Area area)
	{
		super(area);
		
		// Initializes attributes
		this.navigator = navigator;
		this.area = area;
		this.active = true;
		
		// Adds the object into a handler
		area.getKeyHandler().addKeyListener(this);
	}
	
	
	// IMPLMENTED METHODS	---------------------------------------------

	@Override
	public boolean isActive()
	{
		return this.active;
	}

	@Override
	public void activate()
	{
		this.active = true;
	}

	@Override
	public void inactivate()
	{
		this.active = false;
	}

	@Override
	public void onRoomStart(Room room)
	{
		// Does nothing
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Dies
		kill();
	}

	@Override
	public void onKeyDown(char key, int keyCode, boolean coded, double steps)
	{
		// Does nothing
	}

	@Override
	public void onKeyPressed(char key, int keyCode, boolean coded)
	{
		// If esc was pressed, checks if the user wants to leave
		if (key != KeyEvent.VK_ESCAPE)
			return;
		
		new OptionMessageBox(GameSettings.SCREENWIDTH / 2, GameSettings.SCREENHEIGHT / 2, 
				DepthConstants.TOP, 32, "Do you want to leave to the main menu?", 
				GameSettings.BASICFONT, GameSettings.WHITETEXTCOLOR, 
				MultiMediaHolder.getSprite("messages", "messageback"), 
				OptionMessageBox.YESNOPTIONS, MultiMediaHolder.getSprite("messages", "button"), 
				true, true, this, this.area);
	}

	@Override
	public void onKeyReleased(char key, int keyCode, boolean coded)
	{
		// Does nothing
	}

	@Override
	public void onOptionMessageEvent(String clickedoptionname,
			int clickedoptionindex)
	{
		// On yes, goes to the main menu
		if (clickedoptionindex == 0)
			this.navigator.startPhase("mainmenu", null);
	}
}
