package arcane_arcade_field;

import java.awt.Graphics2D;

import utopia_gameobjects.DrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handlers.ActorHandler;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import arcane_arcade_status.WizardStatus;

/**
 * WizardStatusDrawer draws the status effects of the wizard
 *
 * @author Mikko Hilpinen.
 *         Created 30.8.2013.
 */
public class WizardStatusDrawer
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Wizard wizard;
	private ActorHandler animator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new wizardstatusdrawer added to the given superhandler
	 *
	 * @param area The area where the object is placed to
	 * @param wizard the wizard who's status effects are drawn
	 */
	public WizardStatusDrawer(Area area, Wizard wizard)
	{	
		// Initializes attributes
		this.wizard = wizard;
		
		// Creates the drawers and adds them to the handleds
		new StatusDrawer(area, "curse", WizardStatus.PANIC);
		new StatusDrawer(area, "ironflesh", WizardStatus.IRONFLESH);
		new StatusDrawer(area, "haste", WizardStatus.HASTE);
		new StatusDrawer(area, "paralyze", WizardStatus.PARALYZED);
		new StatusDrawer(area, "amnesia", WizardStatus.AMNESIA);
		new StatusDrawer(area, "rebirth", WizardStatus.REBIRTH);
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
		 * @param area The area where the object is placed to
		 * @param statusspritename what sprite in the bank "status" represents 
		 * the status effect
		 * @param status The status effect the drawer represents
		 */
		public StatusDrawer(Area area, String statusspritename, 
				WizardStatus status)
		{
			super(0, 0, WizardStatusDrawer.this.wizard.getDepth() - 1, area);
			
			// Initializes attributes
			this.spritedrawer = new SingleSpriteDrawer(MultiMediaHolder.getSpriteBank(
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
		
		@Override
		public boolean isDead()
		{
			return WizardStatusDrawer.this.wizard.isDead();
		}
	}
}
