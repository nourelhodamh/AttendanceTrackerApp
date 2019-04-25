package com.example.attendancetracker.activities;

import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendancetracker.BackgroundTask;
import com.example.attendancetracker.NetworkConnection;
import com.example.attendancetracker.R;
import com.example.attendancetracker.UserData;
import com.example.attendancetracker.reciever.ConnectionCallback;
import com.example.attendancetracker.reciever.NetworkChangeReceiver;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ConnectionCallback {

    private TextView mDate;
    private TextView mWifiStatuses;
    private TextView mCheckedOut;
    private TextView mCheckedIn;
    private TextView mLeftAt;

    private int mConnectedColor;
    private int mDisconnectedColor;

    private int mFlag = 2;
    int mAsyncFlag;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private NetworkConnection networkConnection;

    private Resources mRes;

    private NetworkChangeReceiver receiver; // The BroadcastReceiver that tracks network connectivity changes.


    public Map map;

    public String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//TextViews
        mWifiStatuses = findViewById(R.id.text_wifi_status);
        mCheckedIn = findViewById(R.id.txt_checked_in);
        mCheckedOut = findViewById(R.id.txt_checked_out);
        mLeftAt = findViewById(R.id.txt_left_at);
        mDate = findViewById(R.id.text_date);

        //a reference to your firebase node
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();


//get date
        getDate();
//Resources
        resources();

//        updateUI(mFlag);
        networkConnection= new NetworkConnection();
        mAsyncFlag= networkConnection.networkStatus(this);
//AsyncTAsk
        BackgroundTask backgroundTask= new BackgroundTask(this);
        backgroundTask.execute(mAsyncFlag);


    }



    private void getTimeStamp() {
        map = new HashMap();
        map.put("Time", ServerValue.TIMESTAMP);
    }

    private String getUserId(){
        if (TextUtils.isEmpty(userId)) {
            userId = mDatabaseReference.push().getKey();
        }
        return userId;
    }

    private void createUserData(Map arrivalTime, Map departureTime) {
        UserData userTimeData = new UserData(arrivalTime, departureTime);
        mDatabaseReference.child(getUserId()).setValue(userTimeData);
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
        SimpleDateFormat sDfr = new SimpleDateFormat("yyyy. MM. dd ");
        String strDate = sDfr.format(calendar.getTime());
        mDate.setText(strDate);
        mDate.setTextColor(Color.BLUE);
    }


    @Override
    public void updateUICallback(int works, Map uTime) {
        createUserData(uTime, uTime);

        switch (works) {
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
                Toast.makeText(getApplicationContext(), "Callback check: Default", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(getApplicationContext(), "Callback:" + works, Toast.LENGTH_LONG).show();

    }

    private void updateUI(int mFlag) {
        switch (mFlag) {
            case 1:
                mWifiStatuses.setText("Connected to Workplace WI-FI");
                mWifiStatuses.setTextColor(mConnectedColor);
                mCheckedIn.setText("Yes");
                mCheckedIn.setTextColor(mConnectedColor);
                break;
            case 0:
                mWifiStatuses.setText("Disconnected of Workplace WI-FI");
                mWifiStatuses.setTextColor(mDisconnectedColor);
                mCheckedIn.setText("No");
                mCheckedIn.setTextColor(mDisconnectedColor);
                mCheckedOut.setText("yes");
                mCheckedOut.setTextColor(mConnectedColor);
                break;
            case 2:
                mWifiStatuses.setText("Lost Connection-Turn on WI-FI");
                mWifiStatuses.setTextColor(mDisconnectedColor);
                mCheckedIn.setText("No");
                mCheckedIn.setTextColor(mDisconnectedColor);
                mCheckedOut.setText("yes");
                mCheckedOut.setTextColor(mConnectedColor);

                break;

            default:

        }
    }

    /*** User data change listener**/
//    private void dataChangeListener() {
//
//        mDatabaseReference.child(userId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                UserData userData = dataSnapshot.getValue(UserData.class);
//                // Display newly updated name and button status
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(), "Add Error", Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }

}
