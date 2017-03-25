package com.sjsu.reports;

import com.sjsu.client.InputReader;
import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.dao.TrackingDAO;
import com.sjsu.member.Member;
import com.sjsu.model.MemberId;
import com.sjsu.track.ReservedParking;
import com.sjsu.track.Ride;
import com.sjsu.track.TrackingInfo;
import com.sjsu.track.Trip;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static com.sjsu.client.PrintUtil.printMenuTitle;


public class ReportManager {

    public static void printMenu() throws IOException {

        printMenuTitle("Manage Report Menu");

        System.out.println("1. Daily Ride");
        System.out.println("2. Daily Trip");
        System.out.println("3. Daily Report");
        System.out.println("4. Monthly Report");

        String option = InputReader.readLine();

        switch (option) {
            case "1":
                dailyRide();
                break;
            case "2":
                dailyTrip();
                break;
            case "3":
                dailyReport();
                break;
            case "4":
                monthlyReport();
                break;
            default:
                ReportManager.printMenu();

        }
    }


    private static void dailyRide() {
        Map<MemberId, List<TrackingInfo>> trackingInfoMap = TrackingDAO.getTrackingInfoMap();

        DailyRide dailyReports = new DailyRide(getReportDate(trackingInfoMap));
        dailyReports.generateReport();

    }
    private static void dailyTrip() {
        Map<MemberId, List<TrackingInfo>> trackingInfoMap = TrackingDAO.getTrackingInfoMap();

        DailyTrip dailyReports = new DailyTrip(getReportDate(trackingInfoMap));
        dailyReports.generateReport();

    }

    private static void dailyReport(){
        dailyRide();
        dailyTrip();

    }
    private static void monthlyReport() {
        Map<MemberId, List<TrackingInfo>> trackingInfoMap = TrackingDAO.getTrackingInfoMap();
        DailyRide dailyRide = new DailyRide("Today's Daily Ride Data" );
        DailyTrip dailyTrip = new DailyTrip("Today's Daily Trip Data" );
        DailyReport dailyReport = new DailyReport("Today's Daily Report Data");
        dailyReport.add(dailyRide);
        dailyReport.add(dailyTrip);

        DailyRide yesterdayDailyRide = new DailyRide("Yesterday's Daily Ride Data");
        DailyTrip yesterdayDailyTrip = new DailyTrip("Yesterday's Daily Trip Data");
        DailyReport yesterdayDailyReport = new DailyReport("Yesterday's Daily Report Data");
        yesterdayDailyReport.add(yesterdayDailyRide);
        yesterdayDailyReport.add(yesterdayDailyTrip);

        MonthlyReport monthlyReport = MonthlyReport.getMonthlyReport("Overall Monthly Report Data");
        monthlyReport.add(dailyReport);
        monthlyReport.add(yesterdayDailyReport);

        ReportClient.report = monthlyReport;
        ReportClient.doClientTasks();


    }

    private static String getReportDate(Map<MemberId, List<TrackingInfo>> trackingInfoMap) {
        Map<Ride, Member> allRides = newHashMap();
        Map<Trip, Member> allTrips = newHashMap();
        Map<ReservedParking, Member> allParking= newHashMap();

        for (Map.Entry<MemberId, List<TrackingInfo>> infos : trackingInfoMap.entrySet()) {
            Member member = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(infos.getKey());
            for (TrackingInfo trackingInfo : infos.getValue()) {
                if(trackingInfo instanceof Ride){
                    allRides.put((Ride)trackingInfo,member);
                }else if(trackingInfo instanceof Trip){
                    allTrips.put((Trip)trackingInfo,member);
                }else if(trackingInfo instanceof ReservedParking){
                    allParking.put((ReservedParking)trackingInfo,member);
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\t\t----- Rides ------\t\t\n");

        int rideCounter=0;
        for(Map.Entry<Ride,Member> entry : allRides.entrySet()){
            rideCounter = rideCounter+1;

            Ride ride = entry.getKey();
            Member member = entry.getValue();
            stringBuilder.append(rideCounter).append(".\t").append(member.getUsername()).append("\t")
                    .append(ride.getMiles()).append(" Miles").append("\t").append(ride.getCost()).append("\n");

        }

        stringBuilder.append("\n\t\t----- Trips ------\t\t\n");
        int tripCounter=0;
        for(Map.Entry<Trip,Member> entry : allTrips.entrySet()){
            tripCounter = tripCounter+1;

            Trip trip = entry.getKey();
            Member member = entry.getValue();
            stringBuilder.append(tripCounter).append(".\t").append(member.getUsername()).append("\t")
                    .append(trip.getMiles()).append(" Miles").append("\t").append(trip.getCost()).append("\n");

        }
        stringBuilder.append("\n\t\t----- Parking ------\t\t\n");

        int parkingCounter=0;
        for(Map.Entry<ReservedParking,Member> entry : allParking.entrySet()){
            parkingCounter = parkingCounter+1;

            ReservedParking reservedParking = entry.getKey();
            Member member = entry.getValue();
            stringBuilder.append(parkingCounter).append(".\t").append(member.getUsername()).append("\t")
                    .append(reservedParking.getCost()).append("\n");
        }

        return stringBuilder.toString();
    }




}
