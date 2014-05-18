package arcane_arcade_tutorials;

import java.awt.Graphics2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import arcane_arcade_field.SpellTarget;
import arcane_arcade_main.GameSettings;
import arcane_arcade_spelleffects.SpellEffect;
import arcane_arcade_status.Element;
import arcane_arcade_worlds.Navigator;
import utopia_gameobjects.CollidingDrawnObject;
import utopia_graphic.SingleSpriteDrawer;
import utopia_handleds.Collidable;
import utopia_listeners.RoomListener;
import utopia_resourcebanks.MultiMediaHolder;
import utopia_utility.CollisionType;
import utopia_utility.DepthConstants;
import utopia_worlds.Area;
import utopia_worlds.Room;

/**
 * CastingTutorial teaches the player to cast basic spells
 * 
 * @author Mikko Hilpinen
 * @since 17.5.2014
 */
public class CastingTutorial
{
	// ATTRIBUTES	-----------------------------------------------------
	
	private int targetsLeft, phase;
	private Area area;
	private Navigator navigator;
	
	
	// CONSTRUCTOR	-----------------------------------------------------
	
	/**
	 * Creates and starts the casting tutorial
	 * 
	 * @param area The area where the tutorial will be held
	 * @param navigator The navigator that handles transitions between the 
	 * areas
	 */
	public CastingTutorial(Area area, Navigator navigator)
	{
		// Initializes attributes
		this.area = area;
		this.navigator = navigator;
		this.phase = 0;

		// Starts the tutorial
		this.targetsLeft = 3;
		new Target(0, area, Element.NOELEMENT);
		new Target(1, area, Element.NOELEMENT);
		new Target(2, area, Element.NOELEMENT);
	}

	
	// OTHER METHODS	-------------------------------------------------
	
	private void onTargetDestroyed()
	{
		// Counts the targets
		this.targetsLeft --;
		
		System.out.println(this.targetsLeft);
		
		// If there are no more targets, moves forward
		if (this.targetsLeft > 0)
			return;
		
		// On phase 0 starts phase 1 and creates new targets
		if (this.phase == 0)
		{
			this.phase = 1;
			this.targetsLeft = 3;
			new Target(0, this.area, Element.BLAZE);
			new Target(1, this.area, Element.TIDE);
			new Target(2, this.area, Element.FROST);
			
		}
		// On phase 1 ends the tutorial
		else
			this.navigator.startPhase("tutorialmenu", null);
	}
	
	
	// SUBCLASSES	-----------------------------------------------------
	
	private class Target extends CollidingDrawnObject implements RoomListener, SpellTarget
	{
		// ATTRIBUTES	-------------------------------------------------
		
		private SingleSpriteDrawer spriteDrawer;
		private Element weakToElement;
		
		
		// CONSTRUCTOR	--------------------------------------------------
		
		public Target(int heightIndex, Area area, Element element)
		{
			super(150, GameSettings.SCREENHEIGHT / 6 + 
					(GameSettings.SCREENHEIGHT / 3) * heightIndex, 
					DepthConstants.FOREGROUND, true, CollisionType.CIRCLE, area);
			
			// Initializes attributes
			String spriteName;
			switch (element)
			{
				case BLAZE: spriteName = "fireelemental"; this.weakToElement = Element.FROST; break;
				case FROST: spriteName = "iceelemental"; this.weakToElement =  Element.TIDE; break;
				case TIDE: spriteName = "waterelemental"; this.weakToElement = Element.BLAZE; break;
				default: spriteName = "target"; this.weakToElement = Element.NOELEMENT;
			}
			
			this.spriteDrawer = new SingleSpriteDrawer(
					MultiMediaHolder.getSprite("tutorial", spriteName), 
					area.getActorHandler(), this);
			
			// Sets up the collision systems
			setCircleCollisionPrecision(getRadius(), 4, 2);
		}
		
		
		// IMPLEMETED METHODS	-----------------------------------------

		@Override
		public Class<?>[] getSupportedListenerClasses()
		{
			return new Class<?>[0];
		}

		@Override
		public int getWidth()
		{
			if (this.spriteDrawer == null)
				return 100;
			return this.spriteDrawer.getSprite().getWidth();
		}

		@Override
		public int getHeight()
		{
			if (this.spriteDrawer == null)
				return 100;
			return this.spriteDrawer.getSprite().getHeight();
		}

		@Override
		public int getOriginX()
		{
			if (this.spriteDrawer == null)
				return 0;
			return this.spriteDrawer.getSprite().getOriginX();
		}

		@Override
		public int getOriginY()
		{
			if (this.spriteDrawer == null)
				return 0;
			return this.spriteDrawer.getSprite().getOriginY();
		}

		@Override
		public void drawSelfBasic(Graphics2D g2d)
		{
			if (this.spriteDrawer != null)
				this.spriteDrawer.drawSprite(g2d, 0, 0);
			
			drawCollisionArea(g2d);
			drawCollisionPoints(g2d);
		}

		@Override
		public void onRoomStart(Room room)
		{
			// Does nothing
		}

		@Override
		public void onRoomEnd(Room room)
		{
			// Dies
			kill();
		}
		
		@Override
		public void onCollision(ArrayList<Double> colpoints,
				Collidable collided, double steps)
		{
			if (isDead())
				return;
			
			// A target is destroyed when a (certain type of) spell collides with it
			if (!(collided instanceof SpellEffect))
				return;
			
			SpellEffect collidedEffect = (SpellEffect) collided;
			
			if (this.weakToElement == Element.NOELEMENT || 
					collidedEffect.getFirstElement() == this.weakToElement || 
					collidedEffect.getSecondElement() == this.weakToElement)
			{
				kill();
				onTargetDestroyed();
			}
		}
	}
}
