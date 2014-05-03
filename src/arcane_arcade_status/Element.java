package arcane_arcade_status;

import java.util.ArrayList;

import arcane_arcade_field.Ball;
import arcane_arcade_spells.ExplosionSpell;
import arcane_arcade_spells.FreezeSpell;
import arcane_arcade_spells.FrostBarrierSpell;
import arcane_arcade_spells.IceMineSpell;
import arcane_arcade_spells.SmokeScreenSpell;
import arcane_arcade_spells.Spell;
import arcane_arcade_spells.TidalWaveSpell;

/**
 * There are multiple elements in the game. Some elements are effective against 
 * some statusses and some aren't. Each element also has an icon. Elements can 
 * also be combined to form a spell.
 *
 * @author Mikko Hilpinen.
 * @since 28.8.2013.
 */
public enum Element
{
	// Elements
	@SuppressWarnings("javadoc")
	BLAZE, 
	@SuppressWarnings("javadoc")
	TIDE, 
	@SuppressWarnings("javadoc")
	FROST, 
	@SuppressWarnings("javadoc")
	EARTH, 
	@SuppressWarnings("javadoc")
	GALE, 
	@SuppressWarnings("javadoc")
	VOLT, 
	@SuppressWarnings("javadoc")
	SOMBER, 
	@SuppressWarnings("javadoc")
	LIGHT,
	@SuppressWarnings("javadoc")
	LUSH, 
	@SuppressWarnings("javadoc")
	TOXIC, 
	@SuppressWarnings("javadoc")
	NOELEMENT;
	
	
	// ATTRIBUTES	-----------------------------------------------------
	
	private static ArrayList<Spell> spells = null;
	private static boolean initialized = false;

	
	// STATIC METHODS	--------------------------------------------------
	
	private static void initializeSpellList()
	{
		// If the spells are already initialized, does nothing
		if (initialized)
			return;
		
		initialized = true;
		spells = new ArrayList<Spell>();
		// Adds the spells to the list
		// NOTICE: Spells need to be in a specific order: 
		// FIRE (fire + fire, fire + water, ...), 
		// WATER (water + water, water + ice, ...), ICE, EARTH, WIND, 
		// LIGHTNING, DARK and LIGHT
		spells.add(new ExplosionSpell());	// Fire Fire
		spells.add(new SmokeScreenSpell());	// Fire Water
		spells.add(new IceMineSpell());		// Fire Ice
		spells.add(new ExplosionSpell());	// Fire Earth
		spells.add(new ExplosionSpell());	// Fire Wind
		spells.add(new ExplosionSpell());	// Fire Lightning
		spells.add(new ExplosionSpell());	// Fire Dark
		spells.add(new ExplosionSpell());	// Fire Light
		spells.add(new ExplosionSpell());	// Fire Lush
		spells.add(new ExplosionSpell());	// Fire Toxic
		
		spells.add(new TidalWaveSpell());	// Water Water
		spells.add(new FrostBarrierSpell());// Water frost
		spells.add(new TidalWaveSpell());	// Tide Earth
		spells.add(new TidalWaveSpell());	// Tide Gale
		spells.add(new TidalWaveSpell());	// Tide Volt
		spells.add(new TidalWaveSpell());	// Tide Somber
		spells.add(new TidalWaveSpell());	// Tide Light
		spells.add(new TidalWaveSpell());	// Tide Lush
		spells.add(new TidalWaveSpell());	// Tide Toxic
		
		spells.add(new FreezeSpell());		// Frost Frost
		
		// TODO: Add spells
	}
	
