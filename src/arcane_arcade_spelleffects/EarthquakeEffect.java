package arcane_arcade_spelleffects;

import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.DrawableHandler;
import helpAndEnums.CollisionType;
import helpAndEnums.DepthConstants;
import worlds.Room;
import arcane_arcade_field.Ball;
import arcane_arcade_field.Wizard;
import arcane_arcade_status.BallStatus;
import arcane_arcade_status.Element;

/**
 * Earthquake creates an area of effect that impacts the ball
 * 
 * @author Mikko Hilpinen. 
 * Created 18.1.2014
 */
public class EarthquakeEffect extends MaskedSpellEffect
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new earthquake effect to the given position.
	 * 
	 * @param x The x-coordinate of the quake's origin
	 * @param y The y-coordinate of the quake's origin
	 * @param drawer The DrawableHandler that will draw the effect
	 * @param collidablehandler The collidablehandler that will handle the 
	 * object's collision checking
	 * @param actorhandler the ActorHandler that will inform the object about 
	 * steps
	 * @param room The room where the quake is located at
	 */
	public EarthquakeEffect(int x, int y, DrawableHandler drawer,
			CollidableHandler collidablehandler, ActorHandler actorhandler,
			Room room)
	{
		super(x, y, DepthConstants.NORMAL, CollisionType.CIRCLE, drawer, 
				collidablehandler, null, actorhandler, room, "earthquake", 
				"earthquakemask", false, true, false, Element.EARTH, 
				Element.NOELEMENT, 70, true);
		
		// Adds other effects
		addAnimationEffect();
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onBallCollision(Ball ball, double x, double y)
	{
		// Causes impact to the ball
		ball.impact(10 * getForceModifier(ball), 5, 
				getDirectionTowardsObject(ball), BallStatus.MUDDY, 25);
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
