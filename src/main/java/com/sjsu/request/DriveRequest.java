package com.sjsu.request;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sjsu.client.InputReader;
import com.sjsu.client.MainMenu;
import com.sjsu.client.PrintUtil;
import com.sjsu.dao.InvalidInputException;
import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.datastore.DataStore;
import com.sjsu.datastore.DummyData;
import com.sjsu.datastore.Session;
import com.sjsu.member.Member;
import com.sjsu.model.GeoLocation;
import com.sjsu.notification.Notification;
import com.sjsu.request.state.RequestFinishedTrackingState;
import com.sjsu.rules.RequestRule;
import com.sjsu.scheduler.SchedulingAlgorithm;
import com.sjsu.track.ServiceTracker;
import com.sjsu.util.IdGenerator;
import org.joda.time.DateTime;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

import static com.sjsu.client.InputReader.readAddressSelection;
import static com.sjsu.client.PrintUtil.printMenuTitle;
import static com.sjsu.client.PrintUtil.roundDistance;
import static com.sjsu.datastore.DataStore.calculateDistance;
import static com.sjsu.request.RequestType.DRIVE_SCHEDULE;

public class DriveRequest extends Request implements Serializable{

    private String startingAddress;
    private String endingAddress;
    private int noOfSeats;
    private DateTime startDate;
    private DateTime endDate;
    private DateTime startTime;

    public DriveRequest(SchedulingAlgorithm schedulingAlgorithm) {
        super(schedulingAlgorithm);
        setRequestType(DRIVE_SCHEDULE);
        setCreationTime(new Date());
        setRequestId(IdGenerator.generateId("R"));
    }

