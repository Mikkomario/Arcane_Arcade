package handleds;


/**
 * All handled in the logic thread are Logicalhandleds and each of them can 
 * be activated and inactivated if needed.
 *
 * @author Mikko Hilpinen.
 *         Created 8.12.2012.
 */
public interface LogicalHandled extends Handled
{
	/**
	 * @return Is the object currently interested in logical events
	 */
	public boolean isActive();
	
	/**
	 * Tries to restart the objects logical processes
	 * @return Was the object made active
	 */
	public boolean activate();
	
	/**
	 * Tries to stop the objects logical processes
	 * @return Was the object made inactive
	 */
	public boolean inactivate();
}
