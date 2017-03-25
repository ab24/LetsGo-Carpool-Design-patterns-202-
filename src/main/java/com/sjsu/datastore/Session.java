package com.sjsu.datastore;

import com.sjsu.member.Member;


public class Session {

    private static Member currentUser;

    public static void addToSession(Member member) {
        currentUser = member;
    }

    public static void removeFromSession() {
        currentUser = null;
    }

    public static boolean isSessionValid() {
        return currentUser != null;
    }

    public static Member getCurrentUser() {
        return currentUser;
    }


}
