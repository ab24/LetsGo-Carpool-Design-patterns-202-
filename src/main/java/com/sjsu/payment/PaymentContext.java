package com.sjsu.payment;

import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.member.Member;
import com.sjsu.member.PlatinumMember;
import com.sjsu.model.DigitalWallet;
import com.sjsu.request.Request;
import com.sjsu.request.RequestType;
import com.sjsu.track.TrackingInfo;

public class PaymentContext {
    private PaymentStrategy paymentStrategy;

    public void processPayment(Request request, TrackingInfo trackingInfo) {
        Member member = MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(request.getMemberId());
        System.out.println("Checking if member is eligible for discount....");

        double discount = 0.0;
        if (member.getNumberOfServicesUsed() > 10) {
            PlatinumMember platinumMember = new PlatinumMember(member);
            discount = platinumMember.calculatePercentageDiscount(trackingInfo.getCost());
            platinumMember.addBalanceToWallet();
        }
        double cost = trackingInfo.getCost();
        double discountedCost = cost - discount;
        RequestType requestType = request.getRequestType();

        if (ifServicesOffered(requestType)) {
            System.out.println("Amount :=> $" + trackingInfo.getCost() + " will be added to your digital wallet.");
            paymentStrategy = new DigitalWalletPayment();
        } else {
            paymentStrategy = setStrategy(member.getWallet(), discountedCost);
        }

        paymentStrategy.processPayment(request, trackingInfo);
    }

    private boolean ifServicesOffered(RequestType requestType) {
        return requestType == RequestType.DRIVE_NOW ||
                requestType == RequestType.DRIVE_SCHEDULE ||
                requestType == RequestType.OFFER_PARKING;
    }

    private PaymentStrategy setStrategy(DigitalWallet wallet, double serviceCost) {
        PaymentStrategy toReturn = null;
        if (wallet.getTotalAmount() >= serviceCost) {
            System.out.println("Amount :=> $" + serviceCost + " will be deducted from your digital wallet.");
            toReturn = new DigitalWalletPayment();
        } else if (wallet.getTotalAmount() < serviceCost) {
            System.out.println("You do not have sufficient balance in your Digital Wallet.");
            System.out.println("Amount :=> $" + serviceCost + " will be deducted from your credit card.");
            toReturn = new CreditCardPayment();
        }
        return toReturn;
    }
}
