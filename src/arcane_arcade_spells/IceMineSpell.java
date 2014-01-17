package arcane_arcade_spells;

import java.util.Random;

import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import helpAndEnums.Movement;
import worlds.Room;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.ScreenSide;
import arcane_arcade_field.Wizard;
import arcane_arcade_spelleffects.IceMineEffect;

/**
 * Ice mine spell scatters multiple ice mines to the field when cast
 * 
 * @author Mikko Hilpinen. 
 * Created 17.12014
 */
public class IceMineSpell extends Spell
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new IceMineSpell
	 */
	public IceMineSpell()
	{
		super(CASTDELAY_LONG, MPUSE_SEMI_HIGH);
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected void createEffects(Wizard caster, BallRelay ballrelay,
			DrawableHandler drawer, ActorHandler actorhandler,
			CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, Room room)
	{
		// Creates 3 ice mines and sets them moving as well
		Random rand = new Random();
		
		for (int i = 0; i < 3; i++)
		{
			int lifetime = 40 + rand.nextInt(20);
			IceMineEffect neweffect = new IceMineEffect((int) caster.getX(), 
					(int) caster.getY(), drawer, collidablehandler, actorhandler, 
					room, lifetime);
			
			double dir = -45 + rand.nextDouble() * 90;
			if (caster.getScreenSide() == ScreenSide.RIGHT)
				dir += 180;
			double speed = 5 + rand.nextDouble() * 10;
			
			neweffect.setMovement(Movement.movementSum(
					Movement.createMovement(dir, speed), caster.getMovement()));
		}
	}

	@Override
	public String getName()
	{
		return "Blaze Crystals";
	}

	@Override
	protected String getSimpleDescription()
	{
		return "Shoots 3 frost crystals that remain on the field for a short "
				+ "while. The crystals explodee upon contact with the ball, "
				+ "causing impact. One can affect the projectile trajectory by "
				+ "moving while casting.";
	}
}