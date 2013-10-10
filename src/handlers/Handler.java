package handlers;

import handleds.Handled;

import java.util.ArrayList;
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
	
	// TODO: Create status system for the new lists (so they are not modified 
	// while iterating)
	
	private LinkedList<Handled> handleds;
	private volatile ArrayList<Handled> handledstoberemoved, handledstobeadded;
	private volatile HashMap<Handled, Integer> handledstobeinserted;
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
		this.killed = false;
		this.handleds = new LinkedList<Handled>();
		this.handledstobeadded = new ArrayList<Handled>();
		this.handledstobeinserted = new HashMap<Handled, Integer>();
		this.handledstoberemoved = new ArrayList<Handled>();
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
		
		// or if autodeath is on and it's empty (but has had an object in it 
		// previously)
		if (this.autodeath)
		{	
			updateStatus();
			//System.out.println("Started: " + this.started + ", empty: " + 
			//this.handleds.isEmpty());
			if (this.started && this.handleds.isEmpty())
				return true;
		}
		
		return false;
	}

	@Override
	public void kill()
	{
		// Tries to permanently inactivate all subhandleds and kill the handler
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
		//System.out.println(this+ " Starts handling objects");
		
		updateStatus();
		
		// TODO: Add stopHandlingException for some cases where the handling 
		// needs to be stopped
		
		// Goes through all the handleds
		Iterator<Handled> iterator = this.handleds.iterator();
		
		while (iterator.hasNext())
		{
			//System.out.println(getClass().getName() + " handles an object");
			
			Handled h = iterator.next();
			
			// TODO: Uncomment the section if you want to (slows the speed but makes 
			// it more accurate
			if (!h.isDead() /*&& !this.handledstoberemoved.containsKey(h)*/)
				handleObject(h);
			else
				removeHandled(h);
		}
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
		//System.out.println(this + " tries to add a handled");
		
		// Handled must be of the supported class
		if (!getSupportedClass().isInstance(h))
		{
			System.err.println(getClass().getName() + 
					" does not support given object's class");
			return;
		}
		
		if (h != null && h != this && !this.handleds.contains(h) && 
				!this.handledstobeadded.contains(h))
		{
			this.handledstobeadded.add(h);
			this.started = true;
			//System.out.println(this + " adds a handled to queue (now " + 
			//			this.handledstobeadded.size() + ")");
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
		if (!this.handledstobeinserted.containsKey(h))
		{
			this.started = true;
			this.handledstobeinserted.put(h, index);
		}
	}
	
	/**
	 * Removes a handled from the group of handled objects
	 *
	 * @param h The handled object to be removed
	 */
	public void removeHandled(Handled h)
	{
		if (h != null && !this.handledstoberemoved.contains(h) && 
				(this.handleds.contains(h) || this.handledstobeadded.contains(h) 
				|| this.handledstobeinserted.containsKey(h)))
		{
			this.handledstoberemoved.add(h);
		}
	}
	
	/**
	 * Removes all the handleds from the handler
	 */
	public void removeAllHandleds()
	{
		for (int i = 0; i < getHandledNumber(); i++)
		{
			removeHandled(getHandled(i));
		}
		// Also cancels the adding of new handleds
		this.handledstobeadded.clear();
		this.handledstobeinserted.clear();
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
	
	/**
	 * Updates the handler list by adding new members and removing old ones. 
	 * This method should not be called during an iteration but is useful before 
	 * testsing the handler status.<br>
	 * Status is automatically updated each time the handleds in the handler 
	 * are handled.
	 * 
	 * @see #handleObjects()
	 */
	protected void updateStatus()
	{
		// Removes the dead handleds (if possible)
		clearRemovedHandleds();
		// Inserts new handleds (if possible)
		insertNewHandleds();
		// Adds the new handleds (if possible)
		addNewHandleds();
	}
	
	// This should be called at the end of the iteration
	private void clearRemovedHandleds()
	{
		if (this.handledstoberemoved.isEmpty())
			return;
		
		for (Handled h : this.handledstoberemoved)
		{
			if (this.handleds.contains(h))
				this.handleds.remove(h);
			else if (this.handledstoberemoved.contains(h))
				this.handledstobeadded.remove(h);
			else if (this.handledstobeinserted.containsKey(h))
				this.handledstobeinserted.remove(h);
		}
		
		this.handledstoberemoved.clear();
	}
	
	private void addNewHandleds()
	{
		// If the handler has no handleds to be added, does nothing
		if (this.handledstobeadded.isEmpty())
		{
			//System.out.println(this + " skipped adding since queue is empty");
			return;
		}
		
		//System.out.println(this + ": Handleds to be added: " + 
		//		this.handledstobeadded.size());
		
		// TODO: Another concurrentModificationException?
		for (Handled h : this.handledstobeadded)
		{
			this.handleds.add(h);
			//System.out.println(this + " added a new handler. Now contains " 
			//		+ this.handleds.size() + " elements");
			
			// Also starts the handler if it wasn't already
			//if (!this.started)
			//	this.started = true;
		}
		
		this.handledstobeadded.clear();
	}
	
	private void insertNewHandleds()
	{
		if (this.handledstobeinserted.isEmpty())
			return;
		
		for (Handled h : this.handledstobeinserted.keySet())
		{
			int index = this.handledstobeinserted.get(h);
			this.handleds.add(index, h);
			
			// Also starts the handler if it wasn't already
			//if (!this.started)
			//	this.started = true;
		}
		
		this.handledstobeinserted.clear();
	}
}
