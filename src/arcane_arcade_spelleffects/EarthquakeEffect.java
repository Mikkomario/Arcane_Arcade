package arcane_arcade_spelleffects;

import utopia_utility.CollisionType;
import utopia_utility.DepthConstants;
import utopia_worlds.Area;
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
	// ATTRIBUTES	-----------------------------------------------------
	
	// Only collides with balls
	private static final Class<?>[] COLLIDEDCLASSES = new Class<?>[]{Ball.class};
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new earthquake effect to the given position.
	 * 
	 * @param x The x-coordinate of the quake's origin
	 * @param y The y-coordinate of the quake's origin
	 * @param area The area where the object is placed to
	 */
	public EarthquakeEffect(int x, int y, Area area)
	{
		super(x, y, DepthConstants.NORMAL, CollisionType.CIRCLE, "earthquake", 
				"earthquakemask", false, true, false, Element.EARTH, 
				Element.NOELEMENT, 70, true, area);
		
		// Adds other effects
		addAnimationEffect();
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onBallCollision(Ball ball, double x, double y)
	{
		// Causes impact to the ball
		ball.impact(10 * getForceModifier(ball), 5, 
				getDirectionTowardsObject(ball), BallStatus.PETRIFIED, 25);
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
