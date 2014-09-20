package space.gui.pipeline.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import space.gui.pipeline.viewable.ViewableDoor;
import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWall;
import space.math.ConcaveHull;
import space.math.Segment2D;
import space.math.Vector2D;

public class MockRoom1 extends MockRoom{

	
	
	public MockRoom1() {
		ArrayList<Vector2D> points = new ArrayList<Vector2D>(200);
		
		
			points.add(new Vector2D(-10,  20));
			points.add(new Vector2D( 30,  30));
			points.add(new Vector2D( 30, 0));
			points.add(new Vector2D(-30, -30));
		
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
			final MockRoom rthis = MockRoom1.this;
			if (lineSeg.start.equals(new Vector2D(30, 0))){
				return Arrays.asList(new ViewableDoor() {
	
					@Override
					public Vector2D getLocation() {
						return new Vector2D(0, -15);
					}

					@Override
					public float getOpenPercent() {
						return getDoorPercent(0.5f);
					}

					@Override
					public ViewableRoom getRoom1() {
						return rthis;
					}

					@Override
					public ViewableRoom getRoom2() {
						return MockRoom.room2;
					}
					
				},
				new ViewableDoor() {
					
					@Override
					public Vector2D getLocation() {
						return new Vector2D(-15, -15-7.5f);
					}

					@Override
					public float getOpenPercent() {
						return 0;//getDoorPercent(0.2f);
					}
					
					@Override
					public ViewableRoom getRoom1() {
						return rthis;
					}

					@Override
					public ViewableRoom getRoom2() {
						return MockRoom1.room2;
					}
					
					
				});
			} else if (lineSeg.start.equals(new Vector2D(-10, 20))){
				return Arrays.asList(new ViewableDoor() {

					@Override
					public Vector2D getLocation() {
						return new Vector2D(-6.1194305f, 20.970146f);
					}

					@Override
					public float getOpenPercent() {
						return getDoorPercent(1);
					}
					
					@Override
					public ViewableRoom getRoom1() {
						return rthis;
					}

					@Override
					public ViewableRoom getRoom2() {
						return null;
					}
					
					
				});
			} else if (lineSeg.start.equals(new Vector2D(-30, -30))){
				return Arrays.asList(new ViewableDoor() {

					@Override
					public Vector2D getLocation() {
						return new Vector2D(-28.514435f, -26.286095f);
					}

					@Override
					public float getOpenPercent() {
						return getDoorPercent(0);
					}
					
					@Override
					public ViewableRoom getRoom1() {
						return rthis;
					}

					@Override
					public ViewableRoom getRoom2() {
						return null;
					}
					
					
				},
				new ViewableDoor() {

					@Override
					public Vector2D getLocation() {
						return new Vector2D(-25.543304f, -18.858284f);
					}

					@Override
					public float getOpenPercent() {
						return getDoorPercent(0.7f);
					}
					
					@Override
					public ViewableRoom getRoom1() {
						return rthis;
					}

					@Override
					public ViewableRoom getRoom2() {
						return null;
					}
					
					
				},
				new ViewableDoor() {

					@Override
					public Vector2D getLocation() {
						return new Vector2D(-12.173228f, 14.566872f);
					}

					@Override
					public float getOpenPercent() {
						return getDoorPercent(0.9f);
					}
					
					@Override
					public ViewableRoom getRoom1() {
						return rthis;
					}

					@Override
					public ViewableRoom getRoom2() {
						return null;
					}
					

				});
			} else {
				return Arrays.asList();
			}
		}

	}
	
	float getDoorPercent(float f) {
		float doord = doorTime + f*6;
		int state = (int)(doord) % 6;
		if (state == 0){
			return doord % 1.0f;
		} if (state == 1 || state == 2){
			return 1;
		} if (state == 3){
			return 1-doord % 1.0f;
		} else { //(state == 4 || state == 5){
			return 0;
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
	
}
