package com.example.attendancetracker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView date;
    private TextView wifiStatus;
    private TextView checkedOut;
    private TextView checkedIn;
    Group group1;
    Group group2;
    String myNetworkName = "KabelBox-9C1C";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiStatus = findViewById(R.id.text_wifi_status);
        date = findViewById(R.id.text_date);

        /*Accessing App resources*/
        Resources res = getResources();


        /*Calling Date function*/
        getDate();

        /*Check connectivity*/
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        /*Checking connectivity to a specific wifi*/
        if (mWifi.isConnected()) {
            String x = returnWifiName();
            String y = x.replaceAll("^\"|\"$", "");
            //Toast.makeText(getApplicationContext(), "wifi connected ", Toast.LENGTH_LONG).show();
            /*network name Toast */
            //Toast.makeText(getApplicationContext(), "connected to" + x, Toast.LENGTH_LONG).show();
            if (y.equals(myNetworkName)) {
                wifiStatus.setText("You are connected");
                int connectedColor = res.getColor(R.color.colorGreen);
                wifiStatus.setTextColor(connectedColor);
            } else if (mWifi.isConnected() && !(y.equals(myNetworkName))) {
                wifiStatus.setText("You are  not connected");
                int disconnectedColor = res.getColor(R.color.colorRed);
                wifiStatus.setTextColor(disconnectedColor);
                //Toast.makeText(getApplicationContext(), "You are not connected to your workplace WI-FI",
                // Toast.LENGTH_LONG).show();
            }
        } else {

            Toast.makeText(getApplicationContext(), "Please turn ON your WI-FI", Toast.LENGTH_LONG).show();
        }


    }

    //function returning "Date"
    public void getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sDfr = new SimpleDateFormat("yyyy. MM. dd ");
        String strDate = sDfr.format(calendar.getTime());
        date.setText(strDate);
        date.setTextColor(Color.BLUE);
    }

    /*Returning Wifi Name*/
    public String returnWifiName() {
        String ssid = "none";
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            ssid = wifiInfo.getSSID();
        }

        return ssid;
    }

}

/*hiding time and status of checked_out
         //group1 = findViewById(R.id.group);
        //((View) group1).setVisibility(View.GONE);

        //group2=findViewById(R.id.group2);
        //group2.setVisibility(View.GONE);*/

