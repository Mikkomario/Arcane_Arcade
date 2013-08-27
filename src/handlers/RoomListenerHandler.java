package handlers;

import handleds.Handled;
import worlds.Room;
import listeners.RoomListener;

/**
 * A roomlistenerhandler handles a group of roomlisteners and informs them 
 * about the events it receives
 *
 * @author Mikko Hilpinen.
 *         Created 11.7.2013.
 */
public class RoomListenerHandler extends Handler implements RoomListener
{
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new roomlistenerhandler with the given information
	 *
	 * @param autodeath Will the handler die when it runs out of handleds
	 * @param superhandler The roomlistenerhandler that informs this 
	 * roomlistenerhandler about room events (optional)
	 */
	public RoomListenerHandler(boolean autodeath, RoomListenerHandler superhandler)
	{
		super(autodeath, superhandler);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	protected Class<?> getSupportedClass()
	{
		return RoomListener.class;
	}

	@Override
	public void onRoomStart(Room room)
	{
		// Cleans unnecessary handleds
		removeDeadHandleds();
		
		// Informs all the listeners about the event
		for (int i = 0; i < getHandledNumber(); i++)
		{
			getListener(i).onRoomStart(room);
		}
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Cleans unnecessary handleds
		removeDeadHandleds();
		
		// Informs all the listeners about the event
		for (int i = 0; i < getHandledNumber(); i++)
		{
			getListener(i).onRoomEnd(room);
		}
	}

	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Adds a new listener to the informed listeners
	 *
	 * @param r The new listener to be added
	 */
	public void addRoomListener(RoomListener r)
	{
		addHandled(r);
	}
	
	private RoomListener getListener(int index)
	{
		Handled maybelistener = getHandled(index);
		
		if (maybelistener instanceof RoomListener)
			return (RoomListener) maybelistener;
		else
			return null;
	}
}
