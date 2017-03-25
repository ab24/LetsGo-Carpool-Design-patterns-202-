package com.sjsu.dao;

import com.sjsu.datastore.DataStore;
import com.sjsu.model.MemberId;
import com.sjsu.request.Request;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class RequestDAO {
    private static RequestDAO requestDAO;

    private RequestDAO() {
    }

    public static RequestDAO getRequestDAO() {
        if (requestDAO == null) {
            requestDAO = new RequestDAO();
        }
        return requestDAO;
    }

    public void save(Request request) {
        Map<MemberId, List<Request>> memberToRequestMap = DataStore.getMemberToRequestMap();
        List<Request> requests = memberToRequestMap.get(request.getMemberId());
        if (requests == null) {
            requests = newArrayList();
        }
        requests.add(request);
        memberToRequestMap.put(request.getMemberId(), requests);
    }
}
