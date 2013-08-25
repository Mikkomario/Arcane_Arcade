package handlers;

import java.util.ArrayList;
import java.util.HashMap;

import listeners.CollisionListener;
import handleds.Actor;
import handleds.Collidable;
import handleds.Handled;
import helpAndEnums.DoublePoint;

/**
 * A handler that checks collisions between multiple collisionlisteners and 
 * Collidables
 *
 * @author Mikko Hilpinen.
 *         Created 18.6.2013.
 * @warning There might be some inaccuracies if CollidableHandlers are added 
 * to the collidables
 */
public class CollisionHandler extends LogicalHandler implements Actor
{
	// TODO: Add separate collisionListenerHandler class (difficult to implement 
	// since the class needs to access the listeners directly and not through 
	// a handler)
	// TODO: If the collidablehandler contains collidablehandlers there will 
	// be problems
	
	// ATTRIBUTES	-----------------------------------------------------
	
	private CollidableHandler collidablehandler;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new empty collisionhandler
	 *
	 * @param autodeath Will the handler die when it runs out of listeners
	 * @param superhandler Which actorhandler will inform the handler about 
	 * the act-event (Optional)
	 */
	public CollisionHandler(boolean autodeath, ActorHandler superhandler)
	{
		super(autodeath, superhandler);
		
		// Initializes attributes
		this.collidablehandler = new CollidableHandler(false, null);
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	public void act()
	{
		// Removes the dead handleds
		removeDeadHandleds();
		
		// Checks collisions between all the listeners and collidables
		for (int listenerind = 0; listenerind < getHandledNumber(); listenerind++)
		{
			// Remembers the important data about the listener
			CollisionListener listener = getCollisionListener(listenerind);
			
			// Inactive listeners are not counted
			if (!listener.isActive())
				continue;
			
			DoublePoint[] colpoints = listener.getCollisionPoints();
			HashMap<Collidable, ArrayList<DoublePoint>> collidedpoints = 
					new HashMap<Collidable, ArrayList<DoublePoint>>();
			
			for (int colind = 0; 
					colind < this.collidablehandler.getHandledNumber(); colind++)
			{
				// Remembers the collidable
				Collidable c = this.collidablehandler.getCollidable(colind);
				
				// Non-solid collidables cannot collide
				if (!c.isSolid())
					continue;
				
				// Listener cannot collide with itself
				if (listener.equals(c))
					continue;
				
				// Checks all points if they would collide
				for (int pointi = 0; pointi < colpoints.length; pointi++)
				{
					Collidable collider = c.pointCollides((int) colpoints[pointi].getX(), 
							(int) colpoints[pointi].getY());
					
					if (collider == null)
						continue;

					// The arraylist may need to be initialized
					if (!collidedpoints.containsKey(collider))
						collidedpoints.put(collider, new ArrayList<DoublePoint>());
					// Remembers the point and the collided object
					collidedpoints.get(collider).add(colpoints[pointi]);
				}
			}
			
			// Informs the listener about each object it collided with
			for (Collidable c: collidedpoints.keySet())
				listener.onCollision(collidedpoints.get(c), c);
		}
	}
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return CollisionListener.class;
	}
	
	@Override
	public boolean kill()
	{
		// In addition to the normal killing, kills the collidablehandler as well
		getCollidableHandler().kill();
		return super.kill();
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The collidablehandler that handles the collisionhandler's 
	 * collision checking
	 */
	public CollidableHandler getCollidableHandler()
	{
		return this.collidablehandler;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Adds a new collisionlistener to the checked listeners
	 *
	 * @param c The new collisionlistener
	 */
	public void addCollisionListener(CollisionListener c)
	{
		super.addHandled(c);
	}
	
	/**
	 * Adds a new collidable to the list of checkked collidables
	 *
	 * @param c The collidable to be added
	 */
	public void addCollidable(Collidable c)
	{
		this.collidablehandler.addCollidable(c);
	}
	
	private CollisionListener getCollisionListener(int index)
	{
		Handled maybeListener = getHandled(index);
		
		if (maybeListener instanceof CollisionListener)
			return (CollisionListener) maybeListener;
		else
			return null;
	}
}
