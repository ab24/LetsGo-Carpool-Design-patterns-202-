package com.sjsu.reports;


public class DailyRide extends Report {

    public DailyRide(String dailyRideData) {
        this();
        reportData = dailyRideData;
    }

    public DailyRide() {
        reportType = "Daily Ride Report";
    }

    @Override
    public void generateReport() {
        super.generateReport();
    }
}
