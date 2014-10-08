package space.network.message.sync;

import java.nio.ByteBuffer;
import space.world.Door;
import space.world.World;

public class DoorSyncMessage implements SyncMessage {

	private int doorId;
	private boolean isOpen;
	private boolean isLocked;
	
	public DoorSyncMessage(int doorId, boolean isOpen, boolean isLocked) {
		this.doorId = doorId;
		this.isOpen = isOpen;
		this.isLocked = isLocked;
	}
	
	public DoorSyncMessage(byte[] data){
		byte tru = (byte) 1;
		ByteBuffer buffer = ByteBuffer.wrap(data);
		doorId = buffer.getInt();
		isOpen = (buffer.get() == tru);
		isLocked = (buffer.get() == tru);
	}
	
	public int getDoorID(){
		return doorId;
	}
	
	public boolean isOpen(){
		return isOpen;
	}
	
	public boolean isLocked(){
		return isLocked;
	}
	
	@Override
	public void applyTo(World world){
		Door d = (Door) world.getEntity(doorId);
		
		//FIXME: make doors sync
		if (isOpen){
			d.open();
		} else {
			//d.closeDoor();
		}

		
		//d.setLocked(isLocked);
	}
	
	@Override
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE/8+2);
		buffer.putInt(doorId);
		buffer.put((byte) ((isOpen) ? 1 : 0));
		buffer.put((byte) ((isLocked) ? 1 : 0));
		return buffer.array();
	}

}
