package skyAbove;

import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

/*
 Generate a random world with 520 types of landscapes possibles
 et entre 3 et 7 oceans de 42000 à 100 000 nums de large
 et avec 156 types dédiés
 */

public class generateWorld {
	static String cheminNewWorld = new String();
	static String cheminNewPlayer = new String();

	public static void generate() throws IOException {
		int randBrut;
		String randString = "0";
		String chromatine = "0";
		String[] chromatineArray = new String[4000];
		Random randGen = new SecureRandom();
		int chromaLength = 0;

		for (int a = 0; a<799999; a++) {
			while (chromaLength < 799999) {
				randBrut = Math.abs(randGen.nextInt());
				randString = Integer.toString(randBrut);

				chromatine = chromatine.concat(randString);
				chromaLength = chromatine.length();
			}
		}
		chromatine = chromatine.substring(0, 799999);
		System.out.println("chromatines created");

		int rangeFirst = 0;
		int rangeLast = 199;

		for (int cl = 0; cl<=3999; cl++) {
			chromatineArray[cl] = chromatine.substring(rangeFirst, rangeLast);
			rangeFirst = 200 + rangeFirst;
			rangeLast = 200 + rangeLast;
		}

		// replace 3 first number by letters of types.csv
		String[][] CSVtypes = readCSV.loadFile("ressources/types.csv");
		String[][] CSVoceans = readCSV.loadFile("ressources/oceans.csv");
		String tempType;

		for (int rw = 0; rw<=3999; rw++) {
			int typeInFile = Integer.parseInt(chromatineArray[rw].substring(0, 2));
			tempType = CSVtypes[0][typeInFile];

			// convert the given string to character array
			char[] chromaChars = chromatineArray[rw].toCharArray();

			// then replace the first numbers by the type letters
			chromaChars[0] = tempType.substring(0).charAt(0);
			chromaChars[1] = tempType.substring(1).charAt(0);
			chromaChars[2] = tempType.substring(0).charAt(0);

			// then reconvert to give the last result to CSV
			chromatineArray[rw] = String.valueOf(chromaChars);
		}
		
		// then generateOceans : create big zones of water
		/// entre 4 oceans, entre 4 et 10 zones chacun
		// and replace the letters by random letters of oceans.csv
		int first = 0;
		int oceansSize;
		int randOceanType;
		String tempOceanType;
		for (int rwo = 0; rwo<=3999; rwo+=999) {
			oceansSize = 200+randGen.nextInt(200);
			for (int ocr=0; ocr<=oceansSize; ocr++) {
				// on récupère le type aléatoire d'océan
				randOceanType = randGen.nextInt(199);
				tempOceanType = CSVoceans[0][randOceanType];

				// on convertit la ligne de la zone en caractères
				char[] chromaCharsOcean = chromatineArray[first].toCharArray();

				// then replace the first numbers by the type letters
				chromaCharsOcean[0] = tempOceanType.substring(0).charAt(0);
				chromaCharsOcean[1] = tempOceanType.substring(1).charAt(0);
				chromaCharsOcean[2] = tempOceanType.substring(0).charAt(0);

				// change the size for the max : 9999
				chromaCharsOcean[3] = "9".charAt(0);
				chromaCharsOcean[4] = "9".charAt(0);
				chromaCharsOcean[5] = "0".charAt(0);
				
				// change the altitude for the minimum
				chromaCharsOcean[6] = "1".charAt(0);
				chromaCharsOcean[7] = "0".charAt(0);
				chromaCharsOcean[8] = "0".charAt(0);

				chromatineArray[first] = String.valueOf(chromaCharsOcean);
				first +=1;
			}
			first = rwo;
		}
		System.out.println("oceans created");

		// le code de génération du CSV //

		// on attribue un numéro de série unique à chaque monde créé
		double serialNumberWorld = Math.random() * 10;
		String doubleAsString = String.valueOf(serialNumberWorld);
		String indexInt = doubleAsString.substring(2);
		cheminNewWorld = "usr/map/" + indexInt + ".csv";
		FileWriter arrayYCarte = new FileWriter(cheminNewWorld);

		cheminNewPlayer = "usr/player/" + indexInt + ".csv";
		FileWriter arrayPlayer = new FileWriter(cheminNewPlayer);

		for (int i = 0; i < 4000; i++) {
			// concatener les donnees de la map et les
			// enregister dans un fichier CSV (usr/map/)
			arrayYCarte.append(chromatineArray[i]+"\n");
		}
		arrayYCarte.close();

		// sauver le nom du monde, le nom du joueur par défaut
		// et la position initiale du joueur (index zone, coordonnéesXY)
		// dans un CSV (usr/player/)
		arrayPlayer.append(createNewLandscape.nameWorld.getText()+ "," + "player1" + "\n");
		arrayPlayer.append("0000,00000000\n");
		arrayPlayer.append("0000,00000000");
		arrayPlayer.close();

		// on met à jour la sélection pour lancer le monde
		LoadFile.selectedMap = "C:\\Users\\Megaport\\eclipse-workspace\\Sky above prototype\\usr\\map\\" + indexInt + ".csv";
		LoadFile.selectedPlayer = "C:\\Users\\Megaport\\eclipse-workspace\\Sky above prototype\\usr\\player\\"+ indexInt + ".csv";

		// on dessine et on stocke la carte
		DrawMapMonde.imageIoWrite(LoadFile.selectedMap);
		createNewLandscape.ADNgen = false;
	}
}