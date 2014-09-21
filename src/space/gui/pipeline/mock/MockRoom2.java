package space.gui.pipeline.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import space.gui.pipeline.viewable.ViewableBeam;
import space.gui.pipeline.viewable.ViewableDoor;
import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWall;
import space.math.ConcaveHull;
import space.math.Segment2D;
import space.math.Vector2D;

public class MockRoom2 extends MockRoom{

	public MockRoom2() {
		ArrayList<Vector2D> points = new ArrayList<Vector2D>(200);
		
		
		points.add(new Vector2D(-30, -30));
		points.add(new Vector2D( 30, 0));
		points.add(new Vector2D( 30, -60));
		
		hull = new ConcaveHull(points);
		
		objects = new ArrayList<Robot>();
		for (int i=0;i<2;i++){
			objects.add(new Robot(new Vector2D((float) Math.random()*10f - 5f, (float) (Math.random()*10f - 5f))));
		}
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
		@Override
		public List<? extends ViewableDoor> getDoors() {
			final MockRoom rthis = MockRoom2.this;
			if (lineSeg.start.equals(new Vector2D(-30, -30))){
				return Arrays.asList(new ViewableDoor() {
	
					@Override
					public Vector2D getLocation() {
						return new Vector2D(0, -15);
					}

					@Override
					public float getOpenPercent() {
						return ((MockRoom1)MockRoom.room1).getDoorPercent(0.5f);
					}

					@Override
					public ViewableRoom getRoom1() {
						return rthis;
					}

					@Override
					public ViewableRoom getRoom2() {
						return MockRoom.room1;
					}
					
				},
				new ViewableDoor() {
					
					@Override
					public Vector2D getLocation() {
						return new Vector2D(-15, -15-7.5f);
					}

					@Override
					public float getOpenPercent() {
						return 0;//((MockRoom1)MockRoom.room1).getDoorPercent(0.2f);
					}
					
					@Override
					public ViewableRoom getRoom1() {
						return rthis;
					}

					@Override
					public ViewableRoom getRoom2() {
						return MockRoom1.room1;
					}
					
					
				});
			} else {
				return Arrays.asList();
			}
		}

	}
	
	@Override
	public Vector2D getAABBTopLeft() {
		return hull.getAABBTopLeft();
	}

	@Override
	public Vector2D getAABBBottomRight() {
		return hull.getAABBBottomRight();
	}
	
	@Override
	public List<? extends ViewableBeam> getBeams() {
		return Arrays.asList();
	}
	
}