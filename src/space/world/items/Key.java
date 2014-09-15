package space.world.items;

import space.math.Vector2D;
import space.world.Exit;

public class Key extends Item implements Movable {
	private Exit exit; //the exit it opens
	
	public Key(Vector2D pos, int i, String d, Exit e) {
		super(pos, i, d);
		exit = e;
	}

	public Exit getExit() {
		return exit;
	}

	@Override
	public boolean canMove() {
		return true;
	}	

}
