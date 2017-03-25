package com.sjsu.model;


public class Address {
    private String apptNumber;
    private String streetName;
    private String city;
    private String state;
    private String country;
    private String postalcode;

    public String getApptNumber() {
        return apptNumber;
    }

    public void setApptNumber(String apptNumber) {
        this.apptNumber = apptNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        if (!getApptNumber().equals(address.getApptNumber())) return false;
        if (!getStreetName().equals(address.getStreetName())) return false;
        if (!getCity().equals(address.getCity())) return false;
        if (!getState().equals(address.getState())) return false;
        if (!getCountry().equals(address.getCountry())) return false;
        return getPostalcode().equals(address.getPostalcode());

    }

    @Override
    public int hashCode() {
        int result = getApptNumber().hashCode();
        result = 31 * result + getStreetName().hashCode();
        result = 31 * result + getCity().hashCode();
        result = 31 * result + getState().hashCode();
        result = 31 * result + getCountry().hashCode();
        result = 31 * result + getPostalcode().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Address{" +
                "apptNumber='" + apptNumber + '\'' +
                ", streetName='" + streetName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", postalcode='" + postalcode + '\'' +
                '}';
    }
}
