package arcane_arcade_field;

import java.awt.Graphics2D;

import arcane_arcade_main.GameSettings;
import arcane_arcade_main.Main;
import drawnobjects.DrawnObject;
import graphic.SpriteDrawer;
import handleds.Drawable;
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
	
	
	// SUBCLASSES	------------------------------------------------------
	
	private interface WizardHudElement extends Drawable
	{
		// This interface is simply used as a wrapper
	}
	
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
			// Gets the element index
			int elemindex = 0;
			Wizard wizard = WizardHudDrawer.this.wizard;
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
			this.spritedrawer.drawSprite(g2d, 0, 0, wizard.getElement(
					elemindex).getElementIconIndex());
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
}
