package com.sjsu.scheduler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sjsu.client.PrintUtil;
import com.sjsu.dao.InvalidInputException;
import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.datastore.DataStore;
import com.sjsu.member.Driver;
import com.sjsu.member.Member;
import com.sjsu.member.Vehicle;
import com.sjsu.model.GeoLocation;
import com.sjsu.request.DriveRequest;
import com.sjsu.request.Request;
import com.sjsu.request.RideRequest;
import com.sjsu.routing.RouteManager;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.sjsu.client.PrintUtil.*;
import static com.sjsu.datastore.DummyData.resolveLocation;


public class SchedulingHelper {

    public static List<DriveRequest> filterDriversToMatch(RideRequest rideRequest) throws InvalidInputException {
        List<DriveRequest> allDriveRequests = DataStore.getAllDriveRequests();
        List<DriveRequest> filteredDriveRequests = Lists.newArrayList();

        for (DriveRequest driveRequest : allDriveRequests) {
            if (canAccommodateRide(rideRequest, driveRequest)) {
                filteredDriveRequests.add(driveRequest);
            }
        }
        return filteredDriveRequests;
    }


    public static boolean canAccommodateRide(RideRequest rideRequest, Request driveRequest) throws InvalidInputException {
        DriveRequest driveReq = (DriveRequest) driveRequest;

        return isSeatAvailable(driveReq) && isRideRouteFallsOnDriveRoute(rideRequest, driveReq) && isRideDateInRange(rideRequest, driveReq) && isRideTimeInRange(rideRequest, driveReq);
    }

    public static boolean isSeatAvailable(DriveRequest driveReq) {
        Driver memberDetailsById = (Driver) MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(driveReq.getMemberId());
        int seatCount = memberDetailsById.getVehicle().getSeatCount();

        return driveReq.getNoOfSeats() < seatCount;
    }

    public static boolean isRideTimeInRange(RideRequest rideRequest, DriveRequest driveReq) throws InvalidInputException {
        DateTime driveStartTime = driveReq.getStartTime();
        GeoLocation driverStartLocation = resolveLocation(driveReq.getStartingAddress());
        GeoLocation riderStartLocation = resolveLocation(rideRequest.getStartingAddress());

        double timeToTravel = GeoLocation.timeToTravel(driverStartLocation, riderStartLocation);

        DateTime driverTimeToReachRiderStartLoc = driveStartTime.plusMinutes((int) timeToTravel);

        // Matching drive time to ride time window
        DateTime rideStartTime = rideRequest.getStartTime();
        DateTime rideWindowLow = rideStartTime.minusMinutes(15);
        DateTime rideWindowHigh = rideStartTime.plusMinutes(15);

        return isBetweenInclusive(rideWindowLow, rideWindowHigh, driverTimeToReachRiderStartLoc);
    }

    public static boolean isRideDateInRange(RideRequest rideRequest, DriveRequest driveReq) {
        DateTime rideStartDate = rideRequest.getStartDate();
        DateTime rideEndDate = rideRequest.getEndDate();

        DateTime driveStartDate = driveReq.getStartDate();
        DateTime driveEndDate = driveReq.getEndDate();


        return isBetweenInclusive(driveStartDate, driveEndDate, rideStartDate) && isBetweenInclusive(driveStartDate, driveEndDate, rideEndDate);
    }

