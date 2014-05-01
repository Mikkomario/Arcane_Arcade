package arcane_arcade_spelleffects;

import utopia_utility.CollisionType;
import utopia_utility.DepthConstants;
import utopia_worlds.Area;
import arcane_arcade_field.Ball;
import arcane_arcade_field.Wizard;
import arcane_arcade_status.Element;

/**
 * Smokeeffect is a simple spelleffect that blocks the line of sight of the 
 * enemy
 *
 * @author Mikko Hilpinen.
 * @since 29.8.2013.
 */
public class SmokeEffect extends MaskedSpellEffect
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new smokeeffect to the given position added to the given 
	 * handlers
	 *
	 * @param x The x-coordinate of the smoke
	 * @param y The y-coordinate of the smoke
	 * @param duration How long will the smoke last (steps)
	 * @param area The area where the object is placed to
	 */
	public SmokeEffect(int x, int y, int duration, Area area)
	{
		super(x, y, DepthConstants.FOREGROUND - 30, CollisionType.BOX, 
				"smoke", "cloudmask", false, false, false, Element.NOELEMENT, 
				Element.NOELEMENT, duration, false, area);
		setBoxCollisionPrecision(2, 2);
		addFadeEffect((int) (duration * 0.1), (int) (duration * 0.8));
		addScaleEffect(duration, -1, 0.7);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onBallCollision(Ball ball, double x, double y)
	{
		// Doesn't collide with balls
	}

	@Override
	public void onSpellCollision(SpellEffect spell, double x, double y)
	{
		//TODO: Add collision with hurricane and other wind type spells when 
		// they appear
	}

	@Override
	public void onWizardCollision(Wizard wizard, double x, double y)
	{
		// Doesn't collide with wizards
	}

	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		// TODO Add collisions with wind spells after they have been made
		return new Class<?>[0];
	}
}
