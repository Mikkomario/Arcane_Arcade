package arcane_arcade_status;

import utopia_listeners.AdvancedMouseListener.MouseButton;

/**
 * ElementIndicatorListeners react to situations where an element indicator is 
 * clicked either with a left or a right mouse button.
 * 
 * @author Mikko Hilpinen
 * @since 28.4.2014
 */
public interface ElementIndicatorListener
{
	/**
	 * This method is called when an elementIndicator is clicked either with 
	 * right or left mouse button
	 * 
	 * @param button The mouse button that was pressed above the indicator
	 * @param source The elementIndicator that was clicked
	 */
	public void onElementIndicatorPressed(MouseButton button, ElementIndicator source);
}
