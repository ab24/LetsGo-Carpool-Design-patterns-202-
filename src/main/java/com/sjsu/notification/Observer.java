package com.sjsu.notification;

import com.sjsu.request.Request;

/**
 * on 8/11/16.
 */
public abstract class Observer {
    public abstract void update(Request request, Notification notification);
}
