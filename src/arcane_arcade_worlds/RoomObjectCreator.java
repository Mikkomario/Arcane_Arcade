package arcane_arcade_worlds;

import utopia_listeners.RoomListener;


/**
 * RoomObjectCreator creates a bunch of objects at the start of the room 
 * using different settings as help.
 *
 * @author Mikko Hilpinen.
 *         Created 3.9.2013.
 */
public interface RoomObjectCreator extends RoomListener
{
	/**
	 * Changes the settings the creator uses when it creates the objects in 
	 * the room
	 *
	 * @param setting The setting object that tells information about how to 
	 * create the objects
	 */
	public void setSettings(AreaSetting setting);
}
