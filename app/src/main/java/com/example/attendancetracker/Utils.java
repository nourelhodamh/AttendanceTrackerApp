package com.example.attendancetracker;

        import android.content.Context;
        import android.widget.Toast;

public class Utils {
    public static final String WORK_NETWORK = "KabelBox-9C1C";

    public static void displayToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
