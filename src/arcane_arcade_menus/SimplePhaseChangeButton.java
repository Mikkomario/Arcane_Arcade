package arcane_arcade_menus;

import java.awt.geom.Point2D;

import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_worlds.Room;
import arcane_arcade_worlds.GamePhase;
import arcane_arcade_worlds.Navigator;
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
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D.Double mousePosition,
			double eventStepTime)
	{
		// Changes the phase when clicked
		if (button == MouseButton.LEFT && eventType == MouseButtonEventType.PRESSED)
			this.navigator.startPhase(this.phase, null);
	}
}
