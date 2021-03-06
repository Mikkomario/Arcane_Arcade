package arcane_arcade_menus;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import utopia_gameobjects.DrawnObject;
import utopia_handleds.Actor;
import utopia_utility.CollisionType;
import utopia_utility.DepthConstants;
import utopia_interfaceElements.AbstractButton;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import arcane_arcade_main.GameSettings;

/**
 * Menubuttons have different functions and can be clicked. MenuButtons also 
 * draw their description near them and change sprite index when mouse goes 
 * over them.
 *
 * @author Mikko Hilpinen.
 * @since 4.9.2013.
 */
public abstract class MenuButton extends AbstractButton
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private boolean mouseon;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a menubutton to the given position with the given information 
	 * added to the given handlers
	 *
	 * @param x The x-coordinate of the button
	 * @param y The y-coordinate of the button
	 * @param message The message the button will show when the mouse hovers 
	 * over it
	 * @param area The area where the object is placed to
	 */
	public MenuButton(int x, int y, String message, Area area)
	{
		super(x, y, DepthConstants.FOREGROUND, 
				MultiMediaHolder.getSpriteBank("menu").getSprite("button"), 
				area);
		
		// Initializes attributes
		this.mouseon = false;
		
		// Sets the radius and collision type
		setCollisionType(CollisionType.CIRCLE);
		setRadius(getSpriteDrawer().getSprite().getOriginX());
		
		// Creates the textdrawer
		new ButtonTextDrawer((int) getX(), (int) getY(), message, area);
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	public boolean listensMouseEnterExit()
	{
		return true;
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType, 
			Point2D.Double mousePosition, double eventStepTime)
	{
		// Changes sprite index when mouse enters or exits the button
		if (eventType == MousePositionEventType.ENTER)
		{
			this.mouseon = true;
			getSpriteDrawer().setImageIndex(1);
		}
		else if (eventType == MousePositionEventType.EXIT)
		{
			this.mouseon = false;
			getSpriteDrawer().setImageIndex(0);
		}
	}
	
	
	// SUBCLASSES	-----------------------------------------------------
	
	// The buttontextdrawer draws a text over the button when the mouse is 
	// over it
	private class ButtonTextDrawer extends DrawnObject implements Actor
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private char[] message;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		/**
		 * Creates a new textdrawer to the given position added to the given 
		 * handlers showing the given message
		 *
		 * @param x The x-coordinate of the text
		 * @param y The y-coordinate of the text
		 * about step events
		 * @param text The text the drawer will draw
		 * @param area The area where the object is placed to
		 */
		public ButtonTextDrawer(int x, int y, String text, Area area)
		{
			super(x, y, DepthConstants.FOREGROUND, area);
			
			// Initializes attributes
			this.message = text.toCharArray();
			
			// Adds the object to the handler
			if (area.getActorHandler() != null)
				area.getActorHandler().addActor(this);
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
			// Draws the text with the basic font and white colour
			g2d.setFont(GameSettings.BASICFONT);
			g2d.setColor(GameSettings.WHITETEXTCOLOR);
			
			g2d.rotate(Math.toRadians(-15 * this.message.length / 2 + 7));
			
			for (int i = 0; i < this.message.length; i++)
			{
				g2d.drawChars(this.message, i, 1, -10, 
						- MenuButton.this.getWidth() / 2 - 20);
				g2d.rotate(Math.toRadians(15));
			}
		}

		@Override
		public boolean isActive()
		{
			return MenuButton.this.isActive();
		}

		@Override
		public void activate()
		{
			// Can't be activated through the textdrawer
		}

		@Override
		public void inactivate()
		{
			// Can't bedeactivated through the textdrawer
		}

		@Override
		public void act(double steps)
		{
			// Adjusts the alpha value of the text
			if (MenuButton.this.mouseon)
			{
				if (getAlpha() < 1)
					adjustAlpha((float) (0.05 * steps));
			}
			else
			{
				if (getAlpha() > 0)
					adjustAlpha((float) (-0.01 * steps));
			}
		}
		
		@Override
		public boolean isVisible()
		{
			return MenuButton.this.isVisible() && getAlpha() > 0;
		}
		
		@Override
		public boolean isDead()
		{
			return MenuButton.this.isDead();
		}
	}
}