package space.gui.pipeline.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
public class MockRoom implements ViewableRoom {

	private static Random random = new Random();
	private static ConcaveHull giftWrap(List<Vector2D> pts){
		Vector2D pointOnHull = getLeftMost(pts);
		Vector2D endpoint = null;
		//int i=0;

		ArrayList<Vector2D> hull = new ArrayList<Vector2D>();
		do{
			hull.add(pointOnHull);
			endpoint = pts.get(0);
			for (Vector2D point : pts){
				if (endpoint == pointOnHull || pointLeftOfLine(point, pointOnHull, endpoint)){
					endpoint = point;
				}
			}
			//i++;
			pointOnHull = endpoint;

		} while (endpoint != hull.get(0));


		return new ConcaveHull(hull);
	}
	private static boolean pointLeftOfLine(Vector2D point, Vector2D lineStart, Vector2D lineEnd) {
		return 1 == Math.signum((lineEnd.getX()-lineStart.getX())*(point.getY()-lineStart.getY()) - (lineEnd.getY()-lineStart.getY())*(point.getX()-lineStart.getX()));
	}
	private static Vector2D getLeftMost(List<Vector2D> pts) {
		Vector2D l = pts.get(0);
		for (Vector2D p : pts){
			if (p.getX() < l.getX()) l=p;
		}
		return l;
	}



	private List<Bunny> objects;
	private ConcaveHull hull;
	public MockRoom() {
		ArrayList<Vector2D> points = new ArrayList<Vector2D>(200);
		for (int i=0;i<100;i++){
			points.add(new Vector2D((float)(0 + random.nextGaussian() * 10), (float)(0 + random.nextGaussian() * 10)));
		}
		hull = giftWrap(points);
		
		objects = new ArrayList<Bunny>();
		for (int i=0;i<2;i++){
			objects.add(new Bunny(new Vector2D((float) Math.random()*10f - 5f, (float) (Math.random()*10f - 5f))));
		}
	}

	@Override
	public LightMode getLightMode() {
		return LightMode.BASIC_LIGHT;
	}

	@Override
	public Vector2D getCentre() {
		return new Vector2D(0,0);
	}

	@Override
	public List<? extends ViewableWall> getWalls() {
		List<Wall> walls = new ArrayList<Wall>();
		for(Segment2D seg : hull){
			walls.add(new Wall(seg));
		}
		return walls;
	}
	
	private class Wall implements ViewableWall{
		private Segment2D lineSeg;
		
		public Wall(Segment2D ls){
			lineSeg = ls;
		}
		@Override
		public Vector2D getStart() {
			return lineSeg.start;
		}

		@Override
		public Vector2D getEnd() {
			return lineSeg.end;
		}

	}
	
	@Override
	public List<? extends ViewableObject> getContainedObjects() {
		return objects;
	}
	
	public void update(int delta) {
		for (Bunny b : objects){
			b.update(delta);
		}
	}
	@Override
	public boolean contains(Vector2D point) {
		return hull.contains(point);
	}

}
