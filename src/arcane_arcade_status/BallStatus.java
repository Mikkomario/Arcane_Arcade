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
	 * A flaming ball is not as easliy affected by water or ice type spells, 
	 * a strong flame also dries wet and melts ice
	 */
	FLAMING, 
	/**
	 * A wet ball is not so easily affected by fire type spells but ice 
	 * and lightningn attacks become more effective against it. A very wet 
	 * ball might cause a flaming condition to go away. Wet condition also 
	 * washes off mud.
	 */
	WET, 
	/**
	 * A frozen ball moves slower than normally and fire attacks are not as 
	 * effective against it. However, earth type spells are very effective. 
	 * If the ball is deeply frozen, it will cause a flaming condition to 
	 * disappear.
	 */
	FROZEN, 
	/**
	 * A muddy ball moves slower than normally and fire and lightning attacks 
	 * are not as effective against it. Wind attack however are super effective. 
	 * Mud makes fire go away more easily than other conditions.
	 */
	MUDDY, 
	/**
	 * A charged ball moves faster and makes lightning attacks more effective. 
	 * Also dries wet.
	 */
	CHARGED, 
	/**
	 * An empty status that does absolutely nothing
	 */
	NOSTATUS;
}
