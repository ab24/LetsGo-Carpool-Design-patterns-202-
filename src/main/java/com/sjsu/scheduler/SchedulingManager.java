package com.sjsu.scheduler;

import com.sjsu.client.InputReader;
import com.sjsu.client.MainMenu;
import com.sjsu.dao.InvalidInputException;
import com.sjsu.datastore.DataStore;
import com.sjsu.model.MemberId;
import com.sjsu.request.Request;
import com.sjsu.request.RequestType;
import com.sjsu.routing.RouteManager;
import com.sjsu.scheduler.Algorithm1;
import com.sjsu.scheduler.Algorithm2;
import com.sjsu.scheduler.SchedulingAlgorithm;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.sjsu.client.PrintUtil.printMenuTitle;
import static com.sjsu.client.PrintUtil.selectOptionString;

public class SchedulingManager {
    public static void printMenu() throws IOException, InvalidInputException {
        printMenuTitle("Scheduling Menu");
        System.out.println("1. Schedule Ride Requests");
        System.out.println("2. Schedule Parking Requests");
        System.out.println("3. Go back to main menu");

        String line = InputReader.readLine();

        switch (line) {
            case "1":
                scheduleRideRequests();
                break;
            case "2":
                scheduleParkingRequests();
                break;
            case "3":
            default:
                MainMenu.getMainMenu().start();
        }
        MainMenu.getMainMenu().start();


    }

    private static void scheduleParkingRequests() throws InvalidInputException, IOException {
        Map<MemberId, List<Request>> memberToRequestMap = DataStore.getMemberToRequestMap();
        for (Map.Entry<MemberId, List<Request>> entry : memberToRequestMap.entrySet()) {
            for (Request request : entry.getValue()) {
                if (ifParkingTypeRequest(request)) {
                    request.processRequest();
                }

            }
        }
        SchedulingAlgorithm schedulingAlgorithm1 = new Algorithm1(new RouteManager());
        schedulingAlgorithm1.scheduleParkingRequest();
    }

    private static boolean ifParkingTypeRequest(Request request) {
        return request.getRequestType() == RequestType.PARKING || request.getRequestType() == RequestType.OFFER_PARKING;
    }

    private static void scheduleRideRequests() throws InvalidInputException, IOException {
        try {
            SchedulingAlgorithm algorithm = new Algorithm1(new RouteManager());
            System.out.println("Do you want to schedule request using 1.Algorithm-1 or 2.Algorithm-2");
            String userSelection = InputReader.readLine();
            if("2".equalsIgnoreCase(userSelection)){
                algorithm = new Algorithm2(new RouteManager());
            }
            Map<MemberId, List<Request>> memberToRequestMap = DataStore.getMemberToRequestMap();
            for (Map.Entry<MemberId, List<Request>> entry : memberToRequestMap.entrySet()) {
                for (Request request : entry.getValue()) {
                    if (ifRideTypeRequest(request)) {
                        request.setSchedulingAlgorithm(algorithm);
                        request.processRequest();

                    }
                }
            }

            // System.out.println("Schedule with 1. Algorithm-1 or 2.Algorithm-2");
            //String userSelection = Input.readLine();
            //if("1".equals(""))

            algorithm.scheduleCarpoolRequest();
        }catch(Exception e){
            System.out.println("Error reading algorithm selection.");
            scheduleRideRequests();
        }

    }

    private static boolean ifRideTypeRequest(Request request) {
        return request.getRequestType() == RequestType.DRIVE_NOW || request.getRequestType() == RequestType.DRIVE_SCHEDULE ||
                request.getRequestType() == RequestType.RIDE_SCHEDULE || request.getRequestType() == RequestType.RIDE_SCHEDULE;
    }
}
