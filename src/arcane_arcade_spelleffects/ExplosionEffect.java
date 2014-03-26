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
 * Explosioneffect is a simple explosion that will last a while and then 
 * disappear
 *
 * @author Mikko Hilpinen.
 *         Created 28.8.2013.
 */
public class ExplosionEffect extends MaskedSpellEffect
{
	// ATTRIBUTES	-----------------------------------------------------
	
	// (Currently) only collides with balls
	private static final Class<?>[] COLLIDEDCLASSES = new Class<?>[]{Ball.class};
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new explosioneffect to the given location and adds it to the 
	 * given handlers
	 *
	 * @param x The explosion's x-coordinate
	 * @param y The explosion's y-coordinate
	 * @param drawer The drawer that will draw the explosion
	 * @param collidablehandler The collidablehandler that will handle the 
	 * explosion's collision checking
	 * @param actorhandler The actorhandler that will inform the explosion 
	 * about act events
	 * @param room The room where the explosion was created at
	 */
	public ExplosionEffect(int x, int y, DrawableHandler drawer,
			CollidableHandler collidablehandler, ActorHandler actorhandler,
			Room room)
	{
		super(x, y, DepthConstants.NORMAL, CollisionType.CIRCLE, drawer, 
				collidablehandler, null, actorhandler, room, "explosion", 
				"explosionmask", false, true, false, Element.BLAZE, 
				Element.NOELEMENT, 30, true);
		setRadius(100);
		addAnimationEffect();
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	public void onBallCollision(Ball ball, double x, double y)
	{
		// Causes impact to the ball
		ball.impact(15 * getForceModifier(ball), 15, 
				getDirectionTowardsObject(ball), BallStatus.FLAMING, 40);
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
