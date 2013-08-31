package handlers;

import java.util.Iterator;

import handleds.Handled;
import listeners.AdvancedKeyListener;

/**
 * This class informs a group of keylisteners about the key events
 *
 * @author Mikko Hilpinen.
 *         Created 14.12.2012.
 */
public class KeyListenerHandler extends LogicalHandler implements AdvancedKeyListener
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
		// Cleans unnecessary handleds
		removeDeadHandleds();
		
		// Informs all listeners
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			AdvancedKeyListener l = (AdvancedKeyListener) iterator.next();
			l.onKeyDown(key, keyCode, coded);
		}
	}

	@Override
	public void onKeyPressed(char key, int keyCode, boolean coded)
	{
		// Cleans unnecessary handleds
		removeDeadHandleds();
		
		// Informs all listeners
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			AdvancedKeyListener l = (AdvancedKeyListener) iterator.next();
			l.onKeyPressed(key, keyCode, coded);
		}
	}

	@Override
	public void onKeyReleased(char key, int keyCode, boolean coded)
	{
		// Cleans unnecessary handleds
		removeDeadHandleds();
		
		// Informs all listeners
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			AdvancedKeyListener l = (AdvancedKeyListener) iterator.next();
			l.onKeyReleased(key, keyCode, coded);
		}
	}
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return AdvancedKeyListener.class;
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
}
