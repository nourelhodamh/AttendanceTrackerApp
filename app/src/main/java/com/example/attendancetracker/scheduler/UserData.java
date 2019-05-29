package com.example.attendancetracker.scheduler;

import java.util.Map;

public class UserData {

    private Map mArrivalTime;
    private Map mDepartureTime;
    private String mUserId;
//    private String mName;

    public UserData() {
    }

    public UserData(String userId, Map arrivalTime, Map departureTime) {

        mUserId = userId;
        mArrivalTime = arrivalTime;
        mDepartureTime = departureTime;
//        mName=name;
    }

//    public String getName() {
//        return mName;
//    }
//
//    public void setName(String mName) {
//        this.mName = mName;
//    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUser) {
        this.mUserId = mUser;
    }

    public Map getArrivalTime() {
        return mArrivalTime;
    }

    public void setArrivalTime(Map mArrivalTime) {
        this.mArrivalTime = mArrivalTime;
    }

    public Map getDepartureTime() {
        return mDepartureTime;
    }

    public void setDepartureTime(Map mDepartureTime) {
        this.mDepartureTime = mDepartureTime;
    }
}
