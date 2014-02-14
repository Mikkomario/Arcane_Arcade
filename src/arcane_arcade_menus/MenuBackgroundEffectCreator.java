package arcane_arcade_menus;

import java.util.Random;

import arcane_arcade_main.GameSettings;
import listeners.RoomListener;
import listeners.TimerEventListener;
import resourcebanks.MultiMediaHolder;
import timers.RandomTimer;
import worlds.Room;


import gameobjects.GameObject;
import handlers.ActorHandler;
import handlers.DrawableHandler;

/**
 * MenuBackGroundCreator creates the menus' background effects: 
 * the stars and the comets
 *
 * @author Mikko Hilpinen.
 *         Created 1.9.2013.
 */
public class MenuBackgroundEffectCreator extends GameObject implements 
		RoomListener, TimerEventListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private static final int STARTIMERID = 1;
	private static final int COMETTIMERID = 2;
	
	private boolean active;
	private DrawableHandler drawer;
	private ActorHandler actorhandler;
	private Room room;
	private int cometoriginx;
	private int cometoriginy;
	private Random random;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates an effect generator that immediately starts to create stars and 
	 * comets to the background
	 *
	 * @param drawer The drawer that will draw the created effects
	 * @param actorhandler The actorhandler that will animate the effects and 
	 * inform them about steps
	 * @param room The room to which the effects are created
	 */
	public MenuBackgroundEffectCreator(DrawableHandler drawer, 
			ActorHandler actorhandler, Room room)
	{
		// Initializes attributes
		this.drawer = drawer;
		this.actorhandler = actorhandler;
		this.room = room;
		this.random = new Random();
		this.cometoriginx = MultiMediaHolder.getSpriteBank("menu").getSprite(
				"comet").getOriginX();
		this.cometoriginy = MultiMediaHolder.getSpriteBank("menu").getSprite(
				"comet").getOriginY();
		this.active = true;
		
		// Setups timers
		new RandomTimer(this, 1, 100, STARTIMERID, actorhandler);
		new RandomTimer(this, 1, 70, COMETTIMERID, actorhandler);
		
		// Adds the creator the room and actorhandler
		if (room != null)
			room.addObject(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
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
		if (timerid == COMETTIMERID)
		{
			// Randomizes the starting position (along the screen border) and scale
			int startposition = (int) (this.random.nextInt(GameSettings.SCREENWIDTH + 
					GameSettings.SCREENHEIGHT) * 0.8);
			double scale = 0.1 + this.random.nextDouble() * 0.9;
			
			int x = 0;
			int y = 0;
			if (startposition < GameSettings.SCREENWIDTH * 0.7)
			{
				x = (int) (GameSettings.SCREENWIDTH * 0.3) + startposition;
				y = (int) (-this.cometoriginy * scale);
			}
			else
			{
				x = GameSettings.SCREENWIDTH + this.cometoriginx;
				y = (int) (startposition - GameSettings.SCREENWIDTH * 0.7 - 
						this.cometoriginy * scale);
			}
			
			new BackgroundComet(x, y, this.drawer, this.actorhandler, 
					this.room, scale);
		}
		else if (timerid == STARTIMERID)
		{
			int stars = 1 + this.random.nextInt(10);
			
			// Creates a random amount of stars to random positions with random 
			// scaling
			for (int i = 0; i < stars; i++)
			{
				int x = this.random.nextInt(GameSettings.SCREENWIDTH);
				int y = this.random.nextInt(GameSettings.SCREENHEIGHT);
				double scale = 0.1 + this.random.nextDouble() * 0.9;
				int duration = 60 + this.random.nextInt(40);
				
				new BackgroundStar(x, y, this.drawer, this.actorhandler, 
						this.room, scale, duration);
			}
		}
	}
}
