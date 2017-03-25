package com.sjsu.request;

import com.sjsu.dao.InvalidInputException;
import com.sjsu.request.state.InvalidRequestTypeException;
import com.sjsu.request.state.RequestState;

import java.io.IOException;
import java.text.ParseException;

public interface IRequest {

    void acceptRequest() throws InvalidRequestTypeException, IOException, ParseException;

    void waitInQueue();

    void processRequest() throws InvalidInputException, IOException;

    void dispatchRequest();

    void rejectRequest();

    void cancelRequest();

    void completeRequest();

    void setRequestState(RequestState state);

    RequestState getRequestState();
}
