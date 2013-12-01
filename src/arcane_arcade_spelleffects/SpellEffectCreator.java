package arcane_arcade_spelleffects;

import timers.ContinuousTimer;
import worlds.Room;
import listeners.RoomListener;
import listeners.TimerEventListener;
import common.GameObject;

import handleds.Actor;
import handlers.ActorHandler;

/**
 * SpellEffectCreator creates spells with certain intervals. Creators aren't 
 * drawn and they die after a certain amount of steps have passed
 *
 * @author Mikko Hilpinen.
 *         Created 28.8.2013.
 */
public abstract class SpellEffectCreator extends GameObject implements Actor, 
		RoomListener, TimerEventListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private boolean active;
	private double lifeleft;
	private int burstsize;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new spelleffectcreator that starts creating new spelleffects 
	 * immediately
	 *
	 * @param duration How long will the creator live (steps)
	 * @param creationdelay How many steps there will be between each creation
	 * @param burstsize How many spelleffects are created at once
	 * @param actorhandler The actorhandler that informs the object about steps
	 * @param room The room in which the creator is created
	 */
	public SpellEffectCreator(int duration, int creationdelay, int burstsize, 
			ActorHandler actorhandler, Room room)
	{
		// Initializes attributes
		this.active = true;
		this.lifeleft = duration;
		this.burstsize = burstsize;
		
		// Sets up the creation timer
		new ContinuousTimer(this, creationdelay, 0, actorhandler).setDelay(1);
		
		// Adds the object to the handler(s)
		if (actorhandler != null)
			actorhandler.addActor(this);
		if (room != null)
			room.addObject(this);
	}
	
	
	// ABSTRACT METHODS	--------------------------------------------------
	
	/**
	 * Here a creator should create a single instance of the effect it 
	 * creates
	 */
	protected abstract void createEffect();
	
	/**
	 * This method is called at the end of each burst since some creators 
	 * might want to react to bursts.
	 */
	protected abstract void onBurstEnd();
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public boolean isActive()
	{
		return this.active;
	}

	@Override
	public void activate()
	{
		this.active = true;
	}

	@Override
	public void inactivate()
	{
		this.active = false;
	}

	@Override
	public void act(double steps)
	{
		// Adjusts the timer
		this.lifeleft -= steps;
		
		// Checks whether the object should die
		if (this.lifeleft <= 0)
			kill();
	}

	@Override
	public void onRoomStart(Room room)
	{
		// Does nothing on room start
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Dies at the end of the room
		kill();
	}
	
	@Override
	public void onTimerEvent(int timerid)
	{
		// Creates a new burst of spelleffects at each event
		createSpellEffects();
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Changes the creator's burstsize
	 *
	 * @param adjustment How much the burstsize is increased / decreased
	 */
	protected void adjustBurstSize(int adjustment)
	{
		this.burstsize += adjustment;
	}
	
	/**
	 * @return How many effects are created at once
	 */
	protected int getBurstSize()
	{
		return this.burstsize;
	}
	
	private void createSpellEffects()
	{
		for (int i = 0; i < this.burstsize; i++)
		{
			createEffect();
		}
	}
}
