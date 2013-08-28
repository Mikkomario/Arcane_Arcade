package listeners;

import graphic.SpriteDrawer;
import handleds.LogicalHandled;

/**
 * Animationlistener is informed when an animation cycle ends
 *
 * @author Mikko Hilpinen.
 *         Created 28.8.2013.
 */
public interface AnimationListener extends LogicalHandled
{
	/**
	 * This method is called when an animation of the sprite ends or, more 
	 * precisely, a cycle in the animation ends.
	 *
	 * @param spritedrawer The spritedrawer that draws the sprite who's 
	 * animation just completed a cycle. 
	 */
	public void onAnimationEnd(SpriteDrawer spritedrawer);
}
