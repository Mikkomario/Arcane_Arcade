package arcane_arcade_worlds;

import arcane_arcade_field.ScreenSide;
import arcane_arcade_status.Element;

/**
 * Fieldsetting desides witch wizards to use, the basic riso regeneration rate 
 * and other stats.
 * 
 *
 * @author Mikko Hilpinen & Unto Solala.
 *         Created 4.9.2013.
 */
public class FieldSetting implements AreaSetting
{
	// ATTRIBUTES	----------------------------------------------------
	
	private int ballnumber, victorypoints, maximumElementNumber;
	private double spelldelaymodifier, manaregenerationmodifier;
	private Element[] usedelementsleft, usedelementsright;
	private int currentleftelementnumber, currentrightelementnumber;
	
	
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates the setting used in the field. The setting contains the given 
	 * information
	 *
	 * @param ballnumber How many balls will be on the field
	 * @param victorypoints How many points are needed to win the match
	 * @param spelldelaymodifier How much the spelldelay is increased / 
	 * decreased (default = 1)
	 * @param manaregenerationmodifier How much faster the mana regenerates 
	 * in the match
	 * @param leftsideelements What elements does the player on the left use
	 * @param rightsideelements What elements does the player on the right use
	 * @param elementnumber How many elements the wizards have
	 */
	public FieldSetting(int ballnumber, int victorypoints, 
			double spelldelaymodifier, double manaregenerationmodifier,
			int elementnumber)
	{
		// Initializes attributes
		this.ballnumber = ballnumber;
		this.victorypoints = victorypoints;
		this.spelldelaymodifier = spelldelaymodifier;
		this.manaregenerationmodifier = manaregenerationmodifier;
		this.usedelementsleft = new Element[elementnumber];
		this.usedelementsright = new Element[elementnumber];
		this.maximumElementNumber = elementnumber;
		this.currentleftelementnumber = 0;
		this.currentrightelementnumber = 0;
	}
	
	
	// GETTERS & SETTERS	----------------------------------------------
	
	/**
	 * @return How many balls will be created into the field
	 */
	public int getBallNumber()
	{
		return this.ballnumber;
	}
	
	/**
	 * @return How many points are needed to win the game
	 */
	public int getVictoryPoints()
	{
		return this.victorypoints;
	}
	
	/**
	 * @return How much larger / smaller the spelldelay is from normal 
	 * (default 1)
	 */
	public double getSpellDelayModifier()
	{
		return this.spelldelaymodifier;
	}
	
	/**
	 * @return How much larger / smaller the mana regeneration is from the 
	 * normal (default 1)
	 */
	public double getManaRegenerationModifier()
	{
		return this.manaregenerationmodifier;
	}
	
	/**
	 * Returns the elements the wizard on the given side uses
	 *
	 * @param side Which side's wizard's elements are asked for
	 * @return The elements used on the given screenside
	 */
	public Element[] getElementsOnSide(ScreenSide side)
	{
		switch (side)
		{
			case LEFT: return this.usedelementsleft;
			case RIGHT: return this.usedelementsright;
			default: return null;
		}
	}
	
	/**
	 * @return The maximum number of magical elements in the field.
	 */
	public int getMaximumElementNumber()
	{
		return this.maximumElementNumber;
	}
	
	/**
	 * Adds the given element to the given side.
	 * 
	 * @param element	The element that will be added to the wizard's arsenal.
	 * @param side	Which wizard gets the new element added, LEFT or RIGHT?
	 */
	public void addElementOnSide(Element element, ScreenSide side)
	{
		switch (side)
		{
			case LEFT: 
			{
				// Only adds an element if there's room left
				if (this.currentleftelementnumber < this.usedelementsleft.length)
				{
					this.usedelementsleft[this.currentleftelementnumber] = element;
					this.currentleftelementnumber ++;
				}
				break;
			}
			case RIGHT: 
			{
				// Only adds an element if there's room left
				if (this.currentrightelementnumber < this.usedelementsright.length)
				{
					this.usedelementsright[this.currentrightelementnumber] = element;
					this.currentrightelementnumber ++;
				}
				break;
			}
		}
	}
	
	/**
	 * Removes the last added element from one side of the screen
	 *
	 * @param side The side from which an element is removed
	 */
	public void removeLastElementFromSide(ScreenSide side)
	{
		switch (side)
		{
			case LEFT:
			{
				// Checks if there's anything to remove
				if (this.currentleftelementnumber == 0)
					return;
				// Removes the element and remembers the new amount of elements
				this.currentleftelementnumber --;
				this.usedelementsleft[this.currentleftelementnumber] = null;
				break;
			}
			case RIGHT:
			{
				// Checks if there's anything to remove
				if (this.currentrightelementnumber == 0)
					return;
				// Removes the element and remembers the new amount of elements
				this.currentrightelementnumber --;
				this.usedelementsright[this.currentrightelementnumber] = null;
				break;
			}
		}
	}
	
	/**
	 * @return Are the elements ready to be used in the field. This method 
	 * should be checked before the using of these settings in the field
	 */
	public boolean elementsAreReady()
	{
		return (this.currentleftelementnumber == this.usedelementsleft.length 
				&& this.currentrightelementnumber == this.usedelementsright.length);
	}
}
