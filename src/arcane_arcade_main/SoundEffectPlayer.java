package arcane_arcade_main;

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
}
