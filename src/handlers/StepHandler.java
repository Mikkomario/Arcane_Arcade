package handlers;

import handleds.Actor;
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
	//private double actionmodifier;
	
	
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
		
		// Initializes attributes
		this.stepduration = stepDuration;
		this.lastMillis = 0;
		this.running = false;
		this.window = window;
		//this.actionmodifier = 1;
		
		// Creates an ApsOptimizer and adds it to the actors
		addActor(new ApsOptimizer(this.stepduration, 8, 4000, 20000, 6));
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
	
	/**
	 * Stops the stephandler from functioning anymore
	 */
	public void stop()
	{
		this.running = false;
	}
	
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
	
	
	// SUBCLASSES	------------------------------------------------------
	
	// ApsOptimizer optimizes the aps to get virtually as close to the 
	// optimal aps as possible
	private class ApsOptimizer implements Actor
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private int optimalsteptime;
		private int optimalaps;
		private int maxstepsizeadjustment;
		private int currentstepsizeadjustments;
		private int checkphase;
		private int checkdelay;
		private int breaklength;
		private long lastcheck;
		private boolean onbreak;
		private boolean dead;
		private int aps;
		private int actions;
		private long lastmillis;
		private int lastapsdifference;
		private int stepsizeadjuster;
		private int maxoptimizations;
		private int optimizationsdone;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		/**
		 * Creates a new apsOptimizer with the given information. The optimizer 
		 * will start to optimize the aps after a small delay
		 *
		 * @param startsteptime How long is the steptime in the beginning 
		 * of the program (how long is the default step time, also defines the 
		 * optimal actions per second value)
		 * @param maxstepsizeadjustment How much the stepsize can be adjusted at 
		 * once (the larger value makes optimization more precise but also 
		 * makes it last longer)
		 * @param checkdelay How many milliseconds the optimizer calculates the 
		 * values before they are adjusted again (>= 1000)
		 * @param breaklength How many milliseconds the optimizer sleeps after 
		 * it has done its optimizing (> checkdelay)
		 * @param maximumoptimizations how many optimizations will be done 
		 * before the optimizer dies
		 */
		public ApsOptimizer(int startsteptime, int maxstepsizeadjustment, 
				int checkdelay, int breaklength, int maximumoptimizations)
		{
			// Initializes attributes
			this.optimalsteptime = startsteptime;
			this.optimalaps = 1000 / this.optimalsteptime;
			this.aps = this.optimalaps;
			this.maxstepsizeadjustment = maxstepsizeadjustment;
			this.checkdelay = checkdelay;
			this.breaklength = breaklength;
			this.checkphase = 0;
			this.lastcheck = System.currentTimeMillis();
			this.onbreak = false;
			this.dead = false;
			this.actions = 0;
			this.lastmillis = System.currentTimeMillis();
			this.lastapsdifference = 0;
			this.stepsizeadjuster = 0;
			this.currentstepsizeadjustments = 0;
			this.maxoptimizations = maximumoptimizations;
			this.optimizationsdone = 0;
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------
		
		@Override
		public boolean isActive()
		{
			// The optimizer is always active
			return true;
		}

		@Override
		public boolean activate()
		{
			return true;
		}

		@Override
		public boolean inactivate()
		{
			return false;
		}

		@Override
		public boolean isDead()
		{
			// The optimizer can only die while on break
			return this.dead && this.onbreak;
		}

		@Override
		public boolean kill()
		{
			this.dead = true;
			return true;
		}

		@Override
		public void act()
		{
			// Calculates the aps
			this.actions ++;
			
			if (System.currentTimeMillis() - this.lastmillis >= 1000)
			{
				this.aps = this.actions;
				this.actions = 0;
				this.lastmillis = System.currentTimeMillis();
			}
			
			// Optimizes (if needed)
			int delay = this.checkdelay;
			if (this.onbreak)
				delay = this.breaklength;
			// Checks if a new check is needed
			if (System.currentTimeMillis() - this.lastcheck >= delay)
				optimize();
		}
		
		private void optimize()
		{
			// Stops the break
			this.onbreak = false;
			
			// Calculates the current aps difference
			int currentapsdifference = Math.abs(this.aps - this.optimalaps);
			
			// Phase 0: Checks if the aps is optimal
			if (this.checkphase == 0)
			{
				// Of the aps is optimal goes to break
				if (currentapsdifference == 0)
					goToBreak();
				// Otherwise advances to the next phase (and updates the steptime)
				else
				{
					this.optimalsteptime = StepHandler.this.stepduration;
					if (this.aps < this.optimalaps)
						this.stepsizeadjuster = -1;
					else
						this.stepsizeadjuster = 1;
					this.checkphase ++;
					this.currentstepsizeadjustments = 0;
				}
			}
			// Phase 1: Slowly adjusts the steptime in hopes of adjusting 
			// the aps
			if (this.checkphase == 1)
			{
				// If the current apsdifference is larger than the last aps or 
				// If there have already been too many adjustments
				// difference, goes to the next phase
				if (currentapsdifference > this.lastapsdifference || 
						this.currentstepsizeadjustments >= 
						this.maxstepsizeadjustment)
				{
					goToBreak();
					StepHandler.this.stepduration = this.optimalsteptime;
				}
				// Otherwise further adjusts the stepduration
				else
				{
					// Checks if the current status is closer to the goal than 
					// the last and remembers it
					if (currentapsdifference < this.lastapsdifference)
						this.optimalsteptime = StepHandler.this.stepduration;
					
					// Increases / decreases the stepduration
					StepHandler.this.stepduration += this.stepsizeadjuster;
					this.currentstepsizeadjustments ++;
				}
			}
			
			// Remembers the last aps difference
			this.lastapsdifference = Math.abs(this.aps - this.optimalaps);
			// Updates the timer
			this.lastcheck = System.currentTimeMillis();
		}
		
		private void goToBreak()
		{
			// Updates the stephandler's action modifier
			//StepHandler.this.actionmodifier = this.optimalaps / 
			//		(double) this.aps;
			
			this.onbreak = true;
			this.checkphase = 0;
			this.optimizationsdone ++;
			
			// If the optimizer has done enough optimizations, it kills itself
			if (this.optimizationsdone >= this.maxoptimizations)
				kill();
		}
	}
}
