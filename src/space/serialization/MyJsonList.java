package space.serialization;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Represents a MyJsonList for classes and entities in the game this is a
 * wrapper class for the raw JSONArray from the library
 *
 * It enables type safety rather than being generic - to avoid accidently put
 * the wrong data type
 *
 * @author Shweta Barapatre
 *
 */

public class MyJsonList implements Iterable<JSONObject> {
	JSONArray list;

	/**
	 * constructor
	 */
	public MyJsonList() {
		list = new JSONArray();
	}

	/**
	 * constructor that takes an already loaded list
	 * 
	 * @param loadedList
	 *            JSONArray
	 */
	public MyJsonList(JSONArray loadedList) {
		list = loadedList;
	}

	/**
	 * add a string to myjsonlist
	 * 
	 * @param toAdd
	 *            string to add
	 */
	public void add(String toAdd) {
		list.add(toAdd);
	}

	/**
	 * add a number to the myjsonlist
	 * 
	 * @param toAdd
	 *            value to add
	 */
	public void add(double toAdd) {
		list.add(toAdd);
	}

	/**
	 * add a boolean to myjsonlist
	 * 
	 * @param toAdd
	 *            boolean to add
	 */
	public void add(boolean toAdd) {
		list.add(toAdd);
	}

	/**
	 * add a myjsonobject to myjsonlist
	 * 
	 * @param toAdd
	 *            myjsonobject to add
	 */
	public void add(MyJsonObject toAdd) {
		list.add(toAdd.getRawObject());
	}

	/**
	 * add a myjsonlist to myjsonlist
	 * 
	 * @param toAdd
	 *            myjsonlist to add
	 */
	public void add(MyJsonList toAdd) {
		list.add(toAdd.getRawList());
	}

	/**
	 * returns original raw JSONArray
	 * 
	 * @return JSONArray
	 */
	public Object getRawList() {
		return list;
	}

	/**
	 * returns size of the list
	 * 
	 * @return int size
	 */
	public int getSize() {
		return list.size();
	}

	@Override
	public Iterator<JSONObject> iterator() {
		return list.iterator();
	}

	/**
	 * gets a myjsonlist from the specified index
	 * 
	 * @param i
	 *            index
	 * @return myjsonlist at index
	 */
	public MyJsonList getMyJsonList(int i) {
		return new MyJsonList((JSONArray) list.get(i));
	}

	/**
	 * returns a number from myjsonlist at that index
	 * 
	 * @param i
	 *            index
	 * @return value
	 */
	public double getNumber(int i) {
		return (double) list.get(i);
	}

	/**
	 * returns myjsonobject from specified index
	 * 
	 * @param i
	 *            index
	 * @return myjsonobject
	 */
	public MyJsonObject getMyJsonObject(int i) {
		return new MyJsonObject((JSONObject) list.get(i));
	}

}
