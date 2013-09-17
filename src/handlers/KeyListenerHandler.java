package handlers;

import handleds.Handled;
import listeners.AdvancedKeyListener;

/**
 * This class informs a group of keylisteners about the key events
 *
 * @author Mikko Hilpinen.
 *         Created 14.12.2012.
 */
public class KeyListenerHandler extends LogicalHandler implements 
		AdvancedKeyListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private AdvancedKeyEvent lastevent;
	private char lastkey;
	private int lastkeycode;
	private boolean lastcoded;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new empty keylistenerhandler. Listeners must be added manually
	 *
	 * @param autodeath Will the handler die when it runs out of living handleds
	 * @param superhandler The handler that will handle this handler (optional)
	 */
	public KeyListenerHandler(boolean autodeath, KeyListenerHandler superhandler)
	{
		super(autodeath, superhandler);
		
		// Initializes attributes
		this.lastevent = null;
		this.lastkey = ' ';
		this.lastkeycode = 0;
		this.lastcoded = false;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onKeyDown(char key, int keyCode, boolean coded)
	{
		informListenersAboutEvent(AdvancedKeyEvent.KEYDOWN, key, keyCode, 
				coded);
	}

	@Override
	public void onKeyPressed(char key, int keyCode, boolean coded)
	{
		informListenersAboutEvent(AdvancedKeyEvent.KEYPRESSED, key, keyCode, 
				coded);
	}

	@Override
	public void onKeyReleased(char key, int keyCode, boolean coded)
	{
		informListenersAboutEvent(AdvancedKeyEvent.KEYRELEASED, key, keyCode, 
				coded);
	}
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return AdvancedKeyListener.class;
	}
	
	@Override
	protected void handleObject(Handled h)
	{
		// Informs the handled about the last event
		AdvancedKeyListener l = (AdvancedKeyListener) h;
		
		// Only if the handled is active
		if (!l.isActive())
			return;
		
		switch (this.lastevent)
		{
			case KEYDOWN: l.onKeyDown(this.lastkey, this.lastkeycode, 
					this.lastcoded); break;
			case KEYPRESSED: l.onKeyPressed(this.lastkey, this.lastkeycode, 
					this.lastcoded); break;
			case KEYRELEASED: l.onKeyReleased(this.lastkey, this.lastkeycode, 
					this.lastcoded); break;
		}
	}
	
	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 * Adds a new listener to the informed listeners
	 *
	 * @param k The KeyListener added
	 */
	public void addKeyListener(AdvancedKeyListener k)
	{
		addHandled(k);
	}
	
	private void informListenersAboutEvent(AdvancedKeyEvent event, char key, 
			int keyCode, boolean coded)
	{
		// Remembers the data
		this.lastkey = key;
		this.lastkeycode = keyCode;
		this.lastcoded = coded;
		this.lastevent = event;
		
		// Informs all the listeners
		handleObjects();
	}
	
	
	// ENUMERATIONS	-------------------------------------------------------
	
	private enum AdvancedKeyEvent
	{
		KEYDOWN, KEYPRESSED, KEYRELEASED;
	}
}
