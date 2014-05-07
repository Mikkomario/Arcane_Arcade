package arcane_arcade_worlds;

import arcane_arcade_field.FieldObjectCreator;
import arcane_arcade_menus.BattleScreenObjectCreator;
import arcane_arcade_menus.ElementScreenObjectCreator;
import arcane_arcade_menus.MainMenuObjectCreator;
import arcane_arcade_menus.OptionScreenObjectCreator;
import arcane_arcade_menus.SpellBookObjectCreator;
import arcane_arcade_menus.TutorialScreenObjectCreator;
import arcane_arcade_menus.VictoryScreenObjectCreator;
import utopia_handlers.ActorHandler;
import utopia_handlers.DrawableHandler;
import utopia_handlers.KeyListenerHandler;
import utopia_handlers.MouseListenerHandler;
import utopia_resourceHandling.ResourceType;
import utopia_resourcebanks.GamePhaseBank;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import utopia_worlds.AreaRelay;

/**
 * Navigator is a class that handles the transition between different rooms 
 * and / or phases of the game.
 *
 * @author Mikko Hilpinen & Unto Solala
 * @since 27.8.2013.
 */
public class Navigator extends AreaRelay
{
	// TODO: Add public static variables for the area names
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new navigator, also creating all the rooms used in the game
	 *
	 * @param drawer The drawableHandler that will draw the rooms
	 * @param actorHandler The actorHandler that will handle all the actors 
	 * in the rooms
	 * @param keyHandler The keyListenerHandler that will inform 
	 * all keyListeners about the key events
	 * @param mouseHandler The mouseListenerHandler that will inform 
	 * the game's objects about mouse events
	 */
	public Navigator(DrawableHandler drawer, ActorHandler actorHandler, 
			KeyListenerHandler keyHandler, 
			MouseListenerHandler mouseHandler)
	{
		// Initializes the backgrounds for the rooms
		//MultiMediaHolder.activateBank(ResourceType.SPRITE, "background", true);
		
		// Initializes the areas
		MultiMediaHolder.activateBank(ResourceType.GAMEPHASE, "default", true);
		GamePhaseBank phaseBank = MultiMediaHolder.getGamePhaseBank("default");
		
		addArea("mainmenu", new SettingUsingArea(
				phaseBank.getPhase("mainmenu"), mouseHandler, actorHandler, 
				drawer, keyHandler));
		addArea("field", new SettingUsingArea(
				phaseBank.getPhase("field"), mouseHandler, actorHandler, 
				drawer, keyHandler));
		addArea("victoryscreen", new SettingUsingArea(
				phaseBank.getPhase("victoryscreen"), mouseHandler, actorHandler, 
				drawer, keyHandler));
		addArea("battlesettingmenu", new SettingUsingArea(
				phaseBank.getPhase("battlesettingmenu"), mouseHandler, actorHandler, 
				drawer, keyHandler));
		addArea("elementmenu", new SettingUsingArea(
				phaseBank.getPhase("elementmenu"), mouseHandler, actorHandler, 
				drawer, keyHandler));
		addArea("optionsmenu", new SettingUsingArea(
				phaseBank.getPhase("optionsmenu"), mouseHandler, actorHandler, 
				drawer, keyHandler));
		addArea("spellbookmenu", new SettingUsingArea(
				phaseBank.getPhase("spellbookmenu"), mouseHandler, actorHandler, 
				drawer, keyHandler));
		addArea("tutorialmenu", new SettingUsingArea(
				phaseBank.getPhase("tutorialmenu"), mouseHandler, actorHandler, 
				drawer, keyHandler));
		
		// Adds the object creators
		new MainMenuObjectCreator((SettingUsingArea) getArea("mainmenu"), this);
		new FieldObjectCreator((SettingUsingArea) getArea("field"), this);
		new VictoryScreenObjectCreator((SettingUsingArea) getArea("victoryscreen"), this);
		new BattleScreenObjectCreator((SettingUsingArea) getArea("battlesettingmenu"), this);
		new ElementScreenObjectCreator((SettingUsingArea) getArea("elementmenu"), this);
		new OptionScreenObjectCreator((SettingUsingArea) getArea("optionsmenu"), this);
		new SpellBookObjectCreator((SettingUsingArea) getArea("spellbookmenu"), this);
		new TutorialScreenObjectCreator((SettingUsingArea) getArea("tutorialmenu"), this);
	}
	
	
	// OTHER METHODS	------------------------------------------------
	
	/**
	 * Stops the current area and starts a new one.
	 * 
	 * @param newAreaName the name of the new area that will start
	 * @param setting The setting(s) used in the new area (optional)
	 */
	public void startPhase(String newAreaName, AreaSetting setting)
	{
		// Ends the current area
		endAllAreas();
		// Starts the new one with specific settings
		Area newArea = getArea(newAreaName);
		
		if (newArea == null)
			return;
		else if (newArea instanceof SettingUsingArea)
			((SettingUsingArea) newArea).start(setting);
		else
			newArea.start();
	}
}
