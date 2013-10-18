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
	private Graphics2D lastg2d;
	private HandlingOperation currentoperation;
	private int insertindex, insertdepth;
	
	
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
		this.lastg2d = null;
		this.currentoperation = null;
		this.insertdepth = 0;
		this.insertindex = 0;
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public void drawSelf(Graphics2D g2d)
	{
		this.lastg2d = g2d;
		this.currentoperation = HandlingOperation.DRAW;
		handleObjects();
	}

	@Override
	public boolean isVisible()
	{
		// Updates the handler status before checking it
		updateStatus();
		
		// Returns false only if all the handleds are invisible
		for (int i = 0; i < getHandledNumber(); i++)
		{
			Drawable d = (Drawable) getHandled(i);
			
			if (d.isVisible())
				return true;
		}
		
		return false;
	}

	@Override
	public void setVisible()
	{
		// tries to set all the drawables visible
		this.currentoperation = HandlingOperation.VISIBILITY;
		handleObjects();
		/*
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			Drawable d = (Drawable) iterator.next();
			d.setVisible();
		}
		*/
	}

	@Override
	public void setInvisible()
	{
		// tries to set all the drawables invisible
		this.currentoperation = HandlingOperation.INVISIBILITY;
		handleObjects();
		/*
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			Drawable d = (Drawable) iterator.next();
			d.setInvisible();
		}
		*/
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
		
		Drawable d = (Drawable) h;
		
		// If the depth sorting is on, finds the spot for the object
		if (this.usesDepth)
		{
			// Repairs any problems in the depth sorting
			checkDepthSorting();
			
			int index = 0;
			int newdepth = d.getDepth();
			
			// TODO: This is not safe and not valid if the list changes 
			// during the process
			for (int i = 0; i < getHandledNumber(); i++)
			{
				Drawable other = (Drawable) getHandled(i);
				
				// Checks if there's an object with a higher depth
				if (other.getDepth() < newdepth)
				{
					insertHandled(d, index);
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
	
	@Override
	protected boolean handleObject(Handled h)
	{
		// Draws the visible object
		Drawable d = (Drawable) h;
		if (d.isVisible())
			d.drawSelf(this.lastg2d);
		
		return true;
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
	
	// Checks if there are errors in current depth sorting and tries to fix them
	private void checkDepthSorting()
	{
		int lastdepth = 999999;

		for (int i = 0; i < getHandledNumber(); i++)
		{
			Drawable drawable = (Drawable) getHandled(i);
			if (drawable.getDepth() > lastdepth)
			{
				//System.out.println("Fixes depth: " + drawable.getDepth() + " / " + lastdepth);
				tryFixingDepthSorting(drawable);
				break;
			}
			lastdepth = drawable.getDepth();
		}
	}
	
	// Tries to fix the depth sorting for the given object
	private void tryFixingDepthSorting(Drawable mistake)
	{
		// TODO: Updatestatus causes concurrentmodification exception
		// TODO: Besides, why do we have to do this so often in the first place?
		//System.out.println("Tries to fix an error in the depth sorting");
		removeHandled(mistake);
		updateStatus();
		addHandled(mistake);
	}
	
	
	// ENUMERATIONS	------------------------------------------------------
	
	// Handlingoperations represent a way to handle a drawable
	private enum HandlingOperation
	{
		DRAW, DEPTHSORT, VISIBILITY, INVISIBILITY;
	}
}
