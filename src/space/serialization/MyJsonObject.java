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
			this.key=key;
		}
		
		public void put(String key, double value){
			obj.put(key, value);
			this.key=key;
		}
		
		public void put(String key, boolean value){
			obj.put(key, value);
			this.key=key;
		}
		
		
		public void put(String key, MyJsonObject value){
			 obj.put(key, value.getRawObject());
			 this.key=key;
		}
		
		public void put(String key, MyJsonList value){
			obj.put(key, value.getRawList());
			this.key=key;
		}
		
		public JSONObject getRawObject(){
			return obj;
		}
		

		@Override
		public String toString() {
			return obj.toJSONString();
		}

		public MyJsonList getMyJsonList(String string) {
			MyJsonList list = new MyJsonList((JSONArray) obj.get(string));
			for(Object k:obj.keySet()){
				this.key = (String) k;
				System.out.println(this.key);
				
			}
			return list;
		}
		public String getString(String string) {
			return (String) obj.get(string);
		}
		public boolean getBoolean(String string) {
			return (boolean) obj.get(string);
		}
		public double getNumber(String string) {
			return (double) obj.get(string);
		}

		public MyJsonObject getMyJsonObject(String string) {
			return new MyJsonObject((JSONObject) obj.get(string));
		}
			
		
}
