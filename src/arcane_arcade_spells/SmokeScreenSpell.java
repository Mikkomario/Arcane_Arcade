package arcane_arcade_spells;

import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import worlds.Room;
import arcane_arcade_field.BallRelay;
import arcane_arcade_field.Wizard;
import arcane_arcade_main.GameSettings;
import arcane_arcade_spelleffects.SmokeEffectCreator;

/**
 * SmokeScreenSpell creates a cloud of smoke, which will last for a while.
 * 
 * @author Unto Solala
 * 			Created 29.8.2013
 */
public class SmokeScreenSpell extends Spell{

	/**
	 * Creates a new SmokeScreen-spell
	 */
	public SmokeScreenSpell()
	{
		super(CASTDELAY_VERY_LONG);
	}

	@Override
	protected void createEffects(Wizard caster, BallRelay ballrelay,
			DrawableHandler drawer, ActorHandler actorhandler,
			CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, Room room) 
	{
		new SmokeEffectCreator(400, actorhandler, room,
				(int)(GameSettings.SCREENWIDTH * 0.7), (int)caster.getY(), 
				drawer, collidablehandler, collisionhandler);
	}
}