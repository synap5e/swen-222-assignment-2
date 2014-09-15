package space.gui.pipeline.mock;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import space.gui.pipeline.viewable.ViewableObject;
import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWall;
import space.math.Vector2D;

public class MockRoom implements ViewableRoom {

	private static Random random = new Random();
	private static List<MockWall> giftWrap(List<Point2D.Float> pts){
		Point2D.Float pointOnHull = getLeftMost(pts);
		Point2D.Float endpoint = null;
		//int i=0;

		ArrayList<Point2D.Float> hull = new ArrayList<Point2D.Float>();
		do{
			hull.add(pointOnHull);
			endpoint = pts.get(0);
			for (Point2D.Float point : pts){
				if (endpoint == pointOnHull || pointLeftOfLine(point, pointOnHull, endpoint)){
					endpoint = point;
				}
			}
			//i++;
			pointOnHull = endpoint;

		} while (endpoint != hull.get(0));


		ArrayList<MockWall> walls = new ArrayList<MockWall>();
		Point2D.Float prev = hull.get(hull.size()-1);
		for (Point2D.Float point : hull){
			walls.add(new MockWall(new Vector2D(prev.x, prev.y), new Vector2D(point.x, point.y)));
			prev = point;
		}

		return walls;
	}
	private static boolean pointLeftOfLine(Point2D.Float point, Point2D.Float lineStart, Point2D.Float lineEnd) {
		return 1 == Math.signum((lineEnd.x-lineStart.x)*(point.y-lineStart.y) - (lineEnd.y-lineStart.y)*(point.x-lineStart.x));
	}
	private static Point2D.Float getLeftMost(List<Point2D.Float> pts) {
		Point2D.Float l = pts.get(0);
		for (Point2D.Float p : pts){
			if (p.x < l.x) l=p;
		}
		return l;
	}



	private List<MockWall> walls;
	private List<Bunny> objects;
	public MockRoom() {
		ArrayList<Point2D.Float> points = new ArrayList<Point2D.Float>(200);
		for (int i=0;i<100;i++){
			points.add(new Point2D.Float((float)(0 + random.nextGaussian() * 10), (float)(0 + random.nextGaussian() * 10)));
		}
		walls = giftWrap(points);
		
		objects = new ArrayList<Bunny>();
		for (int i=0;i<10;i++){
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
		return walls;
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

}
