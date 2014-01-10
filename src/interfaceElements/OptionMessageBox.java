package interfaceElements;

import gameobjects.DimensionalDrawnObject;
import graphic.Sprite;
import graphic.SpriteDrawer;
import handleds.LogicalHandled;
import handlers.ActorHandler;
import handlers.DrawableHandler;
import helpAndEnums.CollisionType;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import listeners.AdvancedMouseListener;
import listeners.OptionMessageBoxListener;
import listeners.TransformationListener;

/**
 * OptionMessageBoxes are interactive messageBoxes that show a number of 
 * buttons that represent different options the user can take. A number of 
 * users can listen to the events caused by the user-box-interaction
 * 
 * @author Mikko Hilpinen
 * created 8.1.2014
 */
public class OptionMessageBox extends MessageBox implements LogicalHandled
{
	// ATTRIBUTES	------------------------------------------------------
	
	private boolean active, autodeath;
	private OptionMessageBoxListener user;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * @param x The x-coordinate of the box's center
	 * @param y The y-coordinate of the box's center
	 * @param depth The drawing depth of the box
	 * @param message The message shown on the box
	 * @param textfont The font with which the message is drawn
	 * @param textcolor The color with which the text is drawn
	 * @param backgroundsprite The sprite used to draw the messageBox
	 * @param options A table containing all the names of the options shown 
	 * as buttons
	 * @param buttonsprite The sprite used to draw the buttons in the box. 
	 * Should contain (at least) 2 subimages so the buttons can react to 
	 * mouse hover
	 * @param diesafteruse Will the optionmessageBox die and disappear after 
	 * one of the options is chosen
	 * @param user The object that is interested in which of the options 
	 * was pressed (optional)
	 * @param drawer The drawableHandler that will draw the box (optional)
	 * @param actorhandler The actorHandler that will animate the background 
	 * sprite (optional)
	 */
	public OptionMessageBox(int x, int y, int depth, String message,
			Font textfont, Color textcolor, Sprite backgroundsprite, 
			String[] options, Sprite buttonsprite, boolean diesafteruse, 
			OptionMessageBoxListener user, DrawableHandler drawer,
			ActorHandler actorhandler)
	{
		super(x, y, depth, message, textfont, textcolor, backgroundsprite, drawer,
				actorhandler);
		
		// Initializes attributes
		this.active = true;
		this.autodeath = diesafteruse;
		this.user = user;
		
		int buttony = backgroundsprite.getHeight() - getOriginY() - MARGIN - 
				buttonsprite.getHeight() + buttonsprite.getOriginY();
		int minbuttonx = -getOriginX() + MARGIN + buttonsprite.getOriginX();
		int maxbuttonx = backgroundsprite.getWidth() - getOriginX() - MARGIN - 
				buttonsprite.getWidth() + buttonsprite.getOriginX();
		
		// Creates the options
		for (int i = 0; i < options.length; i++)
		{
			int buttonx = minbuttonx + (int) ((i / (double) options.length) * 
					(maxbuttonx - minbuttonx));
			
			new OptionButton(buttonx, buttony, buttonsprite, options[i], i, 
					textfont, textcolor, drawer, this);
		}
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
	
	
	// OTHER METHODS	-------------------------------------------------
	
	private void onOptionClick(OptionButton option)
	{
		// Informs the listener about the event
		if (this.user != null)
			this.user.onOptionMessageEvent(option.getText(), option.getIndex());
		// Dies if autodeath is on
		if (this.autodeath)
			kill();
	}
	
	
	// SUBCLASSES	-----------------------------------------------------
	
	/**
	 * OptionButton is a simple button that listens to mouse clicks and 
	 * and informs the messagebox if it is clicked.
	 * 
	 * @author Mikko Hilpinen
	 * created 8.1.2014
	 */
	private class OptionButton extends DimensionalDrawnObject implements 
			TransformationListener, AdvancedMouseListener
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private Point2D relativeposition;
		private SpriteDrawer spritedrawer;
		private OptionMessageBox box;
		private String text;
		private int index;
		private Font textfont;
		private Color textcolor;
		
		
		// CONSTRUCTOR	-------------------------------------------------
		
		public OptionButton(int relativex, int relativey, Sprite buttonsprite, 
				String text, int index, Font textfont, Color textcolor, 
				DrawableHandler drawer, OptionMessageBox containerbox)
		{
			super(0, 0, containerbox.getDepth(), false, 
					CollisionType.BOX, drawer, null);
			
			// Initializes attributes
			this.box = containerbox;
			this.text = text;
			this.index = index;
			this.relativeposition = new Point(relativex, relativey);
			this.spritedrawer = new SpriteDrawer(buttonsprite, null, this);
			this.textfont = textfont;
			this.textcolor = textcolor;
			
			updatePosition();
			
			// Adds the object to the handler(s)
			this.box.getTransformationListenerHandler().addListener(this);
		}
		
		
		// IMPLEMENTED METHODS	------------------------------------------

		@Override
		public int getWidth()
		{
			if (this.spritedrawer == null)
				return 0;
			
			return this.spritedrawer.getSprite().getWidth();
		}

		@Override
		public int getHeight()
		{
			if (this.spritedrawer == null)
				return 0;
			
			return this.spritedrawer.getSprite().getHeight();
		}

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
			// Draws the sprite
			if (this.spritedrawer == null)
				return;
			
			this.spritedrawer.drawSprite(g2d, 0, 0);
			
			g2d.setFont(this.textfont);
			g2d.setColor(this.textcolor);
			g2d.drawString(this.text, 0, 0);
		}

