package com.sjsu.reports;


public class MonthlyReport extends RevenueReport {

    private static MonthlyReport monthlyReport = new MonthlyReport();

    private MonthlyReport(String monthlyReportData) {
        this();
        reportData = monthlyReportData;
    }

    public MonthlyReport() {
        super();
        reportType = "Monthly Report ";
    }

    @Override
    public void generateReport() {
        super.generateReport();
    }

    public static MonthlyReport getMonthlyReport(String reportData){
        monthlyReport.reportData = reportData;
        return MonthlyReport.monthlyReport;
    }
}
