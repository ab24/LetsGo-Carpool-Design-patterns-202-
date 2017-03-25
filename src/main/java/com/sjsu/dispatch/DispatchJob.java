package com.sjsu.dispatch;


import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.request.Request;
import com.sjsu.request.state.RequestSchedulingState;
import org.joda.time.DateTime;

import java.util.Timer;
import java.util.TimerTask;


public class DispatchJob {

    private Request request;
    private Timer timer;

    public DispatchJob(Request request, DateTime dateTime) {
        this.request = request;
        this.timer = new Timer();
        timer.schedule(new DispatchTimerTask(), dateTime.toDate());
    }

    class DispatchTimerTask extends TimerTask {

        @Override
        public void run() {
            System.out.println("****Dispatching request****");

            System.out.println("Request :=> " + request.getRequestId());
            System.out.println("Request Type :=> " + request.getRequestType().name());
            System.out.println("Member Name :=> " + MemberDetailsDAO.getMemberDetailsDAO().getMemberDetailsById(request.getMemberId()).getUsername());
            request.setRequestState(new RequestSchedulingState(request));
            timer.cancel(); //Terminate the timer thread
        }
    }
}
