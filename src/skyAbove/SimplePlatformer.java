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
	
	/// some values for the climber
	int fall=0;
	boolean modeGrappin=false;
	boolean grappinHooked=false;
	double ropeSize = 3.0;
	static int direction=0;

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
/////// //  le menu du jeu (map, inventaire, save, etc) ///////////////////
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
////////////////////////////////////////////////////////////////////
		
///////////////////  le grappin  ////////////////////////////////////
		if (this.upPressed.get() && modeGrappin==false) {
			modeGrappin=true;
			this.upPressed.set(false);
			ObjectsWorld.basePl.setAsleep(true);
			ObjectsWorld.basePl.setActive(false);
			modeGrappinOn();
			}

		if (this.downPressed.get() && modeGrappin==true && ropeSize>=3.0) {
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
			direction = 0;
			connectGrapple(direction, 3.0);
		}

		if (this.rightPressed.get() && modeGrappin==true && grappinHooked == false
		    && this.world.containsBody(ObjectsWorld.accrocheRight)) {
			this.rightPressed.set(false);
			grappinHooked = true;
			direction = 1;
			connectGrapple(direction, 3.0);
		}

		// dangling - pendouillage
		if (this.leftPressed.get() && grappinHooked==true) {
			ObjectsWorld.basePl.applyImpulse(new Vector2(-0.07, 0));				
		}
		if (this.rightPressed.get() && grappinHooked==true) {
			ObjectsWorld.basePl.applyImpulse(new Vector2(0.07, 0));
		}
		
		// climb Up - climb Down
		if (this.upPressed.get() && grappinHooked==true) {
			this.upPressed.set(false);
			ropeSize -= 0.5;
			if (ropeSize>1) {
				if (this.world.containsJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheLeft, ObjectsWorld.basePl, ropeSize+0.5))) this.world.removeJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheLeft, ObjectsWorld.basePl, ropeSize+0.5));
				if (this.world.containsJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheRight, ObjectsWorld.basePl, ropeSize+0.5))) this.world.removeJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheRight, ObjectsWorld.basePl, ropeSize+0.5));
				connectGrapple(direction, ropeSize);
			}
			if (ropeSize==1 && Locate.locatePlayer()[1]>=1) goUp();
			if (ropeSize==1 && Locate.locatePlayer()[1]==0) {
				modeGrappinOff();
				ObjectsWorld.basePl.applyImpulse(new Vector2(0, -10));
			}
		}
		if (this.downPressed.get() && grappinHooked==true) {
			this.downPressed.set(false);
			ropeSize+=0.5;
			if (ropeSize<3.0 && ropeSize>0) { //climb Down
				if (this.world.containsJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheLeft, ObjectsWorld.basePl, ropeSize-0.5))) this.world.removeJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheLeft, ObjectsWorld.basePl, ropeSize-0.5));
				if (this.world.containsJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheRight, ObjectsWorld.basePl, ropeSize-0.5))) this.world.removeJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheRight, ObjectsWorld.basePl, ropeSize-0.5));
				connectGrapple(direction, ropeSize);
			}
			if (ropeSize==3.0) modeGrappinOff();
		}

///////////////////////////////////////////////////////////////////////////

/////////// deplacement entre les tableaux (bottom, top, left, right) /////
		if (ObjectsWorld.basePl.isInContact(ObjectsWorld.bottom) && CurrentYTableauPlayer>=0 && CurrentYTableauPlayer<9990) {
			this.world.removeBody(ObjectsWorld.basePl);
			this.world.removeBody(ObjectsWorld.plateforms);
			modeGrappinOff();
			
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
			modeGrappinOff();

			CurrentYTableauPlayer = CurrentYTableauPlayer-5;
			createZoneBackgrounds.imageIoWrite();
			SimulationFrame.updateBackground();
			ObjectsWorld.basePl.translate(0,20);
			Platforms.reloadPlatforms();
			this.world.addBody(ObjectsWorld.basePl);
			this.world.addBody(ObjectsWorld.plateforms);
		}
		
		if (ObjectsWorld.basePl.isInContact(ObjectsWorld.left)) {
			this.world.removeBody(ObjectsWorld.basePl);
			this.world.removeBody(ObjectsWorld.plateforms);
			modeGrappinOff();
			ObjectsWorld.basePl.setAsleep(true);

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
				ObjectsWorld.basePl.translate(38,0);
				this.world.addBody(ObjectsWorld.basePl);
				this.world.addBody(ObjectsWorld.plateforms);
				ObjectsWorld.basePl.setAsleep(false);
			}
			else {
				CurrentXTableauPlayer = CurrentXTableauPlayer-10;
				createZoneBackgrounds.imageIoWrite();
				SimulationFrame.updateBackground();
				ObjectsWorld.basePl.translate(38,0);
				Platforms.reloadPlatforms();
				this.world.addBody(ObjectsWorld.basePl);
				this.world.addBody(ObjectsWorld.plateforms);
				ObjectsWorld.basePl.setAsleep(false);
			}
		}

		if (ObjectsWorld.basePl.isInContact(ObjectsWorld.right)) {
			this.world.removeBody(ObjectsWorld.basePl);
			this.world.removeBody(ObjectsWorld.plateforms);
			modeGrappinOff();
			ObjectsWorld.basePl.setAsleep(true);


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
				ObjectsWorld.basePl.setAsleep(false);
			}
			else {
				CurrentXTableauPlayer = CurrentXTableauPlayer+10;
				createZoneBackgrounds.imageIoWrite();
				SimulationFrame.updateBackground();
				ObjectsWorld.basePl.translate(-35,0);
				Platforms.reloadPlatforms();
				this.world.addBody(ObjectsWorld.basePl);
				this.world.addBody(ObjectsWorld.plateforms);
				ObjectsWorld.basePl.setAsleep(false);
			}
		}
