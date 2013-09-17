package arcane_arcade_field;

import java.util.Iterator;

import worlds.Room;
import listeners.RoomListener;
import handleds.Handled;
import handlers.Handler;
import helpAndEnums.HelpMath;

/**
 * BallRelay has information about the balls in the game and offers that to the 
 * other objects as well
 *
 * @author Mikko Hilpinen.
 *         Created 28.8.2013.
 */
public class BallRelay extends Handler implements RoomListener
{
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new empty BallRelay, the created balls should be added to the 
	 * relay.
	 * 
	 * @param room The room where the balls are created
	 * @see #addBall(Ball)
	 */
	public BallRelay(Room room)
	{
		super(false, null);
		
		// Adds the object to the room (if possible)
		if (room != null)
			room.addRoomListener(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	protected Class<?> getSupportedClass()
	{
		return Ball.class;
	}
	
	@Override
	public void onRoomStart(Room room)
	{
		// Doesn't do anything
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Kills the object (and the balls)
		kill();
	}
	
	@Override
	protected void handleObject(Handled h)
	{
		// Doesn't do anything since ball handling is done in separate methods
		// I know its not very cool
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Adds a new ball to the list of current balls. The ball will be 
	 * automatically removed when it dies
	 *
	 * @param ball The ball that was created
	 */
	public void addBall(Ball ball)
	{
		addHandled(ball);
	}
	
	/**
	 * @return A table containing all the balls currently in the field
	 */
	public Ball[] getBalls()
	{
		// Removes dead balls and handles them normally
		handleObjects();
		
		// Forms a table containing all balls
		Ball[] balls = new Ball[getHandledNumber()];
		
		Iterator<Handled> iterator = getIterator();
		int index = 0;
		while (iterator.hasNext())
		{
			balls[index] = (Ball) iterator.next();
			index ++;
		}
		
		return balls;
	}
	
	/**
	 * @return are there any balls on the field
	 */
	public boolean thereAreBalls()
	{
		// Goes through the balls and removes unnecessary ones to be sure
		handleObjects();
		
		return getHandledNumber() != 0;
	}
	
	/**
	 * Retuns a ball nearest to the given position (or null if there are no 
	 * balls on the field)
	 *
	 * @param x The x-coordiante of the search position
	 * @param y The y-coordinate of the search position
	 * @return The ball nearest to the given position (or null if no balls are 
	 * on the field)
	 */
	public Ball getNearestBall(int x, int y)
	{
		// Goes through the balls normally at first
		handleObjects();
		
		// If there are no balls, returns nothing
		if (!thereAreBalls())
			return null;
		// If there's just one ball, returns that
		else if (getHandledNumber() == 1)
			return (Ball) getFirstHandled();
		
		// Otherwise finds the closest one
		Ball closestball = (Ball) getFirstHandled();
		double smallestdist = HelpMath.pointDistance(x, y, closestball.getX(), 
				closestball.getY());
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			Ball newball = (Ball) iterator.next();
			double newdist = HelpMath.pointDistance(x, y, newball.getX(), 
					newball.getY());
			if (newdist < smallestdist)
			{
				smallestdist = newdist;
				closestball = newball;
			}
		}
		// Returns the ball found
		return closestball;
	}
}
