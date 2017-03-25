package com.sjsu.member;


import static com.sjsu.model.MembershipType.Platinum;

public class PlatinumMember extends MemberDecorator {
    private final double AMOUNT = 100.00;

    public PlatinumMember(Member member) {
        super(member);
        member.setMembershipType(Platinum);
    }

    public double calculatePercentageDiscount(double amount) {
        return amount * 0.3;
    }

    public void addBalanceToWallet() {
        member.getWallet().creditAmount(AMOUNT);
    }

}
