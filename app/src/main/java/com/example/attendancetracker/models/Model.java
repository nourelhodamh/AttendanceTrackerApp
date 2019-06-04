package com.example.attendancetracker.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.attendancetracker.BR;

public class Model extends BaseObservable {
    private String mUserName;
    private String mArrived;
    private String mLeft;
    private String mDate;
    private String mTotalWorkingHours;
    private String mWifiStatus;
    private String mCheckedInLabel;
    private String mCheckedOutLabel;


    public Model(String mUserName, String mArrived, String mLeft, String mDate, String mTotalWorkingHours, String mWifiStatus, String mCheckedInLabel, String mCheckedOutLabel) {
        this.mUserName = mUserName;
        this.mArrived = mArrived;
        this.mLeft = mLeft;
        this.mDate = mDate;
        this.mTotalWorkingHours = mTotalWorkingHours;
        this.mWifiStatus = mWifiStatus;
        this.mCheckedInLabel = mCheckedInLabel;
        this.mCheckedOutLabel = mCheckedOutLabel;
    }

    public Model() {
    }

    @Bindable
    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
        notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public String getArrived() {
        return mArrived;
    }

    public void setArrived(String mArrived) {
        this.mArrived = mArrived;
        notifyPropertyChanged(BR.arrived);
    }

    @Bindable
    public String getLeft() {
        return mLeft;
    }

    public void setLeft(String mLeft) {
        this.mLeft = mLeft;
        notifyPropertyChanged(BR.left);
    }

    @Bindable
    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
        notifyPropertyChanged(BR.date);
    }

    @Bindable
    public String getTotalWorkingHours() {
        return mTotalWorkingHours;
    }

    public void setTotalWorkingHours(String mTotalWorkingHours) {
        this.mTotalWorkingHours = mTotalWorkingHours;
        notifyPropertyChanged(BR.totalWorkingHours);
    }

    @Bindable
    public String getWifiStatus() {
        return mWifiStatus;
    }

    public void setWifiStatus(String mWifiStatus) {
        this.mWifiStatus = mWifiStatus;
        notifyPropertyChanged(BR.wifiStatus);
    }

    @Bindable
    public String getCheckedInLabel() {
        return mCheckedInLabel;
    }

    public void setCheckedInLabel(String mCheckedInLabel) {
        this.mCheckedInLabel = mCheckedInLabel;
        notifyPropertyChanged(BR.checkedInLabel);
    }

    @Bindable
    public String getCheckedOutLabel() {
        return mCheckedOutLabel;
    }

    public void setCheckedOutLabel(String mCheckedOutLabel) {
        this.mCheckedOutLabel = mCheckedOutLabel;
        notifyPropertyChanged(BR.checkedOutLabel);
    }
}