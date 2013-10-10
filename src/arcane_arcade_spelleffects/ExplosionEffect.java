package arcane_arcade_spelleffects;

import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.DrawableHandler;
import helpAndEnums.CollisionType;
import helpAndEnums.DepthConstants;
import helpAndEnums.HelpMath;
import worlds.Room;
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
				Element.NOELEMENT, 25, true);
		setRadius(100);
		addAnimationEffect();
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	public void onBallCollision(Ball ball, double x, double y)
	{
		// Causes impact to the ball
		ball.impact(17 * getForceModifier(ball), 15, 
				HelpMath.pointDirection(getX(), getY(), ball.getX(), 
				ball.getY()), BallStatus.FLAMING, 40);
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
}
