package com.sjsu.reports;

public class DailyTrip extends Report {

    public DailyTrip(String dailyTripData) {
        this();
        reportData = dailyTripData;
    }

    public DailyTrip() {
        super();
        reportType = "Daily Trip Report";
    }

    @Override
    public void generateReport() {
        super.generateReport();
    }
}
