package arcane_arcade_field;

import java.util.HashMap;

import arcane_arcade_main.Options;
import arcane_arcade_main.SoundEffectPlayer;
import arcane_arcade_status.Element;

import sound.WavSoundQueue;

/**
 * WizardSoundQueuePlayer plays soundqueues made from wizard sounds. The player 
 * can play either element sounds or dialogs.
 *
 * @author Mikko Hilpinen.
 *         Created 8.9.2013.
 */
public class WizardSoundQueuePlayer
{
	// ATTRIBUTES	------------------------------------------------------
	
	private HashMap<Avatar, WavSoundQueue> elementqueues;
	private WavSoundQueue dialogqueue;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new wizardsoundqueueplayer ready to play wizard sounds as 
	 * queues
	 */
	public WizardSoundQueuePlayer()
	{
		// Initializes attributes
		this.dialogqueue = new WavSoundQueue(false);
		this.elementqueues = new HashMap<Avatar, WavSoundQueue>();
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Plays two elementsounds in a succession
	 *
	 * @param element1 The first element
	 * @param element2 The second element
	 * @param avatar The avatar that uses the elements
	 * @param x The x-coordinate of the sound's source on the screen
	 */
	public void playElementSoundCombo(Element element1, Element element2, 
			Avatar avatar, double x)
	{
		// If there is already an element queue playing, ends it
		if (this.elementqueues.containsKey(avatar))
			this.elementqueues.get(avatar).stop();
		// Creates the new queue
		WavSoundQueue newqueue = new WavSoundQueue(true);
		// Adds the sounds to the queue and plays it
		newqueue.addWavSound(avatar.getVoiceBank().getSound(
				element1.getSoundName()), Options.voicevolumeadjustment, 
				SoundEffectPlayer.getEnvironmentalPan(x), false);
		newqueue.addWavSound(avatar.getVoiceBank().getSound(
				element2.getSoundName()), Options.voicevolumeadjustment, 
				SoundEffectPlayer.getEnvironmentalPan(x), true);
		// Adds the queue to the map
		this.elementqueues.put(avatar, newqueue);
	}
	
	
	// ENUMERATIONS
	
	/**
	 * DialogEvent is used to define which sound is played at which situation.
	 *
	 * @author Mikko Hilpinen.
	 *         Created 8.9.2013.
	 */
	public enum DialogEvent
	{
		/**
		 * Victory event is usually played when a wizard wins the round
		 */
		VICTORY, 
		/**
		 * Loss event is usually played when a wizard dies
		 */
		LOSS, 
		/**
		 * Damage event is usually played when a wizard takes damage
		 */
		DAMAGE,
		/**
		 * Strike event is played when the wizard deals damage
		 */
		STRIKE, 
		/**
		 * Curse event is played when the wizard has cursed the opponent
		 */
		CURSE, 
		/**
		 * Cursed event is played when a wizard has been cursed
		 */
		CURSED, 
		/**
		 * Special event is played when a wizard makes something extraordinary
		 */
		SPECIAL, 
		/**
		 * Nodialog event is played when no sound effect is needed
		 */
		NODIALOG;
		
		
		// OTHER METHODS	----------------------------------------------
		
		private DialogEvent getOpposingEvent()
		{
			switch (this)
			{
				case VICTORY: return LOSS;
				case LOSS: return VICTORY;
				case DAMAGE: return STRIKE;
				case STRIKE: return DAMAGE;
				case CURSE: return CURSED;
				case CURSED: return CURSE;
				default: return NODIALOG;
			}
		}
	}
}
