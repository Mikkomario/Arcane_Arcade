package arcane_arcade_main;

/**
 * Gamesettings provides information about the current settings of the game.
 *
 * @author Mikko Hilpinen.
 *         Created 26.8.2013.
 */
public class GameSettings
{
	// ATTRIBUTES	-----------------------------------------------------
	
	/**
	 * Screenwidth tells the width of the game's window
	 */
	public final static int SCREENWIDTH = 1000;
	/**
	 * Screenheight tells the height of the game's window
	 */
	public final static int SCREENHEIGHT = 576;
	/**
	 * Should the fullscreen be set on at the beginning of the game
	 */
	public final static boolean FULLSCREENON = false;
	/**
	 * The name of the file where the sprites are loaded from
	 */
	public final static String SPRITEDATALOCATION = "configure/spriteload.txt";
	/**
	 * How fast the wizards should normally regenerate mana
	 */
	public final static double DEFAULTMANAREGENERATIONRATE = 0.14;
}
