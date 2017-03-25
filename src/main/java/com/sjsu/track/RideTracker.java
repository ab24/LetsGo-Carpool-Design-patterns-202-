package com.sjsu.track;

import com.sjsu.dao.InvalidInputException;
import com.sjsu.dao.TrackingDAO;
import com.sjsu.datastore.DummyData;
import com.sjsu.model.GeoLocation;
import com.sjsu.request.Request;
import com.sjsu.request.RideRequest;
import com.sjsu.util.IdGenerator;
import org.joda.time.DateTime;

import static com.sjsu.client.PrintUtil.roundDistance;
import static com.sjsu.track.TrackingStatus.COMPLETED;


public class RideTracker extends ServiceTracker {

    TrackingDAO trackingDAO = TrackingDAO.getTrackerDAO();

    protected RideTracker(Request request) {
        super(request);
    }

    @Override
    public void startTracking() {
        Ride ride = new Ride();
        ride.setRideId(IdGenerator.generateId("R"));
        ride.setRequest(request);
        ride.setMiles(0);
        ride.setStartTime(DateTime.now());
        ride.setEndTime(null);
        ride.setTrackingStatus(TrackingStatus.STARTED);
        System.out.println("Tracking ride request.");
        System.out.println("Tracking Details  :=> " + ride.toString());
        //print message --- request is being tracked
        trackingDAO.saveTrackingInfo(request.getMemberId(), ride);

    }

    @Override
    public void endTracking() {
        //get trackingInfo object from Trakcing DAO


        Ride rideTrackingInfo = (Ride) trackingDAO.getTrackingInfo(request);
        if (rideTrackingInfo != null) {
            rideTrackingInfo.setEndTime(DateTime.now());
            rideTrackingInfo.setCost(calculateCost(rideTrackingInfo));
            rideTrackingInfo.setTrackingStatus(COMPLETED);
            System.out.println("Completed tracking request.");
            System.out.println("Tracking details :=> " + rideTrackingInfo.toString());
            trackingDAO.updateTrackingInfo(rideTrackingInfo.getRequest().getMemberId(), rideTrackingInfo);
        } else {
            System.out.println("Unable to find a tracking info for a request.");
        }
    }

    public double calculateCost(Ride rideTrackingInfo) {

        /** Calculate the cost based on miles travelled. Miles travelled can be calculated using the difference in distance
         * between source and destination. Source and destination can be obtained from ride request object.

         **/

        RideRequest rideRequest = (RideRequest) rideTrackingInfo.getRequest();
        rideTrackingInfo.setEndTime(DateTime.now());
        rideTrackingInfo.setTrackingStatus(COMPLETED);
        trackingDAO.updateTrackingInfo(rideTrackingInfo.getRequest().getMemberId(), rideTrackingInfo);
        try {
            GeoLocation rideStartGeoLocation = DummyData.resolveLocation(rideRequest.getStartingAddress());
            GeoLocation rideEndGeoLocation = DummyData.resolveLocation(rideRequest.getEndingAddress());


            Double distance = GeoLocation.calculateDistance(rideStartGeoLocation, rideEndGeoLocation);
            rideTrackingInfo.setMiles(Double.valueOf(roundDistance(distance)));

            double totalMiles = rideTrackingInfo.getMiles();
            return totalMiles * 0.5;
        } catch (InvalidInputException e) {
            e.printStackTrace();
        }
        return 0.0;
    }


}

