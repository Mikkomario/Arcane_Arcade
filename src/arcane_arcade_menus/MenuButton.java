package arcane_arcade_menus;

import java.awt.Graphics2D;

import arcane_arcade_worlds.Navigator;

import worlds.Room;

import graphic.SpriteDrawer;
import handlers.ActorHandler;
import handlers.DrawableHandler;
import handlers.MouseListenerHandler;
import helpAndEnums.CollisionType;
import helpAndEnums.DepthConstants;
import drawnobjects.DimensionalDrawnObject;
import listeners.AdvancedMouseListener;
import listeners.RoomListener;

/**
 * Menubuttons have different functions and can be clicked
 *
 * @author Mikko Hilpinen.
 *         Created 4.9.2013.
 */
public abstract class MenuButton extends DimensionalDrawnObject implements 
		AdvancedMouseListener, RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private SpriteDrawer spritedrawer;
	private boolean active;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a menubutton to the given position with the given information 
	 * added to the given handlers
	 *
	 * @param x The x-coordinate of the button
	 * @param y The y-coordinate of the button
	 * @param drawer The drawablehandler that will draw the button
	 * @param actorhandler The actorhandler that will inform the button about 
	 * step events
	 * @param mouselistenerhandler The mouselistenerhandler that will inform 
	 * the button about mouse events
	 * @param room The room where the button is created at
	 * @param message The message the button will show when the mouse hovers 
	 * over it
	 */
	public MenuButton(int x, int y, DrawableHandler drawer, 
			ActorHandler actorhandler, 
			MouseListenerHandler mouselistenerhandler, Room room, 
			String message)
	{
		super(x, y, DepthConstants.FOREGROUND, false, CollisionType.CIRCLE, 
				drawer, null);
		
		// Initializes attributes
		this.spritedrawer = new SpriteDrawer(Navigator.getSpriteBank(
				"menu").getSprite("button"), null);
		this.active = true;
		
		// Sets the radius
		setRadius(this.spritedrawer.getSprite().getOriginX());
		
		// Adds the object to the handlers
		// TODO: Add the textdrawer to the actorhandler
		if (mouselistenerhandler != null)
			mouselistenerhandler.addMouseListener(this);
		if (room != null)
			room.addOnject(this);
	}
	
	
	// ABSTRACT METHODS	------------------------------------------------
	
	/**
	 * This method is called when the button is pressed and it should do 
	 * something
	 */
	protected abstract void doButtonFunction();
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

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
	public void kill()
	{
		// Also kills the spritedrawer
		this.spritedrawer.kill();
		this.spritedrawer = null;
		// TODO: Kill the textdrawer as well (if needed)
		super.kill();
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
	public void onLeftPressed(int mouseX, int mouseY)
	{
		// Does something
		doButtonFunction();
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
		return (pointCollides(x, y) != null);
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		return true;
	}

	@Override
	public void onMouseEnter(int mouseX, int mouseY)
	{
		// Changes image index
		this.spritedrawer.setImageIndex(1);
	}

	@Override
	public void onMouseOver(int mouseX, int mouseY)
	{
		// Does nothing
	}

	@Override
	public void onMouseExit(int mouseX, int mouseY)
	{
		// Changes image index back
		this.spritedrawer.setImageIndex(0);
	}

	@Override
	public void onMouseMove(int mouseX, int mouseY)
	{
		// Does nothing
	}

	@Override
	public int getWidth()
	{
		if (this.spritedrawer == null)
			return 0;
		else
			return this.spritedrawer.getSprite().getWidth();
	}

	@Override
	public int getHeight()
	{
		if (this.spritedrawer == null)
			return 0;
		else
			return this.spritedrawer.getSprite().getHeight();
	}

	@Override
	public int getOriginX()
	{
		if (this.spritedrawer == null)
			return 0;
		else
			return this.spritedrawer.getSprite().getOriginX();
	}

	@Override
	public int getOriginY()
	{
		if (this.spritedrawer == null)
			return 0;
		else
			return this.spritedrawer.getSprite().getOriginY();
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// Draws the sprite
		if (this.spritedrawer != null)
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
		// Dies
		kill();
	}
}
