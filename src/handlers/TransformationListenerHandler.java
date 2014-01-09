package handlers;

import listeners.TransformationListener;
import handleds.Handled;

/**
 * TransformationListenerHandler informs numerous transformationListeners about 
 * transformationEvents of certain objects.
 * 
 * @author Mikko Hilpinen.
 * Created 9.1.2014
 */
public class TransformationListenerHandler extends LogicalHandler 
	implements TransformationListener
{
	// ATTRIBUTES	------------------------------------------------------
	
	private TransformationEvent lastevent;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new tranformationListenerHandler with the given specs
	 * 
	 * @param autodeath Will the handler automatically die if all the listeners 
	 * in it are either killed or removed
	 * @param superhandler The TransformationListenerHandler that will inform 
	 * the handler about the transformation events (optional)
	 */
	public TransformationListenerHandler(boolean autodeath,
			TransformationListenerHandler superhandler)
	{
		super(autodeath, superhandler);
		
		// Initializes attributes
		this.lastevent = null;
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected Class<?> getSupportedClass()
	{
		return TransformationListener.class;
	}

	@Override
	protected boolean handleObject(Handled h)
	{
		TransformationListener l = (TransformationListener) h;
		
		// Informs the object about the last transformation event if need be
		if (l.isActive())
			l.onTransformationEvent(this.lastevent);
		
		return true;
	}

	@Override
	public void onTransformationEvent(TransformationEvent e)
	{
		// Informs all the handleds about the new event
		this.lastevent = e;
		handleObjects();
	}
}
