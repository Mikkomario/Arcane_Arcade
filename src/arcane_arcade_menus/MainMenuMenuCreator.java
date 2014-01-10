package arcane_arcade_menus;

import java.awt.Graphics2D;
import java.awt.Point;



import java.awt.geom.Point2D;

import worlds.Room;
import listeners.RoomListener;
import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.GamePhase;
import arcane_arcade_worlds.Navigator;
import gameobjects.DrawnObject;
import gameobjects.GameObject;
import graphic.SpriteDrawer;
import handlers.DrawableHandler;
import handlers.MouseListenerHandler;
import helpAndEnums.DepthConstants;

/**
 * MainMenuMenuCreator creates the main buttons and the emblem in the center
 * of the MainMenu.
 * 
 * @author Unto Solala
 * 			Created 3.9.2013
 */
public class MainMenuMenuCreator extends GameObject implements RoomListener
{
	//ATTRIBUTES---------------------------------------------------
	
	private MainMenuCenterPiece centerpiece;
	
	
	//CONSTRUCTOR---------------------------------------------------
	
	/**Constructs and places the buttons to the center of the MainMenu.
	 * 
	 * @param drawer	The drawer that will draw the menu corner
	 * @param mousehandler	The mouselistenerhandler that will inform the 
	 * corner about mouse events
	 * @param room	The room where the corner is created at
	 * @param navigator	Navigator is needed for moving between the gamePhases
	 */
	public MainMenuMenuCreator(DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room, Navigator navigator)
	{
		//Let's create the four menuElements
		new MainMenuElement(MainMenuElement.UP, drawer, mousehandler, room, navigator);
		new MainMenuElement(MainMenuElement.RIGHT, drawer, mousehandler, room, navigator);
		new MainMenuElement(MainMenuElement.DOWN, drawer, mousehandler, room, navigator);
		new MainMenuElement(MainMenuElement.LEFT, drawer, mousehandler, room, navigator);
		
		this.centerpiece = new MainMenuCenterPiece(drawer);
		
		if(room != null)
			room.addObject(this);
	}
	//IMPLEMENTED METHODS-------------------------------------------------
	@Override
	public void onRoomStart(Room room) {
		//Does nothing
		
	}

	@Override
	public void onRoomEnd(Room room)
	{
		//System.out.println("Main menu menu creator noticed room ending");
		kill();	
	}
	
	@Override
	public void kill()
	{
		//System.out.println("main menu menu creator got killed");
		this.centerpiece.kill();
		super.kill();
	}

	
	
	/**MainMenuElements are the four main buttons in the MainMenu-screen. The 
	 * buttons take the user to another phase within the game and a new screen.
	 * 
	 * @author Unto Solala & Mikko Hilpinen
	 * 			Created 3.9.2013
	 */
	private class MainMenuElement extends AbstractMaskButton
	{	
		private static final int UP = 0;
		private static final int RIGHT = 1;
		private static final int DOWN = 2;
		private static final int LEFT = 3;
		
		//ATTRIBUTES-----------------------------------------------------
		
