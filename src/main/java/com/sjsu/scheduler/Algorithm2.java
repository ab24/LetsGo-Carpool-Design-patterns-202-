package com.sjsu.scheduler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sjsu.client.InputReader;
import com.sjsu.dao.InvalidInputException;
import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.datastore.DataStore;
import com.sjsu.datastore.Session;
import com.sjsu.member.Driver;
import com.sjsu.member.Member;
import com.sjsu.member.Vehicle;
import com.sjsu.model.MemberId;
import com.sjsu.model.MemberType;
import com.sjsu.request.DriveRequest;
import com.sjsu.request.Request;
import com.sjsu.request.RideRequest;
import com.sjsu.routing.RouteManager;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.sjsu.client.PrintUtil.printLine;
import static com.sjsu.datastore.DataStore.saveScheduledRideRequests;
import static com.sjsu.scheduler.SchedulingHelper.*;

/**
 * In Algorithm 2 - system provides an option to member to select
 */
public class Algorithm2 extends SchedulingAlgorithm {

    Map<RideRequest, DriveRequest> toReschedule = Maps.newHashMap();

    public Algorithm2(RouteManager routeManager) {
        super(routeManager);
    }

    @Override
    public boolean scheduleCarpoolRequest() {
        List<RideRequest> allRideRequests = DataStore.getAllRideRequests();
        if (allRideRequests.size() > 0) {
            Map<DriveRequest, List<RideRequest>> scheduledRides = schedule(allRideRequests);
            printScheduledRides(scheduledRides);
            try {
                getUserChoice(scheduledRides);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }

    private void getUserChoice(Map<DriveRequest, List<RideRequest>> scheduledRides) throws IOException, ParseException {
        Member currentUser = Session.getCurrentUser();
        MemberId currentMemberId = currentUser.getMemberId();
        if (currentUser.getMemberType() == MemberType.Driver) {

            List<DriveRequest> userDriveRequests = getScheduledDriveRequestsForUser(currentMemberId, scheduledRides);

            if (userDriveRequests.size() > 0) {
                System.out.println("******* Your Scheduled Request ********");
                int i = 0;
                getDriveRequestToReSchedule(scheduledRides, userDriveRequests, i);
                if (toReschedule.size() > 0) {
                    System.out.println("\n ----- Rescheduling ----- ");
                    Map<DriveRequest, List<RideRequest>> schedule = schedule(Lists.newArrayList(toReschedule.keySet()));
                    printScheduledRides(schedule);
                }
            } else {
                System.out.println("Scheduling Algorithm2 :=> It looks like you do not have any drive requests to schedule.");
            }

        }

    }

    private void getDriveRequestToReSchedule(Map<DriveRequest, List<RideRequest>> scheduledRides, List<DriveRequest> userDriveRequests, int i) throws IOException {
        for (DriveRequest driveRequest : userDriveRequests) {
            i = i + 1;
            System.out.println(i + " - Want to reschedule this drive request?(y/n) - " + driveRequest.getRequestId() + " - [" + driveRequest.getStartingAddress() + " - " + driveRequest.getEndingAddress() + "]");
            String input = InputReader.readLine();
            if ("y".equalsIgnoreCase(input)) {
                int j = 0;
                getRideRequestsToReschedule(scheduledRides, i, driveRequest, j);
            }
        }
    }

    private void getRideRequestsToReschedule(Map<DriveRequest, List<RideRequest>> scheduledRides, int i, DriveRequest driveRequest, int j) throws IOException {
        for (RideRequest rideRequest : scheduledRides.get(driveRequest)) {
            j = j + 1;
            System.out.println(i + " - Want to reschedule this ride request?(y/n) - " + rideRequest.getRequestId() + " - [" + rideRequest.getStartingAddress() + " - " + rideRequest.getEndingAddress() + "]");
            String rideInput = InputReader.readLine();
            if ("y".equalsIgnoreCase(rideInput)) {
                driveRequest.removeMemberNotificationObserver(rideRequest.getMemberId());
                driveRequest.setNoOfSeats(driveRequest.getNoOfSeats() - rideRequest.getNoOfPassengers());
                toReschedule.put(rideRequest, driveRequest);
            }
        }
    }

    private List<DriveRequest> getScheduledDriveRequestsForUser(MemberId currentMemberId, Map<DriveRequest, List<RideRequest>> scheduledRides) {
        List<DriveRequest> userDriveRequests = Lists.newArrayList();
        Set<DriveRequest> driveRequests = scheduledRides.keySet();
        for (DriveRequest driveRequest : driveRequests) {
            if (driveRequest.getMemberId().equals(currentMemberId)) {
                userDriveRequests.add(driveRequest);
            }
        }
        return userDriveRequests;
    }

    private Map<DriveRequest, List<RideRequest>> schedule(List<RideRequest> allRideRequests) {
        Map<DriveRequest, List<RideRequest>> scheduledRides = Maps.newHashMap();
        try {
            for (RideRequest rideRequest : allRideRequests) {
                List<DriveRequest> driveRequests = filterDriversToMatch(rideRequest);
                Map<DriveRequest, Double> distanceMap = mapDriveRequestsToDistance(rideRequest, driveRequests);
                Map<DriveRequest, Double> sortedDriveRequests = sortByValue(distanceMap);
                DriveRequest nearestDriver = getNearestDriver(sortedDriveRequests);
                if(nearestDriver !=null){
                    nearestDriver.addMemberNotificationObserver(rideRequest.getMemberId());
                    nearestDriver.setNoOfSeats(nearestDriver.getNoOfSeats() + rideRequest.getNoOfPassengers());

                    saveScheduledRideRequests(rideRequest, nearestDriver);

                    addToScheduledRides(scheduledRides, rideRequest, nearestDriver);
                }else{
                    System.out.println("No matching driver found.");
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return scheduledRides;
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

    private DriveRequest getUserSelection(RideRequest rideRequest, Map<DriveRequest, Double> sortedDriveRequests) throws IOException, ParseException {
        printLine();
        System.out.println("We match you with following drivers");
        System.out.println("Your request details :=> ");
        System.out.println("Start Address : " + rideRequest.getStartingAddress() + " End Address : " + rideRequest.getEndingAddress());
        System.out.println("Start Date : " + rideRequest.getStartDate() + " End Date : " + rideRequest.getEndDate());
        System.out.println("Start Time : " + rideRequest.getStartTime());
        printLine();
        System.out.println("Please select one of the driver:");
        int i = 0;
        List<Map.Entry<DriveRequest, Double>> options = Lists.newArrayList();

        for (Map.Entry<DriveRequest, Double> entry : sortedDriveRequests.entrySet()) {
            DriveRequest driveRequest = entry.getKey();
            Driver driver = (Driver) MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(driveRequest.getMemberId());
            printLine();
            System.out.println(i + 1 + " " + driver.getFirstName() + " " + driver.getLastName());
            Vehicle vehicle = driver.getVehicle();
            System.out.println("Distance From Your Location : " + entry.getValue());
            System.out.println("Vehicle Details :=> " + vehicle.getVehicleMake() + " " + vehicle.getVehicleNo());
            System.out.println("Trip Details :=> ");
            System.out.println("Start Location : " + driveRequest.getStartingAddress() + " End Location : " + driveRequest.getEndingAddress());
            System.out.println("Start Date : " + driveRequest.getStartDate() + " End Date : " + driveRequest.getEndDate());
            System.out.println("Start Time : " + driveRequest.getStartTime());
            printLine();
            i = i + 1;
            options.add(entry);
        }

        int selectedOption = InputReader.readInt();
        Map.Entry<DriveRequest, Double> entry = options.get(selectedOption - 1);
        return entry.getKey();

    }

    @Override
    public boolean scheduleParkingRequest() throws InvalidInputException, IOException {
       /* System.out.println("Scheduling parking request");

        if (request instanceof ParkingRequest) {
            ParkingRequest parkingRequest = (ParkingRequest) request;

            try {
                Map<Parking, Double> filteredParking = ParkingDAO.filterParking(parkingRequest);
                printLine();
                System.out.println("We found following matches ");

                int i = 1;
                List<Parking> tempList = Lists.newArrayList();
                for (Map.Entry<Parking, Double> entry : filteredParking.entrySet()) {
                    System.out.println(i + " Distance: " + entry.getValue() + " Estimated Price: " + (entry.getKey().getCostPerHour() * parkingRequest.getDurationInHours()));
                    System.out.println(" Address: " + entry.getKey().getAddress().toString());
                    tempList.add(entry.getKey());
                    i = i + 1;
                }

                int selectedOption = InputReader.readInt();

                Parking selectedParking = tempList.get(selectedOption);

                ParkingDAO.assignParking(parkingRequest, selectedParking);

                System.out.println("Your selection saved successfully.");

            } catch (InvalidInputException | ParseException | IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new InvalidInputException("Invalid Request Type");
        }

        return true;*/
        return false;
    }


    public List<DriveRequest> filterDriversToMatch(RideRequest rideRequest) throws InvalidInputException {
        List<DriveRequest> allDriveRequests = DataStore.getAllDriveRequests();
        List<DriveRequest> filteredDriveRequests = Lists.newArrayList();

        for (DriveRequest driveRequest : allDriveRequests) {
            if (canAccommodateRide(rideRequest, driveRequest)) {
                filteredDriveRequests.add(driveRequest);
            }
        }
        DriveRequest driveRequest = toReschedule.get(rideRequest);
        if (driveRequest != null && filteredDriveRequests.contains(driveRequest)) {
            filteredDriveRequests.remove(driveRequest);
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
