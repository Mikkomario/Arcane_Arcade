package arcane_arcade_field;

import java.util.ArrayList;

import utopia_handleds.Handled;
import utopia_handlers.Handler;
import utopia_listeners.RoomListener;
import utopia_worlds.Room;


/**
 * WizardRelay holds information about the wizards on the field and provides 
 * that information to objects when necessary.
 *
 * @author Mikko Hilpinen.
 *         Created 31.8.2013.
 */
public class WizardRelay extends Handler implements RoomListener
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new empty wizardRelay. New wizards should be added with the 
	 * addWizard() method.
	 * 
	 * @param room The room in which the wizards are created
	 */
	public WizardRelay(Room room)
	{
		super(false, null);
		
		// Adds the listener to the handler (if possible)
		if (room != null)
			room.addRoomListener(this);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected Class<?> getSupportedClass()
	{
		return Wizard.class;
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
	protected boolean handleObject(Handled h)
	{
		// Respawns the wizards
		((Wizard) h).respawn();
		
		return true;
	}
	
	
	// OTHER METHODS	-------------------------------------------------
	
	/**
	 * Adds a new wizard to the list of wizards the relay has information about
	 *
	 * @param w The new wizard
	 */
	public void addWizard(Wizard w)
	{
		addHandled(w);
	}
	
	/**
	 * Returns all wizards from a single side of the screen
	 *
	 * @param side The side of the screen where the wizards are looked for
	 * @return The wizards from the given side of the screen or an empty list 
	 * if there were no wizards
	 */
	public ArrayList<Wizard> getWizardsFromSide(ScreenSide side)
	{
		// Uses an operator for fetching the wizards
		SideWizardCollectorOperator operator = new SideWizardCollectorOperator(side);
		handleObjects(operator);
		
		// Returns the found wizards
		return operator.getFoundWizards();
	}
	
	/**
	 * Makes all the wizards respawn
	 */
	public void respawnWizards()
	{
		// Respawn is done in the default handling operation
		handleObjects();
	}
	
	
	// SUBCLASSES	------------------------------------------------------
	
	private class SideWizardCollectorOperator extends HandlingOperator
	{
		// ATTRIBUTES	--------------------------------------------------
		
		private ArrayList<Wizard> foundWizards;
		private ScreenSide searchSide;
		
		
		// CONSTRUCTOR	--------------------------------------------------
		
		public SideWizardCollectorOperator(ScreenSide searchSide)
		{
			// Initializes attributes
			this.foundWizards = new ArrayList<Wizard>();
			this.searchSide = searchSide;
		}
		
		
		// IMPLEMENTED METHODS	-----------------------------------------
		
		@Override
		protected boolean handleObject(Handled h)
		{
			// Search for wizards with a certain screenSide
			Wizard w = (Wizard) h;
			if (w.getScreenSide() == this.searchSide)
				this.foundWizards.add(w);
			
			return true;
		}
		
		
		// OTHER METHODS	---------------------------------------------
		
		public ArrayList<Wizard> getFoundWizards()
		{
			return this.foundWizards;
		}
	}
}
