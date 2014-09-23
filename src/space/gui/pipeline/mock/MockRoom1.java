package space.gui.pipeline.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import space.gui.pipeline.viewable.ViewableBeam;
import space.gui.pipeline.viewable.ViewableDoor;
import space.gui.pipeline.viewable.ViewableObject;
import space.gui.pipeline.viewable.ViewableRoom;
import space.gui.pipeline.viewable.ViewableWall;
import space.math.ConcaveHull;
import space.math.Segment2D;
import space.math.Vector2D;
import space.math.Vector3D;

public class MockRoom1 extends MockRoom{

	List<Bullet> bullets = new ArrayList<Bullet>();
	
	protected float lf = 5;
	protected Vector2D pos = new Vector2D(0, 0);

	protected Vector3D dir= new Vector3D(0, 0, 1);

	private float el;


	public MockRoom1() {
		ArrayList<Vector2D> points = new ArrayList<Vector2D>(200);
		
		
			points.add(new Vector2D(-10,  20));
			points.add(new Vector2D( 30,  30));
			points.add(new Vector2D( 30, 0));
			points.add(new Vector2D(-30, -30));
		
		hull = new ConcaveHull(points);
		
		objects = new ArrayList<Robot>();
		for (int i=0;i<2;i++){
			objects.add(new Robot(new Vector2D((float) Math.random()*20f - 10f, (float) (Math.random()*20f - 10f))));
		}
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		lf -= delta/200f;
		if (lf < -2){
			pos = getContainedObjects().get(0).getPosition().sub(new Vector2D(0, 0));
			//Vector2D t = objects.get(1).getFacing();
			//dir = new Vector3D(t.getX(), (float) (random.nextDouble() * 0.25 - 0.125), t.getY()).normalized();
			el = (float) (random.nextDouble() * 0.25 - 0.125);
			
			lf = 2;
		}
		for (Bullet pel : new ArrayList<>(bullets)){
			if (!hull.contains(pel.getPosition())){
				bullets.remove(pel);
			}
			pel.update(delta);
		}
		
		if (random.nextDouble() < 0.1){
			Vector2D dir = objects.get(0).getFacing();
			Bullet p = new Bullet(/*new Vector3D(random.nextFloat()*10 - 5, random.nextFloat()*5+2.5f, random.nextFloat()*10 - 5),*/
							new Vector3D(objects.get(0).getPosition().getX(), objects.get(0).getElevation()+4, objects.get(0).getPosition().getY()),
							new Vector3D(dir.getX(), (float) (random.nextDouble() * 0.25 - 0.125), dir.getY()).normalized().mul(10));
			bullets.add(p);
		}
	}
	
	@Override
	public List<? extends ViewableObject> getContainedObjects() {
		ArrayList<ViewableObject> vob = new ArrayList<>();
		vob.addAll(super.getContainedObjects());
		vob.addAll(bullets);
		return vob;
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
					public Vector2D getPosition() {
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

					@Override
					public float getAngle() {
						return 0;
					}

					@Override
					public float getElevation() {
						return 0;
					}
					
				},
				new ViewableDoor() {
					
					@Override
					public Vector2D getPosition() {
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

					@Override
					public float getAngle() {
						return 0;
					}

					@Override
					public float getElevation() {
						return 0;
					}
					
					
				});
			} else if (lineSeg.start.equals(new Vector2D(-10, 20))){
				return Arrays.asList(new ViewableDoor() {

					@Override
					public Vector2D getPosition() {
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

					@Override
					public float getAngle() {
						return 0;
					}

					@Override
					public float getElevation() {
						return 0;
					}
					
					
				});
			} else if (lineSeg.start.equals(new Vector2D(-30, -30))){
				return Arrays.asList(new ViewableDoor() {

					@Override
					public Vector2D getPosition() {
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

					@Override
					public float getAngle() {
						return 0;
					}

					@Override
					public float getElevation() {
						return 0;
					}
					
					
				},
				new ViewableDoor() {

					@Override
					public Vector2D getPosition() {
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

					@Override
					public float getAngle() {
						return 0;
					}

					@Override
					public float getElevation() {
						return 0;
					}
					
					
				},
				new ViewableDoor() {

					@Override
					public Vector2D getPosition() {
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

					@Override
					public float getAngle() {
						return 0;
					}

					@Override
					public float getElevation() {
						return 0;
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

	@Override
	public List<? extends ViewableBeam> getBeams() {
		return Arrays.asList(new ViewableBeam() {
			
			@Override
			public Vector2D getPosition() {
				return objects.get(1).getPosition().add(objects.get(1).getFacing().normalized().mul(0.8f));
			}
			
			@Override
			public float getElevation() {
				return objects.get(1).getElevation() + 6.05f;
			}
			
			@Override
			public float getAngle() {
				return 45;
			}
			
			@Override
			public Vector3D getBeamDirection() {
				Vector2D t = objects.get(1).getFacing();
				return new Vector3D(t.getX(), el, t.getY()).normalized();
			}

			@Override
			public float getRemainingLife() {
				return lf ;
			}
		});
	}
	
}
