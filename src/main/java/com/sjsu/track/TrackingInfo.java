package com.sjsu.track;

import com.sjsu.request.Request;
import org.joda.time.DateTime;

import static com.sjsu.client.PrintUtil.printTime;

public abstract class TrackingInfo {

    private String trackingId;
    private DateTime startTime;
    private DateTime endTime;
    private Request request;
    private double cost;
    private TrackingStatus trackingStatus;

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public TrackingStatus getTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(TrackingStatus trackingStatus) {
        this.trackingStatus = trackingStatus;
    }

    @Override
    public String toString() {
        return "TrackingInfo{" +
                "trackingId='" + trackingId + '\'' +
                ", startTime=" + printTime(startTime) +
                ", endTime=" + printTime(endTime) +
                ", request=" + request.getRequestId() +
                ", tracking status=" + trackingStatus +
                ", cost=" + cost +
                '}';
    }
}
