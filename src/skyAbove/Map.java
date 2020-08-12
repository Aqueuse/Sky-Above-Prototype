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

// class et methode pour ne plus jamais recoder cette GROSSE MERDE de
// graphics et graphics2D : affiche une image depuis un string RELATIF
// parce que ce crayon de merde est une pourriture et qu'on ne devrait
// pas perdre une demi journee de sa vie à le reimplémenter
//       ne pas oublier d'ajouter le dossier au buildpath
//       et d'importer le fichier dans le projet. Don't give up.

public class Map extends JPanel {
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		super.paintComponents(g2);

		String filename = "usr/background/currentMap.png";
		BufferedImage img = null;
		try { img = ImageIO.read(new File(filename));} catch (IOException e) {}

		g2.setColor(SimulationFrame.BleuCiel);
		g2.fillRect(0, 0, 500, 500);
		
		g2.drawImage(img,   // version complète
				0, 0,       // dx1, dy1 - x,y destination 1st corner
				499, 499,   // dx2, dy2 - x,y destination 2nd corner
				0, 0, // sx1, sy1 - x,y source 1st corner
				999, 999, // sx2, sy2 - x,y source 2nd corner
				SimulationFrame.BleuCiel, // bgcolor
				null);     // observer - object to get image modifications 

		int WhereX = ((int)SimplePlatformer.CurrentXTableauPlayer/20);
		int WhereY = ((int)SimplePlatformer.CurrentYTableauPlayer/20);

		g2.setColor(Color.red);
		g2.fillOval(WhereX, WhereY-7, 10, 10);
		
		g2.dispose();
	}

	public void main() {
		SimulationFrame.PanelsContainerGame.add(new Map());
	}
}