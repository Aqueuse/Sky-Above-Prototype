package skyAbove;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// draw and save the current mapMonde when the world is created

public class DrawMapMonde {
	static int ImageWidth = 2000;
	static int ImageHeight = 2000;
	static BufferedImage image = new BufferedImage(ImageWidth, ImageHeight, BufferedImage.TYPE_INT_ARGB);
	static Graphics2D g2 = image.createGraphics();

	public static void imageIoWrite(String loadFile) throws IOException {
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		boolean marronSwitch = false;
		Color marronClair = new Color(222,144,82);
		Color marronFonce = new Color(153,99,56);
		Color ocean = new Color(28,88,153);

		String CSVchroma[][] = readCSV.loadFile(loadFile);

		double angleRoll = 0;
		String TypeColor;
		Color[] typesColor = new Color[4000];

		for (int tc=0; tc<3999; tc++) {
			TypeColor = CSVchroma[0][tc].substring(0,1);

			if (TypeColor.equals("u") || TypeColor.equals("v") ||
				TypeColor.equals("w") || TypeColor.equals("x") ||
				TypeColor.equals("y") || TypeColor.equals("z")) 
			{
				typesColor[tc] = ocean;
			}

			else {
				if (marronSwitch == true) {
					typesColor[tc] = marronClair;
					marronSwitch = false;
				}
				else {
					typesColor[tc] = marronFonce;					
					marronSwitch = true;
				}
			}
		}

		for (int tr=0; tr<3999; tr++) {
			drawTrueArc(1000,1000,600,angleRoll,10,typesColor[tr]);
			angleRoll = angleRoll+0.09;
		}

		g2.setColor(new Color(255, 165, 78));
		g2.fillOval((ImageWidth/4)-60,(ImageHeight/4)-60,(ImageWidth/2)+120,(ImageHeight/2)+120);

		try { ImageIO.write(image, "png", new File("usr/background/currentWorld.png")); } catch (IOException e) {}
	}

	public static void drawTrueArc(double originX, double originY,
		double rayon,
		double startAngle, double arcAngle, Color colorZone) throws IOException {

		// trouver le X1/X2
		double badX = originX-rayon;
		double badY = originY-rayon;
		double badWidth = rayon*2;
		double badHeight = rayon*2;
		double badStart = startAngle;
		double badAngle = arcAngle;

		Shape arc = new Arc2D.Double(
			badX, badY,
			badWidth, badHeight,
			badStart, badAngle,
			Arc2D.PIE);

		g2.setColor(colorZone);
		g2.fill(arc);
	}
}