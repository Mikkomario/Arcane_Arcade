package handlers;

import java.util.ArrayList;

import handleds.Collidable;
import handleds.Handled;

/**
 * This class handles multiple collidables. Any collision checks made for this 
 * object are made for all the collidables.
 *
 * @author Mikko Hilpinen.
 *         Created 18.6.2013.
 */
public class CollidableHandler extends Handler
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private int lasttestx, lasttesty;
	private ArrayList<Collidable> collided;
	private HandlingAction lastaction;
	
	
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
		this.collided = null;
		this.lastaction = null;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return Collidable.class;
	}
	
	@Override
	protected boolean handleObject(Handled h)
	{
		// Checks the collision for the object and updates the collided 
		// attribute
		Collidable c = (Collidable) h;
		
		// If the action is set to collision check, does that
		if (this.lastaction == HandlingAction.COLLISIONCHECK)
		{
			// Non-solid objects can't collide
			if (!c.isSolid())
				return true;
			
			// Checks the collision
			if (!c.pointCollides(this.lasttestx, this.lasttesty))
				return true;
				
			// Adds the collided object to the list
			this.collided.add(c);
		}
		// Otherwise solifies or unsolifies the object
		else if (this.lastaction == HandlingAction.SOLID)
			c.makeSolid();
		else if (this.lastaction == HandlingAction.UNSOLID)
			c.makeUnsolid();
		
		return true;
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

	/**
	 * Returns all the collided objects the collidablehandler handles that 
	 * collide with the given point
	 *
	 * @param x The x-coordinate of the collision point
	 * @param y The y-coordinate of the collision point
	 * @return A list of objects colliding wiht the point or null if no object 
	 * collided with the point
	 */
	public ArrayList<Collidable> getCollidedObjectsAtPoint(int x, int y)
	{
		this.lasttestx = 0;
		this.lasttesty = 0;
		this.collided = new ArrayList<Collidable>();
		this.lastaction = HandlingAction.COLLISIONCHECK;
		
		// Checks collisions through all collidables
		handleObjects();
		
		// If there wasn't collided objects, forgets the data and returns null
		if (this.collided.isEmpty())
		{
			this.collided = null;
			return null;
		}
		
		// Doesn't hold the object in an attribute anymore
		@SuppressWarnings("unchecked")
		ArrayList<Collidable> returnvalue = 
				(ArrayList<Collidable>) this.collided.clone();
		this.collided = null;
		
		return returnvalue;
	}
	
	/**
	 * Goes through the collidables and makes them solid
	 */
	public void makeCollidablesSolid()
	{
		// Tries to make all of the collidables solid
		this.lastaction = HandlingAction.SOLID;
		handleObjects();
	}

	/**
	 * Goes through the collidables and makes the unsolid
	 */
	public void makeCollidablesUnsolid()
	{
		// Tries to make all of the collidables solid
		this.lastaction = HandlingAction.UNSOLID;
		handleObjects();
	}
	
	
	// ENUMERATIONS	------------------------------------------------------
	
	private enum HandlingAction
	{
		SOLID, UNSOLID, COLLISIONCHECK;
	}
}
