package arcane_arcade_field;

import arcane_arcade_spelleffects.SpellEffect;
import utopia_handleds.Collidable;

/**
 * SpellTargets are objects that react to certain spell collisions. These 
 * classes will be (automatically) informed when a spell collides with them.
 * 
 * @author Mikko Hilpinen
 * @since 17.5.2014
 */
public interface SpellTarget extends Collidable
{
	/**
	 * This method is called when a spellEffect collides with a spellTarget.
	 * 
	 * @param collidedEffect The spellEffect instance that collided with the 
	 * target
	 * @param steps How long the collision has taken place since the last event
	 */
	public void onSpellCollision(SpellEffect collidedEffect, double steps);
}
