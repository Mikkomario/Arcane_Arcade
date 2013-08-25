package handleds;

/**
 * Collidable objects can collide with each other. They can also be made unsolid 
 * so that collision detection will be ignored.
 *
 * @author Mikko Hilpinen.
 *         Created 18.6.2013.
 */
public interface Collidable extends Handled
{
	/**
	 * Checks whether a point collides with the object
	 * @param x The x-coordinate of the point (absolute pixel)
	 * @param y The y-coordinate of the point (absolute pixel)
	 * @return The collidable that collides with the object (null if no object 
	 * collides)
	 */
	public Collidable pointCollides(int x, int y);
	
	/**
	 * @return Can the object be collided with at this time
	 */
	public boolean isSolid();
	
	/**
	 * Tries to make the object solid so that the objects will collide with it
	 * @return Was the object made solid
	 */
	public boolean makeSolid();
	
	/**
	 * Tries to make the object unsolid so that no object can collide with it
	 *
	 * @return Was the object successfully made unsolid
	 */
	public boolean makeUnsolid();
}
