package com.sjsu.member;

import com.sjsu.client.InputReader;
import com.sjsu.client.MainMenu;
import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.dao.MemberNotFoundException;
import com.sjsu.datastore.Session;

import java.io.IOException;

import static com.sjsu.client.PrintUtil.notSupported;
import static com.sjsu.client.PrintUtil.printMenuTitle;


public class MemberManager {

    private static MemberManager memberManager;

    private MemberManager() {

    }

    public static MemberManager getMemberManager() {
        if (memberManager == null) {
            memberManager = new MemberManager();
        }
        return memberManager;
    }

    public static void printMenu() throws IOException {
        printMenuTitle("Manage Member Menu");

        System.out.println("1. View profile");
        System.out.println("2. Update profile");
        System.out.println("3. Cancel membership");
        System.out.println("4. Go back to main menu");

        String option = InputReader.readLine();

        switch (option) {
            case "1":
                viewUserProfile();
                break;
            case "2":
                notSupported();
                break;
            case "3":
                //notSupported();
                Member member = Session.getCurrentUser();
                MemberDetailsDAO memberDetailsDAO = MemberDetailsDAO.getMemberDetailsDAO();
                memberDetailsDAO.suspendMembership(member.getMemberId());
                System.out.println("Membership Cancelled");
                break;
            case "4":
            default:
                MainMenu.getMainMenu().start();

        }

    }

    public static void viewUserProfile() throws IOException {

        System.out.println("1. View Current User profile");
        System.out.println("2. Search for User Profile");

        String option = InputReader.readLine();

        switch (option) {
            case "1":
                Member member = Session.getCurrentUser();
                printUserProfile(member.getUsername());
                break;
            case "2":
                System.out.println("Enter UserName to Search");
                String userName = InputReader.readLine();
                printUserProfile(userName);
                break;
            default:
                MainMenu.getMainMenu().start();
        }
    }

    public static void printUserProfile(String userName) throws IOException {
        MemberDetailsDAO memberDetailsDAO = MemberDetailsDAO.getMemberDetailsDAO();
        try {
            Member memberDetails = memberDetailsDAO.getMemberDetailsByUsername(userName);
            boolean ifMemberMatches = userName.equals(memberDetails.getUsername());
            if (ifMemberMatches) {
                System.out.println("***** User Profile *********");
                System.out.println(memberDetails.getMemberId());
                System.out.println("Member User Name        :" + memberDetails.getUsername());
                System.out.println("Member First Name       :" + memberDetails.getFirstName());
                System.out.println("Member Last Name        :" + memberDetails.getLastName());
                System.out.println("Member Contact Number   :" + memberDetails.getContactNumber());
                System.out.println("Member Email Id         :" + memberDetails.getEmailId());
                System.out.println("Member Type             :" + memberDetails.getMemberType());
                System.out.println("Membership Status       :" + memberDetails.getMembershipStatus());
                System.out.println("Membership Type         :" + memberDetails.getMembershipType());
                System.out.println("Member Card Number      :" + memberDetails.getCreditCardNumber());
                System.out.println("Member Digital Wallet   :" + memberDetails.getWallet());
            } else {
                System.out.println("Member not found");
            }
        } catch (MemberNotFoundException e) {

        }
    }

    public Member registerMember(Member member) {
        return MemberDetailsDAO.addMember(member);
    }
}
