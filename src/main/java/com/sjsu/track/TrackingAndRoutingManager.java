package com.sjsu.track;

import com.google.common.collect.Lists;
import com.sjsu.client.InputReader;
import com.sjsu.client.MainMenu;
import com.sjsu.dao.TrackingDAO;
import com.sjsu.datastore.Session;
import com.sjsu.member.Member;
import com.sjsu.model.MemberId;
import com.sjsu.model.MemberType;
import com.sjsu.request.RequestType;

import java.io.IOException;
import java.util.List;

import static com.sjsu.client.PrintUtil.printMenuTitle;

public class TrackingAndRoutingManager {

    public static void printMenu() throws IOException {
        MemberType memberType = Session.getCurrentUser().getMemberType();
        if (memberType.equals(MemberType.Driver)) {
            printMenuTitle("Track request Menu");
            System.out.println("1. Track parking");
            System.out.println("2. Track trip");

            String option = InputReader.readLine();

            switch (option) {
                case "1":
                    showParkingTracker();
                    break;

                case "2":
                    showTripTracker();
                    break;
                default:
                    MainMenu.getMainMenu().start();

            }
        } else if (memberType.equals(MemberType.Rider)) {
            printMenuTitle("Track request Menu");
            System.out.println("1. Track Ride");
            String option = InputReader.readLine();

            switch (option) {
                case "1":
                    showRideTracker();
                    break;
                default:
                    MainMenu.getMainMenu().start();
            }

        } else if (memberType.equals(MemberType.Organization)) {
            printMenuTitle("Track request Menu");
            System.out.println("1. Track offered parking");
            String option = InputReader.readLine();

            switch (option) {
                case "1":
                    showOfferParkingTracker();
                    break;
                default:
                    MainMenu.getMainMenu().start();
            }

        }
    }

    public static void showParkingTracker() {
        Member member = Session.getCurrentUser();
        MemberId memberId = member.getMemberId();

        List<TrackingInfo> trackingInfoList = TrackingDAO.getTrackingInfoForRequestType(RequestType.PARKING, memberId);
        if (trackingInfoList != null) {
            System.out.println("The following the available tracking option for parking");

            for (TrackingInfo info : trackingInfoList) {
                System.out.println(info.toString());

            }
        } else {
            System.out.println("Currently there are no request to be tracked");
        }
    }

    public static void showOfferParkingTracker() {
        Member member = Session.getCurrentUser();
        MemberId memberId = member.getMemberId();

        List<TrackingInfo> trackingInfoList = TrackingDAO.getTrackingInfoForRequestType(RequestType.OFFER_PARKING, memberId);
        if (trackingInfoList != null) {
            System.out.println("The following the available tracking option for parking");

            for (TrackingInfo info : trackingInfoList) {
                System.out.println(info.toString());

            }
        } else {
            System.out.println("Currently there are no request to be tracked");
        }
    }

    public static void showTripTracker() {
        Member member = Session.getCurrentUser();
        MemberId memberId = member.getMemberId();

        List<TrackingInfo> trackingInfoList = TrackingDAO.getTrackingInfoForRequestType(RequestType.DRIVE_NOW, memberId);
        List<TrackingInfo> trackingInfoListSchueduled = TrackingDAO.getTrackingInfoForRequestType(RequestType.DRIVE_SCHEDULE, memberId);
        if (trackingInfoList != null && trackingInfoList.size() > 0) {
            System.out.println("The following the available tracking option for Instant drive requests");

            for (TrackingInfo info : trackingInfoList) {
                System.out.println(info.toString());

            }
        } else if (trackingInfoListSchueduled != null && trackingInfoListSchueduled.size()>0) {
            System.out.println("The following the available tracking option for scheduled drive requests");
            for (TrackingInfo info : trackingInfoListSchueduled) {
                System.out.println(info.toString());

            }

        } else {
            System.out.println("Currently there are no request to be tracked");
        }

    }

    public static void showRideTracker() {

        Member member = Session.getCurrentUser();
        MemberId memberId = member.getMemberId();

        List<TrackingInfo> trackingInfoList = TrackingDAO.getTrackingInfoForRequestType(RequestType.RIDE_NOW, memberId);
        List<TrackingInfo> trackingInfoListSchueduled = TrackingDAO.getTrackingInfoForRequestType(RequestType.RIDE_SCHEDULE, memberId);
        if (trackingInfoList != null) {
            System.out.println("The following the available tracking option for Instant ride requests");

            for (TrackingInfo info : trackingInfoList) {
                System.out.println(info.toString());

            }
        } else if (trackingInfoListSchueduled != null) {
            System.out.println("The following the available tracking option for scheduled ride requests");
            for (TrackingInfo info : trackingInfoListSchueduled) {
                System.out.println(info.toString());

            }

        } else {
            System.out.println("Currently there are no request to be tracked");
        }

    }


}

