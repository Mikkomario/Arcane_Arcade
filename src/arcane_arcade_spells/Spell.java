package arcane_arcade_spells;

import worlds.Room;
import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.Wizard;

/**
 * Spells can be cast by giving them certain information.
 *
 * @author Mikko Hilpinen.
 *         Created 28.8.2013.
 */
public abstract class Spell
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private int castdelay;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new spell with the given stats
	 *
	 * @param castdelay How many steps it will take after casting the spell 
	 * before the caster can cast another spell
	 */
	public Spell(int castdelay)
	{
		// Initializes attributes
		this.castdelay = castdelay;
	}
	
	
	// ABSTRACT METHODS	-------------------------------------------------
	
	/**
	 * Creates the effects of a spell (like flying fireballs) using the 
	 * given information
	 *
	 * @param caster The wizard who casts the spell
	 * @param ballrelay The ballrelay that provide information about the ball 
	 * for the spelleffects
	 * @param drawer The drawer that will draw the spelleffects
	 * @param actorhandler The actorhandler that will call the spelleffects' 
	 * act event
	 * @param collidablehandler The collidablehandler that will handle the 
	 * spelleffects' collision checking 
	 * @param collisionhandler The collisionhandler that will inform the 
	 * spelleffects' about collisions
	 * @param room The room to which the spelleffects will be created
	 */
	protected abstract void createEffects(Wizard caster, BallRelay ballrelay, 
			DrawableHandler drawer, ActorHandler actorhandler, 
			CollidableHandler collidablehandler, 
			CollisionHandler collisionhandler, Room room);
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Excecutes the spell, which only works if the wizard isn't already 
	 * casting a different spell
	 *
	 * @param caster The wizard who casts the spell
	 * @param ballrelay The ballrelay that provide information about the ball 
	 * for the spelleffects
	 * @param drawer The drawer that will draw the spelleffects
	 * @param actorhandler The actorhandler that will call the spelleffects' 
	 * act event
	 * @param collidablehandler The collidablehandler that will handle the 
	 * spelleffects' collision checking 
	 * @param collisionhandler The collisionhandler that will inform the 
	 * spelleffects' about collisions
	 * @param room The room to which the spelleffects will be created
	 */
	public void execute(Wizard caster, BallRelay ballrelay, 
			DrawableHandler drawer, ActorHandler actorhandler, 
			CollidableHandler collidablehandler, 
			CollisionHandler collisionhandler, Room room)
	{
		// Checks cast delay
		if (caster.isCasting())
			return;
		
		// Sets the cast delay
		caster.setCastDelay(this.castdelay);
		
		// Creates an effect
		createEffects(caster, ballrelay, drawer, actorhandler, collidablehandler, collisionhandler, room);
	}
}
