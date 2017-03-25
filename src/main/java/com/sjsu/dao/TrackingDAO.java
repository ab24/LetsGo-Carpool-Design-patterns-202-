package com.sjsu.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sjsu.model.MemberId;
import com.sjsu.request.Request;
import com.sjsu.request.RequestType;
import com.sjsu.track.TrackingInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TrackingDAO {
    private static TrackingDAO trackingDAO;

    private static Map<MemberId, List<TrackingInfo>> trackingInfoMap = Maps.newHashMap();

    public TrackingDAO() {
    }

    public static Map<MemberId, List<TrackingInfo>> getTrackingInfoMap() {
        return trackingInfoMap;
    }

    public static TrackingDAO getTrackerDAO() {
        if (trackingDAO == null) {
            trackingDAO = new TrackingDAO();
        }
        return trackingDAO;
    }

    public static List<TrackingInfo> getTrackingInfoForRequestType(RequestType requestType, MemberId memberId) {
        List<TrackingInfo> trackingInfoList = new ArrayList<>();
        List<TrackingInfo> trackingInfoForMember = trackingInfoMap.get(memberId);
        if (trackingInfoForMember != null) {
            for (TrackingInfo info : trackingInfoForMember) {
                if (info.getRequest().getRequestType().equals(requestType)) {
                    trackingInfoList.add(info);
                }
            }
            return trackingInfoList;
        } else {
            return null;
        }

    }

    public void saveTrackingInfo(MemberId memberId, TrackingInfo trackingInfo) {
        List<TrackingInfo> trackingInfoList = trackingInfoMap.get(memberId);
        if (trackingInfoList == null) {
            trackingInfoList = Lists.newArrayList();
        }
        trackingInfoList.add(trackingInfo);
        trackingInfoMap.put(memberId, trackingInfoList);
    }

    public void updateTrackingInfo(MemberId memberId, TrackingInfo updatedInfo) {
        List<TrackingInfo> trackingInfoList = trackingInfoMap.get(memberId);
        int i = 0;
        for (TrackingInfo info : trackingInfoList) {
            if (info.getRequest().getRequestId().equals(updatedInfo.getRequest().getRequestId())) {
                trackingInfoList.add(i, updatedInfo);
                System.out.println("Tracking info updated successfully.");
                break;
            }
            i = i + 1;
        }
    }

    public TrackingInfo getTrackingInfo(Request request) {

        MemberId memberId = request.getMemberId();

        List<TrackingInfo> trackingInfosForMember = trackingInfoMap.get(memberId);
        if (trackingInfosForMember != null) {
            for (TrackingInfo info : trackingInfosForMember) {
                if (info.getRequest().getRequestId().equals(request.getRequestId())) {
                    return info;
                }
            }
        }
        return null;
    }


}
