package arcane_arcade_menus;


import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.Navigator;

import worlds.Room;

import handlers.DrawableHandler;
import handlers.MouseListenerHandler;
import helpAndEnums.DepthConstants;

/**
 * By clicking the quitmenucorner the user quits the game
 *
 * @author Mikko Hilpinen.
 *         Created 2.9.2013.
 */
public class QuitMenuCorner extends AbstractMaskButton
{	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new quitmenucorner to the top right corner of the screen.
	 *
	 * @param drawer The drawer that will draw the menu corner
	 * @param mousehandler The mouselistenerhandler that will inform the 
	 * corner about mouse events
	 * @param room The room where the corner is created at
	 */
	public QuitMenuCorner(DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room)
	{
		super(GameSettings.SCREENWIDTH, 0, DepthConstants.BACK, 
				Navigator.getSpriteBank("menu").getSprite("quit"), 
				Navigator.getSpriteBank("menu").getSprite("quitmask"), 
				drawer, mousehandler, room);
	}
	
	
	// IMPLEMENTENTED METHODS	------------------------------------------

	@Override
	public void onLeftPressed(int mouseX, int mouseY)
	{
		// Quits the game
		System.exit(0);
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		return true;
	}

	@Override
	public void onMouseEnter(int mouseX, int mouseY)
	{
		// Changes sprite index
		getSpriteDrawer().setImageIndex(1);
	}

	@Override
	public void onMouseExit(int mouseX, int mouseY)
	{
		// Changes sprite index
		getSpriteDrawer().setImageIndex(0);
	}
}
