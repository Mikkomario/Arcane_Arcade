package arcane_arcade_field;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import utopia_gameobjects.BasicPhysicDrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handleds.Collidable;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.DepthConstants;
import utopia_listeners.RoomListener;
import utopia_listeners.TimerEventListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_timers.SingularTimer;
import utopia_worlds.Area;
import utopia_worlds.Room;


/**
 * Server spins, shoots the ball and then vanishes until it is needed again
 *
 * @author Mikko Hilpinen.
 *         Created 27.8.2013.
 */
public class Server extends BasicPhysicDrawnObject implements RoomListener, 
		TimerEventListener
{
	// ATTRIBUTES	----------------------------------------------------
	
	private SingleSpriteDrawer spritedrawer;
	private Area area;
	private BallRelay ballrelay;
	private WizardRelay wizardrelay;
	
	private boolean serving;
	private double rotationfriction;
	private int minrotation, maxrotation, minshootforce, 
			maxshootforce;
	private Random rand;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new server to the given location. The server will then 
	 * automatically shoot the ball
	 *
	 * @param x The server's x-coordinate
	 * @param y The server's y-coordinate
	 * @param ballrelay The ballrelay where the created balls will be created
	 * @param wizardrelay The wizardrelay that has information about the wizards 
	 * in the field
	 * @param area The area where the object is placed to
	 */
	public Server(int x, int y, BallRelay ballrelay, 
			WizardRelay wizardrelay, Area area)
	{
		super(x, y, DepthConstants.BOTTOM - 10, false, CollisionType.BOX, 
				area);
		
		// Initializes attributes
		this.serving = false;
		this.rotationfriction = 0.5;
		this.minrotation = 15;
		this.maxrotation = 30;
		this.minshootforce = 10;
		this.maxshootforce = 15;
		this.spritedrawer = new SingleSpriteDrawer(
				MultiMediaHolder.getSpriteBank("field").getSprite("server"), 
				area.getActorHandler(), this);
		this.area = area;
		this.ballrelay = ballrelay;
		this.wizardrelay = wizardrelay;
		this.rand = new Random();
		
		// Serves the ball
		serve();
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onCollision(ArrayList<Point2D.Double> colpoints,
			Collidable collided, double steps)
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
		
		// Slows the rotation and shortens shoot time if serving
		if (this.serving)
		{
			slowrotation(steps);
		}
	}
	
	@Override
	public void onTimerEvent(int timeridentifier)
	{
		// Shoots the ball on timer event(s)
		shootBall();
	}
	
	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		// Server doesn't collide with anything by default but doesn't limit it 
		// either
		return null;
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
	
	private void slowrotation(double steps)
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
		    addRotation(-this.rotationfriction * steps);
		    
		addRotation(-this.rotationfriction * steps);

		// Checks if the server has stopped spinning
		// And shoots balls soon if it has
		if (getRotation() <= 0)
		{
		    setRotation(0);
		    new SingularTimer(this, 45, 0, this.area.getActorHandler());
		}
	}
	
	private void shootBall()
	{
		this.serving = false;
		// Creates the ball
		Ball newball = new Ball(this.area, (int) getX(), (int) getY(), this.wizardrelay);
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
