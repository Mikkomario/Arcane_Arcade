package arcane_arcade_field;

import java.awt.Graphics2D;
import java.util.ArrayList;

import worlds.Room;

import listeners.AdvancedKeyListener;
import listeners.RoomListener;

import arcane_arcade_main.GameSettings;
import arcane_arcade_main.Main;
import arcane_arcade_spells.TeleportEffect;

import graphic.MaskChecker;
import graphic.SpriteDrawer;
import handleds.Collidable;
import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import handlers.KeyListenerHandler;
import helpAndEnums.CollisionType;
import helpAndEnums.DepthConstants;
import helpAndEnums.DoublePoint;
import drawnobjects.BasicPhysicDrawnObject;

/**
 * Mages are the playable characters in the game
 *
 * @author Mikko Hilpinen.
 *         Created 27.8.2013.
 */
public class Wizard extends BasicPhysicDrawnObject implements 
		AdvancedKeyListener, RoomListener
{
	// ATTRBUTES	----------------------------------------------------
	
	private double friction;
	private double maxspeed;
	private double accelration;
	private int teleportdelay;
	private int teleportdistance;
	
	private Room room;
	private DrawableHandler drawer;
	private ActorHandler actorhandler;
	private CollidableHandler collidablehandler;
	
	private SpriteDrawer spritedrawer;
	private MaskChecker maskchecker;
	private int doubletaptime;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new mage to the default position
	 *
	 * @param drawer The drawablehandler that will draw the mage
	 * @param collidablehandler The collidablehandler that will handle the mage's 
	 * collision checking
	 * @param collisionhandler The collisionhandler that will inform the mage 
	 * about collisions
	 * @param actorhandler The actorhandler that will inform the mage about 
	 * act events
	 * @param keylistenerhandler The keylistenerhandler that will inform 
	 * the object about keypresses
	 * @param room The room that will hold the wizard
	 */
	public Wizard(DrawableHandler drawer, CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, ActorHandler actorhandler, 
			KeyListenerHandler keylistenerhandler, Room room)
	{
		super(80, GameSettings.SCREENHEIGHT / 2, DepthConstants.NORMAL - 10, 
				true, CollisionType.CIRCLE, drawer, collidablehandler,
				collisionhandler, actorhandler);
		
		// Initializes attributes
		this.friction = 0.4;
		this.maxspeed = 5;
		this.accelration = 0.7;
		this.teleportdelay = 20;
		this.teleportdistance = 130;
		this.doubletaptime = 0;
		this.room = room;
		this.drawer = drawer;
		this.actorhandler = actorhandler;
		this.collidablehandler = collidablehandler;
		this.spritedrawer = new SpriteDrawer(
				Main.spritebanks.getBank("creatures").getSprite("redwizard"), 
				actorhandler);
		this.maskchecker = new MaskChecker(
				Main.spritebanks.getBank("creatures").getSprite("wizardmask"));
		
		// Stops the animation
		this.spritedrawer.setImageSpeed(0);
		this.spritedrawer.setImageIndex(0);
		
		// Sets the friction and maximum speed
		setFriction(this.friction);
		setMaxSpeed(this.maxspeed);
		
		// Sets the collision precision
		setCircleCollisionPrecision(27, 6, 2);
		setRelativeCollisionPoints(
				this.maskchecker.getRefinedRelativeCollisionPoints(
				getRelativeCollisionPoints(), 0));
		setRadius(27);
		
		// Adds the object to the handler(s) if possible
		if (keylistenerhandler != null)
			keylistenerhandler.addKeyListener(this);
		// Adds the object to the room (if possible)
		if (room != null)
			room.addOnject(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onCollision(ArrayList<DoublePoint> colpoints,
			Collidable collided)
	{
		// TODO: Add collision with the ball, the walls and certain spells
		//System.out.println("Collision!");
	}

	@Override
	public int getWidth()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getWidth();
		else
			return 0;
	}

	@Override
	public int getHeight()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getHeight();
		else
			return 0;
	}

	@Override
	public int getOriginX()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getOriginX();
		else
			return 0;
	}

	@Override
	public int getOriginY()
	{
		if (this.spritedrawer != null)
			return this.spritedrawer.getSprite().getOriginY();
		else
			return 0;
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// Draws the sprite
		if (this.spritedrawer != null)
			this.spritedrawer.drawSprite(g2d);
	}
	
	@Override
	public boolean kill()
	{
		// Kills the spritedrawer as well
		this.spritedrawer.kill();
		this.spritedrawer = null;
		this.maskchecker = null;
		
		return super.kill();
	}

	@Override
	public void onKeyDown(char key, int keyCode, boolean coded)
	{
		// If W or S was pressed, moves up / down
		if (!coded)
		{
			if (key == 'w' || key == 'W')
				addVelocity(0, -this.accelration);
			else if (key == 's' || key == 'S')
				addVelocity(0, this.accelration);
		}
	}

	@Override
	public void onKeyPressed(char key, int keyCode, boolean coded)
	{
		if (!coded)
		{
			// If w or s was double tapped, teleports
			if (key == 'w' || key == 'W')
			{
				tryTeleporting(-1);
			}
			else if (key == 's' || key == 'S')
			{
				tryTeleporting(1);
			}
		}
		// TODO: Add Casting sometime
	}

	@Override
	public void onKeyReleased(char key, int keyCode, boolean coded)
	{
		// Doesn't do anything
	}
	
	@Override
	public Collidable pointCollides(int x, int y)
	{	
		// Point only collides if it also collides the mask
		Collidable collided = super.pointCollides(x, y);
		
		if (collided == null)
			return null;
		
		if (this.maskchecker.maskContainsRelativePoint(
				negateTransformations(x, y), 0))
			return collided;
		else
			return null;
	}

	@Override
	public void onRoomStart(Room room)
	{
		// Doesn't do anything on room's start since the wizard is always 
		// created upon the start of the room
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// When the room ends, the wizard is killed
		kill();
	}
	
	@Override
	public void act()
	{
		// Does all normal functions
		super.act();
		
		// Checks doubletaptimer
		if (this.doubletaptime > 0)
			this.doubletaptime --;
		
		// Snaps back to the field
		if (getY() < getOriginY())
			setY(getOriginY());
		else if (getY() > GameSettings.SCREENHEIGHT - getHeight() + getOriginY())
			setY(GameSettings.SCREENHEIGHT - getHeight() + getOriginY());
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	// Tries to eleport either up (-1) or down (1)
	private void tryTeleporting(int movementsign)
	{
		if (this.doubletaptime > 0)
		{
			// Adds teleport effect
			new TeleportEffect((int) getX(), (int) getY(), this.drawer, 
					this.collidablehandler, this.actorhandler, this.room);
			// Teleports
			addPosition(0, movementsign * this.teleportdistance);
		}
		this.doubletaptime = this.teleportdelay;
	}
}
