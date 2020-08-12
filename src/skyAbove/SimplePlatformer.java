package skyAbove;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

//import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Rectangle;
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
//		this.world.addBody(ObjectsWorld.plateforms);

		this.world.addBody(ObjectsWorld.bike);
		this.world.addJoint(ObjectsWorld.movePl);
		ObjectsWorld.connectObjects();

		this.world.setGravity(new Vector2(0,20));

		drawLandscape();
		SimulationFrame.updateBackground();
	}

	public void drawLandscape() {
//		double borneLeft = 0;
		double borneRight = 42; // LA reponse, of course
//		double borneTop = 0;
//		double borneBottom = 22.8;
//		double middleX = 21;
		double middleY = 11.4;

		Rectangle plat1 = new Rectangle(5,1);
		ObjectsWorld.plateforms.addFixture(plat1);
		ObjectsWorld.plateforms.setColor(Color.darkGray);
		plat1.translate(borneRight, middleY);

		//		desX = currentMatrice[(currentTableauX+posX)/128][(currentTableauY+posY)/128];
	}	

	@Override
	protected void update(Graphics2D g, double elapsedTime) throws IOException {
		SimulationFrame.messageLabel1 = String.valueOf(CurrentXTableauPlayer);		
		SimulationFrame.messageLabel2 = String.valueOf(CurrentYTableauPlayer);
		SimulationFrame.messageLabel3 = zonePlayerCurrent;
		SimulationFrame.messageLabel4 = SizezoneCurrent;

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
		
		if (ObjectsWorld.bike.isInContact(ObjectsWorld.bottom)) {
			if (this.leftPressed.get()) {
				ObjectsWorld.bike.applyImpulse(new Vector2(-0.05, 0));
			}
			if (this.rightPressed.get()) {
				ObjectsWorld.bike.applyImpulse(new Vector2(0.05, 0));
			}
			if (this.downPressed.get() && CurrentYTableauPlayer>=0 && CurrentYTableauPlayer<9990) {
				this.world.removeBody(ObjectsWorld.bike);
				CurrentYTableauPlayer = CurrentYTableauPlayer+10;
				createZoneBackgrounds.imageIoWrite();
				SimulationFrame.updateBackground();
				//ObjectsWorld.bike.translate(0,-10);
				this.world.addBody(ObjectsWorld.bike);
			}
			if (this.upPressed.get()) {
			}
			if (this.spacePressed.get()) {
				ObjectsWorld.bike.applyImpulse(new Vector2(0, -3));
				this.spacePressed.set(false);
			}
		}

		if (ObjectsWorld.bike.isInContact(ObjectsWorld.left)) {
			this.world.removeBody(ObjectsWorld.bike);
			
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
				SimulationFrame.updateBackground();
				ObjectsWorld.bike.translate(35,0);
				this.world.addBody(ObjectsWorld.bike);
			}
			else {
				CurrentXTableauPlayer = CurrentXTableauPlayer-10;
				createZoneBackgrounds.imageIoWrite();
				SimulationFrame.updateBackground();
				ObjectsWorld.bike.translate(35,0);
				this.world.addBody(ObjectsWorld.bike);
			}
		}

		if (ObjectsWorld.bike.isInContact(ObjectsWorld.right)) {
			this.world.removeBody(ObjectsWorld.bike);

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
				ObjectsWorld.bike.translate(-35,0);
				this.world.addBody(ObjectsWorld.bike);
			}
			else {
				CurrentXTableauPlayer = CurrentXTableauPlayer+10;
				createZoneBackgrounds.imageIoWrite();
				SimulationFrame.updateBackground();
				ObjectsWorld.bike.translate(-35,0);
				this.world.addBody(ObjectsWorld.bike);
			}
		}
		
		if (ObjectsWorld.bike.isInContact(ObjectsWorld.top)) {
		}
		if (ObjectsWorld.bike.isInContact(ObjectsWorld.bottom)) {
		}

		super.update(g, elapsedTime);
	}
}