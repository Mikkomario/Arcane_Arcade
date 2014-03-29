package arcane_arcade_field;

import java.awt.Graphics2D;

import utopia_gameobjects.DrawnObject;
import utopia_gameobjects.GameObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import arcane_arcade_status.BallStatus;

/**
 * BallStatusDrawer draws all ballstatusses that need drawing
 *
 * @author Mikko Hilpinen.
 * @since 30.8.2013.
 */
public class BallStatusDrawer extends GameObject
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Ball ball;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new ballstatusdrawer added to the given superhandler
	 *
	 * @param area The area where the object is placed to
	 * @param ball The ball who's statuseffects are drawn
	 */
	public BallStatusDrawer(Area area, Ball ball)
	{
		super(area);
		
		// Initializes attributes
		this.ball = ball;
		
		// Creates the drawers and adds them to the handleds
		new FlamingDrawer(area);
		new WetDrawer(area);
		new FrozenDrawer(area);
		new MuddyDrawer(area);
		new ChargedDrawer(area);
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	private class StatusDrawer extends DrawnObject
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private SingleSpriteDrawer spritedrawer;
		private BallStatus status;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		/**
		 * Creates a new statusdrawer
		 *
		 * @param statusspritename what sprite in the bank "status" represents 
		 * the status effect
		 * @param status The status effect the drawer represents
		 * @param area The area where the object is placed to
		 */
		public StatusDrawer(String statusspritename, 
				BallStatus status, Area area)
		{
			super(0, 0, 0, area);
			
			// Initializes attributes
			this.spritedrawer = new SingleSpriteDrawer(MultiMediaHolder.getSpriteBank(
					"status").getSprite(statusspritename), 
					area.getActorHandler(), this);
			this.status = status;
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------

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
			// Forces status update
			forceTransformationUpdate();
			
			// Draws the sprite
			this.spritedrawer.drawSprite(g2d, 0, 0);
		}
		
		@Override
		public double getX()
		{
			// Uses the ball's coordinates
			return BallStatusDrawer.this.ball.getX();
		}
		
		@Override
		public double getY()
		{
			// Uses the ball's coordinates
			return BallStatusDrawer.this.ball.getY();
		}
		
		@Override
		public float getAlpha()
		{
			// The alpha value of the object depends on the strength of a 
			// status effect
			return (float) BallStatusDrawer.this.ball.getStatusStrength(
					this.status) / 100;
		}
		
		@Override
		public boolean isVisible()
		{
			// Only status effects that are affecting the ball are visible
			return super.isVisible() && 
					BallStatusDrawer.this.ball.getStatusStrength(
					this.status) > 0;
		}
		
		@Override
		public double getAngle()
		{
			// The drawer uses ball's angle
			return BallStatusDrawer.this.ball.getMovement().getDirection();
		}
		
		@Override
		public boolean isDead()
		{
			return BallStatusDrawer.this.ball.isDead();
		}
	}

	private class FlamingDrawer extends StatusDrawer
	{
		/**
		 * Creates a new flamingdrawer
		 *
		 * @param area The area where the object is placed to
		 */
		public FlamingDrawer(Area area)
		{
			super("flame", BallStatus.FLAMING, area);
		}
		
		@Override
		public double getAngle()
		{
			// The drawer uses ball's angle
			return BallStatusDrawer.this.ball.getMovement().getDirection() + 90;
		}
	}
	
	private class WetDrawer extends StatusDrawer
	{
		/**
		 * Creates a new wetdrawer
		 *
		 * @param area The area where the object is placed to
		 */
		public WetDrawer(Area area)
		{
			super("wet", BallStatus.WET, area);
			setScale(1.5, 1.5);
		}
	}
	
	private class FrozenDrawer extends StatusDrawer
	{
		/**
		 * Creates a new frozendrawer
		 *
		 * @param area The area where the object is placed to
		 */
		public FrozenDrawer(Area area)
		{
			super("freeze", BallStatus.FROZEN, area);
		}
	}
	
	private class MuddyDrawer extends StatusDrawer
	{
		/**
		 * Creates a new muddydrawer
		 *
		 * @param area The area where the object is placed to
		 */
		public MuddyDrawer(Area area)
		{
			super("muddy", BallStatus.MUDDY, area);
		}
	}
	
	private class ChargedDrawer extends StatusDrawer
	{
		/**
		 * Creates a new chargeddrawer
		 *
		 * @param area The area where the object is placed to
		 */
		public ChargedDrawer(Area area)
		{
			super("charge", BallStatus.CHARGED, area);
			
			setScale(1.5, 1.5);
		}
	}
}
