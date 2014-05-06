package arcane_arcade_spells;

import utopia_worlds.Area;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.ScreenSide;
import arcane_arcade_field.Wizard;
import arcane_arcade_spelleffects.FrostBarrierEffect;
import arcane_arcade_status.BallStatus;
import arcane_arcade_status.Element;

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
	protected void createEffects(Wizard caster, BallRelay ballrelay, Area area)
	{
		// Creates a frost barrier in front of the caster
		int x = (int) caster.getX() + 120;
		if (caster.getScreenSide() == ScreenSide.RIGHT)
			x -= 240;
		
		new FrostBarrierEffect(x, (int) caster.getY(), 
				(int) (getCastDelay() * 0.9), caster.getScreenSide(), area);
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

	@Override
	public Element getFirstEffectElement()
	{
		return Element.FROST;
	}

	@Override
	public Element getSecondEffectElement()
	{
		return Element.TIDE;
	}
	
	@Override
	public BallStatus getCausedStatus()
	{
		return BallStatus.NOSTATUS;
	}
}
