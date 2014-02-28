package arcane_arcade_spelleffects;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import gameobjects.BasicPhysicDrawnObject;
import handleds.Collidable;
import handlers.ActorHandler;
import helpAndEnums.CollisionType;
import helpAndEnums.DepthConstants;
import helpAndEnums.Movement;
import worlds.Room;

/**
 * Movingspelleffectcreator moves to a certain direction with a certain speed 
 * and friction while creating spell effects
 *
 * @author Mikko Hilpinen.
 *         Created 29.8.2013.
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
	 * @param actorhandler The actorhandler that informs the object about steps
	 * @param room The room in which the creator is created
	 * @param x The creator's x-coordinate
	 * @param y The creator's y-coordinate
	 * @param initialmovement The movement the creator has when its created
	 * @param friction The friction the creator has (speed / step)
	 */
	public MovingSpellEffectCreator(int duration, int creationdelay,
			int burstsize, ActorHandler actorhandler, Room room, 
			int x, int y, Movement initialmovement, double friction)
	{
		super(duration, creationdelay, burstsize, actorhandler, room, null);
		setFollowedObject(new FollowedPhysicObject(x, y, initialmovement, 
				friction, actorhandler));
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
		 * @param actorhandler The actorhandler that will call the object's 
		 * act event
		 */
		public FollowedPhysicObject(int x, int y, Movement initialmovement, 
				double friction, ActorHandler actorhandler)
		{
			super(x, y, DepthConstants.NORMAL, false, CollisionType.BOX, null, 
					null, null, actorhandler);
			
			// Sets movement & friction
			setMovement(initialmovement);
			setFriction(friction);
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------

		@Override
		public void onCollision(ArrayList<Point2D.Double> colpoints,
				Collidable collided)
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
