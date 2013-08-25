package listeners;

import java.util.ArrayList;

import handleds.Collidable;
import handleds.LogicalHandled;
import helpAndEnums.DoublePoint;

/**
 * Collisionlisteners are interested in collisions and react to them somehow. 
 * Each collisionlistener provides a set of collision points it listens to.
 *
 * @author Mikko Hilpinen.
 *         Created 18.6.2013.
 */
public interface CollisionListener extends LogicalHandled
{
	/**
	 * @return The points which are used in the collision tests. Larger tables 
	 * are a lot more precise but also much slower. The points should be 
	 * absolute in-game pixels
	 */
	public DoublePoint[] getCollisionPoints();
	
	/**
	 * This method is called each time the listening object collides with 
	 * an object
	 * @param colpoints The points in which the collision(s) happened (absolute)
	 * @param collided The object with which the collision(s) happened
	 */
	public void onCollision(ArrayList<DoublePoint> colpoints, Collidable collided);
}
