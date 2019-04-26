package com.example.attendancetracker;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.app.job.JobWorkItem;
import android.content.Context;
import android.widget.Toast;

import java.util.List;

public class MJobScheduler extends JobService {
    private BackgroundTask backgroundTask;

    @Override
    public boolean onStartJob(final JobParameters params) {
        backgroundTask = new BackgroundTask(this) {
            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Toast.makeText(getApplicationContext(), "Jobservice-OnPost", Toast.LENGTH_LONG).show();
                jobFinished(params, false);
            }
        };
        backgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        backgroundTask.cancel(true);
        return false;
    }
}
