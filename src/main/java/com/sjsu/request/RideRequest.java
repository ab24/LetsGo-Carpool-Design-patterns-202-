package com.sjsu.request;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.sjsu.client.InputReader;
import com.sjsu.client.MainMenu;
import com.sjsu.dao.InvalidInputException;
import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.dao.TrackingDAO;
import com.sjsu.datastore.DataStore;
import com.sjsu.datastore.DummyData;
import com.sjsu.datastore.Session;
import com.sjsu.member.Member;
import com.sjsu.model.GeoLocation;
import com.sjsu.notification.Notification;
import com.sjsu.payment.PaymentContext;
import com.sjsu.request.state.RequestFinishedTrackingState;
import com.sjsu.rules.RequestRule;
import com.sjsu.scheduler.SchedulingAlgorithm;
import com.sjsu.track.ServiceTracker;
import com.sjsu.track.TrackingInfo;
import com.sjsu.util.IdGenerator;
import org.joda.time.DateTime;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static com.sjsu.client.InputReader.readAddressSelection;
import static com.sjsu.client.PrintUtil.*;
import static com.sjsu.datastore.DataStore.calculateDistance;
import static com.sjsu.request.RequestType.RIDE_SCHEDULE;

/**
 * Created by abhasin on 8/13/16.
 */
public class RideRequest extends Request implements Serializable{

    private String startingAddress;
    private String endingAddress;
    private int noOfPassengers;
    private DateTime startDate;
    private DateTime endDate;
    private DateTime startTime;


    public RideRequest(SchedulingAlgorithm schedulingAlgorithm) {
        super(schedulingAlgorithm);
        setRequestType(RIDE_SCHEDULE);
        setCreationTime(new Date());
        setRequestId(IdGenerator.generateId("R"));
    }

    public String getStartingAddress() {
        return startingAddress;
    }

    public void setStartingAddress(String startingAddress) {
        this.startingAddress = startingAddress;
    }

    public String getEndingAddress() {
        return endingAddress;
    }

    public void setEndingAddress(String endingAddress) {
        this.endingAddress = endingAddress;
    }

    public int getNoOfPassengers() {
        return noOfPassengers;
    }

    public void setNoOfPassengers(int noOfPassengers) {
        this.noOfPassengers = noOfPassengers;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public void acceptRequest() throws IOException {
        try {
            printMenuTitle("Ride Request Menu");

            System.out.println("*** $50 will be blocked on your digital wallet/credit card till the request is successfully completed.\n");

            System.out.println("Select Starting Address :");
            String startingAddress = readAddressSelection();

            System.out.println("Select Ending Address :");
            String endingAddress = readAddressSelection();

            GeoLocation startGeolocation = DummyData.resolveLocation(startingAddress);
            GeoLocation endGeoLocation = DummyData.resolveLocation(endingAddress);

            System.out.println("\n>>>> Distance from [" + startingAddress + " - " + endingAddress + "] - " + roundDistance(GeoLocation.calculateDistance(startGeolocation, endGeoLocation)) + " Miles\n");


            System.out.println("Number of Passengers :");
            int noPassengers = InputReader.readInt();

            System.out.println("Enter Start Date (mm/dd/yy) :");
            DateTime startDate = InputReader.readDate();

            System.out.println("Enter End Date (mm/dd/yy) :");
            DateTime endDate = InputReader.readDate();

            System.out.println(">>>> We will match you with a driver who can reach your start location 15 minutes before or after the provided time.");

            System.out.println("Enter Start time (hh:mm) :");
            DateTime startTime = InputReader.readTime();

            this.setMemberId(Session.getCurrentUser().getMemberId());
            this.setStartingAddress(startingAddress);
            this.setEndingAddress(endingAddress);
            this.setNoOfPassengers(noPassengers);
            this.setStartDate(startDate);
            this.setEndDate(endDate);
            this.setStartTime(startTime);

            super.acceptRequest();
        } catch (Throwable th) {
            System.out.println("Error accepting ride request.");
            System.out.println("Error message :=> " + th.getMessage());
            System.out.println("Do you want to try again?(y/n)");
            String input = InputReader.readLine();
            if ("y".equalsIgnoreCase(input)) {
                acceptRequest();
            } else {
                MainMenu.getMainMenu().start();
            }
        }
    }

    @Override
    public void processRequest() throws InvalidInputException, IOException {
        //this.getSchedulingAlgorithm().scheduleParkingRequest(this);
        super.processRequest();
    }

    @Override
    public void completeRequest() {
        this.notifyMember(Notification.buildSystemNotification("Thank you for using our services", getUsername()));
        ServiceTracker.getTracker(this).endTracking();
        TrackingInfo trackingInfo = TrackingDAO.getTrackerDAO().getTrackingInfo(this);

        System.out.println(getUsername() + " Processing payment");
        PaymentContext paymentContext = new PaymentContext();
        paymentContext.processPayment(this, trackingInfo);

        super.completeRequest();

        /*Map<RideRequest, DriveRequest> allRiderInThisTrip = getAllRidersInThisTrip();
        boolean allRiderCompleted = areAllRidesCompleted(allRiderInThisTrip);

        if(allRiderCompleted){
            System.out.println("All drive requests are complete. Hence completing ");
            Collection<DriveRequest> values = allRiderInThisTrip.values();
            for(DriveRequest driveRequest : values){
                driveRequest.completeRequest();
            }
        }*/

    }

    private boolean areAllRidesCompleted(Map<RideRequest, DriveRequest> allRiderInThisTrip) {
        boolean allRidesCompleted = true;

        for (Map.Entry<RideRequest, DriveRequest> entry : allRiderInThisTrip.entrySet()) {
            RideRequest rideRequest = entry.getKey();
            if (!(rideRequest.getState() instanceof RequestFinishedTrackingState)) {
                allRidesCompleted = false;
                break;
            }
        }
        return allRidesCompleted;
    }

    private Map<RideRequest, DriveRequest> getAllRidersInThisTrip() {
        Map<RideRequest, DriveRequest> scheduledRideRequests = DataStore.getScheduledRideRequests();
        DriveRequest driveRequest = scheduledRideRequests.get(this);

        return Maps.filterValues(scheduledRideRequests, new Predicate<DriveRequest>() {
            @Override
            public boolean apply(DriveRequest input) {
                return input.getRequestId().equals(driveRequest.getRequestId());
            }
        });
    }

    private String getUsername() {
        return MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(this.getMemberId()).getUsername();
    }

    @Override
    public void dispatchRequest() {
        DriveRequest driveRequest = DataStore.getScheduledRideRequests().get(this);

        Member rider = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(this.getMemberId());
        Member driver = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(driveRequest.getMemberId());

        System.out.println(">>>>>> Starting dispatch");
        driveRequest.notifyMember(Notification.buildSystemNotification("You have a pickup of " + rider.getUsername()
                + " scheduled at " + printTime(this.getStartTime()) + " on " + printDate(this.getStartDate()), driver.getUsername()));

        TrackingInfo driveRequestTrackingInfo = TrackingDAO.getTrackerDAO().getTrackingInfo(driveRequest);
        if (driveRequestTrackingInfo == null) {
            ServiceTracker.getTracker(driveRequest).startTracking();
        }

        this.notifyMember(Notification.buildSystemNotification(rider.getUsername() + " your driver will reach at your location in approximately " + calculateDistance(this, driveRequest).intValue(), rider.getUsername()));
        ServiceTracker.getTracker(this).startTracking();
        super.dispatchRequest();
    }

    @Override
    public void applyRules() {

        RequestRule rule = new RequestRule(this);
        rule.applyRule();

    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }
}
