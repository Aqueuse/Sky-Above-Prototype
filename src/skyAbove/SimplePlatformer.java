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
	boolean grappinHooked=false;

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
		
///////////////////  le grappin  ////////////////////////////////////
		if (this.upPressed.get() && modeGrappin==false) {
			modeGrappin=true;
			this.upPressed.set(false);
			ObjectsWorld.basePl.setAsleep(true);
			ObjectsWorld.basePl.setActive(false);
			modeGrappinOn();
			}

		if (this.downPressed.get() && modeGrappin==true) {
			this.downPressed.set(false);
			ObjectsWorld.basePl.clearAccumulatedForce();
			ObjectsWorld.basePl.setActive(true);
			ObjectsWorld.basePl.setAsleep(false);
			modeGrappinOff();
		}
		
		// grab the hook
		if (this.leftPressed.get() && modeGrappin==true && grappinHooked == false
			&& this.world.containsBody(ObjectsWorld.accrocheLeft)) {
			this.leftPressed.set(false);
			grappinHooked = true;
			connectGrapple(0);
		}

		if (this.rightPressed.get() && modeGrappin==true && grappinHooked == false
		    && this.world.containsBody(ObjectsWorld.accrocheRight)) {
			this.rightPressed.set(false);
			grappinHooked = true;
			connectGrapple(1);
		}

		if (this.leftPressed.get() && grappinHooked==true) {
			ObjectsWorld.basePl.applyImpulse(new Vector2(-0.07, 0));				
		}
		if (this.rightPressed.get() && grappinHooked==true) {
			ObjectsWorld.basePl.applyImpulse(new Vector2(0.07, 0));
		}
		if (this.upPressed.get() && grappinHooked==true) {
			ObjectsWorld.basePl.applyImpulse(new Vector2(0.07, 0));
		}
		
///////////////////////////////////////////////////////////////////////////
		
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
			if (this.leftPressed.get() && modeGrappin== false && grappinHooked==false) {
				ObjectsWorld.basePl.applyImpulse(new Vector2(-0.07, 0));
			}
			if (this.rightPressed.get() && modeGrappin==false && grappinHooked==false) {
				ObjectsWorld.basePl.applyImpulse(new Vector2(0.07, 0));
			}

			if (this.spacePressed.get() && modeGrappin==false && grappinHooked==false) {
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

		if (Platforms.platformsArray[Math.abs(Locate.currentTileX-1)][Math.abs(Locate.currentTileY)]>0
		 || Platforms.platformsArray[Locate.currentTileX][Locate.currentTileY]>0) {
			this.world.addBody(ObjectsWorld.accrocheLeft);
			ObjectsWorld.accrocheLeft.translateToOrigin();
			ObjectsWorld.accrocheLeft.translate((Locate.currentTileX*4), (Locate.currentTileY*4)+0.5);
		}

		if (Platforms.platformsArray[Math.abs(Locate.currentTileX)][Math.abs(Locate.currentTileY)]>0
		 || Platforms.platformsArray[Locate.currentTileX+1][Locate.currentTileY]>0) {
			this.world.addBody(ObjectsWorld.accrocheRight);
			ObjectsWorld.accrocheRight.translateToOrigin();
			ObjectsWorld.accrocheRight.translate((Locate.currentTileX*4)+4, (Locate.currentTileY*4)+0.5);
		}

		// si le joueur appuie alors sur gauche
		// selectionne l'accroche de gauche et fait disparaitre l'autre
		// (même chose a droite)
		
		// si le joueur appuie dans la direction opposée du déploiement
		// du grappin, provoque une légère impulsion pour l'aider

		// si le joueur appuie sur up, fait monter légèrement le joueur
		// en diminuant la longueur de la corde.
		
		// quand la corde est assez courte, un dernier appuie fait monter
		// le joueur sur la plateforme où est accroché le grappin

		// si le joueur appuie vers le bas, le grapin reaugmente section par section
		// si le grappin est au maximum et que le joueur appuie encore sur bas
		// fait disparaitre le grappin et les accroches (retour au comportement initial)

	}

	public void modeGrappinOff() {
		if (this.world.containsBody(ObjectsWorld.accrocheLeft)) this.world.removeBody(ObjectsWorld.accrocheLeft);
		if (this.world.containsBody(ObjectsWorld.accrocheRight)) this.world.removeBody(ObjectsWorld.accrocheRight);

		ObjectsWorld.accrocheLeft.setColor(Color.darkGray);
		ObjectsWorld.accrocheRight.setColor(Color.darkGray);

		ObjectsWorld.basePl.setActive(true);
		ObjectsWorld.basePl.setAsleep(false);
		
		modeGrappin=false;
		grappinHooked=false;
	}

	public void connectGrapple(int direction) {
		if (direction == 0) {
			this.world.removeBody(ObjectsWorld.accrocheRight);
			ObjectsWorld.accrocheLeft.setColor(Color.gray);

			this.world.addJoint(ObjectsWorld.connectGrapple(ObjectsWorld.accrocheLeft, ObjectsWorld.basePl));
			
			ObjectsWorld.basePl.setActive(true);
			ObjectsWorld.basePl.setAsleep(false);

}
		if (direction == 1) {
			this.world.removeBody(ObjectsWorld.accrocheLeft);
			ObjectsWorld.accrocheRight.setColor(Color.gray);

			this.world.addJoint(ObjectsWorld.connectGrapple(ObjectsWorld.accrocheRight, ObjectsWorld.basePl));

			ObjectsWorld.basePl.setActive(true);
			ObjectsWorld.basePl.setAsleep(false);
		}
	}
}