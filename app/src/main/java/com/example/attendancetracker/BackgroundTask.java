package com.example.attendancetracker;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.attendancetracker.reciever.ConnectionCallback;
import com.example.attendancetracker.reciever.NetworkChangeReceiver;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class BackgroundTask extends AsyncTask<Integer, Long, Integer> {
    NetworkChangeReceiver networkReceiver;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private Map mMap;
    String userId;
    private Context mContext;
    private int mflag;
    int i = 0;

    public BackgroundTask(Context context) {
        mContext = context;
    }


    @Override
    protected Integer doInBackground(Integer... integers) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Async");
        //mDatabaseReference.setValue("BackGroundTask");
        getTimeStamp();
        createUserData(mMap, mMap);
//        mflag = integers[i];
//        if (mflag == 1) {
//            mFirebaseDatabase = FirebaseDatabase.getInstance();
//            mDatabaseReference = mFirebaseDatabase.getReference();
//            getTimeStamp();
//        } else if (mflag == 0) {
//            //createUserData(map, map);
//        } else if (mflag == 2) {
//
//        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(mContext, "PreExecute", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        Toast.makeText(mContext, "PostExecute", Toast.LENGTH_LONG).show();
        // createUserData(map, map);
    }

    private void getTimeStamp() {
        mMap = new HashMap();
        mMap.put("Time", ServerValue.TIMESTAMP);
    }

    private String getUserId() {
        if (TextUtils.isEmpty(userId)) {
            userId = mDatabaseReference.push().getKey();
        }
        return userId;
    }

    private void createUserData(Map arrivalTime, Map departureTime) {
        UserData userTimeData = new UserData(arrivalTime, departureTime);
        mDatabaseReference.child(getUserId()).setValue(userTimeData);
    }


}
