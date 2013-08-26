package arcane_arcade_main;

import java.awt.BorderLayout;

import video.GamePanel;
import video.GameWindow;

/**
 * Main class starts the game
 *
 * @author Mikko Hilpinen.
 *         Created 26.8.2013.
 */
public class Main
{
	// ATTRIBUTES	----------------------------------------------------
	
	private GameWindow window;
	private GamePanel mainpanel;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Starts the game
	 */
	public Main()
	{
		// Initializes attributes
		this.mainpanel = new GamePanel(GameSettings.screenwidth, 
				GameSettings.screenheight);
		this.window = new GameWindow(GameSettings.screenwidth, 
				GameSettings.screenheight, "Arcane Arcade");
		// Sets up the window
		this.window.addGamePanel(this.mainpanel, BorderLayout.CENTER);
	}
	
	
	// MAIN METHOD	----------------------------------------------------
	
	/**
	 * Starts the game
	 *
	 * @param args No arguments needed
	 */
	public static void main(String[] args)
	{
		// Starts the game
		new Main();
	}
}
