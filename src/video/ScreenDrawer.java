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
	private long lastdraw;
	private boolean needsupdating;
	private int drawdelay;
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
		this.lastdraw = 0;
		this.needsupdating = true;
		this.drawdelay = drawdelay;
		this.running = false;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public void run()
	{
		this.running = true;
		
		// Draws the screen until stopped
		while (this.running)
			draw();
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Makes the screendrawer update the screen
	 */
	public void callUpdate()
	{
		this.needsupdating = true;
	}
	
	/**
	 * Stops the drawer from no longer drawing the screen
	 */
	public void stop()
	{
		this.running = false;
	}
	
	private void draw()
	{
		// Doesn't draw anything if it doesn't need to
		if (!this.needsupdating)
			return;
		
		// Only draws the screen at certain intervals
		if (System.currentTimeMillis() < this.lastdraw + this.drawdelay)
			return;
			
		this.needsupdating = false;
		this.lastdraw = System.currentTimeMillis();
		this.window.repaint();
	}
}
