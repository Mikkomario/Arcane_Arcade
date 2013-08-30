package handlers;

import java.awt.Graphics2D;

import handleds.Drawable;
import handleds.Handled;

/**
 * The object from this class will draw multiple drawables, calling their 
 * drawSelf-methods and removing them when necessary
 *
 * @author Mikko Hilpinen.
 *         Created 27.11.2012.
 */
public class DrawableHandler extends Handler implements Drawable
{	
	// ATTRIBUTES	------------------------------------------------------
	
	private int depth;
	private boolean usesDepth;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new drawablehandler. Drawables must be added later manually.
	 *
	 * @param autodeath Will the handler die if it has no living drawables to handle
	 * @param usesDepth Will the handler draw the objects in a depth-specific order
	 * @param depth How 'deep' the objects in this handler are drawn
	 * @param superhandler The drawablehandler that will draw this handler (optional)
	 * @see depthConstants
	 */
	public DrawableHandler(boolean autodeath, boolean usesDepth, int depth, 
			DrawableHandler superhandler)
	{
		super(autodeath, superhandler);
		
		// Initializes attributes
		this.depth = depth;
		this.usesDepth = usesDepth;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void drawSelf(Graphics2D g2d)
	{
		// Removes any dead handleds
		removeDeadHandleds();
		
		// This calls for all active actor's act method
		for (int i = 0; i < getHandledNumber(); i++)
		{
			if (getDrawable(i).isVisible())
				getDrawable(i).drawSelf(g2d);
		}
	}

	@Override
	public boolean isVisible()
	{
		// Returns false only if all the handleds are invisible
		for (int i = 0; i < getHandledNumber(); i++)
		{
			if (getDrawable(i).isVisible())
				return true;
		}
		
		return false;
	}

	@Override
	public boolean setVisible()
	{
		// tries to set all the drawables visible, returns false if all the drawables
		// couldn't be made visible
		boolean returnValue = true;
		
		for (int i = 0; i < getHandledNumber(); i++)
		{
			if (!getDrawable(i).setVisible())
				returnValue = false;
		}
		
		return returnValue;
	}

	@Override
	public boolean setInvisible()
	{
		// tries to set all the drawables invisible, returns false if all the drawables
		// couldn't be made invisible
		boolean returnValue = true;
		
		for (int i = 0; i < getHandledNumber(); i++)
		{
			if (!getDrawable(i).setInvisible())
				returnValue = false;
		}
		
		return returnValue;
	}
	
	@Override
	public int getDepth()
	{
		return this.depth;
	}
	
	@Override
	public boolean setDepth(int depth)
	{
		this.depth = depth;
		return true;
	}
	
	@Override
	protected void addHandled(Handled h)
	{
		// Can only add drawables
		if (!(h instanceof Drawable))
			return;
		
		/*
		// Prints all depths in order
		System.out.println("*************************************");
		for (int i = 0; i < getHandledNumber(); i++)
		{
			System.out.println("Depth " + i + "/" + getHandledNumber() + ": " + 
					getDrawable(i).getDepth());
		}
		*/
		
		Drawable d = (Drawable) h;
		
		// If the depth sorting is on, finds the spot for the object
		if (this.usesDepth)
		{
			int index = 0;
			int newdepth = d.getDepth();
			
			while (index < getHandledNumber() - 1)
			{
				// TODO: Why is the index always 0?
				
				// Checks if there's an object with a higher depth
				if (getDrawable(index).getDepth() < newdepth)
				{
					super.insertHandled(d, index);
					return;
				}
				else
					index ++;
			}
			// If no object with a higher depth was found, simply adds the 
			// drawable to the end of the list as usual
		}
		
		super.addHandled(d);
	}
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return Drawable.class;
	}
	
	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 *Adds the given drawable to the handled drawables
	 *
	 * @param d The drawable to be added
	 */
	public void addDrawable(Drawable d)
	{
		addHandled(d);
	}
	
	/**
	 * Gets a handled from the list of handleds casted as a drawable
	 *
	 * @param index The index of the drawable
	 * @return The drawable from the index (or null if no drawable was found 
	 * from the index)
	 */
	private Drawable getDrawable(int index)
	{
		Handled maybeDrawable = getHandled(index);
		if (maybeDrawable instanceof Drawable)
			return (Drawable) maybeDrawable;
		else
			return null;
	}
}
