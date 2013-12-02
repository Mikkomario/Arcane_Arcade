package arcane_arcade_menus;

import graphic.SpriteDrawer;
import handlers.DrawableHandler;
import handlers.KeyListenerHandler;
import helpAndEnums.DepthConstants;
import helpAndEnums.HelpMath;

import java.awt.Graphics2D;
import java.util.ArrayList;

import common.GameObject;

import worlds.Room;

import listeners.AdvancedKeyListener;
import listeners.RoomListener;

import drawnobjects.DrawnObject;

import arcane_arcade_field.ScreenSide;
import arcane_arcade_main.Buttons;
import arcane_arcade_main.GameSettings;
import arcane_arcade_main.Options;
import arcane_arcade_status.Element;
import arcane_arcade_status.ElementIndicator;
import arcane_arcade_worlds.Navigator;

/**
 * This interface lets the users choose the elements they will be using. 
 * The class showsthe possible and the chosen elements on the screen.
 *
 * @author Mikko Hilpinen.
 *         Created 1.12.2013.
 */
public class ElementSelectionInterface
{
	// ATTRIBUTES	------------------------------------------------------
	
	private ArrayList<ElementIndicator> elements;
	private ChosenElementIndicator chosenindicator1, chosenindicator2;
	private int chosenindex1, chosenindex2;
	
	private ElementScreenObjectCreator creator;
	
