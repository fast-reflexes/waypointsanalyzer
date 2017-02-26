package wpchallenge.test;

import junit.framework.TestCase;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import wpchallenge.Record;

public class RecordTest extends TestCase {
	private Record r1;
	private Record r2;

	public RecordTest(String testName) {
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
			r1 = new Record(-95f, 100f, 0.0d, 0.0d, ZonedDateTime.now());
			fail("Illegal speed!"); 
		}
		catch(Exception e) { }
	}
	
	public void testConstructor2() {
		try {
			r1 = new Record(95f, 0f, 0.0d, 0.0d, ZonedDateTime.now());
			fail("Illegal speed limit!"); 
		}
		catch(Exception e) { }
	}
	
	public void testConstructor3() {
		try {
			r1 = new Record(95f, 10f, 0.0d, -200.0d, ZonedDateTime.now());
			fail("Illegal position!"); 
		}
		catch(Exception e) { }
	}
	
	public void testConstructor4() {
		try {
			r1 = new Record(95f, 10f, 0.0d, -200.0d, ZonedDateTime.now().plus(10, ChronoUnit.HOURS));
			fail("Illegal time!"); 
		}
		catch(Exception e) { }
	}
	
	public void testGetters() {
		try { 
			ZonedDateTime z = ZonedDateTime.now();
			r1 = new Record(60.0f, 100.0f, -90.0d, 180.0d, z); 
			assertTrue("speed different from what was set", r1.getSpeed() == 60.0f);
			assertTrue("speed limit different from what was set", r1.getSpeedLimit() == 100.0f);
			assertTrue("position different from what was set", r1.getPosition().getLatitude() == -90.0d && r1.getPosition().getLongitude() == 180.0d);
			assertTrue("time different from what was set", r1.getTime().equals(z) == true);
		}
		catch(Exception e) { fail("Legal constructor call"); }
	}
}