package arcane_arcade_menus;

import java.util.Random;

import utopia_gameobjects.GameObject;
import utopia_listeners.RoomListener;
import utopia_listeners.TimerEventListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_timers.RandomTimer;
import utopia_worlds.Area;
import utopia_worlds.Room;
import arcane_arcade_main.GameSettings;

/**
 * MenuBackGroundCreator creates the menus' background effects: 
 * the stars and the comets
 *
 * @author Mikko Hilpinen.
 * @since 1.9.2013.
 */
public class MenuBackgroundEffectCreator extends GameObject implements 
		RoomListener, TimerEventListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private static final int STARTIMERID = 1;
	private static final int COMETTIMERID = 2;
	
	private boolean active;
	private Area area;
	private int cometoriginx;
	private int cometoriginy;
	private Random random;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates an effect generator that immediately starts to create stars and 
	 * comets to the background
	 *
	 * @param area The area where the objects will be placed to
	 */
	public MenuBackgroundEffectCreator(Area area)
	{
		super(area);
		
		// Initializes attributes
		this.area = area;
		this.random = new Random();
		this.cometoriginx = MultiMediaHolder.getSpriteBank("menu").getSprite(
				"comet").getOriginX();
		this.cometoriginy = MultiMediaHolder.getSpriteBank("menu").getSprite(
				"comet").getOriginY();
		this.active = true;
		
		// Setups timers
		new RandomTimer(this, 1, 100, STARTIMERID, area.getActorHandler());
		new RandomTimer(this, 1, 70, COMETTIMERID, area.getActorHandler());
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
			
			new BackgroundComet(x, y, scale, this.area);
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
				
				new BackgroundStar(x, y, scale, duration, this.area);
			}
		}
	}
}
