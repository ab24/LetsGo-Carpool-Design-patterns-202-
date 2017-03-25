package com.sjsu.client;


import com.sjsu.dao.InvalidInputException;
import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.dao.MemberNotFoundException;
import com.sjsu.datastore.DummyData;
import com.sjsu.datastore.Session;
import com.sjsu.dispatch.DispatchManager;
import com.sjsu.member.*;
import com.sjsu.model.DigitalWallet;
import com.sjsu.model.MemberType;
import com.sjsu.model.MembershipStatus;
import com.sjsu.model.MembershipType;
import com.sjsu.notification.NotificationManager;
import com.sjsu.ratingsandreviews.RatingsAndReviewManager;
import com.sjsu.reports.ReportManager;
import com.sjsu.request.RequestManager;
import com.sjsu.request.state.InvalidRequestTypeException;
import com.sjsu.rules.MemberRule;
import com.sjsu.scheduler.SchedulingManager;
import com.sjsu.track.TrackingAndRoutingManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import static com.sjsu.client.InputReader.readLine;
import static com.sjsu.client.InputReader.readPassword;
import static com.sjsu.client.PrintUtil.*;


public class MainMenu {
    private static MainMenu mainMenu;

    private MainMenu() {
        loadData();
    }

    public static MainMenu getMainMenu() {
        if (mainMenu == null) {
            mainMenu = new MainMenu();
        }
        return mainMenu;
    }

