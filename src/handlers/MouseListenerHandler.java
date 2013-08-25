package handlers;

import listeners.AdvancedMouseListener;

/**
 * Informs multiple mouselisteners about the mouse's movements and button status
 *
 * @author Mikko Hilpinen.
 *         Created 28.12.2012.
 */
public class MouseListenerHandler extends AbstractMouseListenerHandler 
	implements AdvancedMouseListener
{
	// CONSTRUCTOR	-------------------------------------------------------
	
	/**
	 * Creates a new empty mouselistenerhandler
	 *
	 * @param autodeath Will the handler die when it runs out of listeners
	 * @param actorhandler The actorhandler that will handle this handler 
	 * (optional)
	 * @param superhandler The mouselistenerhandler that will inform this 
	 * handler (optional)
	 */
	public MouseListenerHandler(boolean autodeath, 
			ActorHandler actorhandler, MouseListenerHandler superhandler)
	{
		super(autodeath, actorhandler);
		
		// Tries to add the object to the second handler
		if (superhandler != null)
			superhandler.addMouseListener(this);
	}
	
	
	// IMPLEMENTED METHODS	-------------------------------------------------

	@Override
	public void onLeftDown(int mouseX, int mouseY)
	{
		// Does nothing
	}

	@Override
	public void onRightDown(int mouseX, int mouseY)
	{
		// Does nothing
	}

	@Override
	public void onLeftPressed(int mouseX, int mouseY)
	{
		// Updates the mouse status
		setLeftMouseDown(true);
	}

	@Override
	public void onRightPressed(int mouseX, int mouseY)
	{
		// Updates the mouse status
		setRightMouseDown(true);
	}

	@Override
	public boolean listensPosition(int x, int y)
	{
		// Handlers listen all positions
		return true;
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		// Does not have a special area of interrest
		return false;
	}

	@Override
	public void onMouseEnter(int mouseX, int mouseY)
	{
		// Does nothing
	}

	@Override
	public void onMouseExit(int mouseX, int mouseY)
	{
		// Nothing
	}

	@Override
	public void onMouseMove(int mouseX, int mouseY)
	{
		// Updates mouse status
		setMousePosition(mouseX, mouseY);
	}

	@Override
	public void onLeftReleased(int mouseX, int mouseY)
	{
		// Updates mouse status
		setLeftMouseDown(false);
	}

	@Override
	public void onRightReleased(int mouseX, int mouseY)
	{
		// Updates mouse status
		setRightMouseDown(false);
	}

	@Override
	public void onMouseOver(int mouseX, int mouseY)
	{
		// Nothing
	}
}
