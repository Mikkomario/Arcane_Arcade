package arcane_arcade_spelleffects;

import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.DrawableHandler;
import helpAndEnums.CollisionType;
import helpAndEnums.DepthConstants;
import worlds.Room;
import arcane_arcade_field.Ball;
import arcane_arcade_field.ScreenSide;
import arcane_arcade_field.Wizard;
import arcane_arcade_status.BallStatus;
import arcane_arcade_status.Element;

/**
 * Frost barrier bounces the ball right back unless the ball is flaming.
 * 
 * @author Mikko Hilpinen. 
 * Created 17.1.2014
 */
public class FrostBarrierEffect extends SpellEffect
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private ScreenSide side;
	// Currently only collides with balls
	private static final Class<?>[] COLLIDEDCLASSES = new Class<?>[]{Ball.class};
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new frost barrier to the given position
	 * 
	 * @param x The x-coordinate of the barrier
	 * @param y The y-coordinate of the barrier
	 * @param drawer The DrawableHandler that will draw the barrier
	 * @param collidablehandler The collidableHandler that will handle the 
	 * barrier's collision checking
	 * @param actorhandler The actorHandler that informs the object about steps
	 * @param room The room where the barrier resides
	 * @param duration How long the frost barrier will remain on the field
	 * @param side The side the barrier protects
	 */
	public FrostBarrierEffect(int x, int y, DrawableHandler drawer,
			CollidableHandler collidablehandler, ActorHandler actorhandler,
			Room room, int duration, ScreenSide side)
	{
		super(x, y, DepthConstants.NORMAL, CollisionType.BOX, drawer, 
				collidablehandler, null, actorhandler, room, "frostbarrier", 
				false, true, false, Element.FROST, Element.TIDE, duration);
		
		// Initializes attributes
		this.side = side;
		
		// Sets up other effects
		addFadeEffect(30, duration - 10);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onBallCollision(Ball ball, double x, double y)
	{
		// Either bounces the ball or pathetically tries to impact it
		if (ball.getStatusStrength(BallStatus.FLAMING) < 1)
		{
			double newhspeed = Math.abs(ball.getMovement().getHSpeed()) * 0.95;
			if (this.side == ScreenSide.RIGHT)
				newhspeed *= -1;
			
			ball.getMovement().setHSpeed(newhspeed);
		}
		else
		{
			double forcedir = 0;
			if (this.side == ScreenSide.RIGHT)
				forcedir = 180;
			
			ball.impact(1, 20, forcedir, BallStatus.WET, 3);
		}
	}

	@Override
	public void onSpellCollision(SpellEffect spell, double x, double y)
	{
		// Doesn't collide with spells
	}

	@Override
	public void onWizardCollision(Wizard wizard, double x, double y)
	{
		// Doesn't collide with wizards
	}

	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		return COLLIDEDCLASSES;
	}
}
