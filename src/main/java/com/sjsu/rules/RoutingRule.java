package com.sjsu.rules;


public class RoutingRule extends Rule {
    @Override
    public void setRule() {
        System.out.println("Setting Routing Rule");
    }

    @Override
    public void modifyRule() {
        System.out.println("Modify Routing Rule");
    }

    @Override
    public void applyRule() {
        System.out.print("\nApplying Rules :=> Routing Rules Applied\n");
    }
}
