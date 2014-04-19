package arcane_arcade_main;

import java.util.HashMap;

import utopia_fileio.AbstractFileWriter;
import utopia_fileio.FileReader;


/**
 * Options class loads the chosen settings at the start of the game, provides 
 * them to the other elements and may also write new settings as a new save
 *
 * @author Mikko Hilpinen.
 * @since 5.9.2013.
 */
public class Options
{
	// ATTRIBUTES	------------------------------------------------------
	
	/**
	 * The button mappings the wizard on the left side of the screen uses
	 */
	public static HashMap<Buttons, Character> leftwizardbuttons = 
			new HashMap<Buttons, Character>();
	/**
	 * The button mappings the wizard on the right side of the screen uses
	 */
	public static HashMap<Buttons, Character> rightwizardbuttons = 
			new HashMap<Buttons, Character>();
	/**
	 * How many desibels each sound effect's volume should be adjusted from 
	 * the default value
	 */
	public static int soundvolumeadjustment = 0;
	/**
	 * How many desibels each voice effect's volume should be adjusted from 
	 * the default value
	 */
	public static int voicevolumeadjustment = 0;
	/**
	 * How many desibels each music's volume should be adjusted from 
	 * the default value
	 */
	public static int musicvolumeadjustment = 0;
	/**
	 * Should the game start in full screen mode or not
	 */
	public static boolean fullscreenon = false;
	/**
	 * How much panning there can be in the sound effects and voices at maximum. 
	 * (High pan can be disturbing if the user wears headphones) [0, 1]
	 */
	public static double maxenvironmentalpan = 1;
	
	
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
	
	/**
	 * Loads the used settings from a text file t the default location
	 */
	public static void loadSettings()
	{
		OptionsReader reader = new Options().new OptionsReader();
		reader.loadSettings();
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	// Optionssaver saves the current settings into the save file
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
		 * 7 = Panning intro
		 * 8 = Panning
		 * 9 = Fullscreen intro
		 * 10 = Fullscreen
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
						"right side playe.\n&buttons2";
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
				case 5: return "\n* Here are the volume settings for sound, voice " +
						"and music.\n&soundvolume";
				case 6: return soundvolumeadjustment + "\n&musicvolume\n" + 
						musicvolumeadjustment + "\n&voicevolume\n" + 
						voicevolumeadjustment;
				case 7: return "\n* Here is the maximum pan the sound effects " +
						"and voices can have [0,1]\n&maxpan";
				case 8: return maxenvironmentalpan + "";
				case 9: return "\n* This determines if the game will start " +
						"in full screen mode or not.\n&fullscreen";
				case 10: return fullscreenon + "";
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
	
	// Optionsreader updates the settings by reading them from a file
	private class OptionsReader extends FileReader
	{
		// ATTRIBUTES	-------------------------------------------------
		
		/*
		 * The loadphase goes as follows
		 * 0 = Find next phase
		 * 1 = Reads left wizard buttons
		 * 2 = Reads right wizard buttons
		 * 3 = Reads sound volume
		 * 4 = Reads music volume
		 * 5 = Reads voice volume
		 * 6 = Reds maximum pan
		 * 7 = Reads the full screen effect
		 */
		private int loadphase = 0;
		
		
		// IMPLEMENTED METHODS	-----------------------------------------
		
		@Override
		protected void onLine(String line)
		{	
			//System.out.println(line);
			
			// If the line starts with &, changes the loadphase
			if (line.startsWith("&"))
			{
				String phaseline = line.substring(1);
				switch (phaseline)
				{
					case "buttons1": this.loadphase = 1; break;
					case "buttons2": this.loadphase = 2; break;
					case "soundvolume": this.loadphase = 3; break;
					case "musicvolume": this.loadphase = 4; break;
					case "fullscreen": this.loadphase = 7; break;
					case "voicevolume": this.loadphase = 5; break;
					case "maxpan": this.loadphase = 6;
					default: System.err.println("An unknown command: '" + 
							phaseline + "' in the " + "usersettings file!"); break;
				}
			}
			// Otherwise tries to update different data according to the 
			// loadphase
			else
			{
				switch (this.loadphase)
				{
					// Tries to read a button mapping for the left side
					case 1:
					{
						String[] parts = line.split("#");
						// Reads the button part
						Buttons button = readAsButton(parts[0]);
						// Reads the character part
						char key = parts[1].charAt(0);
						// Adds the new mapping to the button mappings
						//System.out.println("Button: " + button + ", key: " + key);
						leftwizardbuttons.put(button, key);
						break;
					}
					// Tries to read a button mapping for the right side
					case 2:
					{
						String[] parts = line.split("#");
						Buttons button = readAsButton(parts[0]);
						char key = parts[1].charAt(0);
						// Adds the new mapping to the button mappings
						rightwizardbuttons.put(button, key);
						break;
					}
					// Changes the sound volume
					case 3: soundvolumeadjustment = readAsInt(line); break;
					// Changes the music volume
					case 4: musicvolumeadjustment = readAsInt(line); break;
					// Changes the voice volume
					case 5: voicevolumeadjustment = readAsInt(line); break;
					// Changes the maximum pan
					case 6: maxenvironmentalpan = readAsDouble(line); break;
					// Changes the full screen modifier
					case 7: fullscreenon = line.equals("true"); break;
					default: System.err.println("The options reader doesn't " +
							"know what to do to the line " + line);
				}
			}
		}
		
		
		// OTHER METHODS	---------------------------------------------
		
		private void loadSettings()
		{
			this.loadphase = 0;
			readFile(GameSettings.OPTIONSDATALOCATION, "*");
		}
		
		private int readAsInt(String intstring)
		{
			int value = 0;
			
			try
			{
				value = Integer.parseInt(intstring);
			}
			catch (NumberFormatException nfe)
			{
				System.err.println("The options couldn't be read from a " +
						"save file since " + intstring + " is not an integer!");
				nfe.printStackTrace();
			}
			
			return value;
		}
		
		private double readAsDouble(String doublestring)
		{
			double value = 0;
			
			try
			{
				value = Double.parseDouble(doublestring);
			}
			catch (NumberFormatException nfe)
			{
				System.err.println("The options couldn't be read from a " +
						"save file since " + doublestring + " is not an double!");
				nfe.printStackTrace();
			}
			
			return value;
		}
		
		private Buttons readAsButton(String buttonstring)
		{
			Buttons button = null;
			for (Buttons b: Buttons.values())
			{
				if (b.toString().equals(buttonstring))
				{
					button = b;
					break;
				}
			}
			
			// Prints an error message if the button was not found
			if (button == null)
				System.err.println("There's an invalid button mapping in " +
						"the user settings. " + buttonstring + 
						" is not a button!");
			
			return button;
		}
	}
}
