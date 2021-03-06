package arcane_arcade_field;

import java.awt.Graphics2D;

import utopia_gameobjects.DrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handleds.Actor;
import utopia_utility.DepthConstants;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import utopia_worlds.Room;
import arcane_arcade_main.GameSettings;
import arcane_arcade_worlds.Navigator;
import arcane_arcade_worlds.VictorySetting;

/**
 * ScoreKeeper keeps track of the game's score and handlers the continuity 
 * of the match
 *
 * @author Mikko Hilpinen.
 * @since 1.9.2013.
 */
public class ScoreKeeper extends DrawnObject implements RoomListener, Actor
{
	// ATTRIBUTES	------------------------------------------------------
	
	private SingleSpriteDrawer spritedrawer;
	private int scoreleft;
	private int scoreright;
	private int maxscore;

	private Server server;
	private WizardRelay wizardrelay;
	private Navigator navigator;
	
	private int respawntime;
	private double respawntimeleft;
	private boolean active;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new scorekeeper with 0 scores
	 *
	 * @param area The area where the object is placed to
	 * @param server The server that serves the ball after each score
	 * @param wizardrelay The wizards who need to be respawned after each 
	 * score
	 * @param navigator The navigator that will handle the transition to the 
	 * victory screen
	 * @param maxscore How many points are needed to win the game
	 */
	public ScoreKeeper(Area area, Server server, WizardRelay wizardrelay, 
			Navigator navigator, int maxscore)
	{
		super(GameSettings.SCREENWIDTH / 2, 20, DepthConstants.HUD, area);
		
		// Initializes attributes
		this.spritedrawer = new SingleSpriteDrawer(
				MultiMediaHolder.getSpriteBank("hud").getSprite("score"), null, this);
		this.navigator = navigator;
		this.scoreleft = 0;
		this.scoreright = 0;
		this.maxscore = maxscore;
		this.wizardrelay = wizardrelay;
		this.server = server;
		this.respawntime = 100;
		this.respawntimeleft = 0;
		this.active = false;
		
		// And to the actorhandler
		if (area.getActorHandler() != null)
			area.getActorHandler().addActor(this);
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
		g2d.setFont(GameSettings.BASICFONT);
		g2d.setColor(GameSettings.WHITETEXTCOLOR);
		g2d.drawString(this.scoreleft + "", 35, 40);
		g2d.drawString(this.scoreright + "", 130, 40);
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
	public void act(double steps)
	{
		// Checks the respawn timer
		if (this.respawntimeleft > 0)
		{
			this.respawntimeleft -= steps;
			
			// Respawns the wizards and serves the ball
			if (this.respawntimeleft <= 0)
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
		if (this.scoreleft >= this.maxscore || this.scoreright >= this.maxscore)
		{
			VictorySetting setting = new VictorySetting(this.scoreleft, 
					this.scoreright);
			this.navigator.startPhase("victoryscreen", setting);
		}
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
