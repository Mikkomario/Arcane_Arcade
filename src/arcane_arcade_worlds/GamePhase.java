package arcane_arcade_worlds;

import resourcebanks.ResourceType;

/**
 * Gamephases represent the phases in a game like the menu screen or the 
 * actual game field
 *
 * @author Mikko Hilpinen & Unto Solala
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
	MAINMENU, 
	/**
	 * In tutorial menu the user chooses which tutorial they would like to 
	 * go through
	 */
	TUTORIALMENU, 
	/**
	 * In options menu the user can change the game options.
	 */
	OPTIONSMENU,
	/**
	 * In spellbook menu the user can view the various spells the game offers.
	 */
	SPELLBOOKMENU,
	/**
	 * In battlesettingmenu the user chooses how they want to change the 
	 * circumstances of the next battle
	 */
	BATTLESETTINGMENU, 
	/**
	 * In the element menu the user chooses which elements to use in the 
	 * following battle
	 */
	ELEMENTMENU,
	/**
	 * In the victory screen the stats of the former battle are shown
	 */
	VICTORYSCREEN;
	
	
	// METHODS	---------------------------------------------------------
	
	/**
	 * Returns the names of the resourceBanks used during the phase.
	 * 
	 * @param banktype What kind of resource bank names should be listed
	 * @return A list containing all the names of the banks of the given 
	 * resourceType that are used during the phase
	 */
	public String[] getUsedBankNames(ResourceType banktype)
	{
		switch (banktype)
		{
			case SPRITE:
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
						String[] returned = {"menu"};
						return returned;
					}
					case TUTORIALMENU:
					{
						String[] returned = {"menu"};
						return returned;
					}
					case OPTIONSMENU:
					{
						String[] returned = {"menu"};
						return returned;
					}
					case SPELLBOOKMENU:
					{
						String[] returned = {"menu"};
						return returned;
					}
					case BATTLESETTINGMENU:
					{
						String[] returned = {"menu"};
						return returned;
					}
					case ELEMENTMENU:
					{
						String[] returned = {"menu", "hud"};
						return returned;
					}
					case VICTORYSCREEN:
					{
						String[] returned = {"menu"};
						return returned;
					}
					default: return new String[0];
				}
			}
			case WAV:
			{
				switch (this)
				{
					case FIELD:
					{
						String[] returned = {"effects"};
						return returned;
					}
					default: return new String[0];
				}
			}
			case MIDI:
			{
				switch (this)
				{
					case FIELD:
					{
						String[] returned = {"field"};
						return returned;
					}
					default:
					{
						String[] returned = {"menu"};
						return returned;
					}
				}
			}
			default: return new String[0];
		}
	}
	
	@Override
	public String toString()
	{
		switch (this)
		{
			case FIELD: return "game";
			case MAINMENU: return "main menu";
			case TUTORIALMENU: return "tutorials";
			case OPTIONSMENU: return "options";
			case SPELLBOOKMENU: return "spellbook";
			case BATTLESETTINGMENU: return "battle settings";
			case ELEMENTMENU: return "element selection";
			case VICTORYSCREEN: return "victory screen";
			
			default: return "???";
		}
	}
}
