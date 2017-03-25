package com.sjsu.dao;

import com.google.common.collect.Maps;
import com.sjsu.datastore.DummyData;
import com.sjsu.member.Member;
import com.sjsu.model.MemberId;
import com.sjsu.util.IdGenerator;

import java.util.Map;

import static com.sjsu.model.MembershipStatus.INACTIVE;


public class MemberDetailsDAO {

    private static MemberDetailsDAO memberDetailsDAO;

    private MemberDetailsDAO() {
    }

    public static MemberDetailsDAO getMemberDetailsDAO() {
        if (memberDetailsDAO == null) {
            memberDetailsDAO = new MemberDetailsDAO();
        }
        return memberDetailsDAO;
    }


    public static Member addMember(Member member) {
        member.setMemberId(MemberId.newId(IdGenerator.generateId("M")));
        DummyData.getMemberDetails().put(member.getMemberId(), member);
        return member;
    }

    public Member getMemberDetailsById(MemberId memberId) {
        return DummyData.getMemberDetails().get(memberId);
    }

    public Member getMemberDetailsByUsername(String username) throws MemberNotFoundException {
        Map<MemberId, Member> longMemberMap = Maps.filterValues(DummyData.getMemberDetails(), member -> {
            return username.equals(member.getUsername());
        });
        Member toReturn;

        if (longMemberMap.values().iterator().hasNext()) {
            toReturn = longMemberMap.values().iterator().next();
        } else {
            throw new MemberNotFoundException("Unable to find member for given username:" + username);
        }

        return toReturn;
    }


    public void updateMemberDetails(MemberId id, Member member) {
        DummyData.getMemberDetails().put(id, member);
    }

    public void suspendMembership(MemberId id) {
        Member member = DummyData.getMemberDetails().get(id);
        member.setMembershipStatus(INACTIVE);
    }


}
