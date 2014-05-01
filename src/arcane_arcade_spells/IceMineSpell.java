package arcane_arcade_spells;

import java.util.Random;

import utopia_utility.Movement;
import utopia_worlds.Area;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.ScreenSide;
import arcane_arcade_field.Wizard;
import arcane_arcade_spelleffects.IceMineEffect;

/**
 * Ice mine spell scatters multiple ice mines to the field when cast
 * 
 * @author Mikko Hilpinen. 
 * @since 17.12.2014
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
	protected void createEffects(Wizard caster, BallRelay ballrelay, Area area)
	{
		// Creates 4 ice mines and sets them moving as well
		Random rand = new Random();
		
		for (int i = 0; i < 4; i++)
		{
			int lifetime = 80 + rand.nextInt(50);
			IceMineEffect neweffect = new IceMineEffect((int) caster.getX(), 
					(int) caster.getY(), lifetime, area);
			
			double dir = -33 + rand.nextDouble() * 66;
			if (caster.getScreenSide() == ScreenSide.RIGHT)
				dir += 180;
			double speed = 7 + rand.nextDouble() * 5;
			
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
		return "Shoots 4 frost crystals that remain on the field for a short "
				+ "while. The crystals explodee upon contact with the ball, "
				+ "causing impact. One can affect the projectile trajectory by "
				+ "moving while casting.";
	}
}
