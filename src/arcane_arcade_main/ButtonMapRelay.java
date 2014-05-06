package arcane_arcade_main;

import java.util.HashMap;

/**
 * ButtonMapCreator creates the button-key-combinations used in the game 
 * and provides them to the objects that need them
 *
 * @author Mikko Hilpinen.
 *         Created 1.9.2013.
 * @deprecated This class is not needed anymore because the options class will 
 * handle button mapping in the future. The class isn't removed yet however 
 * since the button mappings are not saved anywhere yet.
 */
@Deprecated
public class ButtonMapRelay
{
	// ATTRIBUTES	------------------------------------------------------
	
	// TODO: Add file reading at some point
	
	private HashMap<Button, Character> leftwizardbuttons;
	private HashMap<Button, Character> rightwizardbuttons;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new buttonmapcreator and maps the buttons
	 */
	public ButtonMapRelay()
	{
		// Initializes attributes
		this.leftwizardbuttons = new HashMap<Button, Character>();
		this.rightwizardbuttons = new HashMap<Button, Character>();
		
		this.leftwizardbuttons.put(Button.UP, 'w');
		this.leftwizardbuttons.put(Button.DOWN, 's');
		this.leftwizardbuttons.put(Button.LEFT_ELEMENT_UP, 'q');
		this.leftwizardbuttons.put(Button.LEFT_ELEMENT_DOWN, 'a');
		this.leftwizardbuttons.put(Button.RIGHT_ELEMENT_UP, 'e');
		this.leftwizardbuttons.put(Button.RIGHT_ELEMENT_DOWN, 'd');
		this.leftwizardbuttons.put(Button.CAST, 'c');
		
		this.rightwizardbuttons.put(Button.UP, 'i');
		this.rightwizardbuttons.put(Button.DOWN, 'k');
		this.rightwizardbuttons.put(Button.LEFT_ELEMENT_UP, 'u');
		this.rightwizardbuttons.put(Button.LEFT_ELEMENT_DOWN, 'j');
		this.rightwizardbuttons.put(Button.RIGHT_ELEMENT_UP, 'o');
		this.rightwizardbuttons.put(Button.RIGHT_ELEMENT_DOWN, 'l');
		this.rightwizardbuttons.put(Button.CAST, 'b');
	}
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return The buttonmap that holds the buttons of the left wizard
	 */
	public HashMap<Button, Character> getLeftWizardButtons()
	{
		return this.leftwizardbuttons;
	}
	
	/**
	 * @return The buttonmap that holds the buttons of the right wizard
	 */
	public HashMap<Button, Character> getRightWizardButtons()
	{
		return this.rightwizardbuttons;
	}
}
