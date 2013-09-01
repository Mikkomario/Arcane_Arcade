package arcane_arcade_field;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import worlds.Room;

import listeners.RoomListener;

import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.Navigator;

import graphic.SpriteDrawer;
import handleds.Actor;
import handlers.ActorHandler;
import handlers.DrawableHandler;
import helpAndEnums.DepthConstants;
import drawnobjects.DrawnObject;

/**
 * Scorekeeper keeps track of the game's score and handlers the continuity 
 * of the match
 *
 * @author Mikko Hilpinen.
 *         Created 1.9.2013.
 */
public class ScoreKeeper extends DrawnObject implements RoomListener, Actor
{
	// ATTRIBUTES	------------------------------------------------------
	
	private SpriteDrawer spritedrawer;
	private int scoreleft;
	private int scoreright;
	private int maxscore;
	private Font scorefont;
	private Color scorecolor;
	private Server server;
	private WizardRelay wizardrelay;
	private int respawntime;
	private int respawntimeleft;
	private boolean active;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new scorekeeper with 0 scores
	 *
	 * @param drawer The drawablehandler that will draw the scorekeeper
	 * @param actorhandler The actorhandler that will keep the scorehandler 
	 * updated about the steps
	 * @param room The room where the scorekeeper is placed at
	 * @param server The server that serves the ball after each score
	 * @param wizardrelay The wizards who need to be respawned after each 
	 * score
	 */
	public ScoreKeeper(DrawableHandler drawer, ActorHandler actorhandler, 
			Room room, Server server, WizardRelay wizardrelay)
	{
		super(GameSettings.SCREENWIDTH / 2, 20, DepthConstants.HUD, drawer);
		
		// Initializes attributes
		this.spritedrawer = new SpriteDrawer(
				Navigator.getSpriteBank("hud").getSprite("score"), null);
		this.scoreleft = 0;
		this.scoreright = 0;
		this.maxscore = 25;
		this.wizardrelay = wizardrelay;
		this.server = server;
		this.respawntime = 100;
		this.respawntimeleft = 0;
		this.scorefont = new Font("Old English Text MT", Font.BOLD, 40);
		this.scorecolor = new Color(235, 232, 168);
		this.active = false;
		
		// Adds the object to the room (if possible)
		if (room != null)
			room.addOnject(this);
		// And to the actorhandler
		if (actorhandler != null)
			actorhandler.addActor(this);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

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
		if (this.spritedrawer == null)
			return;
		
		// Draws the sprite
		this.spritedrawer.drawSprite(g2d, 0, 0);
		// Also draws the score
		g2d.setFont(this.scorefont);
		g2d.setColor(this.scorecolor);
		g2d.drawString(this.scoreleft + "", 35, 40);
		g2d.drawString(this.scoreright + "", 130, 40);
	}
	
	@Override
	public void kill()
	{
		// Also kills the spritedrawer
		this.spritedrawer.kill();
		this.spritedrawer = null;
		
		super.kill();
	}

	@Override
	public void onRoomStart(Room room)
	{
		// Doesn't do anything
	}

	@Override
	public void onRoomEnd(Room room)
	{
		// Dies
		kill();
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
	public void act()
	{
		// Checks the respawn timer
		if (this.respawntimeleft > 0)
		{
			this.respawntimeleft --;
			
			// Respawns the wizards and serves the ball
			if (this.respawntimeleft == 0)
			{
				this.server.serve();
				this.wizardrelay.respawnWizards();
				inactivate();
			}
		}
	}
	
	
	// OHTER MEHTODS	-------------------------------------------------
	
	/**
	 * Increases a team's score and respawns the wizards & the ball after a 
	 * delay
	 *
	 * @param side The side who gained a point
	 */
	public void score(ScreenSide side)
	{
		// Increases the score
		if (side == ScreenSide.LEFT)
			this.scoreleft ++;
		else
			this.scoreright ++;
		
		// If the game is on, Starts respawning
		if (this.scoreleft < this.maxscore && this.scoreright < this.maxscore)
		{
			this.respawntimeleft = this.respawntime;
			activate();
		}
		
		// If the game was won, goes to the victory screen
		// TODO: Add victory screen
	}
	
	
	/*TODO: Try this at some point
	 * public static void main(String[] a) {
    GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
    String[] fontnames = e.getAvailableFontFamilyNames();
    for (String s : fontnames) {
      System.out.println(s);
    }
  }
	 * 
	 */
}