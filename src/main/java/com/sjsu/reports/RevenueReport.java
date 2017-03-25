package com.sjsu.reports;

import java.util.Vector;


public class RevenueReport extends Report {
    protected Vector directReports = new Vector();


    @Override
    public void generateReport() {
        super.generateReport();

        if( directReports.size() > 0 )
         for( int i = 0; i < directReports.size(); ++i )
                ( (Report)directReports.elementAt( i ) ).generateReport();

    }

    public void add(Report report) {

        this.directReports.addElement(report);

    }
}
