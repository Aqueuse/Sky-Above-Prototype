package skyAbove;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.dyn4j.geometry.Vector2;

public class SimplePlatformer extends SimulationFrame {
	public static int zonePlayerCurrent;
	static int CurrentXTableauPlayer = Integer.parseInt(LoadFile.tableauXPlayer);
	static int CurrentYTableauPlayer = Integer.parseInt(LoadFile.tableauYPlayer);
	static int SizezoneCurrent;
	static String currentADN;

	private AtomicBoolean leftPressed = new AtomicBoolean(false);
	private AtomicBoolean rightPressed = new AtomicBoolean(false);
	private AtomicBoolean downPressed = new AtomicBoolean(false);
	private AtomicBoolean upPressed = new AtomicBoolean(false);
	private AtomicBoolean spacePressed = new AtomicBoolean(false);
	private AtomicBoolean MPressed = new AtomicBoolean(false);
	
	int fall=0;
	boolean modeGrappin=false;
	boolean grappinClimb=false;
	boolean grabLeft=false;
	boolean grabRight=false;
	
	// toggle the Game menu
	static boolean GameMenuLoaded = false;
	GameMenu menuGame = new GameMenu();
	
	public SimplePlatformer() throws IOException {
		super(LoadFile.nameWorld, 32);
		KeyListener listener = new CustomKeyListener();
		SimulationFrame.landscapeCanvas.addKeyListener(listener);
	}

