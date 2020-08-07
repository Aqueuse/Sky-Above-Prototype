package skyAbove;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Map extends JPanel {
		static JPanel mapPanel = new JPanel();

		public static void showMap() throws IOException {
			Graphics g = mapPanel.getGraphics();
			GameMenu.Gameglobal.add(mapPanel);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			String filename = "usr/background/currentMap.png";
			BufferedImage img = null;
			try { img = ImageIO.read(new File(filename));} catch (IOException e) {}

			mapPanel.paintComponents(g2);
		
			g2.drawImage(img,   // version complète
				0, 0,       // dx1, dy1 - x,y destination 1st corner
				800, 800,   // dx2, dy2 - x,y destination 2nd corner
				1000, 1000, // sx1, sy1 - x,y source 1st corner
				1800, 1800, // sx2, sy2 - x,y source 2nd corner
				Color.blue, // bgcolor
				null);     // observer - object to get image modifications 
	}
}
