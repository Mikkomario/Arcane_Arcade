package arcane_arcade_menus;

import java.awt.Graphics2D;

import utopia_gameobjects.DrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_graphic.SpriteDrawer;
import utopia_utility.DepthConstants;
import utopia_listeners.AnimationListener;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import utopia_worlds.Room;

/**
 * A backgroundstar is a visual effect shown in the background of the game
 *
 * @author Mikko Hilpinen.
 * @since 1.9.2013.
 */
public class BackgroundStar extends DrawnObject implements RoomListener, 
		AnimationListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private SingleSpriteDrawer spritedrawer;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new backgroundstar to the given position and with given scaling
	 *
	 * @param x The x-coordinate of the star
	 * @param y The y-coordinate of the star
	 * @param scale How large (close) the star is ]0, 1]
	 * @param duration How long will the star live (in steps)
	 * @param area The area where the object is placed to
	 */
	public BackgroundStar(int x, int y, double scale, int duration, Area area)
	{
		super(x, y, DepthConstants.BOTTOM - 1 - (int) (scale * 10), area);
		
		// Initializes attributes
		this.spritedrawer = new SingleSpriteDrawer(MultiMediaHolder.getSpriteBank(
				"menu").getSprite("star"), area.getActorHandler(), this);
		this.spritedrawer.getAnimationListenerHandler().addAnimationListener(this);
		this.spritedrawer.setAnimationDuration(duration);
		
		// Changes the scaling of the star
		setScale(scale, scale);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public boolean isActive()
	{
		// The star is always listening to the animation
		return true;
	}

	@Override
	public void activate()
	{
		// The star is always active
	}

	@Override
	public void inactivate()
	{
		// The star is always active
	}

	@Override
	public void onAnimationEnd(SpriteDrawer spritedrawer)
	{
		// The star dies at the end of the animation
		kill();
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
		// Doesn't do anything
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Dies
		kill();
	}
}
