package arcane_arcade_menus;

import worlds.Room;
import handlers.DrawableHandler;
import handlers.MouseListenerHandler;

/**
 * MenuCornerCreator creates the corners used in the menus
 *
 * @author Mikko Hilpinen.
 *         Created 2.9.2013.
 */
public class MenuCornerCreator
{
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates the menucorners into the room
	 *
	 * @param drawer The drawer that will draw the menucorners
	 * @param mousehandler The mouselistenerhandler that will inform the quit 
	 * corner about mouse events
	 * @param room The room where the corners are created at
	 * @param onlytopcorners Should only the two top corners be created (true) 
	 * or all four corners (false)
	 */
	public MenuCornerCreator(DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room, boolean onlytopcorners)
	{
		// Creates the quit menucorner
		new QuitMenuCorner(drawer, mousehandler, room);
		// Creates the another top corner
		new MenuCorner(drawer, room, MenuCorner.LEFT_TOP);
		
		// If needed, creates the bottom corners as well
		if (!onlytopcorners)
		{
			new MenuCorner(drawer, room, MenuCorner.LEFT_BOTTOM);
			new MenuCorner(drawer, room, MenuCorner.RIGHT_BOTTOM);
		}
	}
}
