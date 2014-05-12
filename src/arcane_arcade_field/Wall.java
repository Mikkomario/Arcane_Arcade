package arcane_arcade_field;

import java.awt.Graphics2D;

import utopia_gameobjects.DimensionalDrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_graphic.Sprite;
import utopia_listeners.RoomListener;
import utopia_utility.CollisionType;
import utopia_utility.DepthConstants;
import utopia_worlds.Area;
import utopia_worlds.Room;

/**
 * Walls are dimensional drawn objects that block wizard's movement
 * 
 * @author Mikko Hilpinen
 * @since 12.5.2014
 */
public class Wall extends DimensionalDrawnObject implements RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private int width, height;
	private SingleSpriteDrawer spriteDrawer;
	
	private static final Class<?>[] collidedClasses = {Wizard.class};
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new wall with the given dimensions and sprite
	 * 
	 * @param x The x-coordinate of the wall's origin
	 * @param y The y-coordinate of the wall's origin
	 * @param width How wide the wall is (in pixels)
	 * @param height How high the wall is (in pixels)
	 * @param sprite The sprite used to draw the wall
	 * @param area The area where the wall resides at
	 */
	public Wall(int x, int y, int width, int height, Sprite sprite, Area area)
	{
		super(x, y, DepthConstants.BACK, true, CollisionType.BOX, area);
		
		// Initializes attributes
		this.width = width;
		this.height = height;
		this.spriteDrawer = new SingleSpriteDrawer(sprite, area.getActorHandler(), 
				this);
		
		// Scales the wall to a certain size
		double xScale = (double) getWidth() / sprite.getWidth();
		double yScale = (double) getHeight() / sprite.getHeight();
		setScale(xScale, yScale);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		return collidedClasses;
	}

	@Override
	public int getWidth()
	{
		return this.width;
	}

	@Override
	public int getHeight()
	{
		return this.height;
	}

	@Override
	public int getOriginX()
	{
		if (this.spriteDrawer == null)
			return 0;
		else
			return this.spriteDrawer.getSprite().getOriginX();
	}

	@Override
	public int getOriginY()
	{
		if (this.spriteDrawer == null)
			return 0;
		else
			return this.spriteDrawer.getSprite().getOriginY();
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// Draws the sprite
		if (this.spriteDrawer != null)
			this.spriteDrawer.drawSprite(g2d, 0, 0);
	}

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
}
