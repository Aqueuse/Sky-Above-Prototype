package skyAbove;

import org.dyn4j.geometry.Vector2;

public class Locate {
	public static int currentTileX;
	public static int currentTileY;
	
	public static void locatePlayer() {
		Vector2 contacts = ObjectsWorld.basePl.getWorldCenter();

		currentTileX = (int)Math.floor(contacts.x/4);
		currentTileY = (int)Math.floor(contacts.y/4);
	}
}