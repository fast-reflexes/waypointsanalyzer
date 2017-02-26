package wpchallenge;

import java.time.ZonedDateTime;

public class Record {
	
	/**
		Stores a recorded point of a vehicle in time including information about current speed, speed limit on the current road, position in terms of latitude and 
		longitude and finally an unambiguous point in time.
	*/
	
	private float speed, speedLimit;
	private Position pos;
	private ZonedDateTime time;
	
	public Record(float speed, float speedLimit, double latitude, double longitude, ZonedDateTime time) throws Exception {
		this.pos = new Position(latitude, longitude);
		if(speed >= 0) this.speed = speed;
		else throw new Exception("Please provide a speed greater than or equal to 0!");
		if(speedLimit > 0) this.speedLimit = speedLimit;
		else throw new Exception("Please provide a speed limit greater than 0!");
		if(time.compareTo(ZonedDateTime.now()) <= 0) this.time = time;
		else throw new Exception("Time point must be previous in time!");
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