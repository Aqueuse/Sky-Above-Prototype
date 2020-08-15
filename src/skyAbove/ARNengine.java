package skyAbove;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Random;

// prend l'ADN de la zone ou se trouve le joueur
public class ARNengine {
	public static boolean ARNloaded = false;
	static String textureSet;
	static int sizeZone;
	static int niveauMax = LoadFile.hauteurZones[LoadFile.zonePlayer];
	static String parseARN;

	public static int[][] currentMatrice;
	public static int[][] reliefsMap;

	public static void induceARN(String currentADN) throws IOException {
		sizeZone = LoadFile.sizesZones[SimplePlatformer.zonePlayerCurrent];

		int facteurZone = 0;
		int intARN;
		String subARN = new String();
		String outputARN = new String();
		int tempARN;
		String parseARN = LoadFile.adnZones[LoadFile.zonePlayer];
		Random random = new Random();

		// creation de l'ARN à partir de l'ADN
		while (parseARN.length() < 10000) {
			for (int i = 0; i<parseARN.length(); i++) {
				subARN = parseARN.substring(i,i+1);
				intARN = Integer.parseInt(subARN);
				random.setSeed(intARN);

				tempARN = random.nextInt();
				subARN = Integer.toString(tempARN);
				outputARN = outputARN+subARN.substring(5);
			}
			parseARN = parseARN+outputARN;
		}

		// récupérer les informations du caraType pour le dessin
		String[][] caraTypes = readCSV.loadFile("ressources/caraTypes.csv");
		String typeZone = LoadFile.typesZones[LoadFile.zonePlayer].substring(0, 2);

		for (int ct=0; ct<caraTypes[0].length; ct++) {
			if (caraTypes[0][ct].equals(typeZone)) {
				facteurZone = Integer.parseInt(caraTypes[1][ct]);
				textureSet = caraTypes[2][ct];
			}
		}

		parseARN = parseARN.substring(0, sizeZone);

		// transfert vers la carte schématique pre-tracage
		int range = 2;
		int reliefsNumber = Integer.parseInt(parseARN.substring(0,range));
		if (reliefsNumber<10) {
			reliefsNumber=reliefsNumber+10;
		}
		
		String XcoordinatesStr;
		int Xcoordinates;
		double Ycoordinates;
		double tempXY;

		ArrayList<Double> reliefsArray = new ArrayList<Double>();

		for (int p=0; p<reliefsNumber; p++) {
			XcoordinatesStr = parseARN.substring(p*4,(p*4)+4);
			Xcoordinates = Integer.parseInt(XcoordinatesStr);

			if (Xcoordinates < sizeZone-1000) {
				Ycoordinates = Math.floor(Xcoordinates*facteurZone)/10000;
				tempXY = Xcoordinates+Ycoordinates;
				reliefsArray.add(tempXY);
			}
		}

		reliefsArray.sort(Comparator.naturalOrder());
		LinkedHashSet<Double> hashSet = new LinkedHashSet<Double>(reliefsArray);
		ArrayList<Double> reliefsFinal = new ArrayList<Double>(hashSet);

		// on configure la zone de façon a commencer et finir
		// au niveau de la mer (Y=0)
		reliefsFinal.set(0, 0.0);

		String endStr = String.valueOf(sizeZone-1);
		String TempEnd = endStr.concat(".0");
		double endZone = Double.valueOf(TempEnd);
		reliefsFinal.add(endZone);

		double reliefsDb;
		String tempStr;
		String[] tempDb;
		int Xcurrent;
		int Ycurrent;

		int lastX = 0;
		int lastY = 0;

		int deltaHauteur;
		int deltaSize;
		int trancheHauteur;
		int invTrancheHauteur;
		int rapportDelta = 0;

		int localARN;

		// pour chaque relief, pour chaque X, pour chaque Y
		currentMatrice = new int[sizeZone+10][9999];
		reliefsMap = new int[reliefsFinal.size()][2];

		for (int s=1; s<reliefsFinal.size(); s++) {
			reliefsDb = reliefsFinal.get(s);
			tempStr = String.valueOf(reliefsDb);
			tempDb = tempStr.split("\\.");
			Xcurrent = Integer.parseInt(tempDb[0]);
			Ycurrent = Integer.parseInt(tempDb[1]);

			//on sauve une version simplifee pour les fonctions
			//basiques, style creation de map, placement, etc
			reliefsMap[s][0] = Xcurrent;
			reliefsMap[s][1] = Ycurrent;

			deltaSize = Math.abs(Xcurrent-lastX);
			deltaHauteur = Math.abs(Ycurrent-lastY);
			rapportDelta = Math.abs(deltaHauteur/deltaSize);

			for (int l =0; l<deltaSize; l++) {
				if (Ycurrent < lastY) {  /* down */ 
					trancheHauteur = Math.abs(lastY-rapportDelta);
					invTrancheHauteur = 9999 - trancheHauteur;
				}
				else { // up
					trancheHauteur = Math.abs(lastY+rapportDelta);
					invTrancheHauteur = 9999 - trancheHauteur;
				}
				
				// remplissage final
				// landscape
				for (int i=invTrancheHauteur; i<9999; i++) {
					currentMatrice[lastX+l][i]=128;
				}
				// sky-empty
				for (int i=0; i<invTrancheHauteur; i++) {
					currentMatrice[lastX+l][i]=2304;
				}
				
				//le sommet de la tranche est retravaillé en fonction
				// du parseARN pour apporter de la variété dans les
				// reliefs

				// 19*128 = empty = 2432
				// 14*128 = down = 1792
				// 15*128 = up = 1920
				// 0*128 = flat = 0

				localARN = Integer.parseInt(parseARN.substring(lastX+l, lastX+l+1));
				if (localARN % 2 == 0) {
					currentMatrice[lastX+l][invTrancheHauteur]=1920;
				}
				else {
					currentMatrice[lastX+l][invTrancheHauteur]=1792;
				}
				lastY = trancheHauteur;
			}
			lastX = Xcurrent;
			lastY = Ycurrent;
		}

		DrawMapZone.imageIoWrite();
	}
}