package video;

/**
 * Screendrawer redraws the screen when needed and it works in its own thread.
 *
 * @author Mikko Hilpinen.
 *         Created 30.8.2013.
 */
public class ScreenDrawer implements Runnable
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private GameWindow window;
	//private long lastdraw;
	//private boolean needsupdating;
	//private int drawdelay;
	private boolean running;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new screendrawer that will draw the given window after 
	 * added to a thread.
	 *
	 * @param window
	 * @param drawdelay
	 * @see Thread
	 */
	public ScreenDrawer(GameWindow window, int drawdelay)
	{
		// Initializes attributes
		this.window = window;
		//this.lastdraw = 0;
		//this.needsupdating = true;
		//this.drawdelay = drawdelay;
		this.running = false;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public void run()
	{
		// TODO: It is this thread that causes very high CPU-usage!
		this.running = true;
		
		// Draws the screen until stopped
		while (isRunning())
			draw();
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Makes the screendrawer update the screen
	 */
	public void callUpdate()
	{
		//this.needsupdating = true;
		synchronized (this)
		{
			notify();
		}
	}
	
	/**
	 * Stops the drawer from no longer drawing the screen
	 */
	public void stop()
	{
		this.running = false;
	}
	
	/**
	 * @return Is the drawer currently trying to draw stuff
	 */
	public boolean isRunning()
	{
		return this.running;
	}
	
	private void draw()
	{
		// Doesn't draw anything if it doesn't need to
		/*
		if (!this.needsupdating)
			return;
		
		// Only draws the screen at certain intervals
		if (System.currentTimeMillis() < this.lastdraw + this.drawdelay)
			return;
		 */
			
		// TODO: Even without the window repaint, this method is way too heavy
		
		//this.needsupdating = false;
		//this.lastdraw = System.currentTimeMillis();
		// Draws the window and starts waiting for the next order
		this.window.repaint();
		try
		{
			synchronized (this)
			{
				wait();
			}
		}
		catch (InterruptedException exception)
		{
			System.out.println("The drawing thread's wait was interrupted");
			exception.printStackTrace();
		}
	}
}
