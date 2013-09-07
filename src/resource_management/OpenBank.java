package resource_management;

/**
 * Each object which implements this interface will be able to receive commands
 * in the form of an ArrayList<String>.
 *
 * @author Unto Solala
 * 			Created 29.8.2013
 */
public interface OpenBank
{
	/**
	 * Uninitializes the contents of the bank. The bank will be reinitialized 
	 * when something is tried to retrieve from it
	 * 
	 * @warning calling this method while the objects in the bank are in use 
	 * may crash the program depending on the circumstances. It would be safe 
	 * to uninitialize the bank only when the content is not in use.
	 */
	public void uninitialize();
}
