package arcane_arcade_menus;


import java.awt.geom.Point2D;

import resourcebanks.MultiMediaHolder;
import arcane_arcade_main.GameSettings;
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
				MultiMediaHolder.getSpriteBank("menu").getSprite("quit"), 
				MultiMediaHolder.getSpriteBank("menu").getSprite("quitmask"), 
				drawer, mousehandler, room);
	}
	
	
	// IMPLEMENTENTED METHODS	------------------------------------------

	@Override
	public boolean listensMouseEnterExit()
	{
		return true;
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Point2D mousePosition, double eventStepTime)
	{
		// Changes sprite index when mouse enters or exits the button
		if (eventType == MousePositionEventType.ENTER)
			getSpriteDrawer().setImageIndex(1);
		else if (eventType == MousePositionEventType.EXIT)
			getSpriteDrawer().setImageIndex(0);
	}

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Point2D mousePosition,
			double eventStepTime)
	{
		if (button == MouseButton.LEFT && 
				eventType == MouseButtonEventType.PRESSED)
		// Quits the game when clicked
		System.exit(0);
	}
}
