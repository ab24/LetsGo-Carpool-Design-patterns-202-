package com.sjsu.client;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class Config {

    public static DateTime getRequestProcessingTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm");
        //TODO://GET from properties file
        return dateTimeFormatter.parseDateTime("09:00");
    }


}