    public void start() {
        while (true) {
            try {
                printMainMenu();
            } catch (IOException e) {
                System.out.println("Sorry! error processing input.");
            } catch (ParseException | InvalidInputException | InvalidRequestTypeException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadData() {
        try {
            System.out.println("Loading Data ...");
            DummyData.init();
            System.out.println("Data load complete.");
        } catch (FileNotFoundException e) {
            System.out.println("Error loading data from file." + e.getMessage());
        }
    }


    private void printSignInMenu() throws IOException, InvalidInputException, ParseException, InvalidRequestTypeException {
        printLine();
        System.out.print("\nHi! there. Welcome to the Let's Go App\n");
        selectOptionString();
        printLine();
        System.out.println("1. Register Member");
        System.out.println("2. Login");
        String line = readLine();
        switch (line) {
            case "1":
                printRegistrationMenu();
                break;
            case "2":
                printLoginMenu();
                break;
            default:
                System.out.println("Incorrect input");
                printSignInMenu();
        }

    }

    private void printOrgMenu() throws IOException, InvalidInputException, ParseException, InvalidRequestTypeException {

        if (Session.isSessionValid()) {
            printLine();
            System.out.print("\nHi! " + Session.getCurrentUser().getUsername() + "\n");
            selectOptionString();
            printMenuTitle("Main Menu");
            System.out.println("1. Offer Parking");
            System.out.println("2. Cancel Parking");

            String line = readLine();
            switch (line) {
                case "1":
                    RequestManager.showOfferParking();
                    break;
                case "2":
                    notSupported();
                    break;
                default:
                    System.out.println("Incorrect input");
                    printOrgMenu();

                    logoutAndGoToMainMenu();
            }
        } else {
            printSignInMenu();
        }

    }

    private void printMainMenu() throws IOException, InvalidInputException, ParseException, InvalidRequestTypeException {

        if (Session.isSessionValid()) {
            printMenu();
        } else {
            printSignInMenu();
        }
    }

    private void printMenu() throws IOException, InvalidInputException, ParseException, InvalidRequestTypeException {
        printLine();
        System.out.print("\nHi! " + Session.getCurrentUser().getUsername() + "\n");
        selectOptionString();
        printMenuTitle("Main Menu");
        System.out.println("1. Manage Membership");
        System.out.println("2. Manage Request");
        System.out.println("3. Schedule Request");
        System.out.println("4. Dispatch Request");
        System.out.println("5. Track Requests");
        System.out.println("6. View  Notifications");
        System.out.println("7. Ratings and Review");
        System.out.println("8. Generate  Report");
        logoutAndGoToMainMenu();

        String option = InputReader.readLine();

        switch (option) {
            case "1":
                MemberManager.printMenu();
                break;
            case "2":
                RequestManager.printMenu();
                break;
            case "3":
                SchedulingManager.printMenu();
                break;
            case "4":
                DispatchManager.printMenu();
                break;
            case "5":
                TrackingAndRoutingManager.printMenu();
                break;
            case "6":
                NotificationManager.printMenu();
                break;
            case "7":
                RatingsAndReviewManager.printMenu();
                break;
            case "8":
                ReportManager.printMenu();
                break;
            default:
                handleOtherInput(option);

        }

    }

    public void handleOtherInput(String option) {
        if ("x".equalsIgnoreCase(option)) {
            MainMenu.getMainMenu().start();
        } else if ("l".equalsIgnoreCase(option)) {
            Session.removeFromSession();
        } else {
            System.out.println("Invalid input.");
            MainMenu.getMainMenu().start();
        }
    }

    public void logoutAndGoToMainMenu() {
        String mainMenu = "Press (X) to go back to main menu.";
        String logoutMenu = "Press (L) to logout.";
        System.out.println(mainMenu);
        System.out.println(logoutMenu);
    }

    public void printRegistrationMenu() throws IOException, InvalidInputException, ParseException, InvalidRequestTypeException {
        System.out.println("Welcome to member registration");
        printLine();

        System.out.println("1. Register as a Driver");
        System.out.println("2. Register as a Rider");
        System.out.println("3. Register as an Organization");
        String userInput = InputReader.readLine();
        switch (userInput) {
            case "1":
                printDriverRegistrationMenu();
                break;
            case "2":
                printRiderRegistrationMenu();
                break;
            case "3":
                printOrganizationRegistrationMenu();
                break;
            default:
                start();
        }
    }

    private void printOrganizationRegistrationMenu() throws IOException, ParseException, InvalidInputException, InvalidRequestTypeException {
        System.out.println("Enter Organization's Contact First Name :");
        String firstName = InputReader.readLine();

        System.out.println("Enter Organization's Contact Second Name :");
        String secondName = InputReader.readLine();

        System.out.println("Enter Username :");
        String username = InputReader.readLine();

        System.out.println("Enter Password :");
        String password = InputReader.readPassword();

        System.out.println("Enter Email id :");
        String emailId = InputReader.readLine();

        System.out.println("Enter Contact Number :");
        String contactNumer = InputReader.readLine();

        System.out.println("Enter Credit Card Number :");
        String creditCardNumber = InputReader.readLine();

        System.out.println("Enter Tax Number :");
        String taxNumber = InputReader.readLine();

        Organisation organisation = new Organisation();
        organisation.setFirstName(firstName);
        organisation.setLastName(secondName);
        organisation.setUsername(username);
        organisation.setPassword(password);
        organisation.setEmailId(emailId);
        organisation.setContactNumber(contactNumer);
        organisation.setCreditCardNumber(creditCardNumber);
        organisation.setTaxNumber(taxNumber);
        organisation.setMemberType(MemberType.Organization);
        organisation.setMembershipType(MembershipType.Silver);
        organisation.setMembershipStatus(MembershipStatus.ACTIVE);
        DigitalWallet wallet = new DigitalWallet();
        wallet.creditAmount(1000);
        organisation.setWallet(wallet);


        MemberRule memberRule = new MemberRule(organisation);
        memberRule.applyRule();

        MemberManager memberManager = MemberManager.getMemberManager();
        Member registerMember = memberManager.registerMember(organisation);

        System.out.println("Congratulations!! Registration successful.");
        System.out.println("Your membership Id is: " + registerMember.getMemberId());
        Session.addToSession(registerMember);
        printOrgMenu();

    }

    private void printDriverRegistrationMenu() throws IOException, ParseException, InvalidInputException, InvalidRequestTypeException {
        System.out.println("Enter First Name :");
        String firstName = InputReader.readLine();

        System.out.println("Enter Last Name :");
        String lastName = InputReader.readLine();

        System.out.println("Enter Username :");
        String username = InputReader.readLine();

        System.out.println("Enter Password :");
        String password = InputReader.readPassword();

        System.out.println("Enter Email id :");
        String emailId = InputReader.readLine();

        System.out.println("Enter Contact Number :");
        String contactNumer = InputReader.readLine();

        System.out.println("Enter Credit Card Number :");
        String creditCardNumber = InputReader.readLine();

        System.out.println("Enter Driver's Licence Number :");
        String driversLicence = InputReader.readLine();

        System.out.println(" \t\t------   Vehicle Details     -----");
        System.out.println("Enter Vehicle Make :");
        String vehicleMake = InputReader.readLine();

        System.out.println("Enter Vehicle Number :");
        String vehicleNumber = InputReader.readLine();

        System.out.println("Enter Vehicle Seat Count :");
        int vehicleSeatCount = InputReader.readInt();

        //TODO:Write member builder
        Driver driver = new Driver();
        driver.setFirstName(firstName);
        driver.setLastName(lastName);
        driver.setMemberType(MemberType.Driver);
        driver.setMembershipType(MembershipType.Silver);
        driver.setDriverLicenceNumber(driversLicence);
        driver.setEmailId(emailId);
        driver.setPassword(password);
        driver.setContactNumber(contactNumer);
        driver.setMembershipStatus(MembershipStatus.ACTIVE);
        driver.setUsername(username);
        driver.setCreditCardNumber(creditCardNumber);

        Vehicle vehicle = new Vehicle(vehicleMake, vehicleNumber, vehicleSeatCount);


        MemberManager memberManager = MemberManager.getMemberManager();
        Member registerMember = memberManager.registerMember(driver);

        MemberRule memberRule = new MemberRule(driver);
        memberRule.applyRule();

        System.out.println("Congratulations!! Registration successful.");
        System.out.println("Your membership Id is: " + registerMember.getMemberId());
        Session.addToSession(registerMember);
        printMainMenu();
    }

    public void printRiderRegistrationMenu() throws IOException, ParseException, InvalidInputException, InvalidRequestTypeException {
        System.out.println("Enter First Name :");
        String firstName = InputReader.readLine();

        System.out.println("Enter Last Name :");
        String lastName = InputReader.readLine();

        System.out.println("Enter Username :");
        String username = InputReader.readLine();

        System.out.println("Enter Password :");
        String password = InputReader.readPassword();

        System.out.println("Enter Email id :");
        String emailId = InputReader.readLine();

        System.out.println("Enter Contact number :");
        String contactNumer = InputReader.readLine();

        System.out.println("Enter Credit Card Number :");
        String creditCardNumber = InputReader.readLine();


        //TODO:Write member builder
        Rider rider = new Rider();
        rider.setFirstName(firstName);
        rider.setLastName(lastName);
        rider.setMemberType(MemberType.Rider);
        rider.setMembershipType(MembershipType.Silver);
        rider.setEmailId(emailId);
        rider.setPassword(password);
        rider.setContactNumber(contactNumer);
        rider.setMembershipStatus(MembershipStatus.ACTIVE);
        rider.setUsername(username);
        rider.setCreditCardNumber(creditCardNumber);


        MemberManager memberManager = MemberManager.getMemberManager();
        Member registerMember = memberManager.registerMember(rider);

        MemberRule memberRule = new MemberRule(rider);
        memberRule.applyRule();

        System.out.println("Congratulations!! Registration successful.");
        System.out.println("Your membership Id is: " + registerMember.getMemberId());
        Session.addToSession(registerMember);
        printMainMenu();
    }

    private void printLoginMenu() throws IOException, InvalidInputException, ParseException, InvalidRequestTypeException {
        String username = "";
        String password = "";

        System.out.println("Enter username:");
        username = readLine();
        if (username == null || "".equals(username)) {
            System.out.println("Invalid Input! Please enter username");
            printLoginMenu();
        } else {
            System.out.println("Enter password:");
            password = readPassword();
        }

        if (isLoginSuccessful(username, password)) {
            printMainMenu();
        } else {
            System.out.println("Incorrect credentials! Please try again.\n");
        }

    }

    private boolean isLoginSuccessful(String username, String password) {
        MemberDetailsDAO memberDetailsDAO = MemberDetailsDAO.getMemberDetailsDAO();
        try {
            Member memberDetails = memberDetailsDAO.getMemberDetailsByUsername(username);
            boolean ifPasswordMatches = password.equals(memberDetails.getPassword());
            if (ifPasswordMatches) {
                Session.addToSession(memberDetails);
            }
            return ifPasswordMatches;
        } catch (MemberNotFoundException e) {

        }

        return false;
    }


}
