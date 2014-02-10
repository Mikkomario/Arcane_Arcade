package arcane_arcade_worlds;

import java.util.ArrayList;
import java.util.HashMap;

import resourcebanks.MidiMusicBank;
import resourcebanks.OpenMidiMusicBankHolder;
import resourcebanks.OpenSpriteBankHolder;
import resourcebanks.OpenWavSoundBankHolder;
import resourcebanks.SpriteBank;
import resourcebanks.WavSoundBank;
import handlers.ActorHandler;
import handlers.DrawableHandler;
import handlers.KeyListenerHandler;
import handlers.MouseListenerHandler;
import backgrounds.Background;
import arcane_arcade_field.FieldObjectCreator;
import arcane_arcade_main.GameSettings;
import arcane_arcade_menus.BattleScreenObjectCreator;
import arcane_arcade_menus.ElementScreenObjectCreator;
import arcane_arcade_menus.MainMenuObjectCreator;
import arcane_arcade_menus.VictoryScreenObjectCreator;

/**
 * Navigator is a class that handles the transition between different rooms 
 * and / or phases of the game.
 *
 * @author Mikko Hilpinen & Unto Solala
 *         Created 27.8.2013.
 */
public class Navigator
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private static HashMap<String, SpriteBank> activeSpriteBanks 
			= new HashMap<String, SpriteBank>();
	private static HashMap<String, WavSoundBank> activeWavBanks = 
			new HashMap<String, WavSoundBank>();
	private static HashMap<String, MidiMusicBank> activeMidiBanks = 
			new HashMap<String, MidiMusicBank>();
	
	private HashMap<GamePhase, SettingUsingRoom> rooms;
	private OpenSpriteBankHolder spritebankholder;
	private OpenWavSoundBankHolder wavbankholder;
	private OpenMidiMusicBankHolder midibankholder;
	private GamePhase activephase;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new navigator, also creating all the rooms used in the game
	 *
	 * @param drawer The drawablehandler that will draw the rooms
	 * @param actorhandler The actorhandler that will handle all the actors 
	 * in the rooms
	 * @param keylistenerhandler The keylistenerhandler that will inform 
	 * all keylisteners about the keypresses
	 * @param mouselistenerhandler The mouselistenerhandler that will inform 
	 * the game's objects about mouse events
	 * @param spritebankholder The spritebankholder that holds the data of 
	 * the spritebanks used in the game
	 * @param wavholder The WavSoundBankHolder that holds all the wavsoundbanks 
	 * used in the game
	 * @param midibankholder The MidiMusicBankHolder that holds all the 
	 * midiMusicBanks used in the game.
	 */
	public Navigator(DrawableHandler drawer, ActorHandler actorhandler, 
			KeyListenerHandler keylistenerhandler, 
			MouseListenerHandler mouselistenerhandler,
			OpenSpriteBankHolder spritebankholder, 
			OpenWavSoundBankHolder wavholder, 
			OpenMidiMusicBankHolder midibankholder)
	{
		// Initializes attributes
		this.rooms = new HashMap<GamePhase, SettingUsingRoom>();
		this.activephase = null;
		this.spritebankholder = spritebankholder;
		this.wavbankholder = wavholder;
		this.midibankholder = midibankholder;
		
		// Initializes the backgrounds for the rooms
		addSpriteBank("background");
		
		// Initializes the rooms
		initializeMainMenu(drawer, actorhandler, mouselistenerhandler);
		initializeField(drawer, actorhandler, keylistenerhandler);
		initializeVictoryScreen(drawer, actorhandler, mouselistenerhandler);
		initializeBattleScreen(drawer, actorhandler, mouselistenerhandler);
		initializeElementScreen(drawer, actorhandler, mouselistenerhandler, 
				keylistenerhandler);
	}
	
	
	// OTHER METHODS	------------------------------------------------
	
	/**
	 * Stops the current phase and starts a new one.
	 * 
	 * @param phase The new gamephase that will start
	 * @param setting The setting(s) used in the new phase
	 */
	public void startPhase(GamePhase phase, AreaSetting setting)
	{
		// Ends the currently active room
		if (this.activephase != null)
			this.rooms.get(this.activephase).end();
		
		// Updates the loaded resources
		updateResourceBanks(phase, ResourceBankType.MIDI);
		updateResourceBanks(phase, ResourceBankType.SPRITE);
		updateResourceBanks(phase, ResourceBankType.WAV);
		
		// Remembers the new active phase
		this.activephase = phase;
		
		// Updates the settings
		this.rooms.get(phase).setSettings(setting);
		
		// Starts the room of the given phase
		this.rooms.get(phase).start();
	}
	
	/**
	 * Returns a spritebank from the active spritebanks
	 *
	 * @param spritebankname The name of the spritebank
	 * @return The active spritebank with the given name
	 */
	public static SpriteBank getSpriteBank(String spritebankname)
	{
		if (activeSpriteBanks.containsKey(spritebankname))
			return activeSpriteBanks.get(spritebankname);
		else
		{
			System.err.println("SpriteBank named " + spritebankname + 
					" isn't currenly active!");
			return null;
		}
	}
	
	/**
	 * Returns an wavsoundbank if it has been initialized yet
	 *
	 * @param wavbankname The name of the needed wavbank
	 * @return The wavbank with the given name or null if no such bank exists 
	 * or if the bank is not active
	 */
	public static WavSoundBank getWavBank(String wavbankname)
	{
		if (activeWavBanks.containsKey(wavbankname))
			return activeWavBanks.get(wavbankname);
		else
		{
			System.err.println("WavBank named " + wavbankname + 
					" isn't currenly active!");
			return null;
		}
	}
	
	/**
	 * Returns an midiMusicBank if it has been initialized
	 *
	 * @param midibankname The name of the needed midiBank
	 * @return The midiBank with the given name or null if no such bank exists 
	 * or if the bank is not active
	 */
	public static MidiMusicBank getMidiBank(String midibankname)
	{
		if (activeMidiBanks.containsKey(midibankname))
			return activeMidiBanks.get(midibankname);
		else
		{
			System.err.println("MidiBank named " + midibankname + 
					" isn't currenly active!");
			return null;
		}
	}
	
	private void initializeField(DrawableHandler drawer, 
			ActorHandler actorhandler, KeyListenerHandler keyhandler)
	{	
		// Creates the object creator
		FieldObjectCreator creator = new FieldObjectCreator(drawer, actorhandler, 
				keyhandler, this);
		
		SettingUsingRoom field = new SettingUsingRoom(creator, 
				getSimpleBackgroundList("mountains", drawer));
		
		this.rooms.put(GamePhase.FIELD, field);
	}
	
	private void initializeMainMenu(DrawableHandler drawer, 
			ActorHandler actorhandler, MouseListenerHandler mousehandler)
	{
		// Creates the object creator
		MainMenuObjectCreator creator = 
				new MainMenuObjectCreator(drawer, actorhandler, mousehandler, 
				this);
		
		SettingUsingRoom mainmenu = new SettingUsingRoom(creator, 
				getSimpleBackgroundList("space", drawer));
		
		this.rooms.put(GamePhase.MAINMENU, mainmenu);
	}
	
	private void initializeVictoryScreen(DrawableHandler drawer,
			ActorHandler actorhandler, MouseListenerHandler mousehandler)
	{	
		// Creates the object creator
		VictoryScreenObjectCreator creator = new VictoryScreenObjectCreator(drawer,
				actorhandler, mousehandler, this);
		
		SettingUsingRoom victoryscreen = new SettingUsingRoom(creator, 
				getSimpleBackgroundList("space", drawer));
		
		this.rooms.put(GamePhase.VICTORYSCREEN, victoryscreen);
	}
	
	private void initializeBattleScreen(DrawableHandler drawer,
			ActorHandler actorhandler, MouseListenerHandler mousehandler)
	{
		// Creates the object creator
		BattleScreenObjectCreator creator = new BattleScreenObjectCreator(drawer,
				actorhandler, mousehandler, this);
		
		SettingUsingRoom battlescreen = new SettingUsingRoom(creator, 
				getSimpleBackgroundList("space", drawer));
		
		this.rooms.put(GamePhase.BATTLESETTINGMENU, battlescreen);
	}
	
	private void initializeElementScreen(DrawableHandler drawer, 
			ActorHandler actorhandler, MouseListenerHandler mousehandler, 
			KeyListenerHandler keyhandler)
	{
		// Creates the object creator
		ElementScreenObjectCreator creator = new ElementScreenObjectCreator(
				this, drawer, actorhandler, mousehandler, keyhandler);
		
		SettingUsingRoom elementscreen = new SettingUsingRoom(creator, 
				getSimpleBackgroundList("space", drawer));
		
		this.rooms.put(GamePhase.ELEMENTMENU, elementscreen);
	}
	
	// Used in the following update methods
	private void updateResourceBanks(GamePhase newphase, 
			ResourceBankType resourcetype)
	{
		String[] newbanknames = null; 
		
		switch (resourcetype)
		{
			case SPRITE: newbanknames = newphase.getUsedSpriteBanks(); break;
			case WAV: newbanknames = newphase.getUsedWavSoundBanks(); break;
			case MIDI: newbanknames = newphase.getUsedMidiMusicBanks(); break;
		}
		
		// Skips the removal progres if there was no previous phase
		if (this.activephase != null)
		{
			String[] oldbanknames = null;
			
			switch (resourcetype)
			{
				case SPRITE: oldbanknames = 
						this.activephase.getUsedSpriteBanks(); break;
				case WAV: oldbanknames = 
						this.activephase.getUsedWavSoundBanks(); break;
				case MIDI: oldbanknames = 
						this.activephase.getUsedMidiMusicBanks(); break;
			}
			
			// Takes all the new banknames into a list format
			ArrayList<String> newbanknamelist = new ArrayList<String>();
			for (int i = 0; i < newbanknames.length; i++)
			{
				newbanknamelist.add(newbanknames[i]);
			}
			
			// Removes the old banks that aren't active in the new phase
			for (int i = 0; i < oldbanknames.length; i++)
			{
				String oldbankname = oldbanknames[i];
				if (!newbanknamelist.contains(oldbankname))
				{
					switch (resourcetype)
					{
						case SPRITE: removeSpriteBank(oldbankname); break;
						case WAV: removeWavSoundBank(oldbankname); break;
						case MIDI: removeMidiMusicBank(oldbankname); break;
					}
				}
			}
		}
		
		// Adds all the new banks that weren't active already
		// (Checking done in the addSpritebank method)
		for (int i = 0; i < newbanknames.length; i++)
		{
			//addSpriteBank(newbanknames[i]);
			
			switch (resourcetype)
			{
				case SPRITE: addSpriteBank(newbanknames[i]); break;
				case WAV: addWavSoundBank(newbanknames[i]); break;
				case MIDI: addMidiMusicBank(newbanknames[i]); break;
			}
		}
	}
	
	private void addSpriteBank(String bankname)
	{
		// If the bank is already active, does nothing
		if (activeSpriteBanks.containsKey(bankname))
			return;
		
		// Adds the bank to the map and initializes it
		SpriteBank newbank = this.spritebankholder.getOpenSpriteBank(bankname);
		newbank.initializeBank();
		activeSpriteBanks.put(bankname, newbank);
	}
	
	private void addWavSoundBank(String bankname)
	{
		// If the bank is already active, does nothing
		if (activeWavBanks.containsKey(bankname))
			return;
		
		// Adds the bank to the map and initializes it
		WavSoundBank newbank = this.wavbankholder.getWavSoundBank(bankname);
		newbank.initializeBank();
		activeWavBanks.put(bankname, newbank);
	}
	
	// TODO: Do something to this copy-paste, perhaps by using a hashmap<resourcetype, hashmap>
	private void addMidiMusicBank(String bankname)
	{
		// If the bank is already active, does nothing
		if (activeMidiBanks.containsKey(bankname))
			return;
		
		// Adds the bank to the map and initializes it
		MidiMusicBank newbank = this.midibankholder.getMidiMusicBank(bankname);
		newbank.initializeBank();
		activeMidiBanks.put(bankname, newbank);
	}
	
	private static void removeSpriteBank(String bankname)
	{
		// If the bank doesn't contain the bank, does nothing
		if (!activeSpriteBanks.containsKey(bankname))
			return;
		
		// Uninitializes the banks and then removes it
		activeSpriteBanks.get(bankname).uninitialize();
		activeSpriteBanks.remove(bankname);
	}
	
	private static void removeWavSoundBank(String bankname)
	{
		// If the bank doesn't contain the bank, does nothing
		if (!activeWavBanks.containsKey(bankname))
			return;
		
		// Uninitializes the banks and then removes it
		activeWavBanks.get(bankname).uninitialize();
		activeWavBanks.remove(bankname);
	}
	
	private static void removeMidiMusicBank(String bankname)
	{
		// If the bank doesn't contain the bank, does nothing
		if (!activeMidiBanks.containsKey(bankname))
			return;
		
		// Uninitializes the banks and then removes it
		activeMidiBanks.get(bankname).uninitialize();
		activeMidiBanks.remove(bankname);
	}
	
	private static ArrayList<Background> getSimpleBackgroundList(String backname, 
			DrawableHandler drawer)
	{
		Background back = new Background(0, 0, drawer, null, 
				getSpriteBank("background"), backname);
		back.setDimensions(GameSettings.SCREENWIDTH, 
				GameSettings.SCREENHEIGHT);
		ArrayList<Background> backlist = new ArrayList<Background>();
		backlist.add(back);
		
		return backlist;
	}
	
	
	// ENUMERATIONS	------------------------------------------------------
	
	private enum ResourceBankType
	{
		SPRITE, WAV, MIDI;
	}
}
