package interfaceElements;

import graphic.Sprite;
import handlers.ActorHandler;
import handlers.DrawableHandler;

import java.awt.Color;
import java.awt.Font;

/**
 * OptionMessageBoxes are interactive messageBoxes that show a number of 
 * buttons that represent different options the user can take. A number of 
 * users can listen to the events caused by the user-box-interaction
 * 
 * @author Mikko Hilpinen
 */
public class OptionMessageBox extends MessageBox
{
	// ATTRIBUTES	------------------------------------------------------
	
	// TODO: Add an optionbutton class for button functionalities
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * @param x The x-coordinate of the box's center
	 * @param y The y-coordinate of the box's center
	 * @param depth The drawing depth of the box
	 * @param message The message shown on the box
	 * @param textfont The font with which the message is drawn
	 * @param textcolor The color with which the text is drawn
	 * @param backgroundsprite The sprite used to draw the messageBox
	 * @param drawer The drawableHandler that will draw the box (optional)
	 * @param actorhandler The actorHandler that will animate the background 
	 * sprite (optional)
	 */
	public OptionMessageBox(int x, int y, int depth, String message,
			Font textfont, Color textcolor, Sprite backgroundsprite, 
			DrawableHandler drawer,
			ActorHandler actorhandler)
	{
		super(x, y, depth, message, textfont, textcolor, backgroundsprite, drawer,
				actorhandler);
		// TODO Auto-generated constructor stub
	}

}
