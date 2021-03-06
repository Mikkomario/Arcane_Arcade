package arcane_arcade_field;

import utopia_gameobjects.GameObject;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_sound.MidiMusic;
import utopia_worlds.Area;
import utopia_worlds.Room;


/**
 * FieldMusicPlayer plays the music in the basic field. The music starts 
 * at when the object is created and ends when the object dies. The object 
 * automatically dies at the end of the room.
 * 
 * @author Mikko Hilpinen. 
 * Created 14.2.2014
 */
public class FieldMusicPlayer extends GameObject implements RoomListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private MidiMusic music;
	
	
	// CONSTRUCTOR	------------------------------------------------------

	/**
	 * Creates a fieldMusicPlayer that starts to play the music
	 * 
	 * @param area The area where the object is placed to
	 */
	public FieldMusicPlayer(Area area)
	{
		super(area);
		
		// Initializes attributes
		this.music = MultiMediaHolder.getMidiBank("field").getSound("battle");
		
		// Starts to play the field music
		this.music.loop(null);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onRoomStart(Room room)
	{
		// Does nothing
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Dies
		kill();
	}
	
	@Override
	public void kill()
	{
		// Also stops the music
		this.music.stop();
		
		super.kill();
	}

}
