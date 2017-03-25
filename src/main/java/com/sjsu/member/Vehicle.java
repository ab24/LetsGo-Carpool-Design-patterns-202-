package com.sjsu.member;


import com.sjsu.util.IdGenerator;

/**
 * Created by Arun on 8/6/16.
 * Modified by abhasin on 8/13/16
 */
public class Vehicle {
    String vehicleMake;
    String vehicleNo;
    int seatCount;
    String vehicleId;


    public Vehicle(String vehicleMake, String vehicleNo, int seatCount) {
        this.vehicleId = IdGenerator.generateId("V");
        this.setVehicleMake(vehicleMake);
        this.setVehicleNo(vehicleNo);
        this.setSeatCount(seatCount);
    }

    public Vehicle() {

    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
}
