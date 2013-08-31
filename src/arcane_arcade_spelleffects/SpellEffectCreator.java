package arcane_arcade_spelleffects;

import worlds.Room;
import listeners.RoomListener;
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
		RoomListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private boolean active;
	private int lifeleft, tillcreation, creationdelay, burstsize;
	
	
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
		this.creationdelay = creationdelay;
		this.tillcreation = 1;
		this.burstsize = burstsize;
		
		// Adds the object to the handler(s)
		if (actorhandler != null)
			actorhandler.addActor(this);
		if (room != null)
			room.addOnject(this);
	}
	
	
	// ABSTRACT METHODS	--------------------------------------------------
	
	/**
	 * Here a creator should create a single instance of the effect it 
	 * creates
	 */
	protected abstract void createEffect();
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	public boolean isActive()
	{
		return this.active;
	}

	@Override
	public boolean activate()
	{
		this.active = true;
		return true;
	}

	@Override
	public boolean inactivate()
	{
		this.active = false;
		return true;
	}

	@Override
	public void act()
	{
		// Adjusts the timers
		this.lifeleft --;
		this.tillcreation --;
		
		// Checks whether the object should die
		if (this.lifeleft <= 0)
			kill();
		// Or if the object should create a new bunch of effects
		else if (this.tillcreation <= 0)
		{
			this.tillcreation = this.creationdelay;
			for (int i = 0; i < this.burstsize; i++)
			{
				createEffect();
			}
		}
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
}
