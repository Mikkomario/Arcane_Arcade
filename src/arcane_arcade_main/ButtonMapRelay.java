package arcane_arcade_main;

import java.util.HashMap;

/**
 * ButtonMapCreator creates the button-key-combinations used in the game 
 * and provides them to the objects that need them
 *
 * @author Mikko Hilpinen.
 *         Created 1.9.2013.
 */
public class ButtonMapRelay
{
	// ATTRIBUTES	------------------------------------------------------
	
	// TODO: Add file reading at some point
	
	private HashMap<Buttons, Character> leftwizardbuttons;
	private HashMap<Buttons, Character> rightwizardbuttons;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new buttonmapcreator and maps the buttons
	 */
	public ButtonMapRelay()
	{
		// Initializes attributes
		this.leftwizardbuttons = new HashMap<Buttons, Character>();
		this.rightwizardbuttons = new HashMap<Buttons, Character>();
		
		this.leftwizardbuttons.put(Buttons.UP, 'w');
		this.leftwizardbuttons.put(Buttons.DOWN, 's');
		this.leftwizardbuttons.put(Buttons.LEFT_ELEMENT_UP, 'q');
		this.leftwizardbuttons.put(Buttons.LEFT_ELEMENT_DOWN, 'a');
		this.leftwizardbuttons.put(Buttons.RIGHT_ELEMENT_UP, 'e');
		this.leftwizardbuttons.put(Buttons.RIGHT_ELEMENT_DOWN, 'd');
		this.leftwizardbuttons.put(Buttons.CAST, 'c');
		
		this.rightwizardbuttons.put(Buttons.UP, 'i');
		this.rightwizardbuttons.put(Buttons.DOWN, 'k');
		this.rightwizardbuttons.put(Buttons.LEFT_ELEMENT_UP, 'u');
		this.rightwizardbuttons.put(Buttons.LEFT_ELEMENT_DOWN, 'j');
		this.rightwizardbuttons.put(Buttons.RIGHT_ELEMENT_UP, 'o');
		this.rightwizardbuttons.put(Buttons.RIGHT_ELEMENT_DOWN, 'l');
		this.rightwizardbuttons.put(Buttons.CAST, 'b');
	}
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return The buttonmap that holds the buttons of the left wizard
	 */
	public HashMap<Buttons, Character> getLeftWizardButtons()
	{
		return this.leftwizardbuttons;
	}
	
	/**
	 * @return The buttonmap that holds the buttons of the right wizard
	 */
	public HashMap<Buttons, Character> getRightWizardButtons()
	{
		return this.rightwizardbuttons;
	}
}
