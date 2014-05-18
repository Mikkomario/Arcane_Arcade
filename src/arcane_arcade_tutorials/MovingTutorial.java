package arcane_arcade_tutorials;

import java.awt.Graphics2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import utopia_gameobjects.CollidingDrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handleds.Collidable;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_utility.CollisionType;
import utopia_utility.DepthConstants;
import utopia_worlds.Area;
import arcane_arcade_field.Wall;
import arcane_arcade_field.Wizard;
import arcane_arcade_main.Button;
import arcane_arcade_main.GameSettings;
import arcane_arcade_main.Options;
import arcane_arcade_worlds.Navigator;

/**
 * MovingTutorial teaches the player the basics of moving around. The turorial 
 * includes just 2 phases: moving and teleporting.
 * 
 * @author Mikko Hilpinen
 * @since 9.5.2014
 */
public class MovingTutorial
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private int phase, booksLeft;
	private Navigator navigator;
	private Area area;
	private Wizard wizard;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates and starts a new moving tutorial
	 * 
	 * @param wizard The wizard that will complete the tutorial
	 * @param navigator The navigator that is used to move between the areas
	 * @param area The area where the tutorial is held at
	 */
	public MovingTutorial(Wizard wizard, Navigator navigator, Area area)
	{
		// Initializes attributes
		this.phase = 0;
		this.booksLeft = 0;
		this.navigator = navigator;
		this.area = area;
		this.wizard = wizard;
		
		// Starts the tutorial by creating the books
		createBooks();
		
		// TODO: Remove the wizard's colour and colour regeneration
		
		TutorialObjectCreator.showMessage("Move up and down by pressing " + 
				Options.leftwizardbuttons.get(Button.UP) + " and " + 
				Options.leftwizardbuttons.get(Button.DOWN) + ".#Collect the books!", 
				area);
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * This method should be called when a wizard collects a book that is 
	 * part of the tutorial
	 */
	public void onBookCollected()
	{
		this.booksLeft --;
		
		if (this.booksLeft == 0)
		{
			// At the end of the first phase, resets the books & wizard and 
			// creates extra walls
			if (this.phase == 0)
			{
				this.phase ++;
				this.wizard.respawn();
				createWalls();
				createBooks();
				
				TutorialObjectCreator.showMessage(
						"Teleport by double tapping either movement key", this.area);
			}
			// At the end of the second phase, ends the tutorial
			else
				this.navigator.startPhase("tutorialmenu", null);
		}
	}
	
	private void createBooks()
	{
		new Book(50, this.area);
		new Book(GameSettings.SCREENHEIGHT - 50, this.area);
		this.booksLeft += 2;
	}
	
	private void createWalls()
	{
		new Wall(50, 100, 100, 16, 
				MultiMediaHolder.getSprite("tutorial", "wall"), this.area);
		new Wall(50, GameSettings.SCREENHEIGHT - 100, 100, 16, 
				MultiMediaHolder.getSprite("tutorial", "wall"), this.area);
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	/**
	 * Book is a collectible object that is part of the tutorial
	 * 
	 * @author Mikko Hilpinen
	 * @since 9.5.2014
	 */
	public class Book extends CollidingDrawnObject
	{
		// ATTRIBUTES	--------------------------------------------------
		
		private SingleSpriteDrawer spriteDrawer;
		
		
		// CONSTRUCTOR	--------------------------------------------------
		
		/**
		 * Creates a new book to the given position on the y-axis.
		 * 
		 * @param y The y-coordinate of the book's origin
		 * @param area The area where the book will reside at
		 */
		public Book(int y, Area area)
		{
			super(50, y, DepthConstants.NORMAL, true, CollisionType.BOX, area);
			
			// Initializes attributes
			this.spriteDrawer = new SingleSpriteDrawer(
					MultiMediaHolder.getSprite("tutorial", "book"), 
					area.getActorHandler(), this);
			
			setBoxCollisionPrecision(1, 1);
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------

		@Override
		public void onCollision(ArrayList<Double> colpoints,
				Collidable collided, double steps)
		{
			if (!(collided instanceof Wizard))
				return;
			// When the book collides with a wizard it disappears and informs 
			// th etutorial
			kill();
			onBookCollected();
		}

		@Override
		public Class<?>[] getSupportedListenerClasses()
		{
			// No class collides with books
			return new Class<?>[0];
		}

		@Override
		public int getWidth()
		{
			if (this.spriteDrawer == null)
				return 0;
			else return this.spriteDrawer.getSprite().getWidth();
		}

		@Override
		public int getHeight()
		{
			if (this.spriteDrawer == null)
				return 0;
			else return this.spriteDrawer.getSprite().getHeight();
		}

		@Override
		public int getOriginX()
		{
			if (this.spriteDrawer == null)
				return 0;
			else return this.spriteDrawer.getSprite().getOriginX();
		}

		@Override
		public int getOriginY()
		{
			if (this.spriteDrawer == null)
				return 0;
			else return this.spriteDrawer.getSprite().getOriginY();
		}

		@Override
		public void drawSelfBasic(Graphics2D g2d)
		{
			// Draws the sprite
			if (this.spriteDrawer != null)
				this.spriteDrawer.drawSprite(g2d, 0, 0);
		}
	}
}