    private static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Map.Entry.comparingByValue())
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

        return result;
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

    public int getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(int noOfSeats) {
        this.noOfSeats = noOfSeats;
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

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public void acceptRequest() throws IOException {
        try {

            printMenuTitle("Drive Request Menu");

            System.out.println("*** $50 will be blocked on your digital wallet/credit card till the request is successfully completed.\n");

            System.out.println("Select Starting Address :");
            String startingAddress = readAddressSelection();

            System.out.println("Select Ending Address :");
            String endingAddress = readAddressSelection();

            GeoLocation startGeolocation = DummyData.resolveLocation(startingAddress);
            GeoLocation endGeoLocation = DummyData.resolveLocation(endingAddress);

            System.out.println("\n>>>> Distance from [" + startingAddress + " - " + endingAddress + "] - " + roundDistance(GeoLocation.calculateDistance(startGeolocation, endGeoLocation)) + " Miles\n");

            System.out.println("Enter Start Date (mm/dd/yy) :");
            DateTime startDate = InputReader.readDate();

            System.out.println("Enter End Date (mm/dd/yy) :");
            DateTime endDate = InputReader.readDate();

            System.out.println("Enter Start time (hh:mm) :");
            DateTime startTime = InputReader.readTime();



            /*System.out.println("Enter (R) to Register a new vehicle or (E) to use registered vehicles : ");

            Vehicle newVehicle = null;

            if (InputReader.readLine().equalsIgnoreCase("R")) {
                System.out.println("Enter Vehicle Make :");
                String vehicleMake = InputReader.readLine();

                System.out.println("Enter Vehicle Number :");
                String vehicleNo = InputReader.readLine();

                System.out.println("Number of Seats :");
                int noSeats = InputReader.readInt();

                newVehicle = new Vehicle(vehicleMake, vehicleNo, noSeats);

            } else if (InputReader.readLine() == "E") {
                //YET TO IMPLEMENT. NEED TO THINK THROUGH.
            }


            this.vehicle = newVehicle;*/
            this.setMemberId(Session.getCurrentUser().getMemberId());
            this.setStartingAddress(startingAddress);
            this.setEndingAddress(endingAddress);
            this.setStartDate(startDate);
            this.setEndDate(endDate);
            this.setStartTime(startTime);

            this.applyRules();

            super.acceptRequest();
        } catch (Throwable th) {
            System.out.println("Error accepting drive request.");
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
        //this.getSchedulingAlgorithm().scheduleCarpoolRequest(this);
        super.processRequest();
    }

    @Override
    public void completeRequest() {
        super.completeRequest();
    }

    @Override
    public void dispatchRequest() {
        Member driver = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(this.getMemberId());
        List<RideRequest> scheduledRideRequestsForDriveRequest = getScheduledRideRequestsForDriveRequest(this);

        System.out.println(">>>>>> Starting dispatch");

        Map<RideRequest, Double> distanceMap = Maps.newHashMap();

        for (RideRequest rideRequest : scheduledRideRequestsForDriveRequest) {
            Member rider = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(rideRequest.getMemberId());
            rideRequest.notifyMember(Notification.buildSystemNotification(rider.getUsername() + " your driver will reach at your location in approximately " + calculateDistance(rideRequest, this).intValue() + " Minutes", rider.getUsername()));
            rideRequest.setRequestState(new RequestFinishedTrackingState(rideRequest));

            ServiceTracker.getTracker(rideRequest).startTracking();

            distanceMap.put(rideRequest, calculateDistance(rideRequest, this));
        }

        Map<RideRequest, Double> sortedMap = sortByValue(distanceMap);
        String routeNotification = getRouteNotification(sortedMap);
        this.notifyMember(Notification.buildSystemNotification(routeNotification, driver.getUsername()));
        ServiceTracker.getTracker(this).startTracking();
        super.dispatchRequest();
    }

    private String getRouteNotification(Map<RideRequest, Double> sortedMap) {
        Member driver = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(this.getMemberId());
        StringBuilder builder = new StringBuilder(driver.getUsername() + " your trip [" + this.getStartingAddress() + " - " + this.getEndingAddress() + "] is starting now. " +
                "\n Following are the riders for this trip :=> " + "\n");
        int i = 0;
        for (Map.Entry<RideRequest, Double> entry : sortedMap.entrySet()) {
            i = i + 1;
            RideRequest rideRequest = entry.getKey();
            Double distance = entry.getValue();
            Member rider = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(rideRequest.getMemberId());

            builder.append("\t").append(i).append(".").append("\t")
                    .append(rider.getUsername()).append("\t")
                    .append("[").append(rideRequest.getStartingAddress()).append(" - ").append(rideRequest.getEndingAddress()).append("]").append("\t")
                    .append("[Distance of rider start location from your start location: ").append(PrintUtil.roundDistance(distance)).append(" Miles").append("]").append("\n\t");
        }
        return builder.toString();
    }

    private List<RideRequest> getScheduledRideRequestsForDriveRequest(DriveRequest driveRequest) {
        Map<RideRequest, DriveRequest> scheduledRideRequests = DataStore.getScheduledRideRequests();
        boolean containsValue = scheduledRideRequests.containsValue(this);
        if (containsValue) {
            Map<RideRequest, DriveRequest> rideRequestDriveRequestMap = Maps.filterValues(scheduledRideRequests, new Predicate<DriveRequest>() {
                @Override
                public boolean apply(DriveRequest input) {
                    return input.getRequestId().equals(driveRequest.getRequestId());
                }
            });

            Set<RideRequest> rideRequests = rideRequestDriveRequestMap.keySet();
            return Lists.newArrayList(rideRequests);
        }
        return Lists.newArrayList();
    }

    @Override
    public void applyRules() {

        RequestRule rule = new RequestRule(this);
        rule.applyRule();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DriveRequest)) return false;
        if (!super.equals(o)) return false;

        DriveRequest that = (DriveRequest) o;

        if (!getStartingAddress().equals(that.getStartingAddress())) return false;
        if (!getEndingAddress().equals(that.getEndingAddress())) return false;
        if (!getStartDate().equals(that.getStartDate())) return false;
        if (!getEndDate().equals(that.getEndDate())) return false;
        return getStartTime().equals(that.getStartTime());

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getStartingAddress().hashCode();
        result = 31 * result + getEndingAddress().hashCode();
        result = 31 * result + getStartDate().hashCode();
        result = 31 * result + getEndDate().hashCode();
        result = 31 * result + getStartTime().hashCode();
        return result;
    }

}
