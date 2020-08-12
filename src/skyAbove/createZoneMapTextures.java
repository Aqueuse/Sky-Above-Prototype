package skyAbove;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class createZoneMapTextures {
	public static void imageIoWrite() throws IOException {
		File currentBackground = new File("usr/background/currentZoneTextures.png");

		int desX;
		int ImageWidth = ARNengine.sizeZone;
		int ImageHeight = 9998;

		Color Ciel = new Color(45, 142, 247);

		BufferedImage image = new BufferedImage(ImageWidth, ImageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (int posX=0; posX<ARNengine.sizeZone; posX++) {
			System.out.println(posX);
			for (int posY=0; posY<9998; posY++) {
				desX = ARNengine.currentMatrice[posX][posY];
				if (desX == 128) {
					g2.setColor(DrawMapZone.bleuArdoise);
					g2.fillRect(posX, posY, 1, 1);
				}
				else {
					g2.setColor(Ciel);
					g2.fillRect(posX, posY, 128, 128);						
				}
			}
		}

		g2.dispose();
		
		ImageIO.write(image, "png", currentBackground);
	}
}