package com.sjsu.request;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sjsu.dao.InvalidInputException;
import com.sjsu.dao.MemberDetailsDAO;
import com.sjsu.member.Member;
import com.sjsu.model.MemberId;
import com.sjsu.notification.Notification;
import com.sjsu.notification.NotificationType;
import com.sjsu.request.state.InvalidRequestTypeException;
import com.sjsu.request.state.RequestInitiateState;
import com.sjsu.request.state.RequestState;
import com.sjsu.rules.Rule;
import com.sjsu.rules.RuleType;
import com.sjsu.scheduler.SchedulingAlgorithm;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class Request implements IRequest {

    private String requestId;
    private RequestType requestType;
    private Date creationTime;

    private MemberId memberId;

    private Map<RuleType, Rule> rules;

    private RequestState requestState;

    private SchedulingAlgorithm schedulingAlgorithm;

    private List<MemberId> memberNotificationObserver = Lists.newArrayList();


    public Request(SchedulingAlgorithm schedulingAlgorithm) {
        requestState = new RequestInitiateState(this);
        this.schedulingAlgorithm = schedulingAlgorithm;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    protected void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public MemberId getMemberId() {
        return memberId;
    }

    public void setMemberId(MemberId memberId) {
        this.memberId = memberId;
    }

    public void acceptRequest() throws InvalidRequestTypeException, ParseException, IOException {
        requestState.acceptRequest();
    }

    public void waitInQueue() {
        requestState.waitInQueue();
    }

    public void processRequest() throws InvalidInputException, IOException {
        requestState.scheduleRequest();
    }

    public void dispatchRequest() {
        requestState.dispatchRequest();

    }

    public void rejectRequest() {
        requestState.rejectRequest();
    }

    public void cancelRequest() {
        requestState.cancelRequest();
    }

    public void completeRequest() {
        requestState.completeRequest();
    }

    public abstract void applyRules();

    public Map<RuleType, Rule> getRules() {
        return rules;
    }

    public void setRules(Map<RuleType, Rule> rules) {
        this.rules = rules;
    }

    public RequestState getRequestState() {
        return requestState;
    }

    public void setRequestState(RequestState requestState) {
        this.requestState = requestState;
    }

    public SchedulingAlgorithm getSchedulingAlgorithm() {
        return schedulingAlgorithm;
    }

    public void setSchedulingAlgorithm(SchedulingAlgorithm schedulingAlgorithm) {
        this.schedulingAlgorithm = schedulingAlgorithm;
    }

    public RequestState getState() {
        return requestState;
    }

    public List<MemberId> getMemberNotificationObserver() {
        return memberNotificationObserver;
    }

    public void addMemberNotificationObserver(MemberId memberId) {
        memberNotificationObserver.add(memberId);
    }

    public void removeMemberNotificationObserver(MemberId memberId) {
        memberNotificationObserver.remove(memberId);
    }

    public void notifyMember(Notification notification) {
        Set<MemberId> memSet = Sets.newHashSet(memberNotificationObserver);
        memberNotificationObserver = Lists.newArrayList(memSet);
        MemberDetailsDAO memberDetailsDAO = MemberDetailsDAO.getMemberDetailsDAO();
        if (notification.getType() == NotificationType.SYSTEM) {
            memberDetailsDAO.getMemberDetailsById(memberId).update(this, notification);
        } else if (notification.getType() == NotificationType.MEMBER) {
            Member sendingNotification = memberDetailsDAO.getMemberDetailsById(memberId);
            System.out.println("Member is sending notification :=> " + sendingNotification.getUsername());
            if (notification.getSendToUsername().equals("ALL")) {
                for (MemberId memberId : memberNotificationObserver) {
                    Member sendNotificationTo = memberDetailsDAO.getMemberDetailsById(memberId);
                    sendNotificationTo.update(this, notification);
                }
            } else {
                for (MemberId memberId : memberNotificationObserver) {
                    Member memberObserver = memberDetailsDAO.getMemberDetailsById(memberId);
                    if (memberObserver.getUsername().equalsIgnoreCase(notification.getSendToUsername())) {
                        memberObserver.update(this, notification);
                    }
                }
            }

        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;

        Request request = (Request) o;

        if (!getRequestId().equals(request.getRequestId())) return false;
        if (getRequestType() != request.getRequestType()) return false;
        if (!getCreationTime().equals(request.getCreationTime())) return false;
        if (!getMemberId().equals(request.getMemberId())) return false;
        return schedulingAlgorithm.equals(request.schedulingAlgorithm);

    }

    @Override
    public int hashCode() {
        int result = getRequestId().hashCode();
        result = 31 * result + getRequestType().hashCode();
        result = 31 * result + getCreationTime().hashCode();
        result = 31 * result + getMemberId().hashCode();
        result = 31 * result + schedulingAlgorithm.hashCode();
        return result;
    }
}
