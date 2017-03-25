package com.sjsu.request;

import com.sjsu.scheduler.SchedulingAlgorithm;

public abstract class RequestDecorator extends Request {
    protected Request request;

    public RequestDecorator(SchedulingAlgorithm schedulingAlgorithm) {
        super(schedulingAlgorithm);
    }


}
