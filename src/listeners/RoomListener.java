package listeners;

import worlds.Room;
import handleds.LogicalHandled;

/**
 * Roomlisteners react to the start and / or end of the room they are listening 
 * to.
 *
 * @author Mikko Hilpinen.
 *         Created 11.7.2013.
 * @see worlds.Room
 */
public interface RoomListener extends LogicalHandled
{
	/**
	 * This method is called each time a room the object listens to starts
	 *
	 * @param room The room that just started
	 */
	public void onRoomStart(Room room);
	
	/**
	 * This method is called each time a room the object listens to ends
	 *
	 * @param room The room that just ended
	 */
	public void onRoomEnd(Room room);
}
