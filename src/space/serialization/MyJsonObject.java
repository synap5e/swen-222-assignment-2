package space.serialization;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MyJsonObject {
	JSONObject obj;
	
		public MyJsonObject(){
			obj = new JSONObject();
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
