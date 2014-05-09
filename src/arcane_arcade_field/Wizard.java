package arcane_arcade_field;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import utopia_gameobjects.BasicPhysicDrawnObject;
import utopia_graphic.MaskChecker;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handleds.Collidable;
import utopia_utility.CollisionType;
import utopia_utility.DepthConstants;
import utopia_utility.HelpMath;
import utopia_listeners.AdvancedKeyListener;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import utopia_worlds.Room;
import arcane_arcade_field.WizardSoundQueuePlayer.DialogEvent;
import arcane_arcade_main.Button;
import arcane_arcade_main.GameSettings;
import arcane_arcade_spelleffects.ExplosionEffect;
import arcane_arcade_spelleffects.SpellEffect;
import arcane_arcade_spelleffects.TeleportEffect;
import arcane_arcade_spells.Spell;
import arcane_arcade_status.Element;
import arcane_arcade_status.WizardStatus;
import arcane_arcade_tutorials.MovingTutorial;

/**
 * Mages are the playable characters in the game
 *
 * @author Mikko Hilpinen.
 * @since 27.8.2013.
 */
public class Wizard extends BasicPhysicDrawnObject implements 
		AdvancedKeyListener, RoomListener
{
	// ATTRBUTES	----------------------------------------------------
	
	private static final int UP = -1;
	private static final int DOWN = 1;
	private static final int LEFT = -2;
	private static final int RIGHT = 2;
	
	private static final Class<?>[] COLLIDEDCLASSES = new Class<?>[] {
		Ball.class, SpellEffect.class, MovingTutorial.Book.class};
	
	private Avatar avatar;
	
	// TODO: Move some of these to the avatar
	private double friction;
	private double maxspeed;
	private double accelration;
	private int teleportdelay;
	private int teleportdistance;
	private double manaregeneration;
	private double castdelaymodifier;
	
	private Area area;
	private BallRelay ballrelay;
	private ScoreKeeper scorekeeper;
	private WizardSoundQueuePlayer voiceplayer;
	
	private SingleSpriteDrawer spritedrawer;
	private SingleSpriteDrawer castdelaymeterdrawer;
	private MaskChecker maskchecker;
	private Element[] elements;
	private int elementindex1;
	private int elementindex2;
	private double castdelay;
	private double doubletaptime;
	private double mana;
	private int manabeforecasting;
	private int lastcastdelay;
	private Spell currentspell;
	private int hp;
	private int maxhp;
	private double invincibilitytime;
	private int invincibilitydelay;
	private double elementsoundtime;
	private boolean elementsoundactivated;
	
	private HashMap<WizardStatus, Double> statusses;
	private double statusdepletionrate;
	
	private WizardHud huddrawer;
	
	private ScreenSide screenside;
	private HashMap<Button, Character> buttonmaps;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new mage to the default position
	 *
	 * @param area The area where the object is placed to
	 * @param scorekeeper The scorekeeper that will keep track of the 
	 * game's score and respawn the wizard after they die (Optional)
	 * @param ballrelay The ballrelay that holds information about the balls 
	 * in the field. That information will be forwarded to the casted spells.
	 * @param screenside Which side of the room the wizard is created at
	 * @param wizardbuttons The buttons used to control the wizard
	 * @param usedelements Which elements the wizard uses in their spells
	 * @param manaregenerationmodifier How fast the wizard regenerates mana 
	 * (default 1)
	 * @param castdelaymodifier How fast / slow the wizard casts spells 
	 * (default 1) 
	 * @param avatar The avatar of the wizard that defines the wizard's voice 
	 * and other stats.
	 * @param voiceplayer The soundqueueplayer that will play the wizard's 
	 * punchlines
	 * @param usesHud Should the wizard's statistics be presented in a hud?
	 */
	public Wizard(Area area, 
			ScoreKeeper scorekeeper, 
			BallRelay ballrelay, ScreenSide screenside, 
			HashMap<Button, Character> wizardbuttons, Element[] usedelements, 
			double manaregenerationmodifier, double castdelaymodifier, 
			Avatar avatar, WizardSoundQueuePlayer voiceplayer, boolean usesHud)
	{
		super(70, GameSettings.SCREENHEIGHT / 2, DepthConstants.NORMAL - 10, 
				true, CollisionType.CIRCLE, area);
		
		// Initializes attributes
		this.screenside = screenside;
		this.avatar = avatar;
		this.voiceplayer = voiceplayer;
		this.friction = 0.4;
		this.maxspeed = 5;
		this.accelration = 0.7;
		this.teleportdelay = 20;
		this.teleportdistance = 130;
		this.doubletaptime = 0;
		this.area = area;
		this.ballrelay = ballrelay;
		this.scorekeeper = scorekeeper;
		this.elementindex1 = 0;
		this.elementindex2 = 0;
		this.castdelay = 0;
		this.castdelaymodifier = castdelaymodifier;
		this.lastcastdelay = 0;
		this.mana = 100;
		this.manabeforecasting = 100;
		this.manaregeneration = GameSettings.DEFAULTMANAREGENERATIONRATE * 
				manaregenerationmodifier;
		this.maxhp = 3;
		this.hp = this.maxhp;
		this.invincibilitytime = 0;
		this.invincibilitydelay = 60;
		this.spritedrawer = new SingleSpriteDrawer(MultiMediaHolder.getSpriteBank(
				"creatures").getSprite("redwizard"), area.getActorHandler(), this);
		this.maskchecker = new MaskChecker(MultiMediaHolder.getSpriteBank(
				"creatures").getSprite("wizardmask"));
		this.castdelaymeterdrawer = new SingleSpriteDrawer(
				MultiMediaHolder.getSpriteBank("field").getSprite(
				"regeneration"), area.getActorHandler(), this);
		this.buttonmaps = wizardbuttons;
		this.elements = usedelements;
		this.elementsoundactivated = false;
		this.elementsoundtime = 0;
		
		// Initializes the used soundbank (if it isn't initialized already)
		this.avatar.initializeVoiceBank();
		
		// Initializes current spell
		this.currentspell = this.elements[this.elementindex1].getSpell(
				this.elements[this.elementindex2]);
		
		// Initializes status effects
		this.statusses = new HashMap<WizardStatus, Double>();
		for (WizardStatus status: WizardStatus.values())
		{
			this.statusses.put(status, 0.0);
		}
		this.statusdepletionrate = 0.1;
		// Creates the status drawer
		new WizardStatusDrawer(area, this);
		
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
		
		// Setups the hud (if needed)
		this.huddrawer = null;
		if (usesHud)
			this.huddrawer = new WizardHud(area, this);
		
		// Changes some aspects according to the screen side
		if (this.screenside == ScreenSide.RIGHT)
		{
			setXScale(-1);
			setX(GameSettings.SCREENWIDTH - getX());
		}
		
		// Adds the object to the handler(s) if possible
		if (area.getKeyHandler() != null)
			area.getKeyHandler().addKeyListener(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onCollision(ArrayList<Point2D.Double> colpoints,
			Collidable collided, double steps)
	{
		// Collides with the ball (normally causes instant death)
		if (collided instanceof Ball)
		{
			// If the wizard is not invincible, they take a hit
			if (this.invincibilitytime > 0 || 
					getStatusStrength(WizardStatus.PHASING) > 0 || 
					getStatusStrength(WizardStatus.IRONFLESH) > 0)
				return;
			
			// Wizard dies :(
			die();
		}
		// Collides with certain spells
		else if (collided instanceof SpellEffect)
		{
			SpellEffect spell = (SpellEffect) collided;
			if (spell.collidesWithWizards())
			{
				Point2D.Double averagepoint = 
						HelpMath.getAveragePoint(colpoints);
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
		drawCollisionArea(g2d);
		drawCollisionPoints(g2d);
	}
	
	@Override
	public void kill()
	{
		// kills the huddrawer
		if (this.huddrawer != null)
			this.huddrawer.kill();
		this.huddrawer = null;
		
		super.kill();
	}

	@Override
	public void onKeyDown(char key, int keyCode, boolean coded, double steps)
	{
		// If UP or DOWN buttons were pressed, moves up / down
		if (!coded)
		{
			if (key == this.buttonmaps.get(Button.UP))
				move(-1, steps);
			else if (key == this.buttonmaps.get(Button.DOWN))
				move(1, steps);
			// If CAST button was pressed, casts a spell
			if (key == this.buttonmaps.get(Button.CAST))
				castSpell();
		}
	}

	@Override
	public void onKeyPressed(char key, int keyCode, boolean coded)
	{
		if (!coded)
		{
			// If w or s was double tapped, teleports
			if (key == this.buttonmaps.get(Button.UP))
				tryTeleporting(-1);
			else if (key == this.buttonmaps.get(Button.DOWN))
				tryTeleporting(1);
			// If Q or A was pressed, changes the left element
			else if (key == this.buttonmaps.get(Button.LEFT_ELEMENT_UP))
				changeElement(LEFT, UP);
			else if (key == this.buttonmaps.get(Button.LEFT_ELEMENT_DOWN))
				changeElement(LEFT, DOWN);
			// If E or D was pressed, changes the right element
			else if (key == this.buttonmaps.get(Button.RIGHT_ELEMENT_UP))
				changeElement(RIGHT, UP);
			else if (key == this.buttonmaps.get(Button.RIGHT_ELEMENT_DOWN))
				changeElement(RIGHT, DOWN);
		}
	}

	@Override
	public void onKeyReleased(char key, int keyCode, boolean coded)
	{
		// Doesn't do anything
	}
	
	@Override
	public boolean pointCollides(Point2D testPosition)
	{	
		// Point only collides if it also collides the mask
		if (!super.pointCollides(testPosition))
			return false;
		
		return (this.maskchecker.maskContainsRelativePoint(
				negateTransformations(testPosition), 0));
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
		// Also uninitializes the used soundbank
		this.avatar.uninitializeVoiceBank();
	}
	
	@Override
	public void act(double steps)
	{
		// Does all normal functions
		super.act(steps);
		
		// Checks doubletaptimer
		if (this.doubletaptime > 0)
			this.doubletaptime -= steps;
		
		// Checks castdelay
		if (this.castdelay > 0)
		{
			this.castdelay -= steps;
			
			// If the casting ended, stops the sprite
			if (this.castdelay <= 0)
			{
				this.spritedrawer.inactivate();
				this.spritedrawer.setImageIndex(0);
				// Also stops the regen meter
				this.castdelaymeterdrawer.inactivate();
			}
		}
		
		// Checks element sound time
		if (this.elementsoundactivated)
		{
			this.elementsoundtime -= steps;
			
			if (this.elementsoundtime <= 0)
			{
				/*
				System.out.println("Plays sound with elements " + 
						getElement(getFirstElementIndex()) + " and " + 
						getElement(getSecondElementIndex()));
				*/
				
				// Plays a sound for the current elements
				this.voiceplayer.playElementSoundCombo(getElement(
						getFirstElementIndex()), getElement(
						getSecondElementIndex()), this.avatar, getX());
				this.elementsoundactivated = false;
			}
		}
		
		// Snaps back to the field
		if (getY() < getOriginY())
			setY(getOriginY());
		else if (getY() > GameSettings.SCREENHEIGHT - getHeight() + getOriginY())
			setY(GameSettings.SCREENHEIGHT - getHeight() + getOriginY());
		
		// Adjusts the status effects
		depleteStatusses(steps);
		
		// Regenerates mana
		regenerateMana(steps);
		
		// Checks invincibility
		if (this.invincibilitytime > 0)
		{
			this.invincibilitytime -= steps;
			if ((int) this.invincibilitytime / 5 % 2 == 0)
				setVisible();
			else
				setInvisible();
		}
	}
	
	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		return COLLIDEDCLASSES;
	}
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return The spritedrawer that draws the wizard's sprite
	 */
	public SingleSpriteDrawer getSpriteDrawer()
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
		// Modifies the castdelay
		castdelay *= this.castdelaymodifier;
		
		// Remembers the castdelay for calculations
		this.lastcastdelay = castdelay;
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
	protected double getMana()
	{
		return this.mana;
	}
	
	/**
	 * @return The amount of mana the wizard had before they casted their last 
	 * spell
	 */
	protected int getManaBeforeCasting()
	{
		return this.manabeforecasting;
	}
	
	/**
	 * @return The currently active wizard's spell
	 */
	protected Spell getCurrentSpell()
	{
		return this.currentspell;
	}
	
	/**
	 * @return How much mana the wizard regenerates each step
	 */
	public double getManaRegeneration()
	{
		return this.manaregeneration;
	}
	
	/**
	 * @return The maximum health of the wizard
	 */
	public int getMaxHP()
	{
		return this.maxhp;
	}
	
	/**
	 * @return The current health of the wizard
	 */
	public int getHP()
	{
		return this.hp;
	}
	
	/**
	 * @return Which side of the screen the wizard is on
	 */
	public ScreenSide getScreenSide()
	{
		return this.screenside;
	}
	
	/**
	 * @return The wizard's avatar
	 */
	public Avatar getAvatar()
	{
		return this.avatar;
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Sets up the hud that is used for drawing the wizard's statistics
	 */
	public void setupHud()
	{
		if (this.huddrawer == null)
			this.huddrawer = new WizardHud(this.area, this);
	}
	
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
		else if (status == WizardStatus.PHASING)
			setAlpha((float) (0.5 + 0.5 * Math.sin(Math.PI/2 + 
					newstatus / 100 * Math.PI/4)));
		
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
	
	/**
	 * Causes the wizard to take 1 damage (if possible), which can also cause 
	 * death
	 */
	public void causeDamage()
	{
		// If the wizard is invincible, damage is not done
		if (this.invincibilitytime > 0 || 
				getStatusStrength(WizardStatus.PHASING) > 0)
			return;
		
		// Otherwise deals 1 damage and makes the wizard immune to damage 
		// for some time (or die)
		this.hp --;
		
		if (this.hp <= 0)
			die();
		else
		{
			this.invincibilitytime = this.invincibilitydelay;
			
			// If the wizard didn't die, plays a damage dialog
			this.voiceplayer.playDialogEvent(DialogEvent.DAMAGE, this);
		}
	}
	
	/**
	 * Makes the wizard reappera with full HP and Mana
	 */
	public void respawn()
	{
		// The wizard moves back to the center and comes back to life
		setY(GameSettings.SCREENHEIGHT / 2);
		this.hp = this.maxhp;
		this.mana = 100;
		this.invincibilitytime = this.invincibilitydelay;
		activate();
		setVisible();
	}
	
	/**
	 * @return How large portion of the last spell has been casted at the moment 
	 * [0, 1]
	 */
	protected double getCastingProgress()
	{
		// Only works if the wizard is casting
		if (!isCasting())
			return 1;
		else
			return 1 - (this.castdelay / this.lastcastdelay);
	}
	
	private void move(int movementsign, double steps)
	{
		// Doesn't work if the wizard is paralyzed
		if (getStatusStrength(WizardStatus.PARALYZED) > 0)
			return;
		
		// Haste affects accelration
		double speedchange = this.accelration * steps * 
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
			new TeleportEffect((int) getX(), (int) getY(), this.area);
			// Teleports
			addPosition(0, movementsign * this.teleportdistance);
		}
		this.doubletaptime = this.teleportdelay;
	}
	
	// Casts the spell desided by the two active elements
	private void castSpell()
	{
		if (!isCasting())
		{
			// Remembers the amount of mana before casting
			this.manabeforecasting = (int) getMana();
			getCurrentSpell().execute(this, this.ballrelay, this.area);
		}
	}
	
	private void regenerateMana(double steps)
	{
		// Only regenerates mana while not casting
		if (!isCasting())
			adjustMana(this.manaregeneration * steps);
	}
	
	private void die()
	{
		// If the rebirth spell was active, the wizard respawns instead of 
		// dying
		if (getStatusStrength(WizardStatus.REBIRTH) > 0)
		{
			adjustStatus(WizardStatus.REBIRTH, -100);
			
			// Also shouts a special punchline
			this.voiceplayer.playDialogEvent(DialogEvent.SPECIAL, this);
			
			respawn();
			return;
		}
		
		// The wizard disappears for a moment with an explosion
		new ExplosionEffect((int) getX(), (int) getY(), this.area);
		// Also kills the ball(s)
		Ball[] balls = this.ballrelay.getBalls();
		for (int i = 0; i < balls.length; i++)
		{
			balls[i].kill();
		}
		
		// Informs the scorekeeper
		if (this.scorekeeper != null)
			this.scorekeeper.score(getScreenSide().getOppositeSide());
		
		// Also plays a dialog
		this.voiceplayer.playDialogEvent(DialogEvent.LOSS, this);
		
		inactivate();
		setInvisible();
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
		if (index < 0 || index >= this.elements.length)
			return Element.NOELEMENT;
		
		return this.elements[index];
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
		if (index >= this.elements.length - 1)
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
			return this.elements.length - 1;
		else
			return index - 1;
	}
	
	private void depleteStatusses(double steps)
	{
		// Doesn't deplete statusses if they haven't been initialized yet
		if (this.statusses == null)
			return;
		
		for (WizardStatus status: this.statusses.keySet())
			adjustStatus(status, -this.statusdepletionrate * steps);
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
			if (direction == UP)
				this.elementindex1 = getNextElementIndex(this.elementindex1);
			else
				this.elementindex1 = getPreviousElementIndex(this.elementindex1);
		}
		else
		{
			if (direction == UP)
				this.elementindex2 = getNextElementIndex(this.elementindex2);
			else
				this.elementindex2 = getPreviousElementIndex(this.elementindex2);
		}
		
		// Updates the current spell
		this.currentspell = this.elements[this.elementindex1].getSpell(
				this.elements[this.elementindex2]);
		// Informs the hud about the change
		if (this.huddrawer != null)
			this.huddrawer.callSpellUpdate();
		
		// Prepares to play a sound combo
		this.elementsoundtime = 40;
		this.elementsoundactivated = true;
	}
}
