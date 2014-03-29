package arcane_arcade_spelleffects;

import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.DepthConstants;
import utopia_worlds.Area;
import arcane_arcade_field.Ball;
import arcane_arcade_field.Wizard;
import arcane_arcade_status.Element;

/**
 * TeleportEffect is a very simple effect that simply appears and disappears
 *
 * @author Mikko Hilpinen.
 * @since 28.8.2013.
 */
public class TeleportEffect extends SpellEffect
{
	// CONSTRUCTOR	-----------------------------------------------------

	/**
	 * Creates a new teleporteffect to the given position added to the given 
	 * handlers.
	 *
	 * @param x The effect's x-coordinate
	 * @param y The effect's y-coordinate
	 * @param area The area where the object is placed to
	 */
	public TeleportEffect(int x, int y, Area area)
	{
		super(x, y, DepthConstants.NORMAL, CollisionType.CIRCLE, "freeze", 
				false, false, false, Element.NOELEMENT, Element.NOELEMENT, 20, area);
		addAnimationEffect();
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onBallCollision(Ball ball, double x, double y)
	{
		// Doesn't collide with balls
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
		// Nothing collides with teleport effects, they are purely visual
		return new Class<?>[0];
	}
}
