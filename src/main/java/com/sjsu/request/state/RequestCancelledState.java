package com.sjsu.request.state;

import com.sjsu.request.IRequest;
import com.sjsu.request.Request;

import java.io.IOException;
import java.text.ParseException;

public class RequestCancelledState implements RequestState {
    private Request request;

    public RequestCancelledState(Request request) {
        this.request = request;
    }

    @Override
    public void acceptRequest() throws InvalidRequestTypeException, IOException, ParseException {

    }

    @Override
    public void waitInQueue() {

    }

    @Override
    public void scheduleRequest() {

    }

    @Override
    public void dispatchRequest() {

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