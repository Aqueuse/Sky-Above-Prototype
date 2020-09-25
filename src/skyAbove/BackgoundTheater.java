package skyAbove;

import java.io.IOException;
import org.dyn4j.geometry.Vector2;

/*
/////////// deplacement ou scrolling si on est sur la road ////////////////////
 Xmin (left) = 0  -  Xmax (right) = 27.5
 Ymin (up) = 0  -  Ymax (bottom) = 15.8

Comportement ROAD :
	Au centre de la zone, le joueur est translate normalement.
	Si le x du joueur atteint <10 ou >20 alors :
 		-  le currentBackground est élargi * 3 (un de plus à gauche
 	   	   et un de plus à droite).
	Si le joueur atteint la fin du premier ou le début du dernier
	tableau, le current background est encore réécrit (boucle),
	alors c'est le background qui est translate et le joueur est
	fixe à x=20 (defilement right) ou x=10 (defilement left).

Comportement LANDSCAPE :
	Le joueur est translate normalement. S'il atteint
 	x=0,4 ou x=27,5 ou y=0,4 ou y=15,8 alors : 
 		- tout le tableau shift (shift left ou shift droit)
 		- le joueur est translate à l'opposé
 */

public class BackgoundTheater {
	public static int incr = 0;
	static boolean ComportementLandscape = true;
	static boolean ComportementRoad = false;

	public static int dx1 = 0;
	public static int dx2 = 0;
	public static int fall=0;

	static double XplayerRelative = 0.0;
	static double YplayerRelative = 0.0;
	static int XtilePlayer;

	public static void chekContext() throws IOException {
		XplayerRelative = SimplePlatformer.basePl.getWorldCenter().x;
		YplayerRelative = SimplePlatformer.basePl.getWorldCenter().y;
		XtilePlayer = (int)Math.floor(XplayerRelative/2.7);
		
		if (SimplePlatformer.CurrentYTableauPlayer<9990) {
			ComportementLandscape = true;
			ComportementRoad = false;
		}

		if (SimplePlatformer.CurrentYTableauPlayer==9990) {
			if (CustomkeyListener.downPressed.get() &&
				SimplePlatformer.isOnGround.get() &&
				ComportementRoad==false) {
				CustomkeyListener.downPressed.set(false);
				ComportementLandscape = false;
				ComportementRoad = true;
				Platforms.reloadPlatforms();
				SimplePlatformer.basePl.translate(0, 1);

				/// cas particulier : si currentXTableauPlayer
				// est = 0, on garde le landscape
				if (SimplePlatformer.CurrentXTableauPlayer>0) {		
					createTableauBackground.wide();
					SimulationFrame.updateBackground();
					incr=1280;
				}
			}

			if (CustomkeyListener.upPressed.get() &&
				SimplePlatformer.isOnGround.get() &&
				ComportementLandscape==false) {
				CustomkeyListener.upPressed.set(false);
				ComportementLandscape = true;
				ComportementRoad = false;
				Platforms.reloadPlatforms();
				SimplePlatformer.basePl.translate(0, -1);
				SimplePlatformer.basePl.applyImpulse(new Vector2(0, 1));
				incr=0;
				createTableauBackground.tight();
				SimulationFrame.updateBackground();
			}
		}

		if (ComportementLandscape==true) ModeLandscape();
		if (ComportementRoad==true) ModeRoad();
	}

