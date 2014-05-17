package arcane_arcade_spelleffects;

import utopia_utility.CollisionType;
import utopia_utility.DepthConstants;
import utopia_worlds.Area;
import arcane_arcade_field.Ball;
import arcane_arcade_field.Wizard;
import arcane_arcade_status.Element;

/**
 * Ice mines scatter through the field and explode when they collide with the 
 * ball.
 * 
 * @author Mikko Hilpinen. 
 * @since 17.1.2014
 */
public class IceMineEffect extends MaskedSpellEffect
{	
	// ATTRIBUTES	-----------------------------------------------------
	
	private Area area;

	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new ice mine to the given position with the given duration.
	 * 
	 * @param x The new x-coordinate of the mine
	 * @param y The new y-coordinate of the mine
	 * @param lifetime The duration the mine stays on the field
	 * @param area The area where the object is placed to
	 */
	public IceMineEffect(int x, int y, int lifetime, Area area)
	{
		super(x, y, DepthConstants.NORMAL, CollisionType.BOX, "icemine", 
				"iceminemask", false, true, false, Element.BLAZE, Element.FROST, 
				lifetime, false, area);
		
		// Initializes attributes
		this.area = area;

		// Sets additional events
		addFadeEffect(10, lifetime - 10);
		setFriction(0.25);
	}
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------

	@Override
	public void onBallCollision(Ball ball, double x, double y)
	{
		// Creates a new ice explosion effect and then disappears
		new IceExplosionEffect((int) getX(), (int) getY(), this.area);
		kill();
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
