package video;

import handlers.DrawableHandler;
import helpAndEnums.DepthConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Gamepanel is a single panel in the gamescreen that draws numerous drawables. 
 * Gamepanels are used in GameWindows
 * 
 * @author Unto Solala & Mikko Hilpinen. Created 8.8.2013
 * @see video.GameWindow
 */
public class GamePanel extends JPanel
{
	// ATTRIBUTES ---------------------------------------------------------
	
	private int width;
	private int height;
	private boolean needsUpdating;
	private DrawableHandler drawer;
	
	
	// CONSTRUCTOR ---------------------------------------------------------
	
	/**
	 * Creates a new panel with default color being white
	 * 
	 * @param width	Panel's width (in pixels)
	 * @param height Panel's height (in pixels)
	 */
	public GamePanel(int width, int height)
	{
		// Initializes attributes
		this.width = width;
		this.height = height;
		this.needsUpdating = true;
		this.drawer = new DrawableHandler(false, true, DepthConstants.NORMAL, 
				null);
		
		//Let's format our panel
		this.formatPanel();
		
		//And make it visible
		this.setVisible(true);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void paintComponent(Graphics g)
	{
		// The panel draws all stuff inside it (if needed)
		if (this.needsUpdating)
		{
			this.needsUpdating = false;
			Graphics2D g2d = (Graphics2D) g;
			
			// Clears the former drawings
			g2d.clearRect(0, 0, getWidth(), getHeight());
			
			this.drawer.drawSelf(g2d);
		}
	}
	
	
	// PRIVATE METHODS ---------------------------------------------------
	
	private void formatPanel()
	{
		//Let's set the panel's size...
		this.setSizes(this.width, this.height);
		//...And color
		this.setBackground(new Color(0,0,0));
	}
	
	
	// OTHER METHODS ---------------------------------------------------
	
	/**
	 * This method should be called when the screen needs redrawing
	 */
	public void callScreenUpdate()
	{
		this.needsUpdating = true;
	}
	
	/**
	 * Changes the size of the game panel.
	 * 
	 * @param width	Panel's new width (in pixels)
	 * @param height Panel's new height (in pixels)
	 */
	public void setSizes(int width, int height)
	{
		this.setSize(width, height);
		Dimension preferred = new Dimension(width, height);
		this.setPreferredSize(preferred);
		this.setMinimumSize(preferred);
		this.setMaximumSize(preferred);
	}
	
	/**
	 * Changes the panel's background color.
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 */
	public void setBackgroundColor(int red, int green, int blue)
	{
		this.setBackground(new Color(red, green, blue));
	}
	
	/**
	 * Makes the panel visible.
	 */
	public void makeVisible()
	{
		this.setVisible(true);
	}
	
	/**
	 * Makes the panel invisible.
	 */
	public void makeInvisible()
	{
		this.setVisible(false);
	}
	
	/**
	 * @return The drawablehandler that draws the content of this panel
	 */
	public DrawableHandler getDrawer()
	{
		return this.drawer;
	}
}
