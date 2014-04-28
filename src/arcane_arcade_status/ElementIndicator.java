package arcane_arcade_status;

import java.awt.geom.Point2D.Double;

import utopia_interfaceElements.AbstractButton;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;



/**
 * ElementIndicators are used to represent the possible elements to choose 
 * from
 *
 * @author Mikko Hilpinen.
 * @since 1.12.2013.
 */
public class ElementIndicator extends AbstractButton
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private Element element;
	private ElementIndicatorListener listener;
	
	// TODO: Add mouse listening features or create a new class that works with 
	// mouse
	/*
	 * - IndicatorListener interface
	 * - Mouse enter / exit scaling
	 * - Mouse left pressed listening
	 */
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new ElementIndicator to the given position to represent the 
	 * given element
	 *
	 * @param x The x-coordinate of the indicator
	 * @param y The y-coordinate of the indicator
	 * @param depth The drawing depth of the indicator
	 * @param element The element the indicator shows
	 * @param listener The elementIndicatorListener that reacts when the 
	 * indicator is clicked
	 * @param area the area where the object will be placed to
	 */
	public ElementIndicator(int x, int y, int depth, Element element, 
			ElementIndicatorListener listener, Area area)
	{
		super(x, y, depth, MultiMediaHolder.getSpriteBank(
				"hud").getSprite("elements"), area);
		
		// Initializes attributes
		this.listener = listener;
		this.element = element;
		getSpriteDrawer().setImageSpeed(0);
		getSpriteDrawer().setImageIndex(this.element.getElementIconIndex());
		
		setScale(1.25, 1.25);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	public void onMouseButtonEvent(MouseButton button,
			MouseButtonEventType eventType, Double mousePosition,
			double eventStepTime)
	{
		// On left / right mouse pressed, informs the listener
		if (eventType == MouseButtonEventType.PRESSED)
			this.listener.onElementIndicatorPressed(button, this);
	}

	@Override
	public boolean listensMouseEnterExit()
	{
		return true;
	}

	@Override
	public void onMousePositionEvent(MousePositionEventType eventType,
			Double mousePosition, double eventStepTime)
	{
		// Adds a scaling effect
		if (eventType == MousePositionEventType.ENTER)
			setScale(1.4, 1.4);
		else if (eventType == MousePositionEventType.EXIT)
			setScale(1.25, 1.25);
	}
	
	
	// GETTERS & SETTERS	---------------------------------------------
	
	/**
	 * @return The element the indicator represents
	 */
	public Element getElement()
	{
		return this.element;
	}
	
	/**
	 * Changes the element the indicator represents
	 *
	 * @param element The new element shown
	 */
	public void setElement(Element element)
	{
		this.element = element;
		
		getSpriteDrawer().setImageIndex(this.element.getElementIconIndex());
	}
}