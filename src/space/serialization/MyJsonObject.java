package space.serialization;

import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Represents a MyJsonObject for classes and entities in the game this is a
 * wrapper class for the raw JSONObject from the library
 * 
 * It enables type safety rather than just being generic - to avoid accidently
 * put the wrong data type
 * 
 * @author Shweta Barapatre(300287438)
 *
 */

public class MyJsonObject {
	JSONObject obj;

	/**
	 * Constructor - creates a new Json object
	 */
	public MyJsonObject() {
		obj = new JSONObject();
	}

	/**
	 * Second constructor that takes a loaded
	 * 
	 * @param loadedObject
	 *            JSONObject with data
	 */
	public MyJsonObject(JSONObject loadedObject) {
		obj = loadedObject;
	}

	/**
	 * Puts a string value into myjsonobject
	 * 
	 * @param key
	 *            string key
	 * @param value
	 *            string value to add
	 */
	public void put(String key, String value) {
		obj.put(key, value);
	}

	/**
	 * puts a double value into myjsonobject
	 * 
	 * @param key
	 *            string key
	 * @param value
	 *            double value to add
	 */
	public void put(String key, double value) {
		obj.put(key, value);
	}

	/**
	 * puts a boolean value into myjsonobject
	 * 
	 * @param key
	 *            string key
	 * @param value
	 *            boolean value to add
	 */
	public void put(String key, boolean value) {
		obj.put(key, value);
	}

	/**
	 * puts a myjsonobject value into myjsonobject
	 * 
	 * @param key
	 *            string key
	 * @param value
	 *            myjsonobject value to add
	 */
	public void put(String key, MyJsonObject value) {
		obj.put(key, value.getRawObject());
	}

	/**
	 * puts a myjsonlist value into myjsonobject
	 * 
	 * @param key
	 *            string key
	 * @param value
	 *            myjsonobject value
	 */
	public void put(String key, MyJsonList value) {
		obj.put(key, value.getRawList());
	}

	/**
	 * returns raw original JSONObject
	 * 
	 * @return original JSONObject
	 */
	public JSONObject getRawObject() {
		return obj;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return obj.toJSONString();
	}

	/**
	 * gets myjsonlist from given string key
	 * 
	 * @param string
	 *            key to identify value
	 * @return myjsonlist value from key
	 */
	public MyJsonList getMyJsonList(String string) {
		MyJsonList list = new MyJsonList((JSONArray) obj.get(string));
		return list;
	}

	/**
	 * gets String from given string key
	 * 
	 * @param string
	 *            key to identify value
	 * @return String value from key
	 */
	public String getString(String string) {
		return (String) obj.get(string);
	}

	/**
	 * gets boolean from given string key
	 * 
	 * @param string
	 *            key to identify value
	 * @return boolean value from key
	 */
	public boolean getBoolean(String string) {
		return (boolean) obj.get(string);
	}

	/**
	 * gets number value from given string key
	 * 
	 * @param string
	 *            key to identify value
	 * @return double value from key
	 */
	public double getNumber(String string) {
		return (double) obj.get(string);
	}

	/**
	 * gets myjsonobject from given string key
	 * 
	 * @param string
	 *            key to identify value
	 * @return myjsonobject value from key
	 */
	public MyJsonObject getMyJsonObject(String string) {
		MyJsonObject object = new MyJsonObject((JSONObject) obj.get(string));
		return object;
	}

}