	// Custom key adapter to listen for key events
	private class CustomKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					leftPressed.set(true);
					break;
				case KeyEvent.VK_RIGHT:
					rightPressed.set(true);
					break;
				case KeyEvent.VK_DOWN:
					downPressed.set(true);
					break;
				case KeyEvent.VK_UP:
					upPressed.set(true);
					break;
				case KeyEvent.VK_SPACE:
					spacePressed.set(true);
					break;
				case KeyEvent.VK_M:
					MPressed.set(true);
					break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					leftPressed.set(false);
					break;
				case KeyEvent.VK_RIGHT:
					rightPressed.set(false);
					break;
				case KeyEvent.VK_DOWN:
					downPressed.set(false);
					break;
				case KeyEvent.VK_UP:
					upPressed.set(false);
					break;
				case KeyEvent.VK_SPACE:
					spacePressed.set(false);
					break;
				case KeyEvent.VK_M:
					MPressed.set(false);
					break;
			}
		}
	}

	protected void initializeWorld() throws IOException {
		CurrentXTableauPlayer = Integer.parseInt(LoadFile.tableauXPlayer);
		CurrentYTableauPlayer = Integer.parseInt(LoadFile.tableauYPlayer);
		zonePlayerCurrent = LoadFile.zonePlayer;
		SizezoneCurrent =  LoadFile.sizesZones[zonePlayerCurrent];

		this.world.addBody(ObjectsWorld.left);
		this.world.addBody(ObjectsWorld.right);
		this.world.addBody(ObjectsWorld.top);
		this.world.addBody(ObjectsWorld.bottom);
		this.world.addBody(ObjectsWorld.plateforms);

		this.world.addBody(ObjectsWorld.basePl);
		this.world.addJoint(ObjectsWorld.movePl);
		ObjectsWorld.connectObjects();

		this.world.setGravity(new Vector2(0,20));

		Platforms.reloadPlatforms();
		SimulationFrame.updateBackground();
	}

	@Override
	protected void update(Graphics2D g, double elapsedTime) throws IOException {
		// afficher le menu du jeu (map, inventaire, save, etc)
		if (this.MPressed.get()) {
			if (GameMenuLoaded == false) {
				this.MPressed.set(false);
				try { menuGame.ShowGameMenu() ; } catch (Exception e2) { e2.printStackTrace(); }
				SimulationFrame.menuFrame.toFront();
			}
			else {
				this.MPressed.set(false);
				SimulationFrame.gameFrame.toFront();
				GameMenuLoaded = false;
			}
		}
		
		 //le grappin
		if (this.upPressed.get() && modeGrappin==false) {
			modeGrappin=true;
			this.upPressed.set(false);
			ObjectsWorld.basePl.setAsleep(true);
			ObjectsWorld.basePl.setActive(false);
			modeGrappinOn();
			}

		if (this.downPressed.get() && modeGrappin==true) {
			modeGrappin=false;
			this.downPressed.set(false);
			ObjectsWorld.basePl.clearAccumulatedForce();
			ObjectsWorld.basePl.setActive(true);
			ObjectsWorld.basePl.setAsleep(false);
			modeGrappinOff();
		}
		
		if (this.leftPressed.get() && modeGrappin==true && grappinClimb == false) {
			this.leftPressed.set(false);
			grabLeft = true;
			grabRight = false;
			grappinClimb = true;
			connectGrapple();
		}

		if (this.rightPressed.get() && modeGrappin==true && grappinClimb == false) {
			this.rightPressed.set(false);
			grabRight = true;
			grabLeft = false;
			grappinClimb = true;
			connectGrapple();
		}

		if (ObjectsWorld.basePl.isInContact(ObjectsWorld.bottom) && CurrentYTableauPlayer>=0 && CurrentYTableauPlayer<9990) {
			this.world.removeBody(ObjectsWorld.basePl);
			this.world.removeBody(ObjectsWorld.plateforms);
			CurrentYTableauPlayer = CurrentYTableauPlayer+5;
			createZoneBackgrounds.imageIoWrite();
			SimulationFrame.updateBackground();
			ObjectsWorld.basePl.translate(0,-20);
			Platforms.reloadPlatforms();
			this.world.addBody(ObjectsWorld.basePl);
			this.world.addBody(ObjectsWorld.plateforms);
			fall++;
			
			if (fall>=2) {
				ObjectsWorld.basePl.setAsleep(true);
				ObjectsWorld.basePl.setAsleep(false);
			}
		}

		if (ObjectsWorld.basePl.isInContact(ObjectsWorld.top) && CurrentYTableauPlayer>=0 && CurrentYTableauPlayer<9990) {
			this.world.removeBody(ObjectsWorld.basePl);
			this.world.removeBody(ObjectsWorld.plateforms);
			CurrentYTableauPlayer = CurrentYTableauPlayer-5;
			createZoneBackgrounds.imageIoWrite();
			SimulationFrame.updateBackground();
			ObjectsWorld.basePl.translate(0,20);
			Platforms.reloadPlatforms();
			this.world.addBody(ObjectsWorld.basePl);
			this.world.addBody(ObjectsWorld.plateforms);
		}
		
		if (ObjectsWorld.basePl.isInContact(ObjectsWorld.plateforms)) {
			fall=0;
			if (this.leftPressed.get() && modeGrappin==false) {
				ObjectsWorld.basePl.applyImpulse(new Vector2(-0.07, 0));
			}
			if (this.rightPressed.get() && modeGrappin==false) {
				ObjectsWorld.basePl.applyImpulse(new Vector2(0.07, 0));
			}

			if (this.spacePressed.get() && modeGrappin==false) {
				ObjectsWorld.basePl.applyImpulse(new Vector2(0, -10));
				this.spacePressed.set(false);
			}
		}

		if (ObjectsWorld.basePl.isInContact(ObjectsWorld.left)) {
			this.world.removeBody(ObjectsWorld.basePl);
			this.world.removeBody(ObjectsWorld.plateforms);

			if (CurrentXTableauPlayer == 0) {
				zonePlayerCurrent = zonePlayerCurrent-1;
				if (zonePlayerCurrent == 0) {
					zonePlayerCurrent = 3999;
				}

				currentADN = LoadFile.adnZones[zonePlayerCurrent];
				SizezoneCurrent = LoadFile.sizesZones[zonePlayerCurrent];
				CurrentXTableauPlayer = SizezoneCurrent-10;

				ARNengine.induceARN(currentADN);
				createZoneBackgrounds.imageIoWrite();
				DrawMapZone.imageIoWrite();
				Platforms.reloadPlatforms();
				SimulationFrame.updateBackground();
				ObjectsWorld.basePl.translate(35,0);
				this.world.addBody(ObjectsWorld.basePl);
				this.world.addBody(ObjectsWorld.plateforms);
			}
			else {
				CurrentXTableauPlayer = CurrentXTableauPlayer-10;
				createZoneBackgrounds.imageIoWrite();
				SimulationFrame.updateBackground();
				ObjectsWorld.basePl.translate(35,0);
				Platforms.reloadPlatforms();
				this.world.addBody(ObjectsWorld.basePl);
				this.world.addBody(ObjectsWorld.plateforms);
			}
		}

		if (ObjectsWorld.basePl.isInContact(ObjectsWorld.right)) {
			this.world.removeBody(ObjectsWorld.basePl);
			this.world.removeBody(ObjectsWorld.plateforms);

			if (CurrentXTableauPlayer == SizezoneCurrent) {
				zonePlayerCurrent = zonePlayerCurrent+1;
				if (zonePlayerCurrent == 4000) {
					zonePlayerCurrent = 0;
				}
				currentADN = LoadFile.adnZones[zonePlayerCurrent];
				SizezoneCurrent = LoadFile.sizesZones[zonePlayerCurrent];
				CurrentXTableauPlayer = 0;
				ARNengine.induceARN(currentADN);
				createZoneBackgrounds.imageIoWrite();
				DrawMapZone.imageIoWrite();
				SimulationFrame.updateBackground();
				ObjectsWorld.basePl.translate(-35,0);
				Platforms.reloadPlatforms();
				this.world.addBody(ObjectsWorld.basePl);
				this.world.addBody(ObjectsWorld.plateforms);
			}
			else {
				CurrentXTableauPlayer = CurrentXTableauPlayer+10;
				createZoneBackgrounds.imageIoWrite();
				SimulationFrame.updateBackground();
				ObjectsWorld.basePl.translate(-35,0);
				Platforms.reloadPlatforms();
				this.world.addBody(ObjectsWorld.basePl);
				this.world.addBody(ObjectsWorld.plateforms);
			}
		}

		super.update(g, elapsedTime);
	}
	
	public void modeGrappinOn() {
		// quand le joueur appuie sur la flèche du haut,
		// regarde sa position et s'il y a une plateforme
		// au dessus de sa tête (à sa gauche, à sa droite et au dessus de lui)

		// fait apparaitre des les accroches aux extrémités de ces plateformes.

		// si le joueur appuie sur down, fait disparaitre les accroches
		Locate.locatePlayer();

		if (Platforms.platformsArray[Locate.currentTileX-1][Locate.currentTileY]>0
		 || Platforms.platformsArray[Locate.currentTileX][Locate.currentTileY]>0) {
			this.world.addBody(ObjectsWorld.accrocheLeft);
			ObjectsWorld.accrocheLeft.translateToOrigin();
			ObjectsWorld.accrocheLeft.translate((Locate.currentTileX*4), (Locate.currentTileY*4)+0.5);
		}

		if (Platforms.platformsArray[Locate.currentTileX][Locate.currentTileY]>0
		 || Platforms.platformsArray[Locate.currentTileX+1][Locate.currentTileY]>0) {
			this.world.addBody(ObjectsWorld.accrocheRight);
			ObjectsWorld.accrocheRight.translateToOrigin();
			ObjectsWorld.accrocheRight.translate((Locate.currentTileX*4)+4, (Locate.currentTileY*4)+0.5);
		}

		// par défaut, selectionne l'accroche gauche de la plateforme située
		// au dessus à gauche
		
		if (this.world.containsBody(ObjectsWorld.accrocheLeft) &&
			this.world.containsBody(ObjectsWorld.accrocheRight)) {
			ObjectsWorld.accrocheLeft.setColor(Color.gray);	
		}
		
		else {
			ObjectsWorld.accrocheRight.setColor(Color.gray);			
		}

		// si le joueur appuie alors sur gauche
		// selectionne l'accroche et selectionne celle de gauche
		// a la place, puis celle plus à gauche, etc, etc 

		//même chose a droite
		
// si le joueur appuie sur espace, deploie le grapin et fait monter
// légèrement le joueur. Appuyer plusieurs fois sur espace pour faire
// monter le joueur petit a petit
	// le grapin diminue section par section

// si le joueur appuie vers le bas, le grapin reaugmente section par section
	// si le grappin est au maximum et que le joueur rappuie sur bas
	// fait disparaitre le grappin et les accroches

// si aucune plateforme ne se trouve à côté de l'accroche du grappin
	// quand il ne reste plus qu'une section au grappin, le dernier
	// appui sur espace déplace le joueur sur la plateforme de l'accroche.

	}

	public void modeGrappinOff() {
		if (this.world.containsBody(ObjectsWorld.accrocheLeft)) this.world.removeBody(ObjectsWorld.accrocheLeft);
		if (this.world.containsBody(ObjectsWorld.accrocheRight)) this.world.removeBody(ObjectsWorld.accrocheRight);
//		if (this.world.containsJoint(ropeJointLeft) this.world.removeJoint(ObjectsWorld.ropeJointLeft);
//		if (this.world.containsJoint(ropeJointRight) this.world.removeJoint(ObjectsWorld.ropeJointRight);
		
		ObjectsWorld.accrocheLeft.setColor(Color.darkGray);
		ObjectsWorld.accrocheRight.setColor(Color.darkGray);

		ObjectsWorld.basePl.setActive(true);
		ObjectsWorld.basePl.setAsleep(false);

		grabLeft=false;
		grabRight=false;
		grappinClimb=false;
	}

	public void connectGrapple() {
		if (grabLeft == true) {
			this.world.removeBody(ObjectsWorld.accrocheRight);
			ObjectsWorld.accrocheLeft.setColor(Color.gray);

			this.world.addJoint(ObjectsWorld.connectGrapple(ObjectsWorld.accrocheLeft, ObjectsWorld.basePl));
			
			ObjectsWorld.basePl.setActive(true);
			ObjectsWorld.basePl.setAsleep(false);

}
		if (grabRight == true) {
			this.world.removeBody(ObjectsWorld.accrocheLeft);
			ObjectsWorld.accrocheRight.setColor(Color.gray);

			this.world.addJoint(ObjectsWorld.connectGrapple(ObjectsWorld.accrocheRight, ObjectsWorld.basePl));

			ObjectsWorld.basePl.setActive(true);
			ObjectsWorld.basePl.setAsleep(false);
			
		}
	}
}