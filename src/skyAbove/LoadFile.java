package skyAbove;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LoadFile  implements ActionListener {
    public final static JPanel PanCharger = new JPanel();
   	private final static JButton charger = new JButton("Charger");
    private final static JButton flecheRetour = new JButton("Retour");
    private final static JLabel headMenu = new JLabel("Mes mondes");
 	private final static JList<String> listFile = new JList<>();
    private final static JScrollPane scrollableList = new JScrollPane(listFile);

 	// récuperer les noms des mondes dans le dossier map
 	static File dossierMap = new File("usr/map");
	public static String[] listMap = dossierMap.list();
 	static String[] WorldsListing = new String[listMap.length];
    public static String[] nameWorlds = new String[listMap.length];

 	// récuperer les informations des players dans le dossier player
 	static File dossierPlayer = new File("usr/player");
	public static String[] listPlayers = dossierPlayer.list();
 	String[] PlayersCreationTime = new String[listPlayers.length];
 	static String playerFilename;
	static String selectedMap;
	static String selectedPlayer;
	static String nameWorld;
	static String namePlayer;

	static String [] typesZones = new String[4000];
	static Integer [] sizesZones = new Integer[4000];
	static Integer[] hauteurZones = new Integer[4000];
	static String[] adnZones = new String[4000];

	static int positionPlayer;
	static int zonePlayer;
	static String tableauXPlayer;
	static String tableauYPlayer;

	public void openFile() throws IOException {
 	    GridBagLayout gbl_Charger = new GridBagLayout();
 	    gbl_Charger.columnWeights = new double[]{10, 10};
 	    gbl_Charger.rowWeights = new double[]{10, 10};
 		PanCharger.setBackground(new Color(173, 216, 230));
 	    PanCharger.setLayout(gbl_Charger);

 	    // bouton retour
     	GridBagConstraints gbc_retour = new GridBagConstraints();
     	gbc_retour.insets = new Insets(5, 5, 5, 5);
     	gbc_retour.gridx = 0;
     	gbc_retour.gridy = 0;
     	gbc_retour.anchor = GridBagConstraints.NORTHWEST;
    	flecheRetour.setFont(new Font("SansSerif", Font.PLAIN, 30));
		flecheRetour.addActionListener(this);
    	PanCharger.add(flecheRetour, gbc_retour);

    	// petit textField pour la liste des mondes
     	GridBagConstraints gbc_headFile = new GridBagConstraints();
    	gbc_headFile.insets = new Insets(5, 5, 5, 5);
    	gbc_headFile.gridx = 1;
    	gbc_headFile.gridy = 0;
     	gbc_headFile.anchor = GridBagConstraints.NORTH;
    	headMenu.setFont(new Font("SansSerif", Font.PLAIN, 30));
        PanCharger.add(headMenu, gbc_headFile);

    	// afficher la liste<String> result
     	GridBagConstraints gbc_list = new GridBagConstraints();
    	gbc_list.insets = new Insets(5, 5, 5, 5);
    	gbc_list.gridx = 1;
    	gbc_list.gridy = 0;
     	gbc_list.anchor = GridBagConstraints.CENTER;

		// afficher le nom des mondes et la date de création
		for (int i = 0; i<listMap.length; i++) {
			try {
				String cheminPlayer = "C:\\Users\\Megaport\\eclipse-workspace\\Sky above prototype\\usr\\player\\"+listPlayers[i];
				// dummy Path ...
				Path cheminPlayerPath = Paths.get(cheminPlayer);
			    BufferedReader worldsParamFiles = Files.newBufferedReader(cheminPlayerPath, StandardCharsets.UTF_8);

			    Stream<String> listParamFile = worldsParamFiles.lines();
				Object [] ArrayParamFile = listParamFile.toArray();

				String[] firstLineParamFile = ArrayParamFile[0].toString().split(",");
			    nameWorlds[i] = firstLineParamFile[0];
			    
			    BasicFileAttributes attr = Files.readAttributes(cheminPlayerPath, BasicFileAttributes.class);
			    FileTime fileTime = attr.creationTime();
				WorldsListing[i] = nameWorlds[i]+"\r créé le "+fileTime.toString();
			} catch (IOException ex) { }
		}

	 	listFile.setListData(WorldsListing);
        scrollableList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollableList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        PanCharger.add(scrollableList, gbc_list);

    	// bouton charger
     	GridBagConstraints gbc_charger = new GridBagConstraints();
    	gbc_charger.insets = new Insets(0, 0, 5, 5);
    	gbc_charger.gridx = 1;
    	gbc_charger.gridy = 2;
     	gbc_charger.anchor = GridBagConstraints.SOUTHEAST;
    	charger.setFont(new Font("SansSerif", Font.PLAIN, 30));
	    charger.addActionListener(this);
   	    PanCharger.add(charger, gbc_charger);

		menu.PanelsContainer.add(PanCharger);
}

	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Retour" :
			menu.PanelsContainer.remove(PanCharger);
			menu.card.first(menu.PanelsContainer);
			break;
		case "Charger" :
			int selected = listFile.getSelectedIndex();
			selectedMap = "C:\\Users\\Megaport\\eclipse-workspace\\Sky above prototype\\usr\\map\\"+listMap[selected];
			selectedPlayer = "C:\\Users\\Megaport\\eclipse-workspace\\Sky above prototype\\usr\\player\\"+listMap[selected];
			nameWorld = LoadFile.nameWorlds[selected];
			try {
				loadWorld(selectedMap, selectedPlayer);
				SimplePlatformer simulation = new SimplePlatformer();
				simulation.run();
			} catch (IOException e1) {e1.printStackTrace();}
		}
	}

	public static void loadWorld(String cheminMap, String cheminPlayer) throws IOException {
		String[][] CSVmap = readCSV.loadFile(selectedMap);
		String[][] CSVplayer = readCSV.loadFile(selectedPlayer);
		String tempChromatine;

		// calculer et stocker le type, la size et l'ADN des zones
		for (int l = 0; l<4000; l++) {
			tempChromatine = CSVmap[0][l];

			//le type : les trois lettres du début
			typesZones[l] = tempChromatine.substring(0,3);

			// la taille de la zone : les trois lettres suivantes * 10
			sizesZones[l] = Integer.parseInt(tempChromatine.substring(3,6))*10;

			// la hauteur de la zone : les trois lettres suivantes * 10
			hauteurZones[l] = Integer.parseInt(tempChromatine.substring(6,9))*10;

			// l'adn : les 150 chiffres suivants
			adnZones[l] = tempChromatine.substring(6, 155);
		}

		// rechercher la zone et la position du player dans le CSV
		namePlayer = CSVplayer[1][0];
		zonePlayer = Integer.parseInt(CSVplayer[0][1]);

		// 0000(X)0000(Y)
		tableauXPlayer = CSVplayer[1][1].substring(0, 4);
		tableauYPlayer = CSVplayer[1][1].substring(4, 8);
		
		DrawMapMonde.imageIoWrite(selectedMap);
		SimplePlatformer.CurrentXTableauPlayer = Integer.parseInt(LoadFile.tableauXPlayer);
		ARNengine.induceARN(LoadFile.adnZones[zonePlayer]);
		createZoneBackgrounds.imageIoWrite();
	}
}