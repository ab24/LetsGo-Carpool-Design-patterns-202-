package com.sjsu.dispatch;

import com.google.common.collect.Lists;
import com.sjsu.client.InputReader;
import com.sjsu.client.MainMenu;
import com.sjsu.datastore.DataStore;
import com.sjsu.datastore.Session;
import com.sjsu.request.DriveRequest;
import com.sjsu.request.Request;
import com.sjsu.request.RequestType;
import com.sjsu.request.RideRequest;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static com.sjsu.client.PrintUtil.printMenuTitle;


public class DispatchManager {
    public static void printMenu() throws IOException, ParseException {
        printMenuTitle("Dispatcher Menu");
        System.out.println("1. Dispatch Ride Requests");
        System.out.println("2. Dispatch Parking Requests");
        MainMenu.getMainMenu().logoutAndGoToMainMenu();

        String line = InputReader.readLine();

        switch (line) {
            case "1":
                dispatchRideRequests();
                break;
            case "2":
                dispatchParkingRequests();
                break;
            default:
                MainMenu.getMainMenu().handleOtherInput(line);
        }
    }

    private static void dispatchParkingRequests() {

    }

    private static void dispatchRideRequests() throws IOException, ParseException {
        List<Request> toDispatch = Lists.newArrayList();
        List<Request> memberRequests = DataStore.getMemberToRequestMap().get(Session.getCurrentUser().getMemberId());
        for (Request request : memberRequests) {
            if (request.getRequestType() == RequestType.DRIVE_NOW || request.getRequestType() == RequestType.DRIVE_SCHEDULE ||
                    request.getRequestType() == RequestType.RIDE_SCHEDULE || request.getRequestType() == RequestType.RIDE_SCHEDULE) {

                toDispatch.add(request);
            }

        }

        printDispatchMenu(toDispatch);
    }

    private static void printDispatchMenu(List<Request> toDispatch) throws IOException, ParseException {
        System.out.println("**** You will only see your request to dispatch.\n");

        System.out.println("Please select a request to dispatch");
        System.out.println("________________________________________________");
        System.out.println("| No.          Request Type             Details|");
        System.out.println("________________________________________________");
        int i = 0;
        for (Request request : toDispatch) {
            i = i + 1;

            if (request instanceof DriveRequest) {
                DriveRequest driveRequest = (DriveRequest) request;
                System.out.println(i + ". " + "Dispatch Drive Request[" + " " + driveRequest.getStartingAddress() + "-" + driveRequest.getEndingAddress() + "]");
            } else if (request instanceof RideRequest) {
                RideRequest rideRequest = (RideRequest) request;
                System.out.println(i + ". " + "Ride Request[" + " " + rideRequest.getStartingAddress() + "-" + rideRequest.getEndingAddress() + "]");
            }

        }
        int input = InputReader.readInt();
        Request userSelectedRequest = toDispatch.get(input - 1);
        userSelectedRequest.dispatchRequest();
    }
}
