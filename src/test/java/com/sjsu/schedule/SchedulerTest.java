package com.sjsu.schedule;

import com.sjsu.request.RideRequest;
import com.sjsu.request.state.RequestWaitingState;
import com.sjsu.routing.RouteManager;
import com.sjsu.scheduler.Algorithm1;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class SchedulerTest {


    public void test_SceduleAlgorithm1() {
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yy");
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");

        try {
            RideRequest rideRequest = new RideRequest(new Algorithm1(new RouteManager()));
            rideRequest.setStartingAddress("Sunnyvale");
            rideRequest.setEndingAddress("San Francisco");
            rideRequest.setStartDate(dateFormatter.parseDateTime("08/24/16"));
            rideRequest.setEndDate(dateFormatter.parseDateTime("09/24/16"));
            rideRequest.setStartTime(timeFormatter.parseDateTime("10:00"));
            rideRequest.setNoOfPassengers(1);
            rideRequest.setCreationTime(DateTime.now().toDate());
            rideRequest.setRequestId("R1009");

            rideRequest.setRequestState(new RequestWaitingState(rideRequest));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
