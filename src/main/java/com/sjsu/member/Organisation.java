package com.sjsu.member;


public class Organisation extends Member {

    private String TaxNumber;

    public String getTaxNumber() {
        return TaxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        TaxNumber = taxNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organisation)) return false;
        if (!super.equals(o)) return false;

        Organisation organisation = (Organisation) o;
        return getTaxNumber().equals(organisation.getTaxNumber());

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getTaxNumber().hashCode();
        return result;
    }

}


