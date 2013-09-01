package arcane_arcade_menus;

import java.util.Random;

import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.Navigator;

import listeners.RoomListener;

import worlds.Room;

import common.GameObject;

import handleds.Actor;
import handlers.ActorHandler;
import handlers.DrawableHandler;

/**
 * MenuBackGroundCreator creates the menus' background effects: 
 * the stars and the comets
 *
 * @author Mikko Hilpinen.
 *         Created 1.9.2013.
 */
public class MenuBackgroundEffectCreator extends GameObject implements Actor, 
		RoomListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private boolean active;
	private DrawableHandler drawer;
	private ActorHandler actorhandler;
	private Room room;
	private int timetillnextstar;
	private int timetillnextcomet;
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
		this.timetillnextcomet = 0;
		this.timetillnextstar = 0;
		this.cometoriginx = Navigator.getSpriteBank("menu").getSprite(
				"comet").getOriginX();
		this.cometoriginy = Navigator.getSpriteBank("menu").getSprite(
				"comet").getOriginY();
		this.active = true;
		
		// Adds the creator the room
		if (room != null)
			room.addOnject(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
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
		// Creates the stars and comets at certain intervals
		this.timetillnextcomet --;
		this.timetillnextstar --;
		
		if (this.timetillnextcomet < 0)
		{
			this.timetillnextcomet = this.random.nextInt(100);
			
			// Randomizes the starting position (along the screen border) and scale
			int startposition = (int) (this.random.nextInt(GameSettings.SCREENWIDTH + 
					GameSettings.SCREENHEIGHT) * 0.7);
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
		if (this.timetillnextstar < 0)
		{
			this.timetillnextstar = this.random.nextInt(50);
			int stars = 1 + this.random.nextInt(10);
			
			// Creates a random amount of stars to random positions with random 
			// scaling
			for (int i = 0; i < stars; i++)
			{
				int x = this.random.nextInt(GameSettings.SCREENWIDTH);
				int y = this.random.nextInt(GameSettings.SCREENHEIGHT);
				double scale = 0.1 + this.random.nextDouble() * 0.9;
				int duration = 40 + this.random.nextInt(20);
				
				new BackgroundStar(x, y, this.drawer, this.actorhandler, 
						this.room, scale, duration);
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
}
