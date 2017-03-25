package com.sjsu.scheduler;

import com.sjsu.dao.InvalidInputException;
import com.sjsu.routing.RouteManager;

import java.io.IOException;
import java.io.Serializable;


public abstract class SchedulingAlgorithm implements Serializable{
    private RouteManager routeManager;

    protected SchedulingAlgorithm(RouteManager routeManager) {
        this.routeManager = routeManager;
    }

    public abstract boolean scheduleCarpoolRequest();

    public abstract boolean scheduleParkingRequest() throws InvalidInputException, IOException;
}