	private static final Element[] UNLOCKEDELEMETS = {Element.BLAZE, 
			Element.TIDE};
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates the element selection interface
	 * 
	 * @param creator The objectcreator that handles moving of areasettings
	 * @param drawer The drawablehandler that draws all the objects in the 
	 * interface
	 * @param keyhandler The keylistenerhandler that informs the objects about 
	 * key events
	 * @param room The room where the objects are created at
	 */
	public ElementSelectionInterface(ElementScreenObjectCreator creator, 
			DrawableHandler drawer, KeyListenerHandler keyhandler, Room room)
	{
		// Initializes attributes
		this.creator = creator;
		this.elements = new ArrayList<ElementIndicator>();
		this.chosenindex1 = 0;
		this.chosenindex2 = UNLOCKEDELEMETS.length - 1;
		
		double angle = 90;
		
		// Initializes the elements
		for (Element element : UNLOCKEDELEMETS)
		{
			ElementIndicator newelement = new ElementIndicator(
					GameSettings.SCREENWIDTH / 2 + (int) HelpMath.lendirX(120, 
					angle), GameSettings.SCREENHEIGHT / 2 + 
					(int) HelpMath.lendirY(120, angle), DepthConstants.NORMAL, 
					element, drawer, room);
			newelement.scale(1.25, 1.25);
			this.elements.add(newelement);
			
			angle += 360.0 / UNLOCKEDELEMETS.length;
		}
		
		// Initializes indicators
		this.chosenindicator1 = new ChosenElementIndicator(1, drawer, room);
		this.chosenindicator2 = new ChosenElementIndicator(2, drawer, room);
		
		new KeyInputHandler(keyhandler, room);
		
		new ElementListDrawer(ScreenSide.LEFT, drawer, room);
		new ElementListDrawer(ScreenSide.RIGHT, drawer, room);
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	// Playernumber [1, 2], direction[-1, 1]
	private void changeChosenElement(int playernumber, int direction)
	{
		int oldindex = 0;
		
		switch (playernumber)
		{
			case 1: oldindex = this.chosenindex1; break;
			case 2: oldindex = this.chosenindex2; break;
		}
		
		int newindex = oldindex + direction;
		
		// Checks if the index went out of bounds
		if (newindex < 0)
			newindex += this.elements.size();
		else if (newindex >= this.elements.size())
			newindex -= this.elements.size();
		
		switch (playernumber)
		{
			case 1: this.chosenindex1 = newindex; break;
			case 2: this.chosenindex2 = newindex; break;
		}
		
		// Also updates the indicator positions
		this.chosenindicator1.updatePosition();
		this.chosenindicator2.updatePosition();
	}
	
	private void addNewElementToSide(ScreenSide side)
	{
		Element newelement = Element.NOELEMENT;
		if (side == ScreenSide.LEFT)
			newelement = this.elements.get(this.chosenindex1).getElement();
		else
			newelement = this.elements.get(this.chosenindex2).getElement();
		
		this.creator.getSettings().addElementOnSide(newelement, side);
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	/**
	 * This class shows the user which element is currently chosen
	 *
	 * @author Mikko Hilpinen.
	 *         Created 1.12.2013.
	 */
	private class ChosenElementIndicator extends DrawnObject implements 
			RoomListener
	{
		// ATTRIBUTES	--------------------------------------------------
		
		private SpriteDrawer spritedrawer;
		private int playernumber;
		
		
		// CONSTRUCTOR	--------------------------------------------------
		
		/**
		 * Creates a new chosenElementIndicator
		 * 
		 * @param playernumber Which player's place the indicator will show [1,2]?
		 * @param drawer The drawer that will draw the indicator
		 * @param room The room where the indicator is located at
		 */
		private ChosenElementIndicator(int playernumber, DrawableHandler drawer, 
				Room room)
		{
			super(0, 0, DepthConstants.FOREGROUND, drawer);
			
			// Initializes attributes
			this.spritedrawer = new SpriteDrawer(Navigator.getSpriteBank(
					"menu").getSprite("chosen"), null, this);
			this.playernumber = playernumber;
			
			setAlpha(0.65f);
			updatePosition();
			
			// Adds the object to the handler(s)
			if (room != null)
				room.addObject(this);
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------

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
			if (this.spritedrawer != null)
				this.spritedrawer.drawSprite(g2d, 0, 0, this.playernumber - 1);
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
		
		
		// OTHER METHODS	----------------------------------------------
		
		private void updatePosition()
		{
			int index = 0;
			switch (this.playernumber)
			{
				case 1:
					index = ElementSelectionInterface.this.chosenindex1; break;
				case 2:
					index = ElementSelectionInterface.this.chosenindex2; break;
			}
			
			ElementIndicator chosen = 
					ElementSelectionInterface.this.elements.get(index);
			setPosition(chosen.getPosition().x, chosen.getPosition().y);
		}
	}
	
	
	/**
	 * KeyInputHandler reads the keyboard events and calls some of the 
	 * interface's methods when needed
	 *
	 * @author Mikko Hilpinen.
	 *         Created 1.12.2013.
	 */
	private class KeyInputHandler extends GameObject implements 
			AdvancedKeyListener, RoomListener
	{
		// ATTRIBUTES	--------------------------------------------------
		
		private boolean active;
		
		
		// CONSTRUCTOR	---------------------------------------------------
		
		private KeyInputHandler(KeyListenerHandler keylistenerhandler, Room room)
		{
			// Initializes attributes
			this.active = true;
			
			// Adds the object to the handler(s)
			if (keylistenerhandler != null)
				keylistenerhandler.addKeyListener(this);
			if (room != null)
				room.addObject(this);
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------

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
		public void onKeyDown(char key, int keyCode, boolean coded)
		{
			// Does nothing
		}

		@Override
		public void onKeyPressed(char key, int keyCode, boolean coded)
		{
			// Changes the chosen elements on certain keys
			if (!coded)
			{
				// Left & Right changes current position
				if (key == Options.leftwizardbuttons.get(
						Buttons.LEFT_ELEMENT_DOWN))
					changeChosenElement(1, 1);
				else if (key == Options.leftwizardbuttons.get(
						Buttons.RIGHT_ELEMENT_DOWN))
					changeChosenElement(1, -1);
				else if (key == Options.rightwizardbuttons.get(
						Buttons.LEFT_ELEMENT_DOWN))
					changeChosenElement(2, 1);
				else if (key == Options.rightwizardbuttons.get(
						Buttons.RIGHT_ELEMENT_DOWN))
					changeChosenElement(2, -1);
				
				// Up & Down adds / removes elements from the list
				else if (key == Options.leftwizardbuttons.get(Buttons.UP))
					addNewElementToSide(ScreenSide.LEFT);
				else if (key == Options.rightwizardbuttons.get(Buttons.UP))
					addNewElementToSide(ScreenSide.RIGHT);
				else if (key == Options.leftwizardbuttons.get(Buttons.DOWN))
					ElementSelectionInterface.this.creator.getSettings().
							removeLastElementFromSide(ScreenSide.LEFT);
				else if (key == Options.rightwizardbuttons.get(Buttons.DOWN))
					ElementSelectionInterface.this.creator.getSettings().
							removeLastElementFromSide(ScreenSide.RIGHT);
			}
		}

		@Override
		public void onKeyReleased(char key, int keyCode, boolean coded)
		{
			// Does nothing
		}
	}
	
	/**
	 * ElementList handles and draws a list of chosen elements.
	 *
	 * @author Mikko Hilpinen.
	 *         Created 1.12.2013.
	 */
	private class ElementListDrawer extends DrawnObject implements RoomListener
	{
		// ATTRIBUTES	--------------------------------------------------
		
		private SpriteDrawer spritedrawer;
		private ScreenSide side;
		
		
		// CONSTRUCTOR	--------------------------------------------------
		
		/**
		 * Creates a new elementlistdrawer to the given screen side
		 * 
		 * @param side The side the list represents and is drawn on
		 * @param drawer The drawer that draws the object (optional)
		 * @param room The room that holds the object (optional)
		 */
		public ElementListDrawer(ScreenSide side, DrawableHandler drawer, 
				Room room)
		{
			super(0, 200, DepthConstants.NORMAL, drawer);
			
			// Initializes attributes
			this.spritedrawer = new SpriteDrawer(Navigator.getSpriteBank(
					"hud").getSprite("elements"), null, this);
			this.side = side;

			// Sets up the position
			switch (side)
			{
				case LEFT: setX(100); break;
				case RIGHT: setX(GameSettings.SCREENWIDTH - 100); break;
			}
			
			// Adds the object to the handler(s)
			if (room != null)
				room.addObject(this);
		}
		
		
		// IMPLEMENTED METHODS	------------------------------------------

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
		public int getOriginX()
		{
			return 0;
		}

		@Override
		public int getOriginY()
		{
			return 0;
		}

		@Override
		public void drawSelfBasic(Graphics2D g2d)
		{
			if (this.spritedrawer == null)
				return;
			
			Element[] elementlist = 
					ElementSelectionInterface.this.creator.getSettings().
					getElementsOnSide(this.side);
			
			// Draws the elements as sprites
			for (int i = 0; i < elementlist.length; i++)
			{
				this.spritedrawer.drawSprite(g2d, 0, i * 50, 
						elementlist[i].getElementIconIndex());
			}
		}
	}
}
