package arcane_arcade_menus;

import java.awt.Graphics2D;

import graphic.SpriteDrawer;
import handlers.ActorHandler;
import handlers.DrawableHandler;
import handlers.MouseListenerHandler;
import helpAndEnums.DepthConstants;
import worlds.Room;
import common.GameObject;
import drawnobjects.DrawnObject;

import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.AreaSetting;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.RoomObjectCreator;
import arcane_arcade_worlds.VictorySetting;

/**
 * VictoryScreenCreator creates the objects needed in the victory screen at the 
 * start of the room.
 * 
 * @author Unto Solala
 *			Created 4.9.2013
 */
public class VictoryScreenCreator extends GameObject implements RoomObjectCreator{
	
	// ATTRIBUTES	-----------------------------------------------------
	
	private DrawableHandler drawer;
	private ActorHandler actorhandler;
	private MouseListenerHandler mouselistenerhandler;
	private Navigator navigator;
	private VictorySetting victorysetting;
		
	// CONSTRUCTOR	-----------------------------------------------------
	/**
	 * Creates a new VictoryScreenCreator that will use the given handlers. 
	 * The creator will create the objects when the room starts.
	 *
	 * @param drawer The drawer that will draw the created objects
	 * @param actorhandler The actorhandler that will inform the objects about 
	 * act events
	 * @param room The room where the objects will be created
	 * @param mousehandler The mouselistenerhandler that will inform the objects 
	 * about mouse events
	 * @param navigator The navigator that handles the transition between the 
	 * gamephases
	 */
	public VictoryScreenCreator(DrawableHandler drawer, 
			ActorHandler actorhandler, MouseListenerHandler mousehandler, 
			Navigator navigator)
	{
		// Initializes attributes
		this.drawer = drawer;
		this.actorhandler = actorhandler;
		this.mouselistenerhandler = mousehandler;
		this.navigator = navigator;
		this.victorysetting = null;
	}
	
	// IMPLMENTED METHODS ---------------------------------------------

	@Override
	public void onRoomStart(Room room) {
		// Creates the objects
		new MenuCornerCreator(this.drawer, this.mouselistenerhandler, room, true);
		//Let's try to solve our victor and create the WinnerText
		if(this.victorysetting != null){
			int winner = 0;
			if(this.victorysetting.getLeftSidePoints()>this.victorysetting.getRightSidePoints()){
				winner = WinnerText.WINNERLEFT;
			}else{
				winner = WinnerText.WINNERRIGHT;
			}
			//Let's place the WinnerText
			new WinnerText(winner, this.drawer);
		}
	}

	@Override
	public void onRoomEnd(Room room) {
		// Does nothing
	}

	@Override
	public void setSettings(AreaSetting setting) {
		// Checks that the setting is the right type
		if (setting instanceof VictorySetting)
			this.victorysetting = (VictorySetting) setting;
		else
			System.err.println("VictoryScreenCreator requires an " +
					"VictorySetting -setting and will not function otherwise!");
	}
	
	private class WinnerText extends DrawnObject{
		
		private static final int WINNERLEFT = 1;
		private static final int WINNERRIGHT = 2;
		
		//ATTRIBUTES------------------------------------------------------
		
		private SpriteDrawer spritedrawer;
		private int winner;
		
		public WinnerText(int winner, DrawableHandler drawer) {
			super(0, 0, DepthConstants.NORMAL, drawer);
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
			this.spritedrawer = new SpriteDrawer(Navigator.getSpriteBank(
					"menu").getSprite("winner"), null);
			this.spritedrawer.inactivate();
			this.setScale(0.5, 0.5);
		}

		// IMPLEMENTENTED METHODS ------------------------------------------

		@Override
		public int getOriginX() {
			if (this.spritedrawer == null)
				return 0;
			return this.spritedrawer.getSprite().getOriginX();
		}

		@Override
		public int getOriginY() {
			if (this.spritedrawer == null)
				return 0;
			return this.spritedrawer.getSprite().getOriginY();
		}

		@Override
		public void drawSelfBasic(Graphics2D g2d) {
			// Draws the sprite
			if (this.spritedrawer != null)
				this.spritedrawer.drawSprite(g2d, 0, 0);
		}

	}
}
