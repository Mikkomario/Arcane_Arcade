package arcane_arcade_spelleffects;

import worlds.Room;
import graphic.MaskChecker;
import handleds.Collidable;
import handlers.ActorHandler;
import handlers.CollidableHandler;
import handlers.CollisionHandler;
import handlers.DrawableHandler;
import helpAndEnums.CollisionType;
import arcane_arcade_main.Main;
import arcane_arcade_status.Element;

/**
 * MaskedSpellEffect works like any other spell effect except it uses a mask 
 * for collision checking. SpellEffects that want use masks should extend this 
 * class.
 *
 * @author Mikko Hilpinen.
 *         Created 28.8.2013.
 */
public abstract class MaskedSpellEffect extends SpellEffect
{
	// ATTRIBUTES	------------------------------------------------------
	
	private MaskChecker maskchecker;
	private boolean maskanimated;
	
	
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new maskedspelleffect to the given position with the given 
	 * information added to the given handlers.
	 *
	 * @param x The effect's x-coordinate
	 * @param y The effect's y-coordinate
	 * @param depth The effect's drawing depth
	 * @param collisiontype The effect's collision type (= spell's shape)
	 * @param drawer The drawablehandler that will draw the effect
	 * @param collidablehandler The collidablehandler that will handle the 
	 * effect's collision checking
	 * @param collisionhandler The collisionhandler that will inform the effect 
	 * about collisions (if the spell collides with other spells, otherwise null)
	 * @param actorhandler The actorhandler that will call the effect's act-event
	 * @param room The room where the spelleffect was created
	 * @param spritename The effect's sprite's name in the spritebank "spells"
	 * @param maskname The effect's mask's name in the spritebank "spells"
	 * @param collidesWithSpells Does the effect react to collisions with other 
	 * spells
	 * @param collidesWithBalls Does the effect react to collisions with balls
	 * @param collidesWithWizards Does the effect react to collisions with 
	 * wizards
	 * @param element1 The effect's primary element (NOELEMENT if the spell has 
	 * no elements)
	 * @param element2 The effect's secondary element (NOELEMENT if the spell 
	 * doesn't have two elements)
	 * @param lifetime How many steps the effect will live (a negative number 
	 * makes the effect live until killed)
	 * @param isMaskAnimated Is collision checking done with an animated mask 
	 * (true) or a mask with no animation (false)
	 */
	public MaskedSpellEffect(int x, int y, int depth,
			CollisionType collisiontype, DrawableHandler drawer,
			CollidableHandler collidablehandler,
			CollisionHandler collisionhandler, ActorHandler actorhandler, 
			Room room, String spritename, String maskname, 
			boolean collidesWithSpells, boolean collidesWithBalls, 
			boolean collidesWithWizards, Element element1, Element element2, 
			int lifetime, boolean isMaskAnimated)
	{
		super(x, y, depth, collisiontype, drawer, collidablehandler, collisionhandler,
				actorhandler, room, spritename, collidesWithSpells, collidesWithBalls,
				collidesWithWizards, element1, element2, lifetime);
		// Initializes attributes
		this.maskanimated = isMaskAnimated;
		this.maskchecker = new MaskChecker(
				Main.spritebanks.getOpenSpriteBank("spells").getSprite(maskname));
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public Collidable pointCollides(int x, int y)
	{	
		// Point only collides if it also collides the mask
		Collidable collided = super.pointCollides(x, y);
		
		if (collided == null)
			return null;
		
		// Calculates the imageindex used in the collision check
		int maskindex = 0;
		if (this.maskanimated)
			maskindex = getSpriteDrawer().getImageIndex();
		
		if (this.maskchecker.maskContainsRelativePoint(
				negateTransformations(x, y), maskindex))
			return collided;
		else
			return null;
	}
}
