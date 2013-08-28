package arcane_arcade_field;

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
		// Removes all the former balls just to be sure
		removeAllHandleds();
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Kills the object (and the balls)
		kill();
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
		// Removes dead balls
		removeDeadHandleds();
		
		// Forms a table containing all balls
		Ball[] balls = new Ball[getHandledNumber()];
		for (int i = 0; i < getHandledNumber(); i++)
			balls[i] = getBall(i);
		
		return balls;
	}
	
	/**
	 * @return are there any balls on the field
	 */
	public boolean thereAreBalls()
	{
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
		// Removes dead balls
		removeDeadHandleds();
		
		// If there are no balls, returns nothing
		if (!thereAreBalls())
			return null;
		// If there's just one ball, returns that
		else if (getHandledNumber() == 1)
			return getBall(0);
		
		// Otherwise finds the closest one
		Ball closestball = getBall(0);
		double smallestdist = HelpMath.pointDistance(x, y, closestball.getX(), 
				closestball.getY());
		for (int i = 1; i < getHandledNumber(); i++)
		{
			Ball newball = getBall(i);
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
	
	private Ball getBall(int index)
	{
		Handled maybeball = getHandled(index);
		if (maybeball instanceof Ball)
			return (Ball) maybeball;
		else
			return null;
	}
}
