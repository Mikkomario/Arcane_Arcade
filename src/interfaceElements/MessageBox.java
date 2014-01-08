package interfaceElements;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import resourcebanks.SpriteBank;
import gameobjects.DrawnObject;
import graphic.SpriteDrawer;
import handlers.ActorHandler;
import handlers.DrawableHandler;

/**
 * Messageboxes are used to present information to the user in text format. 
 * Messageboxes can be drawn on screen and manipulated like any DrawnObject
 * 
 * @author Mikko Hilpinen
 */
public class MessageBox extends DrawnObject
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private SpriteDrawer spritedrawer;
	private String message;
	// The iterator used in drawing the message on the box
	private AttributedCharacterIterator styledtextiterator;
	private Font font;
	private Color color;
	
	
	// CONSTRUCTOR	-----------------------------------------------------

	/**
	 * Creates a new abstract message to the given position with the given 
	 * message and background.
	 * 
	 * @param x The x-coordinate of the box's center
	 * @param y The y-coordinate of the box's center
	 * @param depth The drawing depth of the box
	 * @param message The message shown in the box
	 * @param textfont The font used in drawing the message
	 * @param textcolor What color is used when drawing the text
	 * @param bank The spritebank that contains the message's background sprite
	 * @param backgroundname The name of the background sprite in the bank
	 * @param drawer The drawablehandler that will draw the messagebox
	 * @param actorhandler The actorhandler that will animate the message 
	 * background (optional)
	 */
	public MessageBox(int x, int y, int depth, String message, 
			Font textfont, Color textcolor, SpriteBank bank, String backgroundname, 
			DrawableHandler drawer, ActorHandler actorhandler)
	{
		super(x, y, depth, drawer);

		// Initializes the attributes
		this.spritedrawer = new SpriteDrawer(bank.getSprite(backgroundname), 
				actorhandler, this);
		this.message = message;
		this.font = textfont;
		this.color = textcolor;
		
		AttributedString attstring = new AttributedString(message);
		attstring.addAttribute(TextAttribute.FONT, this.font);
		
		this.styledtextiterator = attstring.getIterator();
		
		// Adds the object to the handler(s)
		if (drawer != null)
			drawer.addDrawable(this);
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

	@Override
	public int getOriginX()
	{
		if (this.spritedrawer == null)
			return 0;
		
		return this.spritedrawer.getSprite().getWidth() / 2;
	}

	@Override
	public int getOriginY()
	{
		if (this.spritedrawer == null)
			return 0;
		
		return this.spritedrawer.getSprite().getHeight() / 2;
	}

	@Override
	public void drawSelfBasic(Graphics2D g2d)
	{
		Point topLeft = new Point(-this.spritedrawer.getSprite().getOriginX(), 
				-this.spritedrawer.getSprite().getOriginY());
		
		// Draws the background
		if (this.spritedrawer != null)
			this.spritedrawer.drawSprite(g2d, topLeft.x, topLeft.y);
		
		// And then the text
		g2d.setFont(this.font);
		g2d.setColor(this.color);
		
		// From: http://docs.oracle.com/javase/7/docs/api/java/awt/font/LineBreakMeasurer.html
		Point pen = new Point(topLeft.x + 15, topLeft.y + 15);
		FontRenderContext frc = g2d.getFontRenderContext();

		// "let styledText be an AttributedCharacterIterator containing at least
		// one character"
		
		LineBreakMeasurer measurer = new LineBreakMeasurer(this.styledtextiterator, frc);
		float wrappingWidth = this.spritedrawer.getSprite().getWidth() - 15;
		
		while (measurer.getPosition() < this.message.length()/*fStyledText.length()*/)
		{
			TextLayout layout = measurer.nextLayout(wrappingWidth);
		
		    pen.y += (layout.getAscent());
		    float dx = layout.isLeftToRight() ?
		    		 0 : (wrappingWidth - layout.getAdvance());
		
		    layout.draw(g2d, pen.x + dx, pen.y);
		    pen.y += layout.getDescent() + layout.getLeading();
		}
		
		// From oracle docs: 
		// http://docs.oracle.com/javase/7/docs/api/java/awt/font/TextLayout.html
		/*
		FontRenderContext frc = g2d.getFontRenderContext();
		TextLayout layout = new TextLayout(this.message, this.font, frc);
		layout.draw(g2d, (float) topLeft.getX(), (float) topLeft.getY());

		Rectangle2D bounds = layout.getBounds();
		bounds.setRect(
				bounds.getX() + topLeft.getX(), 
				bounds.getY() + topLeft.getY(), 
				bounds.getWidth(), bounds.getHeight());
		g2d.draw(bounds);
		*/
		//g2d.setFont(this.font);
		//g2d.drawString(this.message, - getOriginX(), - getOriginY());
	}
}
