package arcane_arcade_menus;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D.Double;

import utopia_gameobjects.DrawnObject;
import utopia_gameobjects.GameObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_utility.DepthConstants;
import utopia_interfaceElements.AbstractMaskButton;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import utopia_worlds.Room;
import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.Navigator;

/**
 * MainMenuMenuCreator creates the main buttons and the emblem in the center
 * of the MainMenu.
 * 
 * @author Unto Solala
 * @since 3.9.2013
 */
public class MainMenuMenuCreator extends GameObject implements RoomListener
{
	//ATTRIBUTES---------------------------------------------------
	
	private MainMenuCenterPiece centerpiece;
	
	
	//CONSTRUCTOR---------------------------------------------------
	
	/**Constructs and places the buttons to the center of the MainMenu.
	 * 
	 * @param navigator	Navigator is needed for moving between the gamePhases
	 */
	public MainMenuMenuCreator(Navigator navigator)
	{
		super(navigator.getArea("mainmenu"));
		
		Area area = navigator.getArea("mainmenu");
		
		//Let's create the four menuElements
		new MainMenuElement(MainMenuElement.UP, navigator, area);
		new MainMenuElement(MainMenuElement.RIGHT, navigator, area);
		new MainMenuElement(MainMenuElement.DOWN, navigator, area);
		new MainMenuElement(MainMenuElement.LEFT, navigator, area);
		
		this.centerpiece = new MainMenuCenterPiece(area);
	}
	
	
	//IMPLEMENTED METHODS-------------------------------------------------
	
	@Override
	public void onRoomStart(Room room)
	{
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
	
	
	// SUBCLASSES	-----------------------------------------------------
	
	/**MainMenuElements are the four main buttons in the MainMenu-screen. The 
	 * buttons take the user to another phase within the game and a new screen.
	 * 
	 * @author Unto Solala & Mikko Hilpinen
	 * @since 3.9.2013
	 */
	private class MainMenuElement extends AbstractMaskButton
	{	
		private static final int UP = 0;
		private static final int RIGHT = 1;
		private static final int DOWN = 2;
		private static final int LEFT = 3;
		
		
		//ATTRIBUTES-----------------------------------------------------
		
		private int direction;
		private String phaseName;
		private Point startposition;
		private Navigator navigator;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		/**
		 * Constructs one of the buttons in the MainMenu.
		 * 
		 * @param direction	Direction determines where the button is located.
		 * There are four possible locations: UP, RIGHT, DOWN and LEFT.
		 * @param navigator	Navigator is needed for moving between the gamePhases
		 * @param area The area where the object is placed to
		 */
		public MainMenuElement(int direction, Navigator navigator, Area area)
		{
			super(0, 0, DepthConstants.NORMAL, null, null, area);
			
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
					this.phaseName = "battlesettingmenu";
					break;
				}
				case RIGHT: 
				{
					spriteName = "options";
					// Let's move the MenuElement to the right a bit
					x = x + 146;
					this.phaseName = "optionsmenu";
					break;
				}
				case DOWN: 
				{
					spriteName = "spellbook";
					// Let's move the MenuElement down a bit
					y = y + 145;
					this.phaseName = "spellbookmenu";
					break;
				}
				case LEFT: 
				{
					spriteName = "tutorial";
					// Let's move the MenuElement to the left a bit
					x = x - 152;
					this.phaseName = "tutorialmenu";
					break;
				}
			}
			
			//Let's set the position for our MenuElement
			this.setPosition(x, y);
			this.startposition = new Point(x,y);
			
			// Let's initialize rest of the attributes
			this.navigator = navigator;
			
			// Sets the correct sprite and mask
			getSpriteDrawer().setSprite(MultiMediaHolder.getSpriteBank(
					"menu").getSprite(spriteName));
			getMaskChecker().setMask(MultiMediaHolder.getSpriteBank(
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
				MouseButtonEventType eventType, Double mousePosition,
				double eventStepTime)
		{
			//Starts the correct gamePhase if the left button was pressed
			if (button == MouseButton.LEFT && 
					eventType == MouseButtonEventType.PRESSED)
				this.navigator.startPhase(this.phaseName, null);
		}

		@Override
		public void onMousePositionEvent(MousePositionEventType eventType,
				Double mousePosition, double eventStepTime)
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
	 * @since 4.9.2013
	 */
	private class MainMenuCenterPiece extends DrawnObject
	{	
		//ATTRIBUTES------------------------------------------------------
		
		private SingleSpriteDrawer spritedrawer;
		
		
		//CONSTRUCTOR------------------------------------------------------
		
		/**
		 * Creates the center emblem to the MainMenu.
		 * 
		 * @param area The area where the object is placed to
		 */
		public MainMenuCenterPiece(Area area)
		{
			super(GameSettings.SCREENWIDTH/2, GameSettings.SCREENHEIGHT/2, 
					DepthConstants.FOREGROUND, area);
			
			this.spritedrawer = new SingleSpriteDrawer(MultiMediaHolder.getSpriteBank(
					"menu").getSprite("center"), null, this);
			this.spritedrawer.inactivate();
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
	}
}