	/**
	 * Uninitializes the spell list, releasing the memory
	 */
	public static void uninitializeSpellList()
	{
		// If the list is already uninitialized, does nothing
		if (!initialized)
			return;
		
		initialized = false;
		spells.clear();
		spells = null;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * @return The imageindex in the element indicator that represents the 
	 * element
	 */
	public int getElementIconIndex()
	{
		switch(this)
		{
			case BLAZE: return 1;
			case TIDE: return 2;
			case FROST: return 3;
			case EARTH: return 4;
			case GALE: return 5;
			case VOLT: return 6;
			case SOMBER: return 7;
			case LIGHT: return 8;
			
			// TODO: Add sprite index 9 and 10 once an animation allows it
			
			default: return 0;
		}
	}
	
	private BallStatus[] getCounteringStatuses()
	{
		switch (this)
		{
			case BLAZE: 
				BallStatus[] blazeWeak = {BallStatus.WET, BallStatus.FROZEN, BallStatus.PETRIFIED}; 
				return blazeWeak;
			case TIDE: 
				BallStatus[] tideWeak = {BallStatus.FLAMING, BallStatus.PETRIFIED, BallStatus.GROWTH}; 
				return tideWeak;
			case FROST: 
				BallStatus[] frostWeak = {BallStatus.WET, BallStatus.FLAMING}; 
				return frostWeak;
			case EARTH: 
				BallStatus[] earthWeak = {BallStatus.FROZEN}; 
				return earthWeak;
			case GALE: 
				BallStatus[] galeWeak = {BallStatus.FLAMING, BallStatus.PETRIFIED}; 
				return galeWeak;
			case VOLT: 
				BallStatus[] voltWeak = {BallStatus.GROWTH, BallStatus.PETRIFIED}; 
				return voltWeak;
			case LUSH: 
				BallStatus[] lushWeak = {BallStatus.FLAMING, BallStatus.FROZEN, BallStatus.BLIGHT}; 
				return lushWeak;
			case TOXIC: 
				BallStatus[] toxicWeak = {BallStatus.WET}; 
				return toxicWeak;
				
			default: return new BallStatus[0];
		}
	}
	
	private BallStatus[] getBoostingStatuses()
	{
		switch (this)
		{
			case BLAZE: 
				BallStatus[] blaze = {BallStatus.GROWTH, BallStatus.BLIGHT}; 
				return blaze;
			case TIDE: 
				BallStatus[] tide = {BallStatus.FROZEN, BallStatus.BLIGHT}; 
				return tide;
			case FROST: 
				BallStatus[] frost = {BallStatus.PETRIFIED}; 
				return frost;
			case GALE: 
				BallStatus[] gale = {BallStatus.CHARGED, BallStatus.GROWTH}; 
				return gale;
			case VOLT: 
				BallStatus[] volt = {BallStatus.WET, BallStatus.CHARGED}; 
				return volt;
			case LUSH: 
				BallStatus[] lush = {BallStatus.PETRIFIED}; 
				return lush;
			case TOXIC: 
				BallStatus[] toxic = {BallStatus.FLAMING, BallStatus.GROWTH}; 
				return toxic;
				
			default: return new BallStatus[0];
		}
	}
	
	// A status that can only have a positive effect
	/**
	 * @return The status effect this element causes / may cause. 
	 */
	public BallStatus getCausedStatus()
	{
		switch (this)
		{
			case BLAZE: return BallStatus.FLAMING;
			case TIDE: return BallStatus.WET;
			case FROST: return BallStatus.FROZEN;
			case EARTH: return BallStatus.PETRIFIED;
			case VOLT: return BallStatus.CHARGED;
			case LUSH: return BallStatus.GROWTH;
			case TOXIC: return BallStatus.BLIGHT;
			
			default: return BallStatus.NOSTATUS;
		}
	}
	
	private static boolean tableContainsStatus(BallStatus[] table, BallStatus testedStatus)
	{
		for (BallStatus status : table)
		{
			if (status == testedStatus)
				return true;
		}
		
		return false;
	}
	
	/**
	 * Calculates an element combo's force modifier against the given ball's 
	 * current status
	 * 
	 * @param element1 An effect's first element
	 * @param element2 An effect's second element or NOELEMENT if the effect 
	 * only uses a single element
	 * @param ball The ball that will be affected by the effect
	 * @return How much the effect should be adjusted to take the ball's status 
	 * into account
	 */
	public static double getForceModifier(Element element1, Element element2, 
			Ball ball)
	{
		double modifier = 1;
		
		// Tests all active status effects the ball has and returns a summary 
		// of the final modifier
		for (BallStatus status : BallStatus.values())
		{
			if (status == BallStatus.NOSTATUS)
				continue;
			
			double strength = ball.getStatusStrength(status);
			
			if (strength > 0)
				modifier *= getForceModifier(element1, element2, status, strength);
		}
		
		return modifier;
	}
	
	/**
	 * Some elements are weak or strong against certain status effects. This 
	 * method returns a modifier that tells, how much the force should be 
	 * affected by the status.
	 * 
	 * @param element1 The first element of a spell effect
	 * @param element2 The second element of a spell effect or NOELEMENT if 
	 * the effect only has one element
	 * @param status The status effect that is tested
	 * @param strength How strong the tested status currently is
	 * @return What modifier should be added to the force against this 
	 * specific status
	 */
	public static double getForceModifier(Element element1, Element element2, 
			BallStatus status, double strength)
	{
		if (status == BallStatus.NOSTATUS)
			return 1;
		
		double modifier = 1;
		
		// Checks if the status affects the force positively
		if (tableContainsStatus(element1.getBoostingStatuses(), status))
			modifier *= getStrongModifier(strength);
		if (element2 != NOELEMENT && tableContainsStatus(element2.getBoostingStatuses(), status))
			modifier *= getStrongModifier(strength);
			
		// Checks if the status affects the force negatively (only if not ignored)
		if (status != element1.getCausedStatus() && status !=  element2.getCausedStatus())
		{	
			if (tableContainsStatus(element1.getCounteringStatuses(), status))
				modifier *= getWeakModifier(strength);
			if (element2 != NOELEMENT && tableContainsStatus(element2.getCounteringStatuses(), status))
				modifier *= getWeakModifier(strength);
		}
		
		return modifier;
	}
	
	/**
	 * Finds a spell that is created using the this element and another element
	 *
	 * @param element2 The other element of the spell
	 * @return The spell that is formed using this element and the given element 
	 * together
	 */
	public Spell getSpell(Element element2)
	{
		// Initializes the spell list if needed
		if (!initialized)
			initializeSpellList();
		
		// Adds a spell from the list
		// Uses the indexes in a certain order
		int index1 = Math.min(getElementIconIndex() - 1, 
				element2.getElementIconIndex() - 1);
		int index2 = Math.max(getElementIconIndex() - 1, 
				element2.getElementIconIndex() - 1);
		int spellindex = 0;
		
		// Finds the corrent index in the list by using element indexes
		for (int i = 0; i < index1; i ++)
		{
		    spellindex += Element.values().length - 1 - i;
		}
		spellindex += index2 - index1;
		
		// Returns the spell from the list
		if (spellindex < spells.size())
			return spells.get(spellindex);
		// Or a warning and null
		System.err.println("Not enough spells in the element's spell list! " +
				"Spells required " + (spellindex + 1));
		return null;
	}
	
	/**
	 * @return The name of the element sound in the banks
	 */
	public String getSoundName()
	{
		return toString().toLowerCase();
	}
	
	private static double getWeakModifier(double strength)
	{
		return 1 - 0.9 * strength / 100;
	}
	
	private static double getStrongModifier(double strength)
	{
		return 1 + strength / 50;
	}
}
