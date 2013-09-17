package handlers;

import java.util.Iterator;

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
	// ATTRIBUTES	-----------------------------------------------------
	
	private int lasttestx, lasttesty;
	private Collidable lastcollided;
	
	
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
		
		// Initializes attributes
		this.lasttestx = 0;
		this.lasttesty = 0;
		this.lastcollided = null;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public boolean isSolid()
	{
		// Handler is solid if any of the objects are solid
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			Collidable c = (Collidable) iterator.next();
			
			if (c.isSolid())
				return true;
		}
		
		return false;
	}

	@Override
	public void makeSolid()
	{
		// Tries to make all of the collidables solid
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			Collidable c = (Collidable) iterator.next();
			c.makeSolid();
		}
	}

	@Override
	public void makeUnsolid()
	{
		// Tries to make all of the collidables solid
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			Collidable c = (Collidable) iterator.next();
			c.makeUnsolid();
		}
	}
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return Collidable.class;
	}
	
	@Override
	protected void handleObject(Handled h)
	{
		// Checks the collision for the object and updates the collided attribute
		Collidable c = (Collidable) h;
		
		// Non-solid objects can't collide
		if (!c.isSolid())
			return;
		
		// Checks the collision
		Collidable c2 = c.pointCollides(this.lasttestx, this.lasttesty);
		if (c2 != null && this.lastcollided == null)
			this.lastcollided = c2;
	}
	
	
	// OTHER METHODS	-----------------------------------------------------
	
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
		this.lasttestx = 0;
		this.lasttesty = 0;
		this.lastcollided = null;
		
		// Checks collisions through all collidables
		handleObjects();
		
		// Doesn't hold the object in an attribute anymore
		Collidable returnvalue = this.lastcollided;
		this.lastcollided = null;
		
		return returnvalue;
	}
}
