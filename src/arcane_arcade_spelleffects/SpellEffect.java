package arcane_arcade_spelleffects;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import utopia_gameobjects.BasicPhysicDrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handleds.Collidable;
import utopia_utility.CollisionType;
import utopia_utility.HelpMath;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import utopia_worlds.Room;
import arcane_arcade_field.Ball;
import arcane_arcade_field.SpellTarget;
import arcane_arcade_field.Wizard;
import arcane_arcade_status.Element;

/**
 * Spelleffects vary quite much but each of them has one or two elements and 
 * each of them is only temporary. Some might also cause status effects and 
 * some might cause status effects.<p>
 * 
 * Also, each spell effect is represented with a sprite and some may move and / 
 * or rotate.
 *
 * @author Mikko Hilpinen.
 * @since 28.8.2013.
 */
public abstract class SpellEffect extends BasicPhysicDrawnObject implements 
		RoomListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private SingleSpriteDrawer spritedrawer;
	private Element element1, element2;
	private int lifetime;
	private boolean fadesin, fadesout, sizeeffect, scalesin, scalesout;
	private int fadein, fadeout, scalein, scaleout;
	private double minscale, lifeleft;
	private Class<?>[] collidedClasses;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new spelleffect with the given information to the given position
	 *
	 * @param x The effects's x-coordinate
	 * @param y The effects's y-coordinate
	 * @param depth The effect's drawing depth
	 * @param collisiontype The effect's collision type (= shape)
	 * @param spritename The name of the sprite in the spritebank 'spells'
	 * @param spellsCollideWithThis Can other spells collide with this one?
	 * @param collidesWithBalls Will the spell react to collisions with balls
	 * @param collidesWithWizards Will the spell react to collisions with 
	 * wizards
	 * @param element1 The primary element of the spell (use NOELEMENT if the 
	 * spell has no elements
	 * @param element2 The secondary element of the spell (use NOELEMENT if the 
	 * spell doesn't have two elements)
	 * @param lifetime How long will the spelleffect live (steps) (negative 
	 * value means that the spell will remain alive until killed)
	 * @param area The area where the object is placed to
	 */
	public SpellEffect(int x, int y, int depth,
			CollisionType collisiontype, String spritename, boolean spellsCollideWithThis, 
			boolean collidesWithBalls, boolean collidesWithWizards, 
			Element element1, Element element2, int lifetime, Area area)
	{
		// TODO: Sort out this collides with ... stuff
		
		super(x, y, depth, 
				collidesWithBalls || spellsCollideWithThis || collidesWithWizards, 
				collisiontype, area);
		
		// Initializes attributes
		this.element1 = element1;
		this.element2 = element2;
		this.lifeleft = lifetime;
		this.lifetime = lifetime;
		this.spritedrawer = new SingleSpriteDrawer(MultiMediaHolder.getSpriteBank(
				"spells").getSprite(spritename), area.getActorHandler(), this);
		this.fadesin = false;
		this.fadesout = false;
		this.sizeeffect = false;
		this.scalein = 0;
		this.scalesin = false;
		this.scaleout = 0;
		this.scalesout = false;
		this.minscale = 1;
		
		initializeCollidedClassArray(collidesWithWizards, collidesWithBalls, 
				spellsCollideWithThis);
	}
	
	
	// ABSTRACT METHODS	-------------------------------------------------
	
	/**
	 * This method is called when the spell collides with a ball (if it needs 
	 * to react to it somehow)
	 *
	 * @param ball The ball the spell collided with
	 * @param x The average x-coordinate of the collision
	 * @param y The average y-coordinate of the collision
	 */
	public abstract void onBallCollision(Ball ball, double x, double y);
	
	/**
	 * This method is called when the spell collides with another spellEffect 
	 * (if it needs to react to it somehow)
	 *
	 * @param spell The spelleffect the spell collided with
	 * @param x The average x-coordinate of the collision
	 * @param y The average y-coordinate of the collision
	 */
	public abstract void onSpellCollision(SpellEffect spell, double x, double y);
	
	/**
	 * This method is called when the spell collides with a wizard (if it needs 
	 * to react to it somehow)
	 *
	 * @param wizard The wizard the spell collided with
	 * @param x The average x-coordinate of the collision
	 * @param y The average y-coordinate of the collision
	 */
	public abstract void onWizardCollision(Wizard wizard, double x, double y);
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public Class<?>[] getSupportedListenerClasses()
	{
		return this.collidedClasses;
	}
	
	@Override
	public void onCollision(ArrayList<Point2D.Double> colpoints,
			Collidable collided, double steps)
	{
		// If the spell collides with other spells, may react to them
		if (collided instanceof SpellEffect)
		{
			Point2D.Double averagepoint = 
					HelpMath.getAveragePoint(colpoints);
			onSpellCollision((SpellEffect) collided, averagepoint.x, 
					averagepoint.y);
		}
		else if (collided instanceof SpellTarget)
			((SpellTarget) collided).onSpellCollision(this, steps);
	}

	@Override
	public int getWidth()
	{
		return this.spritedrawer.getSprite().getWidth();
	}

	@Override
	public int getHeight()
	{
		return this.spritedrawer.getSprite().getHeight();
	}

	@Override
	public int getOriginX()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getOriginX();
	}

	@Override
	public int getOriginY()
	{
		if (this.spritedrawer == null)
			return 0;
		return this.spritedrawer.getSprite().getOriginY();
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// Draws the sprite
		this.spritedrawer.drawSprite(g2d, 0, 0);
		
		// Testing
		drawCollisionArea(g2d);
	}
	
	@Override
	public void onRoomStart(Room room)
	{
		// Does nothing
	}
	
	@Override
	public void onRoomEnd(Room room)
	{
		// Dies if the room ends
		kill();
	}
	
	@Override
	public void act(double steps)
	{
		super.act(steps);
		
		// Lifetime decreases
		this.lifeleft -= steps;
		// Checks if the object should die
		if (this.lifeleft <= 0)
			kill();
		else
		{
			// Changes the object's size if needed
			if (this.sizeeffect)
			{
				double scale = Math.sin((this.lifeleft / this.lifetime) 
						* Math.PI);
				setScale(scale, scale);
			}
			// Changes the alpha if needed
			if (this.fadesout && this.lifeleft < this.fadeout)
			{
				setAlpha(1 - (this.fadeout - (float) this.lifeleft) / this.fadeout);
			}
			if (this.fadesin && this.lifetime - this.lifeleft < this.fadein && 
					getAlpha() < 1)
			{
				setAlpha(getAlpha() + (float) (steps / this.fadein));
			}
			// Changes the object's scaling if needed
			if (this.scalesout && this.lifeleft < this.scaleout)
			{
				double scale = (1 - this.minscale * (this.scaleout - 
						this.lifeleft) / this.scaleout);
				setScale(scale, scale);
			}
			if (this.scalesin && this.lifetime - this.lifeleft < this.scalein && 
					getXScale() < 1)
			{
				double scale = getXScale() + this.minscale / this.scalein;
				setScale(scale, scale);
			}
		}
	}
	
	
	// GETTERS & SETTTERS	----------------------------------------------
	
	/**
	 * @return The spritedrawer used to draw the object
	 */
	protected SingleSpriteDrawer getSpriteDrawer()
	{
		return this.spritedrawer;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Makes the spell's animation last until the spell is dead
	 */
	protected void addAnimationEffect()
	{
		this.spritedrawer.setImageIndex(0);
		this.spritedrawer.setAnimationDuration((int) this.lifeleft);
		//this.spritedrawer.getAnimationListenerHandler().addAnimationListener(this);
	}
	
	/**
	 * Makes the effect first grow to full size and then disappear
	 */
	protected void addSizeEffect()
	{
		this.sizeeffect = true;
		setScale(0, 0);
	}
	
	/**
	 * Makes the object fade in and / or fade out at certain points of its 
	 * life
	 *
	 * @param fadedin When has the effect fully faded in (in steps, use a 
	 * negative number if no fade in is used)
	 * @param startsfadeout When the effect will start to fade out (in steps, 
	 * use a negative number if no fade out is used)
	 */
	protected void addFadeEffect(int fadedin, int startsfadeout)
	{
		if (fadedin > 0)
		{
			setAlpha(0);
			this.fadesin = true;
			this.fadein = fadedin;
		}
		if (startsfadeout >= 0)
		{
			this.fadesout = true;
			this.fadeout = startsfadeout;
		}
	}
	
	/**
	 * Makes the effect first grow larger and / or then shrink
	 *
	 * @param scaledin When has the effect become full-sized (steps, use a negative 
	 * number if the object doesn't scale in)
	 * @param startscaleout When will the effect start to shrink (steps, use a 
	 * negative number if you don't want the effect to shrink)
	 * @param minscale How small can the effect possibly be / become (< 1)
	 */
	protected void addScaleEffect(int scaledin, int startscaleout, 
			double minscale)
	{
		if (scaledin > 0)
		{
			this.scalesin = true;
			this.scalein = scaledin;
			this.minscale = minscale;
			setScale(minscale, minscale);
		}
		if (startscaleout >= 0)
		{
			this.scalesout = true;
			this.scaleout = startscaleout;
		}
	}
	
	/**
	 * @return Should the ball call onBallCollision method when it collides 
	 * with the spellEffect
	 * @see #onBallCollision(Ball, double, double)
	 */
	/*
	public boolean collidesWithBalls()
	{
		return this.ballcollision;
	}
	*/
	
	/**
	 * @return Should the wizard call onWizardCollision method when it collides 
	 * with the spellEffect
	 * @see #onWizardCollision(Wizard, double, double)
	 */
	/*
	public boolean collidesWithWizards()
	{
		return this.wizardcollision;
	}
	*/
	
	/**
	 * Calculates the force modifier that affects the size of the impact caused 
	 * to the ball.
	 *
	 * @param ball The ball to which impact will be added
	 * @return How large should the impact be proportionally (1 means it stays 
	 * the same, 2 twice as large and so on)
	 */
	protected double getForceModifier(Ball ball)
	{
		return Element.getForceModifier(this.element1, this.element2, ball);
	}
	
	private void initializeCollidedClassArray(boolean collidesWithWizards, 
			boolean collidesWithBalls, boolean collidesWithSpells)
	{
		ArrayList<Class<?>> collidedClasses = new ArrayList<Class<?>>();
		
		if (collidesWithWizards)
			collidedClasses.add(Wizard.class);
		if (collidesWithBalls)
			collidedClasses.add(Ball.class);
		if (collidesWithSpells)
			collidedClasses.add(SpellEffect.class);
		
		// Turns the arraylist into an array and adds spellTarget
		this.collidedClasses = new Class<?>[collidedClasses.size() + 1];
		this.collidedClasses[0] = SpellTarget.class;
		
		for (int i = 0; i < collidedClasses.size(); i++)
		{
			this.collidedClasses[i + 1] = collidedClasses.get(i);
		}
	}
}
