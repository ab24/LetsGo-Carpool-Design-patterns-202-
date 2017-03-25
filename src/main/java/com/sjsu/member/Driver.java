package com.sjsu.member;


public class Driver extends Member {

    private Vehicle vehicle;
    private String driverLicenceNumber;

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getDriverLicenceNumber() {
        return driverLicenceNumber;
    }

    public void setDriverLicenceNumber(String driverLicenceNumber) {
        this.driverLicenceNumber = driverLicenceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Driver)) return false;
        if (!super.equals(o)) return false;

        Driver driver = (Driver) o;

        if (!getVehicle().equals(driver.getVehicle())) return false;
        return getDriverLicenceNumber().equals(driver.getDriverLicenceNumber());

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getVehicle().hashCode();
        result = 31 * result + getDriverLicenceNumber().hashCode();
        return result;
    }
}
