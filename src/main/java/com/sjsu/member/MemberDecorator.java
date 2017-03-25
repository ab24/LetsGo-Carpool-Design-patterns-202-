package com.sjsu.member;

import com.sjsu.model.*;
import com.sjsu.notification.Notification;
import com.sjsu.ratingsandreviews.RatingsAndReview;

import java.util.Date;
import java.util.List;

public abstract class MemberDecorator extends Member{

    protected Member member;

    public MemberDecorator(Member member) {
        this.member = member;
    }
}
