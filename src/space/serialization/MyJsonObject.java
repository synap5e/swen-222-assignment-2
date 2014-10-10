package space.serialization;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 * Represents a MyJsonObject for classes and entities in the game
 * this is a wrapper class for the raw JSONObject from the library
 * 
 * It enables type safety rather than just being generic - to avoid accidently put the wrong data type
 * 
 * @author Shweta Barapatre
 *
 */

public class MyJsonObject {
	JSONObject obj;
	String key;
	
		/**
		 * Constructor - creates a new Json object
		 */
		public MyJsonObject(){
			obj = new JSONObject();
		}
		
		/**
		 * Second constructor that takes a loaded 
		 * @param loadedObject JSONObject with data
		 */
		
		public String getName(){
			return key;
		}
		
		public MyJsonObject(JSONObject loadedObject){
			obj = loadedObject;
		}

		public void put(String key, String value){
			obj.put(key, value);
		}
		
		public void put(String key, double value){
			obj.put(key, value);
		}
		
		public void put(String key, boolean value){
			obj.put(key, value);
		}
		
		public void put(String key, int value){
			obj.put(key, value);
		}
		
		public void put(String key, MyJsonObject value){
			 obj.put(key, value.getRawObject());
		}
		
		public void put(String key, MyJsonList value){
			obj.put(key, value.getRawList());
		}
		
		public JSONObject getRawObject(){
			return obj;
		}
		
		public <E> E get(String key){
			Object value = obj.get(key);
			if (value instanceof JSONArray){
				value = new MyJsonList((JSONArray)value);
			}
			else if (value instanceof JSONObject){
				value = new MyJsonObject((JSONObject)value);
			}
			return (E) value;
		}

		@Override
		public String toString() {
			return obj.toJSONString();
		}

			
		
}
