package arcane_arcade_spells;

import utopia_worlds.Area;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.ScreenSide;
import arcane_arcade_field.Wizard;
import arcane_arcade_main.SoundEffectPlayer;
import arcane_arcade_spelleffects.ExplosionEffect;
import arcane_arcade_status.BallStatus;
import arcane_arcade_status.Element;

/**
 * Explosionspell creates an explosion that will last a while and then 
 * disappear.
 *
 * @author Mikko Hilpinen.
 * @since 28.8.2013.
 */
public class ExplosionSpell extends Spell
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new explosionspell
	 */
	public ExplosionSpell()
	{
		super(CASTDELAY_LONG, MPUSE_SEMI_HIGH);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected void createEffects(Wizard caster, BallRelay ballrelay, Area area)
	{
		 int x = (int) caster.getX() + 120;
		 if (caster.getScreenSide() == ScreenSide.RIGHT)
			 x -= 240;
		
		new ExplosionEffect(x, (int) caster.getY(), area); 
		
		// Also plays an explosion sound effect
		SoundEffectPlayer.playSoundEffect("explosion");
	}

	@Override
	public String getName()
	{
		return "Blaze Burst";
	}

	@Override
	protected String getSimpleDescription()
	{
		return "Creates a strong burst of blaze just in front of you. Causes "
				+ "heavy impact.";
	}

	@Override
	public Element getFirstEffectElement()
	{
		return Element.BLAZE;
	}

	@Override
	public Element getSecondEffectElement()
	{
		return Element.NOELEMENT;
	}
	
	@Override
	public BallStatus getCausedStatus()
	{
		return BallStatus.FLAMING;
	}
}
