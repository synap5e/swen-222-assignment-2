package space.gui.pipeline.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import space.gui.pipeline.ViewableRoom;
import space.gui.pipeline.ViewableWall;
import space.util.Vec2;

public class MockRoom implements ViewableRoom {

	private static Random random = new Random();
	private static List<MockWall> giftWrap(List<Vec2> pts){
		Vec2 pointOnHull = getLeftMost(pts);
		Vec2 endpoint = null;
		//int i=0;

		ArrayList<Vec2> hull = new ArrayList<Vec2>();
		do{
			hull.add(pointOnHull);
			endpoint = pts.get(0);
			for (Vec2 point : pts){
				if (endpoint == pointOnHull || pointLeftOfLine(point, pointOnHull, endpoint)){
					endpoint = point;
				}
			}
			//i++;
			pointOnHull = endpoint;

		} while (endpoint != hull.get(0));


		ArrayList<MockWall> walls = new ArrayList<MockWall>();
		Vec2 prev = hull.get(hull.size()-1);
		for (Vec2 point : hull){
			walls.add(new MockWall(prev, point));
			prev = point;
		}

		return walls;
	}
	private static boolean pointLeftOfLine(Vec2 point, Vec2 lineStart, Vec2 lineEnd) {
		return 1 == Math.signum((lineEnd.getX()-lineStart.getX())*(point.getY()-lineStart.getY()) - (lineEnd.getY()-lineStart.getY())*(point.getX()-lineStart.getX()));
	}
	private static Vec2 getLeftMost(List<Vec2> pts) {
		Vec2 l = pts.get(0);
		for (Vec2 p : pts){
			if (p.getX() < l.getY()) l=p;
		}
		return l;
	}



	private List<MockWall> walls;
	public MockRoom() {
		ArrayList<Vec2> points = new ArrayList<Vec2>(200);
		for (int i=0;i<500;i++){
			points.add(new Vec2((float)(0 + random.nextGaussian() * 5), (float)(0 + random.nextGaussian() * 5)));
		}
		walls = giftWrap(points);
	}

	@Override
	public LightMode getLightMode() {
		return LightMode.BASIC_LIGHT;
	}

	@Override
	public Vec2 getCentre() {
		return new Vec2(0,0);
	}

	@Override
	public List<? extends ViewableWall> getWalls() {
		return walls;
	}

}
