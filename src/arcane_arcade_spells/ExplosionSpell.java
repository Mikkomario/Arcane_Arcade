package arcane_arcade_spells;

import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import worlds.Room;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.ScreenSide;
import arcane_arcade_field.Wizard;
import arcane_arcade_main.SoundEffectPlayer;
import arcane_arcade_spelleffects.ExplosionEffect;

/**
 * Explosionspell creates an explosion that will last a while and then 
 * disappear.
 *
 * @author Mikko Hilpinen.
 *         Created 28.8.2013.
 */
public class ExplosionSpell extends Spell
{
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates a new explosionspell
	 */
	public ExplosionSpell()
	{
		super(CASTDELAY_LONG, MPUSE_SEMI_HIGH);
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------

	@Override
	protected void createEffects(Wizard caster, BallRelay ballrelay,
			DrawableHandler drawer, ActorHandler actorhandler,
			CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, Room room)
	{
		 int x = (int) caster.getX() + 120;
		 if (caster.getScreenSide() == ScreenSide.RIGHT)
			 x -= 240;
		
		new ExplosionEffect(x, (int) caster.getY(), 
				drawer, collidablehandler, actorhandler, room); 
		
		// Also plays an explosion sound effect
		SoundEffectPlayer.playSoundEffect("explosion");
	}
}
