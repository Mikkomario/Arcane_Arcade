package arcane_arcade_menus;

import java.awt.geom.Point2D;

import graphic.MaskChecker;
import graphic.Sprite;
import handlers.DrawableHandler;
import handlers.MouseListenerHandler;
import worlds.Room;

/**
 * AbstractMaskButton is an abstract button that in addition to normal button 
 * functions, also handles collision detection with a mask.
 *
 * @author Mikko Hilpinen.
 *         Created 30.11.2013.
 */
public abstract class AbstractMaskButton extends AbstractButton
{
	// ATTRIBUTES	------------------------------------------------------
	
	private MaskChecker maskchecker;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new button with the given statistics.
	 *
	 * @param x The x-coordinate of the button
	 * @param y The y-coordinate of the button
	 * @param depth The drawing depth of the button
	 * @param sprite The sprite used to draw the button with
	 * @param mask The mask used to check the collisions
	 * @param drawer The drawer that will draw the button (optional)
	 * @param mousehandler The mouselistenerhandler that will inform the button 
	 * about mouse events
	 * @param room The room that will hold the button (optional)
	 */
	public AbstractMaskButton(int x, int y, int depth, Sprite sprite, 
			Sprite mask, DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room)
	{
		super(x, y, depth, sprite, drawer, mousehandler, room);
		
		// Initializes attributes
		this.maskchecker = new MaskChecker(mask);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public boolean pointCollides(Point2D testPosition)
	{
		// Uses mask for collision checking
		return (this.maskchecker != null && 
				this.maskchecker.maskContainsRelativePoint(
				negateTransformations(testPosition), 
				getSpriteDrawer().getImageIndex()));
	}
	
	
	// GETTERS & SETTERS	-----------------------------------------------
	
	/**
	 * @return The maskchecker used for collision testing
	 */
	protected MaskChecker getMaskChecker()
	{
		return this.maskchecker;
	}
}
