package skyAbove;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class createZoneBackgrounds {
	public static void imageIoWrite() throws IOException {
		File currentBackground = new File("usr/background/currentTableau.png");

		int desX;
		int ImageWidth = 1280; // = 10 tiles
		int ImageHeight = 768; // =  6 tiles

		int tempX1;
		int tempX2;
		int tempY1;
		int tempY2;

		Color Ciel = new Color(45, 142, 247);

		BufferedImage image = new BufferedImage(ImageWidth, ImageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		String imgPath = "ressources/textures/"+ARNengine.textureSet;
		BufferedImage textureSET = null;
		try { textureSET = ImageIO.read(new File(imgPath));} catch (IOException e) {}

		// si le tableau est en bas, on dessine aussi la road
		if (SimplePlatformer.CurrentYTableauPlayer == 9990) {
			BufferedImage textureRoad = null;
			try { textureRoad = ImageIO.read(new File("ressources/textures/road.png"));} catch (IOException e) {}

			for (int posX=0; posX<10; posX++) {
				tempX1=posX*128;
				tempX2=tempX1+128;
				for (int posY=0; posY<6; posY++) {
					tempY1=posY*128;
					tempY2=(posY*128)+128;
					desX = ARNengine.currentMatrice[SimplePlatformer.CurrentXTableauPlayer+posX][SimplePlatformer.CurrentYTableauPlayer+posY];
					if (desX == 128) {
						g2.drawImage(textureSET,    // version complète
							tempX1, tempY1, // dx1, dy1 - x,y destination 1st corner
							tempX2, tempY2,   // dx2, dy2 - x,y destination 2nd corner
							desX, 0, 				  // sx1, sy1 - x,y source 1st corner
							desX+128, 128,			  // sx2, sy2 - x,y source 2nd corner
							Color.darkGray, // bgcolor
							null);     // observer - object to get image modifications 									
					}
					else {
						g2.setColor(Ciel);
						g2.fillRect(tempX1, tempY1, 128, 128);						
					}
				}
			}

			for (int posX=0; posX<10; posX++) {
				tempX1=posX*128;
				tempX2=tempX1+128;

				g2.drawImage(textureRoad,  // version complète
						tempX1, 640,     // dx1, dy1 - x,y destination 1st corner
						tempX2, 768,     // dx2, dy2 - x,y destination 2nd corner
						0, 0, 			   // sx1, sy1 - x,y source 1st corner
						128, 128,		   // sx2, sy2 - x,y source 2nd corner
						Color.darkGray, // bgcolor
						null);     // observer - object to get image modifications 									
			}
		}

		else {
			for (int posX=0; posX<10; posX++) {
				tempX1=posX*128;
				tempX2=tempX1+128;
				for (int posY=0; posY<6; posY++) {
					tempY1=posY*128;
					tempY2=(posY*128)+128;
					desX = ARNengine.currentMatrice[SimplePlatformer.CurrentXTableauPlayer+posX][(SimplePlatformer.CurrentYTableauPlayer+posY)];
					if (desX == 128) {
						g2.drawImage(textureSET,    // version complète
							tempX1, tempY1, // dx1, dy1 - x,y destination 1st corner
							tempX2, tempY2,   // dx2, dy2 - x,y destination 2nd corner
							desX, 0, 				  // sx1, sy1 - x,y source 1st corner
							desX+128, 128,			  // sx2, sy2 - x,y source 2nd corner
							Color.darkGray, // bgcolor
							null);     // observer - object to get image modifications 									
					}
					else {
						g2.setColor(Ciel);
						g2.fillRect(tempX1, tempY1, 128, 128);						
					}
				}
			}
		}
		
		ImageIO.write(image, "png", currentBackground);
	}
}