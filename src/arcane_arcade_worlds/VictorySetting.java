package arcane_arcade_worlds;

/**
 * Victorysetting is passed down when the field phase ends and the victory 
 * phase starts
 *
 * @author Mikko Hilpinen.
 *         Created 3.9.2013.
 */
public class VictorySetting implements AreaSetting
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private int leftsidepoints;
	private int rightsidepoints;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new victorysetting with the given information
	 *
	 * @param leftsidepoints
	 * @param rightsidepoints
	 */
	public VictorySetting(int leftsidepoints, int rightsidepoints)
	{
		// Initializes attributes
		this.leftsidepoints = leftsidepoints;
		this.rightsidepoints = rightsidepoints;
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return The points the left screen side had at the end of the match
	 */
	public int getLeftSidePoints()
	{
		return this.leftsidepoints;
	}
	
	/**
	 * @return The points the right screen side had at the end of the match
	 */
	public int getRightSidePoints()
	{
		return this.rightsidepoints;
	}
}
