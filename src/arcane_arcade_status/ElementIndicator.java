package arcane_arcade_status;

import java.awt.Graphics2D;

import utopia_gameobjects.DrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import utopia_worlds.Room;



/**
 * ElementIndicators are used to represent the possible elements to choose 
 * from
 *
 * @author Mikko Hilpinen.
 * @since 1.12.2013.
 */
public class ElementIndicator extends DrawnObject implements RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Element element;
	private SingleSpriteDrawer spritedrawer;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new ElementIndicator to the given position to represent the 
	 * given element
	 *
	 * @param x The x-coordinate of the indicator
	 * @param y The y-coordinate of the indicator
	 * @param depth The drawing depth of the indicator
	 * @param element The element the indicator shows
	 * @param area the area where the object will be placed to
	 */
	public ElementIndicator(int x, int y, int depth, Element element, Area area)
	{
		super(x, y, depth, area);
		
		// Initializes attributes
		this.element = element;
		this.spritedrawer = new SingleSpriteDrawer(MultiMediaHolder.getSpriteBank(
				"hud").getSprite("elements"), null, this);
		this.spritedrawer.setImageIndex(this.element.getElementIconIndex());
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