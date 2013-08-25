package graphic;

import java.util.ArrayList;
import java.util.HashMap;

import common.FileReader;

/**
 * This class holds numberous openspritebanks and provides them to the objects 
 * that need them. The holder loads the banks using a specific file
 *
 * @author Mikko Hilpinen.
 *         Created 26.8.2013.
 */
public class OpenSpriteBankHolder extends FileReader
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private HashMap<String, OpenSpriteBank> banks;
	private String lastbankname;
	private ArrayList<String> lastcommands;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates and initializes new openspritebankholder. The content is loaded 
	 * using the given file.
	 *
	 * @param filename A file that shows information about what banks to create 
	 * (src/data/ automatically included). 
	 * The file should be written as follows:<p>
	 * 
	 * &bankname<br>
	 * spritename#filename(src/data/ automatically included)#number of images#
	 * xorigin#yorigin<br>
	 * anotherspritename#...<br>
	 * ...<br>
	 * &anotherbankname<br>
	 * ...<br>
	 * * this is a comment
	 */
	public OpenSpriteBankHolder(String filename)
	{
		// Initializes attributes
		this.banks = new HashMap<String, OpenSpriteBank>();
		this.lastbankname = null;
		this.lastcommands = new ArrayList<String>();
		// Reads the file
		readFile(filename);
		// Adds the last spritebank and releases the memory
		this.banks.put(this.lastbankname, new OpenSpriteBank(this.lastcommands));
	}

	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	protected void onLine(String line)
	{
		// If the line starts with '*', skips it
		if (line.startsWith("*"))
			return;
		// If the line starts with '&' ends the last bank and starts a new bank
		if (line.startsWith("&"))
		{
			this.banks.put(this.lastbankname, new OpenSpriteBank(this.lastcommands));
			
			this.lastbankname = line.substring(1);
			this.lastcommands.clear();
			return;
		}
		// Otherwise, tries to add a new command to the lastcommands
		this.lastcommands.add(line);
	}
	
	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 * Provides an openspritebank with the given name from the databanks.
	 *
	 * @param bankname The name of the spritebank
	 * @return The spritebank with the given name or null if no spritebank was 
	 * found
	 */
	public OpenSpriteBank getBank(String bankname)
	{
		if (this.banks.containsKey(bankname))
			return this.banks.get(bankname);
		else
		{
			System.err.println("The OpenSpriteBankHolder doesn't hold a bank " +
					"named " + bankname);
			return null;
		}
	}
	
	/**
	 * Uninitializes all the banks held by this object
	 */
	public void uninitializeBanks()
	{
		// Goes through all the banks and uninitializes them
		for (OpenSpriteBank bank: this.banks.values())
		{
			bank.uninitialize();
		}
	}
}
