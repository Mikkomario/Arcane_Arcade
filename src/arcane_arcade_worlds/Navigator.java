package arcane_arcade_worlds;

import java.util.ArrayList;

import handlers.ActorHandler;
import handlers.DrawableHandler;
import handlers.KeyListenerHandler;
import backgrounds.Background;
import arcane_arcade_field.FieldObjectCreator;
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
	 * @param actorhandler The actorhandler that will handle all the actors 
	 * in the rooms
	 * @param keylistenerhandler The keylistenerhandler that will inform 
	 * all keylisteners about the keypresses
	 */
	public Navigator(DrawableHandler drawer, ActorHandler actorhandler, 
			KeyListenerHandler keylistenerhandler)
	{
		// Initializes attributes
		// Initializes field
		Background mountainback = new Background(0, 0, drawer, null, 
				Main.spritebanks.getBank("field"), "mountains");
		mountainback.setDimensions(GameSettings.SCREENWIDTH, 
				GameSettings.SCREENHEIGHT);
		ArrayList<Background> mountainbacks = new ArrayList<Background>();
		mountainbacks.add(mountainback);
		this.field = new Room(mountainbacks);
		this.field.addOnject(new FieldObjectCreator(drawer, actorhandler, 
				keylistenerhandler));
	}
	
	
	// OTHER METHODS	------------------------------------------------
	
	/**
	 * Starts the basic field in the game
	 */
	public void startField()
	{
		this.field.start();
	}
}
