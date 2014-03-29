package arcane_arcade_menus;

import java.awt.Graphics2D;
import java.util.ArrayList;

import utopia_gameobjects.DrawnObject;
import utopia_gameobjects.GameObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_helpAndEnums.DepthConstants;
import utopia_helpAndEnums.HelpMath;
import utopia_listeners.AdvancedKeyListener;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import utopia_worlds.Room;
import arcane_arcade_field.ScreenSide;
import arcane_arcade_main.Buttons;
import arcane_arcade_main.GameSettings;
import arcane_arcade_main.Options;
import arcane_arcade_status.Element;
import arcane_arcade_status.ElementIndicator;

/**
 * This interface lets the users choose the elements they will be using. 
 * The class showsthe possible and the chosen elements on the screen.
 *
 * @author Mikko Hilpinen.
 * @since 1.12.2013.
 */
public class ElementSelectionInterface
{
	// ATTRIBUTES	------------------------------------------------------
	
	private ArrayList<ElementIndicator> elements;
	private ChosenElementIndicator chosenindicator1, chosenindicator2;
	private int chosenindex1, chosenindex2;
	
	private ElementScreenObjectCreator creator;
	
	private static final Element[] UNLOCKEDELEMETS = {Element.BLAZE, 
			Element.TIDE, Element.FROST};
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates the element selection interface
	 * 
	 * @param creator The objectcreator that handles moving of areasettings
	 * @param area The area where the objects will be placed to
	 */
	public ElementSelectionInterface(ElementScreenObjectCreator creator, 
			Area area)
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
					element, area);
			newelement.scale(1.25, 1.25);
			this.elements.add(newelement);
			
			angle += 360.0 / UNLOCKEDELEMETS.length;
		}
		
		// Initializes indicators
		this.chosenindicator1 = new ChosenElementIndicator(1, area);
		this.chosenindicator2 = new ChosenElementIndicator(2, area);
		
		new KeyInputHandler(area);
		
		new ElementListDrawer(ScreenSide.LEFT, area);
		new ElementListDrawer(ScreenSide.RIGHT, area);
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
	 * @since 1.12.2013.
	 */
	private class ChosenElementIndicator extends DrawnObject implements 
			RoomListener
	{
		// ATTRIBUTES	--------------------------------------------------
		
		private SingleSpriteDrawer spritedrawer;
		private int playernumber;
		
		
		// CONSTRUCTOR	--------------------------------------------------
		
		/**
		 * Creates a new chosenElementIndicator
		 * 
		 * @param playernumber Which player's place the indicator will show [1,2]?
		 * @param area The area where the object is placed to
		 */
		private ChosenElementIndicator(int playernumber, Area area)
		{
			super(0, 0, DepthConstants.FOREGROUND, area);
			
			// Initializes attributes
			this.spritedrawer = new SingleSpriteDrawer(MultiMediaHolder.getSpriteBank(
					"menu").getSprite("chosen"), null, this);
			this.playernumber = playernumber;
			
			setAlpha(0.65f);
			updatePosition();
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
	 * @since 1.12.2013.
	 */
	private class KeyInputHandler extends GameObject implements 
			AdvancedKeyListener, RoomListener
	{
		// ATTRIBUTES	--------------------------------------------------
		
		private boolean active;
		
		
		// CONSTRUCTOR	---------------------------------------------------
		
		private KeyInputHandler(Area area)
		{
			super(area);
			
			// Initializes attributes
			this.active = true;
			
			// Adds the object to the handler(s)
			if (area.getKeyHandler() != null)
				area.getKeyHandler().addKeyListener(this);
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
		public void onKeyDown(char key, int keyCode, boolean coded, 
				double steps)
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
	 * @since 1.12.2013.
	 */
	private class ElementListDrawer extends DrawnObject implements RoomListener
	{
		// ATTRIBUTES	--------------------------------------------------
		
		private SingleSpriteDrawer spritedrawer;
		private ScreenSide side;
		
		
		// CONSTRUCTOR	--------------------------------------------------
		
		/**
		 * Creates a new elementlistdrawer to the given screen side
		 * 
		 * @param side The side the list represents and is drawn on
		 * @param area The area where the object is placed to
		 */
		public ElementListDrawer(ScreenSide side, Area area)
		{
			super(0, 200, DepthConstants.NORMAL, area);
			
			// Initializes attributes
			this.spritedrawer = new SingleSpriteDrawer(MultiMediaHolder.getSpriteBank(
					"hud").getSprite("elements"), null, this);
			this.side = side;
			
			// Sets up the position
			switch (side)
			{
				case LEFT: setX(100); break;
				case RIGHT: setX(GameSettings.SCREENWIDTH - 100); break;
			}
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
			if (this.spritedrawer == null)
				return 0;
			else
				return this.spritedrawer.getSprite().getOriginX();
		}

		@Override
		public int getOriginY()
		{
			if (this.spritedrawer == null)
				return 0;
			else
				return this.spritedrawer.getSprite().getOriginY();
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
