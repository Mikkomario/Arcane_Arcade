package timers;

import handlers.ActorHandler;
import listeners.TimerEventListener;

/**
 * SingularTimer is a timer that causes only a single TimerEvent and then dies.
 *
 * @author Mikko Hilpinen.
 *         Created 30.11.2013.
 */
public class SingularTimer extends AbstractTimer
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new timer that will inform the user about an timer event after 
	 * <i>delay</i> steps.
	 *
	 * @param user The timer listener that will be informed about the timerevent
	 * @param delay How many steps will pass before the event is thrown
	 * @param actorhandler The actorhandler that will inform the object about 
	 * steps
	 */
	public SingularTimer(TimerEventListener user, int delay,
			ActorHandler actorhandler)
	{
		super(user, delay, actorhandler);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onTimerEvent()
	{
		// SingularTimer can only be used once
		kill();
	}
}
