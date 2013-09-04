package arcane_arcade_worlds;

import java.util.ArrayList;

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
	private ArrayList<Element> usedelementsleft, usedelementsright;
	
	
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
	 */
	public FieldSetting(int ballnumber, int victorypoints, 
			double spelldelaymodifier, double manaregenerationmodifier, 
			ArrayList<Element> leftsideelements, 
			ArrayList<Element> rightsideelements,
			int maximumElementNumber)
	{
		// Initializes attributes
		this.ballnumber = ballnumber;
		this.victorypoints = victorypoints;
		this.spelldelaymodifier = spelldelaymodifier;
		this.manaregenerationmodifier = manaregenerationmodifier;
		this.usedelementsleft = leftsideelements;
		this.usedelementsright = rightsideelements;
		this.maximumElementNumber = maximumElementNumber;
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
	public ArrayList<Element> getElementsOnSide(ScreenSide side)
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
				this.usedelementsleft.add(element);
				break;
			}
			case RIGHT: 
			{
				this.usedelementsright.add(element);
				break;
			}
		}
	}
}
