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
	
	private LinkedList<Handled> handleds;
	private ArrayList<Handled> handledstoberemoved, handledstobeadded;
	private HashMap<Handled, Integer> handledstobeinserted;
	private boolean autodeath;
	private boolean killed;
	private boolean started; // Have any objects been added to the handler yet
	
	private boolean addlistready, removelistready, insertlistready, handlingready;
	
	
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
		this.addlistready = true;
		this.removelistready = true;
		this.insertlistready = true;
		this.handlingready = true;
		
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
		// TODO: Might cause problems if called during iterating
		
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
		checkHandlingStatus();
		this.handleds.clear();
		checkAddStatus();
		this.handledstobeadded.clear();
		checkInsertStatus();
		this.handledstobeinserted.clear();
		checkRemoveStatus();
		this.handledstoberemoved.clear();
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
		this.handlingready = false;
		Iterator<Handled> iterator = this.handleds.iterator();
		
		while (iterator.hasNext())
		{
			if (this.killed)
				break;
			
			Handled h = iterator.next();
			
			// TODO: Uncomment the section if you want to (slows the speed but makes 
			// it more accurate
			if (!h.isDead() /*&& !this.handledstoberemoved.containsKey(h)*/)
				handleObject(h);
			else
				removeHandled(h);
		}
		this.handlingready = true;
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
		
		if (h != this && !this.handleds.contains(h) && 
				!this.handledstobeadded.contains(h))
		{
			checkAddStatus();
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
			checkInsertStatus();
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
			checkRemoveStatus();
			this.handledstoberemoved.add(h);
		}
	}
	
	/**
	 * Removes all the handleds from the handler
	 */
	public void removeAllHandleds()
	{
		this.handlingready = false;
		for (int i = 0; i < getHandledNumber(); i++)
		{
			checkRemoveStatus();
			removeHandled(getHandled(i));
		}
		this.handlingready = true;
		// Also cancels the adding of new handleds
		checkAddStatus();
		this.handledstobeadded.clear();
		checkInsertStatus();
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
		
		// TODO: Still throws an concurrentmodificationexception
		// Though I can't figure out why...
		this.removelistready = false;
		for (Handled h : this.handledstoberemoved)
		{
			if (this.handleds.contains(h))
			{
				checkHandlingStatus();
				this.handleds.remove(h);
			}
			else if (this.handledstobeadded.contains(h))
			{
				checkAddStatus();
				this.handledstobeadded.remove(h);
			}
			else if (this.handledstobeinserted.containsKey(h))
			{
				checkInsertStatus();
				this.handledstobeinserted.remove(h);
			}
		}
		
		this.handledstoberemoved.clear();
		this.removelistready = true;
	}
	
	private void addNewHandleds()
	{
		// If the handler has no handleds to be added, does nothing
		if (this.handledstobeadded.isEmpty())
			return;
		
		this.addlistready = false;
		for (Handled h : this.handledstobeadded)
		{
			checkHandlingStatus();
			this.handleds.add(h);
		}
		
		this.handledstobeadded.clear();
		this.addlistready = true;
	}
	
	private void insertNewHandleds()
	{
		if (this.handledstobeinserted.isEmpty())
			return;
		
		this.insertlistready = false;
		for (Handled h : this.handledstobeinserted.keySet())
		{
			int index = this.handledstobeinserted.get(h);
			checkHandlingStatus();
			this.handleds.add(index, h);
		}
		
		this.handledstobeinserted.clear();
		this.insertlistready = true;
	}
	
	// TODO: Try DRY
	// PS: This would be so much nicer with pointers
	private void checkAddStatus()
	{
		while(!this.addlistready)
		{
			wait(2);
		}
	}
	
	private void checkInsertStatus()
	{
		while(!this.insertlistready)
		{
			wait(2);
		}
	}
	
	private void checkRemoveStatus()
	{
		while(!this.removelistready)
		{
			wait(2);
		}
	}
	
	private void checkHandlingStatus()
	{
		while(!this.handlingready)
		{
			wait(2);
		}
	}
	
	private void wait(int millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException exception)
		{
			System.err.println("Handling status check was interupted");
			exception.printStackTrace();
		}
	}
}
