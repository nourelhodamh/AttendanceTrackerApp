package com.example.attendancetracker.activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.attendancetracker.ClickHandler;
import com.example.attendancetracker.NetworkConnection;
import com.example.attendancetracker.R;
import com.example.attendancetracker.databinding.ActivityMainBinding;
import com.example.attendancetracker.models.Model;
import com.example.attendancetracker.reciever.ConnectionCallback;
import com.example.attendancetracker.reciever.NetworkChangeReceiver;
import com.example.attendancetracker.scheduler.MJobScheduler;
import com.example.attendancetracker.scheduler.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements ConnectionCallback {


    private int mConnectedColor;
    private int mDisconnectedColor;

    private Resources mRes;

    private NetworkChangeReceiver receiver; // The BroadcastReceiver that tracks network connectivity changes.

    private NetworkConnection networkConnection;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private static final int JOB_ID = 101;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;

    private int mFlag = 2;

    private FirebaseAuth mAuth;


    private final String TAG = MainActivity.class.getSimpleName();

    ActivityMainBinding mBinding;

    private Model model;
    private ClickHandler clickHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        model = new Model();
        mBinding.setModel(model);

        mBinding.setClickHandler(new ClickHandler(this) {
            @Override
            public void onButtonClick(View view) {
                super.onButtonClick(view);
                mAuth.getInstance().signOut();
                stopJobService();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        resources();
        getDate();

        //Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        //Network
        networkConnection = new NetworkConnection();
//       int mAsyncFlag = networkConnection.networkStatus(this);

        //JobService
        startJobService();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Registers BroadcastReceiver to track network connection changes.
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver(this);
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            this.unregisterReceiver(receiver);

        }

    }


    private void resources() {
        mRes = getResources();
        mConnectedColor = mRes.getColor(R.color.colorGreen);
        mDisconnectedColor = mRes.getColor(R.color.colorRed);
    }

    private void getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sDfr = new SimpleDateFormat("yyyy. MM. dd ", Locale.getDefault());
        String strDate = sDfr.format(calendar.getTime());
        model.setDate(strDate);
//        mDate.setTextColor(Color.BLUE);
    }


    private void startJobService() {
        ComponentName serviceComponent = new ComponentName(this, MJobScheduler.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceComponent);

        builder.setPeriodic(600000000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);
        builder.setPersisted(true);

        jobInfo = builder.build();
        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
//        displayToast(MainActivity.this,"MainActivity-JobScheduler");
        Log.d("MainActivity", "onCreate: JobScheduler");
    }


    private void stopJobService() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(JOB_ID);
    }


    @Override
    public void updateUICallback(int wifiFlag) {
        //createUserData(uTime, uTime);
        switch (wifiFlag) {
            case 1:
                updateUI(1);
                break;
            case 0:
                updateUI(0);
                break;
            case 2:
                updateUI(2);
                break;
            default:
                Log.v("MainActivity", "\"Callback check: Default\"");
        }
        Log.d(TAG, "Callback:" + wifiFlag);

    }

    private void updateUI(int mFlag) {
        switch (mFlag) {
            case 1:
                model.setWifiStatus(getString(R.string.msg_connected));
//                model.setTextColor(mConnectedColor);
                model.setCheckedInLabel(getString(R.string.yes));
//                mCheckedIn.setTextColor(mConnectedColor);
                break;
            case 0:
                model.setWifiStatus(getString(R.string.msg_disconnected));
//                mWifiStatuses.setTextColor(mDisconnectedColor);
                model.setCheckedInLabel(getString(R.string.no));
//                mCheckedIn.setTextColor(mDisconnectedColor);
                model.setCheckedOutLabel(getString(R.string.yes));
//                mCheckedOut.setTextColor(mConnectedColor);
                break;
            case 2:
                model.setWifiStatus(getString(R.string.msg_wifi_closed));
//                mWifiStatuses.setTextColor(mDisconnectedColor);
                model.setCheckedInLabel(getString(R.string.no));
//                mCheckedIn.setTextColor(mDisconnectedColor);
                model.setCheckedOutLabel(getString(R.string.yes));
//                mCheckedOut.setTextColor(mConnectedColor);

                break;
            default:
        }
    }

    //AsyncTAsk Class
    public static class BackgroundTask extends AsyncTask<Integer, Long, Integer> {

        private FirebaseDatabase mFirebaseDatabase;
        private DatabaseReference mDatabaseReference;
        private Map mMap;
        String mUserId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("BackgroundTask", "PreExecute");
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference("Model");
            getTimeStamp();
            createUserData(getUserId(), mMap, mMap);
            return null;
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.v("BackgroundTask", "PostExecute");
        }


        private void createUserData(String userID, Map arrivalTime, Map departureTime) {
            UserData user = new UserData(userID, arrivalTime, departureTime);
            mDatabaseReference.child(userID).child("Arrival").setValue(arrivalTime);
            mDatabaseReference.child(userID).child("Departure");

        }

        private void getTimeStamp() {
            mMap = new HashMap();
            mMap.put("Time", ServerValue.TIMESTAMP);

        }

        private String getUserId() {
            if (TextUtils.isEmpty(mUserId)) {
                mUserId = mDatabaseReference.push().getKey();
            }
            return mUserId;
        }


    }

}
