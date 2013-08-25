package handlers;

import handleds.Collidable;
import handleds.Handled;

/**
 * This class handles multiple collidables. Any collision checks made for this 
 * object are made for all the collidables.
 *
 * @author Mikko Hilpinen.
 *         Created 18.6.2013.
 */
public class CollidableHandler extends Handler implements Collidable
{
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new empty collidablehandler
	 *
	 * @param autodeath Will the handler die if it runs out of handleds
	 * @param superhandler The collidablehandler that holds the handler
	 */
	public CollidableHandler(boolean autodeath, CollidableHandler superhandler)
	{
		super(autodeath, superhandler);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public boolean isSolid()
	{
		// Handler is solid if any of the objects are solid
		for (int i = 0; i < getHandledNumber(); i++)
		{
			if (getCollidable(i).isSolid())
				return true;
		}
		
		return false;
	}

	@Override
	public boolean makeSolid()
	{
		boolean returnvalue = true;
		// Tries to make all of the collidables solid
		for (int i = 0; i < getHandledNumber(); i++)
		{
			if (!getCollidable(i).makeSolid())
				returnvalue = false;
		}
		// Returns whether all of the objects were made solid
		return returnvalue;
	}

	@Override
	public boolean makeUnsolid()
	{
		boolean returnvalue = true;
		// Tries to make all of the collidables solid
		for (int i = 0; i < getHandledNumber(); i++)
		{
			if (!getCollidable(i).makeUnsolid())
				returnvalue = false;
		}
		// Returns whether all of the objects were made solid
		return returnvalue;
	}
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return Collidable.class;
	}
	
	
	// OTHER METHODS	-----------------------------------------------------
	
	/**
	 * Returns a collidable from the list of collidables
	 *
	 * @param index The index of the collidable
	 * @return The collidable from the list (or null if no collidable was found 
	 * from the index)
	 */
	protected Collidable getCollidable(int index)
	{
		Handled maybecollidable = getHandled(index);
		// Casts the handled to collidable
		if (maybecollidable instanceof Collidable)
			return (Collidable) maybecollidable;
		else
			return null;
	}
	
	/**
	 * Adds a new collidable to the list of collidables
	 *
	 * @param c The collidable to be added
	 */
	public void addCollidable(Collidable c)
	{
		addHandled(c);
	}

	@Override
	public Collidable pointCollides(int x, int y)
	{
		// Removes dead collidables
		removeDeadHandleds();
		
		// Returns true if any object collides with the point
		for (int i = 0; i < getHandledNumber(); i++)
		{
			Collidable c = getCollidable(i);
			
			// Non-solid objects can't collide
			if (!c.isSolid())
				continue;
			
			// Checks the collision
			Collidable c2 = c.pointCollides(x, y);
			if (c2 != null)
				return c2;
		}
		
		return null;
	}
}
