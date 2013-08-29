package common;

import java.util.ArrayList;

/**
 * Each object which implements this interface will be able to receive commands
 * in the form of an ArrayList<String>.
 *
 * @author Unto Solala
 * 			Created 29.8.2013
 */
public interface OpenBank {
	
	/**Each object which implements OpenBank is able to receive an ArrayList of
	 * commands.
	 * 
	 * @param commands	The list of commands to the object.
	 */
	//public void setCommands(ArrayList<String> commands);
	
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
