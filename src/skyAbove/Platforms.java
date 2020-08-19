package skyAbove;

import org.dyn4j.geometry.Segment;
import org.dyn4j.geometry.Vector2;

public class Platforms {
	// la zone de jeu fait 42 pixels de large
	static double relativeWidth = 42; // LA reponse, of course :p

	// la zone de jeu fait 22,8 pixels de haut
	static double relativeHeight = 22.8;

	static double relativePosX;
	static double relativePosY;
	static double lastRelativePosX;

	static int[][] platformsArray = new int[10][6];

	public static void reloadPlatforms() {
		int desX;
		
		ObjectsWorld.plateforms.removeAllFixtures();
		
		// reset the platforms array
		for (int posX=0; posX<10; posX++) {
			for (int posY=0; posY<6; posY++) {
				platformsArray[posX][posY] = 0;
			}
		}

		for (int posX=0; posX<10; posX++) {
			for (int posY=0; posY<6; posY++) {
				desX = ARNengine.currentMatrice[SimplePlatformer.CurrentXTableauPlayer+posX][SimplePlatformer.CurrentYTableauPlayer+posY];

				if (desX==0) { //flat
					platformsArray[posX][posY] = 1;
					
					ObjectsWorld.plateforms.addFixture(new Segment(
						new Vector2(4*posX,4*posY),
						new Vector2(4*(posX+1),4*posY)));
				}
				if (desX==1664) {// down
					platformsArray[posX][posY] = 2;

					ObjectsWorld.plateforms.addFixture(new Segment(
						new Vector2(4*posX,4*posY),
						new Vector2(4*(posX+1),4*(posY+1) )));
				}
				if (desX==1792) {// up
					platformsArray[posX][posY] = 3;

					ObjectsWorld.plateforms.addFixture(new Segment(
						new Vector2(4*posX,4*(posY+1)),
						new Vector2(4*(posX+1),4*posY)));
				}
			}
		}
	}

	public static void removePlatforms() {
		ObjectsWorld.plateforms.removeAllFixtures();
	}
}
