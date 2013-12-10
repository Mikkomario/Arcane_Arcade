package resourcebanks;

import java.util.ArrayList;
import java.util.HashMap;

import fileio.FileReader;

/**
 * OpenBankHolder is an abstract class which creates and stores various OpenBanks
 * and gives access to them to it's subclasses.
 * 
 * @author Unto Solala & Mikko Hilpinen
 * 			Created 29.8.2013
 *
 */
public abstract class OpenBankHolder extends FileReader
{
	// ATTRIBUTES -----------------------------------------------------

	private HashMap<String, OpenBank> banks;
	private String lastbankname;
	private ArrayList<String> lastcommands;

	
	// CONSTRUCTOR -----------------------------------------------------

	/**
	 * Creates and initializes new OpenBankHolder. The content is loaded using
	 * the given file.
	 * 
	 * @param filename A file that shows information about what banks to create
	 * (data/ automatically included). The file should be written for sprites
	 * as follows:
	 * <p>
	 * &bankname<br>
	 * spritename#filename(data/ automatically included)#number
	 * of images# xorigin#yorigin<br>
	 * anotherspritename#...<br>
	 *  ...<br>
	 *  &anotherbankname<br>
	 *   ...<br>
	 *   * this is a comment
	 */
	@SuppressWarnings("unchecked")
	public OpenBankHolder(String filename)
	{
		// Initializes attributes
		this.banks = new HashMap<String, OpenBank>();
		this.lastbankname = null;
		this.lastcommands = new ArrayList<String>();
		
		// Reads the file
		readFile(filename);
		// Adds the last Bank and releases the memory
		if (this.lastcommands.size() > 0) {
			// System.out.println("Puts " + this.lastcommands.size() +
			// " objects to the bank " + this.lastbankname);
			this.banks.put(this.lastbankname, this.createBank(
					(ArrayList<String>) this.lastcommands.clone()));
		}
		
		this.lastcommands.clear();
		this.lastbankname = null;
	}
	
	
	// ABSTRACT METHODS -------------------------------------------------
	
	/**
	 * Creates and returns a new OpenBank.
	 * 
	 * @param commands	Commands given to the OpenBank.
	 * @return	Returns the new OpenBank.
	 */
	protected abstract OpenBank createBank(ArrayList<String> commands);

	
	// IMPLEMENTED METHODS ----------------------------------------------

	@SuppressWarnings("unchecked")
	@Override
	protected void onLine(String line)
	{
		// If the line starts with '*', skips it
		if (line.startsWith("*"))
			return;
		// If the line starts with '&' ends the last bank and starts a new bank
		if (line.startsWith("&")) {
			if (this.lastbankname != null) {
				// System.out.println("Puts " + this.lastcommands.size() +
				// " objects to the bank " + this.lastbankname);
				this.banks.put(this.lastbankname, this.createBank(
						(ArrayList<String>) this.lastcommands.clone()));
			}
			this.lastbankname = line.substring(1);
			this.lastcommands.clear();
			return;
		}
		// Otherwise, tries to add a new command to the lastcommands
		this.lastcommands.add(line);
	}

	
	// OTHER METHODS ---------------------------------------------------

	/**
	 * Provides an OpenBank with the given name from the databanks.
	 * 
	 * @param bankname	The name of the Bank
	 * @return The Bank with the given name or null if no Bank was found
	 */
	protected OpenBank getBank(String bankname) {
		if (this.banks.containsKey(bankname))
			return this.banks.get(bankname);
		else {
			System.err.println("The OpenBankHolder doesn't hold a bank "
					+ "named " + bankname);
			return null;
		}
	}

	/**
	 * Uninitializes all the banks held by this object
	 */
	public void uninitializeBanks() {
		// Goes through all the banks and uninitializes them
		for (OpenBank bank : this.banks.values()) {
			bank.uninitialize();
		}
	}
}
