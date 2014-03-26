package arcane_arcade_worlds;


import java.util.ArrayList;

import utopia_backgrounds.Background;
import utopia_gameobjects.GameObject;
import utopia_worlds.Room;

/**
 * SettingUsingRoom allows the use of settings and objectcreators and can 
 * inform the objectcreator about new settings when necessary
 *
 * @author Mikko Hilpinen.
 *         Created 3.9.2013.
 */
public class SettingUsingRoom extends Room
{
	// ATTRIBUTES	------------------------------------------------------
	
	private RoomObjectCreator objectcreator;
	
	
	// CONSTRUCTOR	-------------------------------------------------------
	
	/**
	 * Creates a new room that uses the given objectcreator to create the 
	 * objects in the room
	 *
	 * @param objectcreator The objectcreator that will create the objects in 
	 * the room
	 * @param backgrounds The backgrounds the room uses
	 */
	public SettingUsingRoom(RoomObjectCreator objectcreator, 
			ArrayList<Background> backgrounds)
	{
		super(backgrounds);
		
		// Initializes attributes
		this.objectcreator = objectcreator;
		// Adds the creator to the room
		addRoomListener(this.objectcreator);
		if (objectcreator instanceof GameObject)
			addObject((GameObject) this.objectcreator);
	}
	
	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 * Changes the settings the objectcreator uses to create the objects
	 *
	 * @param setting The new settings used by the objectcreator
	 */
	public void setSettings(AreaSetting setting)
	{
		this.objectcreator.setSettings(setting);
	}
	
	/**
	 * Starts the room using the given settings
	 *
	 * @param setting The setings that affect how the objects in the room are 
	 * created
	 */
	public void start(AreaSetting setting)
	{
		setSettings(setting);
		start();
	}
}
