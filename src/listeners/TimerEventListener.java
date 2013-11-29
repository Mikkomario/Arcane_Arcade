package listeners;

import handleds.LogicalHandled;

/**
 * TimerEventListeners react to events caused by timers. They often create 
 * and use Timers as well
 *
 * @author Mikko Hilpinen.
 *         Created 30.11.2013.
 */
public interface TimerEventListener extends LogicalHandled
{
	/**
	 * This method is called by a Timer at certain periods.
	 */
	public void onTimerEvent();
}
