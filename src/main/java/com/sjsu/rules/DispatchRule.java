package com.sjsu.rules;


public class DispatchRule extends Rule {
    @Override
    public void setRule() {
        System.out.println("Setting Dispatch Rule");
    }

    @Override
    public void modifyRule() {
        System.out.println("Modify Dispatch Rule");
    }

    @Override
    public void applyRule() {
        System.out.print("\nApplying Rules :=> Dispatch Rules Applied!\n");

    }
}
