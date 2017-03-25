package com.sjsu.track;


import com.sjsu.request.Request;

public abstract class ServiceTracker {

    protected Request request;

    protected ServiceTracker(Request request) {
        this.request = request;
    }

    public static ServiceTracker getTracker(Request request) {
        switch (request.getRequestType()) {
            case DRIVE_NOW:
            case DRIVE_SCHEDULE:
                return new TripTracker(request);
            case RIDE_NOW:
            case RIDE_SCHEDULE:
                return new RideTracker(request);
            case PARKING:
            case OFFER_PARKING:
                return new ParkingTrackerNew(request);
        }
        return null;
    }

    public abstract void startTracking();

    public abstract void endTracking();
}
