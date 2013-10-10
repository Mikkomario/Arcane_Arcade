package handlers;

import java.util.Iterator;

import handleds.Handled;
import handleds.LogicalHandled;

/**
 * LogicalHandlers specialize in logicalhandleds instead of just any handlers. 
 * This class provides some methods necessary for all subclasses and can be used as
 * a logical handled in other handlers.
 *
 * @author Mikko Hilpinen.
 *         Created 8.12.2012.
 */
public abstract class LogicalHandler extends Handler implements LogicalHandled
{
	// CONSTRUCTOR	-------------------------------------------------------

	/**
	 * Creates a new logicalhandler. Handled objects must be added manually later
	 *
	 * @param autodeath Will the handler die if it runs out of living handleds
	 * @param superhandler The handler that will handle this handler (optional)
	 */
	public LogicalHandler(boolean autodeath, LogicalHandler superhandler)
	{
		super(autodeath, superhandler);
	}
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------

	@Override
	public boolean isActive()
	{
		// Updates the handler status before checking it
		updateStatus();
		
		// Returns false only if all the handleds are inactive
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			LogicalHandled h = (LogicalHandled) iterator.next();
			if (h.isActive())
				return true;
		}
		
		return false;
	}

	@Override
	public void activate()
	{
		// tries to activate all the handled objects
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			LogicalHandled h = (LogicalHandled) iterator.next();
			h.activate();
		}
	}

	@Override
	public void inactivate()
	{
		// tries to inactivate all the handled objects
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			LogicalHandled h = (LogicalHandled) iterator.next();
			h.inactivate();
		}
	}
}
