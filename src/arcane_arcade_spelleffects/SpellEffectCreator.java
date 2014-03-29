package arcane_arcade_spelleffects;

import utopia_gameobjects.GameObject;
import utopia_handleds.Actor;
import utopia_listeners.RoomListener;
import utopia_listeners.TimerEventListener;
import utopia_timers.ContinuousTimer;
import utopia_worlds.Area;
import utopia_worlds.Room;


/**
 * SpellEffectCreator creates spells with certain intervals. Creators aren't 
 * drawn and they die after a certain amount of steps have passed
 *
 * @author Mikko Hilpinen.
 * @since 28.8.2013.
 */
public abstract class SpellEffectCreator extends GameObject implements Actor, 
		RoomListener, TimerEventListener
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private boolean active;
	private double lifeleft;
	private int burstsize;
	private Area area;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new spelleffectcreator that starts creating new spelleffects 
	 * immediately
	 *
	 * @param duration How long will the creator live (steps)
	 * @param creationdelay How many steps there will be between each creation
	 * @param burstsize How many spelleffects are created at once
	 * @param area The area where the objects will be placed to
	 */
	public SpellEffectCreator(int duration, int creationdelay, int burstsize, 
			Area area)
	{
		super(area);
		
		// Initializes attributes
		this.active = true;
		this.lifeleft = duration;
		this.burstsize = burstsize;
		this.area = area;
		
		// Sets up the creation timer
		new ContinuousTimer(this, creationdelay, 0, area.getActorHandler()).setDelay(1);
		
		// Adds the object to the handler(s)
		if (area.getActorHandler() != null)
			area.getActorHandler().addActor(this);
	}
	
	
	// ABSTRACT METHODS	--------------------------------------------------
	
	/**
	 * Here a creator should create a single instance of the effect it 
	 * creates
	 * @param area The area where the effects will be placed to
	 */
	protected abstract void createEffect(Area area);
	
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
			createEffect(this.area);
		}
		// Also informs the subclass that the burst just ended
		onBurstEnd();
	}
}
