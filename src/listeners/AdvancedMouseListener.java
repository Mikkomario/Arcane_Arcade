package listeners;

import handleds.LogicalHandled;

/**
 * Mouselisteners are interested in the mouse's movements and button presses
 *
 * @author Mikko Hilpinen.
 *         Created 28.12.2012.
 */
public interface AdvancedMouseListener extends LogicalHandled
{
	/**
	 * This method is called at each step when the left mouse button is down 
	 * and the mouse is over the object
	 * @param mouseX The mouse's current x-coordinate
	 * @param mouseY The mouse's current y-coordinate
	 * @param steps How long the button has been held down since the last 
	 * event (in steps)
	 */
	public void onLeftDown(int mouseX, int mouseY, double steps);
	
	/**
	 * This method is called at each step when the right mouse button is down 
	 * and the mouse is over the object
	 * @param mouseX The mouse's current x-coordinate
	 * @param mouseY The mouse's current y-coordinate
	 * @param steps How long the button has been held down since the last 
	 * event (in steps)
	 */
	public void onRightDown(int mouseX, int mouseY, double steps);
	
	/**
	 * This method is called when the left mouse button is pressed 
	 * and the mouse is over the object
	 * @param mouseX The mouse's current x-coordinate
	 * @param mouseY The mouse's current y-coordinate
	 */
	public void onLeftPressed(int mouseX, int mouseY);
	
	/**
	 * This method is called when the right mouse button is pressed 
	 * and the mouse is over the object
	 * @param mouseX The mouse's current x-coordinate
	 * @param mouseY The mouse's current y-coordinate
	 */
	public void onRightPressed(int mouseX, int mouseY);
	
	/**
	 * This method is called when the left mouse button is released
	 * and the mouse is over the object
	 * @param mouseX The mouse's current x-coordinate
	 * @param mouseY The mouse's current y-coordinate
	 */
	public void onLeftReleased(int mouseX, int mouseY);
	
	/**
	 * This method is called when the right mouse button is released
	 * and the mouse is over the object
	 * @param mouseX The mouse's current x-coordinate
	 * @param mouseY The mouse's current y-coordinate
	 */
	public void onRightReleased(int mouseX, int mouseY);
	
	/**
	 * Tell's whether the object is interested in clicks at the given position
	 * 
	 * @param x The mouse's x-coordinate
	 * @param y The mouse's y-coordinate
	 * @return Is the object interested if the given position is clicked
	 */
	public boolean listensPosition(int x, int y);
	
	/**
	 * @return Should the listener be informed if the mouse enters or exits its 
	 * area of interrest
	 */
	public boolean listensMouseEnterExit();
	
	/**
	 * This method is called when the mouse enters the listener's area of interrest
	 * @param mouseX The mouse's current x-coordinate
	 * @param mouseY The mouse's current y-coordinate
	 */
	public void onMouseEnter(int mouseX, int mouseY);
	
	/**
	 * This method is called when the mouse is over the listener's area of interrest
	 * @param mouseX The mouse's current x-coordinate
	 * @param mouseY The mouse's current y-coordinate
	 */
	public void onMouseOver(int mouseX, int mouseY);
	
	/**
	 * This method is called when the mouse exits the listener's area of interrest
	 * @param mouseX The mouse's current x-coordinate
	 * @param mouseY The mouse's current y-coordinate
	 */
	public void onMouseExit(int mouseX, int mouseY);
	
	/**
	 * This method is called at each time the mouse was moved and it tells 
	 * the mouse's current position
	 * 
	 * @param mouseX The mouse's current x-coordinate
	 * @param mouseY The mouse's current y-coordinate
	 */
	public void onMouseMove(int mouseX, int mouseY);
	
	/**
	 * @return On which scale the object is interested in mouse button events. 
	 * This affects what events are informed to the listener.
	 */
	public MouseButtonEventScale getCurrentButtonScaleOfInterest();
	
	
	// ENUMERATIONS	-------------------------------------------------------
	
	/**
	 * MouseButtonEventScale is used to define which mouse button events 
	 * should be informed to the listener and which shouldn't.
	 *
	 * @author Mikko Hilpinen.
	 *         Created 9.12.2013.
	 */
	public enum MouseButtonEventScale
	{
		/**
		 * Local mouse events are events that happen inside the object's area 
		 * of interest.<br>
		 * Should be used if the listener is interested only about a certain area
		 */
		LOCAL, 
		/**
		 * All mouse events are included in the global mouse events.<br>
		 * Should be used if the listener is interested in mouse events 
		 * regardles of mouse position
		 */
		GLOBAL,
		/**
		 * No event is included in this category.<br>
		 * Should be used if the listener is not interested in mouse button 
		 * events at all.
		 */
		NONE;
	}
}
