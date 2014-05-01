package arcane_arcade_spelleffects;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import utopia_gameobjects.BasicPhysicDrawnObject;
import utopia_handleds.Collidable;
import utopia_utility.CollisionType;
import utopia_utility.DepthConstants;
import utopia_utility.Movement;
import utopia_worlds.Area;

/**
 * Movingspelleffectcreator moves to a certain direction with a certain speed 
 * and friction while creating spell effects
 *
 * @author Mikko Hilpinen.
 * @since 29.8.2013.
 */
public abstract class MovingSpellEffectCreator extends FollowerSpellEffectCreator
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new creator to the given position moving with the given 
	 * movement and friction
	 *
	 * @param duration How long will the creator live (steps)
	 * @param creationdelay How many steps there will be between each creation
	 * @param burstsize How many spelleffects are created at once
	 * @param x The creator's x-coordinate
	 * @param y The creator's y-coordinate
	 * @param initialmovement The movement the creator has when its created
	 * @param friction The friction the creator has (speed / step)
	 * @param area The area where the object is placed to
	 */
	public MovingSpellEffectCreator(int duration, int creationdelay,
			int burstsize, int x, int y, Movement initialmovement, 
			double friction, Area area)
	{
		super(duration, creationdelay, burstsize, null, area);
		setFollowedObject(new FollowedPhysicObject(x, y, initialmovement, 
				friction, area));
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	private class FollowedPhysicObject  extends BasicPhysicDrawnObject
	{
		// CONSTRUCTOR	--------------------------------------------------
		
		/**
		 * Creates a new followedphysicobject to the given position added 
		 * to the given handlers
		 *
		 * @param x The object's x-coordinate
		 * @param y The object's y-coordinate
		 * @param initialmovement What kind of movement the object has at 
		 * the beginning
		 * @param friction How much friction the object has 
		 * @param area The area where the object is placed to
		 */
		public FollowedPhysicObject(int x, int y, Movement initialmovement, 
				double friction, Area area)
		{
			super(x, y, DepthConstants.NORMAL, false, CollisionType.BOX, area);
			
			// Sets movement & friction
			setMovement(initialmovement);
			setFriction(friction);
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------

		@Override
		public void onCollision(ArrayList<Point2D.Double> colpoints,
				Collidable collided, double steps)
		{
			// Doesn't react to collisions
		}

		@Override
		public int getWidth()
		{
			return 0;
		}

		@Override
		public int getHeight()
		{
			return 0;
		}

		@Override
		public int getOriginX()
		{
			return 0;
		}

		@Override
		public int getOriginY()
		{
			return 0;
		}

		@Override
		public void drawSelfBasic(Graphics2D g2d)
		{
			// Doesn't draw itself
		}

		@Override
		public Class<?>[] getSupportedListenerClasses()
		{
			// No-one collides with the object
			return new Class<?>[0];
		}
	}
}
