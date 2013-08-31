package handlers;

import java.util.Iterator;

import handleds.Handled;
import listeners.CameraListener;

/**
 * This class informs all its sublisteners about changes in camera's position
 *
 * @author Mikko Hilpinen.
 *         Created 7.12.2012.
 */
public class CameraListenerHandler extends LogicalHandler implements CameraListener
{	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new listenerhandler that will inform all sublisteners about
	 * camera's changes. Listeners must be added manually later.
	 * 
	 * @param autodeath Will the handler stop functioning when it runs out of 
	 * handled listeners
	 * @param superhandler The cameralistenerhandler that informs this handler (optional)
	 */
	public CameraListenerHandler(boolean autodeath, CameraListenerHandler superhandler)
	{
		super(autodeath, superhandler);
	}
	
	
	// IMPLEMENTED METHODS	----------------------------------------------

	@Override
	public void informCameraPosition(int posx, int posy, int w, int h, int angle)
	{
		// Cleans the unnecessary handleds
		removeDeadHandleds();
		
		// Informs all sublisteners about the change
		Iterator<Handled> iterator = getIterator();
		
		while (iterator.hasNext())
		{
			CameraListener l = (CameraListener) iterator.next();
			l.informCameraPosition(posx, posy, w, h, angle);
		}	
	}
	
	@Override
	protected Class<?> getSupportedClass()
	{
		return CameraListener.class;
	}
	
	
	// OTHER METHODS	---------------------------------------------------
	
	/**
	 * Adds a new cameralistener to the informed cameralisteners
	 * @param c The listener to be addded
	 */
	public void addListener(CameraListener c)
	{
		super.addHandled(c);
	}
}
