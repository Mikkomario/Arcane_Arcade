package arcane_arcade_menus;

import utopia_worlds.Area;


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
	 * @param area The area where the object is placed to
	 * @param onlytopcorners Should only the two top corners be created (true) 
	 * or all four corners (false)
	 */
	public MenuCornerCreator(Area area, boolean onlytopcorners)
	{
		// Creates the quit menucorner
		new QuitMenuCorner(area);
		// Creates the another top corner
		new MenuCorner(area, MenuCorner.LEFT_TOP);
		
		// If needed, creates the bottom corners as well
		if (!onlytopcorners)
		{
			new MenuCorner(area, MenuCorner.LEFT_BOTTOM);
			new MenuCorner(area, MenuCorner.RIGHT_BOTTOM);
		}
	}
}
