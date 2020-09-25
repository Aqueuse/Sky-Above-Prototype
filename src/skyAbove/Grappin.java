package skyAbove;

import java.awt.Color;

import org.dyn4j.dynamics.joint.RopeJoint;
import org.dyn4j.geometry.Vector2;

public class Grappin {
	/// some values for the climber
	int fall=0;
	static boolean modeGrappin=false;
	static boolean grappinHooked=false;
	static double ropeSize = 2.0;
	static int direction=0;

	public static void checkGrappin() {
		///////////////////  le grappin  ////////////////////////////////////
		if (BackgoundTheater.ComportementLandscape==true) {
			if (CustomkeyListener.upPressed.get() && modeGrappin==false) {
				CustomkeyListener.upPressed.set(false);
				modeGrappin=true;
				SimplePlatformer.basePl.setEnabled(false);
				modeGrappinOn();
			}

			if (CustomkeyListener.downPressed.get() && modeGrappin==true && ropeSize>=2.0) {
				CustomkeyListener.downPressed.set(false);
				modeGrappinOff();
			}

			// grab the hook
			if (CustomkeyListener.leftPressed.get() && modeGrappin==true && grappinHooked == false
					&& SimulationFrame.world.containsBody(SimplePlatformer.accrocheLeft)) {
				CustomkeyListener.leftPressed.set(false);
				grappinHooked = true;
				direction = 0;
				connectGrapple(direction, 1.0);
				SimplePlatformer.basePl.setEnabled(true);
			}

			if (CustomkeyListener.rightPressed.get() && modeGrappin==true && grappinHooked == false
					&& SimulationFrame.world.containsBody(SimplePlatformer.accrocheRight)) {
				CustomkeyListener.rightPressed.set(false);
				grappinHooked = true;
				direction = 1;
				connectGrapple(direction, 1.0);
				SimplePlatformer.basePl.setEnabled(true);
			}

			// dangling - pendouillage
			if (CustomkeyListener.leftPressed.get() && grappinHooked==true) {
				SimplePlatformer.basePl.applyImpulse(new Vector2(-0.07, 0));				
			}
			if (CustomkeyListener.rightPressed.get() && grappinHooked==true) {
				SimplePlatformer.basePl.applyImpulse(new Vector2(0.07, 0));
			}

			// climb Up - climb Down
			if (CustomkeyListener.upPressed.get() && grappinHooked==true) {
				CustomkeyListener.upPressed.set(false);
				ropeSize -= 0.5;
				if (ropeSize>0.2) {
					if (SimulationFrame.world.containsJoint(addGrapple(SimplePlatformer.accrocheLeft, SimplePlatformer.basePl, ropeSize+0.5))) SimulationFrame.world.removeJoint(addGrapple(SimplePlatformer.accrocheLeft, SimplePlatformer.basePl, ropeSize+0.5));
					if (SimulationFrame.world.containsJoint(addGrapple(SimplePlatformer.accrocheRight, SimplePlatformer.basePl, ropeSize+0.5))) SimulationFrame.world.removeJoint(addGrapple(SimplePlatformer.accrocheRight, SimplePlatformer.basePl, ropeSize+0.5));
					connectGrapple(direction, ropeSize);
				}
				if (ropeSize<=0.2) goUp();
			}
			
			if (CustomkeyListener.downPressed.get() && grappinHooked==false && modeGrappin==true) {
				modeGrappinOff();
			}
			
			if (CustomkeyListener.downPressed.get() && grappinHooked==true) {
				CustomkeyListener.downPressed.set(false);
				ropeSize+=0.5;
				if (ropeSize<1.0 && ropeSize>0) { //climb Down
					if (SimulationFrame.world.containsJoint(addGrapple(SimplePlatformer.accrocheLeft, SimplePlatformer.basePl, ropeSize-0.5))) SimulationFrame.world.removeJoint(addGrapple(SimplePlatformer.accrocheLeft, SimplePlatformer.basePl, ropeSize-0.5));
					if (SimulationFrame.world.containsJoint(addGrapple(SimplePlatformer.accrocheRight, SimplePlatformer.basePl, ropeSize-0.5))) SimulationFrame.world.removeJoint(addGrapple(SimplePlatformer.accrocheRight, SimplePlatformer.basePl, ropeSize-0.5));
					connectGrapple(direction, ropeSize);
				}
				if (ropeSize==1.0) modeGrappinOff();
			}
		}
	}
	
