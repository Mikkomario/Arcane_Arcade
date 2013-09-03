package arcane_arcade_menus;

import java.awt.Graphics2D;
import java.awt.Point;

import worlds.Room;

import listeners.AdvancedMouseListener;
import listeners.RoomListener;
import drawnobjects.DrawnObject;
import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.GamePhase;
import arcane_arcade_worlds.Navigator;
import graphic.MaskChecker;
import graphic.SpriteDrawer;
import handlers.DrawableHandler;
import handlers.MouseListenerHandler;
import helpAndEnums.DepthConstants;

/**
 * 
 * @author Unto
 *
 */
public class MainMenuMenuCreator {

	
	
	/**
	 * 
	 * @author Unto
	 *
	 */
	private class MainMenuElements extends DrawnObject implements AdvancedMouseListener, RoomListener{
		
		private static final int UP = 0;
		private static final int RIGHT = 1;
		private static final int DOWN = 2;
		private static final int LEFT = 3;
		//ATTRIBUTES-----------------------------------------------------
		private int direction;
		private MaskChecker maskchecker;
		private SpriteDrawer spritedrawer;
		private GamePhase gamephase;
		private boolean active;
		private Point startposition;
		
		public MainMenuElements(int direction, DrawableHandler drawer, 
				MouseListenerHandler mousehandler, Room room) {
			super(0, 0, DepthConstants.NORMAL, drawer);
			//We need a couple of new variables for construction
			String spriteName = new String();
			int x=GameSettings.SCREENWIDTH/2;
			int y=GameSettings.SCREENHEIGHT/2;
			switch (direction) {
				case UP: 
				{
					spriteName = "play";
					// Let's move the MenuElement up a bit
					y = y - 50;
					break;
				}
				case RIGHT: 
				{
					spriteName = "options";
					// Let's move the MenuElement to the right a bit
					x = x + 50;
					break;
				}
				case DOWN: 
				{
					spriteName = "spellbook";
					// Let's move the MenuElement down a bit
					y = y + 50;
					break;
				}
				case LEFT: 
				{
					spriteName = "tutorial";
					// Let's move the MenuElement to the left a bit
					x = x - 50;
					break;
				}
			}
			//Let's set the position for our MenuElement
			this.setPosition(x, y);
			this.startposition = new Point(x,y);
			// Initializes attributes
			this.spritedrawer = new SpriteDrawer(Navigator.getSpriteBank(
					"menu").getSprite(spriteName), null);
			this.spritedrawer.inactivate();
			this.maskchecker = new MaskChecker(Navigator.getSpriteBank(
					"menu").getSprite(spriteName+"mask"));
			this.active = true;
			
			// Adds the object to the handlers
			if (mousehandler != null)
				mousehandler.addMouseListener(this);
			if (room != null)
				room.addOnject(this);
		}

		// IMPLEMENTENTED METHODS	------------------------------------------

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
		public boolean isActive()
		{
			return this.active;
		}

		@Override
		public void activate()
		{
			this.active = true;
		}

		@Override
		public void inactivate()
		{
			this.active = false;
		}

		@Override
		public void onLeftDown(int mouseX, int mouseY)
		{
			// Does nothing
		}

		@Override
		public void onRightDown(int mouseX, int mouseY)
		{
			// Does nothing
		}

		@Override
		public void onLeftPressed(int mouseX, int mouseY)
		{
		switch (this.direction) {
			case UP: 
			{
				
				break;
			}
			case RIGHT: 
			{
				
				break;
			}
			case DOWN: 
			{
				
				break;
			}
			case LEFT: 
			{
				
				break;
			}
		}
		}

		@Override
		public void onRightPressed(int mouseX, int mouseY)
		{
			// Does nothing
		}

		@Override
		public void onLeftReleased(int mouseX, int mouseY)
		{
			// Does nothing
		}

		@Override
		public void onRightReleased(int mouseX, int mouseY)
		{
			// Does nothing
		}

		@Override
		public boolean listensPosition(int x, int y)
		{
			// If currently has no mask, doesn't listen to the mouse
			if (this.maskchecker == null)
				return false;
			// Checks mask collision
			Point relpoint = negateTransformations(x, y);
			return this.maskchecker.maskContainsRelativePoint(relpoint, 0);
		}

		@Override
		public boolean listensMouseEnterExit()
		{
			return true;
		}

		@Override
		public void onMouseEnter(int mouseX, int mouseY)
		{
			// Changes sprite index
			this.spritedrawer.setImageIndex(1);
		}

		@Override
		public void onMouseOver(int mouseX, int mouseY)
		{
			// Does nothing
		}

		@Override
		public void onMouseExit(int mouseX, int mouseY)
		{
			// Changes sprite index
			this.spritedrawer.setImageIndex(0);
		}

		@Override
		public void onMouseMove(int mouseX, int mouseY)
		{
			// Does nothing
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
		
		@Override
		public void kill()
		{
			// Kills the spritedrawer and maskchecker
			this.spritedrawer.kill();
			this.spritedrawer = null;
			this.maskchecker = null;
			super.kill();
		}
	}
}
