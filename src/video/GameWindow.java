package video;

import handleds.Actor;
import handlers.ActorHandler;
import handlers.KeyListenerHandler;
import handlers.MainKeyListenerHandler;
import handlers.MainMouseListenerHandler;
import handlers.MouseListenerHandler;
import handlers.StepHandler;

import java.awt.BorderLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import listeners.AdvancedKeyListener;
import listeners.AdvancedMouseListener;


/**
 * GameWindow is the main frame of the program in which all the drawing is done. 
 * The window should hold at least one gamepanel.
 * 
 * @author Unto Solala & Mikko Hilpinen. Created 8.8.2013
 * @see video.GamePanel
 */
public class GameWindow extends JFrame
{	
	// ATTRIBUTES ---------------------------------------------------------
	
	private int width;
	private int height;
	
	private MainKeyListenerHandler mainkeyhandler;
	private MainMouseListenerHandler mainmousehandler;
	private StepHandler stephandler;
	private KeyListenerHandler keylistenerhandler;
	private MouseListenerHandler mouselistenerhandler;
	private ActorHandler listeneractorhandler;
	
	private ArrayList<GamePanel> panels;
	
	
	// CONSTRUCTOR ---------------------------------------------------------
	
	/**
	 * Creates a new window frame with given width and height.
	 * 
	 * @param width	Window's width (in pixels).
	 * @param height Window's height (in pixels).
	 * @param title The title shown in the window's border
	 */
	public GameWindow(int width, int height, String title)
	{
		// Initializes attributes
		this.width = width;
		this.height = height;
		this.panels = new ArrayList<GamePanel>();
		
		this.setTitle(title);
		
		//Let's format our window
		this.formatWindow();
		//And make it visible
		this.setVisible(true);
		
		// Adds listener(s) to the window
		addMouseListener(new BasicMouseListener());
		addKeyListener(new BasicKeyListener());
		
		// Creates and initializes important handlers
		this.stephandler = new StepHandler(15, this);
		
		this.listeneractorhandler = new ActorHandler(false, this.stephandler);
		this.mainkeyhandler = new MainKeyListenerHandler(this.listeneractorhandler);
		this.mainmousehandler = new MainMouseListenerHandler(this.listeneractorhandler);
		
		this.keylistenerhandler = new KeyListenerHandler(false, null);
		this.mouselistenerhandler = new MouseListenerHandler(false, 
				this.listeneractorhandler, null);
		
		this.mainkeyhandler.addListener(this.keylistenerhandler);
		this.mainmousehandler.addMouseListener(this.mouselistenerhandler);
		
		// Starts the game
		new Thread(this.stephandler).start();
	}
	
	
	// OTHER METHODS	 ---------------------------------------------------
	
	private void formatWindow()
	{
		//Let's set our window's layout
		this.setLayout(new BorderLayout());
		//Let's make sure our window closes properly
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Let's set our window's size
		this.setSize(this.width, this.height);
	}
	
	
	/**
	 * Adds a new GamePanel to the given direction.
	 * 
	 * @param newPanel	The GamePanel you want to add to the window.
	 * @param direction	The direction where you want to place the panel. (For
	 * example Borderlayout.NORTH)
	 * @see BorderLayout
	 */
	public void addGamePanel(GamePanel newPanel, String direction)
	{
		// Checks the arguments
		if (newPanel == null || direction == null)
			return;
		
		this.add(newPanel, direction);
		this.panels.add(newPanel);
	}
	
	/**
	 * Removes a gamepanel from the window
	 *
	 * @param p The panel to be removed
	 * @param killContent Should the objects drawn in the panel be killed
	 */
	public void removePanel(GamePanel p, boolean killContent)
	{
		if (!this.panels.contains(p))
			return;
		remove(p);
		this.panels.remove(p);
		
		// Kills the content of the panel if needed
		if (killContent)
			p.getDrawer().kill();
	}
	
	/**
	 * Updates mouse's position in the game
	 */
	public void callMousePositionUpdate()
	{
		Point mousePosition = MouseInfo.getPointerInfo().getLocation();
		this.mainmousehandler.setMousePosition(mousePosition.x, mousePosition.y);
	}
	
	/**
	 * This method should be called when the screen needs redrawing
	 */
	public void callScreenUpdate()
	{
		// Updates all the panels in the window
		for (int i = 0; i < this.panels.size(); i++)
		{
			this.panels.get(i).callScreenUpdate();
		}
		// Redraws the screen
		repaint();
	}
	
	/**
	 * Adds a keylistener to the informed listeners
	 *
	 * @param k The keylistener that will be informed
	 */
	public void addKeyListener(AdvancedKeyListener k)
	{
		this.keylistenerhandler.addKeyListener(k);
	}
	
	/**
	 * Adds a new mouselistener to the informed listeners
	 *
	 * @param m The mouselistener that will be informed
	 */
	public void addMouseListener(AdvancedMouseListener m)
	{
		this.mouselistenerhandler.addMouseListener(m);
	}
	
	/**
	 * Adds a new actor to the informed actors
	 *
	 * @param a The actor that will be informed about steps
	 */
	public void addActor(Actor a)
	{
		this.stephandler.addActor(a);
	}
	
	
	// SUBCLASSES	----------------------------------------------------
	
	/**
	 * Main window's helper class, which listens to what the mouse does.
	 * 
	 * @author Unto Solala. Created 8.8.2013
	 */
	private class BasicMouseListener implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			// Not needed
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			// Not needed
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			// Not needed
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			GameWindow.this.mainmousehandler.setMouseStatus(e.getX(), e.getY(), 
					true, e.getButton());
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			GameWindow.this.mainmousehandler.setMouseStatus(e.getX(), e.getY(),
					false, e.getButton());
		}
	}
	
	/**
	 * Main window's helper class, which listens to what the keyboard does.
	 * 
	 * @author Unto Solala. Created 8.8.2013
	 */
	private class BasicKeyListener implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent ke)
		{
			GameWindow.this.mainkeyhandler.onKeyPressed(ke.getKeyChar(), 
					ke.getKeyCode(), ke.getKeyChar() == KeyEvent.CHAR_UNDEFINED);
		}

		@Override
		public void keyReleased(KeyEvent ke)
		{
			GameWindow.this.mainkeyhandler.onKeyReleased(ke.getKeyChar(), 
					ke.getKeyCode(), ke.getKeyChar() == KeyEvent.CHAR_UNDEFINED);
		}

		@Override
		public void keyTyped(KeyEvent arg0)
		{
			// Not needed
		}
	}
}
