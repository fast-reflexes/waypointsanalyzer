package wpchallenge;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

public class WaypointAnalyzer  {
	
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

	/**
		Reads json input from a given file and fills the list with records with Record object.
		
			@param fileName = the name of the file in which the json data to read resides
		
			@return = a boolean indicating whether this operation went well or not
	*/
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
			Collections.sort(records, new RecordTemporalComparator()); // make sure the list is sorted according to time
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
	
	/**
		Analyzes the data in the records array and prints out key facts that can be deduced from these Records in terms of total distance travelled, total time travelled,
		total distance speeding and total time speeding. We assume that the points are ordered according to time and for each pair of points, we calculate the euclidean
		distance in between along with the resulting average speed results. Note well that the real way between these points is, if anything, longer than what we use since 
		the fastest way between two points is the euclidean distance. We assume that no intermediary speed limit has occurred between the readings (since they are hopefully
		done with short intervals) and concludes that if the calculated average speed is higher than the maximum of the speed limits at the two points, then, without doubt,
		we are above a lower threshold for when a vehicle must have been considered speeding. During calculations, we also take into account that the Haversine formula used
		in the Position class is only accurate within 0.5%.
		
			@param printIntermediaryResult = boolean indicating whether we want vebose output during calculations or not
	*/
	public void printSummary(boolean printIntermediaryResults) {
		double dS, dT, tempDistance, tempSpeed; // distance speeding and total distance
		long tS, tT, tempTime; // time speeding and total time
		Record r1, r2;
		dS = dT = tempDistance = tempSpeed = 0.0d;
		tS = tT = tempTime = 0;
		
		System.out.println("Printing summary: ");
		for(int i = 0; i < records.size() - 1; ++i) {
			r1 = records.get(i); // first road segment
			r2 = records.get(i + 1);
			tempDistance = 1000 * r1.getPosition().distanceTo(r2.getPosition()) / 1.005d; // since the Haversine formula is not 100% accurate because of Earth curvature
			tempTime = r1.getTime().until(r2.getTime(), ChronoUnit.SECONDS);
			tempSpeed = tempDistance / (double) tempTime;
			dT += tempDistance; // add to total accumulators
			tT += tempTime;
			if(tempSpeed > Math.max(r1.getSpeedLimit(), r2.getSpeedLimit())) { // if the highest speed limit of both points is below our result, then we have a speeder!
				dS += tempDistance; // add to speeding accumulators
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