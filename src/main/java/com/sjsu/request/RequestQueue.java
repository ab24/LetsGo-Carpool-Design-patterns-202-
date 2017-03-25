package com.sjsu.request;

import java.util.Queue;

import static com.google.common.collect.Queues.newArrayDeque;

/**
 * on 8/7/16.
 */
public class RequestQueue {
    private static Queue<ParkingRequest> parkingRequests = newArrayDeque();
    private static Queue<OfferParkingRequest> offerParkingRequests = newArrayDeque();
    private static Queue<DriveRequest> driveRequests = newArrayDeque();
    private static Queue<RideRequest> rideRequests = newArrayDeque();


    public static Queue<ParkingRequest> getParkingRequests() {
        return parkingRequests;
    }

    public static void setParkingRequests(Queue<ParkingRequest> parkingRequests) {
        RequestQueue.parkingRequests = parkingRequests;
    }

    public static Queue<OfferParkingRequest> getOfferParkingRequests() {
        return offerParkingRequests;
    }

    public static void setOfferParkingRequests(Queue<OfferParkingRequest> offerParkingRequests) {
        RequestQueue.offerParkingRequests = offerParkingRequests;
    }

    public static Queue<DriveRequest> getDriveRequests() {
        return driveRequests;
    }

    public static void setDriveRequests(Queue<DriveRequest> driveRequests) {
        RequestQueue.driveRequests = driveRequests;
    }

    public static Queue<RideRequest> getRideRequests() {
        return rideRequests;
    }

    public static void setRideRequests(Queue<RideRequest> rideRequests) {
        RequestQueue.rideRequests = rideRequests;
    }
}
