package arcane_arcade_menus;

import java.awt.Graphics2D;

import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.Navigator;

import graphic.SpriteDrawer;
import handlers.DrawableHandler;
import helpAndEnums.DepthConstants;
import drawnobjects.DrawnObject;
import worlds.Room;
import listeners.RoomListener;

/**
 * MenuCorner is a simple visual element that is placed in a corner of a menu 
 * screen
 *
 * @author Mikko Hilpinen.
 *         Created 2.9.2013.
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
	
	private SpriteDrawer spritedrawer;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new menucorner to the given corner added to the given handlers
	 *
	 * @param drawer The drawer that will draw the corner
	 * @param room The room where the corner is
	 * @param position The corner's position (left top, left bottom or 
	 * right bottom)
	 * @see #LEFT_BOTTOM
	 * @see #LEFT_TOP
	 * @see #RIGHT_BOTTOM
	 */
	public MenuCorner(DrawableHandler drawer, Room room, int position)
	{
		super(0, 0, DepthConstants.BACK, drawer);
		
		// Initializes attributes
		this.spritedrawer = new SpriteDrawer(Navigator.getSpriteBank(
				"menu").getSprite("corner"), null);
		
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
		
		// Adds the object to the room
		if (room != null)
			room.addOnject(this);
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
