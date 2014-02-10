package resourcebanks;

import java.util.ArrayList;

/**
 * This holder holds multiple MidiMusicBanks and provides them for objects that 
 * need them. The banks are initialized using a specific file.
 * 
 * @author Mikko Hilpinen. 
 * Created 10.2.2014
 */
public class OpenMidiMusicBankHolder extends OpenBankHolder
{
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates and initializes new OpenMidiMusicBankHolder. The content is loaded 
	 * using the given file.
	 *
	 * @param filename A file that shows information about what banks to create 
	 * (data/ automatically included). 
	 * The file should be written as follows:<p>
	 * 
	 * &bankname<br>
	 * midiname#filename(data/ automatically included)<br>
	 * anothermidiname#anotherfilename<br>
	 * ...<br>
	 * &anotherbankname<br>
	 * ...<br>
	 * * this is a comment
	 */
	public OpenMidiMusicBankHolder(String filename)
	{
		super(filename);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected OpenBank createBank(ArrayList<String> commands)
	{
		return new OpenMidiMusicBank(commands);
	}
	
	@Override
	public OpenMidiMusicBank getBank(String bankname)
	{
		OpenBank maybeOpenMidiBank = getBank(bankname);
		
		if(maybeOpenMidiBank instanceof OpenMidiMusicBank)
			return (OpenMidiMusicBank) maybeOpenMidiBank;
		else
			return null;
	}
}
