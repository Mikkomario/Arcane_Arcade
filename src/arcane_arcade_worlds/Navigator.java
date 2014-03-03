package arcane_arcade_worlds;

import java.util.ArrayList;
import java.util.HashMap;

import resourcebanks.MultiMediaHolder;
import resourcebanks.ResourceType;
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
import arcane_arcade_menus.OptionScreenObjectCreator;
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
	
	private HashMap<GamePhase, SettingUsingRoom> rooms;
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
	 */
	public Navigator(DrawableHandler drawer, ActorHandler actorhandler, 
			KeyListenerHandler keylistenerhandler, 
			MouseListenerHandler mouselistenerhandler)
	{
		// Initializes attributes
		this.rooms = new HashMap<GamePhase, SettingUsingRoom>();
		this.activephase = null;
		
		// Initializes the backgrounds for the rooms
		MultiMediaHolder.activateBank(ResourceType.SPRITE, "background", true);
		
		// Initializes the rooms
		initializeMainMenu(drawer, actorhandler, mouselistenerhandler);
		initializeField(drawer, actorhandler, keylistenerhandler);
		initializeVictoryScreen(drawer, actorhandler, mouselistenerhandler);
		initializeBattleScreen(drawer, actorhandler, mouselistenerhandler);
		initializeElementScreen(drawer, actorhandler, mouselistenerhandler, 
				keylistenerhandler);
		initializeOptionScreen(drawer, actorhandler, mouselistenerhandler);
		
		// TODO: Do something about he fact that all backgrounds are 
		// initialized all the time?
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
		updateResourceBanks(phase, ResourceType.MIDI);
		updateResourceBanks(phase, ResourceType.MIDISOUNDTRACK);
		updateResourceBanks(phase, ResourceType.SPRITE);
		updateResourceBanks(phase, ResourceType.WAV);
		
		// Remembers the new active phase
		this.activephase = phase;
		
		// Updates the settings
		this.rooms.get(phase).setSettings(setting);
		
		// Starts the room of the given phase
		this.rooms.get(phase).start();
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
	
	private void initializeOptionScreen(DrawableHandler drawer, 
			ActorHandler actorhandler, MouseListenerHandler mousehandler)
	{
		// Creates the object creator
		OptionScreenObjectCreator creator = new OptionScreenObjectCreator(
				drawer, mousehandler, actorhandler, this);
		
		SettingUsingRoom optionscreen = new SettingUsingRoom(creator, 
				getSimpleBackgroundList("space", drawer));
		
		this.rooms.put(GamePhase.OPTIONSMENU, optionscreen);
	}
	
	private void updateResourceBanks(GamePhase newphase, 
			ResourceType resourcetype)
	{
		String[] newbanknames = newphase.getUsedBankNames(resourcetype);
		
		// Skips the removal progres if there was no previous phase
		if (this.activephase != null)
		{
			String[] oldbanknames = this.activephase.getUsedBankNames(resourcetype);
			
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
					MultiMediaHolder.deactivateBank(resourcetype, oldbankname);
			}
		}
		
		// Adds all the new banks that weren't active already
		// (Checking done in the addSpritebank method)
		for (int i = 0; i < newbanknames.length; i++)
		{
			MultiMediaHolder.activateBank(resourcetype, newbanknames[i], true);
		}
	}
	
	private static ArrayList<Background> getSimpleBackgroundList(String backname, 
			DrawableHandler drawer)
	{
		Background back = new Background(0, 0, drawer, null, 
				MultiMediaHolder.getSpriteBank("background"), backname);
		back.setDimensions(GameSettings.SCREENWIDTH, 
				GameSettings.SCREENHEIGHT);
		ArrayList<Background> backlist = new ArrayList<Background>();
		backlist.add(back);
		
		return backlist;
	}
}
