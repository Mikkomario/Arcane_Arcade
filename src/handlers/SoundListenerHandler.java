package handlers;

import sound.Sound;
import handleds.Handled;
import listeners.SoundListener;

/**
 * Soundlistenerhandler informs multiple listeners about sound events of sounds 
 * it listens to
 *
 * @author Mikko Hilpinen.
 *         Created 19.8.2013.
 */
public class SoundListenerHandler extends LogicalHandler implements SoundListener
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new soundlistenerhandler and adds it to the given handler (if 
	 * possible)
	 *
	 * @param autodeath Will the handler automatically die when it runs out 
	 * of handleds
	 * @param superhandler The soundhandler that will handle the handler
	 */
	public SoundListenerHandler(boolean autodeath, SoundListenerHandler superhandler)
	{
		super(autodeath, superhandler);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected Class<?> getSupportedClass()
	{
		return SoundListener.class;
	}

	@Override
	public void onSoundStart(Sound source)
	{
		// Cleans unnecessary handleds
		removeDeadHandleds();
		
		// Informs all the listeners about the event
		for (int i = 0; i < getHandledNumber(); i++)
		{
			SoundListener s = getListener(i);
			if (s.isActive())
				s.onSoundStart(source);
		}
	}

	@Override
	public void onSoundEnd(Sound source)
	{
		// Cleans unnecessary handleds
		removeDeadHandleds();
		
		// Informs all the listeners about the event
		for (int i = 0; i < getHandledNumber(); i++)
		{
			SoundListener s = getListener(i);
			if (s.isActive())
				s.onSoundEnd(source);
		}
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	private SoundListener getListener(int index)
	{
		Handled maybelistener = getHandled(index);
		
		if (maybelistener instanceof SoundListener)
			return (SoundListener) maybelistener;
		else
			return null;
	}
	
	/**
	 * Adds a new listener to the informed listeners
	 *
	 * @param s The listener to be informed
	 */
	public void addListener(SoundListener s)
	{
		addHandled(s);
	}
}
