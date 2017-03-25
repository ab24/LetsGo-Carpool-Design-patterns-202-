package com.sjsu.rules;


public class NotificationRule extends Rule {
    @Override
    public void setRule() {
        System.out.println("Setting Notification Rule");
    }

    @Override
    public void modifyRule() {
        System.out.println("Modifying Notification Rule");
    }

    @Override
    public void applyRule() {
        System.out.print("\nApplying Rules :=> Notification Rules Applied!\n");
    }
}
