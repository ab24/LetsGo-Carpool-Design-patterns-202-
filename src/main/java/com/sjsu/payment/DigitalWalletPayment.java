package com.sjsu.payment;

import com.sjsu.request.Request;
import com.sjsu.track.TrackingInfo;


public class DigitalWalletPayment implements PaymentStrategy {

    @Override
    public void processPayment(Request request, TrackingInfo trackingInfo) {
        System.out.println("Payment processed :=> Digital Wallet");
    }
}
