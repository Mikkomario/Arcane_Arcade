package arcane_arcade_field;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import worlds.Room;

import listeners.RoomListener;

import arcane_arcade_main.GameSettings;
import arcane_arcade_main.Main;
import arcane_arcade_spelleffects.SpellEffect;
import arcane_arcade_status.BallStatus;

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
	private double flaming, wet, frozen, muddy, charged, statusdepletionrate;
	
	
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
		this.flaming = 0;
		this.wet = 0;
		this.frozen = 0;
		this.muddy = 0;
		this.charged = 0;
		this.spritedrawer = new SpriteDrawer(
				Main.spritebanks.getOpenSpriteBank("field").getSprite("ball"), 
				actorhandler);
		this.statusdepletionrate = 0.2;
		
		// Sets up movement stats
		setMaxSpeed(20);
		
		// Sets the collision precision
		setCircleCollisionPrecision(33, 5, 2);
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
		//drawCollisionPoints(g2d);
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
		
		// Updates the ball's status effects
		adjustStatusses();
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
	 * @param statuseffect A statuseffect applied to the ball (only if full 
	 * force is applied)
	 * @param statusstrength How much the status effect affects the ball [1, 100] 
	 * (100 meaning that the status becomes maxed instantly) 
	 */
	public void impact(double force, int delay, double direction, 
			BallStatus statuseffect, int statusstrength)
	{
		// If there's still force delay left, 90% of the impact is lost
		if (this.forcedelay > 0)
			force *= 0.1;
		else
		{
			// Otherwise updates the delay
			this.forcedelay = delay;
			// Adds the status effect
			addStatus(statuseffect, statusstrength);
		}
		
		// Adds the motion
		addMotion(direction, force);
	}
	
	/**
	 * Returns the strength of a single status effect the ball has
	 *
	 * @param status The status effect the ball might have
	 * @return How strongly is that status affecting the ball [0, 100]
	 */
	public double getStatusStrength(BallStatus status)
	{
		switch(status)
		{
		case FLAMING: return this.flaming;
		case WET: return this.wet;
		case FROZEN: return this.frozen;
		case MUDDY: return this.muddy;
		case CHARGED: return this.charged;
		default: return 0;
		}
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
	
	private void addStatus(BallStatus status, int strength)
	{
		switch(status)
		{
			case FLAMING: this.flaming += strength; break;
			case FROZEN: this.frozen += strength; break;
			case WET: this.wet += strength; break;
			case MUDDY: this.muddy += strength; break;
			case CHARGED: this.charged += strength; break;
			default: break;
		}
	}
	
	private void adjustStatusses()
	{
		// Makes changes according to each status
		if (this.flaming > 0)
		{
		    this.flaming -= this.statusdepletionrate;
		    if (this.frozen > 0)
		        this.frozen -= this.statusdepletionrate;
		    if (this.wet > 0)
		        this.wet -= this.statusdepletionrate * 2;
		}
		if (this.frozen > 0)
		{
		    this.frozen -= this.statusdepletionrate;
		    if (this.flaming > 0)
		    	this.flaming -= this.statusdepletionrate * 1.5;
		    if (getMovement().getSpeed() > this.minspeed * 0.75)
		        getMovement().multiplySPeed(1 - 0.01 * (this.frozen / 100));
		}
		if (this.wet > 0)
		{
		    this.wet -= this.statusdepletionrate;
		    if (this.flaming > 0)
		        this.flaming -= this.statusdepletionrate;
		    if (this.muddy > 0)
		        this.muddy -= this.statusdepletionrate * 2;
		    if (this.charged > 0)
		        this.charged += this.statusdepletionrate * 2;
		}
		if (this.muddy > 0)
		{
		    this.muddy -= this.statusdepletionrate;
		    if (this.flaming > 0)
		        this.flaming -= this.statusdepletionrate * 1.5;
		    if (this.charged > 0)
		        this.charged -= this.statusdepletionrate * 2;
		    if (getMovement().getSpeed() > this.minspeed * 0.75)
		    	getMovement().multiplySPeed(1 - 0.01 * (this.muddy / 100));
		}
		if (this.charged > 0)
		{
		    this.charged -= this.statusdepletionrate;
		    if (this.wet > 0)
		        this.wet -= this.statusdepletionrate;
		    if (getMovement().getSpeed() < getMaxSpeed())
		        getMovement().multiplySPeed(1 + 0.005 * (this.charged / 100));
		}
		// takes the effects back to the limits
		if (this.flaming < 0)
			this.flaming = 0;
		if (this.wet < 0)
			this.wet = 0;
		if (this.frozen < 0)
			this.frozen = 0;
		if (this.muddy < 0)
			this.muddy = 0;
		if (this.charged < 0)
			this.charged = 0;
		else if (this.charged > 100)
			this.charged = 100;
	}
}
