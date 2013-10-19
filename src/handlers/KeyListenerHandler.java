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
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onKeyDown(char key, int keyCode, boolean coded)
	{
		handleObjects(new AdvancedKeyEventOperator(AdvancedKeyEvent.KEYDOWN, 
				key, keyCode, coded));
	}

	@Override
	public void onKeyPressed(char key, int keyCode, boolean coded)
	{
		handleObjects(new AdvancedKeyEventOperator(AdvancedKeyEvent.KEYPRESSED, 
				key, keyCode, coded));
	}

	@Override
	public void onKeyReleased(char key, int keyCode, boolean coded)
	{
		handleObjects(new AdvancedKeyEventOperator(AdvancedKeyEvent.KEYRELEASED, 
				key, keyCode, coded));
	}
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return AdvancedKeyListener.class;
	}
	
	@Override
	protected boolean handleObject(Handled h)
	{
		// Handling is done via operators
		return false;
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
	
	
	// ENUMERATIONS	-------------------------------------------------------
	
	private enum AdvancedKeyEvent
	{
		KEYDOWN, KEYPRESSED, KEYRELEASED;
	}
	
	
	// SUBCLASSES	-------------------------------------------------------
	
	private class AdvancedKeyEventOperator extends HandlingOperator
	{
		// ATTRIBUTES	---------------------------------------------------
		
		private char key;
		private int keycode;
		private boolean coded;
		private AdvancedKeyEvent event;
		
		
		// CONSTRUCTOR	---------------------------------------------------
		
		public AdvancedKeyEventOperator(AdvancedKeyEvent event, char key, 
				int keycode, boolean coded)
		{
			this.key = key;
			this.keycode = keycode;
			this.coded = coded;
			this.event = event;
		}
		
		
		// IMPLEMENTED METHODS	-------------------------------------------
		
		@Override
		protected boolean handleObject(Handled h)
		{
			AdvancedKeyListener l = (AdvancedKeyListener) h;
			
			// Only informs active handleds
			if (!l.isActive())
				return true;
			
			// Calls an event for the handled
			switch (this.event)
			{
				case KEYDOWN: l.onKeyDown(this.key, this.keycode, this.coded); 
						break;
				case KEYPRESSED: l.onKeyPressed(this.key, this.keycode, 
						this.coded); break;
				case KEYRELEASED: l.onKeyReleased(this.key, this.keycode, 
						this.coded); break;
			}
			
			return true;
		}
	}
}
