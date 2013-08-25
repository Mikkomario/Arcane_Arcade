package worlds;

import handleds.Drawable;
import handleds.Handled;
import handleds.LogicalHandled;
import handlers.Handler;
import handlers.RoomListenerHandler;

import java.util.ArrayList;

import listeners.RoomListener;

import common.GameObject;

import backgrounds.Background;

/**
 * Room represents a single restricted area in a game. A room contains a 
 * background and a group of objects. A room can start 
 * and end and it will inform objects about such events.<p>
 * 
 * A room has to be initialized before it is used but can also be uninitialized 
 * to save memory when it is not needed.
 *
 * @author Mikko Hilpinen.
 *         Created 11.7.2013.
 */
public class Room extends Handler
{	
	// ATTRIBUTES	-----------------------------------------------------
	
	private ArrayList<Background> backgrounds;
	private RoomListenerHandler listenerhandler;
	private boolean active;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new room, filled with backgrounds, and objects. 
	 * The room will remain inactive until started.
	 *
	 * @param backgrounds The background(s) used in the room. Use empty list or 
	 * null if no backgrounds will be used.
	 * @see #end()
	 * @see #start()
	 */
	public Room(ArrayList<Background> backgrounds)
	{
		// Rooms aren't handled by anything by default
		super(false, null);
		
		// Initializes attributes
		this.backgrounds = backgrounds;
		this.active = true;
		this.listenerhandler = new RoomListenerHandler(false, null);
		
		// Uninitializes the room
		uninitialize();
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return GameObject.class;
	}
	
	@Override
	public void killWithoutKillingHandleds()
	{
		// In addition to the normal killing process, kills the 
		// backgrounds as well
		if (this.backgrounds != null)
		{
			for (int i = 0; i < this.backgrounds.size(); i++)
				this.backgrounds.get(i).kill();
			
			this.backgrounds.clear();
			this.backgrounds = null;
		}
		
		super.killWithoutKillingHandleds();
	}
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * Changes the backgrounds shown in the room
	 *
	 * @param backgrounds The new background(s) shown in the room (null if 
	 * no background is used in the room)
	 */
	public void setBackgrounds(ArrayList<Background> backgrounds)
	{
		this.backgrounds = backgrounds;
	}
	
	/**
	 * @return The current background(s) shown in the room
	 */
	public ArrayList<Background> getBackgrounds()
	{
		return this.backgrounds;
	}
	
	/**
	 * @return Is the room currently in use.
	 */
	public boolean isActive()
	{
		return this.active && !isDead();
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Adds a new object to the room. If the object is an (active) roomlistener, 
	 * it will be automatically informed about the events in the room.
	 *
	 * @param g The object to be added
	 */
	public void addOnject(GameObject g)
	{
		addHandled(g);
		// If the object is a roomlistener, adds it to the listenerhandler as well
		if (g instanceof RoomListener)
			this.listenerhandler.addRoomListener((RoomListener) g);
	}
	
	/**
	 * Removes a gameobject from the room
	 *
	 * @param g The gameobject to be removed
	 */
	public void removeObject(GameObject g)
	{
		removeHandled(g);
		// If the object was a roomlistener, removes it from there as well
		if (g instanceof RoomListener)
			this.listenerhandler.removeHandled(g);
	}
	
	/**
	 * Starts the room, activating all the objects and backgrounds in it
	 * 
	 * @see #end()
	 */
	public void start()
	{
		// If the room had already been started, nothing happens
		if (this.active)
			return;
		
		this.active = true;
		initialize();
		
		// Informs the listeners about the event
		this.listenerhandler.onRoomStart(this);
	}
	
	/**
	 * Ends the room, deactivating all the objects and backgrounds in it
	 * 
	 * @see #start()
	 */
	public void end()
	{
		// If the room had already been ended, nothing happens
		if (!this.active)
			return;
		
		// Informs the listeners about the event
		this.listenerhandler.onRoomEnd(this);
		
		this.active = false;
		uninitialize();
	}
	
	/**
	 * Here the room (re)initializes all its content
	 * 
	 * @see #uninitialize()
	 */
	protected void initialize()
	{
		// Sets the backgrounds visible and animated
		if (this.backgrounds != null)
		{
			for (int i = 0; i < this.backgrounds.size(); i++)
			{
				Background b = this.backgrounds.get(i);
				b.setVisible();
				b.getSpriteDrawer().activate();
			}
		}
		// Activates all the objects and sets them visible (if applicable)
		for (int i = 0; i < getHandledNumber(); i++)
		{
			Handled h = getHandled(i);
			
			if (h instanceof Drawable)
				((Drawable) h).setVisible();
			if (h instanceof LogicalHandled)
				((LogicalHandled) h).activate();
		}
	}
	
	/**
	 * Here the room uninitializes its content
	 * 
	 * @see #initialize()
	 */
	protected void uninitialize()
	{
		// Sets the backgrounds invisible and unanimated
		if (this.backgrounds != null)
		{
			for (int i = 0; i < this.backgrounds.size(); i++)
			{
				Background b = this.backgrounds.get(i);
				b.setInvisible();
				
				if (b.getSpriteDrawer() != null)
					b.getSpriteDrawer().inactivate();
			}
		}
		// InActivates all the objects and sets them invisible (if applicable)
		for (int i = 0; i < getHandledNumber(); i++)
		{
			Handled h = getHandled(i);
			
			if (h instanceof Drawable)
				((Drawable) h).setInvisible();
			if (h instanceof LogicalHandled)
				((LogicalHandled) h).inactivate();
		}
	}
}
