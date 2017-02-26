package wpchallenge.test;

import junit.framework.TestCase;
import wpchallenge.Position;

public class PositionTest extends TestCase {
	private Position p1;;
	private Position p2;

	public PositionTest(String testName) {
		super(testName);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testConstructor1() {
		try {
			p1 = new Position(-95d, 100);
			fail("Illegal constructor arguments!"); 
		}
		catch(Exception e) { }
	}
	
	public void testConstructor2() {
		try { p1 = new Position(0.0d, 0.0d); }
		catch(Exception e) { fail("Legal constructor parameters"); }
	}
	
	public void testConstructor3() {
		try { p1 = new Position(90.0d, 180.0d); }
		catch(Exception e) { fail("Legal constructor parameters"); }
	}
	
	public void testDistance1() {
		try { 
			p1 = new Position(90.0d, 180.0d); 
			p2 = new Position(90.0d, 180.0d); 
			double dist = p1.distanceTo(p2);
			assertTrue("Distance must be positive!", dist >= 0.0d);
		}
		catch(Exception e) { fail("Legal distance call"); }
	}
	
	public void testDistance2() {
		try { 
			p1 = new Position(0.0d, 0.0d); 
			p2 = new Position(0.0d, 0.0d); 
			double dist = p1.distanceTo(p2);
			assertTrue("Distance must be positive!", dist == 0.0d);
		}
		catch(Exception e) { fail("Legal distance call"); }
	}
	
	public void testDistance3() {
		try { 
			p1 = new Position(0.0d, -180.0d); 
			p2 = new Position(0.0d, 180.0d); 
			double dist = p1.distanceTo(p2);
			assertTrue("Distance must be positive!", dist > 0.0d);
			assertTrue("", dist <= 0.0001d);
		}
		catch(Exception e) { fail("Legal distance call"); }
	}
	
	public void testDistance4() {
		try { 
			p1 = new Position(90.0d, -180.0d); 
			p2 = new Position(-90.0d, 180.0d); 
			double dist = p1.distanceTo(p2);
			assertTrue("Distance must be positive!", dist > 0.0d);
		}
		catch(Exception e) { fail("Legal distance call"); }
	}
	
	public void testLatLong() {
		try { 
			p1 = new Position(-90.0d, 180.0d); 
			assertTrue("Latitude must be >= -90 ans <= 90!", p1.getLatitude() == -90.0d);
			assertTrue("Longitude must be >= -180 ans <= 180!", p1.getLongitude() == 180.0d);
		}
		catch(Exception e) { fail("Legal distance call"); }
	}
}