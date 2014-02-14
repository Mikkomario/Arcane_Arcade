package arcane_arcade_main;

import java.util.HashMap;

import resourcebanks.MidiMusicBank;
import resourcebanks.OpenMidiMusicBankHolder;
import resourcebanks.OpenSpriteBankHolder;
import resourcebanks.OpenWavSoundBankHolder;
import resourcebanks.SpriteBank;
import resourcebanks.WavSoundBank;

/**
 * Multimediaholder keeps track Sprite, Wav and Midi resources 
 * and provides them for other objects. The resources can be 
 * activated and deactivated at-will. The class is wholly static since no 
 * copies of the resources should be made and they should be accessed from 
 * anywhere.
 * 
 * @author Mikko Hilpinen. 
 * Created 14.2.2014
 */
public class MultiMediaHolder
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private static HashMap<String, SpriteBank> activeSpriteBanks = 
			new HashMap<String, SpriteBank>();
	private static HashMap<String, WavSoundBank> activeWavBanks = 
			new HashMap<String, WavSoundBank>();
	private static HashMap<String, MidiMusicBank> activeMidiBanks = 
			new HashMap<String, MidiMusicBank>();
	
	private static OpenSpriteBankHolder spritebankholder = 
			new OpenSpriteBankHolder(GameSettings.SPRITEDATALOCATION);
	private static OpenWavSoundBankHolder wavbankholder = 
			new OpenWavSoundBankHolder(GameSettings.WAVDATALOCATION);
	private static OpenMidiMusicBankHolder midibankholder = 
			new OpenMidiMusicBankHolder(GameSettings.MIDIDATALOCATION);
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	private MultiMediaHolder()
	{
		// Constructor hidden from other classes since only static 
		// interfaces are allowed
	}

	
	// OTHER METHODS	--------------------------------------------------
	
	// This class would work so much better if templates were allowed in Java...
	// TODO: Try to find a way to make this work with enumerations or such
	// This may require some changes to the bank classes as well but would 
	// be most awesome
	
	/**
	 * Returns a spritebank from the activated spritebanks
	 *
	 * @param spritebankname The name of the spritebank
	 * @return The active spritebank with the given name
	 */
	public static SpriteBank getSpriteBank(String spritebankname)
	{
		if (activeSpriteBanks.containsKey(spritebankname))
			return activeSpriteBanks.get(spritebankname);
		else
		{
			System.err.println("SpriteBank named " + spritebankname + 
					" isn't currenly active!");
			return null;
		}
	}
	
	/**
	 * Returns an wavsoundbank if it has been initialized yet
	 *
	 * @param wavbankname The name of the needed wavbank
	 * @return The wavbank with the given name or null if no such bank exists 
	 * or if the bank is not active
	 */
	public static WavSoundBank getWavBank(String wavbankname)
	{
		if (activeWavBanks.containsKey(wavbankname))
			return activeWavBanks.get(wavbankname);
		else
		{
			System.err.println("WavBank named " + wavbankname + 
					" isn't currenly active!");
			return null;
		}
	}
	
	/**
	 * Returns an midiMusicBank if it has been initialized
	 *
	 * @param midibankname The name of the needed midiBank
	 * @return The midiBank with the given name or null if no such bank exists 
	 * or if the bank is not active
	 */
	public static MidiMusicBank getMidiBank(String midibankname)
	{
		if (activeMidiBanks.containsKey(midibankname))
			return activeMidiBanks.get(midibankname);
		else
		{
			System.err.println("MidiBank named " + midibankname + 
					" isn't currenly active!");
			return null;
		}
	}
	
	/**
	 * Activates a spriteBank with the given name. Initializing all the 
	 * resources in the bank.
	 * 
	 * @param bankname The name of the bank to be activated
	 */
	public static void activateSpriteBank(String bankname)
	{
		// If the bank is already active, does nothing
		if (activeSpriteBanks.containsKey(bankname))
			return;
		
		// Adds the bank to the map and initializes it
		SpriteBank newbank = spritebankholder.getOpenSpriteBank(bankname);
		
		if (newbank == null)
		{
			System.err.println("SpriteBank named " + bankname + 
					"doesn't exist and thus cannot be activated");
			return;
		}
		
		newbank.initializeBank();
		activeSpriteBanks.put(bankname, newbank);
	}
	
	/**
	 * Activates a wavBank with the given name. Initializing all the 
	 * resources in the bank.
	 * 
	 * @param bankname The name of the bank to be activated
	 */
	public static void activateWavSoundBank(String bankname)
	{
		// If the bank is already active, does nothing
		if (activeWavBanks.containsKey(bankname))
			return;
		
		// Adds the bank to the map and initializes it
		WavSoundBank newbank = wavbankholder.getWavSoundBank(bankname);
		
		if (newbank == null)
		{
			System.err.println("WavBank named " + bankname + 
					"doesn't exist and thus cannot be activated");
			return;
		}
		
		newbank.initializeBank();
		activeWavBanks.put(bankname, newbank);
	}
	
	/**
	 * Activates a midiBank with the given name. Initializing all the 
	 * resources in the bank.
	 * 
	 * @param bankname The name of the bank to be activated
	 */
	public static void activateMidiMusicBank(String bankname)
	{
		// If the bank is already active, does nothing
		if (activeMidiBanks.containsKey(bankname))
			return;
		
		// Adds the bank to the map and initializes it
		MidiMusicBank newbank = midibankholder.getMidiMusicBank(bankname);
		
		if (newbank == null)
		{
			System.err.println("MidiBank named " + bankname + 
					"doesn't exist and thus cannot be activated");
			return;
		}
		
		newbank.initializeBank();
		activeMidiBanks.put(bankname, newbank);
	}
	
	/**
	 * Deactivates the spriteBank with the given name, uninitializing all 
	 * the resources in it.
	 * 
	 * @param bankname The name of the bank to be deactivated
	 * @warning Make sure that the resources aren't in use at the time of the 
	 * activation
	 */
	public static void deactivateSpriteBank(String bankname)
	{
		// If the bank doesn't contain the bank, does nothing
		if (!activeSpriteBanks.containsKey(bankname))
			return;
		
		// Uninitializes the banks and then removes it
		activeSpriteBanks.get(bankname).uninitialize();
		activeSpriteBanks.remove(bankname);
	}
	
	/**
	 * Deactivates the WavBank with the given name, uninitializing all 
	 * the resources in it.
	 * 
	 * @param bankname The name of the bank to be deactivated
	 * @warning Make sure that the resources aren't in use at the time of the 
	 * activation
	 */
	public static void deactivateWavSoundBank(String bankname)
	{
		// If the bank doesn't contain the bank, does nothing
		if (!activeWavBanks.containsKey(bankname))
			return;
		
		// Uninitializes the banks and then removes it
		activeWavBanks.get(bankname).uninitialize();
		activeWavBanks.remove(bankname);
	}
	
	/**
	 * Deactivates the midiBank with the given name, uninitializing all 
	 * the resources in it.
	 * 
	 * @param bankname The name of the bank to be deactivated
	 * @warning Make sure that the resources aren't in use at the time of the 
	 * activation
	 */
	public static void deactivateMidiMusicBank(String bankname)
	{
		// If the bank doesn't contain the bank, does nothing
		if (!activeMidiBanks.containsKey(bankname))
			return;
		
		// Uninitializes the banks and then removes it
		activeMidiBanks.get(bankname).uninitialize();
		activeMidiBanks.remove(bankname);
	}
}
