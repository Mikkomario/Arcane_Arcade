package arcane_arcade_spelleffects;

import utopia_gameobjects.DrawnObject;
import utopia_worlds.Area;


/**
 * This class works like a spelleffectcreator except that it follows a specified 
 * object around, keeping track of its position and speed
 *
 * @author Mikko Hilpinen.
 * @since 29.8.2013.
 */
public abstract class FollowerSpellEffectCreator extends SpellEffectCreator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private DrawnObject followedobject;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new followerspelleffectcreator that follows the specified object.
	 *
	 * @param duration How long the creator remains active
	 * @param creationdelay How long interval is kept between object creations 
	 * (in steps)
	 * @param burstsize How many objects are created at once
	 * @param followedobject The object the creator keeps track of
	 * @param area The area where the objects will be placed to
	 */
	public FollowerSpellEffectCreator(int duration, int creationdelay,
			int burstsize, DrawnObject followedobject, Area area)
	{
		super(duration, creationdelay, burstsize, area);
		
		this.followedobject = followedobject;
		//System.out.println("Created a creator that follows " + followedobject);
	}
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return The object the creator keeps track of
	 */
	protected DrawnObject getFollowedObject()
	{
		return this.followedobject;
	}
	
	/**
	 * Changes the object the creator follows
	 *
	 * @param newfollowed The new followed object
	 */
	protected void setFollowedObject(DrawnObject newfollowed)
	{
		this.followedobject = newfollowed;
	}
}
