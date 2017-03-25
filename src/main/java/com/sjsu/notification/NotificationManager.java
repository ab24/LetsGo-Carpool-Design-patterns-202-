package com.sjsu.notification;

import com.sjsu.client.InputReader;
import com.sjsu.client.MainMenu;
import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.datastore.DataStore;
import com.sjsu.datastore.Session;
import com.sjsu.member.Member;
import com.sjsu.model.MemberId;
import com.sjsu.request.DriveRequest;
import com.sjsu.request.Request;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static com.sjsu.client.PrintUtil.printLine;
import static com.sjsu.client.PrintUtil.printMenuTitle;

public class NotificationManager {

    public NotificationManager() {

    }

    public static void printMenu() throws IOException {
        printMenuTitle("Notifications");
        System.out.println("1. Notifications to Members");
        System.out.println("2. Send Notifications");
        System.out.println("4. Go Back");
        String option = InputReader.readLine();

        switch (option) {
            case "1":
                memberNotifications();
                break;
            case "2":
                sendNotifications();
                break;
            default:
                MainMenu.getMainMenu().start();

        }


    }

    public static void memberNotifications() throws IOException {
        //Print the member notifications
        // Get the currentMember from Session
        //get all notifications for a member (member.getNotification())
        //filter and show only those notification which have (notification.getType() == NotificationType.MEMBER)
        printMenuTitle("Member Notifications");
        Member member = Session.getCurrentUser();
        List<Notification> notificationList = member.getNotifications();
        for (Notification notifications : notificationList) {
            System.out.println(notifications.toString());

        }

    }


    public static void sendNotifications() throws IOException {
        //Send Notifications
       /* List<DriveRequest> allDriveRequests = DataStore.getAllDriveRequests();
       // Member rider = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(this.getMemberId());

        if(allDriveRequests.isEmpty()){
            System.out.println("No Drive Requests found");
        } else {
            for(Request driveRequest:allDriveRequests){
                Member driver = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(driveRequest.getMemberId());
                System.out.println("Ride Id:" + driveRequest.getRequestId());
                System.out.println("Enter Ride ID to send Message");
                String option = InputReader.readLine();
                driveRequest.notifyMember(Notification.buildMemberNotification("Dear Rider", "john123" , driver.getUsername()));
               // rideRequest.notifyMember(Notification.buildSystemNotification("Text to Rider \n" + rider.getUsername() + " your driver mately " + calculateDistance(rideRequest, this).intValue(), rider.getUsername()));
                memberNotifications();

            }

        }*/

        Member currentUser = Session.getCurrentUser();
        List<Request> allDriveRequestsForMember = DataStore.getAllDriveRequestsForMember(currentUser.getMemberId());
        if (allDriveRequestsForMember != null && allDriveRequestsForMember.size() > 0) {
            try {
                printLine();
                System.out.println("Select drive request for which to send notification");
                printLine();
                int i = 0;
                for (Request request : allDriveRequestsForMember) {
                    i = i + 1;
                    DriveRequest driveRequest = (DriveRequest) request;
                    System.out.println(i + ". - [" + driveRequest.getStartingAddress() + " - " + driveRequest.getEndingAddress() + "]");
                }

                int userInput = InputReader.readInt();
                DriveRequest selected = (DriveRequest) allDriveRequestsForMember.get(userInput - 1);
                System.out.println("Enter notification details: ");
                String notif = InputReader.readLine();
                selected.notifyMember(Notification.buildMemberNotification(notif, "ALL", currentUser.getUsername()));
                for (MemberId memberId : selected.getMemberNotificationObserver()) {
                    Member rider = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(memberId);
                    //TODO: send notification to specific rider
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("You do not have any drive requests yet.");
        }

        //get Current user
        //get All driverRequests for currently logged-in member from -
        // DataStore.getAllDriveRequestsForMember(memberId)
        // if list is empty
        //print -- "you dont have drive requests --and only drivers can send notifications"
        //else
        //ask user -- if he wants to send notification to a group or single user
        //ask user to select drive request for which to send notification
        //if group i.e tp notify all riders on a selected trip/drive request
        //driveRequest.notifyMember(Notification.buildMemberNotification(message, driver's username,"ALL"))
        // else ask(provide an option to select) username of a rider in a given trip/request
        //driveRequest.notifyMember(Notification.buildMemberNotification(message, driver's username,selectedRider'susername))

    }

    public static void riderNotifications() throws IOException {
        //Print the member notifications
        // Get the currentMember from Session
        //get all notifications for a member (member.getNotification())
        //filter and show only those notification which have (notification.getType() == NotificationType.MEMBER)
        printMenuTitle("Member Notifications");
        Member member = Session.getCurrentUser();
        List<Notification> notificationList = member.getNotifications();
        for (Notification notifications : notificationList) {
            if (notifications.getType() == NotificationType.MEMBER) {
                System.out.println(notifications.toString());
            }
        }
    }


}