		@Override
		public boolean isActive()
		{
			return this.box.isActive();
		}

		@Override
		public void activate()
		{
			this.box.activate();
		}

		@Override
		public void inactivate()
		{
			this.box.inactivate();
		}

		@Override
		public void onTransformationEvent(TransformationEvent e)
		{
			// Resets the position
			updatePosition();
		}
		
		@Override
		public double getXScale()
		{
			return this.box.getXScale();
		}
		
		@Override
		public double getYScale()
		{
			return this.box.getYScale();
		}
		
		@Override
		public double getXShear()
		{
			return this.box.getXShear();
		}
		
		@Override
		public double getYShear()
		{
			return this.box.getYShear();
		}
		
		@Override
		public double getAngle()
		{
			return this.box.getAngle();
		}
		
		@Override
		public void onLeftDown(int mouseX, int mouseY, double steps)
		{
			// Does nothing
		}

		@Override
		public void onRightDown(int mouseX, int mouseY, double steps)
		{
			// Does nothing
		}

		@Override
		public void onLeftPressed(int mouseX, int mouseY)
		{
			// Informs the box about button press
			this.box.onOptionClick(this);
		}

		@Override
		public void onRightPressed(int mouseX, int mouseY)
		{
			// Does nothing
		}

		@Override
		public void onLeftReleased(int mouseX, int mouseY)
		{
			// Does nothing
		}

		@Override
		public void onRightReleased(int mouseX, int mouseY)
		{
			// Does nothing
		}

		@Override
		public boolean listensPosition(int x, int y)
		{
			return pointCollides(x, y);
		}

		@Override
		public boolean listensMouseEnterExit()
		{
			return true;
		}

		@Override
		public void onMouseEnter(int mouseX, int mouseY)
		{
			// Button reacts to mouse over by changing sprite index
			this.spritedrawer.setImageIndex(1);
		}

		@Override
		public void onMouseOver(int mouseX, int mouseY)
		{
			// Does nothing
		}

		@Override
		public void onMouseExit(int mouseX, int mouseY)
		{
			this.spritedrawer.setImageIndex(0);
		}

		@Override
		public void onMouseMove(int mouseX, int mouseY)
		{
			// Does nothing
		}

		@Override
		public MouseButtonEventScale getCurrentButtonScaleOfInterest()
		{
			return MouseButtonEventScale.LOCAL;
		}
		
		@Override
		public boolean isVisible()
		{
			return super.isVisible() && this.box.isVisible();
		}
		
		@Override
		public boolean isDead()
		{
			return super.isDead() || this.box.isDead();
		}
		
		
		// GETTERS & SETTERS	-----------------------------------------
		
		public int getIndex()
		{
			return this.index;
		}
		
		public String getText()
		{
			return this.text;
		}
		
		
		// OTHER METHODS	---------------------------------------------
		
		private void updatePosition()
		{
			// Keeps the object in the same relative point relative to the 
			// containing box
			Point2D newposition = this.box.transform(
					this.relativeposition.getX(), this.relativeposition.getY());
			
			setPosition(newposition.getX(), newposition.getY());
		}
	}
}
