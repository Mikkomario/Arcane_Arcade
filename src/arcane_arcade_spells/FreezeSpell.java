package arcane_arcade_spells;

import utopia_worlds.Area;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.ScreenSide;
import arcane_arcade_field.Wizard;
import arcane_arcade_spelleffects.FreezeSpellEffect;
import arcane_arcade_status.BallStatus;
import arcane_arcade_status.Element;

/**
 * Freeze spell creates a powerful burst in front of the caster.
 * 
 * @author Mikko Hilpinen. 
 * @since 18.1.2014
 */
public class FreezeSpell extends Spell
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new freeze spell
	 */
	public FreezeSpell()
	{
		super(CASTDELAY_NORMAL, MPUSE_MEDIUM);
	}

	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected void createEffects(Wizard caster, BallRelay ballrelay, Area area)
	{
		// Creates a freeze burst
		int x = (int) caster.getX() + 120;
		 if (caster.getScreenSide() == ScreenSide.RIGHT)
			 x -= 240;
		
		new FreezeSpellEffect(x, (int) caster.getY(), area); 
	}

	@Override
	public String getName()
	{
		return "Frost Burst";
	}

	@Override
	protected String getSimpleDescription()
	{
		return "Creates a lenghty but less powerful burst in front of the caster. "
				+ "The burst impacts the ball.";
	}

	@Override
	public Element getFirstEffectElement()
	{
		return Element.FROST;
	}

	@Override
	public Element getSecondEffectElement()
	{
		return Element.NOELEMENT;
	}
	
	@Override
	public BallStatus getCausedStatus()
	{
		return BallStatus.FROZEN;
	}
}
