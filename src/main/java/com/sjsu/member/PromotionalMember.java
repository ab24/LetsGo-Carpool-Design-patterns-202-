package com.sjsu.member;


public class PromotionalMember extends MemberDecorator {

    public PromotionalMember(Member member) {
        super(member);
    }

    public double applyPromotionalDiscount(double amount) {
        return amount * 0.8;
    }
}
