package arcane_arcade_spelleffects;

import java.util.Random;

import arcane_arcade_field.Wizard;
import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.DrawableHandler;
import helpAndEnums.HelpMath;
import helpAndEnums.Movement;
import worlds.Room;
import drawnobjects.BasicPhysicDrawnObject;

/**
 * WaveEffect creator follows a wizard and creates three waves of waveeffects
 *
 * @author Mikko Hilpinen.
 *         Created 29.8.2013.
 */
public class WaveEffectCreator extends FollowerSpellEffectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private DrawableHandler drawer;
	private ActorHandler actorhandler;
	private CollidableHandler collidablehandler;
	private Room room;
	private Random rand;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new waveeffectcreator that will create the new waves
	 * @param drawer The drawablehandler that will draw the spells
	 * @param actorhandler The actorhandler that will call the creator's and 
	 * waves' act event
	 * @param collidablehandler The collidable that will check the spells' 
	 * collisions
	 * @param room The room where the spells are created at
	 * @param caster The wizard who casted the spell
	 */
	public WaveEffectCreator(DrawableHandler drawer, ActorHandler actorhandler, 
			CollidableHandler collidablehandler, Room room, Wizard caster)
	{
		super(40, 15, 3, actorhandler, room, caster);
		
		// Initializes attributes
		this.drawer = drawer;
		this.actorhandler = actorhandler;
		this.collidablehandler = collidablehandler;
		this.room = room;
		this.rand = new Random();
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	protected void createEffect()
	{
		// Creates a wave effect
		WaveEffect wave = new WaveEffect((int) getFollowedObject().getX(), 
				(int) getFollowedObject().getY(), this.drawer, 
				this.collidablehandler, this.actorhandler, this.room);
		// Changes the effects direction and speed
		double randomdir = HelpMath.checkDirection(-30 + 
				this.rand.nextDouble() * 60);
		double randomspeed = 6 + this.rand.nextDouble() * 3;
		wave.setMovement(Movement.createMovement(randomdir, randomspeed));
		// Also adds the followed objec'ts movement speed
		if (getFollowedObject() instanceof BasicPhysicDrawnObject)
			wave.addVelocity(0, ((BasicPhysicDrawnObject) 
					getFollowedObject()).getMovement().getVSpeed() * 0.8);
		// Changes the effects angle as well
		wave.setAngle(wave.getMovement().getDirection());
	}
}
