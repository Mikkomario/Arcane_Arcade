package handlers;

import graphic.SpriteDrawer;
import handleds.Handled;
import listeners.AnimationListener;

/**
 * Animationlistenerhandler informs numerous animationlisteners about animation 
 * events
 *
 * @author Mikko Hilpinen.
 *         Created 28.8.2013.
 * @see graphic.SpriteDrawer
 */
public class AnimationListenerHandler extends LogicalHandler implements 
		AnimationListener
{
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new empty animationlistenerhandler with the given information
	 *
	 * @param autodeath Will the handler die when it runs out of listeners
	 * @param superhandler The animationlistenerhandler that will inform 
	 * the handler about animation events (optional)
	 */
	public AnimationListenerHandler(boolean autodeath,
			AnimationListenerHandler superhandler)
	{
		super(autodeath, superhandler);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected Class<?> getSupportedClass()
	{
		return AnimationListener.class;
	}

	@Override
	public void onAnimationEnd(SpriteDrawer spritedrawer)
	{
		// Kills all dead listeners
		removeDeadHandleds();
		
		// Informs all active listeners
		for (int i = 0; i < getHandledNumber(); i++)
		{
			AnimationListener l = getListener(i);
			
			if (l.isActive())
				l.onAnimationEnd(spritedrawer);
		}
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Adds a new animationlistener to the informed listeners
	 *
	 * @param l The animationlistener added
	 */
	public void addAnimationListener(AnimationListener l)
	{
		addHandled(l);
	}
	
	private AnimationListener getListener(int index)
	{
		Handled maybelistener = getHandled(index);
		if (maybelistener instanceof AnimationListener)
			return (AnimationListener) maybelistener;
		else
			return null;
	}
}
