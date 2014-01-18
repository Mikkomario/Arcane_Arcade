package arcane_arcade_spells;

import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import worlds.Room;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.Wizard;
import arcane_arcade_spelleffects.EarthquakeEffectCreator;

/**
 * Earthquakespell creates a lenghty series of quakes.
 * 
 * @author Mikko Hilpinen. 
 * Created 18.1.2014
 */
public class EarthquakeSpell extends Spell
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new earthquake spell
	 */
	public EarthquakeSpell()
	{
		super(CASTDELAY_VERY_LONG, MPUSE_HIGH);
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected void createEffects(Wizard caster, BallRelay ballrelay,
			DrawableHandler drawer, ActorHandler actorhandler,
			CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, Room room)
	{
		new EarthquakeEffectCreator((int) caster.getX(), (int) caster.getY(), 
				drawer, actorhandler, collidablehandler, room);
	}

	@Override
	public String getName()
	{
		return "Earthquake";
	}

	@Override
	protected String getSimpleDescription()
	{
		return "Creates a series of quakes that impact the ball and cause "
				+ "muddy status. The area of effect gets larger with each quake.";
	}
}
