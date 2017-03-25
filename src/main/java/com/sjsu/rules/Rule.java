package com.sjsu.rules;


public abstract class Rule {

    private int ruleId;

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public void createRule() {
        setRule();
        if (isModificationRequired()) {//hook operation
            modifyRule();
        }
        approveRule();
        publishRule();
    }

    private boolean isModificationRequired() {
        return false;
    }

    public abstract void setRule();

    public abstract void modifyRule();

    public abstract void applyRule();

    public void approveRule() {
        System.out.println("Rule has been approved.");
    }

    public void publishRule() {
        System.out.println("Rule with id:" + ruleId + " has been published");
    }
}
