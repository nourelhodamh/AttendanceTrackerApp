package com.example.attendancetracker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.attendancetracker.R;
import com.example.attendancetracker.activities.MainActivity;
import com.example.attendancetracker.reciever.NetworkChangeReceiver;
import com.example.attendancetracker.scheduler.UserData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.text.DateFormat.getDateTimeInstance;

public class AsyncActivity extends AppCompatActivity {
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mUserId = bundle.getString("UserId");

//        BackgroundTask backgroundTask= new BackgroundTask(this);
//        backgroundTask.execute();

    }


    public static class BackgroundTask extends AsyncTask<Integer, Long, Integer> {
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

        private WeakReference<MainActivity> mainActivity;

        public BackgroundTask(MainActivity mainActivity) {
            this.mainActivity = new WeakReference<>(mainActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



            Log.v("BackgroundTask", "PreExecute");
            //Toast.makeText(mContext, "PreExecute", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference("User");
            getTimeStamp();
            createUserData(getUserId(), mMap, mMap);
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
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.v("BackgroundTask", "PostExecute");
            //Toast.makeText(mContext, "PostExecute", Toast.LENGTH_LONG).show();
            // createUserData(map, map);
        }

        private void getTimeStamp() {
            mMap = new HashMap();
            mMap.put("Time", ServerValue.TIMESTAMP);

        }

        public String getTimeDate(long timestamp) {
            try {
                DateFormat dateFormat = getDateTimeInstance();
                Date netDate = (new Date(timestamp));
                return dateFormat.format(netDate);
            } catch (Exception e) {
                return "date";
            }
        }

        private String getUserId() {
            if (TextUtils.isEmpty(userId)) {
                userId = mDatabaseReference.push().getKey();
            }
            return userId;
        }

        private void createUserData(String userID, Map arrivalTime, Map departureTime) {
            UserData user = new UserData(userID, arrivalTime, departureTime);
            mDatabaseReference.child(userID).child("Arrival").setValue(arrivalTime);
            mDatabaseReference.child(userID).child("Departure").setValue(departureTime);

        }


    }

}
