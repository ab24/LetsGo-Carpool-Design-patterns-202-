package com.sjsu.rules;

import com.sjsu.member.Member;


public class MemberRule extends Rule {

    Member member;

    public MemberRule(Member member) {
        this.member = member;
    }

    @Override
    public void setRule() {
        System.out.println("Setting Member Rule");
    }

    @Override
    public void modifyRule() {
        System.out.println("Modifying Member Rule");
    }

    @Override
    public void applyRule() {
        //Member Rule Logic
        System.out.print("\nApplying Rules :=> Membership Rules Applied!\n");
    }
}
