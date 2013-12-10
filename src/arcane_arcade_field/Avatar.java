package arcane_arcade_field;

import resourcebanks.OpenWavSoundBankHolder;
import resourcebanks.WavSoundBank;
import arcane_arcade_main.GameSettings;

/**
 * Each character represents a single playable wizard. Each of the wizards 
 * has unique graphics, stats and voice.
 *
 * @author Mikko Hilpinen.
 *         Created 8.9.2013.
 */
public enum Avatar
{
	/**
	 * Aka: Gandalf the Red. The red mage with finnish voice.
	 */
	GANDALF, 
	/**
	 * The White Wizard who speaks english.
	 */
	WHITEWIZARD, 
	/**
	 * Yasunori Abe who speaks japanese.
	 */
	ABE;
	
	
	// ATTRIBUTES	-----------------------------------------------------
	
	private static OpenWavSoundBankHolder voiceholder = 
			new OpenWavSoundBankHolder(GameSettings.WIZARDVOICEDATALOCATION);
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return The soundbank that contains the voices used by the character
	 */
	public WavSoundBank getVoiceBank()
	{
		switch (this)
		{
			case GANDALF: return voiceholder.getWavSoundBank("gandalf");
			case WHITEWIZARD: return voiceholder.getWavSoundBank("whitewizard");
			case ABE: return voiceholder.getWavSoundBank("abe");
			default: return null;
		}
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Initializes the soundbank used by this character
	 */
	public void initializeVoiceBank()
	{
		this.getVoiceBank().initializeBank();
	}
	
	/**
	 * Uninitializes the soundbank used by this character
	 */
	public void uninitializeVoiceBank()
	{
		this.getVoiceBank().uninitialize();
	}
}
