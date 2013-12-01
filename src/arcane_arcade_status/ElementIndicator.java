package arcane_arcade_status;

import graphic.SpriteDrawer;
import handlers.DrawableHandler;

import java.awt.Graphics2D;

import worlds.Room;

import listeners.RoomListener;

import arcane_arcade_worlds.Navigator;

import drawnobjects.DrawnObject;

/**
 * ElementIndicators are used to represent the possible elements to choose 
 * from
 *
 * @author Mikko Hilpinen.
 *         Created 1.12.2013.
 */
public class ElementIndicator extends DrawnObject implements RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Element element;
	private SpriteDrawer spritedrawer;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new ElementIndicator to the given position to represent the 
	 * given element
	 *
	 * @param x The x-coordinate of the indicator
	 * @param y The y-coordinate of the indicator
	 * @param depth The drawing depth of the indicator
	 * @param element The element the indicator shows
	 * @param drawer The drawer that will draw the object (optional)
	 * @param room The room that holds the object
	 */
	public ElementIndicator(int x, int y, int depth, Element element, 
			DrawableHandler drawer, Room room)
	{
		super(x, y, depth, drawer);
		
		// Initializes attributes
		this.element = element;
		this.spritedrawer = new SpriteDrawer(Navigator.getSpriteBank(
				"hud").getSprite("elements"), null);
		this.spritedrawer.setImageIndex(this.element.getElementIconIndex());
		
		// Adds the object to the handler(s)
		if (room != null)
			room.addObject(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

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
		// Draws the sprite
		if (this.spritedrawer != null)
			this.spritedrawer.drawSprite(g2d, 0, 0);
	}
	
	@Override
	public void kill()
	{
		// Also kills the spritedrawer
		this.spritedrawer.kill();
		this.spritedrawer = null;
		
		super.kill();
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
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return The element the indicator represents
	 */
	public Element getElement()
	{
		return this.element;
	}
	
	/**
	 * Changes the element the indicator represents
	 *
	 * @param element The new element shown
	 */
	public void setElement(Element element)
	{
		this.element = element;
		
		if (this.spritedrawer != null)
			this.spritedrawer.setImageIndex(this.element.getElementIconIndex());
	}
}