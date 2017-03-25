package com.sjsu.scheduler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sjsu.dao.InvalidInputException;
import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.dao.ParkingDAO;
import com.sjsu.datastore.DataStore;
import com.sjsu.member.Driver;
import com.sjsu.model.Parking;
import com.sjsu.request.*;
import com.sjsu.routing.RouteManager;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.sjsu.client.PrintUtil.printLine;
import static com.sjsu.datastore.DataStore.saveScheduledRideRequests;
import static com.sjsu.scheduler.SchedulingHelper.*;

/**
 * In algorithm1 system matches demand to supply
 */

public class Algorithm1 extends SchedulingAlgorithm {
    public Algorithm1(RouteManager routeManager) {
        super(routeManager);
    }

    private static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Map.Entry.comparingByValue())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }

    private static boolean isBetweenInclusive(DateTime start, DateTime end, DateTime target) {
        return !target.isBefore(start) && !target.isAfter(end);
    }

    @Override
    public boolean scheduleCarpoolRequest() {
        Map<DriveRequest, List<RideRequest>> scheduledRides = Maps.newHashMap();
        try {
            List<RideRequest> allRideRequests = DataStore.getAllRideRequests();
            System.out.println("Scheduling " + allRideRequests.size() + " ride requests with " + DataStore.getAllDriveRequests().size() + " drive requests");
            {
                for (RideRequest rideRequest : allRideRequests) {
                    List<DriveRequest> driveRequests = filterDriversToMatch(rideRequest);
                    if (driveRequests.size() != 0) {
                        Map<DriveRequest, Double> distanceMap = SchedulingHelper.mapDriveRequestsToDistance(rideRequest, driveRequests);
                        Map<DriveRequest, Double> sortedDriveRequests = sortByValue(distanceMap);

                        DriveRequest nearestDriver = getNearestDriver(sortedDriveRequests);
                        nearestDriver.addMemberNotificationObserver(rideRequest.getMemberId());
                        nearestDriver.setNoOfSeats(nearestDriver.getNoOfSeats() + rideRequest.getNoOfPassengers());

                        saveScheduledRideRequests(rideRequest, nearestDriver);

                        addToScheduledRides(scheduledRides, rideRequest, nearestDriver);
                    } else {
                        System.out.println("No match found");
                    }

                }
            }
            printScheduledRides(scheduledRides);

        } catch (InvalidInputException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean scheduleParkingRequest() throws InvalidInputException, IOException {

        List<ParkingRequest> allParkingRequests = DataStore.getAllParkingRequests();
        for (ParkingRequest parkingRequest : allParkingRequests) {
            System.out.println("Scheduling parking request");

            try {
                Map<Parking, Double> filteredParking = ParkingDAO.filterParking(parkingRequest);
                printLine();
                System.out.println("Algorithm1 :=> You are assigned a following parking.");

                Parking selectedParking = null;

                for (Map.Entry<Parking, Double> entry : filteredParking.entrySet()) {
                    selectedParking = entry.getKey();
                    break;
                }

                ParkingDAO.assignParking(parkingRequest, selectedParking);

                System.out.println("Your selection saved successfully.");

            } catch (InvalidInputException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private void addToScheduledRides(Map<DriveRequest, List<RideRequest>> scheduledRides, RideRequest rideRequest, DriveRequest nearestDriver) {
        List<RideRequest> rideRequests = scheduledRides.get(nearestDriver);
        if (rideRequests == null) {
            rideRequests = Lists.newArrayList();
        }
        rideRequests.add(rideRequest);
        scheduledRides.put(nearestDriver, rideRequests);
    }

    private DriveRequest getNearestDriver(Map<DriveRequest, Double> sortedDriveRequests) {
        for (Map.Entry<DriveRequest, Double> entry : sortedDriveRequests.entrySet()) {
            return entry.getKey();
        }
        return null;
    }

    public List<DriveRequest> filterDriversToMatch(RideRequest rideRequest) throws InvalidInputException {
        List<DriveRequest> allDriveRequests = DataStore.getAllDriveRequests();
        List<DriveRequest> filteredDriveRequests = Lists.newArrayList();

        for (DriveRequest driveRequest : allDriveRequests) {
            if (canAccommodateRide(rideRequest, driveRequest)) {
                filteredDriveRequests.add(driveRequest);
            }
        }
        return filteredDriveRequests;
    }

    private boolean canAccommodateRide(RideRequest rideRequest, Request driveRequest) throws InvalidInputException {
        DriveRequest driveReq = (DriveRequest) driveRequest;

        return isSeatAvailable(driveReq, rideRequest) && isRideRouteFallsOnDriveRoute(rideRequest, driveReq) &&
                isRideDateInRange(rideRequest, driveReq) && isRideTimeInRange(rideRequest, driveReq);
    }

    private boolean isSeatAvailable(DriveRequest driveReq, RideRequest rideRequest) {
        Driver memberDetailsById = (Driver) MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(driveReq.getMemberId());
        int seatCount = memberDetailsById.getVehicle().getSeatCount();

        return driveReq.getNoOfSeats() < seatCount && (seatCount - driveReq.getNoOfSeats()) >= rideRequest.getNoOfPassengers();
    }
}
