package com.sjsu.payment;

import com.sjsu.request.Request;
import com.sjsu.track.TrackingInfo;

public interface PaymentStrategy {
    void processPayment(Request request, TrackingInfo trackingInfo);
}
