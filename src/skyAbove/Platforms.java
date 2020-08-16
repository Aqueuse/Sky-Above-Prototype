package skyAbove;

import java.awt.Color;
import org.dyn4j.geometry.Segment;
import org.dyn4j.geometry.Vector2;

public class Platforms {
	// la zone de jeu fait 42 pixels de large
	static double relativeWidth = 42; // LA reponse, of course :p

	// la zone de jeu fait 22,8 pixels de haut
	static double relativeHeight = 22.8;

	public static void reloadPlatforms() {
		int desX;
		int XtilesNB = 10;
		int YtilesNB = 6;

		double relativePosX;
		double relativePosY;
		double lastRelativePosX;

		double XTile = 4;

		ObjectsWorld.plateforms.removeAllFixtures();

		for (int posX=0; posX<XtilesNB; posX++) {
			for (int posY=0; posY<YtilesNB; posY++) {
				desX = ARNengine.currentMatrice[SimplePlatformer.CurrentXTableauPlayer+posX][SimplePlatformer.CurrentYTableauPlayer+posY];
				if (desX==0) {
					relativePosX = XTile * posX;
					lastRelativePosX = XTile * (posX+1);
					relativePosY = XTile * posY;

					ObjectsWorld.plateforms.addFixture(new Segment(
						new Vector2(relativePosX,relativePosY),
						new Vector2(lastRelativePosX,relativePosY)));
				}
				if (desX==1664) {// down
					relativePosX = XTile * posX;
					lastRelativePosX = XTile * (posX+1);
					relativePosY = XTile * posY;

					ObjectsWorld.plateforms.addFixture(new Segment(
						new Vector2(relativePosX,relativePosY),
						new Vector2(lastRelativePosX,relativePosY+4)));					
				}
				if (desX==1792) {// up
					relativePosX = XTile * posX;
					lastRelativePosX = XTile * (posX+1);
					relativePosY = XTile * posY;

					ObjectsWorld.plateforms.addFixture(new Segment(
						new Vector2(relativePosX,relativePosY-4),
						new Vector2(lastRelativePosX,relativePosY)));										
				}
			}
		}

		ObjectsWorld.plateforms.setColor(Color.RED);
	}

	public static void removePlatforms() {
		ObjectsWorld.plateforms.removeAllFixtures();
	}
}
