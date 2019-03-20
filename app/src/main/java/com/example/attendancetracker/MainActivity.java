package com.example.attendancetracker;

import android.graphics.Color;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView wifi_status, date,txt_checked_out,txt_checked_in
            ;
    Group group1, group2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hiding time and status of checked_out
         //group1 = findViewById(R.id.group);
        //((View) group1).setVisibility(View.GONE);

        //group2=findViewById(R.id.group2);
        //group2.setVisibility(View.GONE);

        txt_checked_out=findViewById(R.id.txt_checked_out);
        txt_checked_out.setTextColor(Color.rgb(183, 27, 27));

        txt_checked_in=findViewById(R.id.txt_checked_in);
        txt_checked_in.setTextColor(Color.rgb(183, 27, 27));

        wifi_status = findViewById(R.id.text_wifi_status);
        wifi_status.setText("You are not connected yet!");
        wifi_status.setTextColor(Color.rgb(183, 27, 27));


        date = findViewById(R.id.text_date);
        getDate();

    }

    //unction to return the date
    public void getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfr = new SimpleDateFormat("yyyy. MM. dd ");
        String strDate = sdfr.format(calendar.getTime());
        date.setText(strDate);
        date.setTextColor(Color.BLUE);
    }

}

