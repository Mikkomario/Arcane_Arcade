package handlers;

import video.GameWindow;


/**
 * This class calculates millisconds and calls all actors when a certain number 
 * of milliseconds has passed. All of the actors should be under the command of 
 * this object. This object doesn't stop functioning by itself if it runs out 
 * of actors.<p>
 *
 * @author Mikko Hilpinen.
 *         Created 29.11.2012.
 */
public class StepHandler extends ActorHandler implements Runnable
{
	// ATTRIBUTES	-------------------------------------------------------
	
	private int stepduration;
	private long lastMillis;
	private boolean running;
	private GameWindow window;
	
	
	// CONSTRUCTOR	-------------------------------------------------------
	
	/**
	 * This creates a new stephandler. Actors are informed 
	 * when a certain number of milliseconds has passed. Actors can be 
	 * added using addActor method.
	 * 
	 * @param stepDuration How long does a single step last in milliseconds.
	 * In other words, how often are the actors updated.
	 * @param window The which which created the stepHandler
	 * @see #addActor(handleds.Actor)
	 */
	public StepHandler(int stepDuration, GameWindow window)
	{
		super(false, null); // Stephandler doesn't have a superhandler
		this.stepduration = stepDuration;
		
		this.lastMillis = 0;
		this.running = false;
		this.window = window;
	}
	
	
	// IMPLEMENTED METHODS	-----------------------------------------------

	@Override
	public void run()
	{
		this.running = true;
		
		// Starts counting steps and does it until the object is killed
		while (this.running)
			update();
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	// This method updates the actors and the window when needed
	private void update()
	{
		// Checks the current millis and performs a "step" if needed
		if (Math.abs(System.currentTimeMillis() - this.lastMillis) > 
			this.stepduration)
		{
			// Calls all actors
			if (!isDead())
			{
				act();
				
				// Updates the game according to the changes
				this.window.callScreenUpdate();
				this.window.callMousePositionUpdate();
			}
			// Stops running if dies
			else
				this.running = false;
			
			// Remembers the time
			this.lastMillis = System.currentTimeMillis();
		}
	}
}
