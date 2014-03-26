package arcane_arcade_spelleffects;

import utopia_handlers.ActorHandler;
import utopia_handlers.CollidableHandler;
import utopia_handlers.DrawableHandler;
import utopia_helpAndEnums.CollisionType;
import utopia_helpAndEnums.DepthConstants;
import utopia_worlds.Room;
import arcane_arcade_field.Ball;
import arcane_arcade_field.Wizard;
import arcane_arcade_status.BallStatus;
import arcane_arcade_status.Element;

/**
 * Freeze spell is a burst with a short duration but high power
 * 
 * @author Mikko Hilpinen. 
 * Created 18.1.2014
 */
public class FreezeSpellEffect extends MaskedSpellEffect
{
	// ATTRIBUTES	------------------------------------------------------
	
	// Currently only collides with balls
	private static final Class<?>[] COLLIDEDCLASSES = new Class<?>[]{Ball.class};
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new freeze burst to the given location
	 * 
	 * @param x The x-coordinate of the burst
	 * @param y The y-coordinate of the burst
	 * @param drawer The DrawableHandler that will draw the effect
	 * @param collidablehandler The collidableHandler that will handle the 
	 * object's collision checking
	 * @param actorhandler The actorhandler that will inform the object about 
	 * steps
	 * @param room The room where the object is located at
	 */
	public FreezeSpellEffect(int x, int y, DrawableHandler drawer,
			CollidableHandler collidablehandler,
			ActorHandler actorhandler,
			Room room)
	{
		super(x, y, DepthConstants.NORMAL, CollisionType.CIRCLE, drawer, 
				collidablehandler, null, actorhandler, room, "freezespell", 
				"freezemask", false, true, false, Element.FROST, 
				Element.NOELEMENT, 100, true);
		
		// Adds additional effects
		setRadius(75);
		//addAnimationEffect();
		addFadeEffect(2, 95);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onBallCollision(Ball ball, double x, double y)
	{
		// Freeze burst causes impact
		ball.impact(12 * getForceModifier(ball), 15, 
				getDirectionTowardsObject(ball), BallStatus.FROZEN, 40);
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
