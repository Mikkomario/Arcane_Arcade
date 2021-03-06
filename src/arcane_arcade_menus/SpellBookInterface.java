package arcane_arcade_menus;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import utopia_gameobjects.DrawnObject;
import utopia_graphic.Sprite;
import utopia_graphic.SpriteDrawerObject;
import utopia_graphic.ParagraphDrawer;
import utopia_listeners.AdvancedMouseListener.MouseButton;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_utility.DepthConstants;
import utopia_utility.HelpMath;
import utopia_worlds.Area;
import utopia_worlds.Room;
import arcane_arcade_main.GameSettings;
import arcane_arcade_spells.Spell;
import arcane_arcade_status.BallStatus;
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
public class SpellBookInterface implements ElementIndicatorListener, RoomListener
{	
	// ATTRIBUTES	------------------------------------------------------
	
	private ArrayList<ElementIndicator> indicators;
	private ArrayList<SpriteDrawerObject> buffIndicators;
	private ElementIndicator lastChosenIndicator;
	private SpriteDrawerObject firstChosenMarker, secondChosenMarker, 
			statusBoostIndicator;
	private SpellInformationDrawer informationDrawer;
	private MeterDrawer colourDrawer, timeDrawer;
	private boolean dead;
	private Area area;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new SpellBookInterface
	 * @param area The area where the interface will be created
	 */
	public SpellBookInterface(Area area)
	{
		// Initializes attributes
		this.lastChosenIndicator = null;
		this.indicators = new ArrayList<ElementIndicator>();
		this.informationDrawer = new SpellInformationDrawer(area);
		this.buffIndicators = new ArrayList<SpriteDrawerObject>();
		this.colourDrawer = new MeterDrawer(MeterDrawer.COLOUR);
		this.timeDrawer = new MeterDrawer(MeterDrawer.TIME);
		
		this.dead = false;
		this.area = area;
		
		createChosenMarker(area, false);
		createChosenMarker(area, true);
		
		// Creates the Indicators
		ElementIndicator.createIndicators(area, this);
		
		// Creates the status boost indicator
		this.statusBoostIndicator = new SpriteDrawerObject(area, 
				DepthConstants.FOREGROUND, null, 
				MultiMediaHolder.getSprite("hud", "statusbuff"));
		this.statusBoostIndicator.setPosition(-400, -400);
		
		// Adds the interface into the area
		area.addRoomListener(this);
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
			
			Spell spell = this.lastChosenIndicator.getElement().getSpell(source.getElement());
			
			// Also updates the shown data
			this.informationDrawer.setSpell(spell);
			
			// And the buffs / debuffs
			createBuffIndicators(spell);
			
			// And the colour & time usages
			this.colourDrawer.setSpell(spell);
			this.timeDrawer.setSpell(spell);
			
			// And the buffed status
			updateStatusBoostPosition(spell);
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
	
	@Override
	public boolean isDead()
	{
		return this.dead;
	}

	@Override
	public void kill()
	{
		// Kills the objects in the interface
		killInterfaceElements();
		this.dead = true;
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
	
	
	// OTHER METHODS	--------------------------------------------------
	
	private void createChosenMarker(Area area, boolean isSecondary)
	{
		SpriteDrawerObject marker = new SpriteDrawerObject(area, 
				DepthConstants.FOREGROUND, null, 
				MultiMediaHolder.getSpriteBank("menu").getSprite("chosen2"));
		
		marker.setPosition(-400, -400);
		
		if (isSecondary)
		{
			marker.setScale(-1, -1);
			this.secondChosenMarker = marker;
		}
		else
			this.firstChosenMarker = marker;
	}
	
	private void createBuffIndicators(Spell spell)
	{
		// First destroys the previous indicators
		killBuffIndicators();

		for (ElementIndicator indicator : this.indicators)
		{
			createBuffIndicator(spell, indicator);
		}
	}
	
	private void createBuffIndicator(Spell currentSpell, 
			ElementIndicator elementIndicator)
	{
		// Checks what kind of buff is needed or if one isn't needed at all
		double forceModifier = Element.getForceModifier(
				currentSpell.getFirstEffectElement(), 
				currentSpell.getSecondEffectElement(), 
				elementIndicator.getElement().getCausedStatus(), 100);
		
		if (forceModifier > 0.9 && forceModifier < 1.1)
			return;
		
		// Creates the buffIndicator
		SpriteDrawerObject buffIndicator = new SpriteDrawerObject(this.area, 
				DepthConstants.FOREGROUND, null, 
				MultiMediaHolder.getSpriteBank("hud").getSprite("buffarrow"));
		buffIndicator.getSpriteDrawer().setImageSpeed(0);
		buffIndicator.goToRelativePosition(new Point2D.Double(50, 50), 
				elementIndicator);
		
		// Sets the correct buff
		if (forceModifier < 0.9)
			buffIndicator.getSpriteDrawer().setImageIndex(1);
		else
			buffIndicator.getSpriteDrawer().setImageIndex(0);
		
		// Remembers the new indicator
		this.buffIndicators.add(buffIndicator);
	}
	
	private void killInterfaceElements()
	{
		// Kills the indicators
		for (int i = 0; i < this.indicators.size(); i++)
		{
			this.indicators.get(i).kill();
		}
		this.indicators.clear();
		
		// Kills the markers
		this.firstChosenMarker.kill();
		this.secondChosenMarker.kill();
		
		// Kills the information drawer
		this.informationDrawer.kill();
		
		// Also kills the buffindicators
		killBuffIndicators();
		
		// And the statusbuffindicator
		this.statusBoostIndicator.kill();
		
		// And the meters
		this.colourDrawer.killBlocks();
		this.timeDrawer.killBlocks();
	}
	
	private void killBuffIndicators()
	{
		// Kills all the remaining buffindicators
		for (int i = 0; i < this.buffIndicators.size(); i++)
		{
			this.buffIndicators.get(i).kill();
		}
		this.buffIndicators.clear();
	}
	
	private void updateStatusBoostPosition(Spell spell)
	{
		// Gets the buffed status
		BallStatus status = spell.getCausedStatus();
		
		// If the status is NOSTATUS, hides the icon
		if (status == BallStatus.NOSTATUS)
		{
			this.statusBoostIndicator.setPosition(-400, -400);
			return;
		}
		
		// Otherwise finds the elementIndicator representing the buff and 
		// positions the indicator to it
		for (ElementIndicator elementIndicator : this.indicators)
		{
			if (elementIndicator.getElement().getCausedStatus().equals(status))
			{
				this.statusBoostIndicator.goToRelativePosition(
						new Point2D.Double(0, 0), elementIndicator);
				return;
			}
		}
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	private class SpellInformationDrawer extends DrawnObject
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private ParagraphDrawer infoDrawer;
		private String spellName;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		public SpellInformationDrawer(Area area)
		{
			super(GameSettings.SCREENWIDTH / 2, GameSettings.SCREENHEIGHT / 2, 
					DepthConstants.HUD, area);
			
			// TODO: Use a smaller font here
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
		
		
		// OTHER METHODS	--------------------------------------------
		
		public void setSpell(Spell spell)
		{
			this.spellName = spell.getName();
			this.infoDrawer.setText(spell.getDescription());
		}
	}
	
	private class MeterDrawer
	{
		// ATTRIBUTES	------------------------------------------------
		
		private ArrayList<SpriteDrawerObject> meterBlocks;
		private int type;
		
		public static final int COLOUR = 1, TIME = 2;
		
		
		// CONSTRUCTOR	------------------------------------------------
		
		public MeterDrawer(int type)
		{
			// Initializes attributes
			this.type = type;
			this.meterBlocks = new ArrayList<SpriteDrawerObject>();
		}
		
		
		// OTHER METHODS	--------------------------------------------
		
		public void killBlocks()
		{
			for (SpriteDrawerObject block : this.meterBlocks)
			{
				block.kill();
			}
			
			this.meterBlocks.clear();
		}
		
		public void setSpell(Spell spell)
		{
			// Kills the previous blocks
			killBlocks();
			
			// Collects data
			int amount = 0;
			int angle = 0;
			Sprite blockSprite;
			
			if (this.type == COLOUR)
			{
				amount = spell.getManaUsage();
				angle = 180;
				blockSprite = MultiMediaHolder.getSprite("hud", "mpuse2");
			}
			else
			{
				amount = spell.getCastDelay() / 3;
				blockSprite = MultiMediaHolder.getSprite("hud", "timeuse");
			}
			
			// Creates the blocks
			int blocksLeft = amount / 10 + 1;
			SpriteDrawerObject latestBlock = null;
			
			while (blocksLeft > 0)
			{
				//System.out.println("Creates a block");
				
				double x = GameSettings.SCREENWIDTH / 2 + HelpMath.lendirX(330, angle);
				double y = GameSettings.SCREENHEIGHT / 2 + HelpMath.lendirY(260, angle);
				
				latestBlock = new SpriteDrawerObject(SpellBookInterface.this.area, 
						DepthConstants.NORMAL, null, blockSprite);
				
				latestBlock.setPosition(x, y);
				if (this.type == COLOUR)
					latestBlock.setAngle(angle);
				this.meterBlocks.add(latestBlock);
				
				angle += 10;
				blocksLeft --;
			}
			
			// Shrinks the latest block a bit since it's not supposed to be full
			double scaling = (amount % 10) / 10.0;
			latestBlock.setScale(scaling, scaling);
		}
	}
}
