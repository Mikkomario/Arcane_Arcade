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
	 * The name of the file where the sprites are loaded from
	 */
	public final static String SPRITEDATALOCATION = "configure/spriteload.txt";
	/**
	 * The name of the file where the wav sound effects are loaded from
	 */
	public final static String WAVDATALOCATION = "configure/wavload.txt";
	/**
	 * The name of the file where the midi musics are loaded from
	 */
	public final static String MIDIDATALOCATION = "configure/midiload.txt";
	/**
	 * The name of the file where the midi soundtracks are loaded from
	 */
	public final static String MIDITRACKDATALOCATION = "configure/miditrackload.txt";
	/**
	 * The name of the file where the wizard voice wavs are loaded from
	 */
	public final static String WIZARDVOICEDATALOCATION = "configure/wizardvoiceload.txt";
	/**
	 * The name of the file where the sprites are loaded from
	 */
	public final static String OPTIONSDATALOCATION = "configure/usersettings.txt";
	/**
	 * How fast the wizards should normally regenerate mana
	 */
	public final static double DEFAULTMANAREGENERATIONRATE = 0.14;
	/**
	 * The basic font used in the game
	 */
	public final static Font BASICFONT = new Font("Old English Text MT", 
			Font.BOLD, 30);
	/**
	 * The basic white colour used in the basic texts of the game
	 */
	public final static Color WHITETEXTCOLOR = new Color(235, 232, 168);
}
