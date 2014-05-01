package arcane_arcade_menus;

import utopia_listeners.AdvancedMouseListener.MouseButton;
import utopia_worlds.Area;
import arcane_arcade_status.ElementIndicator;
import arcane_arcade_status.ElementIndicatorListener;

/**
 * SpellBookInterface includes the guide system in the spellBook menu. In other 
 * words it shows the possible elements, allows the user to choose any spell 
 * by combining them together and provides information about that spell.
 * 
 * @author Mikko Hilpinen
 * @since 30.4.2014
 */
public class SpellBookInterface implements ElementIndicatorListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	// TODO: Add the two chosen icons
	// TODO: Add a list of the indicators
	private ElementIndicator lastChosenIndicator;
	// TODO: Add the spellInformation presenter(s)
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new SpellBookInterface
	 * @param area The area where the interface will be created
	 */
	public SpellBookInterface(Area area)
	{
		// Initializes attributes
		this.lastChosenIndicator = null;
		
		// Creates the Indicators
		ElementIndicator.createIndicators(area, this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onElementIndicatorPressed(MouseButton button,
			ElementIndicator source)
	{
		// TODO: Move the chosenRings
		// TODO: Update the presented data
		
		// Updates the last chosen indicator
		this.lastChosenIndicator = source;
	}

	@Override
	public void onElementIndicatorCreated(ElementIndicator newIndicator)
	{
		// Does nothing
	}
}
