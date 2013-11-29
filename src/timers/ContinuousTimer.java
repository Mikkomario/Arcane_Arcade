package timers;

import handlers.ActorHandler;
import listeners.TimerEventListener;

/**
 * ContinuousTimer will inform the user about timer events constantly after 
 * certain intervals.
 *
 * @author Mikko Hilpinen.
 *         Created 30.11.2013.
 */
public class ContinuousTimer extends AbstractTimer
{
	// ATTRIBUTES	------------------------------------------------------
	
	private int interval;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new timer that will start to inform the user at given intervals
	 *
	 * @param user The timer listener that will be informed about the timer 
	 * events
	 * @param interval How many steps there are between each timer event
	 * @param actorhandler The actorhandler that will inform the timer about 
	 * steps (optional)
	 */
	public ContinuousTimer(TimerEventListener user, int interval,
			ActorHandler actorhandler)
	{
		super(user, interval, actorhandler);
		
		// Initializes attributes
		this.interval = interval;
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	public void onTimerEvent()
	{
		// Resets the timer
		delay(this.interval);
	}
}
