package com.sjsu.client;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;

public class PrintUtil {

    public static void printLine() {
        System.out.print("\n________________________________________________\n");
    }

    public static void selectOptionString() {
        System.out.print("Please select one of the following options : ");
    }

    public static void printMenuTitle(String title) {
        printLine();
        System.out.print("\t\t>>>>>> " + title.toUpperCase() + " <<<<<<");
        printLine();
    }

    public static void notSupported() {
        System.out.println("Sorry! This operation is not yet supported.");
    }

    public static String printDate(DateTime date) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yy");
            return dateFormatter.print(date);
        } catch (Exception ex) {
            return "NA";
        }
    }

    public static String printTime(DateTime time) {
        try {
            DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm");
            return timeFormatter.print(time);
        } catch (Exception e) {
            return "NA";
        }
    }

    public static String roundDistance(Double distance) {
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(distance);
    }
}
