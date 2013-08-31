package arcane_arcade_field;

import java.util.ArrayList;
import java.util.Iterator;

import worlds.Room;
import listeners.RoomListener;
import handleds.Handled;
import handlers.Handler;

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
		// Removes dead balls
		removeDeadHandleds();
		
		// Forms a table containing all wizards that reside on the given side
		ArrayList<Wizard> wizards = new ArrayList<Wizard>();
		
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			Wizard w = (Wizard) iterator.next();
			if (w.getScreenSide() == side)
				wizards.add(w);
		}
		
		return wizards;
	}
}