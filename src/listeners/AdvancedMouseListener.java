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
	 */
	public void onLeftDown(int mouseX, int mouseY);
	
	/**
	 * This method is called at each step when the right mouse button is down 
	 * and the mouse is over the object
	 * @param mouseX The mouse's current x-coordinate
	 * @param mouseY The mouse's current y-coordinate
	 */
	public void onRightDown(int mouseX, int mouseY);
	
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
}
