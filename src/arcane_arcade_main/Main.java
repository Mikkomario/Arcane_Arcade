package arcane_arcade_main;

import java.awt.BorderLayout;

import utopia_resourceHandling.ResourceType;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_video.GamePanel;
import utopia_video.GameWindow;
import arcane_arcade_worlds.Navigator;

/**
 * Main class starts the game
 *
 * @author Mikko Hilpinen & Unto Solala.
 * @since 26.8.2013.
 */
public class Main
{	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Starts the game
	 */
	public Main()
	{
		// Reads the options from a file
		Options.loadSettings();
		
		// Initializes the resource banks
		MultiMediaHolder.initializeResourceDatabase(ResourceType.MIDI, 
				GameSettings.MIDIDATALOCATION);
		MultiMediaHolder.initializeResourceDatabase(ResourceType.SPRITE, 
				GameSettings.SPRITEDATALOCATION);
		MultiMediaHolder.initializeResourceDatabase(ResourceType.WAV, 
				GameSettings.WAVDATALOCATION);
		MultiMediaHolder.initializeResourceDatabase(ResourceType.MIDISOUNDTRACK, 
				GameSettings.MIDITRACKDATALOCATION);
		MultiMediaHolder.initializeResourceDatabase(ResourceType.GAMEPHASE, 
				"configure/gamephaseload.txt");
		
		// Initializes attributes
		GamePanel mainpanel = new GamePanel(GameSettings.SCREENWIDTH, 
				GameSettings.SCREENHEIGHT);
		
		boolean fullscreen = Options.fullscreenon;
		
		GameWindow window = new GameWindow(GameSettings.SCREENWIDTH, 
				GameSettings.SCREENHEIGHT, "Arcane Arcade", !fullscreen, 
				120, 10, false);
		// Sets up the window
		window.addGamePanel(mainpanel, BorderLayout.CENTER);
		// Sets the window to the fullscreen if needed
		if (fullscreen)
			window.setFullScreen(true);
		
		// Starts the game by starting the main menu
		new Navigator(mainpanel.getDrawer(), window.getStepHandler(), 
				window.getKeyListenerHandler(), 
				window.getMouseListenerHandler()).startPhase("mainmenu", null);		
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