		private int direction;
		private GamePhase gamephase;
		private Point startposition;
		private Navigator navigator;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		/**
		 * Constructs one of the buttons in the MainMenu.
		 * 
		 * @param direction	Direction determines where the button is located.
		 * There are four possible locations: UP, RIGHT, DOWN and LEFT.
		 * @param drawer The drawer that will draw the menuElement (optional)
		 * @param mousehandler	The mouselistenerhandler that will inform the 
		 * element about mouse events (optional)
		 * @param room	The room where the corner is created at (optional)
		 * @param navigator	Navigator is needed for moving between the gamePhases
		 */
		public MainMenuElement(int direction, DrawableHandler drawer, 
				MouseListenerHandler mousehandler, Room room, 
				Navigator navigator)
		{
			super(0, 0, DepthConstants.NORMAL, null, null, drawer, 
					mousehandler, room);
			
			//We need a couple of new variables for construction
			String spriteName = new String();
			int x=GameSettings.SCREENWIDTH/2;
			this.direction = direction;
			int y=GameSettings.SCREENHEIGHT/2;
			
			switch (this.direction) 
			{
				case UP: 
				{
					spriteName = "play";
					// Let's move the MenuElement up a bit
					y = y - 140;
					this.gamephase = GamePhase.BATTLESETTINGMENU;
					break;
				}
				case RIGHT: 
				{
					spriteName = "options";
					// Let's move the MenuElement to the right a bit
					x = x + 146;
					this.gamephase = GamePhase.OPTIONSMENU;
					break;
				}
				case DOWN: 
				{
					spriteName = "spellbook";
					// Let's move the MenuElement down a bit
					y = y + 145;
					this.gamephase = GamePhase.SPELLBOOKMENU;
					break;
				}
				case LEFT: 
				{
					spriteName = "tutorial";
					// Let's move the MenuElement to the left a bit
					x = x - 152;
					this.gamephase = GamePhase.TUTORIALMENU;
					break;
				}
			}
			
			//Let's set the position for our MenuElement
			this.setPosition(x, y);
			this.startposition = new Point(x,y);
			
			// Let's initialize rest of the attributes
			this.navigator = navigator;
			
			// Sets the correct sprite and mask
			getSpriteDrawer().setSprite(Navigator.getSpriteBank(
					"menu").getSprite(spriteName));
			getMaskChecker().setMask(Navigator.getSpriteBank(
					"menu").getSprite(spriteName+"mask"));
		}

		
		// IMPLEMENTENTED METHODS	------------------------------------------

		@Override
		public boolean listensMouseEnterExit()
		{
			return true;
		}

		@Override
		public void onMouseButtonEvent(MouseButton button,
				MouseButtonEventType eventType, Point2D mousePosition,
				double eventStepTime)
		{
			//Starts the correct gamePhase if the left button was pressed
			if (button == MouseButton.LEFT && 
					eventType == MouseButtonEventType.PRESSED)
				this.navigator.startPhase(this.gamephase, null);
		}

		@Override
		public void onMousePositionEvent(MousePositionEventType eventType,
				Point2D mousePosition, double eventStepTime)
		{
			// Resets the position of the MenuElement on mouse exit
			if (eventType == MousePositionEventType.EXIT)
				this.setPosition(this.startposition.getX(), 
						this.startposition.getY());
			
			// Moves the MenuElement slightly when the mouse enters
			if (eventType == MousePositionEventType.ENTER)
			{
				switch (this.direction) 
				{
					case UP: 
					{
						this.setPosition(this.startposition.getX(), 
								this.startposition.getY()-15);
						break;
					}
					case RIGHT: 
					{
						this.setPosition(this.startposition.getX()+15, 
								this.startposition.getY());
						break;
					}
					case DOWN: 
					{
						this.setPosition(this.startposition.getX(), 
								this.startposition.getY()+15);
						break;
					}
					case LEFT: 
					{
						this.setPosition(this.startposition.getX()-15, 
								this.startposition.getY());
						break;
					}
				}
			}
		}
	}
	
	/**
	 * MainMenuCenterPiece is the emblem in the center of the main menu.
	 * 
	 * @author Unto Solala
	 *			Created 4.9.2013
	 */
	private class MainMenuCenterPiece extends DrawnObject
	{	
		//ATTRIBUTES------------------------------------------------------
		
		private SpriteDrawer spritedrawer;
		
		
		//CONSTRUCTOR------------------------------------------------------
		
		/**Draws the center emblem to the MainMenu.
		 * 
		 * @param drawer	The drawer that will draw the menu corner
		 */
		public MainMenuCenterPiece(DrawableHandler drawer) {
			super(GameSettings.SCREENWIDTH/2, GameSettings.SCREENHEIGHT/2, 
					DepthConstants.FOREGROUND, drawer);
			
			this.spritedrawer = new SpriteDrawer(Navigator.getSpriteBank(
					"menu").getSprite("center"), null, this);
			this.spritedrawer.inactivate();
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
		public void drawSelfBasic(Graphics2D g2d)
		{
			// Draws the sprite
			if (this.spritedrawer != null)
				this.spritedrawer.drawSprite(g2d, 0, 0);
		}
	}
}
