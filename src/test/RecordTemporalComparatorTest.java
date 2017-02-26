package wpchallenge.test;

import wpchallenge.RecordTemporalComparator;
import wpchallenge.Record;
import junit.framework.TestCase;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class RecordTemporalComparatorTest extends TestCase {
	private RecordTemporalComparator rtc;

	public RecordTemporalComparatorTest(String testName) {
		super(testName);
	}

	protected void setUp() throws Exception {
		super.setUp();
		rtc = new RecordTemporalComparator();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void test1() {
		try {
			int res = rtc.compare(new Record(10f, 20f, 0.0d, 0.0d, ZonedDateTime.now()), new Record(10f, 20f, 0.0d, 0.0d, ZonedDateTime.now().minus(10, ChronoUnit.SECONDS)));
			assertTrue("Wrong ordering of dates, should return positive number", res > 0);
		}
		catch(Exception e) { fail("Legal constructor calls"); }
	}
	
	public void test2() {
		try {
			int res = rtc.compare(new Record(10f, 20f, 0.0d, 0.0d, ZonedDateTime.now().minus(10, ChronoUnit.SECONDS)), new Record(10f, 20f, 0.0d, 0.0d, ZonedDateTime.now()));
			assertTrue("Wrong ordering of dates, should return negative number", res < 0);
		}
		catch(Exception e) { fail("Legal constructor calls"); }
	}
	
	public void test3() {
		try {
			ZonedDateTime z = ZonedDateTime.now();
			int res = rtc.compare(new Record(10f, 20f, 0.0d, 0.0d, z), new Record(10f, 20f, 0.0d, 0.0d, z));
			assertTrue("Wrong ordering of dates, should return 0", res == 0);
		}
		catch(Exception e) { fail("Legal constructor calls"); }
	}

	
}