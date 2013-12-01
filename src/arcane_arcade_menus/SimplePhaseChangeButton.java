package arcane_arcade_menus;

import arcane_arcade_worlds.GamePhase;
import arcane_arcade_worlds.Navigator;
import handlers.ActorHandler;
import handlers.DrawableHandler;
import handlers.MouseListenerHandler;
import worlds.Room;

/**
 * This button takes the user to a new gamephase though it doesn't support the 
 * use of areasettings upon change. For that one has to create its own class.
 *
 * @author Mikko Hilpinen.
 *         Created 1.12.2013.
 */
public class SimplePhaseChangeButton extends MenuButton
{
	// ATTRIBUTES	------------------------------------------------------
	
	private Navigator navigator;
	private GamePhase phase;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new PhaseChangeButton to the given position with the given 
	 * statistics.
	 *
	 * @param x The x-coordinate of the button (pixels)
	 * @param y The y-coordinate of the button (pixels)
	 * @param newphase The phase where the button will take the user
	 * @param navigator The navigator that is used for phase changing
	 * @param drawer The drawer that will draw the button (optional)
	 * @param actorhandler The actorhandler that will inform the button about 
	 * steps (optional)
	 * @param mouselistenerhandler The mouselistenerhandler that will inform 
	 * the button about mouse events (optional)
	 * @param room The room that holds the button (optional)
	 */
	public SimplePhaseChangeButton(int x, int y, GamePhase newphase, 
			Navigator navigator, DrawableHandler drawer, 
			ActorHandler actorhandler, 
			MouseListenerHandler mouselistenerhandler, Room room)
	{
		super(x, y, drawer, actorhandler, mouselistenerhandler, room, 
				"To " + newphase.toString());
		
		// Initializes attributes
		this.navigator = navigator;
		this.phase = newphase;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onLeftPressed(int mouseX, int mouseY)
	{
		// Changes the phase
		this.navigator.startPhase(this.phase, null);
	}
}
