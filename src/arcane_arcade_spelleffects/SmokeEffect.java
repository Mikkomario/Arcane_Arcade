package arcane_arcade_spelleffects;

import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.DrawableHandler;
import helpAndEnums.CollisionType;
import helpAndEnums.DepthConstants;
import worlds.Room;
import arcane_arcade_field.Ball;
import arcane_arcade_field.Wizard;
import arcane_arcade_status.Element;

/**
 * Smokeeffect is a simple spelleffect that blocks the line of sight of the 
 * enemy
 *
 * @author Mikko Hilpinen.
 *         Created 29.8.2013.
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
	 * @param drawer The drawer that will draw the smoke
	 * @param collidablehandler The collidablehandler that will handle the 
	 * smoke's collision checking
	 * @param actorhandler The actorhandler that will inform the object about 
	 * steps
	 * @param room The room where the object is created at
	 * @param duration How long will the smoke last
	 */
	public SmokeEffect(int x, int y, DrawableHandler drawer, 
			CollidableHandler collidablehandler, ActorHandler actorhandler,
			Room room, int duration)
	{
		super(x, y, DepthConstants.FOREGROUND - 30, CollisionType.BOX, drawer, 
				collidablehandler, null, actorhandler, room, 
				"smoke", "cloudmask", false, false, false, Element.NOELEMENT, 
				Element.NOELEMENT, duration, false);
		setBoxCollisionPrecision(2, 2);
		addFadeEffect((int) (duration * 0.2), (int) (duration * 0.6));
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
