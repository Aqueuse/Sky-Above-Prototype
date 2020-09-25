package skyAbove;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class createTableauBackground {
	// empty = 0
	// plateformes :
	// flat = 10 | down = 20 | up = 30
	// tile nu = 40
	
	public static int facteurZoom = 0;
	
	public static void wide()  throws IOException {
		File currentBackground = new File("usr/background/currentTableau.png");

		int ImageWidth = 3840;
		int ImageHeight = 768;

		int desX;

		int tempX1;
		int tempX2;
		int tempY1;
		int tempY2;

		int Xpoints[] = new int[3];
		int Ypoints[] = new int[3];

		BufferedImage image = new BufferedImage(ImageWidth, ImageHeight, BufferedImage.TYPE_INT_ARGB); 
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.green);
		
		// preparation de la transparence
		Composite opaque = g2.getComposite();
        AlphaComposite transparent = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER);

		BufferedImage herbe = null;
		try { herbe = ImageIO.read(new File("ressources/textures/herbe.png"));} catch (IOException e) {}

		for (int posX=0; posX<30; posX++) {
			tempX1=posX*128;
			tempX2=tempX1+128;
			for (int posY=0; posY<6; posY++) {
				tempY1=posY*128;
				tempY2=(posY*128)+128;
				desX = ARNengine.currentMatrice
						[SimplePlatformer.CurrentXTableauPlayer+posX-10]
						[(SimplePlatformer.CurrentYTableauPlayer+posY)];

				if (desX==10) { // plateforme flat
					g2.setComposite(transparent);
					g2.fillRect(tempX1, tempY1, 128, 128);
					g2.drawImage(herbe,  // version compl�te
							tempX1, tempY1,     // dx1, dy1 - x,y destination 1st corner
							tempX2, tempY2,     // dx2, dy2 - x,y destination 2nd corner
							0, 0, 			   // sx1, sy1 - x,y source 1st corner
							128, 128,		   // sx2, sy2 - x,y source 2nd corner
							null, // bgcolor
							null);     // observer - object to get image modifications 									
					g2.setComposite(opaque);
				}
				if (desX==20) {
					Xpoints[0] = tempX1;
					Xpoints[1] = tempX1+128;
					Xpoints[2] = tempX1;
					Ypoints[0] = tempY1;
					Ypoints[1] = tempY1+128;
					Ypoints[2] = tempY1;

					g2.fillPolygon(Xpoints, Ypoints, 3);
				}
				if (desX==30) {
					Xpoints[0] = tempX1;
					Xpoints[1] = tempX1+128;
					Xpoints[2] = tempX1;
					Ypoints[0] = tempY1;
					Ypoints[1] = tempY1+128;
					Ypoints[2] = tempY1;

					g2.fillPolygon(Xpoints, Ypoints, 3);
				}
				if (desX==40) {
					g2.fillRect(tempX1, tempY1, 128, 128);
				}
			}
		}

		if (SimplePlatformer.CurrentYTableauPlayer == 9990) {
			// si le tableau est en bas, on dessine aussi la road
			BufferedImage textureRoad = null;
			try { textureRoad = ImageIO.read(new File("ressources/textures/road.png"));} catch (IOException e) {}

			for (int posX=0; posX<30; posX++) {
				tempX1=posX*128;
				tempX2=tempX1+128;

				g2.drawImage(textureRoad,  // version compl�te
						tempX1, 640,     // dx1, dy1 - x,y destination 1st corner
						tempX2, 768,     // dx2, dy2 - x,y destination 2nd corner
						0, 0, 			   // sx1, sy1 - x,y source 1st corner
						128, 128,		   // sx2, sy2 - x,y source 2nd corner
						SimplePlatformer.BleuCiel, // bgcolor
						null);     // observer - object to get image modifications 									
			}
		}
		ImageIO.write(image, "png", currentBackground);
	}
	
	public static void tight() throws IOException {
		File currentBackground = new File("usr/background/currentTableau.png");

		int ImageWidth = 1280+(facteurZoom*5*128);
		int ImageHeight = 768+(facteurZoom*5*128);

		int desX;

		int tempX1;
		int tempX2;
		int tempY1;
		int tempY2;

		int Xpoints[] = new int[3];
		int Ypoints[] = new int[3];

		BufferedImage image = new BufferedImage(ImageWidth, ImageHeight, BufferedImage.TYPE_INT_ARGB); 
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color(100,121,107));
		
		// preparation de la transparence
		Composite opaque = g2.getComposite();
        AlphaComposite transparent = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER);

		BufferedImage herbe = null;
		try { herbe = ImageIO.read(new File("ressources/textures/herbe.png"));} catch (IOException e) {}

		for (int posX=0; posX<10+(facteurZoom*5); posX++) {
			tempX1=posX*128;
			tempX2=tempX1+128;
			for (int posY=0; posY<6+(facteurZoom*5); posY++) {
				tempY1=posY*128;
				tempY2=(posY*128)+128;
				desX = ARNengine.currentMatrice[SimplePlatformer.CurrentXTableauPlayer+posX][(SimplePlatformer.CurrentYTableauPlayer+posY)];

				if (desX==10) { // plateforme flat
					g2.setComposite(transparent);
					g2.fillRect(tempX1, tempY1, 128, 128);
					g2.drawImage(herbe,  // version compl�te
							tempX1, tempY1,     // dx1, dy1 - x,y destination 1st corner
							tempX2, tempY2,     // dx2, dy2 - x,y destination 2nd corner
							0, 0, 			   // sx1, sy1 - x,y source 1st corner
							128, 128,		   // sx2, sy2 - x,y source 2nd corner
							null, // bgcolor
							null);     // observer - object to get image modifications 									
					g2.setComposite(opaque);
				}
				if (desX==20) {
					Xpoints[0] = tempX1;
					Xpoints[1] = tempX1+128;
					Xpoints[2] = tempX1;
					Ypoints[0] = tempY1;
					Ypoints[1] = tempY1+128;
					Ypoints[2] = tempY1;

					g2.fillPolygon(Xpoints, Ypoints, 3);
				}
				if (desX==30) {
					Xpoints[0] = tempX1;
					Xpoints[1] = tempX1+128;
					Xpoints[2] = tempX1;
					Ypoints[0] = tempY1;
					Ypoints[1] = tempY1+128;
					Ypoints[2] = tempY1;

					g2.fillPolygon(Xpoints, Ypoints, 3);
				}
				if (desX==40) {
					g2.fillRect(tempX1, tempY1, 128, 128);
				}
			}
		}

		if (SimplePlatformer.CurrentYTableauPlayer == 9990) {
			// si le tableau est en bas, on dessine aussi la road
			BufferedImage textureRoad = null;
			try { textureRoad = ImageIO.read(new File("ressources/textures/road.png"));} catch (IOException e) {}

			for (int posX=0; posX<10+(facteurZoom*5); posX++) {
				tempX1=posX*128;
				tempX2=tempX1+128;

				g2.drawImage(textureRoad,  // version compl�te
						tempX1, 640,     // dx1, dy1 - x,y destination 1st corner
						tempX2, 768,     // dx2, dy2 - x,y destination 2nd corner
						0, 0, 			   // sx1, sy1 - x,y source 1st corner
						128, 128,		   // sx2, sy2 - x,y source 2nd corner
						SimplePlatformer.BleuCiel, // bgcolor
						null);     // observer - object to get image modifications 									
			}
		}

		ImageIO.write(image, "png", currentBackground);
	}
}