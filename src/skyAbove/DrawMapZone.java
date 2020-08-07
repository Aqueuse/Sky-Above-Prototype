package skyAbove;

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
	public static void imageIoWrite() throws IOException {
		Color marronClair = new Color(232,212,152);
		Color marronFonce = new Color(219,176,101);

		int ImageWidth = ARNengine.sizeZone/10;
		int ImageHeight = 999;

		BufferedImage image = new BufferedImage(ImageWidth, ImageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// remplir le fond avec du marron clair
		g2.setColor(marronClair);
		g2.fillRect(0, 0, ImageWidth, ImageHeight);

		int[] Xpoints = new int[ARNengine.reliefsMap.length];
		int[] Ypoints = new int[ARNengine.reliefsMap.length];

		for (int w=0; w<ARNengine.reliefsMap.length; w++) {
			Xpoints[w] = ARNengine.reliefsMap[w][0]/10;
			Ypoints[w] = ARNengine.reliefsMap[w][1]/10;
		}
		
		g2.setColor(marronFonce);
		g2.fillPolygon(Xpoints, Ypoints, ARNengine.reliefsMap.length);

		// Flip the image vertically
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -image.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);
		
		try { ImageIO.write(image, "png", new File("usr/background/currentMap.png")); } catch (IOException e) {}
		System.out.println("carte de zone créée");
	}
}