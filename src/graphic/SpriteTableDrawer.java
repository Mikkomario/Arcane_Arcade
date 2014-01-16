package graphic;

import gameobjects.DrawnObject;
import handlers.ActorHandler;

/**
 * 
 * @author Mikko Hilpinen. 
 * Created 16.1.2014
 */
public class SpriteTableDrawer extends SpriteDrawer
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Sprite[] sprites;
	private int currentid;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new SpriteTableDrawer with the given data
	 * 
	 * @param sprites A table containing the sprites the drawer will draw
	 * @param animator The actorHandler that will animate the sprites (optional)
	 * @param user The object the drawer is tied into. The spritedrawer will 
	 * automatically die when the user dies. (Optional)
	 */
	public SpriteTableDrawer(Sprite[] sprites, ActorHandler animator, DrawnObject user)
	{
		super(animator, user);
		
		// Initializes attributes
		this.sprites = sprites;
		this.currentid = 0;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	protected Sprite getCurrentSprite()
	{
		return this.sprites[this.currentid];
	}

	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 * Changes the currently shown sprite to the sprite with the given 
	 * index in the table
	 * 
	 * @param newIndex The index of the new shown sprite
	 * @param resetImageIndex Should the sprite's animation restart from 
	 * the beginning
	 */
	public void setSpriteIndex(int newIndex, boolean resetImageIndex)
	{
		if (this.sprites == null || this.sprites.length == 0)
			this.currentid = 0;
		
		// If the index is too large / small, loops through the list
		this.currentid = Math.abs(newIndex % this.sprites.length);
		
		if (resetImageIndex)
			setImageIndex(0);
	}
	
	/**
	 * Changes the shown sprite to the next one in the table. If the end of the 
	 * table was reached the index loops
	 * 
	 * @param resetImageIndex 
	 */
	public void changeToNextSprite(boolean resetImageIndex)
	{
		setSpriteIndex(this.currentid + 1, resetImageIndex);
	}
	
	/**
	 * Changes the shown sprite to the last one in the table. If the start of 
	 * the table was reached the index loops
	 * 
	 * @param resetImageIndex
	 */
	public void changeToPreviousSprite(boolean resetImageIndex)
	{
		setSpriteIndex(this.currentid - 1, resetImageIndex);
	}
}
