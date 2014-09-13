package space.world;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWall;
import space.util.Vec2;

public class Room implements ViewableRoom{
	private LightMode mode;
	private int id;
	private String description;
	private Polygon roomShape;
	
	public Room(LightMode m, int i, String d, Polygon r){
		mode = m;
		id = i;
		description = d;
		roomShape = r;
	}
	
	public Room(LightMode m, int i, String d, List<Vec2> points){
		mode = m;
		id = i;
		description = d;
		setUpPolygon(points);
	}

	
	@Override
	public Vec2 getCentre() {
		float x = (float) roomShape.getBounds().getCenterX();
		float y = (float) roomShape.getBounds().getCenterY();
		return new Vec2(x,y);
	}

	@Override
	public List<? extends ViewableWall> getWalls() {
		List<Wall> walls = new ArrayList<Wall>();
		//cant get points of polygon :(
		return null;
	}
	
	public int getId() {
		return id;
	}

	@Override
	public LightMode getLightMode() {
		return mode;
	}
	
	private void setUpPolygon(List<Vec2> points){
		roomShape = new Polygon();
		for(Vec2 p : points){
			roomShape.addPoint((int)p.getX(), (int)p.getY());
		}
	}
	
	private class Wall implements ViewableWall{
		private Vec2 start;
		private Vec2 end;
		
		public Wall(Vec2 s, Vec2 e){
			start = s;
			end = e;
		}
		@Override
		public Vec2 getStart() {
			return start;
		}

		@Override
		public Vec2 getEnd() {
			return end;
		}

	}
}
