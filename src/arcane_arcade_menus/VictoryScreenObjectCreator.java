package arcane_arcade_menus;

import java.awt.Graphics2D;

import utopia_gameobjects.DrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_helpAndEnums.DepthConstants;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import utopia_worlds.Room;
import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.SettingUsingArea;
import arcane_arcade_worlds.SettingUsingAreaObjectCreator;
import arcane_arcade_worlds.VictorySetting;

/**
 * VictoryScreenCreator creates the objects needed in the victory screen at the 
 * start of the room.
 * 
 * @author Unto Solala & Mikko Hilpinen
 * @since 4.9.2013
 */
public class VictoryScreenObjectCreator extends SettingUsingAreaObjectCreator
{	
	// ATTRIBUTES	-----------------------------------------------------
	
	private Navigator navigator;
	private VictorySetting victorysetting;
	
		
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new VictoryScreenObjectCreator that will use the given handlers. 
	 * The creator will create the objects when the room starts.
	 * 
	 * @param victoryScreen The victory screen where the objects will be created
	 * @param navigator The navigator that handles the transition between the 
	 * gamePhases
	 */
	public VictoryScreenObjectCreator(SettingUsingArea victoryScreen, 
			Navigator navigator)
	{
		super(victoryScreen, "space", "background", GameSettings.SCREENWIDTH, 
				GameSettings.SCREENHEIGHT, VictorySetting.class);
		
		// Initializes attributes
		this.navigator = navigator;
		this.victorysetting = null;
	}
	
	
	// IMPLMENTED METHODS ---------------------------------------------

	
	@Override
	protected void onSettingsChange(AreaSetting newSettings)
	{
		this.victorysetting = (VictorySetting) newSettings;
	}

	@Override
	protected void createObjects(Area area)
	{
		// Creates the objects
		new MenuBackgroundEffectCreator(area);
		new MenuCornerCreator(area, true);
		
		new SimplePhaseChangeButton(GameSettings.SCREENWIDTH - 100, 
				GameSettings.SCREENHEIGHT / 2, "mainmenu", 
				this.navigator, area);
		
		//Let's try to solve our victor and create the WinnerText
		if (this.victorysetting != null)
		{
			int winner = 0;
			if (this.victorysetting.getLeftSidePoints() > this.victorysetting
					.getRightSidePoints())
				winner = WinnerText.WINNERLEFT;
			else
				winner = WinnerText.WINNERRIGHT;
			
			// Let's place the WinnerText
			new WinnerText(winner, area);
		}
	}
	
	
	/**
	 * Draws the "Winner"-text on the winning player's side.
	 * 
	 * @author Unto Solala
	 *			4.9-2013
	 */
	private class WinnerText extends DrawnObject implements RoomListener
	{
		
		private static final int WINNERLEFT = 1;
		private static final int WINNERRIGHT = 2;
		
		// TODO: Change this to screenside
		
		//ATTRIBUTES------------------------------------------------------
		
		private SingleSpriteDrawer spritedrawer;
		private int winner;
		
		public WinnerText(int winner, Area area)
		{
			super(0, 0, DepthConstants.NORMAL, area);
			this.winner = winner;
			int x = GameSettings.SCREENWIDTH/2;
			int y = 5*(GameSettings.SCREENHEIGHT/6);
			switch (this.winner) 
			{
				case WINNERLEFT: 
				{
					// If we're here, the winner was the left player
					x = x - GameSettings.SCREENWIDTH / 5;
					break;
				}
				case WINNERRIGHT: 
				{
					// If we're here, the winner was the right player
					x = x + GameSettings.SCREENWIDTH / 5;
					break;
				}
			}
			//Let's set the position for our WinnerText
			this.setPosition(x, y);
			this.spritedrawer = new SingleSpriteDrawer(MultiMediaHolder.getSpriteBank(
					"menu").getSprite("winner"), null, this);
			this.spritedrawer.inactivate();
			this.setScale(0.5, 0.5);
		}

		// IMPLEMENTENTED METHODS ------------------------------------------

		@Override
		public int getOriginX()
		{
			if (this.spritedrawer == null)
				return 0;
			return this.spritedrawer.getSprite().getOriginX();
		}

		@Override
		public int getOriginY()
		{
			if (this.spritedrawer == null)
				return 0;
			return this.spritedrawer.getSprite().getOriginY();
		}

		@Override
		public void drawSelfBasic(Graphics2D g2d)
		{
			// Draws the sprite
			if (this.spritedrawer != null)
				this.spritedrawer.drawSprite(g2d, 0, 0);
		}

		@Override
		public void onRoomStart(Room room)
		{
			// Does nothing
		}

		@Override
		public void onRoomEnd(Room room)
		{
			// Dies
			kill();
		}
	}
}
