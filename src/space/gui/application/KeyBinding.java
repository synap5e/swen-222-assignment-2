package space.gui.application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.matthiasmann.twl.Event;

/**
 * The model of the mapping between keys and actions.
 * It is an implementation of a two-way map as it looks up action to key,
 * and key to actions.
 * 
 * @author Matt Graham 300211545
 */

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
	
	//define default bindings.
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
	
	private boolean isActive;
	
	/**
	 * Create a new key binding model with the default bindings.
	 */
	public KeyBinding(){
		this(DEFAULT_MAP);
	}

	/**
	 * Creates a new key binding model with the given bindings.
	 * 
	 * @param defaultMap the initial key bindings
	 */
	public KeyBinding(Map<Integer, String> defaultMap){
		this.keyMap = new HashMap<Integer, String>();
		this.actionMap = new HashMap<String, Integer>();
		
		for(int key : defaultMap.keySet()){
			addBinding(key, defaultMap.get(key));
		}
	}
	
	/**
	 * Sets whether user input is allowed.
	 * 
	 * @param flag
	 */
	public void setActive(boolean flag){
		isActive = flag;
	}
	
	/**
	 * Gets whether user input is allowed.
	 * 
	 * @return
	 */
	public boolean isActive(){
		return isActive;
	}

	/**
	 * Adds a binding to the maps.
	 * Note: new binding infer new actions, which shouldn't happen after initialisation.
	 * 
	 * @param key the key to be bound
	 * @param action the action for the key
	 */
	protected void addBinding(int key, String action){
		keyMap.put(key, action);
		actionMap.put(action, key);
	}
	
	
	/**
	 * Re-maps a key to a new binding.
	 * 
	 * @param key the key to be bound
	 * @param action the action for the key
	 */
	public void changeBinding(int key, String action){
		String previousAction = keyMap.get(key);
		int previousKey = actionMap.get(action);
		
		keyMap.remove(previousKey);
		actionMap.remove(previousAction);
		
		addBinding(key, action);
	}

	/**
	 * Gets the key of an action.
	 * 
	 * @param action
	 * @return the key binding to the action
	 */
	public int getKey(String action) {
		return actionMap.get(action);
	}
	
	/**
	 * Gets the action of a key.
	 * 
	 * @param key
	 * @return the action bound to the key
	 */
	public String getAction(int key) {
		return keyMap.get(key);
	}

	
	/**
	 * Get whether a key has a binding.
	 * 
	 * @param key
	 * @return if key has a binding
	 */
	public boolean containsKey(int key) {
		return keyMap.containsKey(key);
	}

	/**
	 * Gets the character of the key bound to the action.
	 * 
	 * @param action
	 * @return the char of the key
	 */
	public String getKeyName(String action) {
		return Event.getKeyNameForCode(getKey(action));
	}
	
	/**
	 * Gets the character of the key.
	 * 
	 * @param action
	 * @return the char of the key
	 */
	public String getKeyName(int key) {
		return Event.getKeyNameForCode(key);
	}

	/**
	 * Gets a complete set of the key bindings
	 * 
	 * @return an iterable read-only set of key bindings
	 */
	public Set<Map.Entry<String, Integer>> getActionSet() {
		return Collections.unmodifiableSet(actionMap.entrySet());
	}
}
