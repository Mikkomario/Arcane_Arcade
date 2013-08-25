package backgrounds;

import graphic.SpriteBank;
import handlers.ActorHandler;
import handlers.DrawableHandler;

/**
 * Tiles are backgrounds that have certain proportions.
 *
 * @author Mikko Hilpinen.
 *         Created 6.7.2013.
 * @see Background
 */
public class Tile extends Background
{
	// CONSTRUCTOR	------------------------------------------------------
	
	/**
	 * Creates a new tile with the given information
	 *
	 * @param x The x-coordinate of the tile's top left position
	 * @param y The y-coordinate of the tile's top left position
	 * @param drawer The drawableHandler that will draw the tile
	 * @param actorhandler The actorhandler that will animate the tile (optional, 
	 * for animated tiles)
	 * @param bank The spritebank that holds the tile's texture
	 * @param texturename The name of tile's texture in the spritebank
	 * @param width The width of the tile
	 * @param height The height of the tile
	 */
	public Tile(int x, int y, DrawableHandler drawer,
			ActorHandler actorhandler, SpriteBank bank, String texturename, 
			int width, int height)
	{
		super(x, y, drawer, actorhandler, bank, texturename);
		// Sets the size of the tile
		setDimensions(width, height);
		// Tiles are drawn a bit above the backgrounds
		setDepth(getDepth() - 5);
	}
}
