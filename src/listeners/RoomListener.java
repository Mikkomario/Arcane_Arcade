package listeners;

import worlds.Room;
import handleds.Handled;

/**
 * Roomlisteners react to the start and / or end of the room they are listening 
 * to.<br>Remember to add the object into the room either as a GameObject or 
 * as a listener.
 *
 * @author Mikko Hilpinen.
 *         Created 11.7.2013.
 * @see worlds.Room
 */
public interface RoomListener extends Handled
{
	/**
	 * This method is called each time a room the object listens to starts. 
	 * This method is called even if the object was inactive at the time.
	 *
	 * @param room The room that just started
	 */
	public void onRoomStart(Room room);
	
	/**
	 * This method is called each time a room the object listens to ends
	 * This method is called even if the object was inactive at the time.
	 *
	 * @param room The room that just ended
	 */
	public void onRoomEnd(Room room);
}
