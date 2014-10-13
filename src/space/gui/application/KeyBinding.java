package space.gui.application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.matthiasmann.twl.Event;

public class KeyBinding {

	public final static String ACTION_MENU = "menu";
	public final static String ACTION_INTERACT = "interact";
	public final static String ACTION_RIFLE = "rifle";
	public final static String ACTION_TORCH = "torch";
	public final static String ACTION_INVENTORY = "inventory";
	
	public final static String MOVE_FORWARD = "forward";
	public final static String MOVE_BACKWARD = "backward";
	public final static String MOVE_LEFT = "left";
	public final static String MOVE_RIGHT = "right";
	
	private final static Map<Integer, String> DEFAULT_MAP;
	static {
        Map<Integer, String> temp = new HashMap<Integer, String>();
        temp.put(Event.KEY_ESCAPE, ACTION_MENU);
        
        temp.put(Event.KEY_E, ACTION_INTERACT);
        temp.put(Event.KEY_R, ACTION_RIFLE);
        temp.put(Event.KEY_F, ACTION_TORCH);
        temp.put(Event.KEY_Q, ACTION_INVENTORY);
        
        temp.put(Event.KEY_W, MOVE_FORWARD);
        temp.put(Event.KEY_S, MOVE_BACKWARD);
        temp.put(Event.KEY_A, MOVE_LEFT);
        temp.put(Event.KEY_D, MOVE_RIGHT);
        
        DEFAULT_MAP = Collections.unmodifiableMap(temp);
    }

	private Map<Integer, String> keyMap;
	private Map<String, Integer> actionMap;

	public KeyBinding(){
		this.keyMap = new HashMap<Integer, String>();
		this.actionMap = new HashMap<String, Integer>();
		
		for(int key : DEFAULT_MAP.keySet()){
			addBinding(key, DEFAULT_MAP.get(key));
		}
	}

	protected void addBinding(int key, String action){
		keyMap.put(key, action);
		actionMap.put(action, key);
	}
	
	public void changeBinding(int key, String action){
		String previousAction = keyMap.get(key);
		int previousKey = actionMap.get(action);
		
		keyMap.remove(previousKey);
		actionMap.remove(previousAction);
		
		addBinding(key, action);
	}

	public int getKey(String action) {
		return actionMap.get(action);
	}
	
	public String getAction(int key) {
		return keyMap.get(key);
	}

	public boolean containsKey(int key) {
		return keyMap.containsKey(key);
	}

	public String getKeyName(String action) {
		return Event.getKeyNameForCode(getKey(action));
	}
	
	public String getKeyName(int key) {
		return Event.getKeyNameForCode(key);
	}

	public Set<Map.Entry<String, Integer>> getActionSet() {
		return Collections.unmodifiableSet(actionMap.entrySet());
	}
}
