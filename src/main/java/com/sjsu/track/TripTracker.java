package com.sjsu.track;


import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.dao.TrackingDAO;
import com.sjsu.member.Driver;
import com.sjsu.member.Member;
import com.sjsu.request.Request;
import com.sjsu.util.IdGenerator;
import org.joda.time.DateTime;

import static com.sjsu.track.TrackingStatus.COMPLETED;

public class TripTracker extends ServiceTracker {

    private TrackingDAO trackingDAO = TrackingDAO.getTrackerDAO();

    public TripTracker(Request request) {
        super(request);
    }

    @Override
    public void startTracking() {
        Driver driver = getDriverDetails(request);

        Trip trip = new Trip();
        trip.setTrackingId(IdGenerator.generateId("T"));
        trip.setRequest(request);
        trip.setDriverId(request.getMemberId());
        trip.setVehicleId(driver.getVehicle().getVehicleId());
        trip.setStartTime(DateTime.now());
        trip.setTrackingStatus(TrackingStatus.STARTED);
        trip.setAvailableSeats(driver.getVehicle().getSeatCount() - request.getMemberNotificationObserver().size());
        trip.setMiles(0);
        System.out.println("Tracking trip request.");
        System.out.println("Tracking Details  :=> " + trip.toString());

        trackingDAO.saveTrackingInfo(driver.getMemberId(), trip);

    }

    private Driver getDriverDetails(Request request) {
        Member memberDetailsById = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(request.getMemberId());
        return (Driver) memberDetailsById;
    }

    @Override
    public void endTracking() {

        Trip tripTrackingInfo = (Trip) trackingDAO.getTrackingInfo(request);
        if (tripTrackingInfo != null) {
            tripTrackingInfo.setEndTime(DateTime.now());
            tripTrackingInfo.setTrackingStatus(COMPLETED);
            System.out.println("Completed tracking request.");
            System.out.println("Tracking details :=> " + tripTrackingInfo.toString());
            trackingDAO.updateTrackingInfo(tripTrackingInfo.getRequest().getMemberId(), tripTrackingInfo);
        } else {
            System.out.println("Unable to find a tracking info for a request.");
        }
    }


}
