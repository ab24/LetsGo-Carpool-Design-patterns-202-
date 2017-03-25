package com.sjsu.request.state;

import com.sjsu.dao.RequestDAO;
import com.sjsu.datastore.Session;
import com.sjsu.member.Member;
import com.sjsu.model.InsufficientBalanceException;
import com.sjsu.request.Request;

import java.io.IOException;
import java.text.ParseException;


public class RequestInitiateState implements RequestState {
    private Request request;

    public RequestInitiateState(Request request) {
        this.request = request;
    }

    public void acceptRequest() throws InvalidRequestTypeException, IOException, ParseException {
        Member currentUser = Session.getCurrentUser();
        try {
            currentUser.getWallet().debitAmount(50.00);
            RequestDAO.getRequestDAO().save(request);
            currentUser.setNumberOfServicesUsed(currentUser.getNumberOfServicesUsed()+1);
            request.setRequestState(new RequestWaitingState(request));
            System.out.println("Request State :=> [" + request.getRequestId() + "] Request accepted and added to the queue.");
        } catch (InsufficientBalanceException e) {
            System.out.println("Error occurred while processing your request. Your request will not be processed.");
            System.out.println("Error message :" + e.getMessage());
        }

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

    public void rejectRequest() {

    }

    public void cancelRequest() {

    }

    public void completeRequest() {

    }

}
