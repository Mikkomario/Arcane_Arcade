package arcane_arcade_main;

import java.util.HashMap;

import common.AbstractFileWriter;

/**
 * Options class loads the chosen settings at the start of the game, provides 
 * them to the other elements and may also write new settings as a new save
 *
 * @author Mikko Hilpinen.
 *         Created 5.9.2013.
 */
public class Options
{
	// ATTRIBUTES	------------------------------------------------------
	
	/**
	 * The button mappings the wizard on the left side of the screen uses
	 */
	public static HashMap<Buttons, Character> leftwizardbuttons = null;
	/**
	 * The button mappings the wizard on the right side of the screen uses
	 */
	public static HashMap<Buttons, Character> rightwizardbuttons = null;
	/**
	 * How many desibels each sound effect's volume should be adjusted from 
	 * the default value
	 */
	public static int soundvolumeadjustment = 0;
	/**
	 * How many desibels each music's volume should be adjusted from 
	 * the default value
	 */
	public static int musicvolumeadjustment = 0;
	/**
	 * Should the game start in full screen mode or not
	 */
	public static boolean fullscreenon = false;
	
	
	// STATIC METHODS	-------------------------------------------------
	
	/**
	 * Saves the current settings into a save file. Overwriting any existing 
	 * files.
	 */
	public static void saveSettings()
	{
		OptionsSaver saver = new Options().new OptionsSaver();
		saver.saveSettings();
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	private class OptionsSaver extends AbstractFileWriter
	{
		// ATTRIBUTES	--------------------------------------------------
		
		/*
		 * The writephases go as follows:
		 * 0 = Explanation
		 * 1 = Leftwizardbuttons intro
		 * 2 = Leftwizardbuttons
		 * 3 = Rigthwizardbuttons intro
		 * 4 = Rightwizardbuttons
		 * 5 = Volume intro
		 * 6 = Volumes
		 * 7 = Fullscreen intro
		 * 8 = Fullscreen
		 */
		private int writephase;
		
		
		// IMPLEMENTED METHODS	------------------------------------------
		
		@Override
		protected String writeLine(int lineindex)
		{
			this.writephase ++;
			
			// Writes something according to the writephase
			switch(this.writephase)
			{
				case 0: return "* These are the user settings. It would be " +
						"wise to adjust them from inside the game.\n\n";
				case 1: return "* These are the button mappings for the left " +
						"side player.\n&buttons1";
				case 2:
				{
					// Collects all the left buttons
					String buttonstring = "";
					for (Buttons button: leftwizardbuttons.keySet())
					{
						buttonstring += button.toString() + "#" + 
								leftwizardbuttons.get(button) + "\n";
					}
					return buttonstring;
				}
				case 3: return "\n* These are the button mappings for the " +
						"right side playe.\nbuttons2";
				case 4:
				{
					// Collects all the left buttons
					String buttonstring = "";
					for (Buttons button: rightwizardbuttons.keySet())
					{
						buttonstring += button.toString() + "#" + 
								rightwizardbuttons.get(button) + "\n";
					}
					return buttonstring;
				}
				case 5: return "\n* Here are the volume settings for sound " +
						"and music.\n&soundvolume";
				case 6: return soundvolumeadjustment + "\n&musicvolume\n" + 
						musicvolumeadjustment;
				case 7: return "\n* This determines if the game will start " +
						"in full screen mode or not.\n&fullscreen";
				case 8: return fullscreenon + "";
				default: return END_OF_STREAM;
			}
		}
		
		
		// OTHER METHODS	----------------------------------------------
		
		private void saveSettings()
		{
			// Initializes attributes
			this.writephase = -1;
			
			// Saves settings
			saveIntoFile(GameSettings.OPTIONSDATALOCATION);
		}
	}
}
