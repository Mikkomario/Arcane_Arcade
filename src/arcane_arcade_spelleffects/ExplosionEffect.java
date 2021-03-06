package arcane_arcade_spelleffects;

import utopia_utility.CollisionType;
import utopia_utility.DepthConstants;
import utopia_worlds.Area;
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
	 * @param area The area where the object is placed to
	 */
	public ExplosionEffect(int x, int y, Area area)
	{
		super(x, y, DepthConstants.NORMAL, CollisionType.CIRCLE, "explosion", 
				"explosionmask", false, true, false, Element.BLAZE, 
				Element.NOELEMENT, 30, true, area);
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
}
