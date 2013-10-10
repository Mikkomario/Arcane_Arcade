package arcane_arcade_spelleffects;

import worlds.Room;
import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.DrawableHandler;
import helpAndEnums.CollisionType;
import helpAndEnums.DepthConstants;
import arcane_arcade_field.Ball;
import arcane_arcade_field.Wizard;
import arcane_arcade_status.Element;

/**
 * TeleportEffect is a very simple effect that simply appears and disappears
 *
 * @author Mikko Hilpinen.
 *         Created 28.8.2013.
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
	 * @param drawer The drawablehandler that will draw the effect
	 * @param collidablehandler The collidablehandler that will handle the 
	 * effect's collision checking
	 * @param actorhandler The actorhandler that will call the objec't act 
	 * event
	 * @param room The room in which the effect was created
	 */
	public TeleportEffect(int x, int y,
			DrawableHandler drawer, CollidableHandler collidablehandler, 
			ActorHandler actorhandler, Room room)
	{
		super(x, y, DepthConstants.NORMAL, CollisionType.CIRCLE, 
				drawer, collidablehandler, null, actorhandler, room, "freeze", 
				false, false, false, Element.NOELEMENT, Element.NOELEMENT, 20);
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
}
