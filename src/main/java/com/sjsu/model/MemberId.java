package com.sjsu.model;


import java.io.Serializable;

public class MemberId implements Serializable{
    private String id;

    public MemberId() {
    }

    private MemberId(String id) {
        this.id = id;
    }

    public static MemberId newId(String id) {
        return new MemberId(id);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberId)) return false;

        MemberId memberId = (MemberId) o;

        return id.equals(memberId.id);

    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "MemberId{" +
                "id='" + id + '\'' +
                '}';
    }
}
