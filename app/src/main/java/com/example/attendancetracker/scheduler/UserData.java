package com.example.attendancetracker.scheduler;

import java.util.Map;

public class UserData {

    private Map mArrivalTime;
    private Map mDepartureTime;
    private String mUserId;

    public UserData() {
    }

    public UserData(String userId, Map arrivalTime, Map departureTime) {

        mUserId = userId;
        mArrivalTime = arrivalTime;
        mDepartureTime = departureTime;
    }
    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUser) {
        this.mUserId = mUser;
    }

    public Map getmArrivalTime() {
        return mArrivalTime;
    }

    public void setmArrivalTime(Map mArrivalTime) {
        this.mArrivalTime = mArrivalTime;
    }

    public Map getmDepartureTime() {
        return mDepartureTime;
    }

    public void setmDepartureTime(Map mDepartureTime) {
        this.mDepartureTime = mDepartureTime;
    }
}
