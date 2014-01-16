package arcane_arcade_field;

import java.awt.Graphics2D;

import arcane_arcade_status.BallStatus;
import arcane_arcade_worlds.Navigator;

import gameobjects.DrawnObject;
import graphic.SingleSpriteDrawer;
import handlers.ActorHandler;
import handlers.DrawableHandler;

/**
 * BallStatusDrawer draws all ballstatusses that need drawing
 *
 * @author Mikko Hilpinen.
 *         Created 30.8.2013.
 */
public class BallStatusDrawer extends DrawableHandler
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Ball ball;
	private ActorHandler animator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new ballstatusdrawer added to the given superhandler
	 *
	 * @param superhandler Which drawablehandler will draw the drawer
	 * @param animator Which actorhandler will animate the status sprites
	 * @param ball The ball who's statuseffects are drawn
	 */
	public BallStatusDrawer(DrawableHandler superhandler, ActorHandler animator, 
			Ball ball)
	{
		super(true, false, ball.getDepth() - 1, superhandler);
		
		// Initializes attributes
		this.ball = ball;
		this.animator = animator;
		
		// Creates the drawers and adds them to the handleds
		new FlamingDrawer(this);
		new WetDrawer(this);
		new FrozenDrawer(this);
		new MuddyDrawer(this);
		new ChargedDrawer(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	protected Class<?> getSupportedClass()
	{
		// Only handles statusdrawers
		return StatusDrawer.class;
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
		 * @param drawer The drawablehandler that will draw the drawer
		 * @param statusspritename what sprite in the bank "status" represents 
		 * the status effect
		 * @param status The status effect the drawer represents
		 */
		public StatusDrawer(BallStatusDrawer drawer, String statusspritename, 
				BallStatus status)
		{
			super(0, 0, 0, drawer);
			
			// Initializes attributes
			this.spritedrawer = new SingleSpriteDrawer(Navigator.getSpriteBank(
					"status").getSprite(statusspritename), 
					BallStatusDrawer.this.animator, this);
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
	}

	private class FlamingDrawer extends StatusDrawer
	{
		/**
		 * Creates a new flamingdrawer
		 *
		 * @param drawer The drawer that will draw the drawer
		 */
		public FlamingDrawer(BallStatusDrawer drawer)
		{
			super(drawer, "flame", BallStatus.FLAMING);
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
		 * Creates a new flamingdrawer
		 *
		 * @param drawer The drawer that will draw the drawer
		 */
		public WetDrawer(BallStatusDrawer drawer)
		{
			super(drawer, "wet", BallStatus.WET);
			setScale(1.5, 1.5);
		}
	}
	
	private class FrozenDrawer extends StatusDrawer
	{
		/**
		 * Creates a new flamingdrawer
		 *
		 * @param drawer The drawer that will draw the drawer
		 */
		public FrozenDrawer(BallStatusDrawer drawer)
		{
			super(drawer, "freeze", BallStatus.FROZEN);
		}
	}
	
	private class MuddyDrawer extends StatusDrawer
	{
		/**
		 * Creates a new flamingdrawer
		 *
		 * @param drawer The drawer that will draw the drawer
		 */
		public MuddyDrawer(BallStatusDrawer drawer)
		{
			super(drawer, "muddy", BallStatus.MUDDY);
		}
	}
	
	private class ChargedDrawer extends StatusDrawer
	{
		/**
		 * Creates a new flamingdrawer
		 *
		 * @param drawer The drawer that will draw the drawer
		 */
		public ChargedDrawer(BallStatusDrawer drawer)
		{
			super(drawer, "charge", BallStatus.CHARGED);
			
			setScale(1.5, 1.5);
		}
	}
}
