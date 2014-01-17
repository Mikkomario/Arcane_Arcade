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
 * Ice mines scatter through the field and explode when they collide with the 
 * ball.
 * 
 * @author Mikko Hilpinen. 
 * Created 17.1.2014
 */
public class IceMineEffect extends MaskedSpellEffect
{	
	// ATTRIBUTES	-----------------------------------------------------
	
	private DrawableHandler drawer;
	private ActorHandler actorhandler;
	private CollidableHandler collidablehandler;
	private Room room;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new ice mine to the given position with the given duration.
	 * 
	 * @param x The new x-coordinate of the mine
	 * @param y The new y-coordinate of the mine
	 * @param drawer The drawableHandler that will draw the mine
	 * @param collidablehandler The collidableHandler that will handle the 
	 * object's collision checking
	 * @param actorhandler The actorHandler that will inform the object about 
	 * steps
	 * @param room The room where the mine is located at
	 * @param lifetime The duration the mine stays on the field
	 */
	public IceMineEffect(int x, int y, DrawableHandler drawer, 
			CollidableHandler collidablehandler, ActorHandler actorhandler,
			Room room, int lifetime)
	{
		super(x, y, DepthConstants.NORMAL, CollisionType.BOX, drawer, 
				collidablehandler, null, actorhandler, room, "icemine", 
				"iceminemask", false, true, false, Element.BLAZE, Element.FROST, 
				lifetime, false);
		
		// Initializes attributes
		this.drawer = drawer;
		this.actorhandler = actorhandler;
		this.collidablehandler = collidablehandler;
		this.room = room;

		// Sets additional events
		addFadeEffect(10, lifetime - 10);
		setFriction(0.25);
	}
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------

	@Override
	public void onBallCollision(Ball ball, double x, double y)
	{
		// Creates a new ice explosion effect and then disappears
		new IceExplosionEffect((int) getX(), (int) getY(), this.drawer, 
				this.collidablehandler, this.actorhandler, this.room);
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
