package handlers;

import handleds.Handled;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Handlers specialize in handling certain types of objects. Each handler can 
 * inform its subobjects and can be handled itself.
 *
 * @author Mikko Hilpinen.
 *         Created 8.12.2012.
 */
public abstract class Handler implements Handled
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private LinkedList<Handled> handleds;
	private HashMap<Handled, Handler> handledstoberemoved;
	private HashMap<Handled, Handler> handledstobeadded;
	private HashMap<Handler, HashMap<Handled, Integer>> handledstobeinserted;
	private Handler superhandler;
	private boolean autodeath;
	private boolean killed;
	private boolean started; // Have any objects been added to the handler yet
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new handler that is still empty. Handled objects must be added 
	 * manually later. If autodeath is set on, the handled will be destroyed as 
	 * soon as it becomes empty.
	 *
	 * @param autodeath Will the handler die automatically when it becomes empty
	 * @param superhandler The handler that will handle the object (optional)
	 */
	public Handler(boolean autodeath, Handler superhandler)
	{
		// Initializes attributes
		this.autodeath = autodeath;
		this.superhandler = superhandler;
		this.killed = false;
		this.handleds = new LinkedList<Handled>();
		this.handledstoberemoved = new HashMap<Handled, Handler>();
		this.handledstobeadded = new HashMap<Handled, Handler>();
		this.handledstobeinserted = new HashMap<Handler, HashMap<Handled, 
				Integer>>();
		this.handledstobeinserted.put(this, new HashMap<Handled, Integer>());
		this.started = false;
		
		// Tries to add itself to the superhandler
		if (superhandler != null)
			superhandler.addHandled(this);
	}
	
	
	// ABSTRACT METHODS	---------------------------------------------------
	
	/**
	 * @return The class supported by the handler
	 */
	protected abstract Class<?> getSupportedClass();
	
	/**
	 * Many handlers are supposed to do something to the handled objects. 
	 * That something should be done in this method. The method is called as 
	 * a part of the handleObjects method.
	 *
	 * @param h The handler that may need handling
	 */
	protected abstract void handleObject(Handled h);
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------

	@Override
	public boolean isDead()
	{
		// The handler is dead if it was killed
		if (this.killed)
			return true;
		
		// or if autodeath is on and it's empty (but has had an object in it previously)
		if (this.autodeath)
		{
			// Removes dead handleds to be sure
			// TODO: Removed this part to not cause concurrentmodificationexceptions
			
			if (this.started && this.handleds.isEmpty())
				return true;
		}
		
		return false;
	}

	@Override
	public void kill()
	{
		// Tries to permanently inactivate all subhandleds. If all were 
		// successfully killed, this handldler is also killed in the process
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			iterator.next().kill();
		}
		
		// Also erases the memory
		killWithoutKillingHandleds();
	}
	
	/**
	 * Kills the handler but spares the handleds in the handler. This should 
	 * be used instead of kill -method if, for example, the handleds are still 
	 * used in another handler.
	 */
	public void killWithoutKillingHandleds()
	{
		this.handleds.clear();
		this.killed = true;
	}
	
	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 * Goes through all the handleds and calls handleObject -method for those 
	 * objects
	 * 
	 * @see #handleObject(Handled)
	 */
	protected void handleObjects()
	{
		// TODO: Add stopHandlingException for some cases where the handling 
		// needs to be stopped
		
		// Goes through all the handleds
		Iterator<Handled> iterator = this.handleds.iterator();
		
		while (iterator.hasNext())
		{
			Handled h = iterator.next();
			
			// TODO: Uncomment the section if you want to (slows the speed but makes 
			// it more accurate
			if (!h.isDead() /*&& !this.handledstoberemoved.containsKey(h)*/)
				handleObject(h);
			else
				removeHandled(h);
		}
		
		// Removes the dead handleds (if possible)
		clearRemovedHandleds();
		// Inserts new handleds (if possible)
		insertNewHandleds();
		// Adds the new handleds (if possible)
		addNewHandleds();
	}
	
	/**
	 * @return The iterator of the handled list
	 */
	protected Iterator<Handled> getIterator()
	{
		return this.handleds.iterator();
	}
	
	/**
	 * Adds a new object to the handled objects
	 *
	 * @param h The object to be handled
	 */
	protected void addHandled(Handled h)
	{	
		// Handled must be of the supported class
		if (!getSupportedClass().isInstance(h))
			return;
		
		if (h != null && !this.handleds.contains(h) && 
				!this.handledstobeadded.containsKey(h))
		{
			this.handledstobeadded.put(h, this);
			
			// Also starts the handler if it wasn't already
			if (!this.started)
				this.started = true;
		}
	}
	
	/**
	 * Adds a new object to the handled objects
	 *
	 * @param h The object to be handled
	 * @param index The index to which the handled is inserted to
	 */
	protected void insertHandled(Handled h, int index)
	{
		//this.handleds.add(index, h);
		if (!this.handledstobeinserted.get(this).containsKey(h))
			this.handledstobeinserted.get(this).put(h, index);
	}
	
	/**
	 * Removes a handled from the group of handled objects
	 *
	 * @param h The handled object to be removed
	 */
	public void removeHandled(Handled h)
	{
		// TODO: Changed this to use the new mehanic, might cause problems
		if (h != null && this.handleds.contains(h) && 
				!this.handledstoberemoved.containsKey(h))
		{
			//this.handleds.remove(h);
			this.handledstoberemoved.put(h, this);
		}
	}
	
	/**
	 * Removes all the handleds from the handler
	 */
	protected void removeAllHandleds()
	{
		// TODO: CHanged this to use the new deadhandlers mechanic, might 
		// cause problems
		//this.handleds.clear();
		for (int i = 0; i < getHandledNumber(); i++)
		{
			removeHandled(getHandled(i));
		}
	}
	
	/**
	 * @return How many objects is the handler currently taking care of
	 */
	protected int getHandledNumber()
	{
		return this.handleds.size();
	}
	
	/**
	 * @return The first handled in the list of handleds
	 */
	protected Handled getFirstHandled()
	{
		return this.handleds.getFirst();
	}
	
	/**
	 * Returns a certain handled form the list
	 *
	 * @param index The index from which the handled is taken
	 * @return The handled from the given index or null if no such index exists
	 * @warning Normally it is adviced to use the iterator to go through the 
	 * handleds but if the caller modifies the list during the iteration, this 
	 * method should be used instead
	 * @see #getIterator()
	 */
	protected Handled getHandled(int index)
	{
		if (index < 0 || index >= getHandledNumber())
			return null;
		return this.handleds.get(index);
	}
	
	// This should be called at the end of the iteration
	private void clearRemovedHandleds()
	{
		// If the handler is the top handler, removes the handleds
		if (this.superhandler == null)
		{
			for (Handled h : this.handledstoberemoved.keySet())
			{
				Handler handler = this.handledstoberemoved.get(h);
				handler.handleds.remove(h);
			}
		}
		// Otherwise delegates the removal progres for the superhandler
		else
		{
			// TODO: Clone needed?
			//delegateRemoval((HashMap<Handled, Handler>) 
			//		this.handledstoberemoved.clone());
			delegateRemoval(this.handledstoberemoved);
		}
		
		this.handledstoberemoved.clear();
	}
	
	private void delegateRemoval(HashMap<Handled, Handler> removed)
	{
		/*
		for (Handled h : removed.keySet())
		{
			this.handledstoberemoved.put(h, removed.get(h));
		}
		*/
		this.superhandler.handledstoberemoved.putAll(removed);
	}
	
	private void addNewHandleds()
	{
		// Adds the new handleds from the collected map or delegates the adding 
		// progress
		// If the handler is the top handler, removes the handleds
		if (this.superhandler == null)
		{
			for (Handled h : this.handledstobeadded.keySet())
			{
				Handler handler = this.handledstobeadded.get(h);
				handler.handleds.add(h);
			}
		}
		// Otherwise delegates the removal progres for the superhandler
		else
			delegateAdding(this.handledstobeadded);
		
		this.handledstobeadded.clear();
	}
	
	private void delegateAdding(HashMap<Handled, Handler> added)
	{
		/*
		for (Handled h : added.keySet())
		{
			this.handledstobeadded.put(h, added.get(h));
		}
		*/
		this.superhandler.handledstobeadded.putAll(added);
	}
	
	private void delegateInsterting(HashMap<Handler, HashMap<Handled, 
			Integer>> inserted)
	{
		// TODO: Test if addAll works, return this if not
		/*
		for (Handler handler : inserted.keySet())
		{
			this.handledstobeinserted.put(handler, inserted.get(handler));
		}
		*/
		this.superhandler.handledstobeinserted.putAll(inserted);
	}
	
	private void insertNewHandleds()
	{
		// Adds the new handleds from the collected map or delegates the adding 
		// progress
		// If the handler is the top handler, removes the handleds
		if (this.superhandler == null)
		{
			// Goes through all the handlers and handleds
			for (Handler handler : this.handledstobeinserted.keySet())
			{
				for (Handled h : this.handledstobeinserted.get(handler).keySet())
				{
					int index = this.handledstobeinserted.get(handler).get(h);
					handler.handleds.add(index, h);
				}
			}
		}
		// Otherwise delegates the removal progress for the superhandler
		else
			delegateInsterting(this.handledstobeinserted);
		
		this.handledstobeinserted.clear();
	}
}
