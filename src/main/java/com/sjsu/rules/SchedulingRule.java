package com.sjsu.rules;


public class SchedulingRule extends Rule {
    @Override
    public void setRule() {
        System.out.println("Setting Scheduling Rule");
    }

    @Override
    public void modifyRule() {
        System.out.println("Modify scheduling Rule");
    }

    @Override
    public void applyRule() {

        System.out.print("\nApplying Rules :=> Scheduling Rules Applied!\n");
    }
}
