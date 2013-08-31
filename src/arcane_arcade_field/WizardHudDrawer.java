package arcane_arcade_field;

import java.awt.Graphics2D;
import java.util.Iterator;

import arcane_arcade_main.GameSettings;
import arcane_arcade_main.Main;
import drawnobjects.DrawnObject;
import graphic.SpriteDrawer;
import handleds.Drawable;
import handleds.Handled;
import handlers.DrawableHandler;
import helpAndEnums.DepthConstants;

/**
 * Wizardhud draws some information on screen like the remaining mana, 
 * mana usage, HP and active elements.
 *
 * @author Mikko Hilpinen.
 *         Created 30.8.2013.
 */
public class WizardHudDrawer extends DrawableHandler
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Wizard wizard;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new wizardstatusdrawer added to the given superhandler
	 *
	 * @param superhandler Which drawablehandler will draw the drawer
	 * @param wizard the wizard who's status effects are drawn
	 */
	public WizardHudDrawer(DrawableHandler superhandler, Wizard wizard)
	{
		super(true, true, DepthConstants.HUD, superhandler);
		
		// Initializes attributes
		this.wizard = wizard;
		
		// Creates the drawers and adds them to the handleds
		int element1x = 5;
		int elementy = GameSettings.SCREENHEIGHT - 140;
		// Current 1
		ElementDrawer elementexample = new ElementDrawer(element1x, elementy, 
				DepthConstants.HUD -1, this, 
				ElementDrawer.ELEMENTINDEX_CURRENT);
		// Current 2
		new ElementDrawer(element1x + elementexample.getWidth(), elementy, 
				DepthConstants.HUD -1, this, 
				ElementDrawer.ELEMENTINDEX_CURRENT_SECOND);
		// Next 1
		new ElementDrawer(element1x, elementy - 
				(int) (elementexample.getHeight() * 0.5), DepthConstants.HUD, 
				this, ElementDrawer.ELEMENTINDEX_NEXT);
		// Next 2
		new ElementDrawer(element1x + elementexample.getWidth(), elementy - 
				(int) (elementexample.getHeight() * 0.5), DepthConstants.HUD, 
				this, ElementDrawer.ELEMENTINDEX_NEXT_SECOND);
		// Last 1
		new ElementDrawer(element1x, elementy + 
				(int) (elementexample.getHeight() * 0.5), DepthConstants.HUD, 
				this, ElementDrawer.ELEMENTINDEX_LAST);
		// Last 2
		new ElementDrawer(element1x + elementexample.getWidth(), elementy + 
				(int) (elementexample.getHeight() * 0.5), DepthConstants.HUD, 
				this, ElementDrawer.ELEMENTINDEX_LAST_SECOND);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	protected Class<?> getSupportedClass()
	{
		// Only handles statusdrawers
		return WizardHudElement.class;
	}
	
	
	// OTHER METHODS	--------------------------------------------------
	
	/**
	 * Updates the hud to show values according to the new element combination
	 */
	protected void callSpellUpdate()
	{
		// Goes through the handleds and updates their status
		Iterator<Handled> iterator = getIterator();
		while (iterator.hasNext())
		{
			((WizardHudElement) iterator.next()).onSpellChange(); 
		}
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
		
		private SpriteDrawer spritedrawer;
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
		 */
		public ElementDrawer(int x, int y, int depth, WizardHudDrawer drawer, 
				int elementindex)
		{
			super(x, y, depth, drawer);
			
			// Initializes attributes
			this.spritedrawer = new SpriteDrawer(
					Main.spritebanks.getOpenSpriteBank("hud").getSprite(
					"elements"), null);
			this.elementindex = elementindex;
			this.elementspriteindex = 0;
			
			onSpellChange();
			
			// Changes alpha depending on the elementindex
			if (this.elementindex == ELEMENTINDEX_LAST ||
					this.elementindex == ELEMENTINDEX_LAST_SECOND ||
					this.elementindex == ELEMENTINDEX_NEXT || 
					this.elementindex == ELEMENTINDEX_NEXT_SECOND)
				setAlpha(0.4f);
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
			Wizard wizard = WizardHudDrawer.this.wizard;
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
		private SpriteDrawer spritedrawer;
		
		
		// CONSTRUCTOR	--------------------------------------------------
		
		/**
		 * Creates a new MPMeterdrawer to the given position added to the 
		 * given handler.
		 *
		 * @param x The meter's top left corner's y-coordinate
		 * @param y The meter's top left corner's x-coordinate
		 * @param depth The drawing depth of the meter
		 * @param drawer The drawer that will draw the meter
		 * @param length How long is the meter [0, 10]
		 * @param meterimageindex Which subimage from the MP-meter sprite is 
		 * used to represent the meter's blocks
		 */
		public MPMeterDrawer(int x, int y, int depth, WizardHudDrawer drawer, 
				int length, int meterimageindex)
		{
			super(x, y, depth, drawer);
			
			// Initializes attributes
			this.spritedrawer = new SpriteDrawer(
					Main.spritebanks.getOpenSpriteBank("hud").getSprite(
					"mp"), null);
			this.spritedrawer.setImageIndex(meterimageindex);
			this.length = length;
			
			// Fixes the length
			if (this.length < 0)
				this.length = 0;
			else if (this.length > 10)
				this.length = 10;
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
			int blockwidth = this.spritedrawer.getSprite().getWidth();
			
			// Draws blocks up to the length of the meter
			for (int i = 0; i < this.length; i++)
			{
				this.spritedrawer.drawSprite(g2d, i * blockwidth, 0);
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
}
