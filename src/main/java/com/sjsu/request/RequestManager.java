package com.sjsu.request;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.sjsu.client.InputReader;
import com.sjsu.client.MainMenu;
import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.datastore.DataStore;
import com.sjsu.datastore.Session;
import com.sjsu.member.Member;
import com.sjsu.model.MemberId;
import com.sjsu.model.MemberType;
import com.sjsu.request.state.InvalidRequestTypeException;
import com.sjsu.request.state.RequestFinishedTrackingState;
import com.sjsu.request.state.RequestDispatchingState;
import com.sjsu.routing.RouteManager;
import com.sjsu.scheduler.Algorithm1;
import com.sjsu.scheduler.Algorithm2;
import com.sjsu.scheduler.SchedulingAlgorithm;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static com.sjsu.client.PrintUtil.*;

public class RequestManager {

    public static void printMenu() throws IOException, ParseException, InvalidRequestTypeException {
        printMenuTitle("Manage Request Menu");
        if (Session.getCurrentUser().getMemberType() == MemberType.Driver) {
            printDriverMenu();
        } else {
            printRiderMenu();
        }

    }

    private static void printRiderMenu() throws IOException {
        System.out.println("1. Book a carpool");
        System.out.println("2. Book a parking");
        System.out.println("3. View current requests");

        MainMenu.getMainMenu().logoutAndGoToMainMenu();

        String option = InputReader.readLine();

        switch (option) {
            case "1":
                showBookCarpool();
                break;
            case "2":
                showBookParkingMenu();
                break;
            case "3":
                showCurrentRequests();
                break;
            default:
                MainMenu.getMainMenu().handleOtherInput(option);

        }
        MainMenu.getMainMenu().start();

    }

    private static void showCurrentRequests() {
        Map<MemberId, List<Request>> memberToRequestMap = DataStore.getMemberToRequestMap();
        Map<ParkingRequest, Member> parkingRequests = Maps.newHashMap();
        Map<OfferParkingRequest, Member> offerParkingRequests = Maps.newHashMap();
        Map<DriveRequest, Member> driveRequests = Maps.newHashMap();
        Map<RideRequest, Member> rideRequests = Maps.newHashMap();

        int i = 0;
        for (Map.Entry<MemberId, List<Request>> entry : memberToRequestMap.entrySet()) {
            i = i + 1;
            MemberId memberId = entry.getKey();
            List<Request> requests = entry.getValue();

            Member member = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(memberId);

            for (Request request : requests) {
                if (request instanceof DriveRequest) {
                    driveRequests.put((DriveRequest) request, member);
                } else if (request instanceof RideRequest) {
                    rideRequests.put((RideRequest) request, member);
                } else if (request instanceof ParkingRequest) {
                    parkingRequests.put((ParkingRequest) request, member);
                } else if (request instanceof OfferParkingRequest) {
                    offerParkingRequests.put((OfferParkingRequest) request, member);
                }
            }

        }
        printDriveRequests(driveRequests);
        printRideRequests(rideRequests);
        printParkingRequests(parkingRequests);
        printOfferParkingRequests(offerParkingRequests);
    }

    private static void printOfferParkingRequests(Map<OfferParkingRequest, Member> offerParkingRequests) {

    }

    private static void printParkingRequests(Map<ParkingRequest, Member> parkingRequests) {

    }

    private static void printRideRequests(Map<RideRequest, Member> toPrint) {
        printMenuTitle("Ride Requests");
        int i = 0;
        for (Map.Entry<RideRequest, Member> entry : toPrint.entrySet()) {
            i = i + 1;
            RideRequest request = entry.getKey();
            Member member = entry.getValue();
            StringBuilder stringBuilder = new StringBuilder().append(i).append(" - ")
                    .append("USERNAME : ").append(member.getUsername()).append(" - ")
                    .append("REQUEST-ID : ").append(request.getRequestId()).append(" - ")
                    .append("PASSENGERS : ").append(request.getNoOfPassengers()).append(" - ")
                    .append("SRC-DEST : ").append("[").append(request.getStartingAddress()).append("-").append(request.getEndingAddress()).append("]").append(" - ")
                    .append("CARPOOL-DATES : ").append("[").append(printDate(request.getStartDate())).append(" - ").append(printDate(request.getEndDate())).append("]").append(" - ")
                    .append("TIME : ").append(printTime(request.getStartTime())).append("\n");

            System.out.println(stringBuilder.toString());
        }
    }

