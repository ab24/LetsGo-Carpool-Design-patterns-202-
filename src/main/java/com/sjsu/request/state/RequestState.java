package com.sjsu.request.state;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;


public interface RequestState extends Serializable{
    void acceptRequest() throws InvalidRequestTypeException, IOException, ParseException;

    void waitInQueue();

    void scheduleRequest();

    void dispatchRequest();

    void rejectRequest();

    void cancelRequest();

    void completeRequest();

}
