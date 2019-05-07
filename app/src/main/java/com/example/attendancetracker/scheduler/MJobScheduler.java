package com.example.attendancetracker.scheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.example.attendancetracker.activities.AsyncActivity;

public class MJobScheduler extends JobService {
//    private BackgroundTask backgroundTask;

    private AsyncActivity.BackgroundTask backgroundTask;
    @Override
    public boolean onStartJob(final JobParameters params) {

        backgroundTask= new AsyncActivity.BackgroundTask(this){
            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.v("MJobScheduler", "OnPost");
                //Toast.makeText(getApplicationContext(), "Jobservice-OnPost", Toast.LENGTH_LONG).show();
                jobFinished(params, false);
            }
        };
        backgroundTask.execute();


//        backgroundTask = new BackgroundTask(this) {
//           @Override
//            protected void onPostExecute(Integer integer) {
//                super.onPostExecute(integer);
//                Log.v("MJobScheduler", "OnPost");
//                //Toast.makeText(getApplicationContext(), "Jobservice-OnPost", Toast.LENGTH_LONG).show();
//                jobFinished(params, false);
//            }
//        };
//        backgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        backgroundTask.cancel(true);
        return false;
    }
}
