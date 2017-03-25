package com.sjsu.reports;


public class DailyReport extends RevenueReport {

    public DailyReport(String savingReportData) {
        this();
        reportData = savingReportData;
    }

    public DailyReport() {
        super();
        reportType = "Daily Report";
    }

    @Override
    public void generateReport() {
        super.generateReport();
    }
}
