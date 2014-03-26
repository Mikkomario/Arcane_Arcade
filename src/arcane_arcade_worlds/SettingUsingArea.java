package arcane_arcade_worlds;

import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.KeyListenerHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_resourceHandling.GamePhase;
import utopia_worlds.Area;

/**
 * SettingUsingArea allows the use of settings and objectCreators and can 
 * inform the objectCreator about new settings when necessary
 *
 * @author Mikko Hilpinen.
 * @since 3.9.2013.
 */
public class SettingUsingArea extends Area
{
	// ATTRIBUTES	------------------------------------------------------
	
	private SettingUsingAreaObjectCreator objectcreator;
	
	
	// CONSTRUCTOR	-------------------------------------------------------
	
	/**
	 * Creates a new room that uses the given handlers and gamePhase. The 
	 * objectCreator should be added separately using a specific method
	 * 
	 * @param gamePhase The gamePhase that is active while this area is used
	 * @param mouseHandler The mouseHandler that will inform the objects about 
	 * mouse events
	 * @param actorHandler The actorHandler that will inform the objects about 
	 * step events 
	 * @param drawer The drawer that will draw the objects 
	 * @param keyHandler The keyListenerHandler that will inform the objects 
	 * about key events
	 * @see #setObjectCreator(SettingUsingAreaObjectCreator)
	 */
	public SettingUsingArea(GamePhase gamePhase, 
			MouseListenerHandler mouseHandler, ActorHandler actorHandler, 
			DrawableHandler drawer, KeyListenerHandler keyHandler)
	{
		super(gamePhase, mouseHandler, actorHandler, drawer, keyHandler);
		
		// Initializes attributes
		this.objectcreator = null;
	}
	
	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 * Changes the object creator that will be informed about the setting changes
	 * 
	 * @param objectCreator The new object Creator used in the room
	 */
	public void setObjectCreator(SettingUsingAreaObjectCreator objectCreator)
	{
		this.objectcreator = objectCreator;
	}
	
	/**
	 * Changes the settings the objectCreator uses to create the objects
	 *
	 * @param setting The new settings used by the objectCreator
	 */
	public void setSettings(AreaSetting setting)
	{
		this.objectcreator.setSettings(setting);
	}
	
	/**
	 * Starts the room using the given settings
	 *
	 * @param setting The settings that affect how the objects in the room are 
	 * created
	 */
	public void start(AreaSetting setting)
	{
		setSettings(setting);
		start();
	}
}
