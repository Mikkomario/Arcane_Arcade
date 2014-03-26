package arcane_arcade_spelleffects;

import utopia_handlers.ActorHandler;
import utopia_handlers.CollidableHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.DepthConstants;
import utopia_worlds.Room;
import arcane_arcade_field.Ball;
import arcane_arcade_field.Wizard;
import arcane_arcade_status.BallStatus;
import arcane_arcade_status.Element;

/**
 * Ice explosions are created when the ball collides with an ice mine. They 
 * work pretty much like normal explosions except for their dual typing.
 * 
 * @author Mikko Hilpinen. 
 * Created 17.1.2014
 */
public class IceExplosionEffect extends MaskedSpellEffect
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private static final Class<?>[] COLLIDEDCLASSES = new Class<?>[]{Ball.class};
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new iceExplosionEffect to the given position
	 * 
	 * @param x The new x-coordinate of the explosion
	 * @param y The new y-coordinate of the explosion
	 * @param drawer The DrawableHandler that will draw the explosion
	 * @param collidablehandler The collidableHandler that will handle the object's 
	 * collision checking
	 * @param actorhandler The actorHandler that will inform the object about 
	 * steps
	 * @param room The room where the explosion happens
	 */
	public IceExplosionEffect(int x, int y, DrawableHandler drawer,
			CollidableHandler collidablehandler, ActorHandler actorhandler,
			Room room)
	{
		super(x, y, DepthConstants.NORMAL, CollisionType.CIRCLE, drawer, 
				collidablehandler, null, actorhandler, room, "iceexplosion", 
				"iceexplosionmask", false, true, false, Element.BLAZE, 
				Element.FROST, 40, true);
		
		// Setups effects
		setRadius(100);
		addAnimationEffect();
	}

	@Override
	public void onBallCollision(Ball ball, double x, double y)
	{
		// Causes impact
		ball.impact(15 * getForceModifier(ball), 15, 
				getDirectionTowardsObject(ball), BallStatus.NOSTATUS, 0);
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
