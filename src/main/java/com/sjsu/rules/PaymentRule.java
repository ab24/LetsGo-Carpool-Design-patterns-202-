package com.sjsu.rules;


public class PaymentRule extends Rule {
    @Override
    public void setRule() {
        System.out.println("Setting Payment Rule");
    }

    @Override
    public void modifyRule() {
        System.out.println("Modify Payment Rule");
    }

    @Override
    public void applyRule() {
        System.out.print("\nApplying Rules :=> Payment Rules Applied\n");
    }
}
