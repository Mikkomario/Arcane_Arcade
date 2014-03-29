package arcane_arcade_menus;

import utopia_gameobjects.GameObject;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_sound.SoundTrack;
import utopia_worlds.Area;


/**
 * MenuThemePlayer plays the music theme in the menus or changes the phase 
 * of the theme.
 * 
 * @author Mikko Hilpinen. 
 * @since 14.2.2014
 */
public class MenuThemePlayer extends GameObject
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new MenuThemePlayer that either starts to play to menu music 
	 * or simply changes its phase. The player dies soon after it has 
	 * started the music
	 * 
	 * @param area The area where the object is placed to
	 * @param phase The phase the music should go to next
	 */
	public MenuThemePlayer(Area area, int phase)
	{
		super(area);
		
		// If the music is not yet playing, starts it
		SoundTrack theme = MultiMediaHolder.getMidiTrackBank("menu").getTrack("maintheme");
		
		if (!theme.isPlaying())
			theme.play(null);
		
		// Sets the correct phase
		theme.setJumpToIndex(phase);
			
		kill();
	}
}
