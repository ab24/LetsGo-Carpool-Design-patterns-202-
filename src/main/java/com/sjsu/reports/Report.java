package com.sjsu.reports;


public abstract class Report {
    String reportData = "Could not generate Report.";
    String reportType = "Could not find report type.";

    public void generateReport() {

        System.out.println(reportType);
        System.out.println(reportData + "\n");

    }
}
