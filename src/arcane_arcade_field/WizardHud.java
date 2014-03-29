package arcane_arcade_field;

import java.awt.Graphics2D;

import utopia_gameobjects.DrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handleds.Drawable;
import utopia_handleds.Handled;
import utopia_handlers.Handler;
import utopia_helpAndEnums.DepthConstants;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import arcane_arcade_main.GameSettings;

/**
 * Wizardhud draws some information on screen like the remaining mana, 
 * mana usage, HP and active elements.
 *
 * @author Mikko Hilpinen.
 * @since 30.8.2013.
 */
public class WizardHud extends Handler
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private static int mpblockwidth = 32;
	private static int mpblockheight = 32;
	private static int hpblockwidth = 32;
	private static int hpblockheight = 32;
	
	private Wizard wizard;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new wizardstatusdrawer added to the given superhandler
	 *
	 * @param area The area where the object is placed to
	 * @param wizard the wizard who's status effects are drawn
	 */
	public WizardHud(Area area, Wizard wizard)
	{
		super(true, null);
		
		// Initializes attributes
		this.wizard = wizard;
		
		// Creates the drawers and adds them to the handleds
		int element1x = 35;
		int elementy = GameSettings.SCREENHEIGHT - 97;
		ScreenSide side = this.wizard.getScreenSide();
		// Positions are different if the screenside is different
		if (side == ScreenSide.RIGHT)
			element1x = GameSettings.SCREENWIDTH - 93;
		int depth = DepthConstants.HUD;
		
		// Current 1
		ElementDrawer current1 = new ElementDrawer(element1x, elementy, 
				depth - 1, this, ElementDrawer.ELEMENTINDEX_CURRENT, area);
		int elementiconwidth = current1.getWidth();
		int elementiconheight = current1.getHeight();
		// Current 2
		new ElementDrawer(element1x + elementiconwidth, 
				elementy, depth - 1, this, ElementDrawer.ELEMENTINDEX_CURRENT_SECOND, area);
		// Next 1
		new ElementDrawer(element1x, elementy - elementiconheight / 2, depth, this, 
				ElementDrawer.ELEMENTINDEX_NEXT, area);
		// Next 2
		new ElementDrawer(element1x + elementiconwidth, 
				elementy - elementiconheight / 2, depth, this, 
				ElementDrawer.ELEMENTINDEX_NEXT_SECOND, area);
		// Last 1
		new ElementDrawer(element1x, elementy + elementiconheight / 2, depth, 
				this, ElementDrawer.ELEMENTINDEX_LAST, area);
		// Last 2
		new ElementDrawer(element1x + elementiconwidth, 
				elementy + elementiconheight / 2, depth, 
				this, ElementDrawer.ELEMENTINDEX_LAST_SECOND, area);
		
		// Creates the MP-meters and adds them to the drawer
		int meterx = element1x + (int) (elementiconwidth * 1.5 + 10);
		int metery = GameSettings.SCREENHEIGHT - mpblockheight * 2 - 10;
		
		if (side == ScreenSide.RIGHT)
			meterx = element1x - (int) (elementiconwidth * 0.5 + 10);
		
		// The bottom meter
		new MPMeterDrawer(meterx, metery, depth + 4, this, 0, area);
		// Lost mp meter
		new LostMPDrawer(meterx, metery, depth + 3, this, area);
		// Current mp meter
		new CurrentMPDrawer(meterx, metery, depth + 2, this, area);
		// MP use meter
		new MPUseMeter(meterx, metery - 10, depth + 1, this, side, area);
		
		// Creates the HP-meter and adds it to the drawer
		metery -= hpblockheight + 10;
		new HealthMeter(meterx, metery, depth + 2, this, area);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	protected Class<?> getSupportedClass()
	{
		// Only handles statusdrawers
		return WizardHudElement.class;
	}
	
	@Override
	protected boolean handleObject(Handled h)
	{
		((WizardHudElement) h).onSpellChange(); 
		return true;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Updates the hud to show values according to the new element combination
	 */
	protected void callSpellUpdate()
	{
		// Goes through the handleds and updates their status
		handleObjects();
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	private interface WizardHudElement extends Drawable
	{
		// WizardHudElements may react to spell changes
		public void onSpellChange();
	}
	
	// Elementdrawer draws a single element icon
	private class ElementDrawer extends DrawnObject implements WizardHudElement
	{
		// ATTRIBUTES	------------------------------------------------
		
		private static final int ELEMENTINDEX_LAST = -1;
		private static final int ELEMENTINDEX_LAST_SECOND = -2;
		private static final int ELEMENTINDEX_NEXT = 1;
		private static final int ELEMENTINDEX_NEXT_SECOND = 2;
		private static final int ELEMENTINDEX_CURRENT = 0;
		private static final int ELEMENTINDEX_CURRENT_SECOND = 3;
		
		private SingleSpriteDrawer spritedrawer;
		private int elementindex;
		private int elementspriteindex;
		
		
		// CONSTRUCTOR	------------------------------------------------
		
		/**
		 * Creates a new elementdrawer with the given information to the given 
		 * position
		 *
		 * @param x The x-coordinate of the icon's left top corner
		 * @param y The y-coordinate of the icon's left top corner
		 * @param depth The drawing depth of the hud element
		 * @param drawer The HudElementDrawer that will draw the element
		 * @param elementindex Which element index the drawer will draw (use 
		 * the static values the class provides)
		 * @param area The area where the object is placed to
		 */
		public ElementDrawer(int x, int y, int depth, WizardHud drawer, 
				int elementindex, Area area)
		{
			super(x, y, depth, area);
			
			// Initializes attributes
			this.spritedrawer = new SingleSpriteDrawer(
					MultiMediaHolder.getSpriteBank("hud").getSprite(
					"elements"), null, this);
			this.elementindex = elementindex;
			this.elementspriteindex = 0;
			
			onSpellChange();
			
			// Changes alpha depending on the elementindex
			if (this.elementindex == ELEMENTINDEX_LAST ||
					this.elementindex == ELEMENTINDEX_LAST_SECOND ||
					this.elementindex == ELEMENTINDEX_NEXT || 
					this.elementindex == ELEMENTINDEX_NEXT_SECOND)
				setAlpha(0.4f);
			
			// If the element resides on the right side it is mirrored
			if (WizardHud.this.wizard.getScreenSide() == ScreenSide.RIGHT)
				scale(-1, 1);
			
			drawer.addHandled(this);
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
			// Doesn't draw itself if the object hasn't been initialized yet
			if (this.spritedrawer == null)
				return;
			
			// Draws the shown element
			this.spritedrawer.drawSprite(g2d, 0, 0, this.elementspriteindex);
		}
		
		@Override
		public void onSpellChange()
		{
			// Gets the element index
			Wizard wizard = WizardHud.this.wizard;
			int elemindex = 0;
			switch(this.elementindex)
			{
				case ELEMENTINDEX_CURRENT: elemindex = 
						wizard.getFirstElementIndex(); break;
				case ELEMENTINDEX_CURRENT_SECOND: elemindex = 
						wizard.getSecondElementIndex(); break;
				case ELEMENTINDEX_LAST: elemindex = 
						wizard.getPreviousElementIndex(
						wizard.getFirstElementIndex()); break;
				case ELEMENTINDEX_LAST_SECOND: elemindex = 
						wizard.getPreviousElementIndex(
						wizard.getSecondElementIndex()); break;
				case ELEMENTINDEX_NEXT: elemindex = 
						wizard.getNextElementIndex(
						wizard.getFirstElementIndex()); break;
				case ELEMENTINDEX_NEXT_SECOND: elemindex = 
						wizard.getNextElementIndex(
						wizard.getSecondElementIndex()); break;
			}
			this.elementspriteindex = 
					wizard.getElement(elemindex).getElementIconIndex();
		}
		
		
		// OTHER METHODS	---------------------------------------------
		
		private int getHeight()
		{
			return this.spritedrawer.getSprite().getHeight();
		}
		
		private int getWidth()
		{
			return this.spritedrawer.getSprite().getWidth();
		}
	}
	
	// MP-Meter drawer draws a parth of the mp-meter to the given location.
	private class MPMeterDrawer extends DrawnObject implements WizardHudElement
	{
		// ATTRIBUTES	--------------------------------------------------
		
		private int length;
		private SingleSpriteDrawer spritedrawer;
		
		
		// CONSTRUCTOR	--------------------------------------------------
		
		/**
		 * Creates a new MPMeterdrawer to the given position added to the 
		 * given handler.
		 *
		 * @param x The meter's top left corner's y-coordinate
		 * @param y The meter's top left corner's x-coordinate
		 * @param depth The drawing depth of the meter
		 * @param drawer The drawer that will draw the meter
		 * @param meterimageindex Which subimage from the MP-meter sprite is 
		 * used to represent the meter's blocks
		 * @param area The area where the object is placed to
		 */
		public MPMeterDrawer(int x, int y, int depth, WizardHud drawer, 
				int meterimageindex, Area area)
		{
			super(x, y, depth, area);
			
			// Initializes attributes
			this.spritedrawer = new SingleSpriteDrawer(
					MultiMediaHolder.getSpriteBank("hud").getSprite(
					"mp"), null, this);
			this.spritedrawer.setImageIndex(meterimageindex);
			this.length = 10;
			
			// Right side elements are flipped
			if (WizardHud.this.wizard.getScreenSide() == ScreenSide.RIGHT)
				scale(-1, 1);
			
			drawer.addHandled(this);
		}
		
		
		// IMPLEMENTED METHODS	------------------------------------------

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
			// Draws blocks up to the length of the meter
			for (int i = 0; i < this.length; i++)
			{
				this.spritedrawer.drawSprite(g2d, 
						i * WizardHud.mpblockwidth, 0);
			}
		}
		
		@Override
		public void onSpellChange()
		{
			// Does nothing
		}
		
		
		// OTHER METHODS	----------------------------------------------
		
		/**
		 * Changes how many blocks of the meter is drawn
		 *
		 * @param newlength The new amount of blocks drawn
		 */
		protected void setLength(int newlength)
		{
			this.length = newlength;
			
			// Fixes the length
			if (this.length < 0)
				this.length = 0;
			else if (this.length > 10)
				this.length = 10;
		}
	}
	
	
	// This MPMeterDrawer draws the blue MP-Bar showing the current MP
	private class CurrentMPDrawer extends MPMeterDrawer
	{
		// CONSTRUCTOR	-------------------------------------------------
		
		/**
		 * Creates a new MPMeterdrawer to the given position added to the 
		 * given handler.
		 *
		 * @param x The meter's top left corner's y-coordinate
		 * @param y The meter's top left corner's x-coordinate
		 * @param depth The drawing depth of the meter
		 * @param drawer The drawer that will draw the meter
		 * @param area The area where the object is placed to
		 */
		public CurrentMPDrawer(int x, int y, int depth, WizardHud drawer, Area area)
		{
			super(x, y, depth, drawer, 1, area);
		}
		
		
		// IMPLEMENTED METHODS	----------------------------------------
		
		@Override
		public void drawSelfBasic(Graphics2D g2d)
		{
			// Adjusts length before drawing the meter
			setLength((int) WizardHud.this.wizard.getMana() / 10);
			
			super.drawSelfBasic(g2d);
		}
	}
	
	
	// This MPMeterdrawer draws the MP lost during the casting
	private class LostMPDrawer extends MPMeterDrawer
	{
		// CONSTRUCTOR	------------------------------------------------
		
		/**
		 * Creates a new MPMeterdrawer to the given position added to the 
		 * given handler.
		 *
		 * @param x The meter's top left corner's y-coordinate
		 * @param y The meter's top left corner's x-coordinate
		 * @param depth The drawing depth of the meter
		 * @param drawer The drawer that will draw the meter
		 * @param area The area where the object is placed to
		 */
		public LostMPDrawer(int x, int y, int depth, WizardHud drawer, Area area)
		{
			super(x, y, depth, drawer, 2, area);
		}
		
		
		// IMPLEMENTED METHODS	----------------------------------------
		
		@Override
		public void drawSelf(Graphics2D g2d)
		{
			// Adjusts length before drawing the meter
			setLength(WizardHud.this.wizard.getManaBeforeCasting() / 10);
			// Also adjusts the alpha value
			setAlpha(1 - 
					(float) WizardHud.this.wizard.getCastingProgress());
			
			super.drawSelf(g2d);
		}
		
		@Override
		public boolean isVisible()
		{
			// The meter is only visible while the wizard is casting
			return super.isVisible() && WizardHud.this.wizard.isCasting();
		}
	}
	
	
	// This class draws a stick that shows how much mana will be used when the 
	// next spell is cast
	private class MPUseMeter extends DrawnObject implements WizardHudElement
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private int meterwidth;
		private SingleSpriteDrawer spritedrawer;
		private int mpuse;
		private int xscalemodifier;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		/**
		 * Creates a new MPUseMeter to the given position added to the given 
		 * handler
		 *
		 * @param x The x-coordinate of the meter's left top corner
		 * @param y The y-coordinate of the meter's left top corner
		 * @param depth How deep the meter will be drawn
		 * @param drawer Which wizardHudDrawer draws the meter
		 * @param side On which side of the screen the meter is drawn
		 * @param area The area where the object is placed to
		 */
		public MPUseMeter(int x, int y, int depth, WizardHud drawer, 
				ScreenSide side, Area area)
		{
			super(x, y, depth, area);
			
			// Initializes attributes
			this.spritedrawer = new SingleSpriteDrawer(
					MultiMediaHolder.getSpriteBank("hud").getSprite(
					"mpuse"), null, this);
			this.meterwidth = this.spritedrawer.getSprite().getWidth() - 
					this.spritedrawer.getSprite().getOriginX();
			this.mpuse = 0;
			this.xscalemodifier = 1;
			
			if (side == ScreenSide.RIGHT)
				this.xscalemodifier = -1;
			
			onSpellChange();
			
			drawer.addHandled(this);
		}
		
		
		// IMPLMENTED MEHTODS	-----------------------------------------

		@Override
		public void onSpellChange()
		{
			// Updates the MP-use and the scaling of the meter
			this.mpuse = 
					WizardHud.this.wizard.getCurrentSpell().getManaUsage();
			setXScale((this.mpuse / 10.0) * WizardHud.mpblockwidth / 
					this.meterwidth * this.xscalemodifier);
		}

		@Override
		public int getOriginX()
		{
			return this.spritedrawer.getSprite().getOriginX();
		}

		@Override
		public int getOriginY()
		{
			return this.spritedrawer.getSprite().getOriginY();
		}

		@Override
		public void drawSelfBasic(Graphics2D g2d)
		{
			// Sets the meter to the right position
			double x = (WizardHud.this.wizard.getMana() - this.mpuse) / 
					10.0 * WizardHud.mpblockwidth * (1 / Math.abs(getXScale()));
			// Checks if the wizard has enough mana and changes the image index 
			// if not
			int imgindex = 0;
			if (!WizardHud.this.wizard.hasEnoughMana(this.mpuse))
				imgindex = 1;
			
			this.spritedrawer.drawSprite(g2d,(int) x, 0, imgindex);
		}
	}
	
	
	// Healthmeterdrawer draws a wizard's health meter
	private class HealthMeter extends DrawnObject implements WizardHudElement
	{
		// ATTRIBUTES	--------------------------------------------------
		
		private SingleSpriteDrawer spritedrawer;
		
		
		// CONSTRUCTOR	--------------------------------------------------
		
		/**
		 * Creates a new healthmeterdrawer to the given position added to the 
		 * given drawer
		 *
		 * @param x The x-coordinate of the meter's left top corner
		 * @param y The y-coordinate of the meter's left top corner
		 * @param depth The drawing depth of the meter
		 * @param drawer The huddrawer that will draw the meter
		 * @param area The area where the object is placed to
		 */
		public HealthMeter(int x, int y, int depth, WizardHud drawer, 
				Area area)
		{
			super(x, y, depth, area);
			
			// Initializes attributes
			this.spritedrawer = new SingleSpriteDrawer(
					MultiMediaHolder.getSpriteBank("hud").getSprite(
					"hp"), null, this);
			
			// Flips the meter if on the right side
			if (WizardHud.this.wizard.getScreenSide() == ScreenSide.RIGHT)
				scale(-1, 1);
			
			drawer.addHandled(this);
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
			// draws full or empty hearts
			int hp = WizardHud.this.wizard.getHP();
			int maxhp = WizardHud.this.wizard.getMaxHP();
			
			for (int full = 0; full < hp; full ++)
			{
				this.spritedrawer.drawSprite(g2d, 
						full * WizardHud.hpblockwidth, 0, 1);
			}
			for (int empty = hp; empty < maxhp; empty ++)
			{
				this.spritedrawer.drawSprite(g2d, 
						empty * WizardHud.hpblockwidth, 0, 0);
			}
		}

		@Override
		public void onSpellChange()
		{
			// Doesn't react to spell change
		}
	}
}
