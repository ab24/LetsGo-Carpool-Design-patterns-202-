package com.sjsu.request.state;

import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.request.IRequest;
import com.sjsu.request.Request;

import java.io.IOException;
import java.text.ParseException;


public class RequestDispatchingState implements RequestState {
    private Request request;

    public RequestDispatchingState(Request request) {
        this.request = request;
    }

    @Override
    public void acceptRequest() throws InvalidRequestTypeException, IOException, ParseException {
        System.out.println("Request already accepted");
    }

    @Override
    public void waitInQueue() {
        System.out.println("Request was fetched from queue for processing.");
    }

    @Override
    public void scheduleRequest() {
        System.out.println("Request was scheduled");
    }

    @Override
    public void dispatchRequest() {
        System.out.println("Request dispatched");
        request.setRequestState(new RequestFinishedTrackingState(request));
    }

    private String getUsername() {
        return MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(request.getMemberId()).getUsername();
    }

    @Override
    public void rejectRequest() {

    }

    @Override
    public void cancelRequest() {

    }

    @Override
    public void completeRequest() {

    }
}
