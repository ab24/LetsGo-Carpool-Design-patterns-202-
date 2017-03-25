package com.sjsu.ratingsandreviews;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sjsu.client.InputReader;
import com.sjsu.client.MainMenu;
import com.sjsu.client.PrintUtil;
import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.dao.TrackingDAO;
import com.sjsu.datastore.DataStore;
import com.sjsu.datastore.Session;
import com.sjsu.member.Member;
import com.sjsu.model.MemberId;
import com.sjsu.request.*;
import com.sjsu.track.TrackingInfo;
import com.sjsu.track.TrackingStatus;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static com.sjsu.ratingsandreviews.RatingsAndReview.newRatingsAndReview;


public class RatingsAndReviewManager {
    public static void printMenu() {
        try {
            PrintUtil.printMenuTitle("Ratings and Review Menu");
            System.out.println("1. View Ratings and Reviews");
            System.out.println("2. Provide Ratings and Reviews");
            MainMenu.getMainMenu().logoutAndGoToMainMenu();


            String line = InputReader.readLine();

            switch (line) {
                case "1":
                    showRatingsAndReviews();
                    break;
                case "2":
                    provideRatingsAndReviews();
                    break;
                default:
                    MainMenu.getMainMenu().handleOtherInput(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void provideRatingsAndReviews() {
        try {
            Member currentUser = Session.getCurrentUser();
            List<TrackingInfo> trackingInfos = TrackingDAO.getTrackingInfoMap().get(currentUser.getMemberId());

            Request toRate = getRequestToRate(newArrayList(newHashSet(trackingInfos)));

            if (toRate != null) {
                if (toRate instanceof DriveRequest) {
                    rateDriveRequest((DriveRequest) toRate);
                } else if (toRate instanceof RideRequest) {
                    rateRideRequest((RideRequest) toRate);
                } else if (toRate instanceof ParkingRequest) {
                    rateParkingRequest((ParkingRequest) toRate);
                } else if (toRate instanceof OfferParkingRequest) {
                    rateOfferParkingRequest((OfferParkingRequest) toRate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("");
    }

    private static void rateOfferParkingRequest(OfferParkingRequest toRate) {
        System.out.println("Coming soon..");
    }

    private static void rateParkingRequest(ParkingRequest toRate) {
        System.out.println("Coming soon..");
    }

    private static void rateRideRequest(RideRequest toRate) {
        try {
            DriveRequest driveRequest = DataStore.getScheduledRideRequests().get(toRate);
            if (driveRequest != null) {
                Member driver = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(driveRequest.getMemberId());

                System.out.println("Please Rate your driver " + driver.getUsername() + "[(1 -Low & 5-Highest)]" + " : ");
                int rate = InputReader.readInt();
                System.out.println("Help us improve by providing your Review : ");
                String review = InputReader.readLine();

                driver.addRatingsAndReview(newRatingsAndReview(rate, review));

                System.out.println("Thank you for your Feedback");
            }
        } catch (Exception ex) {
            System.out.println("Error rating riders : " + ex.getMessage());
        }
    }

    private static void rateDriveRequest(DriveRequest toRate) {
        try {
            List<MemberId> memberNotificationObserver = toRate.getMemberNotificationObserver();
            System.out.println("Rate Riders of this trip");
            int i = 0;
            for (MemberId memberId : memberNotificationObserver) {
                i = i + 1;
                Member rider = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(memberId);
                System.out.println(i + ". " + "Please Rate your rider " + rider.getUsername() + "[(1 -Low & 5-Highest)]" + " : ");
                int rate = InputReader.readInt();
                System.out.println("Help us improve by providing your Review : ");
                String review = InputReader.readLine();

                rider.addRatingsAndReview(newRatingsAndReview(rate, review));

                System.out.println("Thank you for your Feedback");
            }
        } catch (IOException e) {
            System.out.println("Error rating riders : " + e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static Request getRequestToRate(List<TrackingInfo> trackingInfos) throws IOException, ParseException {
        List<TrackingInfo> filteredTrackingInfo = newArrayList();
        int i = 0;
        for (TrackingInfo trackingInfo : trackingInfos) {
            if (trackingInfo.getTrackingStatus() == TrackingStatus.COMPLETED) {
                i = i + 1;
                filteredTrackingInfo.add(trackingInfo);
                Request request = trackingInfo.getRequest();
                if (request instanceof DriveRequest) {
                    DriveRequest driveRequest = (DriveRequest) request;
                    System.out.println(i + ". - [" + driveRequest.getRequestId() + "] Drive Request - [" + driveRequest.getStartingAddress() + " - " + driveRequest.getEndingAddress() + "]");
                } else if (request instanceof RideRequest) {
                    RideRequest rideRequest = (RideRequest) request;
                    System.out.println(i + ". - [" + rideRequest.getRequestId() + "] Ride Request - [" + rideRequest.getStartingAddress() + " - " + rideRequest.getEndingAddress() + "]");
                } else if (request instanceof ParkingRequest) {
                    ParkingRequest parkingRequest = (ParkingRequest) request;
                    System.out.println(i + ". - [" + parkingRequest.getRequestId() + "] Parking Request - [" + parkingRequest.getCity() + "]");
                } else if (request instanceof OfferParkingRequest) {
                    OfferParkingRequest offerParkingRequest = (OfferParkingRequest) request;
                    System.out.println(i + ". - [" + offerParkingRequest.getRequestId() + "] Offer Parking Request - [" + offerParkingRequest.getCity() + "]");
                }
            }
        }
        if (filteredTrackingInfo.size() > 0) {
            int userSelection = InputReader.readInt();
            return filteredTrackingInfo.get(userSelection - 1).getRequest();
        }

        System.out.println("You do not have any rides or trips which are complete.");
        return null;

    }


    private static void showRatingsAndReviews() {
        Member currentUser = Session.getCurrentUser();

        List<RatingsAndReview> ratingsAndReviews = currentUser.getRatingsAndReviews();
        if (ratingsAndReviews.size() > 0) {
            int i = 0;
            for (RatingsAndReview ratingsAndReview : ratingsAndReviews) {
                i = i + 1;
                System.out.println(i + ". - " + ratingsAndReview.getRating() + " - " + ratingsAndReview.getReview());
            }
        } else {
            System.out.println("You do not have any ratings and reviews yet.");
        }

    }
}
