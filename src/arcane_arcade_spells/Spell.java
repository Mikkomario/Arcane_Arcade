package arcane_arcade_spells;

import utopia_worlds.Area;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.Wizard;
import arcane_arcade_main.GameSettings;
import arcane_arcade_status.BallStatus;
import arcane_arcade_status.Element;

/**
 * Spells can be cast by giving them certain information.
 *
 * @author Mikko Hilpinen.
 * @since 28.8.2013.
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
	protected static final int CASTDELAY_SHORT = 45;
	/**
	 * A medium cast delay used in most spells
	 */
	protected static final int CASTDELAY_NORMAL = 75;
	/**
	 * A long cast delay used in heavy spells
	 */
	protected static final int CASTDELAY_LONG = 100;
	/**
	 * A very long cast delay used in very powerful spells
	 */
	protected static final int CASTDELAY_VERY_LONG = 145;
	/**
	 * A super long cast delay used only in the most powerful and 
	 * rare spells
	 */
	protected static final int CASTDELAY_EPIC = 200;
	
	/**
	 * A very low mana cost used by spells that have ligth effects or are 
	 * used in quick succession
	 */
	protected static final int MPUSE_VERY_LOW = 12;
	/**
	 * A low mana cost used by spells that have light effects and / or are 
	 * medium curses
	 */
	protected static final int MPUSE_LOW = 16;
	/**
	 * A medium mana cost used by most spells
	 */
	protected static final int MPUSE_MEDIUM = 22;
	/**
	 * A bit higher than normal mana cost used by rather effective spells or 
	 * blessings
	 */
	protected static final int MPUSE_SEMI_HIGH = 26;
	/**
	 * A high mana cost used by very effective spells and / or blessings
	 */
	protected static final int MPUSE_HIGH = 33;
	/**
	 * A very high mana cost used only by very strong blessings or very lenghty 
	 * and effective spells
	 */
	protected static final int MPUSE_VERY_HIGH = 44;
	/**
	 * An extremely high mana cost used only by the most incredible spells 
	 * like the rebirth
	 */
	protected static final int MPUSE_EPIC = 55;
	
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
		
		// Manausage is lowered according to the length of the spell (affects 
		// regeneration)
		this.manausage = (int) (manausage - castdelay * 
				GameSettings.DEFAULTMANAREGENERATIONRATE * 0.5);
	}
	
	
	// ABSTRACT METHODS	-------------------------------------------------
	
	/**
	 * Creates the effects of a spell (like flying fireballs) using the 
	 * given information
	 *
	 * @param caster The wizard who casts the spell
	 * @param ballrelay The ballrelay that provides information about the ball(s) 
	 * for the spelleffects
	 * @param area the area where the objects will be placed to
	 */
	protected abstract void createEffects(Wizard caster, BallRelay ballrelay, 
			Area area);
	
	/**
	 * @return The name of the spell
	 */
	public abstract String getName();
	
	/**
	 * @return A short description of what the spell does
	 */
	protected abstract String getSimpleDescription();
	
	/**
	 * @return The first element used in the spell's effect(s). 
	 * Use NOELEMENT if the spell doesn't use elements
	 */
	public abstract Element getFirstEffectElement();
	
	/**
	 * @return The second element used in the spell's effect(s). 
	 * Use NOELEMENT if the spell doesn't use elements or has only single element
	 */
	public abstract Element getSecondEffectElement();
	
	/**
	 * @return a status effect that is caused when the spell affects the ball. 
	 * Return NOSTATUS if no status effect is caused
	 */
	public abstract BallStatus getCausedStatus();
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return How much mana the spell consumes
	 */
	public int getManaUsage()
	{
		return this.manausage;
	}
	
	/**
	 * @return How long it takes for the wizard to recover from casting the 
	 * spell
	 */
	public int getCastDelay()
	{
		return this.castdelay;
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Excecutes the spell, which only works if the wizard isn't already 
	 * casting a different spell
	 *
	 * @param caster The wizard who casts the spell
	 * @param ballrelay The ballrelay that provide information about the ball 
	 * for the spelleffects
	 * @param area the area where the objects will be placed to
	 */
	public void execute(Wizard caster, BallRelay ballrelay, Area area)
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
		createEffects(caster, ballrelay, area);
	}
	
	/**
	 * @return A description explaining the use and statistics of the spell
	 */
	public String getDescription()
	{
		// TODO: Change the visibility of the abstract method(s) when you can
		return getSimpleDescription();// + " " + getDelayDescription() + 
				//getColourUsageDescription();
	}
	/*
	private String getDelayDescription()
	{
		if (this.castdelay <= CASTDELAY_SHORT)
			return " The spell is very easy to cast";
		if (this.castdelay <= CASTDELAY_LONG)
			return "The spell is moderately easy to cast";
		if (this.castdelay < CASTDELAY_VERY_LONG)
			return "The spell is rather difficult to cast";
		
		return "The spell is very difficult to cast";
	}
	
	private String getColourUsageDescription()
	{
		if (this.manausage <= MPUSE_LOW)
			return " and uses only little colour.";
		if (this.manausage <= MPUSE_SEMI_HIGH)
			return " and uses a moderate amount of colour.";
		if (this.manausage <= MPUSE_VERY_HIGH)
			return " and uses a lot colour.";
		
		return " and uses insane amounts of colour.";
	}
	*/
}
