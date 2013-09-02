package arcane_arcade_menus;

import java.awt.Graphics2D;
import java.awt.Point;

import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.Navigator;

import worlds.Room;

import listeners.AdvancedMouseListener;
import listeners.RoomListener;

import graphic.MaskChecker;
import graphic.SpriteDrawer;
import handlers.DrawableHandler;
import handlers.MouseListenerHandler;
import helpAndEnums.DepthConstants;
import drawnobjects.DrawnObject;

/**
 * By clicking the quitmenucorner the user quits the game
 *
 * @author Mikko Hilpinen.
 *         Created 2.9.2013.
 */
public class QuitMenuCorner extends DrawnObject implements 
		AdvancedMouseListener, RoomListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private SpriteDrawer spritedrawer;
	private MaskChecker maskchecker;
	private boolean active;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new quitmenucorner to the top right corner of the screen.
	 *
	 * @param drawer The drawer that will draw the menu corner
	 * @param mousehandler The mouselistenerhandler that will inform the 
	 * corner about mouse events
	 * @param room The room where the corner is created at
	 */
	public QuitMenuCorner(DrawableHandler drawer, 
			MouseListenerHandler mousehandler, Room room)
	{
		super(GameSettings.SCREENWIDTH, 0, DepthConstants.BACK, drawer);
		
		// Initializes attributes
		this.spritedrawer = new SpriteDrawer(Navigator.getSpriteBank(
				"menu").getSprite("quit"), null);
		this.spritedrawer.inactivate();
		this.maskchecker = new MaskChecker(Navigator.getSpriteBank(
				"menu").getSprite("quitmask"));
		this.active = true;
		
		// Adds the object to the handlers
		if (mousehandler != null)
			mousehandler.addMouseListener(this);
		if (room != null)
			room.addOnject(this);
	}
	
	
	// IMPLEMENTENTED METHODS	------------------------------------------

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
		if (this.spritedrawer != null)
			this.spritedrawer.drawSprite(g2d, 0, 0);
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

	@Override
	public void onLeftDown(int mouseX, int mouseY)
	{
		// Does nothing
	}

	@Override
	public void onRightDown(int mouseX, int mouseY)
	{
		// Does nothing
	}

	@Override
	public void onLeftPressed(int mouseX, int mouseY)
	{
		// Quits the game
		System.exit(0);
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
		// If currently has no mask, doesn't listen to the mouse
		if (this.maskchecker == null)
			return false;
		// Checks mask collision
		Point relpoint = negateTransformations(x, y);
		return this.maskchecker.maskContainsRelativePoint(relpoint, 0);
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		return true;
	}

	@Override
	public void onMouseEnter(int mouseX, int mouseY)
	{
		// Changes sprite index
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
		// Changes sprite index
		this.spritedrawer.setImageIndex(0);
	}

	@Override
	public void onMouseMove(int mouseX, int mouseY)
	{
		// Does nothing
	}

	@Override
	public void onRoomStart(Room room)
	{
		// Does nothing
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Dies
		kill();
	}
	
	@Override
	public void kill()
	{
		// Kills the spritedrawer and maskchecker
		this.spritedrawer.kill();
		this.spritedrawer = null;
		this.maskchecker = null;
		super.kill();
	}
}
