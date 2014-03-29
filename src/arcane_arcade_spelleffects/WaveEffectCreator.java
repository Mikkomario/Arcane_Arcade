package arcane_arcade_spelleffects;

import java.util.Random;

import utopia_gameobjects.BasicPhysicDrawnObject;
import utopia_helpAndEnums.HelpMath;
import utopia_helpAndEnums.Movement;
import utopia_worlds.Area;
import arcane_arcade_field.ScreenSide;
import arcane_arcade_field.Wizard;
import arcane_arcade_main.SoundEffectPlayer;

/**
 * WaveEffect creator follows a wizard and creates three waves of waveeffects
 *
 * @author Mikko Hilpinen.
 * @since 29.8.2013.
 */
public class WaveEffectCreator extends FollowerSpellEffectCreator
{	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new waveeffectcreator that will create the new waves
	 * @param area The area where the objects will be placed to
	 * @param caster The wizard who casted the spell
	 */
	public WaveEffectCreator(Area area, Wizard caster)
	{
		super(40, 19, 5, caster, area);
		
		// Also plays a wave sound
		SoundEffectPlayer.playSoundEffect("strongwave");
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	protected void createEffect(Area area)
	{
		// Only works if there's an object to follow
		if (getFollowedObject() == null)
		{
			System.err.println("WaveEffectCreator can't find object to follow!");
			return;
		}
		
		// Creates a wave effect
		WaveEffect wave = new WaveEffect((int) getFollowedObject().getX(), 
				(int) getFollowedObject().getY(), area);
		
		// Changes the effects direction and speed
		double randomdir = HelpMath.checkDirection(-25 + 
				new Random().nextDouble() * 50);
		double speed = 4.5;
		
		wave.setMovement(Movement.createMovement(randomdir, speed));
		
		// If the caster was on the right side of the screen, the movement is 
		// inverted
		Wizard caster = (Wizard) getFollowedObject();
		if (caster.getScreenSide() == ScreenSide.RIGHT)
			wave.setMovement(wave.getMovement().getOpposingMovement());
		
		// Also adds the followed objec'ts movement speed
		if (getFollowedObject() instanceof BasicPhysicDrawnObject)
			wave.addVelocity(0, caster.getMovement().getVSpeed());
		// Changes the effects angle as well
		wave.setAngle(wave.getMovement().getDirection());
	}
	
	@Override
	protected void onBurstEnd()
	{
		// Adjusts the burst size after each burst
		this.adjustBurstSize(-2);
	}
}
