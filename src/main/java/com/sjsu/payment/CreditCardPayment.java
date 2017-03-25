package com.sjsu.payment;

import com.sjsu.request.Request;
import com.sjsu.track.TrackingInfo;


public class CreditCardPayment implements PaymentStrategy {
    @Override
    public void processPayment(Request request, TrackingInfo trackingInfo) {
        System.out.println("Payment processed :=> Credit Card");
    }
}
