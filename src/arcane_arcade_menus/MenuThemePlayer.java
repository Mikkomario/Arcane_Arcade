package arcane_arcade_menus;

import resourcebanks.MultiMediaHolder;
import sound.SoundTrack;
import worlds.Room;
import gameobjects.GameObject;

/**
 * MenuThemePlayer plays the music theme in the menus or changes the phase 
 * of the theme.
 * 
 * @author Mikko Hilpinen. 
 * Created 14.2.2014
 */
public class MenuThemePlayer extends GameObject
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new MenuThemePlayer that either starts to play to menu music 
	 * or simply changes its phase. The player dies soon after it has 
	 * started the music
	 * 
	 * @param room The room where the player is located at
	 * @param phase The phase the music should go to next
	 */
	public MenuThemePlayer(Room room, int phase)
	{
		// Adds the object to the room
		room.addObject(this);
		
		// If the music is not yet playing, starts it
		SoundTrack theme = MultiMediaHolder.getMidiTrackBank("menu").getTrack("maintheme");
		
		if (!theme.isPlaying())
			theme.play(null);
		
		// Sets the correct phase
		theme.setJumpToIndex(phase);
			
		kill();
	}
}
