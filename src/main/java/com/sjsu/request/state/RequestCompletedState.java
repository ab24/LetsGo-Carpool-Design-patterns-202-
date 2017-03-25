package com.sjsu.request.state;

import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.dao.TrackingDAO;
import com.sjsu.datastore.Session;
import com.sjsu.member.Member;
import com.sjsu.notification.Notification;
import com.sjsu.payment.PaymentContext;
import com.sjsu.request.IRequest;
import com.sjsu.request.Request;
import com.sjsu.track.ServiceTracker;
import com.sjsu.track.TrackingInfo;

import java.io.IOException;
import java.text.ParseException;


public class RequestCompletedState implements RequestState {
    private Request request;

    public RequestCompletedState(Request request) {
        this.request = request;
    }

    @Override
    public void acceptRequest() throws InvalidRequestTypeException, IOException, ParseException {
        System.out.println("Request already accepted");
    }

    @Override
    public void waitInQueue() {
        System.out.println("Request removed out of waiting queue");
    }

    @Override
    public void scheduleRequest() {
        System.out.println("Request already scheduled");
    }

    @Override
    public void dispatchRequest() {
        System.out.println("Request was dispatched");
    }

    @Override
    public void rejectRequest() {
        System.out.println("Request was dispatched");
    }

    @Override
    public void cancelRequest() {
        System.out.println("Request was dispatched");
    }

    @Override
    public void completeRequest() {
        System.out.println("Request completed");
    }

}