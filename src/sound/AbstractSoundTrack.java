package sound;

import listeners.SoundListener;

/**
 * Abstractsoundtrack provides the necessary methods for the soundtracks 
 * so they can easily play custom tracks.
 *
 * @author Mikko Hilpinen.
 *         Created 25.8.2013.
 */
public abstract class AbstractSoundTrack extends Sound implements SoundListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private int currentindex, currentloopcount;
	private Sound currentsound;
	private boolean paused, delayed, loops, released;
	
	
	// CONSTRUCTROR	------------------------------------------------------
	
	/**
	 * Creates a new soundtrack with the given information
	 *
	 * @param name The name of the track
	 */
	public AbstractSoundTrack(String name)
	{
		super(name);
		
		// Initializes attributes
		this.currentindex = 0;
		this.currentloopcount = 0;
		this.currentsound = null;
		this.paused = false;
		this.delayed = false;
		this.loops = false;
		this.released = false;
	}
	
	
	// ABSTRACT METHODS	-------------------------------------------------
	
	/**
	 * Plays a certain phase of the track. This means playing a certain sound 
	 * once and adding the track as the listener. A subclass may wish to 
	 * affect the playing of the sound as well and it should be done in this 
	 * method.
	 *
	 * @param index The index of the phase played
	 * @return The sound that was just played
	 * @see sound.Sound#play(SoundListener)
	 */
	protected abstract Sound playPhase(int index);
	
	/**
	 * Returns how many times the given phase should be repeated
	 *
	 * @param index The index of the phase that will be played
	 * @return How many times the phase should be repeated (a negative number 
	 * means that the phase will be repeated until the track is released, 
	 * though the sound will be played at least once)
	 */
	protected abstract int getLoopCount(int index);
	
	/**
	 * @return How many phases there are in the track
	 */
	protected abstract int getMaxPhase();
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public boolean isActive()
	{
		// The track is always active so it doesn't break (inactivating is done 
		// by pausing)
		return true;
	}
	
	@Override
	public void activate()
	{
		// The track is always active
	}
	
	@Override
	public void inactivate()
	{
		// The track is always active
	}
	
	@Override
	public void onSoundStart(Sound source)
	{
		// Does nothing
	}
	
	@Override
	public void onSoundEnd(Sound source)
	{
		// If the sound was stopped, doesn't do anything
		if (!isPlaying())
			return;
		// Plays the next sound (if not paused, in which case delays the sound)
		if (this.paused)
		{
			this.delayed = true;
			System.out.println("Delays");
		}
		else
			playnextsound();
	}
	
	/**
	 * Stops the track from playing
	 */
	@Override
	public void stopSound()
	{
		// Stops the current sound and the track
		this.delayed = false;
		this.paused = false;
		this.released = false;
		this.currentsound.stop();
	}
	
	/**
	 * Pauses the track. The track can be continued from the same spot with 
	 * unpause method
	 */
	@Override
	public void pause()
	{
		this.paused = true;
		this.currentsound.pause();
	}
	
	/**
	 * Unpauses the track from the last state
	 */
	@Override
	public void unpause()
	{
		// TODO: Unpausing doesn't always seem to work (when not delayed)
		this.paused = false;
		// Continues the track if it was delayed
		if (this.delayed)
			playnextsound();
		// Otherwise just continues the former sound
		else
			this.currentsound.unpause();
	}
	
	/**
	 * Plays through the track once
	 */
	@Override
	public void playSound()
	{
		// Doesn't work if the track was killed
		if (isDead())
			return;
		
		// Updates information
		this.currentindex = 0;
		this.currentloopcount = getLoopCount(this.currentindex);
		this.paused = false;
		this.delayed = false;
		this.loops = false;
		this.released = false;
		
		// Plays the first sound
		this.currentsound = playPhase(this.currentindex);
	}
	
	/**
	 * Plays through the track repeatedly until stopped
	 */
	@Override
	public void loopSound()
	{
		playSound();
		this.loops = true;
	}	
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Releases the track from the next infinite loop when it starts.
	 */
	public void release()
	{
		this.released = true;
	}
	
	/**
	 * Negates the last release so that the track will not break out of the 
	 * next infinite loop until it is released again.
	 * 
	 * @see #release()
	 */
	public void unrelease()
	{
		this.released = false;
	}
	
	private void playnextsound()
	{
		// Only plays the next sound if the track is still playing
		if (!isPlaying())
			return;
		
		// The sound is no longer delayed
		this.delayed = false;
		
		// Checks whether more loops are needed
		// Loops the current sound if needed
		if (this.currentloopcount > 0 || 
				(this.currentloopcount < 0 && !this.released))
		{
			this.currentloopcount --;
			this.currentsound = playPhase(this.currentindex);
		}
		// Or plays the next sound
		else
		{
			// If the track was released from an infinite loop, remembers it
			if (this.currentloopcount < 0)
				this.released = false;
			
			// Gathers the information
			this.currentindex ++;
			
			// If the end of the track was reached, either repeats or stops
			if (this.currentindex >= getMaxPhase())
			{
				if (this.loops)
					this.currentindex = 0;
				else
				{
					this.delayed = false;
					this.paused = false;
					this.released = false;
					informSoundEnd();
					return;
				}
			}
			// Updates loop count
			this.currentloopcount = getLoopCount(this.currentindex);
			
			// And plays the new sound
			this.currentsound = playPhase(this.currentindex);
		}
	}
}
