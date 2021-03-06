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
	static int sizeZone = 0;
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

		// creation de l'ARN a partir de l'ADN
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

		// r�cup�rer les informations du caraType pour le dessin
		String[][] caraTypes = readCSV.loadFile("ressources/caraTypes.csv");
		String typeZone = LoadFile.typesZones[LoadFile.zonePlayer].substring(0, 2);

		for (int ct=0; ct<caraTypes[0].length; ct++) {
			if (caraTypes[0][ct].equals(typeZone)) {
				facteurZone = Integer.parseInt(caraTypes[1][ct]);
				textureSet = caraTypes[2][ct];
			}
		}

		parseARN = parseARN.substring(0, sizeZone);

		// transfert vers la carte schematique pre-tracage
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

		// on configure la zone de fa�on a commencer et finir
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
		boolean down;

		int localARN;

		// pour chaque relief, pour chaque X, pour chaque Y
		currentMatrice = new int[sizeZone+10][10000];
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
					down=true;
				}
				else { // up
					trancheHauteur = Math.abs(lastY+rapportDelta);
					invTrancheHauteur = 9999 - trancheHauteur;
					down=false;
				}

				// remplissage final
				String tranche = String.valueOf(trancheHauteur);
				String trancheARN="";
				
				// on cr�e un ARNvertical pour chaque tranche, afin de
				//pouvoir distribuer les platformes et autres bonus ou
				//objets du jeu
				while (trancheARN.length() < trancheHauteur) {
					for (int i = 0; i<trancheARN.length(); i++) {
						subARN = tranche.substring(i,i+1);
						intARN = Integer.parseInt(subARN);
						random.setSeed(intARN);

						tempARN = random.nextInt();
						subARN = Integer.toString(tempARN);
						outputARN = outputARN+subARN.substring(5);
					}
					trancheARN = trancheARN+outputARN;
				}

				trancheARN = trancheARN.substring(0, trancheHauteur);
				int localVerticalARN;
				int incrY=0;

				// landscape
				for (int i=invTrancheHauteur; i<9999; i++) {
					localVerticalARN=Integer.parseInt(trancheARN.substring(incrY, incrY+1));
					incrY+=1;
					if (localVerticalARN%2 == 0) {
						currentMatrice[lastX+l][i]=40; // tile nu
					}
					else {
						currentMatrice[lastX+l][i]=10; //plateforme flat
					}
				}
				// sky-empty
				for (int i=0; i<invTrancheHauteur; i++) {
					currentMatrice[lastX+l][i]=0; //empty
				}

				//le sommet de la tranche est retravaill� en fonction
				// du parseARN pour apporter de la vari�t� dans les
				// reliefs

				// empty = 0
				// flat = 10
				// down = 20
				// up = 30
				// tile nu = 40

				localARN = Integer.parseInt(parseARN.substring(lastX+l, lastX+l+1));
				if (localARN % 2 == 0) {
					currentMatrice[lastX+l][invTrancheHauteur]=0;
				}
				else {
					// on change le tile selon qu'on monte ou descende
					if (down==true) {
						currentMatrice[lastX+l][invTrancheHauteur]=20; //down
					}
					else {
						currentMatrice[lastX+l][invTrancheHauteur]=30; // up
					}
				}
				lastY = trancheHauteur;
			}

			lastX = Xcurrent;
			lastY = Ycurrent;
		}

		DrawMapZone.imageIoWrite();
	}
}