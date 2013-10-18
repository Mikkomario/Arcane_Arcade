package handlers;

import handleds.Handled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

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
	private ArrayList<Handled> handledstoberemoved, handledstobeadded;
	private HashMap<Handled, Integer> handledstobeinserted;
	private boolean autodeath;
	private boolean killed;
	private boolean started; // Have any objects been added to the handler yet
	
	private HashMap<HandlingOperation, ReentrantLock> locks;
	
	
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
		this.locks = new HashMap<HandlingOperation, ReentrantLock>();
		this.locks.put(HandlingOperation.HANDLE, new ReentrantLock());
		this.locks.put(HandlingOperation.ADD, new ReentrantLock());
		this.locks.put(HandlingOperation.REMOVE, new ReentrantLock());
		this.locks.put(HandlingOperation.INSERT, new ReentrantLock());
		
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
		// Safely clears the handleds
		clearOperationList(HandlingOperation.HANDLE);
		// Safely clears the added handleds
		clearOperationList(HandlingOperation.ADD);
		// Safely clears the inserted handleds
		clearOperationList(HandlingOperation.INSERT);
		// And finally clears the removed handleds
		clearOperationList(HandlingOperation.REMOVE);
		
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
		
		// Goes through all the handleds
		// TODO: There's an deadlock here that occurs randomly when the room 
		// end is informed
		System.out.println(this + " tries to open handling lock");
		this.locks.get(HandlingOperation.HANDLE).lock();
		System.out.println(this + " opened handling lock");
		try
		{
			Iterator<Handled> iterator = this.handleds.iterator();
			
			while (iterator.hasNext())
			{
				if (this.killed)
					break;
				
				Handled h = iterator.next();
				
				if (!h.isDead() /*&& !this.handledstoberemoved.containsKey(h)*/)
					handleObject(h);
				else
					removeHandled(h);
			}
		}
		finally
		{
			this.locks.get(HandlingOperation.HANDLE).unlock();
			System.out.println(this + " closed the handling lock");
		}
		
		updateStatus();
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
		
		// Performs necessary checks
		if (h != this && !this.handleds.contains(h) && 
				!this.handledstobeadded.contains(h))
		{
			// Adds the handled to the queue
			addToOperationList(HandlingOperation.ADD, h);
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
		if (h != this && !this.handleds.contains(h) && 
				!this.handledstobeinserted.containsKey(h))
		{
			this.started = true;
			addToOperationList(HandlingOperation.INSERT, h, index);
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
				(this.handleds.contains(h) /* TODO: uncomment if removal 
				becomes the first operation || 
				this.handledstobeadded.contains(h) 
				|| this.handledstobeinserted.containsKey(h)*/
				))
		{
			addToOperationList(HandlingOperation.REMOVE, h);
		}
	}
	
	/**
	 * Removes all the handleds from the handler
	 */
	public void removeAllHandleds()
	{
		this.locks.get(HandlingOperation.HANDLE).lock();
		try
		{
			Iterator<Handled> iter = getIterator();
			
			while (iter.hasNext())
			{
				removeHandled(iter.next());
			}
		}
		finally { this.locks.get(HandlingOperation.HANDLE).unlock(); }
		
		// Also cancels the adding of new handleds
		clearOperationList(HandlingOperation.ADD);
		clearOperationList(HandlingOperation.INSERT);
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
		// TODO: Throws nullpointer
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
		// Inserts new handleds (if possible)
		insertNewHandleds();
		// Adds the new handleds (if possible)
		addNewHandleds();
		// Removes the dead handleds (if possible)
		clearRemovedHandleds();
	}
	
	// This should be called at the end of the iteration
	private void clearRemovedHandleds()
	{
		if (this.handledstoberemoved.isEmpty())
			return;
		
		this.locks.get(HandlingOperation.REMOVE).lock();
		try
		{
			// Removes all removed handleds from handleds or added or 
			// inserted handleds
			for (Handled h : this.handledstoberemoved)
			{
				if (this.handleds.contains(h))
					removeFromOperationList(HandlingOperation.HANDLE, h);
				/* TODO: Readd them if you take the removal back as the first 
				 * operation
				else if (this.handledstobeadded.contains(h))
					removeFromOperationList(HandlingOperation.ADD, h);
				else if (this.handledstobeinserted.containsKey(h))
					removeFromOperationList(HandlingOperation.INSERT, h);
				*/
			}
			
			// Empties the removing list
			// TODO: One might want to change these into clearoperationList(...)
			this.handledstoberemoved.clear();
		}
		finally { this.locks.get(HandlingOperation.REMOVE).unlock(); }
	}
	
	private void addNewHandleds()
	{
		// If the handler has no handleds to be added, does nothing
		if (this.handledstobeadded.isEmpty())
			return;
		
		this.locks.get(HandlingOperation.ADD).lock();
		try
		{
			// Adds all handleds from the addlist to the handleds
			for (Handled h : this.handledstobeadded)
			{
				addToOperationList(HandlingOperation.HANDLE, h);
			}
			
			// Clears the addlist
			this.handledstobeadded.clear();
		}
		finally { this.locks.get(HandlingOperation.ADD).unlock(); }
	}
	
	private void insertNewHandleds()
	{
		if (this.handledstobeinserted.isEmpty())
			return;
		
		this.locks.get(HandlingOperation.INSERT).lock();
		try
		{
			// Inserts all handleds to certain positions in the list
			for (Handled h : this.handledstobeinserted.keySet())
			{
				int index = this.handledstobeinserted.get(h);
				addToOperationList(HandlingOperation.HANDLE, h, index);
			}
			
			// Clears the insert list
			this.handledstobeinserted.clear();
		}
		finally { this.locks.get(HandlingOperation.INSERT).unlock(); }
	}
	
	// Thread-safely clears a data structure used with the given operation type
	// TODO: Try to figure out a way to make this without copy-paste, though 
	// it might be difficult since there are no function pointers in java
	private void clearOperationList(HandlingOperation o)
	{
		// Checks the argument
		if (o == null)
			return;
		
		// Locks the correct lock
		this.locks.get(o).lock();
		
		try
		{
			switch (o)
			{
				// I really wish I had function pointers in use now...
				case HANDLE: this.handleds.clear(); break;
				case ADD: this.handledstobeadded.clear(); break;
				case REMOVE: this.handledstoberemoved.clear(); break;
				case INSERT: this.handledstobeinserted.clear(); break;
			}
		}
		finally
		{
			this.locks.get(o).unlock();
		}
	}
	
	// Thread safely adds an handled to an operation list
	private void addToOperationList(HandlingOperation o, Handled h)
	{
		// Checks the argument
		if (o == null || h == null)
			return;
		
		// Locks the correct lock
		this.locks.get(o).lock();
		
		try
		{
			switch (o)
			{
				case HANDLE: this.handleds.add(h); break;
				case ADD: this.handledstobeadded.add(h); break;
				case REMOVE: this.handledstoberemoved.add(h); break;
				case INSERT: System.err.println("Index must be given if " +
						"something needs to be added to the insertlist"); break;
			}
		}
		finally
		{
			this.locks.get(o).unlock();
		}
	}
	
	// Thread safely adds an handled to the insertion list with the given index
	private void addToOperationList(HandlingOperation o, Handled h, int index)
	{	
		// Locks the correct lock
		this.locks.get(o).lock();
		
		try
		{
			switch (o)
			{
				case INSERT: this.handledstobeinserted.put(h, index); break;
				case HANDLE: this.handleds.add(index, h); break;
				case ADD: this.handledstobeadded.add(index, h); break;
				case REMOVE: this.handledstoberemoved.add(index, h); break;
			}
		}
		finally
		{
			this.locks.get(o).unlock();
		}
	}
	
	// Thread safely removes an handled from an operation list
	private void removeFromOperationList(HandlingOperation o, Handled h)
	{
		// Checks the argument
		if (o == null || h == null)
			return;
		
		// Locks the correct lock
		this.locks.get(o).lock();
		
		try
		{
			switch (o)
			{
				case HANDLE: this.handleds.remove(h); break;
				case ADD: this.handledstobeadded.remove(h); break;
				case REMOVE: this.handledstoberemoved.remove(h); break;
				case INSERT: this.handledstobeinserted.remove(h); break;
			}
		}
		finally
		{
			this.locks.get(o).unlock();
		}
	}
	
	
	// ENUMERATIONS	------------------------------------------------------
	
	private enum HandlingOperation
	{
		HANDLE, ADD, REMOVE, INSERT;
	}
}
