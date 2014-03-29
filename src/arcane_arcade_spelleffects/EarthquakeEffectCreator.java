package arcane_arcade_spelleffects;

import utopia_worlds.Area;


/**
 * This creator creates two earthquake effects with different sizes with a 
 * moderate delay.
 * 
 * @author Mikko Hilpinen. 
 * @since 18.1.2014
 */
public class EarthquakeEffectCreator extends SpellEffectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private int x, y;
	private double currentscaling;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new earthquakeCreator that immediately starts to create effects
	 * 
	 * @param x The x-coordinate of the origin of the bursts
	 * @param y The y-coordinate of the origin of the bursts
	 * @param area The area where the objects will be placed to
	 */
	public EarthquakeEffectCreator(int x, int y, Area area)
	{
		super(80, 39, 1, area);

		// Initializes attributes
		this.x = x;
		this.y = y;
		this.currentscaling = 1;
	}

	@Override
	protected void createEffect(Area area)
	{
		// Creates a new earthquake effect with a certain amount of scaling
		new EarthquakeEffect(this.x, this.y, area).setScale(
				this.currentscaling, this.currentscaling);
		
		// Adjusts the scaling
		this.currentscaling *= 1.33;
	}

	@Override
	protected void onBurstEnd()
	{
		// Does nothing
	}
}
