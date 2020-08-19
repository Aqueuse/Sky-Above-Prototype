package skyAbove;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class createZoneBackgrounds {
	public static int facteurZoom = 0;
	public static void imageIoWrite() throws IOException {
		File currentBackground = new File("usr/background/currentTableau.png");

		int desX;

		// gestion du zoom :
		// 	 plan rapproché = 10 tiles/6 tiles
		//   plan intermediaire = 15 tiles/11 tiles
		//   dezoom total = 20 tiles / 16 tiles
		int ImageWidth = 1280+(facteurZoom*5*128);
		int ImageHeight = 768+(facteurZoom*5*128);

		int tempX1;
		int tempX2;
		int tempY1;
		int tempY2;

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

			for (int posX=0; posX<10+(facteurZoom*5); posX++) {
				tempX1=posX*128;
				tempX2=tempX1+128;
				for (int posY=0; posY<6+(facteurZoom*5); posY++) {
					tempY1=posY*128;
					tempY2=(posY*128)+128;
					desX = ARNengine.currentMatrice[SimplePlatformer.CurrentXTableauPlayer+posX][SimplePlatformer.CurrentYTableauPlayer+posY];
					if (desX<2432) {
						g2.drawImage(textureSET,    // version complète
							tempX1, tempY1, // dx1, dy1 - x,y destination 1st corner
							tempX2, tempY2,   // dx2, dy2 - x,y destination 2nd corner
							desX, 0, 				  // sx1, sy1 - x,y source 1st corner
							desX+128, 128,			  // sx2, sy2 - x,y source 2nd corner
							SimplePlatformer.BleuCiel, // bgcolor
							null);     // observer - object to get image modifications 									
					}
				}
			}

			for (int posX=0; posX<10+(facteurZoom*5); posX++) {
				tempX1=posX*128;
				tempX2=tempX1+128;

				g2.drawImage(textureRoad,  // version complète
						tempX1, 640,     // dx1, dy1 - x,y destination 1st corner
						tempX2, 768,     // dx2, dy2 - x,y destination 2nd corner
						0, 0, 			   // sx1, sy1 - x,y source 1st corner
						128, 128,		   // sx2, sy2 - x,y source 2nd corner
						SimplePlatformer.BleuCiel, // bgcolor
						null);     // observer - object to get image modifications 									
			}
		}

		else {
			for (int posX=0; posX<10+(facteurZoom*5); posX++) {
				tempX1=posX*128;
				tempX2=tempX1+128;
				for (int posY=0; posY<6+(facteurZoom*5); posY++) {
					tempY1=posY*128;
					tempY2=(posY*128)+128;
					desX = ARNengine.currentMatrice[SimplePlatformer.CurrentXTableauPlayer+posX][(SimplePlatformer.CurrentYTableauPlayer+posY)];
					if (desX<2304) {
						g2.drawImage(textureSET,    // version complète
							tempX1, tempY1, // dx1, dy1 - x,y destination 1st corner
							tempX2, tempY2,   // dx2, dy2 - x,y destination 2nd corner
							desX, 0, 				  // sx1, sy1 - x,y source 1st corner
							desX+128, 128,			  // sx2, sy2 - x,y source 2nd corner
							SimplePlatformer.BleuCiel, // bgcolor
							null);     // observer - object to get image modifications 									
					}
				}
			}
		}

		ImageIO.write(image, "png", currentBackground);
	}
}