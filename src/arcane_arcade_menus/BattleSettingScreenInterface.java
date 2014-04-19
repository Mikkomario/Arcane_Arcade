package arcane_arcade_menus;

import utopia_graphic.Sprite;
import utopia_interfaceElements.IntegerOptionBar;
import utopia_interfaceElements.SliderIntegerOptionBar;
import utopia_interfaceElements.StringOptionBar;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import arcane_arcade_main.GameSettings;
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
	
	private IntegerOptionBar elementNumberBar;
	private StringOptionBar manaRegenBar, castDelayBar;
	private SliderIntegerOptionBar maxPointSlider;
	private static final String[] OPTIONS = {"Minimal", "Small", "Normal", 
		"High", "Maximal"};
	
	
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
		
		Sprite arrowSprite = MultiMediaHolder.getSpriteBank("menu").getSprite("arrow");
		Sprite arrowMask = MultiMediaHolder.getSpriteBank("menu").getSprite("arrowmask");
		
		// Initializes attributes
		this.maxPointSlider = new SliderIntegerOptionBar(columnx, columny, 
				15, 1, 99, "Points for Victory",  GameSettings.BASICFONT, 
				GameSettings.WHITETEXTCOLOR, MultiMediaHolder.getSpriteBank("menu").getSprite("sliderback"), 
				MultiMediaHolder.getSpriteBank("menu").getSprite("sliderhandle"), area);
		this.manaRegenBar = new StringOptionBar(columnx, columny + ydifference, 
				OPTIONS, 2, "Colour regen rate", GameSettings.BASICFONT, 
				GameSettings.WHITETEXTCOLOR, arrowSprite, arrowMask, area);
		this.castDelayBar = new StringOptionBar(columnx, columny + ydifference * 2, 
				OPTIONS, 2, "Cast delay length", GameSettings.BASICFONT, 
				GameSettings.WHITETEXTCOLOR, arrowSprite, arrowMask, area);
		this.elementNumberBar = new IntegerOptionBar(columnx, columny + ydifference * 3, 
				3, 1, 3, "Number of elements", GameSettings.BASICFONT, 
				GameSettings.WHITETEXTCOLOR, arrowSprite, arrowMask, area);
		/*
		this.maxpointbar = new OptionBar(columnx, columny, 15, 1, 99, 
				"Points for Victory", area);
		this.manaregenbar = new OptionBar(columnx, columny + ydifference, 
				5, 1, 10, "Colour regen rate", area);
		this.castdelaybar = new OptionBar(columnx, columny + ydifference * 2, 
				5, 1, 10, "Cast time rate", area);
		this.elementnumberbar = new OptionBar(columnx, columny + ydifference * 3, 
				3, 1, 3, "Number of Elements", area);
		*/
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * @return Creates a new instance of fieldsettings based on the values added 
	 * to the option bars
	 */
	public FieldSetting createFieldSetting()
	{
		return new FieldSetting(1, this.maxPointSlider.getValue(), 
				(this.castDelayBar.getValue() + 1) / 5.0, 
				(this.manaRegenBar.getValue() + 1) / 5.0, 
				this.elementNumberBar.getValue());
	}
}