	public static void ModeLandscape() throws IOException {
		if (CustomkeyListener.leftPressed.get() && XplayerRelative>0.4) {
			SimplePlatformer.basePl.translate(-0.20/DrawImageOnBody.speedSprite,0);
			SimplePlatformer.basePl.applyImpulse(new Vector2(0,0.1));
		}

		if (CustomkeyListener.rightPressed.get() && XplayerRelative<27.5) {
			SimplePlatformer.basePl.translate(0.20/DrawImageOnBody.speedSprite,0);
			SimplePlatformer.basePl.applyImpulse(new Vector2(0,0.1));
		}

		if (SimplePlatformer.isOnGround.get() && CustomkeyListener.spacePressed.get()) {
			CustomkeyListener.spacePressed.set(false);
			SimplePlatformer.basePl.applyImpulse(new Vector2(0,-25));
		}

		// bottom
		if (YplayerRelative>15.8 && SimplePlatformer.CurrentYTableauPlayer>=0 && SimplePlatformer.CurrentYTableauPlayer<9990) {
			SimplePlatformer.CurrentYTableauPlayer = SimplePlatformer.CurrentYTableauPlayer+5;
			createTableauBackground.tight();
			SimulationFrame.updateBackground();
			Platforms.reloadPlatforms();
			SimplePlatformer.basePl.translate(0,-14);
			fall++;

			if (fall>=2) {
				SimplePlatformer.basePl.setEnabled(false);
				SimplePlatformer.basePl.setEnabled(true);
			}
		}

		/// top
		if (YplayerRelative < 0.4 && SimplePlatformer.CurrentYTableauPlayer>=0) {
			SimplePlatformer.CurrentYTableauPlayer = SimplePlatformer.CurrentYTableauPlayer-5;
			createTableauBackground.tight();
			SimulationFrame.updateBackground();
			Platforms.reloadPlatforms();
			SimplePlatformer.basePl.translate(0,14);
		}

		// left
		if (XplayerRelative<0.400) {
			if (SimplePlatformer.CurrentXTableauPlayer == 0) {
				SimplePlatformer.zonePlayerCurrent = SimplePlatformer.zonePlayerCurrent-1;
				if (SimplePlatformer.zonePlayerCurrent == -1) {
					SimplePlatformer.zonePlayerCurrent = 3999;
				}

				SimplePlatformer.currentADN = LoadFile.adnZones[SimplePlatformer.zonePlayerCurrent];
				SimplePlatformer.SizezoneCurrent = LoadFile.sizesZones[SimplePlatformer.zonePlayerCurrent];
				SimplePlatformer.CurrentXTableauPlayer = SimplePlatformer.SizezoneCurrent-10;
				ARNengine.induceARN(SimplePlatformer.currentADN);
				createTableauBackground.tight();
				DrawMapZone.imageIoWrite();
				SimulationFrame.updateBackground();
				Platforms.reloadPlatforms();
				SimplePlatformer.basePl.translate(26,0);
			}
			else {
				SimplePlatformer.CurrentXTableauPlayer = SimplePlatformer.CurrentXTableauPlayer-10;
				createTableauBackground.tight();
				SimulationFrame.updateBackground();
				Platforms.reloadPlatforms();
				SimplePlatformer.basePl.translate(26,0);
			}
		}

		// right
		if (XplayerRelative>27) {
			if (SimplePlatformer.CurrentXTableauPlayer == SimplePlatformer.SizezoneCurrent) {
				SimplePlatformer.zonePlayerCurrent = SimplePlatformer.zonePlayerCurrent+1;
				if (SimplePlatformer.zonePlayerCurrent == 4000) {
					SimplePlatformer.zonePlayerCurrent = 0;
				}
				SimplePlatformer.currentADN = LoadFile.adnZones[SimplePlatformer.zonePlayerCurrent];
				SimplePlatformer.SizezoneCurrent = LoadFile.sizesZones[SimplePlatformer.zonePlayerCurrent];
				SimplePlatformer.CurrentXTableauPlayer = 0;
				ARNengine.induceARN(SimplePlatformer.currentADN);
				createTableauBackground.tight();
				DrawMapZone.imageIoWrite();
				SimulationFrame.updateBackground();
				Platforms.reloadPlatforms();
				SimplePlatformer.basePl.translate(-26,0);
			}
			else {
				SimplePlatformer.CurrentXTableauPlayer = SimplePlatformer.CurrentXTableauPlayer+10;
				createTableauBackground.tight();
				SimulationFrame.updateBackground();
				Platforms.reloadPlatforms();
				SimplePlatformer.basePl.translate(-26,0);
			}
		}
	}

	public static void ModeRoad() throws IOException {
		if (SimplePlatformer.isOnGround.get() && CustomkeyListener.spacePressed.get()) {
			CustomkeyListener.spacePressed.set(false);
			SimplePlatformer.basePl.applyImpulse(new Vector2(0,-20));
		}

		if (CustomkeyListener.leftPressed.get()) {
			incr=incr-8;
			if (incr<=0) {
				SimplePlatformer.CurrentXTableauPlayer=SimplePlatformer.CurrentXTableauPlayer-10;
				createTableauBackground.wide();
				SimulationFrame.updateBackground();
				incr=1280;
			}
		}
		if (CustomkeyListener.rightPressed.get()) {
			incr=incr+8;
			if (incr>=2560) {
				SimplePlatformer.CurrentXTableauPlayer=SimplePlatformer.CurrentXTableauPlayer+10;
				createTableauBackground.wide();
				SimulationFrame.updateBackground();
				incr=1280;
			}
		}
	}
}