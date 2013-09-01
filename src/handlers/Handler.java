package handlers;

import handleds.Handled;

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
			removeDeadHandleds();
			
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
		
		if (h != null && !this.handleds.contains(h))
		{
			this.handleds.add(h);
			
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
		this.handleds.add(index, h);
		/*
		// Handled must be of the supported class
		if (!getSupportedClass().isInstance(h))
			return;
		
		// TODO: Messes up the order of the drawables somehow
		if (h != null && !this.handleds.contains(h))
		{
			// Removes all of the handleds after the index to another list
			List<Handled> sublist = this.handleds.subList(index, 
					this.handleds.size() - 1);
			List<Handled> holding = new ArrayList<Handled>();
			//holding.addAll(sublist);
			for (int i = 0; i < sublist.size(); i++)
			{
				holding.add(sublist.get(i));
			}
			sublist.clear();
			// Adds the current handled
			this.handleds.add(h);
			// Adds the moved handleds back to the list
			//this.handleds.addAll(holding);
			for (int i = holding.size() - 1; i >= 0; i--)
			{
				//System.out.println("Adding " + holding.get(i).getClass().getName());
				this.handleds.add(holding.get(i));
			}
			holding.clear();
			
			// Also starts the handler if it wasn't already
			if (!this.started)
				this.started = true;
		}
		*/
	}
	
	/**
	 * Removes a handled from the group of handled objects
	 *
	 * @param h The handled object to be removed
	 */
	public void removeHandled(Handled h)
	{
		if (h != null && this.handleds.contains(h))
			this.handleds.remove(h);
	}
	
	/**
	 * Removes all the handleds from the handler
	 */
	protected void removeAllHandleds()
	{
		this.handleds.clear();
	}
	
	/**
	 * Removes possible dead handleds from the handled objects
	 */
	protected void removeDeadHandleds()
	{
		// Removes all the dead handleds from the list to save processing time
		for (int i = 0; i < getHandledNumber(); i++)
		{
			Handled h = getHandled(i);
			if (h.isDead())
				this.handleds.remove(h);
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
	
	/*
	 * Returns a single handled from the list of handled objects
	 * 
	 * @param index The index of the handled object
	 * @return The object or null if no such index exists
	 */
	/*
	protected Handled getHandled(int index)
	{
		if (index >= 0 && index < getHandledNumber())
			return this.handleds.get(index);
		else
			return null;
	}
	*/
}
