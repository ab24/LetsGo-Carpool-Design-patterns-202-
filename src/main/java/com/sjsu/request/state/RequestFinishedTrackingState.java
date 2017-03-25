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


public class RequestFinishedTrackingState implements RequestState {
    private Request request;

    public RequestFinishedTrackingState(Request request) {
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
        request.notifyMember(Notification.buildSystemNotification("Thank you for using our services. \nReleasing hold on $50.\n Help us improve by providing your Review.", getUsername()));
        ServiceTracker.getTracker(request).endTracking();
        TrackingInfo trackingInfo = TrackingDAO.getTrackerDAO().getTrackingInfo(request);


        System.out.println("Processing payment");
        PaymentContext paymentContext = new PaymentContext();
        paymentContext.processPayment(request, trackingInfo);

        Member currentUser = Session.getCurrentUser();
        currentUser.getWallet().creditAmount(50.00);
        request.setRequestState(new RequestCompletedState(request));
    }

    private String getUsername() {
        return MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(request.getMemberId()).getUsername();
    }
}