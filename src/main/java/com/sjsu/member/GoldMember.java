package com.sjsu.member;


public class GoldMember extends Member {


//    private double percentageDiscount;

    //Apply goldMember discount for rides
    public GoldMember() {
        super();
        //setMemberType(MemberType.GOLD);
    }

    public double calculatePercentageDiscount(double amount) {
        return amount * 0.5;
    }

}
