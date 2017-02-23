import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

public class WaypointAnalyzer  {

	private static class Position {
		private double latitude, longitude;
		private static final int EARTH_RADIUS = 6371; // in kms
		
		public Position(double latitude, double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}
		
		public double getLongitude() {
			return longitude;
		}
		
		public double getLatitude() {
			return latitude;
		}
		
		public double distanceTo(Position p) {
			double p1Lat, p1Long, p2Lat, p2Long;
			p1Lat = Math.toRadians(latitude);
			p2Lat = Math.toRadians(p.getLatitude());
			p1Long = Math.toRadians(longitude);
			p2Long = Math.toRadians(p.getLongitude());
			//d = 2r * arcsin(sqrt(sin2((lat2 - lat1) / 2) + cos(lat1) * cos(lat2) * sin2((long2 - long1) / 2)))
			double rp = Math.pow(Math.sin((p2Lat - p1Lat) / ((double) 2)), 2) + Math.cos(p1Lat) * Math.cos(p2Lat) *  Math.pow(Math.sin((p2Long - p1Long) / ((double) 2)), 2);
			return 2 * EARTH_RADIUS * Math.asin(Math.sqrt(rp));
		}
	}
	
	public static class Record {
		private float speed, speedLimit;
		private Position pos;
		private ZonedDateTime time;
		
		public Record(float speed, float speedLimit, double latitude, double longitude, ZonedDateTime time) {
			this.pos = new Position(latitude, longitude);
			this.speed = speed;
			this.speedLimit = speedLimit;
			this.time = time;
		}
		
		public ZonedDateTime getTime() {
			return time;
		}
		
		public Position getPosition() {
			return pos;
		}
		
		public float getSpeed() {
			return speed;
		}
		
		public float getSpeedLimit() {
			return speedLimit;
		}
		
		@Override
		public String toString() {
			return new String("speed: " + speed + ", speed limit: " + speedLimit + ", position: " + pos.getLatitude() + " / " + pos.getLongitude() + ", time: " + time.toString());
		}
		
	}
	
	public static class RecordTemporalComparator implements Comparator<Record> {
		
		public int compare(Record r1, Record r2) {
			return r1.getTime().compareTo(r2.getTime());
		}
		
		public boolean equals(Object o) {
			if(o instanceof RecordTemporalComparator) return true;
			else return false;
		}

	}
	
	private JsonParser jp;
	private ArrayList<Record> records;
	
	public WaypointAnalyzer() {
		jp = new JsonParser();
	}
	
	public static void main(String[] args) {
		WaypointAnalyzer wa;
		boolean success;
		if(args.length > 0) {
			wa = new WaypointAnalyzer();
			success = wa.readInput(args[0]);
			if(success) {
				wa.printRecords();
				wa.printSummary(true);
			}
		}
		else System.out.println("Please supply a file name where the input data can be found ...");
		System.exit(0);
	}

	public boolean readInput(String fileName) {
		char[] data = null;
		int fileLength;
		records = new ArrayList<Record>();
		File inp;
		FileReader fr = null;
		String s;
		try {
			inp = new File(fileName);
			fileLength = (int) inp.length();
			data = new char[fileLength];
			fr = new FileReader(inp);
			fr.read(data, 0, fileLength);
			fr.close();
			s = new String(data);
			
			JsonArray ja = (JsonArray) jp.parse(s);
			JsonObject current, position;
			for(int i = 0 ; i < ja.size(); ++i) {
				current = ja.get(i).getAsJsonObject();
				position = current.getAsJsonObject("position");
				records.add(new Record(
								current.get("speed").getAsFloat(), 
								current.get("speed_limit").getAsFloat(), 
								position.get("latitude").getAsFloat(), 
								position.get("longitude").getAsFloat(),
								ZonedDateTime.parse(current.get("timestamp").getAsString()) //ex. ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]")
							)
				);
			}
			Collections.sort(records, new RecordTemporalComparator());
			return true;
		}
		catch(IOException ioe) {
			System.out.println(ioe.getMessage());
			System.out.println("Something went wrong while attempting to read the file, please try again ...");
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Something went wrong when processing the input data, is it a correctly formatted JSON array with waypoint objects ...?");
		}
		return false;
	}
	
	public void printSummary(boolean printIntermediaryResults) {
		double dS, dT, tempDistance, tempSpeed; // distance speeding and total distance
		long tS, tT, tempTime; // time speeding and total time
		Record r1, r2;
		dS = dT = tempDistance = tempSpeed = 0.0d;
		tS = tT = tempTime = 0;
		
		System.out.println("Printing summary: ");
		for(int i = 0; i < records.size() - 1; ++i) {
			r1 = records.get(i);
			r2 = records.get(i + 1);
			tempDistance = 1000 * r1.getPosition().distanceTo(r2.getPosition());
			tempTime = r1.getTime().until(r2.getTime(), ChronoUnit.SECONDS);
			tempSpeed = tempDistance / (double) tempTime;
			dT += tempDistance;
			tT += tempTime;
			if(tempSpeed > Math.max(r1.getSpeedLimit(), r2.getSpeedLimit())) {
				dS += tempDistance;
				tS += tempTime;
			}
			if(printIntermediaryResults) {
				System.out.println("Record " + i + " to " + (i + 1) + ": distance: " + tempDistance + " m, avg. speed: " + tempSpeed + " m/s, time: " + tempTime + " s, max speed limit: " + Math.max(r1.getSpeedLimit(), r2.getSpeedLimit()) + " m/s");
			}
		}
		System.out.println("Distance Speeding: " + (int) dS);
		System.out.println("Duration Speeding: " + tS);
		System.out.println("Distance Total: " + (int) dT);
		System.out.println("Duration Total: " + tT);
	}
	
	public void printRecords() {
		Iterator<Record> it = records.iterator();
		System.out.println("Printing the records: ");
		while(it.hasNext()) {
			System.out.println(it.next().toString());
		}
		System.out.println();
	}

}