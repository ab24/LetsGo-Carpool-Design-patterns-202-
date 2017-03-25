package com.sjsu.request;

import com.sjsu.rules.RequestRule;
import com.sjsu.scheduler.SchedulingAlgorithm;

import java.io.IOException;
import java.text.ParseException;

/**
 * on 8/6/16.
 * Modified by abhasin on 8/14/16
 */
public class BiddingRequest extends RequestDecorator {
    public BiddingRequest(SchedulingAlgorithm schedulingAlgorithm) {
        super(schedulingAlgorithm);
    }

    @Override
    public void acceptRequest() throws IOException, ParseException {

    }

    @Override
    public void waitInQueue() {

    }

    @Override
    public void processRequest() {

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

    @Override
    public void applyRules() {

        RequestRule rule = new RequestRule(this);
        rule.applyRule();

    }
}
