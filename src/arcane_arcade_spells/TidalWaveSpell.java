package arcane_arcade_spells;

import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import worlds.Room;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.Wizard;
import arcane_arcade_spelleffects.WaveEffectCreator;

/**
 * Tidalwavespell creates three waves of waves from the caster
 *
 * @author Mikko Hilpinen.
 *         Created 29.8.2013.
 */
public class TidalWaveSpell extends Spell
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new tidalwavespell ready to be casted
	 */
	public TidalWaveSpell()
	{
		super(CASTDELAY_LONG, MPUSE_MEDIUM);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected void createEffects(Wizard caster, BallRelay ballrelay,
			DrawableHandler drawer, ActorHandler actorhandler,
			CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, Room room)
	{
		new WaveEffectCreator(drawer, actorhandler, collidablehandler, room, 
				caster);
	}

	@Override
	public String getName()
	{
		return "Tide Wave";
	}


	@Override
	protected String getSimpleDescription()
	{
		return "Shoots three waves of tide with a short range. You can affect "
				+ "the projectile direction by moving. Causes moderate "
				+ "impact and wet.";
	}
}
