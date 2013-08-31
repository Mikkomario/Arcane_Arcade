package arcane_arcade_field;

/**
 * Many spells, the Wizard class and some hud elements function differently 
 * depending on from which side of the screen they are created from.
 *
 * @author Mikko Hilpinen.
 *         Created 31.8.2013.
 */
public enum ScreenSide
{
	/**
	 * The left side of the field
	 */
	LEFT, 
	/**
	 * The right side of the field
	 */
	RIGHT;
	
	
	// METHODS	--------------------------------------------------------
	
	/**
	 * @return The side opposing to the side called
	 */
	public ScreenSide getOppositeSide()
	{
		switch (this)
		{
			case LEFT: return RIGHT;
			case RIGHT: return LEFT;
			default: return LEFT;
		}
	}
}
