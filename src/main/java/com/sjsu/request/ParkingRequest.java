package com.sjsu.request;

import com.sjsu.client.InputReader;
import com.sjsu.datastore.Session;
import com.sjsu.request.state.InvalidRequestTypeException;
import com.sjsu.rules.RequestRule;
import com.sjsu.scheduler.SchedulingAlgorithm;
import com.sjsu.util.IdGenerator;
import org.joda.time.DateTime;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import static com.sjsu.client.InputReader.readAddressSelection;
import static com.sjsu.client.PrintUtil.printLine;
import static com.sjsu.request.RequestType.PARKING;


public class ParkingRequest extends Request {

    private String city;
    private DateTime StartDate;
    private DateTime endDate;
    private DateTime time;
    private int durationInHours;
    private DateTime startTime;

    public ParkingRequest(SchedulingAlgorithm schedulingAlgorithm) {
        super(schedulingAlgorithm);
        setRequestType(PARKING);
        setCreationTime(new Date());
        setRequestId(IdGenerator.generateId("R"));
    }

    @Override
    public void acceptRequest() throws IOException, ParseException, InvalidRequestTypeException {

        printLine();
        System.out.println("Find Parking menu");
        printLine();

        System.out.println("Select parking location :");
        String postalCode = readAddressSelection();

        System.out.println("Enter Start Date (mm/dd/yy) :");
        DateTime startDate = InputReader.readDate();

        System.out.println("Enter End Date (mm/dd/yy) :");
        DateTime endDate = InputReader.readDate();

        System.out.println("Enter Start time (hh:mm) :");
        DateTime startTime = InputReader.readTime();

        System.out.println("Enter Duration in Hours(hh) :");
        int duration = InputReader.readInt();

        this.setMemberId(Session.getCurrentUser().getMemberId());
        this.setCreationTime(new Date());
        this.setCity(postalCode);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setDurationInHours(duration);
        this.setStartTime(startTime);

        super.acceptRequest();
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public DateTime getStartDate() {
        return StartDate;
    }

    public void setStartDate(DateTime startDate) {
        StartDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }

    public int getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(int durationInHours) {
        this.durationInHours = durationInHours;
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
