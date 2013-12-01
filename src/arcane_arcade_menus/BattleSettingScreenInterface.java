package arcane_arcade_menus;

import arcane_arcade_worlds.FieldSetting;
import worlds.Room;
import handlers.DrawableHandler;
import handlers.MouseListenerHandler;

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
	 * @param drawer The drawer that will draw the option bars
	 * @param mousehandler The mouseListenerHandler that will inform the 
	 * option bars about mouse events
	 * @param room The room that holds the option bars
	 */
	public BattleSettingScreenInterface(DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room)
	{
		int columnx = 200;
		int columny = 150;
		int ydifference = 50;
		
		// Initializes attributes
		this.maxpointbar = new OptionBar(columnx, columny, drawer, 15, 1, 99, 
				"Points for Victory", mousehandler, room);
		this.manaregenbar = new OptionBar(columnx, columny + ydifference, 
				drawer, 5, 1, 10, "Colour regen rate", mousehandler, room);
		this.castdelaybar = new OptionBar(columnx, columny + ydifference * 2, 
				drawer, 5, 1, 10, "Cast time rate", mousehandler, room);
		this.elementnumberbar = new OptionBar(columnx, columny + ydifference * 3, 
				drawer, 2, 1, 2, "Number of Elements", mousehandler, room);
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
