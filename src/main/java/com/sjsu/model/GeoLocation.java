package com.sjsu.model;

public class GeoLocation {
    private static final Double AVG_TIME_TO_DRIVE_PER_MILE_IN_MINUTES = 1.0;
    private Double latitude;
    private Double longitude;

    //Calculated based on formula given here - http://www.movable-type.co.uk/scripts/latlong.html
    public static Double calculateDistance(GeoLocation geoLocation1, GeoLocation geoLocation2) {
        double lat1 = geoLocation1.getLatitude();
        double lon1 = geoLocation1.getLongitude();

        double lat2 = geoLocation2.getLatitude();
        double lon2 = geoLocation2.getLongitude();

        return distance(lat1, lon1, lat2, lon2);
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static double timeToTravel(GeoLocation driverStartLocation, GeoLocation riderStartLocation) {
        Double distance = calculateDistance(driverStartLocation, riderStartLocation);
        return distance * AVG_TIME_TO_DRIVE_PER_MILE_IN_MINUTES;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeoLocation)) return false;

        GeoLocation geoLocation = (GeoLocation) o;

        if (!getLatitude().equals(geoLocation.getLatitude())) return false;
        return getLongitude().equals(geoLocation.getLongitude());

    }

    @Override
    public int hashCode() {
        int result = getLatitude().hashCode();
        result = 31 * result + getLongitude().hashCode();
        return result;
    }
}
