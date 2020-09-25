package skyAbove;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import org.dyn4j.dynamics.BodyFixture;

final class DrawImageOnBody extends SimulationBody {
	public static File walk_left = new File("ressources/characters/GraveRobber/GraveRobber_walk_left.png");
	public static File walk_right = new File("ressources/characters/GraveRobber/GraveRobber_walk_right.png");
	public static File idle_left = new File("ressources/characters/GraveRobber/GraveRobber_idle_left.png");
	public static File idle_right = new File("ressources/characters/GraveRobber/GraveRobber_idle_right.png");

	static BufferedImage walkLeft;
	static BufferedImage walkRight;
	static BufferedImage idleLeft;
	static BufferedImage idleRight;

	static int srcSprite = 0;
	static int spritesQuantity = 5;
	public static int SpriteSelect = 1;
	// speedSprite=3 for slow walk  / speedSprite=1 for running
	static int speedSprite = 2;
	static int rotate = 0;

	@Override
	protected void renderFixture(Graphics2D g, double scale, BodyFixture fixture, Color color) {	    		

		// type =  -2 (walk left)
		// 		    2 (walk right)
		// 		   -1 (idle left)
		// 		    1 (idle right)


		if (SpriteSelect==-2) { 
			g.drawImage(walkLeft,-72,-96,72,48,srcSprite, 0,srcSprite+48, 48,null, null);
		}
		if (SpriteSelect==2) {
			g.drawImage(walkRight,-72,-96,72,48,srcSprite, 0,srcSprite+48, 48,null, null);
		}
		if (SpriteSelect==-1) {
			g.drawImage(idleLeft,-72,-96,72,48,srcSprite, 0,srcSprite+48, 48,null, null);
		}
		if (SpriteSelect==1) {
			g.drawImage(idleRight,-72,-96,72,48,srcSprite, 0,srcSprite+48, 48,null, null);
		}
	}
}