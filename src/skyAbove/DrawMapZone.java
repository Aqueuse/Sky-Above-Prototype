package skyAbove;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// draw and save the current zone map when the zone is loaded

public class DrawMapZone {
	public static Color bleuArdoise = new Color(79,100,133);

	public static void imageIoWrite() throws IOException {
		int ImageWidth = ARNengine.sizeZone/10;
		int ImageHeight = 999;

		BufferedImage image = new BufferedImage(ImageWidth, ImageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// remplir le fond avec du darker ciel
		g2.setColor(SimulationFrame.DarkerCiel);
		g2.fillRect(0, 0, ImageWidth, ImageHeight);
		
		int lastX = 0;
		int lastY = 0;
		int currentX = 0;
		int currentY = 0;

		g2.setStroke(new BasicStroke(7));
		g2.setColor(bleuArdoise);
		for (int w=1; w<ARNengine.reliefsMap.length; w++) {
			lastX = ARNengine.reliefsMap[w-1][0]/10;
			lastY = ARNengine.reliefsMap[w-1][1]/10;
			currentX = ARNengine.reliefsMap[w][0]/10;
			currentY = ARNengine.reliefsMap[w][1]/10;

			g2.drawLine(lastX, lastY, currentX, currentY);
		}

		g2.drawLine(0, 5, ARNengine.sizeZone/10, 5);

		// Flip the image vertically
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -image.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);
		
		try { ImageIO.write(image, "png", new File("usr/background/currentMap.png")); } catch (IOException e) {}
	}
}