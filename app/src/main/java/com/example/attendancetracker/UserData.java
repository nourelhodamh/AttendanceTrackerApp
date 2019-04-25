package com.example.attendancetracker;

import java.util.Map;

public class UserData {

    private Map mArrivalTime;
    private Map mDepartureTime;

    public UserData() {}

    public UserData( Map arrivalTime, Map departureTime) {

        mArrivalTime = arrivalTime;
        mDepartureTime = departureTime;
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
