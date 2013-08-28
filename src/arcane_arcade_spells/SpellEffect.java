package arcane_arcade_spells;

import java.awt.Graphics2D;
import java.util.ArrayList;

import worlds.Room;

import listeners.AnimationListener;
import listeners.RoomListener;

import arcane_arcade_field.Ball;
import arcane_arcade_field.Wizard;
import arcane_arcade_main.Main;
import arcane_arcade_status.Element;

import graphic.SpriteDrawer;
import handleds.Collidable;
import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import helpAndEnums.CollisionType;
import helpAndEnums.DoublePoint;
import drawnobjects.BasicPhysicDrawnObject;

/**
 * Spelleffects vary quite much but each of them has one or two elements and 
 * each of them is only temporary. Some might also cause status effects and 
 * some might cause status effects.<p>
 * 
 * Also, each spell effect is represented with a sprite and some may move and / 
 * or rotate.
 *
 * @author Mikko Hilpinen.
 *         Created 28.8.2013.
 */
public abstract class SpellEffect extends BasicPhysicDrawnObject implements 
		RoomListener, AnimationListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private SpriteDrawer spritedrawer;
	private boolean spellcollision, ballcollision, wizardcollision;
	private Element element1, element2;
	private DeathType deathtype;
	private int lifeleft;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new spelleffect with the given information to the given position
	 *
	 * @param x The effects's x-coordinate
	 * @param y The effects's y-coordinate
	 * @param depth The effect's drawing depth
	 * @param collisiontype The effect's collision type (= shape)
	 * @param drawer The drawer that will draw the effect
	 * @param collidablehandler The collidablehandler that will handle the 
	 * effect's collision checking (null if not colliding)
	 * @param collisionhandler The collisionhandler that will inform the object 
	 * about collisions (null if the spell doesn't collide with other spells)
	 * @param actorhandler The actorhandler that will call the spell's act 
	 * event
	 * @param spritename The name of the sprite in the spritebank 'spells'
	 * @param collidesWithSpells Does the spell collide with other spells. If 
	 * this is on, the spell must call setBoxCollisionPrecision or 
	 * setCircleCollision and setRadius methods in its constructor.
	 * @param collidesWithBalls Will the spell react to collisions with balls
	 * @param collidesWithWizards Will the spell react to collisions with 
	 * wizards
	 * @param element1 The primary element of the spell (use NOELEMENT if the 
	 * spell has no elements
	 * @param element2 The secondary element of the spell (use NOELEMENT if the 
	 * spell doesn't have two elements)
	 * @param deathtype How will the spelleffect die
	 * @param lifetime How long will the spelleffect live (steps) (negative 
	 * value means that the spell will remain alive until killed)
	 */
	public SpellEffect(int x, int y, int depth,
			CollisionType collisiontype, DrawableHandler drawer,
			CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, ActorHandler actorhandler, 
			String spritename, boolean collidesWithSpells, 
			boolean collidesWithBalls, boolean collidesWithWizards, 
			Element element1, Element element2, DeathType deathtype, 
			int lifetime)
	{
		super(x, y, depth, 
				collidesWithBalls || collidesWithSpells || collidesWithWizards, 
				collisiontype, drawer, collidablehandler, collisionhandler, 
				actorhandler);
		
		// Initializes attributes
		this.spellcollision = collidesWithSpells;
		this.ballcollision = collidesWithBalls;
		this.wizardcollision = collidesWithWizards;
		this.element1 = element1;
		this.element2 = element2;
		this.deathtype = deathtype;
		this.lifeleft = lifetime;
		this.spritedrawer = new SpriteDrawer(
				Main.spritebanks.getBank("spells").getSprite(spritename), 
				actorhandler);
		
		// Sets up the deaths
		if (this.deathtype == DeathType.ANIMATION)
		{
			this.spritedrawer.setImageIndex(0);
			this.spritedrawer.setImageSpeed(
					this.spritedrawer.getSprite().getImageNumber() / lifetime);
			this.spritedrawer.getAnimationListenerHandler().addAnimationListener(this);
		}
		else if (this.deathtype == DeathType.FADE)
			setAlpha(0);
	}
	
	
	// ABSTRACT METHODS	-------------------------------------------------
	
	/**
	 * This method is called when the spell collides with a ball (if it needs 
	 * to react to it somehow)
	 *
	 * @param ball The ball the spell collided with
	 * @param x The average x-coordinate of the collision
	 * @param y The average y-coordinate of the collision
	 * @see #collidesWithBalls()
	 */
	public abstract void onBallCollision(Ball ball, int x, int y);
	
	/**
	 * This method is called when the spell collides with another spelleffect 
	 * (if it needs to react to it somehow)
	 *
	 * @param spell The spelleffect the spell collided with
	 * @param x The average x-coordinate of the collision
	 * @param y The average y-coordinate of the collision
	 */
	public abstract void onSpellCollision(SpellEffect spell, int x, int y);
	
	/**
	 * This method is called when the spell collides with a wizard (if it needs 
	 * to react to it somehow)
	 *
	 * @param wizard The wizard the spell collided with
	 * @param x The average x-coordinate of the collision
	 * @param y The average y-coordinate of the collision
	 * @see #collidesWithWizards()
	 */
	public abstract void onWizardCollision(Wizard wizard, int x, int y);
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onCollision(ArrayList<DoublePoint> colpoints,
			Collidable collided)
	{
		// If the spell collides with other spells, may react to them
		if (this.spellcollision && collided instanceof SpellEffect)
		{
			// Calculates the center collision point
			double x = colpoints.get(0).getX();
			double y = colpoints.get(0).getY();
			
			for (int i = 1; i < colpoints.size(); i++)
			{
				x += colpoints.get(i).getX();
				y += colpoints.get(i).getY();
			}
			
			x /= colpoints.size();
			y /= colpoints.size();
			
			onSpellCollision((SpellEffect) collided, (int) x, (int) y);
		}
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
		return this.spritedrawer.getSprite().getOriginX();
	}

	@Override
	public int getOriginY()
	{
		return this.spritedrawer.getSprite().getOriginY();
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		// Draws the sprite
		this.spritedrawer.drawSprite(g2d);
	}
	
	@Override
	public boolean kill()
	{
		// Also kills the spritedrawer
		this.spritedrawer.kill();
		
		return super.kill();
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
	public void onAnimationEnd(SpriteDrawer spritedrawer)
	{
		// If the effect is supposed to die at the end of the animation, dies
		if (this.deathtype == DeathType.ANIMATION)
			kill();
	}
	
	@Override
	public void act()
	{
		super.act();
		
		// Lifetime decreases
		this.lifeleft --;
		// Checks if the object should die
		if (this.lifeleft == 0)
			kill();
		// Changes the alpha if needed
		else if (this.deathtype == DeathType.FADE)
		{
			if (this.lifeleft < 20)
				setAlpha((float)((20 - this.lifeleft) / 20.0));
			else if (getAlpha() < 1)
				setAlpha(getAlpha() + 0.1f);
		}
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * @return Should the ball call onBallCollision method when it collides 
	 * with the spellEffect
	 * @see #onBallCollision(Ball, int, int)
	 */
	public boolean collidesWithBalls()
	{
		return this.ballcollision;
	}
	
	/**
	 * @return Should the wizard call onWizardCollision method when it collides 
	 * with the spellEffect
	 * @see #onWizardCollision(Wizard, int, int)
	 */
	public boolean collidesWithWizards()
	{
		return this.wizardcollision;
	}
	
	// TODO: Add force modifier calculations after ball has statusses
	
	
	// ENUMERATIONS	-----------------------------------------------------
	
	/**
	 * DeathType represents the way the spellEffect will die / stop being
	 *
	 * @author Mikko Hilpinen.
	 *         Created 28.8.2013.
	 */
	public enum DeathType
	{
		/**
		 * The spellEffect will simply die after a certain amount of steps 
		 * has passed. No extra effects included
		 */
		TIME, 
		/**
		 * The spellEffect will die when it reaches the end of its sprites 
		 * animation. The speed of the animation will be set to last a certain 
		 * amount of steps
		 */
		ANIMATION, 
		/**
		 * The spell will quickly fade in and then fade out when a certain amount 
		 * of steps has passed
		 */
		FADE;
	}
}
