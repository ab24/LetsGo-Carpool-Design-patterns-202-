package com.sjsu.track;

import com.sjsu.dao.ParkingDAO;
import com.sjsu.dao.TrackingDAO;
import com.sjsu.request.Request;
import org.joda.time.DateTime;
import org.joda.time.Duration;

public class ParkingTrackerNew extends ServiceTracker {

    TrackingDAO trackingDAO = TrackingDAO.getTrackerDAO();

    protected ParkingTrackerNew(Request request) {
        super(request);
    }

    @Override
    public void startTracking() {

        ReservedParking parking = new ReservedParking();
        parking.setStartTime(DateTime.now());
        parking.setRequest(request);
        parking.setTrackingStatus(TrackingStatus.STARTED);
        parking.setParkingId(ParkingDAO.getAssignedParkingForRequest(request.getRequestId()).getParkingId());

        trackingDAO.saveTrackingInfo(request.getMemberId(), parking);
        System.out.println("Tracking parking request.");
        System.out.println("Tracking Details :=> " + parking.toString());
    }

    @Override
    public void endTracking() {
        ReservedParking trackingInfo = (ReservedParking) trackingDAO.getTrackingInfo(request);
        if (trackingInfo != null) {
            trackingInfo.setEndTime(DateTime.now());
            trackingInfo.setCost(calculateCost(trackingInfo));
            trackingInfo.setTrackingStatus(TrackingStatus.COMPLETED);
            System.out.println("Completed tracking request.");
            System.out.println("Tracking details :=> " + trackingInfo.toString());
            trackingDAO.updateTrackingInfo(trackingInfo.getRequest().getMemberId(), trackingInfo);
        } else {
            System.out.println("Unable to find a tracking info for a request.");
        }
    }

    private double calculateCost(ReservedParking reservedParking) {
        Duration duration = new Duration(reservedParking.getStartTime(), reservedParking.getEndTime());
        return ParkingDAO.getParkingById(reservedParking.getParkingId()).getCostPerHour() * (duration.getStandardHours());

    }
}
