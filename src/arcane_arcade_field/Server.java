package arcane_arcade_field;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import worlds.Room;

import listeners.RoomListener;

import arcane_arcade_main.Main;

import graphic.SpriteDrawer;
import handleds.Collidable;
import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import helpAndEnums.CollisionType;
import helpAndEnums.DepthConstants;
import helpAndEnums.DoublePoint;
import drawnobjects.BasicPhysicDrawnObject;

/**
 * Server spins, shoots the ball and then vanishes until it is needed again
 *
 * @author Mikko Hilpinen.
 *         Created 27.8.2013.
 */
public class Server extends BasicPhysicDrawnObject implements RoomListener
{
	// ATTRIBUTES	----------------------------------------------------
	
	private SpriteDrawer spritedrawer;
	private ActorHandler actorhandler;
	private DrawableHandler drawer;
	private CollidableHandler collidablehandler;
	private CollisionHandler collisionhandler;
	private Room room;
	private BallRelay ballrelay;
	private WizardRelay wizardrelay;
	
	private boolean serving;
	private double rotationfriction;
	private int shootdelay, minrotation, maxrotation, minshootforce, 
			maxshootforce;
	private Random rand;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new server to the given location. The server will then 
	 * automatically shoot the ball
	 *
	 * @param x The server's x-coordinate
	 * @param y The server's y-coordinate
	 * @param drawer The drawer that will draw the server and the balls
	 * @param actorhandler The actorhandler that will call the act-events of 
	 * the server and the balls
	 * @param collidablehandler The collidablehandler that will handle the 
	 * collision detection of the ball
	 * @param collisionhandler The collisionhandler that will inform the 
	 * ball about collisions
	 * @param room where the server and the balls are created at
	 * @param ballrelay The ballrelay where the created balls will be created
	 * @param wizardrelay The wizardrelay that has information about the wizards 
	 * in the field
	 */
	public Server(int x, int y, DrawableHandler drawer, 
			ActorHandler actorhandler, CollidableHandler collidablehandler, 
			CollisionHandler collisionhandler, Room room, BallRelay ballrelay, 
			WizardRelay wizardrelay)
	{
		super(x, y, DepthConstants.BOTTOM - 10, false, CollisionType.BOX, 
				drawer, null, null, actorhandler);
		
		// Initializes attributes
		this.serving = false;
		this.rotationfriction = 0.5;
		this.shootdelay = 0;
		this.minrotation = 15;
		this.maxrotation = 30;
		this.minshootforce = 10;
		this.maxshootforce = 15;
		this.spritedrawer = new SpriteDrawer(
				Main.spritebanks.getOpenSpriteBank("field").getSprite("server"), 
				actorhandler);
		this.actorhandler = actorhandler;
		this.collidablehandler = collidablehandler;
		this.collisionhandler = collisionhandler;
		this.drawer = drawer;
		this.room = room;
		this.ballrelay = ballrelay;
		this.wizardrelay = wizardrelay;
		this.rand = new Random();
		
		// Serves the ball
		serve();
		
		// Adds the server to the room where it was created (if possible)
		if (this.room != null)
			this.room.addOnject(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onCollision(ArrayList<DoublePoint> colpoints,
			Collidable collided)
	{
		// Servers don't collide with anything
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
		if (this.spritedrawer == null)
			return;
		this.spritedrawer.drawSprite(g2d, 0, 0);
	}
	
	@Override
	public boolean kill()
	{
		// Also kills the spritedrawer when dies
		this.spritedrawer.kill();
		
		return super.kill();
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
	public void act()
	{
		super.act();
		
		// Slows the rotation and shortens shoot time if serving
		if (this.serving)
		{
			slowrotation();
			
			if (getRotation() == 0)
			{
				if (this.shootdelay > 0)
					this.shootdelay --;
				else
					shootBall();
			}
		}
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * The server becomes visible and starts rotating. After the server has 
	 * stopped, it shoots a ball and becomes invisible again.
	 */
	public void serve()
	{
		// Only works if the server isn't already serving
		if (this.serving)
			return;
		// Makes the object visible
		setVisible();
		activate();
		// Starts rotating
		setAngle(this.rand.nextDouble() * 360);
		setRotation(this.minrotation + this.rand.nextDouble() * 
				(this.maxrotation - this.minrotation));
		this.serving = true;
	}
	
	private void slowrotation()
	{
		// Slows down the rotation
		if (getRotation() == 0)
			return;
		
		// Doesn't slow down when facing up / down
		if (getAngle() > 45 && getAngle() < 135)
		    return;
		if (getAngle() > 225 && getAngle() < 315)
		    return;
		    
		// Slows down extra if the angle is about 22.5*n
		if (getAngle() % 22.5 < 5)
		    addRotation(-this.rotationfriction);
		    
		addRotation(-this.rotationfriction);

		// Checks if the server has stopped spinning
		// And shoots balls soon if it has
		if (getRotation() <= 0)
		{
		    setRotation(0);
		    this.shootdelay = 45;
		}
	}
	
	private void shootBall()
	{
		this.serving = false;
		// Creates the ball
		Ball newball = new Ball((int) getX(), (int) getY(), this.drawer, 
				this.collidablehandler, this.collisionhandler, 
				this.actorhandler, this.room, this.wizardrelay);
		// Adds the ball to the relay
		if (this.ballrelay != null)
			this.ballrelay.addBall(newball);
		// Shoots the ball
		newball.setMotion(getAngle(), this.minshootforce + 
				this.rand.nextDouble() * (this.maxshootforce - 
				this.minshootforce));
		// Vanishes
		setInvisible();
		inactivate();
	}
}
