package arcane_arcade_spelleffects;

import utopia_utility.CollisionType;
import utopia_utility.DepthConstants;
import utopia_worlds.Area;
import arcane_arcade_field.Ball;
import arcane_arcade_field.Wizard;
import arcane_arcade_status.BallStatus;
import arcane_arcade_status.Element;

/**
 * WaveEffect is a simple wave which will last for a while.
 * 
 * @author Unto Solala & Mikko Hilpinen
 * @since 29.8.2013
 */
public class WaveEffect extends MaskedSpellEffect
{
	//CONSTRUCTOR	--------------------------------------------------
	
	/**
	 * Creates a new wave-effect to the given location and adds it to the 
	 * given handlers
	 *
	 * @param x The wave's x-coordinate
	 * @param y Theewave's y-coordinate
	 * @param area The area where the object is placed to
	 */
	public WaveEffect(int x, int y, Area area)
	{
		super(x, y, DepthConstants.NORMAL, CollisionType.BOX, "wave", 
				"wavemask", false, true, false, Element.TIDE, 
				Element.NOELEMENT, 35, true, area);
		addAnimationEffect();
		addScaleEffect(25, -1, 0.5);
	}

	//IMPLEMENTED METHODS----------------------------------------------
	
	@Override
	public void onBallCollision(Ball ball, double x, double y)
	{
		// Causes impact to the ball
		ball.impact(2.2 * getForceModifier(ball), 4, getMovement().getDirection(), 
				BallStatus.WET, 9);	
	}

	@Override
	public void onSpellCollision(SpellEffect spell, double x, double y) {
		// Doesn't collide with spells
	}

	@Override
	public void onWizardCollision(Wizard wizard, double x, double y) {
		// Doesn't collide with Wizards
	}
}
