package arcane_arcade_menus;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import resourcebanks.MultiMediaHolder;
import arcane_arcade_main.GameSettings;
import worlds.Room;
import listeners.RoomListener;
import gameobjects.BasicPhysicDrawnObject;
import graphic.SingleSpriteDrawer;
import handleds.Collidable;
import handlers.ActorHandler;
import handlers.DrawableHandler;
import helpAndEnums.CollisionType;
import helpAndEnums.DepthConstants;

/**
 * BackgroundComet is a comet that travels through the screen and then dies
 *
 * @author Mikko Hilpinen.
 *         Created 1.9.2013.
 */
public class BackgroundComet extends BasicPhysicDrawnObject implements RoomListener
{
	// ATTRIBUTES	-------------------------------------------------------
	
	private SingleSpriteDrawer spritedrawer;
	
	
	// CONSTRUCTOR	-------------------------------------------------------
	
	/**
	 * Creates a new backgroundcomet to the given position
	 *
	 * @param x The x-coordinate of the comet's starting position
	 * @param y The y-coordinate of the comet's starting position
	 * @param drawer The DrawableHandler that will draw the comet
	 * @param actorhandler The actorhandler that will inform the comet about 
	 * the act events
	 * @param room The room where the comet was created at
	 * @param scale How large the comet is (also determines speed and depth)
	 */
	public BackgroundComet(int x, int y, DrawableHandler drawer, 
			ActorHandler actorhandler, Room room, double scale)
	{
		super(x, y, DepthConstants.BOTTOM - 15 - (int) (scale * 10), false, 
				CollisionType.CIRCLE, drawer, null, null, actorhandler);
		
		// Initializes attributes
		this.spritedrawer = new SingleSpriteDrawer(MultiMediaHolder.getSpriteBank(
				"menu").getSprite("comet"), actorhandler, this);
		
		// Changes the scaling and speed
		setScale(scale, scale);
		setMotion(240, scale * 15);
		
		// Adds the comet to the room
		if (room != null)
			room.addObject(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onCollision(ArrayList<Point2D.Double> colpoints,
			Collidable collided)
	{
		// Doesn't collide with things
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
		// Doesn't do anything on room start
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Dies at the end of the room
		kill();
	}
	
	@Override
	public void act(double steps)
	{
		super.act(steps);
		
		// In addition to normal acting, comets check if they should die
		if (getY() > GameSettings.SCREENHEIGHT + getOriginY() * getYScale() || 
				getX() < -getWidth() * getXScale())
			kill();
	}

	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		// Doens't limit collided classes
		return null;
	}
}