    private static void printDriveRequests(Map<DriveRequest, Member> toPrint) {
        Gson gson = new Gson();
        String toJson = gson.toJson(toPrint);
        //System.out.println(toJson);
        printMenuTitle("Drive Requests");
        int i = 0;
        for (Map.Entry<DriveRequest, Member> entry : toPrint.entrySet()) {
            i = i + 1;
            DriveRequest request = entry.getKey();
            Member member = entry.getValue();
            StringBuilder stringBuilder = new StringBuilder().append(i).append(" - ")
                    .append("USERNAME : ").append(member.getUsername()).append(" - ")
                    .append("REQUEST-ID : ").append(request.getRequestId()).append(" - ")
                    .append("SEATS : ").append(request.getNoOfSeats()).append(" - ")
                    .append("SRC-DEST : ").append("[").append(request.getStartingAddress()).append("-").append(request.getEndingAddress()).append("]").append(" - ")
                    .append("DRIVE-DATES : ").append("[").append(printDate(request.getStartDate())).append(" - ").append(printDate(request.getEndDate())).append(" - ")
                    .append("TIME : ").append(printTime(request.getStartTime())).append("\n");

            System.out.println(stringBuilder.toString());
        }
    }

    private static void printDriverMenu() throws IOException, InvalidRequestTypeException, ParseException {
        System.out.println("1. Offer to drive");
        System.out.println("2. Complete a ride");
        System.out.println("3. Complete a trip");
        System.out.println("4. Book a parking");
        System.out.println("5. View current requests");
        MainMenu.getMainMenu().logoutAndGoToMainMenu();

        String option = InputReader.readLine();

        switch (option) {

            case "1":
                showOfferDrive();
                break;
            case "2":
                showCompleteRideMenu();
                break;
            case "3":
                showCompleteTripMenu();
                break;
            case "4":
                showBookParkingMenu();
                break;
            case "5":
                showCurrentRequests();
                break;
            default:
                MainMenu.getMainMenu().handleOtherInput(option);

        }
        MainMenu.getMainMenu().start();

    }

    private static void showCompleteTripMenu() throws IOException, ParseException {
        Member currentDriver = Session.getCurrentUser();

        List<DriveRequest> driveRequestsForDriver = getDriveRequestsForDriver(currentDriver);
        if (driveRequestsForDriver.size() > 0) {
            DriveRequest selectedDriveRequestToComplete = selectDriveRequestToComplete(driveRequestsForDriver);
            selectedDriveRequestToComplete.completeRequest();
        } else {
            System.out.println("We did not find a dispatched drive request which has all rides completed?");
            System.out.println("Please mark rides as complete before completing a trip.");
        }
    }

    private static DriveRequest selectDriveRequestToComplete(List<DriveRequest> driveRequestsForDriver) throws IOException, ParseException {
        System.out.println("Please select a trip to complete.");
        int i = 0;
        for (DriveRequest driveRequest : driveRequestsForDriver) {
            i = i + 1;
            System.out.println(i + ". - " + "Trip [" + driveRequest.getStartingAddress() + " - " + driveRequest.getEndingAddress() + "]");
        }
        int option = InputReader.readInt();

        return driveRequestsForDriver.get(option - 1);
    }

    private static List<DriveRequest> getDriveRequestsForDriver(Member currentDriver) {
        Map<RideRequest, DriveRequest> scheduledRideRequests = DataStore.getScheduledRideRequests();
        Map<RideRequest, DriveRequest> rideRequestDriveRequestMap = Maps.filterEntries(scheduledRideRequests, new Predicate<Map.Entry<RideRequest, DriveRequest>>() {
            @Override
            public boolean apply(Map.Entry<RideRequest, DriveRequest> input) {
                RideRequest rideRequest = input.getKey();
                DriveRequest driveRequest = input.getValue();

                return driveRequest.getMemberId().equals(currentDriver.getMemberId()) &&
                        driveRequest.getState() instanceof RequestDispatchingState && rideRequest.getState() instanceof RequestFinishedTrackingState;
            }
        });
        return Lists.newArrayList(rideRequestDriveRequestMap.values());
    }

