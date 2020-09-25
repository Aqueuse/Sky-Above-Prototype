package skyAbove;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.imageio.ImageIO;

import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.TimeStep;
import org.dyn4j.dynamics.contact.Contact;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.dynamics.joint.MotorJoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.ContactCollisionData;
import org.dyn4j.world.PhysicsWorld;
import org.dyn4j.world.listener.ContactListenerAdapter;
import org.dyn4j.world.listener.StepListenerAdapter;

public class SimplePlatformer extends SimulationFrame {
	static DrawImageOnBody basePl = new DrawImageOnBody();
	static SimulationBody plateforms = new SimulationBody();
	static final AtomicBoolean isOnGround = new AtomicBoolean(false);
	static final Object FLOOR_BODY = new Object();
	static boolean left=false;

	static int zonePlayerCurrent;
	static int CurrentXTableauPlayer;
	static int CurrentYTableauPlayer;
	static int SizezoneCurrent;
	static String currentADN;

	// toggle the Game menu
	static boolean GameMenuLoaded = false;
	GameMenu menuGame = new GameMenu();

	// les accroches pour l'escalade
	static Rectangle accroche = new Rectangle(0.5,1);
	static SimulationBody accrocheLeft = new SimulationBody();
	static SimulationBody accrocheRight = new SimulationBody();

	public SimplePlatformer(String name, double scale) throws IOException {
		super("mini platformer test", 45.0);
		KeyListener listener = new CustomkeyListener();
		SimulationFrame.landscapeCanvas.addKeyListener(listener);
	}
	@Override
	protected void initializeWorld() throws IOException {		
		// chargement des sprites
		try {DrawImageOnBody.walkLeft = ImageIO.read(DrawImageOnBody.walk_left);} catch (IOException e) {}
		try {DrawImageOnBody.walkRight = ImageIO.read(DrawImageOnBody.walk_right);} catch (IOException e) {}
		try {DrawImageOnBody.idleLeft = ImageIO.read(DrawImageOnBody.idle_left);} catch (IOException e) {}
		try {DrawImageOnBody.idleRight = ImageIO.read(DrawImageOnBody.idle_right);} catch (IOException e) {}

		Settings settings = new Settings();
		settings.setStepFrequency(0.05);
		world.setSettings(settings);
		plateforms.setMass(MassType.INFINITE);
		plateforms.setUserData(FLOOR_BODY);
		Platforms.reloadPlatforms();
		
		accrocheLeft.addFixture(accroche);
		accrocheRight.addFixture(accroche);
		accrocheLeft.setColor(Color.DARK_GRAY);
		accrocheRight.setColor(Color.DARK_GRAY);

		basePl.addFixture(Geometry.createRectangle(1,2), 1, 1, 0.0 );		
		basePl.setMass(MassType.FIXED_ANGULAR_VELOCITY);
		basePl.createAABB();
		basePl.translate(LoadFile.currentRelXPlayer, LoadFile.currentRelYPlayer);

		MotorJoint<SimulationBody> movePl = new MotorJoint<SimulationBody>(basePl, plateforms);
		movePl.setCollisionAllowed(true);

		SimulationFrame.world.setGravity(new Vector2(0,20));
		SimulationFrame.world.addBody(basePl);
		SimulationFrame.world.addBody(plateforms);
		SimulationFrame.world.addJoint(movePl);

		createTableauBackground.tight();
		SimulationFrame.updateBackground();
		BackgoundTheater.chekContext();
		
		SimulationFrame.world.addStepListener(new StepListenerAdapter<SimulationBody>() {
			@Override
			public void begin(TimeStep step, PhysicsWorld<SimulationBody, ?> world) {
				super.begin(step, world);
				// at the beginning of each world step, check if the body is in
				// contact with any of the floor bodies
				boolean isGround = false;

				java.util.List<SimulationBody> bodies = world.getInContactBodies(basePl, false);
				for (int i = 0; i < bodies.size(); i++) {
					if (bodies.get(i).getUserData() == FLOOR_BODY) {
						isGround = true;
						break;
					}
				}
				if (!isGround) {
					isOnGround.set(false);
					basePl.applyImpulse(new Vector2(0,0.1));
				}

				try {BackgoundTheater.chekContext();} catch (IOException e) {}

				///// animation des sprites //////
				DrawImageOnBody.rotate+=1;

				if (DrawImageOnBody.rotate==DrawImageOnBody.speedSprite) {
					DrawImageOnBody.srcSprite+=48;
					DrawImageOnBody.rotate=0;
				}
				
				if (DrawImageOnBody.srcSprite>48*DrawImageOnBody.spritesQuantity) {
					DrawImageOnBody.srcSprite=0;
				}
				
				if (CustomkeyListener.leftPressed.get()) {
					DrawImageOnBody.spritesQuantity=5;
					DrawImageOnBody.SpriteSelect=-2;
					left=true;
				}
				if (CustomkeyListener.rightPressed.get()) {
					DrawImageOnBody.spritesQuantity=5;
					DrawImageOnBody.SpriteSelect=2;
					left=false;
				}
				if (!CustomkeyListener.leftPressed.get() && !CustomkeyListener.rightPressed.get() && !CustomkeyListener.spacePressed.get()) {
					DrawImageOnBody.spritesQuantity=3;
					if (left==true) {
						DrawImageOnBody.SpriteSelect=-1;
					}
					if (left==false) {
						DrawImageOnBody.SpriteSelect=1;
					}
				}
				////////////////////////////////////////////////////////////////////

				///////////  le menu du jeu (map, inventaire, save, etc) ///////////////////
				if (CustomkeyListener.MPressed.get()) {
					CustomkeyListener.MPressed.set(false);
					if (GameMenuLoaded == false) {
						try { menuGame.ShowGameMenu() ; } catch (Exception e2) { e2.printStackTrace(); }
						SimulationFrame.menuFrame.toFront();
						GameMenuLoaded=true;
					}
					else {
						SimulationFrame.gameFrame.toFront();
						GameMenuLoaded = false;
					}
				}
				////////////////////////////////////////////////////////////////////
			}
		});

		// then, when a contact is created between two bodies, check if the bodies
		// are floor and wheel, if so, then set the color and flag
		SimulationFrame.world.addContactListener(new ContactListenerAdapter<SimulationBody>() {
			private boolean isContactWithFloor(ContactConstraint<SimulationBody> contactConstraint) {
				if ((contactConstraint.getBody1() == basePl || contactConstraint.getBody2() == basePl) &&
						(contactConstraint.getBody1().getUserData() == FLOOR_BODY || contactConstraint.getBody2().getUserData() == FLOOR_BODY)) {
					return true;
				}
				return false;
			}

			@Override
			public void persist(ContactCollisionData<SimulationBody> collision, Contact oldContact, Contact newContact) {
				if (isContactWithFloor(collision.getContactConstraint())) {
					isOnGround.set(true);
				}
				super.persist(collision, oldContact, newContact);
			}
		});
	}

	@Override
	protected void update(Graphics2D g, double elapsedTime) throws IOException {
		Grappin.checkGrappin();
		super.update(g, elapsedTime);
	}

	public static void main(String[] args) throws IOException {
		SimplePlatformer simulation = new SimplePlatformer("demo", 10);
		simulation.run();
	}
}