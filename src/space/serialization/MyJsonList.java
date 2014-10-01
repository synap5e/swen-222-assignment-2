package space.serialization;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MyJsonList {
	JSONArray list;
	
	public MyJsonList(){
		list = new JSONArray();
	}

	public MyJsonList(JSONArray loadedList) {
		list = loadedList;
	}
	
	public void add(String toAdd){
		list.add(toAdd);
	}
	
	public void add(double toAdd){
		list.add(toAdd);
	}
	
	public void add(boolean toAdd){
		list.add(toAdd);
	}
	
	public void add(MyJsonObject toAdd){
		 list.add(toAdd.getRawObject());
	}
	
	public void put(MyJsonList toAdd){
		list.add(toAdd.getRawList());
	}

	public Object getRawList() {
		return list;
	}
	
	public int getSize(){
		return list.size();
	}
	
	public <E> E get(int index){
		Object value = list.get(index);
		if (value instanceof JSONArray){
			value = new MyJsonList((JSONArray)value);
		}
		else if (value instanceof JSONObject){
			value = new MyJsonObject((JSONObject)value);
		}
		return (E) value;
	}

}