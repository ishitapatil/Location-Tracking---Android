package com.ishita.locationupdater.ui;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.ishita.locationupdater.R;
import com.ishita.locationupdater.services.LocationService;
import com.ishita.locationupdater.ui.controller.LocationController;
import com.ishita.locationupdater.utility.Category;

public class LocationActivity extends Activity {

    private static final String TAG = LocationActivity.class.getSimpleName();

    private LocationController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ToggleButton tbOnOff = findViewById(R.id.tb_on_off);
        controller = new LocationController(this);
        tbOnOff.setOnClickListener(controller);
    }



    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(1322);
        Log.d(Category.CATEGORY_UI,"Job cancelled");

    }

    public void startJob(){
        ComponentName componentName = new ComponentName(this, LocationService.class);
        JobInfo jobInfo = new JobInfo.Builder(1322,componentName)
//                .setRequiredNetworkType()
                .setPersisted(true)
                .setPeriodic(60000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int result = scheduler.schedule(jobInfo);
        if(result == JobScheduler.RESULT_SUCCESS){
            Log.d(Category.CATEGORY_UI,"Job scheduled success ");
        }else {
            Log.d(Category.CATEGORY_UI,"Job scheduled failed");
        }
    }
}
