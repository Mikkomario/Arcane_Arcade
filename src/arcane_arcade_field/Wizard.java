package arcane_arcade_field;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import worlds.Room;

import listeners.AdvancedKeyListener;
import listeners.RoomListener;

import arcane_arcade_main.GameSettings;
import arcane_arcade_main.Main;
import arcane_arcade_spelleffects.SpellEffect;
import arcane_arcade_spelleffects.TeleportEffect;
import arcane_arcade_spells.Spell;
import arcane_arcade_status.Element;
import arcane_arcade_status.WizardStatus;

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
import helpAndEnums.HelpMath;
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
	
	private static final int UP = -1;
	private static final int DOWN = 1;
	private static final int LEFT = -2;
	private static final int RIGHT = 2;
	
	private double friction;
	private double maxspeed;
	private double accelration;
	private int teleportdelay;
	private int teleportdistance;
	
	private Room room;
	private DrawableHandler drawer;
	private ActorHandler actorhandler;
	private CollidableHandler collidablehandler;
	private BallRelay ballrelay;
	private CollisionHandler collisionhandler;
	
	private SpriteDrawer spritedrawer;
	private SpriteDrawer castdelaymeterdrawer;
	private MaskChecker maskchecker;
	private ArrayList<Element> elements;
	private int elementindex1;
	private int elementindex2;
	private int castdelay;
	private int doubletaptime;
	private double mana;
	private Spell currentspell;
	
	private WizardStatusDrawer statusdrawer;
	private HashMap<WizardStatus, Double> statusses;
	private double statusdepletionrate;
	
	private WizardHudDrawer huddrawer;
	
	
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
	 * @param ballrelay The ballrelay that holds information about the balls 
	 * in the field. That information will be forwarded to the casted spells.
	 */
	public Wizard(DrawableHandler drawer, CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, ActorHandler actorhandler, 
			KeyListenerHandler keylistenerhandler, Room room, BallRelay ballrelay)
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
		this.ballrelay = ballrelay;
		this.collisionhandler = collisionhandler;
		this.elementindex1 = 0;
		this.elementindex2 = 0;
		this.castdelay = 0;
		this.mana = 100;
		this.spritedrawer = new SpriteDrawer(
				Main.spritebanks.getOpenSpriteBank("creatures").getSprite("redwizard"), 
				actorhandler);
		this.maskchecker = new MaskChecker(
				Main.spritebanks.getOpenSpriteBank("creatures").getSprite("wizardmask"));
		this.castdelaymeterdrawer = new SpriteDrawer(
				Main.spritebanks.getOpenSpriteBank("field").getSprite(
				"regeneration"), actorhandler);
		// Initializes element list with two elements
		// TODO: Add elements
		this.elements = new ArrayList<Element>();
		this.elements.add(Element.FIRE);
		this.elements.add(Element.WATER);
		
		// Initializes current spell
		this.currentspell = this.elements.get(this.elementindex1).getSpell(
				this.elements.get(this.elementindex2));
		
		// Initializes status effects
		this.statusses = new HashMap<WizardStatus, Double>();
		for (WizardStatus status: WizardStatus.values())
		{
			this.statusses.put(status, 0.0);
		}
		this.statusdepletionrate = 0.1;
		this.statusdrawer = new WizardStatusDrawer(drawer, actorhandler, this);
		// Initializes HUD
		this.huddrawer = new WizardHudDrawer(drawer, this);
		
		// Stops the animation(s)
		this.spritedrawer.inactivate();
		this.spritedrawer.setImageIndex(0);
		this.castdelaymeterdrawer.inactivate();
		
		// Sets the friction and maximum speed
		setFriction(this.friction);
		setMaxSpeed(this.maxspeed);
		
		// Sets the collision precision
		setCircleCollisionPrecision(26, 5, 2);
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
		// TODO: Add collision with the ball
		// Collides with certain spells
		if (collided instanceof SpellEffect)
		{
			SpellEffect spell = (SpellEffect) collided;
			if (spell.collidesWithWizards())
			{
				Point averagepoint = 
						HelpMath.getAverageDoublePoint(colpoints).getAsPoint();
				spell.onWizardCollision(this, averagepoint.x, averagepoint.y);
			}
		}
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
			this.spritedrawer.drawSprite(g2d, 0, 0);
		// Also draws the castregenmeter (if casting)
		if (isCasting())
			this.castdelaymeterdrawer.drawSprite(g2d, 
					getSpriteDrawer().getSprite().getOriginX() - 
					this.castdelaymeterdrawer.getSprite().getOriginX(), 
					getSpriteDrawer().getSprite().getOriginY() - 
					this.castdelaymeterdrawer.getSprite().getOriginY());
		//drawCollisionPoints(g2d);
	}
	
	@Override
	public boolean kill()
	{
		// Kills the spritedrawer as well
		this.spritedrawer.kill();
		this.spritedrawer = null;
		this.maskchecker = null;
		// And the statusdrawer too
		this.statusdrawer.kill();
		this.statusdrawer = null;
		// And the huddrawer
		this.huddrawer.kill();
		this.huddrawer = null;
		
		return super.kill();
	}

	@Override
	public void onKeyDown(char key, int keyCode, boolean coded)
	{
		// If W or S was pressed, moves up / down
		if (!coded)
		{
			if (key == 'w' || key == 'W')
				move(-1);
			else if (key == 's' || key == 'S')
				move(1);
		}
		// TODO: Get some other button than control to do the job since 
		// it BLOCKS ALL OTHER BUTTONS (wtf?)
		else
		{
			// If ctrl was pressed, casts a spell
			if (keyCode == KeyEvent.VK_CONTROL)
				castSpell();
		}
	}

	@Override
	public void onKeyPressed(char key, int keyCode, boolean coded)
	{
		if (!coded)
		{
			// If w or s was double tapped, teleports
			if (key == 'w' || key == 'W')
				tryTeleporting(-1);
			else if (key == 's' || key == 'S')
				tryTeleporting(1);
			// If Q or A was pressed, changes the left element
			else if (key == 'Q' || key == 'q')
				changeElement(LEFT, UP);
			else if (key == 'A' || key == 'a')
				changeElement(LEFT, DOWN);
			// If E or D was pressed, changes the right element
			else if (key == 'E' || key == 'e')
				changeElement(RIGHT, UP);
			else if (key == 'D' || key == 'd')
				changeElement(RIGHT, DOWN);
		}
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
		
		// Checks castdelay
		if (this.castdelay > 0)
		{
			this.castdelay --;
			
			// If the casting ended, stops the sprite
			if (this.castdelay == 0)
			{
				this.spritedrawer.inactivate();
				this.spritedrawer.setImageIndex(0);
				// Also stops the regen meter
				this.castdelaymeterdrawer.inactivate();
			}
		}
		
		// Snaps back to the field
		if (getY() < getOriginY())
			setY(getOriginY());
		else if (getY() > GameSettings.SCREENHEIGHT - getHeight() + getOriginY())
			setY(GameSettings.SCREENHEIGHT - getHeight() + getOriginY());
		
		// Adjusts the status effects
		depleteStatusses();
	}
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return The spritedrawer that draws the wizard's sprite
	 */
	public SpriteDrawer getSpriteDrawer()
	{
		return this.spritedrawer;
	}
	
	/**
	 * @return Is the wizard currently casting a spell
	 */
	public boolean isCasting()
	{
		return this.castdelay > 0;
	}
	
	/**
	 * Changes the remaining time, how long the wizard is casting a new spell. 
	 * Also sets a new animation and a meter to show the progress
	 * 
	 * @param castdelay How long the wizard will be casting
	 */
	public void setCastDelay(int castdelay)
	{
		this.castdelay = castdelay;
		
		// Restarts the image index
		getSpriteDrawer().setImageIndex(0);
		this.castdelaymeterdrawer.setImageIndex(0);
		// Sets the animation on
		getSpriteDrawer().setAnimationDuration(castdelay);
		getSpriteDrawer().activate();
		this.castdelaymeterdrawer.setAnimationDuration(castdelay);
		this.castdelaymeterdrawer.activate();
	}
	
	/**
	 * Changes how much mana the wizard has left
	 *
	 * @param adjustment How much the remaining mana is increased / decreased 
	 * [-100, 100]
	 */
	public void adjustMana(double adjustment)
	{
		// Changes how much wizard has mana
		this.mana += adjustment;
		
		// If the mana went under 0 or over 100, fixes the value
		if (this.mana < 0)
			this.mana = 0;
		else if (this.mana > 100)
			this.mana = 100;
	}
	
	/**
	 * Checks if the wizard has enough mana left to use it
	 *
	 * @param mpusage How much mana the wizard has to have left ]0, 100]
	 * @return Does the wizard have at least the given amount of mana
	 */
	public boolean hasEnoughMana(int mpusage)
	{
		return this.mana >= mpusage;
	}
	
	/**
	 * @return How much mana the wizard has left [0, 100]
	 */
	protected int getMana()
	{
		return (int) this.mana;
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	
	/**
	 * Changes the strenght of a wizard's status effect
	 *
	 * @param status What status effect is adjusted
	 * @param adjustment How much the effect is adjusted [-100, 100] (0 being 
	 * the minimun final falue and 100 being the maximum final value).
	 */
	public void adjustStatus(WizardStatus status, double adjustment)
	{
		double oldstatus = this.statusses.get(status);
		// Checks the new value and fixes it if needed
		double newstatus = oldstatus + adjustment;
		if (newstatus < 0)
			newstatus = 0;
		else if (newstatus > 100)
			newstatus = 100;
		
		// If the status didn't change, does nothing else
		if (oldstatus == newstatus)
			return;
		
		// Some status changes change the gameplay
		if (status == WizardStatus.SLIPPERY)
			setFriction(this.friction * (1 - newstatus / 100));
		else if (status == WizardStatus.HASTE)
			setMaxSpeed(this.maxspeed * (1 + newstatus / 75));
		
		// Changes the status
		this.statusses.put(status, newstatus);
	}
	
	/**
	 * Returns the strength of a status effect affecting (or not affecting) the 
	 * wizard
	 *
	 * @param status The status effect who's strength is asked
	 * @return The strength of the status effect [0, 100]
	 */
	public double getStatusStrength(WizardStatus status)
	{	
		if (this.statusses == null)
			return 0;
		
		return this.statusses.get(status);
	}
	
	private void move(int movementsign)
	{
		// Doesn't work if the wizard is paralyzed
		if (getStatusStrength(WizardStatus.PARALYZED) > 0)
			return;
		
		// Haste affects accelration
		double speedchange = this.accelration * 
				(1 + getStatusStrength(WizardStatus.HASTE) / 75);
		
		// Panic switches the controls
		if (getStatusStrength(WizardStatus.PANIC) > 0)
			movementsign *= -1;
		
		addVelocity(0, movementsign * speedchange);
	}
	
	// Tries to eleport either up (-1) or down (1)
	private void tryTeleporting(int movementsign)
	{
		// Panic switches the controls
		if (getStatusStrength(WizardStatus.PANIC) > 0)
			movementsign *= -1;
		
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
	
	// Casts the spell desided by the two active elements
	private void castSpell()
	{
		if (!isCasting())
			this.currentspell.execute(this, this.ballrelay, this.drawer, 
					this.actorhandler, this.collidablehandler, 
					this.collisionhandler, this.room);
	}
	
	/**
	 * Returns an element from the given element index
	 *
	 * @param index The index of the element looked for
	 * @return The element from the given index
	 * @see Wizard#getFirstElementIndex()
	 * @see Wizard#getSecondElementIndex()
	 * @see Wizard#getNextElementIndex(int)
	 * @see Wizard#getPreviousElementIndex(int)
	 */
	protected Element getElement(int index)
	{
		if (index < 0 || index >= this.elements.size())
			return Element.NOELEMENT;
		
		return this.elements.get(index);
	}
	
	/**
	 * @return The index of the first active element
	 */
	protected int getFirstElementIndex()
	{
		return this.elementindex1;
	}
	
	/**
	 * @return The index of the second active element
	 */
	protected int getSecondElementIndex()
	{
		return this.elementindex2;
	}
	
	/**
	 * The index of the element that is next to the given element
	 *
	 * @param index The previous element index to the one looked for
	 * @return The element index next to the given element index
	 */
	protected int getNextElementIndex(int index)
	{
		if (index >= this.elements.size() - 1)
			return 0;
		else
			return index + 1;
	}
	
	/**
	 * The index of the element that is previous to the given element
	 *
	 * @param index The next element index to the one looked for
	 * @return The element index previous to the given element index
	 */
	protected int getPreviousElementIndex(int index)
	{
		if (index <= 0)
			return this.elements.size() - 1;
		else
			return index - 1;
	}
	
	private void depleteStatusses()
	{
		// Doesn't deplete statusses if they haven't been initialized yet
		if (this.statusses == null)
			return;
		
		for (WizardStatus status: this.statusses.keySet())
			adjustStatus(status, -this.statusdepletionrate);
	}
	
	private void changeElement(int elementside, int direction)
	{
		// Panic switches the controls
		if (getStatusStrength(WizardStatus.PANIC) > 0)
		{
			elementside *= -1;
			direction *= -1;
		}
		
		// Changes elements
		if (elementside == LEFT)
		{
			if (direction == DOWN)
				this.elementindex1 = getNextElementIndex(this.elementindex1);
			else
				this.elementindex1 = getPreviousElementIndex(this.elementindex1);
		}
		else
		{
			if (direction == DOWN)
				this.elementindex2 = getNextElementIndex(this.elementindex2);
			else
				this.elementindex2 = getPreviousElementIndex(this.elementindex2);
		}
		
		// Updates the current spell
		this.currentspell = this.elements.get(this.elementindex1).getSpell(
				this.elements.get(this.elementindex2));
		// Informs the hud about the change
		this.huddrawer.callSpellUpdate();
	}
}