    private static void showCompleteRideMenu() {
        Member currentDriver = Session.getCurrentUser();

        Map<RideRequest, DriveRequest> driveRequestsForDriver = getDriveRequestForDriverMap(currentDriver);
        if (driveRequestsForDriver.size() > 0) {
            try {
                RideRequest rideRequest = selectRideRequestToComplete(driveRequestsForDriver);
                rideRequest.completeRequest();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Sorry! we did not find any trip which is dispatched and has all rides completed.");
            //System.out.println("Please mark rides as complete before completing a trip.");
        }
    }

    private static RideRequest selectRideRequestToComplete(Map<RideRequest, DriveRequest> driveRequestsForDriver) throws IOException, ParseException {
        int i = 0;
        List<RideRequest> track = Lists.newArrayList();
        for (Map.Entry<RideRequest, DriveRequest> entry : driveRequestsForDriver.entrySet()) {
            i = i + 1;
            RideRequest rideRequest = entry.getKey();
            Member rider = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(rideRequest.getMemberId());

            track.add(rideRequest);

            System.out.println(i + ". - " + "Rider:" + rider.getUsername() + " - " + "Ride: [" + rideRequest.getStartingAddress() + " - " + rideRequest.getEndingAddress() + "]");
        }

        int selectedOption = InputReader.readInt();
        return track.get(selectedOption - 1);
    }

    private static Map<RideRequest, DriveRequest> getDriveRequestForDriverMap(Member currentDriver) {
        Map<RideRequest, DriveRequest> scheduledRideRequests = DataStore.getScheduledRideRequests();

        return Maps.filterEntries(scheduledRideRequests, new Predicate<Map.Entry<RideRequest, DriveRequest>>() {
            @Override
            public boolean apply(Map.Entry<RideRequest, DriveRequest> input) {
                RideRequest rideRequest = input.getKey();
                DriveRequest driveRequest = input.getValue();
                return driveRequest.getMemberId().equals(currentDriver.getMemberId()) && rideRequest.getState() instanceof RequestFinishedTrackingState;
            }
        });
    }


    public static void showBookParkingMenu() {
        ParkingRequest request = new ParkingRequest(new Algorithm1(new RouteManager()));
        try {
            request.acceptRequest();
            request.waitInQueue();
            /*while (request.getState() instanceof RequestWaitingState) {
                System.out.println("Waiting for the request to be picked up ....");
                Thread.sleep(500);
            }
            request.processRequest();
            request.dispatchRequest();*/
            System.out.println("Do you want to exit parking?(y/n)");
            String exitParking = InputReader.readLine();

            if ("y".equalsIgnoreCase(exitParking)) {
                request.completeRequest();
            }

        } catch (Throwable e) {
            System.out.println("Error accepting parking request." + e.getMessage());
        }
    }

    public static void showBookCarpool() throws IOException {

        Algorithm1 schedulingAlgorithm = new Algorithm1(new RouteManager());
        RideRequest rideRequest = new RideRequest(schedulingAlgorithm);
        try {
            rideRequest.acceptRequest();
            rideRequest.waitInQueue();

        } catch (Throwable th) {
            th.printStackTrace();
        }

    }

    public static void showOfferDrive() throws IOException, ParseException, InvalidRequestTypeException {
        System.out.println("Would you like to have an option to select a riders?(y/n)");
        String answer = InputReader.readLine();
        SchedulingAlgorithm schedulingAlgorithm;
        if ("y".equalsIgnoreCase(answer)) {
            schedulingAlgorithm = new Algorithm2(new RouteManager());
        } else {
            schedulingAlgorithm = new Algorithm1(new RouteManager());
        }
        DriveRequest driveRequest = new DriveRequest(schedulingAlgorithm);
        driveRequest.acceptRequest();
        driveRequest.waitInQueue();

    }

    public static void showOfferParking() {

        OfferParkingRequest offerParkingRequest = new OfferParkingRequest(new Algorithm1(new RouteManager()));
        try {
            offerParkingRequest.acceptRequest();
            offerParkingRequest.waitInQueue();
            /*while (offerParkingRequest.getState() instanceof RequestWaitingState) {
                System.out.println("Waiting for the request to be picked up ....");
                Thread.sleep(500);
            }
            offerParkingRequest.processRequest();
            offerParkingRequest.dispatchRequest();*/

        } catch (Throwable e) {
            System.out.println("Error accepting offer parking request." + e.getMessage());
        }

    }

}
