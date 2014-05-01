package arcane_arcade_spells;

import utopia_worlds.Area;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.Wizard;
import arcane_arcade_spelleffects.WaveEffectCreator;
import arcane_arcade_status.Element;

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
	protected void createEffects(Wizard caster, BallRelay ballrelay, Area area)
	{
		new WaveEffectCreator(area, caster);
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
	
	@Override
	public Element getFirstEffectElement()
	{
		return Element.TIDE;
	}

	@Override
	public Element getSecondEffectElement()
	{
		return Element.NOELEMENT;
	}
}
