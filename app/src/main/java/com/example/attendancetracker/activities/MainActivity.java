package com.example.attendancetracker.activities;

import android.content.IntentFilter;
import android.content.res.Resources;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.widget.TextView;
import android.widget.Toast;

import com.example.attendancetracker.NetworkConnection;
import com.example.attendancetracker.R;
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

    FirebaseDatabase database;
    DatabaseReference myRef;


    // The BroadcastReceiver that tracks network connectivity changes.
    private NetworkChangeReceiver receiver = new NetworkChangeReceiver(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //views
        mWifiStatuses = findViewById(R.id.text_wifi_status);
        mCheckedIn = findViewById(R.id.txt_checked_in);
        mCheckedOut = findViewById(R.id.txt_checked_out);
        mLeftAt = findViewById(R.id.txt_left_at);
        mDate = findViewById(R.id.text_date);


// custom colors
        Resources res = getResources();
        mConnectedColor = res.getColor(R.color.colorGreen);
        mDisconnectedColor = res.getColor(R.color.colorRed);

//network
        NetworkConnection networkConnection = new NetworkConnection();
        mFlag = networkConnection.networkStatus(this);
        updateUI(mFlag);

//get date
        getDate();

//Connecting to FirebaseDatabase
        database = FirebaseDatabase.getInstance();

//get reference to Arriving Node
        myRef = database.getReference("Arriving");
        myRef.child("Status").setValue("Yes");

//get reference to Departure Node
        myRef = database.getReference("Departure");
        myRef.child("Status").setValue("No");

//Saving timeStamp of arrival to FirebaseDatabase
        Map map = new HashMap();
        map.put("Time Stamp", ServerValue.TIMESTAMP);
        myRef.child("Time of Arrival").updateChildren(map);


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

    private void getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sDfr = new SimpleDateFormat("yyyy. MM. dd ");
        String strDate = sDfr.format(calendar.getTime());
        mDate.setText(strDate);
        mDate.setTextColor(Color.BLUE);
    }

    @Override
    public void isWorking(int works) {
        switch (works) {
            case 1:
                updateUI(1);
                break;
            case 0:
                updateUI(0);
                break;
            case 2:
                updateUI(2);
            default:
                Toast.makeText(getApplicationContext(), "ON Resume: Default", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(getApplicationContext(), "CallBAck" + works, Toast.LENGTH_LONG).show();

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


   /* public void networkStatus() {
        connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        activeInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (activeInfo != null && activeInfo.isConnected()) {

            mWifiName = returnWifiName().replaceAll("^\"|\"$", "");

            Toast.makeText(getApplicationContext(), "wifi connected ", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "connected to" + mWifiName, Toast.LENGTH_LONG).show();

            if (mWifiName.equals(mWorkNetwork)) {
                mFlag = 1;
                updateUI(mFlag);

            } else if (activeInfo.isConnected() && !(mWifiName.equals(mWorkNetwork))) {
                mFlag = 0;
                updateUI(mFlag);
                Toast.makeText(getApplicationContext(), "You are not connected to your workplace WI-FI",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            mFlag = 2;
            updateUI(mFlag);
            Toast.makeText(getApplicationContext(), "Lost Connection-Open Your Wifi",
                    Toast.LENGTH_LONG).show();
        }
    }

    private String returnWifiName() {
        String ssid = "none";
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            ssid = wifiInfo.getSSID();
        }
        return ssid;
    }



    private void receiverUpdateUI() {
        if (receiver.isWifiConnected() == 1) {
            updateUI(1);
        } else if (receiver.isWifiConnected() == 0) {
            updateUI(0);
        } else if (receiver.isWifiConnected() == 2) {
            updateUI(2);
        }
    }*/
}



