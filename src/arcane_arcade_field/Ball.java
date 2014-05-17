package arcane_arcade_field;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.HashMap;

import utopia_gameobjects.BouncingBasicPhysicDrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handleds.Collidable;
import utopia_utility.CollisionType;
import utopia_utility.DepthConstants;
import utopia_utility.HelpMath;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import utopia_worlds.Room;
import arcane_arcade_main.GameSettings;
import arcane_arcade_spelleffects.SpellEffect;
import arcane_arcade_status.BallStatus;
import arcane_arcade_status.WizardStatus;

/**
 * Ball is one of the main objects of the game. It mainly flies around the 
 * field and collides with different things.
 *
 * @author Mikko Hilpinen.
 *         Created 27.8.2013.
 */
public class Ball extends BouncingBasicPhysicDrawnObject implements RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private SingleSpriteDrawer spritedrawer;
	private BallStatusDrawer statusdrawer;
	
	private WizardRelay wizardrelay;
	
	private double airfriction, forcedelay;
	private int minspeed;
	private double statusdepletionrate;
	
	private HashMap<BallStatus, java.lang.Double> status;
	
	private static final Class<?>[] COLLIDINGCLASSES = new Class<?>[] {
		Wizard.class, SpellEffect.class};
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new ball to the given position
	 *
	 * @param area The area where the object is placed to
	 * @param x The ball's x-coordinate
	 * @param y The ball's y-coordinate
	 * @param wizardrelay The WizardRelay that contains information about the 
	 * wizards of the field
	 */
	public Ball(Area area, int x, int y, WizardRelay wizardrelay)
	{
		super(x, y, DepthConstants.NORMAL, true, CollisionType.CIRCLE, area);
		
		// Initializes attributes
		this.airfriction = 0.005;
		this.forcedelay = 0;
		this.minspeed = 6;
		this.spritedrawer = new SingleSpriteDrawer(
				MultiMediaHolder.getSpriteBank("field").getSprite("ball"), 
				area.getActorHandler(), this);
		this.statusdrawer = new BallStatusDrawer(area, this);
		this.statusdepletionrate = 0.1;
		this.wizardrelay = wizardrelay;
		this.status = new HashMap<BallStatus, java.lang.Double>();
		
		for (BallStatus ballStatus: BallStatus.values())
		{
			this.status.put(ballStatus, 0.0);
		}
		
		// Sets up movement stats
		setMaxSpeed(20);
		
		// Sets the collision precision
		setCircleCollisionPrecision(33, 5, 2);
		setRadius(33);
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------
	
	@Override
	public void onCollision(ArrayList<Double> colpoints, Collidable collided,
			double steps)
	{
		// The ball collides with spells that need to collide with it
		if (collided instanceof SpellEffect)
		{
			SpellEffect effect = (SpellEffect) collided;
			
			// Calculates the average collision point
			Point2D.Double averagepoint = 
					HelpMath.getAveragePoint(colpoints);
			
			// Informs the spell about the collision
			effect.onBallCollision(this, averagepoint.x, averagepoint.y);
		}
		// The ball may also collide with a wizard if they have IRONFLESH
		else if (collided instanceof Wizard)
		{
			Wizard wizard = (Wizard) collided;
			
			if (wizard.getStatusStrength(WizardStatus.IRONFLESH) > 0)
				bounceWithoutRotationFrom(wizard, colpoints.get(0), 1, 0, steps);
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
		this.spritedrawer.drawSprite(g2d, 0, 0);
		drawCollisionArea(g2d);
		drawCollisionPoints(g2d);
	}
	
	@Override
	public void kill()
	{
		// Kills the status drawer as well
		if (this.statusdrawer != null)
			this.statusdrawer.kill();
		this.statusdrawer = null;
		
		super.kill();
	}
	
	@Override
	public void act(double steps)
	{
		// Does all normal functions
		super.act(steps);
		
		// Snaps back to the field
		bounceFromScreenBorders();
		
		// Each step slows the ball a bit
		if (getMovement().getSpeed() > this.minspeed)
			getMovement().multiplySPeed(Math.pow(1 - this.airfriction, steps));
		
		// Rotates the ball depending on the current speed
		addAngle(getMovement().getSpeed() * steps);
		
		// Updates the force delay
		if (this.forcedelay > 0)
			this.forcedelay -= steps;
		
		// Updates the ball's status effects
		adjustStatusses(steps);
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
	
	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		// Only wizards and spells collide with the ball
		// TODO: Update this if needed
		return COLLIDINGCLASSES;
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
		return this.status.get(status);
	}
	
	private void bounceFromScreenBorders()
	{
		if (getY() < getRadius())
		{
			getMovement().setVSpeed(Math.abs(getMovement().getVSpeed()));
		}
		else if (getY() > GameSettings.SCREENHEIGHT - getRadius())
		{
			getMovement().setVSpeed(-Math.abs(getMovement().getVSpeed()));
		}
		// If the ball bounced from a vertical screen border, it hurts the 
		// wizard on that side of the screen
		if (getX() < getRadius())
		{
			getMovement().setHSpeed(Math.abs(getMovement().getHSpeed()));
			hurtWizards(ScreenSide.LEFT);
		}
		else if (getX() > GameSettings.SCREENWIDTH - getRadius())
		{
			getMovement().setHSpeed(-Math.abs(getMovement().getHSpeed()));
			hurtWizards(ScreenSide.RIGHT);
		}
	}
	
	private void hurtWizards(ScreenSide side)
	{
		ArrayList<Wizard> wizards = this.wizardrelay.getWizardsFromSide(side);
		// Goes through the list and hurts all wizards
		for (int i = 0; i < wizards.size(); i++)
		{
			wizards.get(i).causeDamage();
		}
	}
	
	private void addStatus(BallStatus status, double strength)
	{
		double value = this.status.get(status) + strength;
		
		if (value > 100)
			value = 100;
		else if (value < 0)
			value = 0;
		
		this.status.put(status, value);
	}
	
	private void adjustStatusses(double steps)
	{
		// Makes changes according to each status
		for (BallStatus ballStatus : this.status.keySet())
		{
			if (getStatusStrength(ballStatus) > 0)
			{
				// Depletes the status normally
				addStatus(ballStatus, -this.statusdepletionrate * steps);
			    
				// Increases some statuses if they are present
			    for (BallStatus buffedStatus : ballStatus.getBuffedStatuses())
			    {
			    	if (getStatusStrength(buffedStatus) > 0)
			    		addStatus(buffedStatus, this.statusdepletionrate * 2 * steps);
			    }
			    // Decreases others
			    for (BallStatus debuffedStatus : ballStatus.getDebuffedStatuses())
			    {
			    	if (getStatusStrength(debuffedStatus) > 0)
			    		addStatus(debuffedStatus, -this.statusdepletionrate * 2 * steps);
			    }
			}
		}
	}
}
