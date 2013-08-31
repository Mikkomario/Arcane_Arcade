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
	
	/**
	 * A very short castdelay usually used in repeatable / ongoing spells
	 */
	protected static final int CASTDELAY_VERY_SHORT = 25;
	/**
	 * A short castdelay used in light spells
	 */
	protected static final int CASTDELAY_SHORT = 33;
	/**
	 * A medium cast delay used in most spells
	 */
	protected static final int CASTDELAY_NORMAL = 45;
	/**
	 * A long cast delay used in heavy spells
	 */
	protected static final int CASTDELAY_LONG = 80;
	/**
	 * A very long cast delay used in very powerful spells
	 */
	protected static final int CASTDELAY_VERY_LONG = 120;
	/**
	 * A super long cast delay used only in the most powerful and 
	 * rare spells
	 */
	protected static final int CASTDELAY_EPIC = 170;
	
	/**
	 * A very low mana cost used by spells that have ligth effects or are 
	 * used in quick succession
	 */
	protected static final int MPUSE_VERY_LOW = 13;
	/**
	 * A low mana cost used by spells that have light effects and / or are 
	 * medium curses
	 */
	protected static final int MPUSE_LOW = 18;
	/**
	 * A medium mana cost used by most spells
	 */
	protected static final int MPUSE_MEDIUM = 24;
	/**
	 * A bit higher than normal mana cost used by rather effective spells or 
	 * blessings
	 */
	protected static final int MPUSE_SEMI_HIGH = 28;
	/**
	 * A high mana cost used by very effective spells and / or blessings
	 */
	protected static final int MPUSE_HIGH = 35;
	/**
	 * A very high mana cost used only by very strong blessings or very lenghty 
	 * and effective spells
	 */
	protected static final int MPUSE_VERY_HIGH = 42;
	/**
	 * An extremely high mana cost used only by the most incredible spells 
	 * like the rebirth
	 */
	protected static final int MPUSE_EPIC = 75;
	
	private int castdelay;
	private int manausage;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new spell with the given stats
	 *
	 * @param castdelay How many steps it will take after casting the spell 
	 * before the caster can cast another spell
	 * @param manausage How much the spell uses mana
	 */
	public Spell(int castdelay, int manausage)
	{
		// Initializes attributes
		this.castdelay = castdelay;
		this.manausage = manausage;
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
		// Checks if the wizard has enough mana
		if (!caster.hasEnoughMana(this.manausage))
			return;
		
		// Sets the cast delay
		caster.setCastDelay(this.castdelay);
		// Uses mana
		caster.adjustMana(-this.manausage);
		
		// Creates an effect
		createEffects(caster, ballrelay, drawer, actorhandler, collidablehandler, collisionhandler, room);
	}
}
