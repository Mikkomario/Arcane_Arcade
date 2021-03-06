package arcane_arcade_main;

import utopia_resourcebanks.MultiMediaHolder;
import arcane_arcade_field.Avatar;

/**
 * SoundEffectPlayer provides more easy tools to play different wavsounds in 
 * the game
 *
 * @author Mikko Hilpinen.
 *         Created 8.9.2013.
 */
public class SoundEffectPlayer
{
	/**
	 * Plays a single sound effect from the "effects" bank with no panning 
	 * and volume adjustment given by the options
	 *
	 * @param effectname The name of the sound effect that will be played. 
	 * The effects sound bank should contain a sound with the given name.
	 */
	public static void playSoundEffect(String effectname)
	{
		// Simply plays the sound with certain volume settings
		MultiMediaHolder.getWavBank("effects").getSound(effectname).play(
				Options.soundvolumeadjustment, 0, null);
	}
	
	/**
	 * Plays a soundeffect panning it according to its position on the screen
	 *
	 * @param effectname The name of the sound played. Should be contained in 
	 * the "effects" soundbank
	 * @param x The x-coordinate of the sound's origin on screen
	 */
	public static void playSoundEffecWithEnvironmentalPan(String effectname, 
			double x)
	{
		// Calculates the used pan
		float pan = getEnvironmentalPan(x);
		// Plays the sound effect
		MultiMediaHolder.getWavBank("effects").getSound(effectname).play(
				Options.soundvolumeadjustment, pan, null);
	}
	
	/**
	 * Plays a certain voice for a wizard
	 *
	 * @param avatar The type of the wizard making the sound
	 * @param voicename The name of the played sound. The sound should exist in 
	 * the bank
	 * @param x The x-coordinate of the sound's origin on the screen
	 */
	public static void playWizardVoice(Avatar avatar, String voicename, double x)
	{
		// Calculates the used pan
		float pan = getEnvironmentalPan(x);
		// Plays the voice
		avatar.getVoiceBank().getSound(voicename).play(
				Options.voicevolumeadjustment, pan, null);
	}
	
	/**
	 * Returns a pan calculated from the source's position on the screen
	 *
	 * @param x The sound source's x-coordinate on the screen
	 * @return The pan the sound should use.
	 */
	public static float getEnvironmentalPan(double x)
	{
		/*
		float returned = (float) (((x - GameSettings.SCREENWIDTH / 2.0) / 
				(GameSettings.SCREENWIDTH / 2.0)) * Options.maxenvironmentalpan);
		
		System.out.println("Panning sound: " + returned);
		*/
		return (float) (((x - GameSettings.SCREENWIDTH / 2.0) / 
				(GameSettings.SCREENWIDTH / 2.0)) * Options.maxenvironmentalpan);
	}
}
