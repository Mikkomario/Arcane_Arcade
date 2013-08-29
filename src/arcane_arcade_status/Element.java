package arcane_arcade_status;

import java.util.ArrayList;

import arcane_arcade_spells.ExplosionSpell;
import arcane_arcade_spells.SmokeScreenSpell;
import arcane_arcade_spells.Spell;
import arcane_arcade_spells.TidalWaveSpell;

/**
 * There are multiple elements in the game. Some elements are effective against 
 * some statusses and some aren't. Each element also has an icon. Elements can 
 * also be combined to form a spell.
 *
 * @author Mikko Hilpinen.
 *         Created 28.8.2013.
 */
public enum Element
{
	// Elements
	@SuppressWarnings("javadoc")
	FIRE, 
	@SuppressWarnings("javadoc")
	WATER, 
	@SuppressWarnings("javadoc")
	ICE, 
	@SuppressWarnings("javadoc")
	EARTH, 
	@SuppressWarnings("javadoc")
	WIND, 
	@SuppressWarnings("javadoc")
	LIGHTNING, 
	@SuppressWarnings("javadoc")
	DARK, 
	@SuppressWarnings("javadoc")
	LIGHT,
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
		spells.add(new ExplosionSpell());	// Fire Ice
		spells.add(new ExplosionSpell());	// Fire Earth
		spells.add(new ExplosionSpell());	// Fire Wind
		spells.add(new ExplosionSpell());	// Fire Lightning
		spells.add(new ExplosionSpell());	// Fire Dark
		spells.add(new ExplosionSpell());	// Fire Light
		spells.add(new TidalWaveSpell());	// Water Water
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
			case FIRE: return 0;
			case WATER: return 1;
			case ICE: return 2;
			case EARTH: return 3;
			case WIND: return 4;
			case LIGHTNING: return 5;
			case DARK: return 6;
			case LIGHT: return 7;
			
			default: return 0;
		}
	}
	
	/**
	 * Some elements are weak or strong against certain status effects. This 
	 * method returns a modifier that tells, how much the force should be 
	 * affected by the status.
	 *
	 * @param status The statuseffect the ball currently has
	 * @param strength How strong the status effect on the ball is [0, 100]
	 * @return A modifier about how much the force should be affected (1 means 
	 * that the status doesn't affect the force, 2 means that the force will 
	 * increase 100% and 0.5 means that the force will be halved and so on)
	 */
	public double getForceModifier(BallStatus status, double strength)
	{
		switch(this)
		{
			// Fire is weak against water, ice and muddy
			case FIRE:
			{
				if (status == BallStatus.WET || status == BallStatus.FROZEN 
						|| status == BallStatus.MUDDY)
					return getWeakModifier(strength);
				else
					return 1;
			}
			// Water is weak against fire
			case WATER:
			{
				if (status == BallStatus.FLAMING)
					return getWeakModifier(strength);
				else
					return 1;
			}
			// Ice is weak against fire, but strong against water
			case ICE:
			{
				if (status == BallStatus.FLAMING)
					return getWeakModifier(strength);
				else if (status == BallStatus.WET)
					return getStrongModifier(strength);
				else
					return 1;
			}
			// Earth is weak against water but strong against ice
			case EARTH:
			{
				if (status == BallStatus.WET)
					return getWeakModifier(strength);
				else if (status == BallStatus.FROZEN)
					return getStrongModifier(strength);
				else
					return 1;
			}
			// Wind is strong against earth but weak against fire
			case WIND:
			{
				if (status ==  BallStatus.FLAMING)
					return getWeakModifier(strength);
				else if (status == BallStatus.MUDDY)
					return getStrongModifier(strength);
				else
					return 1;
			}
			// Lightning is strong against lightning and water but weak 
			// against earth
			case LIGHTNING:
			{
				if (status == BallStatus.MUDDY)
					return getWeakModifier(strength);
				else if (status == BallStatus.WET || status == BallStatus.CHARGED)
					return getStrongModifier(strength);
				else
					return 1;
			}
			default: return 1;
		}
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
		int index1 = Math.min(getElementIconIndex(), 
				element2.getElementIconIndex());
		int index2 = Math.max(getElementIconIndex(), 
				element2.getElementIconIndex());
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
	
	private double getWeakModifier(double strength)
	{
		return 1 - 0.9 * strength / 100;
	}
	
	private double getStrongModifier(double strength)
	{
		return 1 + strength / 100;
	}
}
