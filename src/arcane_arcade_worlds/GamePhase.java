package arcane_arcade_worlds;

/**
 * Gamephases represent the phases in a game like the menu screen or the 
 * actual game field
 *
 * @author Mikko Hilpinen.
 *         Created 1.9.2013.
 */
public enum GamePhase
{
	/**
	 * In field, tow mages play pong against each other. It's the main part 
	 * of the game.
	 */
	FIELD,
	/**
	 * In the main menu, the user can access different phases of the game
	 */
	MAINMENU;
	
	
	// METHODS	---------------------------------------------------------
	
	/**
	 * @return The names of the spritebanks that should be initialized 
	 * during this gamephase
	 */
	public String[] getUsedSpriteBanks()
	{
		switch (this)
		{
			case FIELD:
			{
				String[] returned = {"background", "field", "hud", "status", 
						"creatures", "spells"};
				return returned;
			}
			case MAINMENU:
			{
				String[] returned = {"menu", "background"};
				return returned;
			}
			default: return new String[0];
		}
	}
}