	public static void modeGrappinOn() {
		int localCurrentX = Locate.locatePlayer()[0];
		int localCurrentY = Locate.locatePlayer()[1];
		
		if (localCurrentX>=1 && localCurrentX<10) {
			if (Platforms.platformsArray[localCurrentX-1][localCurrentY]>0
					|| Platforms.platformsArray[localCurrentX][localCurrentY]>0) {
				SimulationFrame.world.addBody(SimplePlatformer.accrocheLeft);
				SimplePlatformer.accrocheLeft.translateToOrigin();
				SimplePlatformer.accrocheLeft.translate((localCurrentX*2.8505), (localCurrentY*2.8505)+0.5);
			}

			if (Platforms.platformsArray[localCurrentX][localCurrentY]>0
					|| Platforms.platformsArray[localCurrentX+1][localCurrentY]>0) {
				SimulationFrame.world.addBody(SimplePlatformer.accrocheRight);
				SimplePlatformer.accrocheRight.translateToOrigin();
				SimplePlatformer.accrocheRight.translate((localCurrentX*2.8505)+2.8505, (localCurrentY*2.8505)+0.5);
			}
		}
	}

	public static void modeGrappinOff() {
		if (SimplePlatformer.world.containsBody(SimplePlatformer.accrocheLeft)) SimulationFrame.world.removeBody(SimplePlatformer.accrocheLeft); 
		if (SimulationFrame.world.containsBody(SimplePlatformer.accrocheRight)) SimulationFrame.world.removeBody(SimplePlatformer.accrocheRight);
		
		if (SimulationFrame.world.containsJoint(addGrapple(SimplePlatformer.accrocheLeft, SimplePlatformer.basePl, 1.0))) SimulationFrame.world.removeJoint(addGrapple(SimplePlatformer.accrocheLeft, SimplePlatformer.basePl, 1.0));
		if (SimulationFrame.world.containsJoint(addGrapple(SimplePlatformer.accrocheRight, SimplePlatformer.basePl, 1.0))) SimulationFrame.world.removeJoint(addGrapple(SimplePlatformer.accrocheRight, SimplePlatformer.basePl, 1.0));

		SimplePlatformer.accrocheLeft.setColor(Color.darkGray);
		SimplePlatformer.accrocheRight.setColor(Color.darkGray);

		modeGrappin = false;
		grappinHooked=false;
		ropeSize = 1.0;
		
		SimplePlatformer.basePl.setEnabled(true);
		SimplePlatformer.basePl.applyImpulse(new Vector2(0,-0.1));
	}

	public static void connectGrapple(int direction, double ropeSize) {
		if (direction == 0) {
			SimulationFrame.world.removeBody(SimplePlatformer.accrocheRight);
			SimplePlatformer.accrocheLeft.setColor(Color.gray);
			SimulationFrame.world.addJoint(addGrapple(SimplePlatformer.accrocheLeft, SimplePlatformer.basePl, ropeSize));
		}
		if (direction == 1) {
			SimulationFrame.world.removeBody(SimplePlatformer.accrocheLeft);
			SimplePlatformer.accrocheRight.setColor(Color.gray);
			SimulationFrame.world.addJoint(addGrapple(SimplePlatformer.accrocheRight, SimplePlatformer.basePl, ropeSize));
		}
	}

	public static void goUp() {
		// enlever le ropeJoint
		if (SimulationFrame.world.containsJoint(addGrapple(SimplePlatformer.accrocheLeft, SimplePlatformer.basePl, ropeSize))) SimulationFrame.world.removeJoint(addGrapple(SimplePlatformer.accrocheLeft, SimplePlatformer.basePl, ropeSize));
		if (SimulationFrame.world.containsJoint(addGrapple(SimplePlatformer.accrocheRight, SimplePlatformer.basePl, ropeSize))) SimulationFrame.world.removeJoint(addGrapple(SimplePlatformer.accrocheRight, SimplePlatformer.basePl, ropeSize));

		// translate = deplacement relatif !
		if (direction==0) SimplePlatformer.basePl.translate(-1,-2.2);
		if (direction==1) SimplePlatformer.basePl.translate(1,-2.2);
		modeGrappinOff();
	}

	public static RopeJoint<SimulationBody> addGrapple(SimulationBody anchor1, SimulationBody anchor2, double upperLimit) {
		RopeJoint<SimulationBody> ropeJoint = new RopeJoint<SimulationBody>(
				anchor1,
				anchor2,
				new Vector2(
						anchor1.getWorldCenter().x,
						anchor1.getWorldCenter().y),
				new Vector2(
						anchor2.getWorldCenter().x,
						anchor2.getWorldCenter().y)
				);

		ropeJoint.setLowerLimit(0.0);
		ropeJoint.setLowerLimitEnabled(true);
		ropeJoint.setUpperLimit(upperLimit);
		ropeJoint.setUpperLimitEnabled(true);

		return ropeJoint;
	}
}
