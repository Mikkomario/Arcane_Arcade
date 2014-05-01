package arcane_arcade_menus;


import java.awt.geom.Point2D;

import utopia_utility.DepthConstants;
import utopia_interfaceElements.AbstractMaskButton;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import arcane_arcade_main.GameSettings;
/**
 * By clicking the quitmenucorner the user quits the game
 *
 * @author Mikko Hilpinen.
 * @since 2.9.2013.
 */
public class QuitMenuCorner extends AbstractMaskButton
{	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new quitmenucorner to the top right corner of the screen.
	 *
	 * @param area The area where the object is placed to
	 */
	public QuitMenuCorner(Area area)
	{
		super(GameSettings.SCREENWIDTH, 0, DepthConstants.BACK, 
				MultiMediaHolder.getSpriteBank("menu").getSprite("quit"), 
				MultiMediaHolder.getSpriteBank("menu").getSprite("quitmask"), 
				area);
	}
	
	
	// IMPLEMENTENTED METHODS	------------------------------------------

	@Override
	public boolean listensMouseEnterExit()
	{
		return true;
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType, 
			Point2D.Double mousePosition, double eventStepTime)
	{
		// Changes sprite index when mouse enters or exits the button
		if (eventType == MousePositionEventType.ENTER)
			getSpriteDrawer().setImageIndex(1);
		else if (eventType == MousePositionEventType.EXIT)
			getSpriteDrawer().setImageIndex(0);
	}

	@Override
	public void onMouseButtonEvent(MouseButton button, 
			MouseButtonEventType eventType, Point2D.Double mousePosition, 
			double eventStepTime)
	{
		if (button == MouseButton.LEFT && 
				eventType == MouseButtonEventType.PRESSED)
		// Quits the game when clicked
		System.exit(0);
	}
}
