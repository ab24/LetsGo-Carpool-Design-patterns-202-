package com.sjsu.request;

import com.sjsu.client.InputReader;
import com.sjsu.dao.InvalidInputException;
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
import static com.sjsu.request.RequestType.OFFER_PARKING;

/**
 * Created by abhasin on 8/13//16.
 */
public class OfferParkingRequest extends Request {

    private String city;
    private DateTime StartDate;
    private DateTime endDate;
    private int durationInHours;
    private DateTime startTime;
    private int costPerHour;

    public OfferParkingRequest(SchedulingAlgorithm schedulingAlgorithm) {
        super(schedulingAlgorithm);
        setRequestId(IdGenerator.generateId("R"));
        setRequestType(OFFER_PARKING);
        setCreationTime(new Date());
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

    public int getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(int durationInHours) {
        this.durationInHours = durationInHours;
    }

    public int getCostPerHour() {
        return costPerHour;
    }

    public void setCostPerHour(int costPerHour) {
        this.costPerHour = costPerHour;
    }

    @Override
    public void acceptRequest() throws IOException, ParseException, InvalidRequestTypeException {

        printLine();
        System.out.println("Offer Parking menu");
        printLine();

        System.out.println("Select Address for Parking :");
        String startingAddress = readAddressSelection();

        System.out.println("Enter Start Date (mm/dd/yy) :");
        DateTime startdate = InputReader.readDate();

        System.out.println("Enter End Date (mm/dd/yy) :");
        DateTime endDate = InputReader.readDate();

        System.out.println("Enter Start time (hh:mm) :");
        DateTime startTime = InputReader.readTime();

        System.out.println("Enter Duration in Hours(hh) :");
        int duration = InputReader.readInt();

        System.out.println("Enter Cost per Hour :");
        int cost = InputReader.readInt();

        applyRules();

        this.setMemberId(Session.getCurrentUser().getMemberId());
        this.setCity(startingAddress);
        this.setStartDate(startdate);
        this.setEndDate(endDate);
        this.setDurationInHours(duration);
        this.setStartTime(startTime);
        this.setCostPerHour(cost);
        super.acceptRequest();

    }

    @Override
    public void processRequest() throws InvalidInputException, IOException {
        //this.getSchedulingAlgorithm().scheduleParkingRequest(this);
        super.processRequest();
    }

    @Override
    public void completeRequest() {
        super.completeRequest();
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
