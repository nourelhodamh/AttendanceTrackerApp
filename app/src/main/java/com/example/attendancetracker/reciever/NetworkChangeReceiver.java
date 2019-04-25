package com.example.attendancetracker.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.attendancetracker.R;
import com.example.attendancetracker.UserData;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

import static com.example.attendancetracker.Utils.WORK_NETWORK;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private String mWifiName;
    String ssid = "none";

    private static ConnectivityManager conn;
    public static NetworkInfo networkInfo;

    private static WifiManager wifiManager;
    public static WifiInfo wifiInfo;

    private ConnectionCallback connectionCallback;

    public NetworkChangeReceiver(ConnectionCallback callback) {
        super();
        this.connectionCallback = callback;

    }

    public NetworkChangeReceiver() {

    }


    @Override
    public void onReceive(Context context, Intent intent) {

        isConnectedToRequiredWifi(context);
        connectionCallback.updateUICallback(isConnectedToRequiredWifi(context), trackingTimeOne());
    }

    public int isConnectedToRequiredWifi(Context context) {
        mWifiName = returnWifiName(context).replaceAll("^\"|\"$", "");
        if (isOnline(context)) {
            if (check(mWifiName)) {
                trackingTimeOne();
                Toast.makeText(context, context.getString(R.string.connected_to_database), Toast.LENGTH_LONG).show();
                return 1;
            } else if (isOnline(context) && !(check(mWifiName))) {

                Toast.makeText(context, "Disconnected of Database", Toast.LENGTH_LONG).show();
                return 0;
            }
        } else if (!(isOnline(context))) {
            Toast.makeText(context, "Open your WI-FI", Toast.LENGTH_LONG).show();
            return 2;
        }
        return 2;
    }


    public Map trackingTimeOne() {
        Map map = new HashMap();
        map.put("Time", ServerValue.TIMESTAMP);
        return map;
    }


    private boolean isOnline(Context context) {
        conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = conn.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if ((networkInfo != null && networkInfo.isConnected())) {
            return true;
        }
        return false;
    }


    private boolean check(String mWifiName) {
        if (mWifiName.equals(WORK_NETWORK)) {
            return true;
        } else {
            return false;
        }
    }

    private String returnWifiName(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            ssid = wifiInfo.getSSID();
        }
        return ssid;
    }

}




