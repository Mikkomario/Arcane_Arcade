package arcane_arcade_menus;

import java.awt.Graphics2D;

import worlds.Room;

import listeners.AdvancedMouseListener;
import listeners.RoomListener;

import graphic.Sprite;
import graphic.SpriteDrawer;
import handlers.DrawableHandler;
import handlers.MouseListenerHandler;
import helpAndEnums.CollisionType;
import drawnobjects.DimensionalDrawnObject;

/**
 * AbstractButton class implements some of the most common functions all the 
 * buttons in the game have while leaving important functionalities to the 
 * subclasses to handle.<p>
 * 
 * The class uses a sprite to draw itself though it doesn't handle animation
 *
 * @author Mikko Hilpinen.
 *         Created 30.11.2013.
 */
public abstract class AbstractButton extends DimensionalDrawnObject implements 
		AdvancedMouseListener, RoomListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private SpriteDrawer spritedrawer;
	private boolean active;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new button to the given position with the given statistics. 
	 * The button uses the given sprite for drawing itself.
	 *
	 * @param x The new x-coordinate of the button (in pixels)
	 * @param y The new y-coordinate of teh button (in pixels)
	 * @param depth The drawing depth of the button
	 * @param sprite The sprite that is used for drawing the button
	 * @param drawer The drawablehandler that will draw the button (optional)
	 * @param mousehandler The mouseListenerHandler that will inform the object 
	 * about mouse events (optional)
	 * @param room The room that will hold the button (optional)
	 */
	public AbstractButton(int x, int y, int depth, Sprite sprite, 
			DrawableHandler drawer, MouseListenerHandler mousehandler, Room room)
	{
		super(x, y, depth, false, CollisionType.BOX, drawer, null);
		
		// Initializes attributes
		this.active = true;
		
		// Initializes spritedrawer
		this.spritedrawer = new SpriteDrawer(sprite, null, this);
		
		// Adds the button to the handlers
		if (room != null)
			room.addObject(this);
		if (mousehandler != null)
			mousehandler.addMouseListener(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public int getWidth()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getWidth();
		
		return 0;
	}

	@Override
	public int getHeight()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getHeight();
		
		return 0;
	}

	@Override
	public int getOriginX()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getOriginX();
		
		return 0;
	}

	@Override
	public int getOriginY()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getOriginY();
		
		return 0;
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		if (this.spritedrawer == null)
			return;
		
		// Draws the sprite
		this.spritedrawer.drawSprite(g2d, 0, 0);
	}

	@Override
	public void onRoomStart(Room room)
	{
		// Does nothing
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Dies at the end of the room
		kill();
	}

	@Override
	public boolean isActive()
	{
		return this.active;
	}

	@Override
	public void activate()
	{
		this.active = true;
	}

	@Override
	public void inactivate()
	{
		this.active = false;
	}

	@Override
	public void onLeftDown(int mouseX, int mouseY)
	{
		// Does nothing
	}

	@Override
	public void onRightDown(int mouseX, int mouseY)
	{
		// Does nothing
	}

	@Override
	public void onRightPressed(int mouseX, int mouseY)
	{
		// Does nothing
	}

	@Override
	public void onLeftReleased(int mouseX, int mouseY)
	{
		// Does nothing
	}

	@Override
	public void onRightReleased(int mouseX, int mouseY)
	{
		// Does nothing
	}

	@Override
	public boolean listensPosition(int x, int y)
	{
		return pointCollides(x, y);
	}

	@Override
	public void onMouseOver(int mouseX, int mouseY)
	{
		// Does nothing
	}

	@Override
	public void onMouseMove(int mouseX, int mouseY)
	{
		// Does nothing
	}
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return The spritedrawer used in drawing the button or null if the button 
	 * is dead
	 */
	protected SpriteDrawer getSpriteDrawer()
	{
		return this.spritedrawer;
	}
}
