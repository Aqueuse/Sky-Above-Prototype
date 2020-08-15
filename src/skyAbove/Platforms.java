package skyAbove;

import java.awt.Color;
//import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Rectangle;

public class Platforms {
	// la zone de jeu fait 42 pixels de large
	static double borneLeft = 0;
	static double borneRight = 42; // LA reponse, of course

	// la zone de jeu fait 22,8 pixels de haut
	static double borneTop = 0;
	static double borneBottom = 22.8;

	static double middleX = 21;
	static double middleY = 11.4;
	
	public static void reloadPlatforms() {
		int desX;

		ObjectsWorld.plateforms.removeAllFixtures();		
		for (int posX=0; posX<10+(createZoneBackgrounds.facteurZoom*5); posX++) {
			for (int posY=0; posY<6+(createZoneBackgrounds.facteurZoom*5); posY++) {
				desX = ARNengine.currentMatrice[SimplePlatformer.CurrentXTableauPlayer+posX][SimplePlatformer.CurrentYTableauPlayer+posY];

				if (desX<2432) {
					Rectangle plat1 = new Rectangle(5,1);
					plat1.translate(20, 11);
					ObjectsWorld.plateforms.addFixture(plat1);
				}
			}
		}

		ObjectsWorld.plateforms.setColor(Color.DARK_GRAY);
	}
		
	public static void removePlatforms() {
		ObjectsWorld.plateforms.removeAllFixtures();
	}
}
