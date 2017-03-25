package com.sjsu.request.state;

import com.sjsu.request.Request;

import java.io.IOException;
import java.text.ParseException;

/**
 * on 8/8/16.
 */

public class RequestSchedulingState implements RequestState {
    private Request request;

    public RequestSchedulingState(Request request) {
        this.request = request;
    }

    @Override
    public void acceptRequest() throws InvalidRequestTypeException, IOException, ParseException {
        System.out.println("Request already accepted.");
    }

    @Override
    public void waitInQueue() {
        System.out.println("Request fetched from the waiting state and is currently being processed.");
    }

    @Override
    public void scheduleRequest() {
        System.out.println("Scheduling request");
        request.setRequestState(new RequestDispatchingState(request));
    }

    @Override
    public void dispatchRequest() {
        System.out.println("Request is not yet scheduled");

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
