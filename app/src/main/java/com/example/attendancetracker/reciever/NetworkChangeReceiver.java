package com.example.attendancetracker.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.example.attendancetracker.R;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

import static com.example.attendancetracker.Utils.WORK_NETWORK;
import static com.example.attendancetracker.Utils.displayToast;

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



    @Override
    public void onReceive(Context context, Intent intent) {

        isConnectedToRequiredWifi(context);
        connectionCallback.updateUICallback(isConnectedToRequiredWifi(context));
    }

    public int isConnectedToRequiredWifi(Context context) {
        mWifiName = returnWifiName(context).replaceAll("^\"|\"$", "");

        if (isOnline(context)) {
            if (check(mWifiName)) {
                displayToast(context,context.getString(R.string.msg_connected));
                return 1;

            } else if (isOnline(context) && !(check(mWifiName))) {

                displayToast(context,context.getString(R.string.msg_disconnected));
                return 0;
            }
        } else if (!(isOnline(context))) {
            displayToast(context,context.getString(R.string.msg_wifi_closed));
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

    private String returnWifiName(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            ssid = wifiInfo.getSSID();
        }
        return ssid;
    }

    private boolean check(String mWifiName) {
        if (mWifiName.equals(WORK_NETWORK)) {
            return true;
        } else {
            return false;
        }
    }



}




