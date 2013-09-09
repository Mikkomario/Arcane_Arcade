package arcane_arcade_spelleffects;

import java.util.Random;

import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import helpAndEnums.Movement;
import worlds.Room;

/**SmokeEffectCreator creates SmokeEffects.
 * 
 * @author Unto Solala
 * 			Creator 29.8.2013
 *
 */
public class SmokeEffectCreator extends SpellEffectCreator {
	
	//ATTRIBUTES----------------------------------------------------
	
	private Random randomGenerator;
	private int x;
	private int y;
	private DrawableHandler drawer;
	private CollidableHandler collidablehandler;
	private CollisionHandler collisionhandler;
	private ActorHandler actorhandler;
	private Room room;
	

	//CONSTRUCTOR--------------------------------------------------.-
	
	/**Creates a new SmokeEffectCreator, which in turn creates new SmokeEffects
	 * 
	 * @param duration	How long will the creator live (steps).
	 * @param creationdelay	How many steps there will be between each creation.
	 * @param burstsize	How many spelleffects are created at once.
	 * @param actorhandler	The actorhandler that informs the object about steps.
	 * @param room	The room in which the creator is created.
	 * @param x	The x-coordinates of the creator.
	 * @param y	The y-coordinates of the creator.
	 * @param drawer	The drawer that will draw the smoke.
	 * @param collidablehandler	The collidablehandler that will handle the 
	 * smoke's collision checking.
	 * @param collisionhandler	Informs the smokeEffects about collisions.	
	 */
	public SmokeEffectCreator(int duration,
			ActorHandler actorhandler, Room room, int x, int y,
			DrawableHandler drawer, CollidableHandler collidablehandler,
			CollisionHandler collisionhandler) {
		super(duration, 20, 3, actorhandler, room);
		this.randomGenerator = new Random();
		this.x = x;
		this.y = y;
		this.drawer = drawer;
		this.collidablehandler = collidablehandler;
		this.collisionhandler = collisionhandler;
		this.actorhandler = actorhandler;
		this.room = room;
	}

	//IMPLEMENTED METHODS--------------------------------------------------
	
	@Override
	protected void createEffect() {
		// Direction is a random number between 0 and 360
		double randomDirection = (this.randomGenerator.nextDouble() * 360);
		// Speed is a random number between 1 and 3
		double randomSpeed = (1 + (this.randomGenerator.nextDouble() * 3));
		//Creates a new smokeEffect which lasts somewhere between 50-150 steps
		SmokeEffect smokeScreen = new SmokeEffect(this.x, this.y, this.drawer,
				this.collidablehandler, this.collisionhandler,
				this.actorhandler, this.room,
				(int) (50 + this.randomGenerator.nextDouble() * 100));
		smokeScreen.setMovement(Movement.createMovement(randomDirection, randomSpeed));
		// Friction of the smoke-plumes
		smokeScreen.setFriction(0.04);
	}

	@Override
	protected void onBurstEnd()
	{
		// Does nothing
	}
}
