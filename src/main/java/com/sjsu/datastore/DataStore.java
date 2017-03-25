package com.sjsu.datastore;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.sjsu.dao.InvalidInputException;
import com.sjsu.model.GeoLocation;
import com.sjsu.model.MemberId;
import com.sjsu.model.Parking;
import com.sjsu.notification.Notification;
import com.sjsu.request.*;
import com.sjsu.scheduler.Algorithm1;
import com.sjsu.scheduler.Algorithm2;

import java.util.*;

import static com.google.common.collect.Maps.newHashMap;


public class DataStore {

    private static Map<String, Parking> requestIdToParking = newHashMap();
    private static Map<MemberId, List<Request>> memberToRequestMap = new HashMap<>();
    private static Map<RideRequest, DriveRequest> scheduledRideRequests = newHashMap();
    private static Map<ParkingRequest, OfferParkingRequest> scheduledParkingRequests = newHashMap();

    private static Map<String, List<Notification>> memberIdToNotification;

    public static Map<String, Parking> getRequestIdToParkingMap() {
        return requestIdToParking;
    }

    public static Map<MemberId, List<Request>> getMemberToRequestMap() {
        return memberToRequestMap;
    }

    public static void addToRequestToParkingMap(ParkingRequest parkingRequest, Parking selectedParking) {
        requestIdToParking.put(parkingRequest.getRequestId(), selectedParking);
    }

    public static List<Request> getAllRequests() {
        ArrayList<Request> allRequests = Lists.newArrayList();

        for (Map.Entry<MemberId, List<Request>> entry : memberToRequestMap.entrySet()) {
            allRequests.addAll(entry.getValue());
        }
        return allRequests;
    }

    public static List<DriveRequest> getAllDriveRequests() {
        List<Request> allRequests = Lists.newArrayList();

        for (Map.Entry<MemberId, List<Request>> entry : memberToRequestMap.entrySet()) {
            for (Request request : entry.getValue()) {
                if (isDriveRequest(request)) {
                    allRequests.add(request);
                }
            }
        }
        Collections.sort(allRequests, new OrderByDate());
        return Lists.transform(allRequests, new Function<Request, DriveRequest>() {
            @Override
            public DriveRequest apply(Request input) {
                return (DriveRequest) input;
            }
        });
    }


    /*public static List<RideRequest> getAllRideRequestsForAlgorithm1() {
        Collection<RideRequest> rideRequests = Collections2.filter(getAllRideRequests(), new Predicate<RideRequest>() {
            @Override
            public boolean apply(RideRequest rideRequest) {
                return rideRequest.getSchedulingAlgorithm() instanceof Algorithm1;
            }
        });
        return Lists.newArrayList(rideRequests);
    }

    public static List<RideRequest> getAllRideRequestsForAlgorithm2() {
        Collection<RideRequest> rideRequests = Collections2.filter(getAllRideRequests(), new Predicate<RideRequest>() {
            @Override
            public boolean apply(RideRequest rideRequest) {
                return rideRequest.getSchedulingAlgorithm() instanceof Algorithm2;
            }
        });
        return Lists.newArrayList(rideRequests);
    }*/

//    public static List<ParkingRequest> getAllParkingRequests(){
//        List<Request> allRequests = Lists.newArrayList();
//    }

    public static List<RideRequest> getAllRideRequests() {
        List<Request> allRequests = Lists.newArrayList();

        for (Map.Entry<MemberId, List<Request>> entry : memberToRequestMap.entrySet()) {
            for (Request request : entry.getValue()) {
                if (isRideRequest(request)) {
                    allRequests.add(request);
                }
            }
        }

        Collections.sort(allRequests, new OrderByDate());

        return Lists.transform(allRequests, new Function<Request, RideRequest>() {
            @Override
            public RideRequest apply(Request input) {
                return (RideRequest) input;
            }
        });
    }
    public static List<ParkingRequest> getAllParkingRequests() {
        List<Request> allRequests = Lists.newArrayList();

        for (Map.Entry<MemberId, List<Request>> entry : memberToRequestMap.entrySet()) {
            for (Request request : entry.getValue()) {
                if (isParkingRequest(request)) {
                    allRequests.add(request);
                }
            }
        }

        Collections.sort(allRequests,new OrderByDate());

        return Lists.transform(allRequests, new Function<Request, ParkingRequest>() {
            @Override
            public ParkingRequest apply(Request input) {
                return (ParkingRequest) input;
            }
        });
    }

    private static boolean isRideRequest(Request request) {
        return request.getRequestType() == RequestType.RIDE_NOW ||
                request.getRequestType() == RequestType.RIDE_SCHEDULE;
    }

    public static void saveScheduledRideRequests(RideRequest rideRequest, DriveRequest driveRequest) {
        scheduledRideRequests.put(rideRequest, driveRequest);
    }

    public static void saveScheduledParkingRequests(ParkingRequest parkingRequest, OfferParkingRequest offerParkingRequest) {
        scheduledParkingRequests.put(parkingRequest, offerParkingRequest);
    }

    public static Double calculateDistance(RideRequest rideRequest, DriveRequest driveRequest) {
        try {
            GeoLocation rideStartGeoLocation = DummyData.resolveLocation(rideRequest.getStartingAddress());
            GeoLocation driveStartGeoLocation = DummyData.resolveLocation(driveRequest.getStartingAddress());

            return GeoLocation.calculateDistance(rideStartGeoLocation, driveStartGeoLocation);

        } catch (InvalidInputException e) {
            e.printStackTrace();
        }

        return 0.0;

    }

    public static Map<RideRequest, DriveRequest> getScheduledRideRequests() {
        return scheduledRideRequests;
    }

    public static Map<ParkingRequest, OfferParkingRequest> getScheduledParkingRequests() {
        return scheduledParkingRequests;
    }

    public static List<Request> getAllDriveRequestsForMember(MemberId memberId) {
        List<Request> allDriveRequests = Lists.newArrayList();

        List<Request> memberRequests = memberToRequestMap.get(memberId);

        for (Request request : memberRequests) {
            if (isDriveRequest(request)) {
                allDriveRequests.add(request);
            }
        }

        return allDriveRequests;
    }

    private static boolean isDriveRequest(Request request) {
        return (request.getRequestType() == RequestType.DRIVE_NOW ||
                request.getRequestType() == RequestType.DRIVE_SCHEDULE);
    }
    private static boolean isParkingRequest(Request request) {
        return  request.getRequestType() == RequestType.PARKING;
    }
    private static boolean isOfferParkingRequest(Request request) {
        return request.getRequestType() == RequestType.OFFER_PARKING;
    }

    private static class OrderByDate extends Ordering<Request> {

        @Override
        public int compare(Request left, Request right) {
            return left.getCreationTime().compareTo(right.getCreationTime());
        }
    }
}
