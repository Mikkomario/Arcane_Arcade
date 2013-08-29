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
 * WaveEffect is a simple wave which will last for a while.
 * 
 * @author Unto Solala
 * 			Created 29.8.2013
 */
public class WaveEffect extends MaskedSpellEffect
{
	//CONSTRUCTOR--------------------------------------------------
	
	/**
	 * Creates a new wave-effect to the given location and adds it to the 
	 * given handlers
	 *
	 * @param x The wave's x-coordinate
	 * @param y Theewave's y-coordinate
	 * @param drawer The drawer that will draw the wave
	 * @param collidablehandler The collidablehandler that will handle the 
	 * wave's collision checking
	 * @param actorhandler The actorhandler that will inform the wave
	 * about act events
	 * @param room The room where the wave was created at
	 * @param caster	The wizard casting the spell.
	 */
	public WaveEffect(int x, int y, DrawableHandler drawer,
			CollidableHandler collidablehandler, ActorHandler actorhandler,
			Room room)
	{
		super(x, y, DepthConstants.NORMAL, CollisionType.BOX, drawer, 
				collidablehandler, null, actorhandler, room, "wave", 
				"wavemask", false, true, false, Element.WATER, 
				Element.NOELEMENT, 35, true);
		addAnimationEffect();
		addScaleEffect(25, -1, 0.5);
	}

	//IMPLEMENTED METHODS----------------------------------------------
	
	@Override
	public void onBallCollision(Ball ball, int x, int y)
	{
		// Causes impact to the ball
		ball.impact(3 * getForceModifier(ball), 4, getMovement().getDirection(), 
				BallStatus.WET, 9);	
	}

	@Override
	public void onSpellCollision(SpellEffect spell, int x, int y) {
		// Doesn't collide with spells
	}

	@Override
	public void onWizardCollision(Wizard wizard, int x, int y) {
		// Doesn't collide with Wizards
	}
}
