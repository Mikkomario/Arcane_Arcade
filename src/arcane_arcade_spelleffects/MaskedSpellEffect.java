package arcane_arcade_spelleffects;

import java.awt.geom.Point2D;

import utopia_graphic.MaskChecker;
import utopia_helpAndEnums.CollisionType;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_worlds.Area;
import arcane_arcade_status.Element;

/**
 * MaskedSpellEffect works like any other spell effect except it uses a mask 
 * for collision checking. SpellEffects that want use masks should extend this 
 * class.
 *
 * @author Mikko Hilpinen.
 * @since 28.8.2013.
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
	 * @param area The area where the object is placed to
	 */
	public MaskedSpellEffect(int x, int y, int depth,
			CollisionType collisiontype, String spritename, String maskname, 
			boolean collidesWithSpells, boolean collidesWithBalls, 
			boolean collidesWithWizards, Element element1, Element element2, 
			int lifetime, boolean isMaskAnimated, Area area)
	{
		super(x, y, depth, collisiontype, spritename, collidesWithSpells, collidesWithBalls,
				collidesWithWizards, element1, element2, lifetime, area);
		// Initializes attributes
		this.maskanimated = isMaskAnimated;
		this.maskchecker = new MaskChecker(
				MultiMediaHolder.getSpriteBank("spells").getSprite(maskname));
	}
	
	
	// IMPLEMENTED METHODS	---------------------------------------------
	
	@Override
	public boolean pointCollides(Point2D testPosition)
	{	
		// Point only collides if it also collides the mask
		if (!super.pointCollides(testPosition))
			return false;
		
		// Calculates the imageindex used in the collision check
		int maskindex = 0;
		if (this.maskanimated)
			maskindex = getSpriteDrawer().getImageIndex();
		
		return (this.maskchecker.maskContainsRelativePoint(
				negateTransformations(testPosition), maskindex));
	}
}
