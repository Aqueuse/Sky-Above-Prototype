package skyAbove;

import org.dyn4j.geometry.Segment;
import org.dyn4j.geometry.Vector2;

public class Platforms {
	static double relativePosX;
	static double relativePosY;
	static double lastRelativePosX;

	static int[][] platformsArray = new int[10][6];

	public static void reloadPlatforms() {
		int desX;
		SimplePlatformer.plateforms.removeAllFixtures();

		if (BackgoundTheater.ComportementRoad==true) {
			SimplePlatformer.plateforms.addFixture(new Segment(
					new Vector2(0,15.4),
					new Vector2(30,15.4)));
		}

		if (BackgoundTheater.ComportementLandscape==true) {
			// reset the platforms array
			for (int posX=0; posX<10; posX++) {
				for (int posY=0; posY<6; posY++) {
					platformsArray[posX][posY] = 0;
				}
			}

			if (SimplePlatformer.CurrentYTableauPlayer<9990) {
				for (int posX=0; posX<10; posX++) {
					for (int posY=0; posY<6; posY++) {
						desX = ARNengine.currentMatrice[SimplePlatformer.CurrentXTableauPlayer+posX][SimplePlatformer.CurrentYTableauPlayer+posY];

						if (desX==10) { //flat
							platformsArray[posX][posY] = 1;
							SimplePlatformer.plateforms.addFixture(new Segment(
									new Vector2(2.8505*posX,2.8505*posY),
									new Vector2(2.8505*(posX+1),2.8505*posY)));
						}
						if (desX==20) {// down
							platformsArray[posX][posY] = 2;
							SimplePlatformer.plateforms.addFixture(new Segment(
									new Vector2(2.8505*posX,2.8505*posY),
									new Vector2(2.8505*(posX+1),2.8505*(posY+1) )));
						}
						if (desX==30) {// up
							platformsArray[posX][posY] = 3;
							SimplePlatformer.plateforms.addFixture(new Segment(
									new Vector2(2.8505*posX,2.8505*(posY+1)),
									new Vector2(2.8505*(posX+1),2.8505*posY)));
						}
					}
				}
			}
			if (SimplePlatformer.CurrentYTableauPlayer==9990) {
				SimplePlatformer.plateforms.addFixture(new Segment(
						new Vector2(0,14.5),
						new Vector2(28.8505,14.5)));

				for (int posX=0; posX<10; posX++) {
					for (int posY=0; posY<5; posY++) {
						desX = ARNengine.currentMatrice[SimplePlatformer.CurrentXTableauPlayer+posX][SimplePlatformer.CurrentYTableauPlayer+posY];

						if (desX==10) { //flat
							platformsArray[posX][posY] = 1;
							SimplePlatformer.plateforms.addFixture(new Segment(
									new Vector2(2.8505*posX,2.8505*posY),
									new Vector2(2.8505*(posX+1),2.8505*posY)));
						}
						if (desX==20) {// down
							platformsArray[posX][posY] = 2;
							SimplePlatformer.plateforms.addFixture(new Segment(
									new Vector2(2.8505*posX,2.8505*posY),
									new Vector2(2.8505*(posX+1),2.8505*(posY+1) )));
						}
						if (desX==30) {// up
							platformsArray[posX][posY] = 3;
							SimplePlatformer.plateforms.addFixture(new Segment(
									new Vector2(2.8505*posX,2.8505*(posY+1)),
									new Vector2(2.8505*(posX+1),2.8505*posY)));
						}
					}
				}
			}
		}
	}
}