    public static boolean isRideRouteFallsOnDriveRoute(RideRequest rideRequest, DriveRequest driveReq) {
        String rideStartingAddress = rideRequest.getStartingAddress();
        String rideEndingAddress = rideRequest.getEndingAddress();

        String driveStartAddress = driveReq.getStartingAddress();
        String driveEndAddress = driveReq.getEndingAddress();

        List<String> rideRoute = RouteManager.getRouteDetails(rideStartingAddress, rideEndingAddress);
        List<String> driveRoute = RouteManager.getRouteDetails(driveStartAddress, driveEndAddress);

        int indexOfSubList = Collections.indexOfSubList(driveRoute, rideRoute);

        return indexOfSubList >= 0;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Map.Entry.comparingByValue())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }


    public static Map<DriveRequest, Double> mapDriveRequestsToDistance(RideRequest rideRequest, List<DriveRequest> driveRequests) throws InvalidInputException {
        Map<DriveRequest, Double> toReturn = Maps.newHashMap();

        for (DriveRequest driveRequest : driveRequests) {
            GeoLocation geoLocation_rider = resolveLocation(rideRequest.getStartingAddress());
            GeoLocation geoLocation_driver = resolveLocation(driveRequest.getStartingAddress());

            double distanceFromDriverToRider = GeoLocation.calculateDistance(geoLocation_driver, geoLocation_rider);

            toReturn.put(driveRequest, distanceFromDriverToRider);
        }
        return toReturn;
    }

    public static void printScheduledRides(Map<DriveRequest, List<RideRequest>> scheduledRides) {
        if (scheduledRides.size() > 0) {
            printLine();
            System.out.print("\nWe scheduled following trips\n");
            printLine();
            int i = 0;
            for (Map.Entry<DriveRequest, List<RideRequest>> entry : scheduledRides.entrySet()) {
                i = i + 1;
                System.out.println("************************************************");
                System.out.println("*                    " + i + ".Trip                *");
                System.out.println("************************************************");
                DriveRequest driveRequest = entry.getKey();
                Driver driver = (Driver) MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(driveRequest.getMemberId());
                System.out.println("Driver :=> " + driver.getFirstName() + " " + driver.getLastName());
                Vehicle vehicle = driver.getVehicle();
                //System.out.println("Distance From Your Location : " + entry.getValue());
                System.out.println("\tVehicle Details :" + "Make:" + vehicle.getVehicleMake() + " " + "No:" + vehicle.getVehicleNo() + " " + "Capacity:" + vehicle.getSeatCount());
                System.out.println("\tTrip Details : " + driveRequest.getStartingAddress() + " - " + driveRequest.getEndingAddress());
                System.out.println("\tTrip Dates : " + printDate(driveRequest.getStartDate()) + " - " + printDate(driveRequest.getEndDate()));
                System.out.println("\tTrip Start Time : " + printTime(driveRequest.getStartTime()));
                System.out.println("\tNumber Of Riders: " + driveRequest.getNoOfSeats());
                System.out.println("\tSeats Available: " + (driver.getVehicle().getSeatCount() - driveRequest.getNoOfSeats()));
                printLine();
                System.out.println("Following are the riders assigned to this trip :");
                printLine();

                for (RideRequest rideRequest : entry.getValue()) {
                    Member rider = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(rideRequest.getMemberId());
                    System.out.println("Rider :=> " + rider.getFirstName() + " " + rider.getLastName());
                    System.out.println("\tRide Details : " + rideRequest.getStartingAddress() + " - " + rideRequest.getEndingAddress());
                    System.out.println("\tRide Dates : " + printDate(rideRequest.getStartDate()) + " - " + printDate(rideRequest.getEndDate()));
                    System.out.println("\tRide Time : " + printTime(rideRequest.getStartTime()));
                    System.out.println("\tTime required for driver to reach rider's start location : " + printTimeRequiredByDriver(driveRequest, rideRequest) + " Minutes");
                    printLine();
                }

            }
        }
    }

    private static String printTimeRequiredByDriver(DriveRequest driveRequest, RideRequest rideRequest) {
        try {
            GeoLocation driveStart = resolveLocation(driveRequest.getStartingAddress());
            GeoLocation rideStart = resolveLocation(rideRequest.getStartingAddress());

            Double distance = GeoLocation.calculateDistance(driveStart, rideStart);

            return PrintUtil.roundDistance(distance * 1);

        } catch (InvalidInputException e) {
            e.printStackTrace();
        }
        return "Could not calculate";
    }


    public static boolean isBetweenInclusive(DateTime start, DateTime end, DateTime target) {
        return !target.isBefore(start) && !target.isAfter(end);
    }
}
