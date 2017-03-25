package com.sjsu.request.state;

import com.sjsu.request.Request;
import org.joda.time.DateTime;

import java.io.IOException;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;


public class RequestWaitingState implements RequestState {
    private Request request;

    public RequestWaitingState(Request request) {
        this.request = request;
    }

    @Override
    public void acceptRequest() throws InvalidRequestTypeException, IOException, ParseException {
        System.out.println("Request State :=> [" + request.getRequestId() + "] Request already accepted.");
    }

    @Override
    public void waitInQueue() {
        System.out.println("Request State :=> [" + request.getRequestId() + "] Request in waiting queue.");
        request.setRequestState(new RequestSchedulingState(request));
        //WaitingState waitingState = new WaitingState(request, Config.getRequestProcessingTime());
    }

    @Override
    public void scheduleRequest() {
        System.out.println("Request State :=> [" + request.getRequestId() + "] Request still in waiting state.");
    }

    @Override
    public void dispatchRequest() {

    }

    @Override
    public void rejectRequest() {
        System.out.println("Request State :=> [" + request.getRequestId() + "] Request still in waiting state.");
    }

    @Override
    public void cancelRequest() {
        System.out.println("Request State :=> [" + request.getRequestId() + "] Request still in waiting state.");
    }

    @Override
    public void completeRequest() {
        System.out.println("Request State :=> [" + request.getRequestId() + "] Request still in waiting state.");
    }

}


class WaitingState {
    private Timer timer;
    private Request request;

    public WaitingState(Request request, DateTime dateTime) {
        this.request = request;
        timer = new Timer();
        timer.schedule(new WaitingTask(), dateTime.toDate());
    }

    class WaitingTask extends TimerTask {
        public void run() {
            System.out.println("Time's up!");
            request.setRequestState(new RequestSchedulingState(request));
            timer.cancel(); //Terminate the timer thread
        }
    }
}
