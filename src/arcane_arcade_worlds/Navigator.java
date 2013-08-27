package arcane_arcade_worlds;

import java.util.ArrayList;

import handlers.DrawableHandler;
import backgrounds.Background;
import arcane_arcade_main.GameSettings;
import arcane_arcade_main.Main;
import worlds.Room;

/**
 * Navigator is a class that handles the transition between different rooms 
 * and / or phases of the game. (Currently the class only creates the first 
 * and only room)
 *
 * @author Mikko Hilpinen.
 *         Created 27.8.2013.
 */
public class Navigator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Room field;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new navigator, also creating all the rooms used in the game
	 *
	 * @param drawer The drawablehandler that will draw the rooms
	 */
	public Navigator(DrawableHandler drawer)
	{
		// Initializes attributes
		Background mountainback = new Background(GameSettings.SCREENWIDTH/2, 
				GameSettings.SCREENHEIGHT/2, drawer, null, 
				Main.spritebanks.getBank("field"), "mountains");
		mountainback.setDimensions(GameSettings.SCREENWIDTH, GameSettings.SCREENHEIGHT);
		ArrayList<Background> mountainbacks = new ArrayList<Background>();
		mountainbacks.add(mountainback);
		//System.out.println(mountainback.getSpriteDrawer().getSprite().getWidth());
		this.field = new Room(mountainbacks);
		//System.out.println(mountainback.isVisible());
	}
	
	
	// OTHER METHODS	------------------------------------------------
	
	/**
	 * Starts the basic field in the game
	 */
	public void startField()
	{
		//System.out.println("starts the room");
		this.field.start();
		//System.out.println("room started");
	}
}
