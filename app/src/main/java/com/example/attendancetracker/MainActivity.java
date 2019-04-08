package com.example.attendancetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.drm.DrmStore;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView date;
    private TextView wifiStatus;
    private TextView checkedOut;
    private TextView checkedIn;
    private TextView leftAt;
    private Group group1;
    private Group group2;
    private String myNetworkName = "KabelBox-9C1C";
    private ConnectivityManager connManager;
    private NetworkInfo activeNetwork;
    String y;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private int connectedColor;
    private int disconnectedColor;


    String TAG;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiStatus = findViewById(R.id.text_wifi_status);
        checkedIn = findViewById(R.id.txt_checked_in);
        checkedOut = findViewById(R.id.txt_checked_out);
        leftAt = findViewById(R.id.txt_left_at);
        date = findViewById(R.id.text_date);
        btn = findViewById(R.id.btn);

        /*Network*/
        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        /*Accessing App resources*/
        Resources res = getResources();
        connectedColor = res.getColor(R.color.colorGreen);
        disconnectedColor = res.getColor(R.color.colorRed);

        /*Get The date*/
        getDate();

        /*Checking connectivity to a specific wifi*/
        if (activeNetwork.isConnected()) {
            String x = returnWifiName();
            y = x.replaceAll("^\"|\"$", "");
            Toast.makeText(getApplicationContext(), "wifi connected ", Toast.LENGTH_LONG).show();
            /*network name Toast */
            Toast.makeText(getApplicationContext(), "connected to" + x, Toast.LENGTH_LONG).show();
            if (y.equals(myNetworkName)) {
                connectedToNetwork();
            } else if (activeNetwork.isConnected() && !(y.equals(myNetworkName))) {
                disconnectedToNetwork();

                Toast.makeText(getApplicationContext(), "You are not connected to your workplace WI-FI",
                        Toast.LENGTH_LONG).show();
            }
        } else {

            //Toast.makeText(getApplicationContext(), "Please turn ON your WI-FI", Toast.LENGTH_LONG).show();
        }


        /*Connecting to FirebaseDatabase*/
        database = FirebaseDatabase.getInstance();

        //get reference to Arriving Node
        myRef = database.getReference("Arriving");
        myRef.child("Status").setValue("Yes");

        /*get reference to Departure Node*/
        myRef = database.getReference("Departure");
        myRef.child("Status").setValue("No");


        /*Saving timeStamp of arrival to FirebaseDatabase*/
        Map map = new HashMap();
        map.put("Time Stamp", ServerValue.TIMESTAMP);
        myRef.child("Time").updateChildren(map);


//button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // myRef.setValue("test");
            }
        });

    }

    /*Function Returning Wifi Name*/
    private String returnWifiName() {
        String ssid = "none";
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            ssid = wifiInfo.getSSID();
        }

        return ssid;
    }

    /*function returning "Date"*/
    private void getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sDfr = new SimpleDateFormat("yyyy. MM. dd ");
        String strDate = sDfr.format(calendar.getTime());
        date.setText(strDate);
        date.setTextColor(Color.BLUE);
    }

    private void connectedToNetwork() {
        wifiStatus.setText("You are connected");
        wifiStatus.setTextColor(connectedColor);
        checkedIn.setText("Yes");
        checkedIn.setTextColor(connectedColor);
    }

    private void disconnectedToNetwork() {
        wifiStatus.setText("You are  not connected");
        wifiStatus.setTextColor(disconnectedColor);
        checkedIn.setText("No");
        checkedIn.setTextColor(disconnectedColor);
        checkedOut.setText("yes");
        checkedOut.setTextColor(connectedColor);
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(nCReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(nCReceiver);

    }

    private BroadcastReceiver nCReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*Checking connectivity to a specific wifi*/
            if (activeNetwork.isConnected()) {
                String x = returnWifiName();
                y = x.replaceAll("^\"|\"$", "");

                if (y.equals(myNetworkName)) {
                    connectedToNetwork();
                } else if (activeNetwork.isConnected() && !(y.equals(myNetworkName))) {
                    disconnectedToNetwork();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please turn ON your WI-FI", Toast.LENGTH_LONG).show();
            }
        }
    };


}




