package wpchallenge;

public class Position {
	
	/**
		Stores a position in terms of a longitude and latitude
	*/
	
	private double latitude, longitude;
	private static final int EARTH_RADIUS = 6371; // in kms
	
	public Position(double latitude, double longitude) throws Exception {
		if(latitude < -90d || latitude > 90d) throw new Exception("Please provide a latitude between -90 and 90 (inclusive)");
		else this.latitude = latitude;
		if(longitude < -180d || longitude > 180d) throw new Exception("Please provide a longitude between -180 and 180 (inclusive)");
		else this.longitude = longitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	/**
		Calculates the distance between two points on Earth given in terms of longitude and latitude. Several formula for this exist, the one being implemented here
		being the Haversine formula. This does not take into account that the Earth is NOT a perfect sphere which affects the results in the way that there are only 
		correct within 0.5% from the real value. The Haversine formula uses radians.
		
			@param p = the position to which we want to measure the distance
		
			@return = the distance in kilometers between this point and p up to 0.5% accuracy.
	*/
	public double distanceTo(Position p) {
		double p1Lat, p1Long, p2Lat, p2Long;
		p1Lat = Math.toRadians(latitude); // convert to radians
		p2Lat = Math.toRadians(p.getLatitude());
		p1Long = Math.toRadians(longitude);
		p2Long = Math.toRadians(p.getLongitude());
		//d = 2r * arcsin(sqrt(sin2((lat2 - lat1) / 2) + cos(lat1) * cos(lat2) * sin2((long2 - long1) / 2)))
		double rp = Math.pow(Math.sin((p2Lat - p1Lat) /  2d), 2) + Math.cos(p1Lat) * Math.cos(p2Lat) *  Math.pow(Math.sin((p2Long - p1Long) / 2d), 2);
		return 2d * EARTH_RADIUS * Math.asin(Math.sqrt(rp)); // note, rp can never be negative since sin^2 is always positive and cos is positive when >= -90 and <= 90.
	}
}