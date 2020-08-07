package skyAbove;

import org.dyn4j.dynamics.joint.MotorJoint;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Segment;
import org.dyn4j.geometry.Vector2;

public class ObjectsWorld {
	public static SimulationBody left = new SimulationBody();
	public static SimulationBody right = new SimulationBody();
	public static SimulationBody top = new SimulationBody();
	public static SimulationBody bottom = new SimulationBody();

	public static SimulationBody plateforms = new SimulationBody();
	
	public static Segment lftSeg;
	public static Segment rightSeg;
	public static Segment topSeg;
	public static Segment bottomSeg;

	// draw the player in bike
	public static SimulationBody bike = new SimulationBody();
	public static MotorJoint movePl = new MotorJoint(bike, ObjectsWorld.bottom);
	
	public static void connectObjects() {
		ObjectsWorld.left.setMass(MassType.INFINITE);
		ObjectsWorld.right.setMass(MassType.INFINITE);
		ObjectsWorld.top.setMass(MassType.INFINITE);
		ObjectsWorld.bottom.setMass(MassType.INFINITE);
		ObjectsWorld.plateforms.setMass(MassType.INFINITE);

		left.addFixture(new Segment(new Vector2(0.1,0.1), new Vector2(0.1,20)));
		right.addFixture(new Segment(new Vector2(39.5,0.1), new Vector2(39.5,20)));
		top.addFixture(new Segment(new Vector2(0.1,0.1), new Vector2(39.5,0.1)));
		bottom.addFixture(new Segment(new Vector2(0.1,20), new Vector2(39.5,20)));

		bike.addFixture(Geometry.createRectangle(0.8, 0.8));
		bike.translate(20,19.5);
		movePl.setCollisionAllowed(true);
		bike.setMass(MassType.NORMAL);
		bike.createAABB();
}
}
