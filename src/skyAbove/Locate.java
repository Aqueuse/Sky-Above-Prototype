package skyAbove;

import org.dyn4j.geometry.Vector2;

public class Locate {
	public static double Xplayer;
	public static double Yplayer;
	public static int[] locatePlayer() {
		int[] currentTile = new int[2];
		Vector2 contacts = ObjectsWorld.basePl.getWorldCenter();

		currentTile[0] = (int)Math.floor(contacts.x/4);
		currentTile[1] = (int)Math.floor(contacts.y/4);
		return currentTile;
	}
	
	public static void simpleLocate() { //for very simple usages
		Vector2 contacts = ObjectsWorld.basePl.getWorldCenter();

		Xplayer = (int)Math.floor(contacts.x/4);
		Yplayer = (int)Math.floor(contacts.y/4);
	}
}