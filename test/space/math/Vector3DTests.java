package space.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class Vector3DTests {
	private static final float EPSILON = 0.0001f;
	@Test
	public void equals(){
		float x = 1.12f;
		float y = 22.91f;
		float z = 11.27f;
		Vector3D v1 = new Vector3D(x,y,z);
		Vector3D v2 = new Vector3D(x,y,z);
		assertTrue("Vectors with the same value should be equal", v1.equals(v2));
	}

	@Test
	public void notEqual(){
		float x = 1.12f;
		float y = 22.91f;
		float z = 11.27f;
		Vector3D v1 = new Vector3D(x,y,z);
		Vector3D v2 = new Vector3D(x,y,x);
		assertFalse("Vectors with different values should not be equal",v1.equals(v2));
	}

	@Test
	public void dotProduct1(){
		Vector3D v1 = new Vector3D(12.4f,14.1f,6.92f);
		Vector3D v2 = new Vector3D(3.26f,20.9f,10.7f);
		assertTrue("Expected result was: 409.158 but actual result was: "+v1.dot(v2),Math.abs(409.158f - v1.dot(v2)) < EPSILON);
	}

	@Test
	public void dotProduct2(){
		Vector3D v1 = new Vector3D(12,11,9);
		Vector3D v2 = new Vector3D(5,13,37);
		assertTrue("Expected result was: 536 but actual result was: "+v1.dot(v2),Math.abs(536 - v1.dot(v2)) < EPSILON);
	}

	@Test
	public void crossProduct(){
		Vector3D v1 = new Vector3D(6.6f,1,4.1f);
		Vector3D v2 = new Vector3D(4,2,10.2f);
		Vector3D result = new Vector3D(2,-50.92f,9.2f);
		assertEquals(result,v1.cross(v2));
	}

	@Test
	public void add(){
		Vector3D v1 = new Vector3D(30.5f,1.9f,0);
		Vector3D v2 = new Vector3D(1,2,7.32f);
		Vector3D result = new Vector3D(31.5f,3.9f,7.32f);
		assertEquals(result,v1.add(v2));
		assertFalse("Should not have changed original vector",v1.equals(result));
		assertFalse("Should not have changed original vector",v2.equals(result));
	}

	@Test
	public void addLocal(){
		Vector3D v1 = new Vector3D(30.5f,1.9f,0);
		Vector3D v2 = new Vector3D(1,2,7.32f);
		Vector3D result = new Vector3D(31.5f,3.9f,7.32f);
		assertEquals(result,v1.addLocal(v2));
		assertEquals(result,v1);
		assertFalse("Should not have changed other vector",result.equals(v2));
	}

	@Test
	public void subtract(){
		Vector3D v1 = new Vector3D(12,14,7);
		Vector3D v2 = new Vector3D(3.5f,20,10);
		Vector3D result = new Vector3D(8.5f,-6,-3f);
		assertEquals(result,v1.sub(v2));
		assertFalse("Should not have changed original vector",v1.equals(result));
		assertFalse("Should not have changed original vector",v2.equals(result));
	}

	@Test
	public void subtractLocal(){
		Vector3D v1 = new Vector3D(12,14,7);
		Vector3D v2 = new Vector3D(3.5f,20,10);
		Vector3D result = new Vector3D(8.5f,-6,-3f);
		assertEquals(result,v1.subLocal(v2));
		assertEquals(result,v1);
		assertFalse("Should not have changed other vector",v2.equals(result));
	}

	@Test
	public void multiply(){
		Vector3D v = new Vector3D(3.3f,2.3f,7);
		Vector3D result = new Vector3D(23.1f,16.1f,49);
		assertEquals(result,v.mul(7));
		assertFalse(result.equals(v));
	}

	@Test
	public void multiplyLocal(){
		Vector3D v = new Vector3D(3.3f,2.3f,7);
		Vector3D result = new Vector3D(23.1f,16.1f,49);
		assertEquals(result,v.mulLocal(7));
		assertTrue(result.equals(v));
	}

	@Test
	public void squareLength(){
		Vector3D v = new Vector3D(2.2f,8.12f,3.29f);
		assertTrue(Math.abs(81.5985 - v.sqLen()) < EPSILON);
	}

}
