package arcane_arcade_spells;

import utopia_worlds.Area;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.ScreenSide;
import arcane_arcade_field.Wizard;
import arcane_arcade_main.GameSettings;
import arcane_arcade_main.SoundEffectPlayer;
import arcane_arcade_spelleffects.SmokeEffectCreator;
import arcane_arcade_status.BallStatus;
import arcane_arcade_status.Element;

/**
 * SmokeScreenSpell creates a cloud of smoke, which will last for a while.
 * 
 * @author Unto Solala
 * 			Created 29.8.2013
 */
public class SmokeScreenSpell extends Spell{

	/**
	 * Creates a new SmokeScreen-spell
	 */
	public SmokeScreenSpell()
	{
		super(CASTDELAY_VERY_LONG, MPUSE_VERY_LOW);
	}

	@Override
	protected void createEffects(Wizard caster, BallRelay ballrelay, Area area) 
	{
		int x = (int)(GameSettings.SCREENWIDTH * 0.8);
		if (caster.getScreenSide() == ScreenSide.RIGHT)
			x = (int)(GameSettings.SCREENWIDTH * 0.2);
		
		new SmokeEffectCreator(350, x, (int)caster.getY(), area);
		
		// Also plays an smokescreen sound effect
		SoundEffectPlayer.playSoundEffect("smoke");
	}

	@Override
	public String getName()
	{
		return "Smokescreen";
	}

	@Override
	protected String getSimpleDescription()
	{
		return "Creates a burst of distracting smoke on the other player's "
				+ "side for a moderate duration.";
	}
	
	@Override
	public Element getFirstEffectElement()
	{
		return Element.NOELEMENT;
	}

	@Override
	public Element getSecondEffectElement()
	{
		return Element.NOELEMENT;
	}
	
	@Override
	public BallStatus getCausedStatus()
	{
		return BallStatus.NOSTATUS;
	}
}
