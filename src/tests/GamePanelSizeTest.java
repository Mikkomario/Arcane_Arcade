package tests;

import gameobjects.DrawnObject;
import handlers.DrawableHandler;

import java.awt.Graphics2D;

import arcane_arcade_main.GameSettings;


/**
 * Gamepanelsizetest draws a box with certain dimensions to the screen
 *
 * @author Gandalf.
 *         Created 26.8.2013.
 */
public class GamePanelSizeTest extends DrawnObject
{
	// CONSTRUCTOR	----------------------------------------------------
	
	/**
	 * Creates a new gamepanelsizetest
	 * @param drawer The drawer that will draw the object
	 */
	public GamePanelSizeTest(DrawableHandler drawer)
	{
		super(0, 0, 0, drawer);
	}
	
	
	// IMPLEMENTED METHODS	--------------------------------------------

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
		g2d.drawRect(5, 5, GameSettings.SCREENWIDTH - 30, GameSettings.SCREENHEIGHT - 30);
	}
}
