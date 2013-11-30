package arcane_arcade_menus;

import java.awt.Graphics2D;

import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.Navigator;

import worlds.Room;

import listeners.RoomListener;

import handlers.DrawableHandler;
import handlers.MouseListenerHandler;
import helpAndEnums.DepthConstants;
import drawnobjects.DrawnObject;

/**
 * Creates an OptionBar for one of the game's options.
 * 
 * @author Unto Solala & Mikko Hilpinen
 * 			Created 8.9.2013
 */
public class OptionBar extends DrawnObject implements RoomListener
{
	// ATTRIBUTES-------------------------------------------------------
	
	private boolean active;
	private int value;
	private int minValue;
	private int maxValue;
	private String description;
	private DrawableHandler drawer;
	private OptionBarButton leftbutton, rightbutton;

	
	//CONSTRUCTOR-------------------------------------------------------
	
	/**
	 * Constructs an OptionBar based on the given parameters.
	 * 
	 * @param x The x-coordinate of the bar's left side (in pixels)
	 * @param y The y-coordinate of the bar's top (in pixels)
	 * @param drawer The drawablehandler that will draw the bar (optional)
	 * @param defaultValue The value the bar will have as default
	 * @param minValue The minimum value the bar can have
	 * @param maxValue The maximum value the bar can have
	 * @param description The description shown in the bar
	 * @param mousehandler The mouseHandler that will inform the bar about 
	 * mouse events
	 */
	public OptionBar(int x, int y, DrawableHandler drawer, int defaultValue,
			int minValue, int maxValue, String description, 
			MouseListenerHandler mousehandler)
	{
		super(x, y, DepthConstants.NORMAL, drawer);

		this.active = true;
		this.value = defaultValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.description = description;
		this.drawer = drawer;
		
		this.leftbutton = new OptionBarButton((int)this.getX(),
				(int)this.getY(), this.drawer, mousehandler,
				OptionBarButton.LEFT);
		this.rightbutton = new OptionBarButton((int)this.getX()+100,
				(int)this.getY(), this.drawer, mousehandler,
				OptionBarButton.RIGHT);
	}
	
	
	//GETTERS & SETTERS ------------------------------------------------
	
	/**
	 * @return The value the user has chosen
	 */
	public int getValue()
	{
		return this.value;
	}
	
	
	//IMPLEMENTED METHODS ----------------------------------------------
	
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
		g2d.setFont(GameSettings.BASICFONT);
		g2d.setColor(GameSettings.WHITETEXTCOLOR);
		g2d.drawString(""+this.value, 50, 0);
		g2d.drawString(this.description, 150, 0);
	}

	@Override
	public void onRoomStart(Room room)
	{
		// Does nothing
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Death approaches
		kill();
	}
	
	@Override
	public void kill()
	{
		super.kill();
		this.leftbutton.kill();
		this.rightbutton.kill();
	}

	/**
	 * OptionBarButtons are buttons used to change the OptionBar's
	 * values.
	 * 
	 * @author Unto Solala & Mikko Hilpinen
	 * 			Created 8.9.2013
	 */
	private class OptionBarButton extends AbstractMaskButton
	{
		private static final int RIGHT = 0;
		private static final int LEFT = 1;
		
		
		// ATTRIBUTES-------------------------------------------------------
		
		private int direction;
		
		
		//CONSTRUCTOR-------------------------------------------------------
		
		/**
		 * Creates the OptionBarButtons, which are used to change the values
		 * of various options in the game.
		 * 
		 * @param x	The x-coordinate of the button
		 * @param y The y-coordinate of the button
		 * @param drawer	The drawer that will draw the button
		 * @param mousehandler	The mouselistenerhandler that will inform the 
		 * button about mouse events
		 * @param direction	The direction the button is pointing, if it points
		 * to the LEFT, the button will lower the value. If it points to the 
		 * RIGHT, the button will increase the value.
		 */
		public OptionBarButton(int x, int y, DrawableHandler drawer,
				MouseListenerHandler mousehandler, int direction)
		{
			super(x, y, DepthConstants.NORMAL, 
					Navigator.getSpriteBank("menu").getSprite("arrow"), 
					Navigator.getSpriteBank("menu").getSprite("arrowmask"), 
					drawer, mousehandler, null);
			
			this.direction = direction;
			
			if (this.direction == LEFT)
				this.setXScale(-1);
		}

		
		// IMPLEMENTENTED METHODS ------------------------------------------

		@Override
		public boolean isActive()
		{
			return OptionBar.this.active;
		}

		@Override
		public void activate()
		{
			OptionBar.this.active = true;
		}

		@Override
		public void inactivate()
		{
			OptionBar.this.active = false;
		}

		@Override
		public void onLeftPressed(int mouseX, int mouseY)
		{
			//System.out.println("Left mouse button pressed");
			if(this.direction == LEFT)
			{
				//The arrow points to the left
				if(OptionBar.this.value>OptionBar.this.minValue)
					OptionBar.this.value = OptionBar.this.value -1;
			}
			else
			{
				//The arrow points to the right
				if(OptionBar.this.value<OptionBar.this.maxValue)
					OptionBar.this.value = OptionBar.this.value +1;
			}
		}

		@Override
		public boolean listensMouseEnterExit()
		{
			return true;
		}

		@Override
		public void onMouseEnter(int mouseX, int mouseY)
		{
			// Changes image index
			//System.out.println("You're hovering over the arrow.");
			getSpriteDrawer().setImageIndex(1);
		}

		@Override
		public void onMouseOver(int mouseX, int mouseY)
		{
			// Does nothing
		}

		@Override
		public void onMouseExit(int mouseX, int mouseY)
		{
			// Changes image index back
			//System.out.println("You left the arrow alone.");
			getSpriteDrawer().setImageIndex(0);
		}
	}
}
