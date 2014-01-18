package arcane_arcade_spelleffects;

import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.DrawableHandler;
import worlds.Room;

/**
 * This creator creates two earthquake effects with different sizes with a 
 * moderate delay.
 * 
 * @author Mikko Hilpinen. 
 * Created 18.1.2014
 */
public class EarthquakeEffectCreator extends SpellEffectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private int x, y;
	private DrawableHandler drawer;
	private ActorHandler actorhandler;
	private CollidableHandler collidablehandler;
	private Room room;
	private double currentscaling;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new earthquakeCreator that immediately starts to create effects
	 * 
	 * @param x The x-coordinate of the origin of the bursts
	 * @param y The y-coordinate of the origin of the bursts
	 * @param drawer The drawableHandler that will draw the effects
	 * @param actorhandler The actorHandler that will inform the objects 
	 * about steps
	 * @param collidableHandler The collidableHandler that will handle the 
	 * effects' collision checking
	 * @param room The room where the creator is located at
	 */
	public EarthquakeEffectCreator(int x, int y, DrawableHandler drawer, 
			ActorHandler actorhandler, CollidableHandler collidableHandler, 
			Room room)
	{
		super(80, 39, 1, actorhandler, room);

		// Initializes attributes
		this.x = x;
		this.y = y;
		this.currentscaling = 1;
		this.drawer = drawer;
		this.actorhandler = actorhandler;
		this.collidablehandler = collidableHandler;
		this.room = room;
	}

	@Override
	protected void createEffect()
	{
		// Creates a new earthquake effect with a certain amount of scaling
		new EarthquakeEffect(this.x, this.y, this.drawer, 
				this.collidablehandler, this.actorhandler, this.room).setScale(
				this.currentscaling, this.currentscaling);
		
		// Adjusts the scaling
		this.currentscaling *= 1.33;
	}

	@Override
	protected void onBurstEnd()
	{
		// Does nothing
	}
}
