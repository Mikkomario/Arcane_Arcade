package arcane_arcade_menus;

import java.awt.Graphics2D;

import utopia_gameobjects.DrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_utility.DepthConstants;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import utopia_worlds.Room;
import arcane_arcade_main.GameSettings;

/**
 * MenuCorner is a simple visual element that is placed in a corner of a menu 
 * screen
 *
 * @author Mikko Hilpinen.
 * @since 2.9.2013.
 */
public class MenuCorner extends DrawnObject implements RoomListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	/**
	 * The screen's top left corner as a position
	 */
	public static final int LEFT_TOP = 0;
	/**
	 * The screen's bottom left corner as a position
	 */
	public static final int LEFT_BOTTOM = 1;
	/**
	 * The screen's bottom right corner as a position
	 */
	public static final int RIGHT_BOTTOM = 2;
	
	private SingleSpriteDrawer spritedrawer;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new menucorner to the given corner added to the given handlers
	 *
	 * @param area The area where the object is placed to
	 * @param position The corner's position (left top, left bottom or 
	 * right bottom)
	 * @see #LEFT_BOTTOM
	 * @see #LEFT_TOP
	 * @see #RIGHT_BOTTOM
	 */
	public MenuCorner(Area area, int position)
	{
		super(0, 0, DepthConstants.BACK, area);
		
		// Initializes attributes
		this.spritedrawer = new SingleSpriteDrawer(MultiMediaHolder.getSpriteBank(
				"menu").getSprite("corner"), null, this);
		
		// Changes the position and angle
		if (position == LEFT_BOTTOM)
		{
			setY(GameSettings.SCREENHEIGHT);
			setAngle(90);
		}
		else if (position == RIGHT_BOTTOM)
		{
			setX(GameSettings.SCREENWIDTH);
			setY(GameSettings.SCREENHEIGHT);
			setAngle(180);
		}
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

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
	public int getOriginX()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getOriginX();
	}

	@Override
	public int getOriginY()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getOriginY();
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// Draws the corner sprite
		if (this.spritedrawer != null)
			this.spritedrawer.drawSprite(g2d, 0, 0);
	}
}
