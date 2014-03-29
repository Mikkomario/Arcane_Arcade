package arcane_arcade_spelleffects;

import java.util.Random;

import utopia_helpAndEnums.Movement;
import utopia_worlds.Area;


/**
 * SmokeEffectCreator creates SmokeEffects.
 * 
 * @author Unto Solala & Mikko Hilpinen
 * @since 29.8.2013
 */
public class SmokeEffectCreator extends SpellEffectCreator
{
	//ATTRIBUTES----------------------------------------------------
	
	private Random randomGenerator;
	private int x;
	private int y;
	

	//CONSTRUCTOR--------------------------------------------------.-
	
	/**Creates a new SmokeEffectCreator, which in turn creates new SmokeEffects
	 * 
	 * @param duration	How long will the creator live (steps).
	 * @param x	The x-coordinates of the creator.
	 * @param y	The y-coordinates of the creator.
	 * @param area The area where the object is placed to
	 */
	public SmokeEffectCreator(int duration, int x, int y, Area area)
	{
		super(duration, 30, 3, area);
		this.randomGenerator = new Random();
		this.x = x;
		this.y = y;
	}

	//IMPLEMENTED METHODS--------------------------------------------------
	
	@Override
	protected void createEffect(Area area)
	{
		// Direction is a random number between 0 and 360
		double randomDirection = (this.randomGenerator.nextDouble() * 360);
		// Speed is a random number
		double randomSpeed = (1 + (this.randomGenerator.nextDouble() * 3.5));
		//Creates a new smokeEffect which lasts somewhere between 80-160 steps
		SmokeEffect smokeScreen = new SmokeEffect(this.x, this.y, 
				(int) (80 + this.randomGenerator.nextDouble() * 80), area);
		smokeScreen.setMovement(Movement.createMovement(randomDirection, randomSpeed));
		// Friction of the smoke-plumes
		smokeScreen.setFriction(0.04);
	}

	@Override
	protected void onBurstEnd()
	{
		// Does nothing
	}
}
