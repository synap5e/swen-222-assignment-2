package space.serialization;
import static org.junit.Assert.*;

import org.junit.Test;
public class MyJsonListTests {

	@Test
	public void validString(){
		MyJsonList list = new MyJsonList();
		list.add("foo");
		list.add("bar");
		assertTrue("first string should be foo", list.getString(0).equals("foo"));
		assertTrue("second string should be bar", list.getString(1).equals("bar"));
	}
	
	@Test
	public void invalidString(){
		MyJsonList list = new MyJsonList();
		list.add("bar");
		list.add("foo");
		assertFalse("first string should not be foo", list.getString(0).equals("foo"));
		assertFalse("second string should not be bar", list.getString(1).equals("bar"));
	}
	
	@Test
	public void validDouble(){
		MyJsonList list = new MyJsonList();
		list.add(0);
		list.add(1);
		assertTrue("first value should be 0", list.getNumber(0)==0);
		assertTrue("second value should be 1", list.getNumber(1)==1);
	}
	
	@Test
	public void invalidDouble(){
		MyJsonList list = new MyJsonList();
		list.add(1);
		list.add(2);
		assertFalse("first value should not be 0", list.getNumber(0)==0);
		assertFalse("second value should not be 1", list.getNumber(1)==1);
	}
	
	@Test
	public void validBoolean(){
		MyJsonList list = new MyJsonList();
		list.add(true);
		list.add(false);
		assertTrue("first value should be true", list.getBoolean(0)==true);
		assertTrue("second value should be false", list.getBoolean(1)==false);
	}
	
	@Test
	public void invalidBoolean(){
		MyJsonList list = new MyJsonList();
		list.add(false);
		list.add(true);
		assertFalse("first value should be not be true", list.getBoolean(0)==true);
		assertFalse("second value should be not be false", list.getBoolean(1)==false);
	}
	
	@Test
	public void validMyJsonObject(){
		MyJsonList list = new MyJsonList();
		MyJsonObject object = new MyJsonObject();
		object.put("test", true);
		list.add(object);
		MyJsonObject gotten = list.getMyJsonObject(0);
		assertTrue("value should be foo", object.getBoolean("test"));
	}
	
	@Test
	public void validMyJsonList(){
		MyJsonList list = new MyJsonList();
		MyJsonObject object  = new MyJsonObject();
		list.add("foo");
		list.add(true);
		object.put("test", list);
		MyJsonList gotten = object.getMyJsonList("test");
		assertTrue("the first value should be foo", gotten.getString(0).equals("foo"));
		assertTrue("the second value should be true", gotten.getBoolean(1)==true);
	}
	
	public void validSize(){
		MyJsonList list = new MyJsonList();
		list.add("foo");
		list.add(12);
		list.add("false");
		assertTrue(list.getSize()==3);
	}
	
	@Test
	public void testCastExceptionStringNotBoolean(){
		MyJsonList list = new MyJsonList();
		list.add("foo");
		try{
			list.getBoolean(0);
			assert false;
		}
		catch(ClassCastException e){
			assert true;
		}
	}
	
	@Test
	public void testCastExceptionDoubleNotString(){
		MyJsonList list = new MyJsonList();
		list.add(0);
		try{
			list.getString(0);
			assert false;
		}
		catch(ClassCastException e){
			assert true;
		}
	}
}