/////////////////////////////////////////////////////////////////////////
		
/////////////   deplacement du joueur au sein du tableau /////////////////
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

		super.update(g, elapsedTime);
	}
	
	public void modeGrappinOn() {
		int localCurrentX = Math.abs(Locate.locatePlayer()[0]);
		int localCurrentY = Math.abs(Locate.locatePlayer()[1]);

		if (localCurrentX>1 && localCurrentX<10) {
			if (Platforms.platformsArray[localCurrentX-1][localCurrentY]>0
			|| Platforms.platformsArray[localCurrentX][localCurrentY]>0) {
				this.world.addBody(ObjectsWorld.accrocheLeft);
				ObjectsWorld.accrocheLeft.translateToOrigin();
				ObjectsWorld.accrocheLeft.translate((localCurrentX*4), (localCurrentY*4)+0.5);
			}

			if (Platforms.platformsArray[localCurrentX][localCurrentY]>0
			|| Platforms.platformsArray[localCurrentX+1][localCurrentY]>0) {
				this.world.addBody(ObjectsWorld.accrocheRight);
				ObjectsWorld.accrocheRight.translateToOrigin();
				ObjectsWorld.accrocheRight.translate((localCurrentX*4)+4, (localCurrentY*4)+0.5);
			}
		}
	}

	public void modeGrappinOff() {
		if (this.world.containsBody(ObjectsWorld.accrocheLeft)) this.world.removeBody(ObjectsWorld.accrocheLeft);
		if (this.world.containsBody(ObjectsWorld.accrocheRight)) this.world.removeBody(ObjectsWorld.accrocheRight);
		if (this.world.containsJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheLeft, ObjectsWorld.basePl, 3.0))) this.world.removeJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheLeft, ObjectsWorld.basePl, 4.0));
		if (this.world.containsJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheRight, ObjectsWorld.basePl, 3.0))) this.world.removeJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheRight, ObjectsWorld.basePl, 4.0));

		ObjectsWorld.accrocheLeft.setColor(Color.darkGray);
		ObjectsWorld.accrocheRight.setColor(Color.darkGray);
		
		modeGrappin=false;
		grappinHooked=false;
		ropeSize = 3.0;
	}

	public void connectGrapple(int direction, double ropeSize) {
		if (direction == 0) {
			this.world.removeBody(ObjectsWorld.accrocheRight);
			ObjectsWorld.accrocheLeft.setColor(Color.gray);
			this.world.addJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheLeft, ObjectsWorld.basePl, ropeSize));

			ObjectsWorld.basePl.setActive(true);
			ObjectsWorld.basePl.setAsleep(false);
}
		if (direction == 1) {
			this.world.removeBody(ObjectsWorld.accrocheLeft);
			ObjectsWorld.accrocheRight.setColor(Color.gray);
			this.world.addJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheRight, ObjectsWorld.basePl, ropeSize));

			ObjectsWorld.basePl.setActive(true);
			ObjectsWorld.basePl.setAsleep(false);
		}
	}
	
	public void goUp() {
		// enlever le ropeJoint
		if (this.world.containsJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheLeft, ObjectsWorld.basePl, 3.0))) this.world.removeJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheLeft, ObjectsWorld.basePl, 4.0));
		if (this.world.containsJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheRight, ObjectsWorld.basePl, 3.0))) this.world.removeJoint(ObjectsWorld.addGrapple(ObjectsWorld.accrocheRight, ObjectsWorld.basePl, 4.0));
		
		//arrêter le joueur
		ObjectsWorld.basePl.setAsleep(true);

		// translate = deplacement relatif !
		if (direction==0) ObjectsWorld.basePl.translate(-1,-2.2);
		if (direction==1) ObjectsWorld.basePl.translate(1,-2.2);

		modeGrappinOff();
		ObjectsWorld.basePl.setAsleep(false);
	}
}