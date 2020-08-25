package skyAbove;

import java.awt.Color;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.joint.MotorJoint;
import org.dyn4j.dynamics.joint.RopeJoint;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Segment;
import org.dyn4j.geometry.Vector2;

public class ObjectsWorld {
	public static SimulationBody left = new SimulationBody();
	public static SimulationBody right = new SimulationBody();
	public static SimulationBody top = new SimulationBody();
	public static SimulationBody bottom = new SimulationBody();

	public static SimulationBody plateforms = new SimulationBody();

	// draw the player
	public static SimulationBody basePl = new SimulationBody();
	public static SimulationBody handsPl = new SimulationBody();
	public static MotorJoint movePl = new MotorJoint(basePl, plateforms);
	public static Rectangle body = new Rectangle(0.8, 0.8);
	public static BodyFixture bodyBase = new BodyFixture(body);

	// les accroches pour l'escalade
	public static Rectangle accroche = new Rectangle(0.5,1);
	public static SimulationBody accrocheLeft = new SimulationBody();
	public static SimulationBody accrocheRight = new SimulationBody();

	public static void connectObjects() {
		left.setMass(MassType.INFINITE);
		right.setMass(MassType.INFINITE);
		top.setMass(MassType.INFINITE);
		plateforms.setMass(MassType.INFINITE);
		accrocheLeft.setMass(MassType.INFINITE);
		accrocheRight.setMass(MassType.INFINITE);

		accrocheLeft.setColor(Color.darkGray);
		accrocheRight.setColor(Color.darkGray);

		left.addFixture(new Segment(new Vector2(0,0), new Vector2(0,22.4)));
		right.addFixture(new Segment(new Vector2(39.5,0), new Vector2(39.5,22.4)));
		top.addFixture(new Segment(new Vector2(0,0), new Vector2(39.5,0)));
		bottom.addFixture(new Segment(new Vector2(0,22.4), new Vector2(39.5,22.4)));

		ObjectsWorld.accrocheLeft.addFixture(ObjectsWorld.accroche);
		ObjectsWorld.accrocheRight.addFixture(ObjectsWorld.accroche);

		bodyBase.setFriction(0.3);
		basePl.addFixture(bodyBase);
		basePl.translate((LoadFile.currentRelXPlayer*4)+2,(LoadFile.currentRelYPlayer*4)+3.8);

		movePl.setCollisionAllowed(true);
		basePl.setMass(MassType.NORMAL);
		basePl.createAABB();
	}
	
	public static RopeJoint addGrapple(SimulationBody anchor1, SimulationBody anchor2, double upperLimit) {
		RopeJoint ropeJoint = new RopeJoint(
				anchor1,
				anchor2,
				new Vector2(
						anchor1.getWorldCenter().x,
						anchor1.getWorldCenter().y),
				new Vector2(
						anchor2.getWorldCenter().x,
						anchor2.getWorldCenter().y
						)
				);

		ropeJoint.setLowerLimit(0.0);
		ropeJoint.setLowerLimitEnabled(true);
		ropeJoint.setUpperLimit(upperLimit);
		ropeJoint.setUpperLimitEnabled(true);

		return ropeJoint;
	}
}