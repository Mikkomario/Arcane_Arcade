package arcane_arcade_menus;

import java.awt.Graphics2D;
import java.util.ArrayList;

import utopia_gameobjects.DrawnObject;
import utopia_graphic.ParagraphDrawer;
import utopia_graphic.SingleSpriteDrawer;
import utopia_listeners.AdvancedMouseListener.MouseButton;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_utility.DepthConstants;
import utopia_worlds.Area;
import utopia_worlds.Room;
import arcane_arcade_main.GameSettings;
import arcane_arcade_spells.Spell;
import arcane_arcade_status.Element;
import arcane_arcade_status.ElementIndicator;
import arcane_arcade_status.ElementIndicatorListener;

/**
 * SpellBookInterface includes the guide system in the spellBook menu. In other 
 * words it shows the possible elements, allows the user to choose any spell 
 * by combining them together and provides information about that spell.
 * 
 * @author Mikko Hilpinen
 * @since 30.4.2014
 */
public class SpellBookInterface implements ElementIndicatorListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private ArrayList<ElementIndicator> indicators;
	private ElementIndicator lastChosenIndicator;
	private ChosenMarker firstChosenMarker, secondChosenMarker;
	private SpellInformationDrawer informationDrawer;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new SpellBookInterface
	 * @param area The area where the interface will be created
	 */
	public SpellBookInterface(Area area)
	{
		// Initializes attributes
		this.lastChosenIndicator = null;
		this.firstChosenMarker = new ChosenMarker(area, false);
		this.secondChosenMarker = new ChosenMarker(area, true);
		this.indicators = new ArrayList<ElementIndicator>();
		this.informationDrawer = new SpellInformationDrawer(area);
		
		// Creates the Indicators
		ElementIndicator.createIndicators(area, this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void onElementIndicatorPressed(MouseButton button,
			ElementIndicator source)
	{
		// Moves the chosenMarkers
		this.firstChosenMarker.setPosition(source.getPosition());
		
		if (this.lastChosenIndicator != null)
		{	
			this.secondChosenMarker.setPosition(this.lastChosenIndicator.getPosition());
			
			// Also updates the shown data
			this.informationDrawer.setElements(this.lastChosenIndicator.getElement(), 
					source.getElement());
		}
		
		// Updates the last chosen indicator
		this.lastChosenIndicator = source;
	}

	@Override
	public void onElementIndicatorCreated(ElementIndicator newIndicator)
	{
		// Adds the new indicator to the list
		this.indicators.add(newIndicator);
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	private class ChosenMarker extends DrawnObject implements RoomListener
	{
		// ATTRIBUTES	--------------------------------------------------
		
		private SingleSpriteDrawer spriteDrawer;
		
		
		// CONSTRUCTOR	--------------------------------------------------
		
		public ChosenMarker(Area area, boolean isSecondary)
		{
			super(-400, -400, DepthConstants.FOREGROUND, area);
			
			// Initializes attributes
			this.spriteDrawer = new SingleSpriteDrawer(
					MultiMediaHolder.getSpriteBank("menu").getSprite("chosen2"), 
					area.getActorHandler(), this);
			
			if (isSecondary)
				setScale(-1, -1);
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------

		@Override
		public int getOriginX()
		{
			if (this.spriteDrawer == null)
				return 0;
			return this.spriteDrawer.getSprite().getOriginX();
		}

		@Override
		public int getOriginY()
		{
			if (this.spriteDrawer == null)
				return 0;
			return this.spriteDrawer.getSprite().getOriginY();
		}

		@Override
		public void drawSelfBasic(Graphics2D g2d)
		{
			if (this.spriteDrawer != null)
				this.spriteDrawer.drawSprite(g2d, 0, 0);
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
	
	private class SpellInformationDrawer extends DrawnObject implements RoomListener
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private ParagraphDrawer infoDrawer;
		private String spellName;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		public SpellInformationDrawer(Area area)
		{
			super(GameSettings.SCREENWIDTH / 2, GameSettings.SCREENHEIGHT / 2, 
					DepthConstants.HUD, area);
			
			// Initializes attributes
			this.spellName = "Help:";
			this.infoDrawer = new ParagraphDrawer("Click two elements to for spell information.", 
					GameSettings.BASICFONT, GameSettings.WHITETEXTCOLOR, 500, this);
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------

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
			// Draws the information
			if (this.infoDrawer != null)
				this.infoDrawer.drawText(g2d, -250, -75);
			
			g2d.setFont(GameSettings.BASICFONT);
			g2d.setColor(GameSettings.WHITETEXTCOLOR);
			
			// Draws the spell name
			g2d.drawString(this.spellName, -100, -100);
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
		
		
		// OTHER METHODS	--------------------------------------------
		
		public void setElements(Element element1, Element element2)
		{
			Spell spell = element1.getSpell(element2);
			this.spellName = spell.getName();
			this.infoDrawer.setText(spell.getDescription());
		}
	}
}
