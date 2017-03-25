package com.sjsu.notification;

import com.sjsu.request.Request;


public class NotificationManagerOld {
    public static void printMenu() {

    }

    public static void sendRequestStartingNotification(Request request) {
        switch (request.getRequestType()) {
            case PARKING:

                break;
            case OFFER_PARKING:
                break;
            case DRIVE_NOW:
                break;
            case DRIVE_SCHEDULE:
                break;
            case RIDE_NOW:
                break;
            case RIDE_SCHEDULE:
                break;

        }
    }


}
