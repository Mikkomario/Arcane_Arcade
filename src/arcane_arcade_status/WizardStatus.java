package arcane_arcade_status;

/**
 * WizardStatus represents a status effect a wizard can have
 *
 * @author Mikko Hilpinen.
 *         Created 30.8.2013.
 */
public enum WizardStatus
{
	/**
	 * While panicked, the wizard's controls don't function normally
	 */
	PANIC, 
	/**
	 * While phasing, the wizard is immune to damage
	 */
	PHASING, 
	/**
	 * While slippery, wizard's friction is reduced considerably
	 */
	SLIPPERY, 
	/**
	 * Ironflesh makes wizard immune to ball collisions (the ball(s) will 
	 * bounce off from the wizard)
	 */
	IRONFLESH, 
	/**
	 * Haste makes wizard move faster
	 */
	HASTE, 
	/**
	 * Paralyze makes wizard unable to move without teleporting
	 */
	PARALYZED, 
	/**
	 * Amnesia makes the element icons disappear
	 */
	AMNESIA, 
	/**
	 * Rebirth makes wizard respawn if they die
	 */
	REBIRTH;
}
