package arcane_arcade_status;

/**
 * There are multiple different status effects a ball might 
 * have. Each causes the ball to be affected more or less strongly by different 
 * elements.
 *
 * @author Mikko Hilpinen.
 *         Created 28.8.2013.
 */
public enum BallStatus
{
	/**
	 * Tide, Frost, Gale and Lush spells become less effective against flaming 
	 * balls while the power of toxic spells increases. Flaming balls explode 
	 * when they collide with walls. A flame dries wet and burns lush as well.
	 */
	FLAMING, 
	/**
	 * Blaze, Frost and Toxic spells are less effective against wet balls while 
	 * Volt becomes stronger. Wet balls shift vertically. Wet melts frozen and 
	 * removes blight. Wet increases Growth and Charge.
	 */
	WET, 
	/**
	 * Blaze, Earth and Lush spells are less effective against frozen balls while 
	 * Tide spells become stronger. Frozen balls are not affected by air friction. 
	 * Frozen puts out flames but boosts wet
	 */
	FROZEN, 
	/**
	 * Blaze, Tide, Gale and Volt are less effective against petrified balls. 
	 * Frost and Lush spells become stronger. Petrification decreases the changes 
	 * in the ball's status. Petrification removes charge as well
	 */
	PETRIFIED, 
	/**
	 * A Volt and Gale spells are more effective against charged balls. Charge 
	 * makes the ball gain speed from air friction. Charge also removes wet.
	 */
	CHARGED, 
	/**
	 * Tide and Volt spells are less effective against grown balls while Blaze, 
	 * Gale and Toxic become stronger. Growth makes the ball grow in size and 
	 * removes charge and inreases flaming condition.
	 */
	GROWTH, 
	/**
	 * Lush spells are less effective against blighted balls while Blaze and 
	 * Tide become stronger. Blighted balls leave toxic puddles when they collide 
	 * with walls.
	 */
	BLIGHT,
	/**
	 * An empty status that does absolutely nothing
	 */
	NOSTATUS;
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * @return A table containing the statuses that should get boosted by this 
	 * particular status effect
	 */
	public BallStatus[] getBuffedStatuses()
	{
		switch (this)
		{
			case WET: BallStatus[] wetbuffs = {GROWTH, CHARGED}; return wetbuffs;
			case FROZEN: BallStatus[] frozenbuffs = {WET}; return frozenbuffs;
			case GROWTH: BallStatus[] growthbuffs = {FLAMING}; return growthbuffs;
			
			default: return new BallStatus[0];
		}
	}
	
	/**
	 * @return A table containing the statuses that should get decreased by this 
	 * particular status effect
	 */
	public BallStatus[] getDebuffedStatuses()
	{
		switch (this)
		{
			case FLAMING: BallStatus[] flamingdebuffs = {GROWTH, WET}; return flamingdebuffs;
			case WET: BallStatus[] wetdebuffs = {FROZEN, BLIGHT}; return wetdebuffs;
			case FROZEN: BallStatus[] frozendebuffs = {FLAMING}; return frozendebuffs;
			case PETRIFIED: BallStatus[] petrifieddebuffs = {CHARGED}; return petrifieddebuffs;
			case CHARGED: BallStatus[] chargeddebuffs = {WET}; return chargeddebuffs;
			case GROWTH: BallStatus[] growthdebuffs = {CHARGED}; return growthdebuffs;
			
			default: return new BallStatus[0];
		}
	}
}
