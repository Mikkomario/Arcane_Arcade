package arcane_arcade_spells;

import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import worlds.Room;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.ScreenSide;
import arcane_arcade_field.Wizard;
import arcane_arcade_spelleffects.FreezeSpellEffect;

/**
 * Freeze spell creates a powerful burst in front of the caster. The timing 
 * has to be precise since the burst is very short.
 * 
 * @author Mikko Hilpinen. 
 * Created 18.1.2014
 */
public class FreezeSpell extends Spell
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new freeze spell
	 */
	public FreezeSpell()
	{
		super(CASTDELAY_NORMAL, MPUSE_HIGH);
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected void createEffects(Wizard caster, BallRelay ballrelay,
			DrawableHandler drawer, ActorHandler actorhandler,
			CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, Room room)
	{
		// Creates a freeze burst
		int x = (int) caster.getX() + 120;
		 if (caster.getScreenSide() == ScreenSide.RIGHT)
			 x -= 240;
		
		new FreezeSpellEffect(x, (int) caster.getY(), 
				drawer, collidablehandler, actorhandler, room); 
	}

	@Override
	public String getName()
	{
		return "Frost Burst";
	}

	@Override
	protected String getSimpleDescription()
	{
		return "Creates a short but powerful burst in front of the caster. "
				+ "The burst impacts the ball and causes freezing.";
	}

}
