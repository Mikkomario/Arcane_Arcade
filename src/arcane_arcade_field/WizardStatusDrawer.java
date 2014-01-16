package arcane_arcade_field;

import gameobjects.DrawnObject;
import graphic.SingleSpriteDrawer;
import handlers.ActorHandler;
import handlers.DrawableHandler;

import java.awt.Graphics2D;

import arcane_arcade_status.WizardStatus;
import arcane_arcade_worlds.Navigator;

/**
 * WizardStatusDrawer draws the status effects of the wizard
 *
 * @author Mikko Hilpinen.
 *         Created 30.8.2013.
 */
public class WizardStatusDrawer extends DrawableHandler
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Wizard wizard;
	private ActorHandler animator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new wizardstatusdrawer added to the given superhandler
	 *
	 * @param superhandler Which drawablehandler will draw the drawer
	 * @param animator Which actorhandler will animate the status sprites
	 * @param wizard the wizard who's status effects are drawn
	 */
	public WizardStatusDrawer(DrawableHandler superhandler, ActorHandler animator, 
			Wizard wizard)
	{
		super(true, false, wizard.getDepth() - 1, superhandler);
		
		// Initializes attributes
		this.wizard = wizard;
		this.animator = animator;
		
		// Creates the drawers and adds them to the handleds
		new StatusDrawer(this, "curse", WizardStatus.PANIC);
		new StatusDrawer(this, "ironflesh", WizardStatus.IRONFLESH);
		new StatusDrawer(this, "haste", WizardStatus.HASTE);
		new StatusDrawer(this, "paralyze", WizardStatus.PARALYZED);
		new StatusDrawer(this, "amnesia", WizardStatus.AMNESIA);
		new StatusDrawer(this, "rebirth", WizardStatus.REBIRTH);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------
	
	@Override
	protected Class<?> getSupportedClass()
	{
		// Only handles statusdrawers
		return StatusDrawer.class;
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	private class StatusDrawer extends DrawnObject
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private SingleSpriteDrawer spritedrawer;
		private WizardStatus status;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		/**
		 * Creates a new statusdrawer
		 *
		 * @param drawer The drawablehandler that will draw the drawer
		 * @param statusspritename what sprite in the bank "status" represents 
		 * the status effect
		 * @param status The status effect the drawer represents
		 */
		public StatusDrawer(WizardStatusDrawer drawer, String statusspritename, 
				WizardStatus status)
		{
			super(0, 0, 0, drawer);
			
			// Initializes attributes
			this.spritedrawer = new SingleSpriteDrawer(Navigator.getSpriteBank(
					"status").getSprite(statusspritename), 
					WizardStatusDrawer.this.animator, this);
			this.status = status;
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------

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
			// Forces transformation update (since wizard's transformations 
			// are used)
			forceTransformationUpdate();
			
			// Draws the sprite
			this.spritedrawer.drawSprite(g2d, 0, 0);
		}
		
		@Override
		public double getX()
		{
			// Uses the ball's coordinates
			return WizardStatusDrawer.this.wizard.getX();
		}
		
		@Override
		public double getY()
		{
			// Uses the ball's coordinates
			return WizardStatusDrawer.this.wizard.getY();
		}
		
		@Override
		public float getAlpha()
		{
			// The alpha value of the object depends on the strength of a 
			// status effect
			return (float) WizardStatusDrawer.this.wizard.getStatusStrength(
					this.status) / 100;
		}
		
		@Override
		public boolean isVisible()
		{
			// Only status effects that are affecting the ball are visible
			return super.isVisible() && this.status != null && 
					WizardStatusDrawer.this.wizard.getStatusStrength(
					this.status) > 0;
		}
	}
}
