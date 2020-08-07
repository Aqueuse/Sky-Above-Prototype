package skyAbove;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

////																////
////       Generic Class for Reading CSV and doing it a automagic   ////
////       				  array of arrays B|						////
////	     (first generic class ever, be nice and proud)			////
////

public class readCSV {
	static int split;
	static Stream<String> lines;
	static String line;
	static int FileLength;
	static String [][] genericArray = new String [split] [FileLength];		

	// on récupère le chemin du fichier de façon absolue (String)
	// style "C:\\Users\\Megaport\\blablabla\\fichier.csv"
	public static String [][] loadFile(String cheminString) throws IOException {
		// dummy Path ... (abruti !)
		Path chemin = Paths.get(cheminString);
		BufferedReader SplitCSV = Files.newBufferedReader(chemin, StandardCharsets.UTF_8);

		lines = SplitCSV.lines();
		Object [] ArrayLines = lines.toArray();
		
		// tester s'il y a une virgule, sinon ne créer qu'une seule colonne
		String testSplit = ArrayLines[0].toString();
		if (testSplit.contains(",")) {
			String[] currentLine = ArrayLines[0].toString().split(",");

			FileLength = ArrayLines.length;
			split = currentLine.length;
			// create two dimensionnal array B| [colonnes] [cellules]
			genericArray = new String [split] [FileLength];		

			for (int l = 0; l<ArrayLines.length; l++) {
				currentLine = ArrayLines[l].toString().split(",");
				for (int s = 0; s<split; s++) {
					if (currentLine[s] == null) {
						genericArray[s][l] = currentLine[s-1];
					}
					else {
						genericArray[s][l] = currentLine[s];					
					}
				}
			}
		}
		// version simplifiée, pas de split donc une seule colonne
		else {
			FileLength = ArrayLines.length;
			// create two dimensionnal array B| [colonnes] [cellules]
			genericArray = new String [1] [FileLength];
			String currentLine;

			for (int u = 0; u<ArrayLines.length; u++) {
				currentLine = ArrayLines[u].toString();
				genericArray[0][u] = currentLine;
			}
		}
		return genericArray;
	}
}