package graphic;

import java.awt.Graphics2D;

import handleds.Actor;
import handlers.ActorHandler;

/**
 * Spritedrawer is able to draw animated sprites for an object. Object's can 
 * draw the sprite calling the drawSprite method.
 *
 * @author Mikko Hilpinen.
 *         Created 2.7.2013.
 */
public class SpriteDrawer implements Actor
{
	// ATTRIBUTES	-------------------------------------------------------
	
		private Sprite sprite;
		private double imageSpeed, imageIndex;
		private boolean alive, active;
		
		
	// CONSTRUCTOR	-------------------------------------------------------
		
	/**
	 * Creates a new spritedrawer with the given sprite to draw.
	 *
	 * @param sprite The sprite which the drawer will draw
	 * @param animator The actorhandler that calls the drawer's animation 
	 * (optional)
	 */
	public SpriteDrawer(Sprite sprite, ActorHandler animator)
	{
		// Initializes the attributes
		this.sprite = sprite;
		
		this.imageSpeed = 0.1;
		this.imageIndex = 0;
		this.alive = true;
		this.active = true;
		
		// Adds the spritedrawer to the handler, if possible
		if (animator != null)
			animator.addActor(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public boolean isActive()
	{
		return this.active;
	}

	@Override
	public boolean activate()
	{
		this.active = true;
		return true;
	}

	@Override
	public boolean inactivate()
	{
		this.active = false;
		return true;
	}

	@Override
	public boolean isDead()
	{
		return !this.alive;
	}

	@Override
	public boolean kill()
	{
		this.alive = false;
		return true;
	}

	@Override
	public void act()
	{
		// Animates the sprite
		animate();
	}
	
	
	// GETTERS & SETTERS	-----------------------------------------------
	
	/**
	 * @return The sprite as which the object is represented
	 */
	public Sprite getSprite()
	{
		return this.sprite;
	}
	
	/**
	 *This method changes the sprite with which the object is represented. The 
	 *image index will be set to 0 in the process.
	 * @param newSprite The new sprite that will be drawn
	 */
	public void setSprite(Sprite newSprite)
	{
		if (newSprite == null)
			return;
		
		this.sprite = newSprite;
		this.imageIndex = 0;
	}
	
	/**
	 * @return How fast the frames in the animation change (frames / step) 
	 * (default at 0.1)
	 */
	public double getImageSpeed()
	{
		return this.imageSpeed;
	}
	
	/**
	 * Changes how fast the frames in the animation change
	 * 
	 * @param imageSpeed The new animation speed (frames / step) (0.1 by default)
	 */
	public void setImageSpeed(double imageSpeed)
	{
		this.imageSpeed = imageSpeed;
	}
	
	/**
	 * @return Which subimage from the animation is currently drawn [0, numberOfSubimages[
	 */
	public int getImageIndex()
	{
		return (int) this.imageIndex;
	}
	
	/**
	 * Changes which subimage from the animation is currently drawn
	 * 
	 * @param imageIndex The index of the subimage drawn [0, numberOfSubimages[
	 */
	public void setImageIndex(int imageIndex)
	{
		this.imageIndex = imageIndex;
		// Also checks the new index
		checkImageIndex();
	}
	
	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 * Draws the sprite. Should be called in the DrawnObject's drawSelfBasic 
	 * method or in another similar method.
	 * 
	 * @param g2d The graphics object that does the actual drawing
	 */
	public void drawSprite(Graphics2D g2d)
	{
		// Draws the sprite
		g2d.drawImage(getSprite().getSubImage(getImageIndex()), 0, 0, null);
	}
	
	// Handles the change of the image index
	private void animate()
	{
		this.imageIndex += getImageSpeed();
		checkImageIndex();
	}
	
	// Returns the imageindex to a valid value
	private void checkImageIndex()
	{
		this.imageIndex = this.imageIndex % getSprite().getImageNumber();
		
		if (this.imageIndex < 0)
			this.imageIndex += getSprite().getImageNumber();
	}
}
