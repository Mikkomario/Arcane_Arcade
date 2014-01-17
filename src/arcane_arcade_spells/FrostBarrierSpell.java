package arcane_arcade_spells;

import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import worlds.Room;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.ScreenSide;
import arcane_arcade_field.Wizard;
import arcane_arcade_spelleffects.FrostBarrierEffect;

/**
 * Frost barrier spell conjures a frozen wall in front of the caster. The wall 
 * can be run through with fire but otherwise it's very effective
 * 
 * @author Mikko Hilpinen. 
 * Created 18.1.2014
 */
public class FrostBarrierSpell extends Spell
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new frost barrier spell
	 */
	public FrostBarrierSpell()
	{
		super(CASTDELAY_VERY_LONG, MPUSE_HIGH);
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	protected void createEffects(Wizard caster, BallRelay ballrelay,
			DrawableHandler drawer, ActorHandler actorhandler,
			CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, Room room)
	{
		// Creates a frost barrier in front of the caster
		int x = (int) caster.getX() + 120;
		if (caster.getScreenSide() == ScreenSide.RIGHT)
			x -= 240;
		
		new FrostBarrierEffect(x, (int) caster.getY(), drawer, 
				collidablehandler, actorhandler, room, 
				(int) (getCastDelay() * 0.75), caster.getScreenSide());
	}

	@Override
	public String getName()
	{
		return "Frost Barrier";
	}

	@Override
	protected String getSimpleDescription()
	{
		return "Creates a solid wall of frost in front of the caster. The wall "
				+ "deflects the ball if it collides with it though if the ball "
				+ "is on fire it will simply melt through.";
	}
}
