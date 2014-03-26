package arcane_arcade_worlds;

import utopia_worlds.AreaObjectCreator;

/**
 * SettingUsingAreaObjectCreator creates objects at the start of an area. 
 * Some settings can be relayed to the creator, making it possibly behave 
 * differently.
 * 
 * @author Mikko Hilpinen
 * @since 26.3.2014
 */
public abstract class SettingUsingAreaObjectCreator extends AreaObjectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Class<?> supportedSettingClass;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new AreaObjectCreator to the given area with the given statistics
	 * 
	 * @param area The area where the objects will be created to
	 * @param backgroundName The name of the background sprite used in the area 
	 * (null if no background)
	 * @param backgroundBankName The name of the background sprite bank used in 
	 * the area (null if no background)
	 * @param areaWidth How wide the area is (used for background scaling) (optional)
	 * @param areaHeight How high the area is (used for background scaling) (optional)
	 * @param supportedSettingClass The setting type the class supports 
	 * (null if no settings are supported)
	 */
	public SettingUsingAreaObjectCreator(SettingUsingArea area, String backgroundName,
			String backgroundBankName, int areaWidth, int areaHeight, 
			Class<?> supportedSettingClass)
	{
		super(area, backgroundName, backgroundBankName, areaWidth, areaHeight);
		
		// Initializes attributes
		this.supportedSettingClass = supportedSettingClass;
		
		// Adds the object as the settingUsingArea's object creator
		area.setObjectCreator(this);
	}
	
	
	// ABSTRACT METHODS	--------------------------------------------------
	
	/**
	 * Informs the subclass about the changed settings. Only supported setting 
	 * types are informed.
	 * 
	 * @param newSettings The new settings used in the room
	 */
	protected abstract void onSettingsChange(AreaSetting newSettings);

	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * Changes the settings the room uses.
	 * 
	 * @param newSettings The new settings used in the room
	 */
	public void setSettings(AreaSetting newSettings)
	{
		if (newSettings == null || this.supportedSettingClass == null || 
				!this.supportedSettingClass.isInstance(newSettings))
			return;
		
		onSettingsChange(newSettings);
	}
}
