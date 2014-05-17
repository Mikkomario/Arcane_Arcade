package arcane_arcade_spelleffects;

import utopia_utility.CollisionType;
import utopia_utility.DepthConstants;
import utopia_worlds.Area;
import arcane_arcade_field.Ball;
import arcane_arcade_field.Wizard;
import arcane_arcade_status.BallStatus;
import arcane_arcade_status.Element;

/**
 * Ice explosions are created when the ball collides with an ice mine. They 
 * work pretty much like normal explosions except for their dual typing.
 * 
 * @author Mikko Hilpinen. 
 * @since 17.1.2014
 */
public class IceExplosionEffect extends MaskedSpellEffect
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new iceExplosionEffect to the given position
	 * 
	 * @param x The new x-coordinate of the explosion
	 * @param y The new y-coordinate of the explosion
	 * @param area The area where the object is placed to
	 */
	public IceExplosionEffect(int x, int y, Area area)
	{
		super(x, y, DepthConstants.NORMAL, CollisionType.CIRCLE, "iceexplosion", 
				"iceexplosionmask", false, true, false, Element.BLAZE, 
				Element.FROST, 40, true, area);
		
		// Setups effects
		setRadius(100);
		addAnimationEffect();
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

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
}
