package arcane_arcade_menus;

import java.awt.Graphics2D;
import java.awt.geom.Point2D.Double;
import java.util.HashMap;

import arcane_arcade_main.Button;
import arcane_arcade_main.GameSettings;
import arcane_arcade_main.Options;
import utopia_graphic.Sprite;
import utopia_utility.CollisionType;
import utopia_utility.DepthConstants;
import utopia_interfaceElements.AbstractButton;
import utopia_interfaceElements.MessageBox;
import utopia_interfaceElements.OptionMessageBox;
import utopia_interfaceElements.SliderIntegerOptionBar;
import utopia_interfaceElements.StringOptionBar;
import utopia_listeners.AdvancedKeyListener;
import utopia_listeners.OptionMessageBoxListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;

/**
 * OptionsIntercade handles the different option interface elements and 
 * the transactions between them. ie: creating elements and saving the settings.
 * 
 * @author Mikko Hilpinen
 * @since 20.4.2014
 */
public class OptionsInterface
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private StringOptionBar fullscreenBar;
	private SliderIntegerOptionBar wizardVolumeSlider, effectVolumeSlider, 
			maxPanSlider;
	private RebindKeysButton rebind1, rebind2;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new OptionsInterface to the given area
	 * @param area The area where the interface elements will be placed
	 */
	public OptionsInterface(Area area)
	{
		int columnx = 200;
		int columny = 100;
		int ydifference = 50;
		
		// Initializes attributes
		Sprite arrowSprite = MultiMediaHolder.getSpriteBank("menu").getSprite("arrow");
		Sprite arrowMask = MultiMediaHolder.getSpriteBank("menu").getSprite("arrowmask");
		String[] options = {"OFF", "ON"};
		int fullscreenDefaultIndex = 0;
		if (Options.fullscreenon)
			fullscreenDefaultIndex = 1;
		
		this.fullscreenBar = new StringOptionBar(columnx, columny, options, 
				fullscreenDefaultIndex, "'Fullscreen'", GameSettings.BASICFONT, 
				GameSettings.WHITETEXTCOLOR, arrowSprite, arrowMask, area);
		
		Sprite sliderBackSprite = MultiMediaHolder.getSpriteBank("menu").getSprite("sliderback");
		Sprite sliderHandleSprite = MultiMediaHolder.getSpriteBank("menu").getSprite("sliderhandle");
		
		//System.out.println("Wizard Voice: " + Options.voicevolumeadjustment);
		//System.out.println("Effects: " + Options.soundvolumeadjustment);
		
		this.wizardVolumeSlider = new SliderIntegerOptionBar(columnx, 
				columny + ydifference, Options.voicevolumeadjustment, -20, 20, 
				"Voice volume adjustment", GameSettings.BASICFONT, 
				GameSettings.WHITETEXTCOLOR, sliderBackSprite, sliderHandleSprite, 
				area);
		
		this.effectVolumeSlider = new SliderIntegerOptionBar(columnx, 
				columny + ydifference * 2, Options.soundvolumeadjustment, -20, 
				20, "Sound effect volume adjustment", GameSettings.BASICFONT, 
				GameSettings.WHITETEXTCOLOR, sliderBackSprite, sliderHandleSprite, 
				area);
		
		this.maxPanSlider = new SliderIntegerOptionBar(columnx, 
				columny + ydifference * 3, 50, 0, 100, "Maximum pan %", 
				GameSettings.BASICFONT, GameSettings.WHITETEXTCOLOR, 
				sliderBackSprite, sliderHandleSprite, area);
		
		this.rebind1 = new RebindKeysButton(columnx + 50, columny + ydifference * 5, 
				RebindKeysButton.PLAYER1, area);
		
		this.rebind2 = new RebindKeysButton(columnx + 50, columny + ydifference * 7, 
				RebindKeysButton.PLAYER2, area);
		
		new ApplyChangesButton(GameSettings.SCREENWIDTH / 2, 
				GameSettings.SCREENHEIGHT - 50, area);
	}
	
	
	// SUBCLASSES	-----------------------------------------------------
	
	private abstract class OptionButton extends AbstractButton
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private String description;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		public OptionButton(int x, int y, String description, Area area)
		{
			super(x, y, DepthConstants.HUD, 
					MultiMediaHolder.getSpriteBank("menu").getSprite("button"), 
					area);
			
			// Initializes attributes
			this.description = description;
			
			// Changes collisions
			setCollisionType(CollisionType.CIRCLE);
		}
		
		
		// ABSTRACT METHODS	--------------------------------------------
		
		protected abstract void onLeftClick();
		
		
		// IMPLEMENTED METHODS	----------------------------------------

		@Override
		public void onMouseButtonEvent(MouseButton button,
				MouseButtonEventType eventType, Double mousePosition,
				double eventStepTime)
		{
			// Checks if left mouse button was pressed
			if (button == MouseButton.LEFT && eventType == MouseButtonEventType.PRESSED)
				onLeftClick();
		}

		@Override
		public boolean listensMouseEnterExit()
		{
			return true;
		}

		@Override
		public void onMousePositionEvent(MousePositionEventType eventType,
				Double mousePosition, double eventStepTime)
		{
			if (eventType == MousePositionEventType.ENTER)
				getSpriteDrawer().setImageIndex(1);
			else if (eventType == MousePositionEventType.EXIT)
				getSpriteDrawer().setImageIndex(0);
		}
		
		@Override
		public void drawSelfBasic(Graphics2D g2d)
		{
			super.drawSelfBasic(g2d);
			
			// Also draws the description
			g2d.drawString(this.description, 100, 40);
		}
	}
	
	private class ApplyChangesButton extends OptionButton
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private Area area;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		public ApplyChangesButton(int x, int y, Area area)
		{
			super(x, y, "Apply Changes", area);
			
			// Initializes attributes
			this.area = area;
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------

		@Override
		protected void onLeftClick()
		{
			// Adjusts the options and saves them
			if (OptionsInterface.this.fullscreenBar.getValue() == 0)
				Options.fullscreenon = false;
			else
				Options.fullscreenon = true;
			
			Options.voicevolumeadjustment = 
					OptionsInterface.this.wizardVolumeSlider.getValue();
			Options.soundvolumeadjustment = 
					OptionsInterface.this.effectVolumeSlider.getValue();
			Options.maxenvironmentalpan = 
					OptionsInterface.this.maxPanSlider.getValue() / 100.0;
			
			OptionsInterface.this.rebind1.saveChanges();
			OptionsInterface.this.rebind2.saveChanges();
			
			Options.saveSettings();
			
			// Informs the user about the change
			new OptionMessageBox(GameSettings.SCREENWIDTH / 2, 
					GameSettings.SCREENHEIGHT / 2, DepthConstants.TOP, 32, 
					"Changes successfully saved!", GameSettings.BASICFONT, 
					GameSettings.WHITETEXTCOLOR, 
					MultiMediaHolder.getSpriteBank("menu").getSprite("messageback"), 
					OptionMessageBox.OKOPTIONS, 
					MultiMediaHolder.getSpriteBank("menu").getSprite("button"), 
					true, true, null, this.area);
		}
	}
	
	private class RebindKeysButton extends OptionButton implements 
			AdvancedKeyListener, OptionMessageBoxListener
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private HashMap<Button, Character> newButtons;
		private int player, currentButtonIndex;
		private Area area;
		private MessageBox lastMessageBox;
		private Button[] buttons;
		private char lastQuestionableKey;
		private boolean askingForKeys;
		
		public static final int PLAYER1 = 1, PLAYER2 = 2;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		public RebindKeysButton(int x, int y, int player, Area area)
		{
			super(x, y, "Rebind keys for player " + player, area);
			
			// Initializes attributes
			this.player = player;
			this.currentButtonIndex = -1;
			this.area = area;
			this.lastMessageBox = null;
			this.newButtons = null;
			this.buttons = Button.values();
			this.lastQuestionableKey = 'a';
			this.askingForKeys = false;
			
			// Adds the object to the handler(s)
			area.getKeyHandler().addKeyListener(this);
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------

		@Override
		protected void onLeftClick()
		{
			// Starts asking the rebinds
			this.currentButtonIndex = 0;
			this.newButtons = new HashMap<Button, Character>();
			createNewRebindPromt();
		}

		@Override
		public void onKeyDown(char key, int keyCode, boolean coded, double steps)
		{
			// Does nothing
		}

		@Override
		public void onKeyPressed(char key, int keyCode, boolean coded)
		{
			// Only works while the buttons are being rebound
			if (!this.askingForKeys)
				return;
			
			this.askingForKeys = false;
			
			// Also checks that the pressed key is a character key (= not coded)
			if (coded)
				// If so, informs the user and asks again
			{
				new OptionMessageBox(GameSettings.SCREENWIDTH / 2, 
						GameSettings.SCREENHEIGHT / 2, DepthConstants.TOP, 32, 
						"Unacceptable key, please try again with a different one", 
						GameSettings.BASICFONT, GameSettings.WHITETEXTCOLOR, 
						MultiMediaHolder.getSpriteBank("menu").getSprite("messageback"), 
						OptionMessageBox.OKOPTIONS, 
						MultiMediaHolder.getSpriteBank("menu").getSprite("button"), 
						true, true, this, this.area);
				return;
			}
			
			HashMap<Button, Character> otherBinds;
			
			if (this.player == PLAYER1)
				otherBinds = Options.rightwizardbuttons;
			else
				otherBinds = Options.leftwizardbuttons;
			
			// Checks if the bind is already in use. If so, lets the player 
			// try again or just continue
			if (this.newButtons.containsValue(key) || otherBinds.containsValue(key))
				new OptionMessageBox(GameSettings.SCREENWIDTH / 2, 
						GameSettings.SCREENHEIGHT / 2, DepthConstants.TOP, 32, 
						"The given key is already in use!#Do you want to use it anyway?", 
						GameSettings.BASICFONT, GameSettings.WHITETEXTCOLOR, 
						MultiMediaHolder.getSpriteBank("menu").getSprite("messageback"), 
						OptionMessageBox.YESNOPTIONS, 
						MultiMediaHolder.getSpriteBank("menu").getSprite("button"), 
						true, true, this, this.area);
			// Otherwise just saves the button and asks a new button
			else
			{
				saveKey(key);
				createNewRebindPromt();
			}
		}

		@Override
		public void onKeyReleased(char key, int keyCode, boolean coded)
		{
			// Does nothing
		}
		
		@Override
		public void onOptionMessageEvent(String clickedoptionname,
				int clickedoptionindex)
		{
			// If Yes was clicked, saves the button and asks a new one
			if (clickedoptionname.equalsIgnoreCase("yes"))
				saveKey(this.lastQuestionableKey);
			
			// If No or OK was clicked, just asks the button again
			createNewRebindPromt();
		}
		
		
		// OTHER METHODS	---------------------------------------------
		
		public void saveChanges()
		{
			// Only saves the changes if they are complete (and there actually 
			// are changes made)
			if (this.currentButtonIndex == -1 && this.newButtons != null)
			{
				if (this.player == PLAYER1)
					Options.leftwizardbuttons = this.newButtons;
				else
					Options.rightwizardbuttons = this.newButtons;
			}
		}
		
		private void saveKey(char key)
		{
			this.newButtons.put(this.buttons[this.currentButtonIndex], key);
			this.currentButtonIndex ++;
			
			// If the end was reached, resets the index back to -1 (= doesn't ask anymore)
			if (this.currentButtonIndex >= this.buttons.length)
				this.currentButtonIndex = -1;
		}
		
		private void createNewRebindPromt()
		{
			if (this.lastMessageBox != null)
				this.lastMessageBox.kill();
			
			// Only creates a new promt if there are more buttons to ask for
			if (this.currentButtonIndex >= this.buttons.length || this.currentButtonIndex < 0)
				return;
			
			// Collects some important information
			Button button = this.buttons[this.currentButtonIndex];
			char oldCharacter;
			
			if (this.player == PLAYER1)
				oldCharacter = Options.leftwizardbuttons.get(button);
			else
				oldCharacter = Options.rightwizardbuttons.get(button);
			
			// Creates the actual message box
			this.lastMessageBox = new MessageBox(GameSettings.SCREENWIDTH / 2, 
					GameSettings.SCREENHEIGHT / 2, DepthConstants.TOP, 32, 
					"Please press the key used for " + button + "#Previously " + 
					oldCharacter, GameSettings.BASICFONT, 
					GameSettings.WHITETEXTCOLOR, 
					MultiMediaHolder.getSpriteBank("menu").getSprite("messageback"), 
					this.area);
			
			this.askingForKeys = true;
		}
	}
}
