package com.sjsu.notification;

import com.sjsu.util.IdGenerator;


public abstract class Notification {

    private String notificationId;
    private String message;
    private NotificationType type;
    private String sendToUsername;
    private String sentFromUsername;

    public Notification() {

    }

    private Notification(String notificationId, NotificationType type, String message, String sendToUsername, String sentFromUsername) {
        this.notificationId = notificationId;
        this.message = message;
        this.type = type;
        this.sendToUsername = sendToUsername;
        this.sentFromUsername = sentFromUsername;
    }

    public static Notification buildMemberNotification(String message, String sendToUsername, String sendFromUsername) {
        String notificationId = IdGenerator.generateId("N");
        return new MemberNotification(notificationId, NotificationType.MEMBER, message, sendToUsername, sendFromUsername);
    }

    public static Notification buildSystemNotification(String message, String sendToUsername) {
        String notificationId = IdGenerator.generateId("N");

        return new SystemNotification(notificationId, NotificationType.SYSTEM, message, sendToUsername, "System");
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId='" + notificationId + '\'' +
                ", message='" + message + '\'' +
                ", sendTo=" + sendToUsername +
                ", sentFrom=" + sentFromUsername +
                '}';
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }

    public String getSendToUsername() {
        return sendToUsername;
    }

    public String getSentFromUsername() {
        return sentFromUsername;
    }

    private static class SystemNotification extends Notification {
        public SystemNotification(String notificationId, NotificationType type, String message, String sendToUsername, String sentFromUsername) {
            super(notificationId, type, message, sendToUsername, sentFromUsername);
        }
    }

    private static class MemberNotification extends Notification {
        public MemberNotification(String notificationId, NotificationType type, String message, String sendToUsername, String system) {
            super(notificationId, type, message, sendToUsername, system);
        }
    }
}


