package arcane_arcade_spells;

import utopia_worlds.Area;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.Wizard;
import arcane_arcade_spelleffects.EarthquakeEffectCreator;
import arcane_arcade_status.BallStatus;
import arcane_arcade_status.Element;

/**
 * Earthquakespell creates a lenghty series of quakes.
 * 
 * @author Mikko Hilpinen. 
 * @since 18.1.2014
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
	protected void createEffects(Wizard caster, BallRelay ballrelay, Area area)
	{
		new EarthquakeEffectCreator((int) caster.getX(), (int) caster.getY(), 
				area);
	}

	@Override
	public String getName()
	{
		return "Earthquake";
	}

	@Override
	protected String getSimpleDescription()
	{
		return "Creates a series of quakes that impact the ball. The area "
				+ "of effect gets larger with each quake.";
	}

	@Override
	public Element getFirstEffectElement()
	{
		return Element.EARTH;
	}

	@Override
	public Element getSecondEffectElement()
	{
		return Element.NOELEMENT;
	}

	@Override
	public BallStatus getCausedStatus()
	{
		return BallStatus.PETRIFIED;
	}
}
