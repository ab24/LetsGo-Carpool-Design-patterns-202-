package com.sjsu.member;

import com.sjsu.model.*;
import com.sjsu.notification.Notification;
import com.sjsu.notification.Observer;
import com.sjsu.ratingsandreviews.RatingsAndReview;
import com.sjsu.request.Request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.sjsu.client.PrintUtil.printLine;


public abstract class Member extends Observer implements Serializable {
    private MemberId memberId;
    private String username;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String emailId;
    private int numberOfServicesUsed;
    private Date membershipStartDate;
    private MemberType memberType;
    private MembershipType membershipType;
    private MembershipStatus membershipStatus;
    private String password;
    private String creditCardNumber;
    private List<RatingsAndReview> ratingsAndReviews = newArrayList();
    private List<Notification> notifications = newArrayList();
    private DigitalWallet wallet;

    public MemberId getMemberId() {
        return memberId;
    }

    public void setMemberId(MemberId memberId) {
        this.memberId = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Date getMembershipStartDate() {
        return membershipStartDate;
    }

    public void setMembershipStartDate(Date membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public MembershipStatus getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(MembershipStatus membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public DigitalWallet getWallet() {
        return wallet;
    }

    public void setWallet(DigitalWallet wallet) {
        this.wallet = wallet;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public List<RatingsAndReview> getRatingsAndReviews() {
        return ratingsAndReviews;
    }

    public void addRatingsAndReview(RatingsAndReview ratingsAndReview) {
        this.ratingsAndReviews.add(ratingsAndReview);
    }

    public int getNumberOfServicesUsed() {
        return numberOfServicesUsed;
    }


    public void setNumberOfServicesUsed(int numberOfServicesUsed) {
        this.numberOfServicesUsed = numberOfServicesUsed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;

        Member member = (Member) o;

        if (!getMemberId().equals(member.getMemberId())) return false;
        if (!getUsername().equals(member.getUsername())) return false;
        if (!getFirstName().equals(member.getFirstName())) return false;
        if (!getLastName().equals(member.getLastName())) return false;
        if (!getContactNumber().equals(member.getContactNumber())) return false;
        if (!getEmailId().equals(member.getEmailId())) return false;
        if (!getMembershipStartDate().equals(member.getMembershipStartDate())) return false;
        if (getMemberType() != member.getMemberType()) return false;
        if (getMembershipStatus() != member.getMembershipStatus()) return false;
        return getPassword().equals(member.getPassword());

    }

    @Override
    public int hashCode() {
        int result = getMemberId().hashCode();
        result = 31 * result + getUsername().hashCode();
        result = 31 * result + getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getContactNumber().hashCode();
        result = 31 * result + getEmailId().hashCode();
        result = 31 * result + getMembershipStartDate().hashCode();
        result = 31 * result + getMemberType().hashCode();
        result = 31 * result + getMembershipStatus().hashCode();
        result = 31 * result + getPassword().hashCode();
        return result;
    }

    @Override
    public void update(Request request, Notification notification) {
        printLine();
        System.out.println("Member :=> " + username + " received notification");
        System.out.println("Notification Type :=> " + notification.getType());
        System.out.println("Notification :=> " + notification.toString());
        notifications.add(notification);
        printLine();
    }
}
