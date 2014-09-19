package space.gui.pipeline.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import space.gui.pipeline.viewable.ViewableDoor;
import space.gui.pipeline.viewable.ViewableObject;
import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWall;
import space.math.ConcaveHull;
import space.math.Segment2D;
import space.math.Vector2D;

/**
 * 
 * @author Simon Pinfold
 *
 */
public abstract class MockRoom implements ViewableRoom {

	private static Random random = new Random();



	public static MockRoom room1 = new MockRoom1();
	public static MockRoom room2 = new MockRoom2();
	


	protected List<Robot> objects;
	protected ConcaveHull hull;

	@Override
	public LightMode getLightMode() {
		return LightMode.BASIC_LIGHT;
	}

	@Override
	public Vector2D getCentre() {
		return hull.getCentre();
	}

	@Override
	public List<? extends ViewableObject> getContainedObjects() {
		return objects;
	}
	
	float doorTime = 0;
	public void update(int delta) {
		for (Robot b : objects){
			b.update(delta);
		}
		doorTime += delta/1000f;
	}
	
	@Override
	public boolean contains(Vector2D point) {
		return hull.contains(point);
	}

	@Override
	public List<? extends ViewableDoor> getAllDoors() {
		List<ViewableDoor> vd = new ArrayList<>();
		for (ViewableWall v : this.getWalls()){
			vd.addAll(v.getDoors());
		}
		return vd;
	}

}
