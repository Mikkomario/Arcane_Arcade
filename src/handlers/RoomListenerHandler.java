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
	// ATTRIBUTES	------------------------------------------------------
	
	private RoomEvent lastevent;
	private Room lastroom;
	
	
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
		
		// Initializes attributes
		this.lastevent = RoomEvent.START;
		this.lastroom = null;
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
		informRoomEvent(RoomEvent.START, room);
	}

	@Override
	public void onRoomEnd(Room room)
	{
		informRoomEvent(RoomEvent.END, room);
	}
	
	@Override
	protected void handleObject(Handled h)
	{	
		// Informs the listener about the active event
		RoomListener listener = (RoomListener) h;
		
		if (this.lastevent == RoomEvent.START)
			listener.onRoomStart(this.lastroom);
		// TODO: Room end can cause the game to freeze?
		else if (this.lastevent == RoomEvent.END)
			listener.onRoomEnd(this.lastroom);
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
	
	private void informRoomEvent(RoomEvent event, Room room)
	{
		// Updates the information
		this.lastevent = event;
		this.lastroom = room;
		
		//System.out.println("Informs room event " + event.toString());
		// Informs the listener
		handleObjects();
		
		// Releases the information
		this.lastroom = null;
	}
	
	
	// ENUMERATIONS	------------------------------------------------------
	
	private enum RoomEvent
	{
		START, END;
	}
}
