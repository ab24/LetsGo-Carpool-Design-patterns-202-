package com.sjsu.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;


public class Parking implements Serializable {

    private String parkingId;
    private String memberId;
    private String city;
    private Double costPerHour;
    private Integer numberOfParkingSpotAvailable;
    private DateTime startDate;
    private DateTime endDate;
    private DateTime startTime;
    private DateTime endTime;


    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }



    public Double getCostPerHour() {
        return costPerHour;
    }

    public void setCostPerHour(Double costPerHour) {
        this.costPerHour = costPerHour;
    }

    public Integer getNumberOfParkingSpotAvailable() {
        return numberOfParkingSpotAvailable;
    }

    public void setNumberOfParkingSpotAvailable(Integer numberOfParkingSpotAvailable) {
        this.numberOfParkingSpotAvailable = numberOfParkingSpotAvailable;
    }

    public DateTime getStartDate() {

        return startDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStartDate(DateTime startDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yy");
        this.startDate = dateTimeFormatter.parseDateTime(startDate.toString());
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yy");
        this.endDate = dateTimeFormatter.parseDateTime(endDate.toString());
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm");
        this.startTime = dateTimeFormatter.parseDateTime(startTime.toString());
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm");
        this.endTime = dateTimeFormatter.parseDateTime(endTime.toString());
    }


    public boolean isSpotAvailable() {
        return numberOfParkingSpotAvailable > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parking)) return false;

        Parking parking = (Parking) o;

        if (!getParkingId().equals(parking.getParkingId())) return false;
        if (!getMemberId().equals(parking.getMemberId())) return false;
        if (!city.equals(parking.city)) return false;
        if (!getCostPerHour().equals(parking.getCostPerHour())) return false;
        if (!getNumberOfParkingSpotAvailable().equals(parking.getNumberOfParkingSpotAvailable())) return false;
        if (!getStartDate().equals(parking.getStartDate())) return false;
        if (!getEndDate().equals(parking.getEndDate())) return false;
        if (!getStartTime().equals(parking.getStartTime())) return false;
        return getEndTime().equals(parking.getEndTime());

    }

    @Override
    public int hashCode() {
        int result = getParkingId().hashCode();
        result = 31 * result + getMemberId().hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + getCostPerHour().hashCode();
        result = 31 * result + getNumberOfParkingSpotAvailable().hashCode();
        result = 31 * result + getStartDate().hashCode();
        result = 31 * result + getEndDate().hashCode();
        result = 31 * result + getStartTime().hashCode();
        result = 31 * result + getEndTime().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Parking{" +
                "parkingId='" + parkingId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", city='" + city + '\'' +
                ", costPerHour=" + costPerHour +
                ", numberOfParkingSpotAvailable=" + numberOfParkingSpotAvailable +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
