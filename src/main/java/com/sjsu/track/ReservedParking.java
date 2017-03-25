package com.sjsu.track;


public class ReservedParking extends TrackingInfo {
    private String parkingId;

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    @Override
    public String toString() {
        return "ReservedParking{" +
                "parkingId='" + parkingId + '\'' +
                '}' + super.toString();
    }
}
