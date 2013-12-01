package arcane_arcade_menus;

import common.GameObject;

import worlds.Room;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.FieldSetting;
import arcane_arcade_worlds.RoomObjectCreator;

/**
 * ElementScreen Object creator creates the necessary interface elements to 
 * the element screen in which the users will pick the elements they use in 
 * the following battle.
 *
 * @author Mikko Hilpinen.
 *         Created 30.11.2013.
 */
public class ElementScreenObjectCreator extends GameObject implements 
		RoomObjectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private FieldSetting settings;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new ElementScreenObjectCreator
	 */
	public ElementScreenObjectCreator()
	{
		// Initializes attributes
		this.settings = null;
	}
		
	@Override
	public void onRoomStart(Room room)
	{
		// TODO: Add object creation
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Does nothing
	}

	@Override
	public void setSettings(AreaSetting setting)
	{
		// Only works with fieldsettings
		if (setting instanceof FieldSetting)
			this.settings = (FieldSetting) setting;
		else
			System.err.println("ElementScreenObjectCreator only works with " +
					"FieldSetting type settings");
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The settings used and modified in this room
	 */
	public FieldSetting getSettings()
	{
		return this.settings;
	}
}
