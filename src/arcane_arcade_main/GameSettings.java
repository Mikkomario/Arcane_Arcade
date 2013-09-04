package arcane_arcade_main;

import java.awt.Color;
import java.awt.Font;

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
	/**
	 * The basic font used in the game
	 */
	public final static Font BASICFONT = new Font("Old English Text MT", 
			Font.BOLD, 40);
	/**
	 * The basic white colour used in the basic texts of the game
	 */
	public final static Color WHITETEXTCOLOR = new Color(235, 232, 168);
}
