package com.mycompany.myapp.service;

import org.springframework.stereotype.Service;

//https://stackoverflow.com/questions/32771458/distance-from-lat-lng-point-to-minor-arc-segment
@Service
public class Distance {
	public double pointToLineDistance(double lon1, double lat1, double lon2, double lat2, double lon3, double lat3) {
	    lat1 = Math.toRadians(lat1);
	    lat2 = Math.toRadians(lat2);
	    lat3 = Math.toRadians(lat3);
	    lon1 = Math.toRadians(lon1);
	    lon2 = Math.toRadians(lon2);
	    lon3 = Math.toRadians(lon3);

	    // Earth's radius in meters
	    double R = 6371000;

	    // Prerequisites for the formulas
	    double bear12 = bear(lat1, lon1, lat2, lon2);
	    double bear13 = bear(lat1, lon1, lat3, lon3);
	    double dis13 = dis(lat1, lon1, lat3, lon3);

	    // Is relative bearing obtuse?
	    if (Math.abs(bear13 - bear12) > (Math.PI / 2))
	        return dis13;

	    // Find the cross-track distance.
	    double dxt = Math.asin(Math.sin(dis13 / R) * Math.sin(bear13 - bear12)) * R;

	    // Is p4 beyond the arc?
	    double dis12 = dis(lat1, lon1, lat2, lon2);
	    double dis14 = Math.acos(Math.cos(dis13 / R) / Math.cos(dxt / R)) * R;
	    if (dis14 > dis12)
	        return dis(lat2, lon2, lat3, lon3);
	    return Math.abs(dxt);
	}

	private double dis(double latA, double lonA, double latB, double lonB) {
	    double R = 6371000;
	    return Math.acos(Math.sin(latA) * Math.sin(latB) + Math.cos(latA) * Math.cos(latB) * Math.cos(lonB - lonA)) * R;
	}

	private double bear(double latA, double lonA, double latB, double lonB) {
	    // BEAR Finds the bearing from one lat / lon point to another.
	    return Math.atan2(Math.sin(lonB - lonA) * Math.cos(latB), Math.cos(latA) * Math.sin(latB) - Math.sin(latA) * Math.cos(latB) * Math.cos(lonB - lonA));
	}
}
