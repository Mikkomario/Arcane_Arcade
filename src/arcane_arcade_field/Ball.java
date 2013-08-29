package arcane_arcade_field;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import worlds.Room;

import listeners.RoomListener;

import arcane_arcade_main.GameSettings;
import arcane_arcade_main.Main;
import arcane_arcade_spelleffects.SpellEffect;

import graphic.SpriteDrawer;
import handleds.Collidable;
import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import helpAndEnums.CollisionType;
import helpAndEnums.DepthConstants;
import helpAndEnums.DoublePoint;
import helpAndEnums.HelpMath;
import drawnobjects.BasicPhysicDrawnObject;

/**
 * Ball is one of the main objects of the game. It mainly flies around the 
 * field and collides with different things.
 *
 * @author Mikko Hilpinen.
 *         Created 27.8.2013.
 */
public class Ball extends BasicPhysicDrawnObject implements RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private SpriteDrawer spritedrawer;
	
	private double airfriction, forcedelay;
	private int minspeed;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new ball to the given position
	 *
	 * @param x The ball's x-coordinate
	 * @param y The ball's y-coordinate
	 * @param drawer THe drawablehandler that will draw the ball
	 * @param collidablehandler The collidablehandler that will handle the ball's 
	 * collision checking
	 * @param collisionhandler The collisionhandler that will inform the ball 
	 * about collisions
	 * @param actorhandler The actorhandler that will call the ball's act-event
	 * @param room The room where the ball is created
	 */
	public Ball(int x, int y, DrawableHandler drawer,
			CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, ActorHandler actorhandler, 
			Room room)
	{
		super(x, y, DepthConstants.NORMAL, true, CollisionType.CIRCLE, drawer, 
				collidablehandler, collisionhandler, actorhandler);
		
		// Initializes attributes
		this.airfriction = 0.005;
		this.forcedelay = 0;
		this.minspeed = 6;
		this.spritedrawer = new SpriteDrawer(
				Main.spritebanks.getOpenSpriteBank("field").getSprite("ball"), 
				actorhandler);
		
		// Sets up movement stats
		setMaxSpeed(20);
		
		// Sets the collision precision
		setCircleCollisionPrecision(33, 6, 2);
		setRadius(33);
		
		// Adds the ball to the room (if possible)
		if (room != null)
			room.addOnject(this);
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	public void onCollision(ArrayList<DoublePoint> colpoints,
			Collidable collided)
	{
		// The ball collides with spells that need to collide with it
		if (collided instanceof SpellEffect)
		{
			SpellEffect effect = (SpellEffect) collided;
			// Some spells don't react to ball collisions
			if (!effect.collidesWithBalls())
				return;
			
			// Calculates the average collision point
			Point averagepoint = 
					HelpMath.getAverageDoublePoint(colpoints).getAsPoint();
			
			// Informs the spell about the collision
			effect.onBallCollision(this, averagepoint.x, averagepoint.y);
		}
	}

	@Override
	public int getWidth()
	{
		return this.spritedrawer.getSprite().getWidth();
	}

	@Override
	public int getHeight()
	{
		return this.spritedrawer.getSprite().getHeight();
	}

	@Override
	public int getOriginX()
	{
		return this.spritedrawer.getSprite().getOriginX();
	}

	@Override
	public int getOriginY()
	{
		return this.spritedrawer.getSprite().getOriginY();
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// Draws the sprite
		this.spritedrawer.drawSprite(g2d, 0, 0);
	}
	
	@Override
	public boolean kill()
	{
		// Kills the spritedrawer as well
		this.spritedrawer.kill();
		this.spritedrawer = null;
		
		return super.kill();
	}
	
	@Override
	public void act()
	{
		// Does all normal functions
		super.act();
		
		// Snaps back to the field
		bounceFromScreenBorders();
		
		// Each step slows the ball a bit
		if (getMovement().getSpeed() > this.minspeed)
			getMovement().multiplySPeed(1 - this.airfriction);
		
		// Rotates the ball depending on the current speed
		addAngle(getMovement().getSpeed());
		
		// Updates the force delay
		if (this.forcedelay > 0)
			this.forcedelay --;
	}
	
	@Override
	public void onRoomStart(Room room)
	{
		// Doesn't do anything
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// The ball is killed upon the end of the room
		kill();
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * With this method, an object can affect the ball's movement course.
	 *
	 * @param force How much force is added to the ball
	 * @param delay How long is the delay before full force can be applied again
	 * @param direction Towards which direction should the ball move
	 */
	public void impact(double force, int delay, double direction)
	{
		// If there's still force delay left, 90% of the impact is lost
		if (this.forcedelay > 0)
			force *= 0.1;
		else
		{
			// Otherwise updates the delay
			this.forcedelay = delay;
		}
		
		// Adds the motion
		addMotion(direction, force);
	}
	
	private void bounceFromScreenBorders()
	{
		if (getY() < getRadius())
			getMovement().setVSpeed(Math.abs(getMovement().getVSpeed()));
		else if (getY() > GameSettings.SCREENHEIGHT - getRadius())
			getMovement().setVSpeed(-Math.abs(getMovement().getVSpeed()));
		if (getX() < getRadius())
			getMovement().setHSpeed(Math.abs(getMovement().getHSpeed()));
		else if (getX() > GameSettings.SCREENWIDTH - getRadius())
			getMovement().setHSpeed(-Math.abs(getMovement().getHSpeed()));
	}
}
