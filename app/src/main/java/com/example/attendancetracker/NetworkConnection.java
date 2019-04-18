package com.example.attendancetracker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkConnection {


    Utils utils= new Utils();


    private static  ConnectivityManager connMgr;
    public static NetworkInfo activeInfo;

    private String ssid = "none";
    private String mWifiName;
    public int flag;

    public NetworkConnection() {
        super();
    }

    /**
     * Function returns if wifi is connected to workplace network or not
     */

    public int networkStatus(Context context) {
        connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeInfo =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (activeInfo != null && activeInfo.isConnected()) {

            WifiManager wifiManager = (WifiManager)
                    context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo =
                    wifiManager.getConnectionInfo();

            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                ssid = wifiInfo.getSSID();
            }

            mWifiName = ssid.replaceAll("^\"|\"$", "");


            if (mWifiName.equals(utils.WORK_NETWORK)) {
                return flag=1;

            } else if (activeInfo.isConnected() && !(mWifiName.equals(utils.WORK_NETWORK))) {
                return flag=0;

            }
        } else {
            return flag=2;

        }
        return 0;
    }


}
