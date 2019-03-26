package com.example.attendancetracker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FirebaseBackgroundService extends Service {
 
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
