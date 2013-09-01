package arcane_arcade_menus;

import java.awt.Graphics2D;

import arcane_arcade_worlds.Navigator;

import worlds.Room;

import graphic.SpriteDrawer;
import handlers.ActorHandler;
import handlers.DrawableHandler;
import helpAndEnums.DepthConstants;
import listeners.AnimationListener;
import listeners.RoomListener;
import drawnobjects.DrawnObject;

/**
 * A backgroundstar is a visual effect shown in the background of the game
 *
 * @author Mikko Hilpinen.
 *         Created 1.9.2013.
 */
public class BackgroundStar extends DrawnObject implements RoomListener, 
		AnimationListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private SpriteDrawer spritedrawer;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new backgroundstar to the given position and with given scaling
	 *
	 * @param x The x-coordinate of the star
	 * @param y The y-coordinate of the star
	 * @param drawer The drawer that will draw the star
	 * @param animator The animator that will animate the star's sprite
	 * @param room The room in which the star is created
	 * @param scale How large (close) the star is ]0, 1]
	 * @param duration How long will the star live (in steps)
	 */
	public BackgroundStar(int x, int y, DrawableHandler drawer, 
			ActorHandler animator, Room room, double scale, int duration)
	{
		super(x, y, DepthConstants.BOTTOM - 1 - (int) (scale * 10), drawer);
		
		// Initializes attributes
		this.spritedrawer = new SpriteDrawer(Navigator.getSpriteBank(
				"menu").getSprite("star"), animator);
		this.spritedrawer.setAnimationDuration(duration);
		
		// Changes the scaling of the star
		setScale(scale, scale);
		
		// Adds the listener to the room
		if (room != null)
			room.addOnject(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public boolean isActive()
	{
		// The star is always listening to the animation
		return true;
	}

	@Override
	public boolean activate()
	{
		return true;
	}

	@Override
	public boolean inactivate()
	{
		return false;
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
	
	@Override
	public boolean kill()
	{
		// Also kills the spritedrawer
		this.spritedrawer.kill();
		return super.kill();
	}
}
