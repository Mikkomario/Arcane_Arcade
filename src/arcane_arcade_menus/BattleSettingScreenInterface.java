package arcane_arcade_menus;

import utopia_worlds.Area;
import arcane_arcade_worlds.FieldSetting;

/**
 * This class creates multiple option bars and collects data from them when 
 * needed. This data is then used in creation of a new FieldSetting instance.
 *
 * @author Mikko Hilpinen.
 *         Created 30.11.2013.
 */
public class BattleSettingScreenInterface
{
	// ATTRIBUTES	------------------------------------------------------
	
	private OptionBar maxpointbar, manaregenbar, castdelaybar, elementnumberbar;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates the battleSettingScreenInterface and all option bars in it.
	 *
	 * @param area The area where the objects will be placed to
	 */
	public BattleSettingScreenInterface(Area area)
	{
		int columnx = 200;
		int columny = 150;
		int ydifference = 50;
		
		// Initializes attributes
		this.maxpointbar = new OptionBar(columnx, columny, 15, 1, 99, 
				"Points for Victory", area);
		this.manaregenbar = new OptionBar(columnx, columny + ydifference, 
				5, 1, 10, "Colour regen rate", area);
		this.castdelaybar = new OptionBar(columnx, columny + ydifference * 2, 
				5, 1, 10, "Cast time rate", area);
		this.elementnumberbar = new OptionBar(columnx, columny + ydifference * 3, 
				3, 1, 3, "Number of Elements", area);
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * @return Creates a new instance of fieldsettings based on the values added 
	 * to the option bars
	 */
	public FieldSetting createFieldSetting()
	{
		return new FieldSetting(1, this.maxpointbar.getValue(), 
				this.castdelaybar.getValue() / 5.0, 
				this.manaregenbar.getValue() / 5.0, 
				this.elementnumberbar.getValue());
	}
}
