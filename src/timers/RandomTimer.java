package timers;

import java.util.Random;

import handlers.ActorHandler;
import listeners.TimerEventListener;

/**
 * RandomTimer works like a ContinuousTimer except that it uses random intervals 
 * instead of static intervals. The user of the timer will be informed at 
 * varying intervals
 *
 * @author Mikko Hilpinen.
 *         Created 30.11.2013.
 */
public class RandomTimer extends AbstractTimer
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Random rand;
	private int minimun, maximum;
	
	
	// CONSTRUCTOR	-----------------------------------------------------

	/**
	 * Creates a new RandomTimer that will start to inform the user about timer 
	 * events at intervals randomized between the given values.
	 *
	 * @param user The timer listener that will be informed about the events
	 * @param mindelay How long is the shortest possible delay before an event 
	 * is thrown (in steps)
	 * @param maxdelay How long is the longest possible delay before an event 
	 * is thrown (in steps)
	 * @param actorhandler The actorhandler that will inform the timer about 
	 * steps
	 */
	public RandomTimer(TimerEventListener user, int mindelay, int maxdelay,
			ActorHandler actorhandler)
	{
		super(user, 100, actorhandler);

		// Initializes attributes
		this.rand = new Random();
		this.minimun = mindelay;
		this.maximum = maxdelay;
		
		// Randomizes the delay
		setDelay(getRandomDelay());
	}

	@Override
	public void onTimerEvent()
	{
		// Randomizes the delay
		setDelay(getRandomDelay());
	}
	
	
	// OTHER METHODS	--------------------------------------------------

	private int getRandomDelay()
	{
		return this.minimun + this.rand.nextInt(this.maximum - 
				this.minimun + 1);
	}
}
