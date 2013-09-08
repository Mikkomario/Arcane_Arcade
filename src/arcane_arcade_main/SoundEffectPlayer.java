package arcane_arcade_main;

import arcane_arcade_field.Avatar;
import arcane_arcade_worlds.Navigator;

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
		Navigator.getWavBank("effects").getSound(effectname).play(
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
		Navigator.getWavBank("effects").getSound(effectname).play(
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
	
	private static float getEnvironmentalPan(double x)
	{
		return (float) (((x - GameSettings.SCREENWIDTH / 2) / 
				(GameSettings.SCREENWIDTH / 2)) * Options.maxenvironmentalpan);
	}
}
