package com.sjsu.track;

import com.sjsu.model.MemberId;


public class Trip extends TrackingInfo {

    private MemberId driverId;
    private String vehicleId;
    private int availableSeats;
    private double miles;


    public MemberId getDriverId() {
        return driverId;
    }

    public void setDriverId(MemberId driverId) {
        this.driverId = driverId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }


    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getMiles() {
        return miles;
    }

    public void setMiles(double miles) {
        this.miles = miles;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "driverId=" + driverId +
                ", vehicleId='" + vehicleId + '\'' +
                ", availableSeats=" + availableSeats +
                ", miles=" + miles + " " + super.toString() +
                '}';
    }
}
