package com.sjsu.track;


public class Ride extends TrackingInfo {

    private String rideId;

    private double miles;

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public double getMiles() {
        return miles;
    }

    public void setMiles(double miles) {
        this.miles = miles;
    }


    @Override
    public String toString() {
        return "Ride{" +
                "rideId='" + rideId + '\'' +
                ", miles=" + miles + " " + super.toString() +

                '}';
    }
}
