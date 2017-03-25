package com.sjsu.rules;

import com.sjsu.request.Request;


public class RequestRule extends Rule {

    Request request;

    public RequestRule(Request request) {
        this.request = request;
    }

    @Override
    public void setRule() {
        System.out.println("Setting Request Rule Rule");
    }

    @Override
    public void modifyRule() {
        System.out.println("Modify Request Rule");
    }

    @Override
    public void applyRule() {
        System.out.print("\nApplying Rules :=> Request Rules Applied\n");
    }
}
