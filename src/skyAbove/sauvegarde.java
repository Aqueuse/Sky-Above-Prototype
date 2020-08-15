package skyAbove;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class sauvegarde {
	public void Save() throws IOException {
		// ecrire le tableau current et la zone current dans
		// le fichier CSV du player current
		
		//remplacer la deuxième ligne du fichier player
		String Zoneplayer = String.format("%04d", SimplePlatformer.zonePlayerCurrent);
		String TableauXplayer = String.format("%04d", SimplePlatformer.CurrentXTableauPlayer);
		String TableauYplayer = String.format("%04d", SimplePlatformer.CurrentYTableauPlayer);

		String NewPOSline = Zoneplayer+","+TableauXplayer+TableauYplayer;

		//dummy path....
		Path chemin = Paths.get(LoadFile.selectedPlayer);
		List<String> oldPlayerSave = Files.readAllLines(chemin);
		// we replace the old position by the new one
		oldPlayerSave.set(1, NewPOSline);

		FileWriter writer = new FileWriter(LoadFile.selectedPlayer);

		for (int l =0; l<oldPlayerSave.size(); l++) {
			writer.write(oldPlayerSave.get(l)+"\n");	
		}

		writer.close();

		// tell the player that the party was saved
		Notifications.setNotificationMessage("partie sauvée");
	}
}
