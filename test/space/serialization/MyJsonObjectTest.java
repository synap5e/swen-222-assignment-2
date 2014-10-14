package space.serialization;
import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.junit.Test;

public class MyJsonObjectTest {
	
	@Test
	public void validString(){
		MyJsonObject object  = new MyJsonObject();
		object.put("test", "foo");
		assertTrue("the string foo should be the value", object.getString("test").equals("foo"));
	}
	@Test
	public void validDouble(){
		MyJsonObject object  = new MyJsonObject();
		object.put("test", 1);
		assertTrue("the number 1 should be the value", object.getNumber("test")==1);
	}
	@Test
	public void validMyJsonObject(){
		MyJsonObject toPut = new MyJsonObject();
		toPut.put("addedtotoPut",1);
		MyJsonObject object  = new MyJsonObject();
		object.put("test", toPut);
		MyJsonObject gotten = object.getMyJsonObject("test");
		assertTrue("the value from added MyJsonObject should be 1", gotten.getNumber("addedtotoPut")==1);
	}
	@Test
	public void validBoolean(){
		MyJsonObject object  = new MyJsonObject();
		object.put("test", true);
		assertTrue("the value should be true", object.getBoolean("test")==true);
	}
	
	@Test
	public void validmyJsonList(){
		MyJsonObject object  = new MyJsonObject();
		MyJsonList list = new MyJsonList();
		list.add(1.5);
		object.put("test", list);
		MyJsonList gotten = object.getMyJsonList("test");
		assertTrue("the value should be 1", gotten.getNumber(0)==1.5);
		
	}
	@Test
	public void validRawObject(){
		JSONObject raw = new JSONObject();
		MyJsonObject object  = new MyJsonObject(raw);
		assertTrue(object.getRawObject().equals(raw));
	}
	
	@Test
	public void validPutString(){
		MyJsonObject object  = new MyJsonObject();
		object.put("test", "foo");
		assertTrue("the string foo should be the value", object.getString("test").equals("foo"));
	}
	
	@Test
	public void testCastExceptionStringNotBoolean(){
		MyJsonObject object = new MyJsonObject();
		object.put("test", "foo");
		try{
			object.getBoolean("test");
			assert false;
		}
		catch(ClassCastException e){
			assert true;
		}
	}
	
	@Test
	public void testCastExceptionDoubleNotString(){
		MyJsonObject object = new MyJsonObject();
		object.put("test", 0);
		try{
			object.getString("test");
			assert false;
		}
		catch(ClassCastException e){
			assert true;
		}
	}

	
}
