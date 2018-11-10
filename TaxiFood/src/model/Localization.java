package model;

import java.math.BigDecimal;

import pember.kmeans.geo.GeographicPoint;

public class Localization implements GeographicPoint {
	private BigDecimal latitude;
	private BigDecimal longitude;
    public static final double R = 6372.8 * 1000;
	
	public Localization(BigDecimal latitude, BigDecimal longitude) {
		if(latitude == null) {
			throw new IllegalArgumentException("Latitude cannot be null.");
		}
		if(longitude == null) {
			throw new IllegalArgumentException("Longitude cannot be null.");
		}
		
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	public BigDecimal getLongitude() {
		return longitude;
	}
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	@Override
	public String toString() {
		return "Localization [Latitude=" + latitude + ", Longitude=" + longitude + "]";
	}
	
	public double distanceTo(Localization localization) {
		 // taken from http://rosettacode.org/wiki/Haversine_formula#Java
		double lat1 = this.latitude.doubleValue();
		double lat2 = localization.latitude.doubleValue();

        double dLat = Math.toRadians( lat2 - lat1 );
        double dLon = Math.toRadians(localization.longitude.doubleValue() - this.longitude.doubleValue());
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        // R is the radius of the earth, set above statically
        return R * c;
	}
	
	
}
