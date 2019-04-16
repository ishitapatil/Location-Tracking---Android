package com.ishita.locationupdater.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ishita.locationupdater.R;
import com.ishita.locationupdater.model.LocationInformation;
import com.ishita.locationupdater.services.LocationService;
import com.ishita.locationupdater.ui.adapters.LocationListAdapter;
import com.ishita.locationupdater.ui.controller.LocationController;
import com.ishita.locationupdater.utility.AppConstants;
import com.ishita.locationupdater.utility.Category;

public class LocationActivity extends Activity {

    private static final String TAG = LocationActivity.class.getSimpleName();

    private LocationController controller;
    private ListView lvLocations;
    ToggleButton tbOnOff;
    LocationListAdapter adapter;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        controller = new LocationController(this);
        initUI();
    }

    private void initUI(){
        tbOnOff = findViewById(R.id.tb_on_off);
        tbOnOff.setOnClickListener(controller);
        tbOnOff.setChecked(getCurrentTrackerSettigs());
        lvLocations = findViewById(R.id.lv_locations);
        adapter = new LocationListAdapter(this);
        lvLocations.setAdapter(adapter);
        lvLocations.setOnItemClickListener(controller);

    }

    @Override
    protected void onPause() {
        controller.removeListeners();
        super.onPause();
    }

    @Override
    protected void onResume() {
        refreshAdapter();
        controller.addListeners();
        super.onResume();
    }

    public void refreshAdapter() {
        if(adapter!=null) {
            adapter.setListAndNotify();
        }
    }

    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(1322);
        Log.d(Category.CATEGORY_UI,"Job cancelled");
        Toast.makeText(this, R.string.msg_tracker_disabled,Toast.LENGTH_SHORT).show();
        changeTrackerSetting(false);
    }

    public void startJob(){
        ComponentName componentName = new ComponentName(this, LocationService.class);
        JobInfo jobInfo = new JobInfo.Builder(1322,componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                .setPersisted(true)
//                .setPeriodic(10000)
                .setMinimumLatency(10000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int result = scheduler.schedule(jobInfo);
        if(result == JobScheduler.RESULT_SUCCESS){
            Log.d(Category.CATEGORY_UI,"Job scheduled success ");
            Toast.makeText(this, R.string.msg_tracker_enabled,Toast.LENGTH_SHORT).show();
            changeTrackerSetting(true);
        }else {
            Log.d(Category.CATEGORY_UI,"Job scheduled failed");
        }
    }

    public void showDialog(LocationInformation information){
        dialog = new Dialog(this);
        dialog.setTitle(R.string.title_info);
        View view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
        TextView tvCoordinates = view.findViewById(R.id.tv_coordinates);
        tvCoordinates.setText(getString(R.string.lbl_lat_long,information.getLatitude(), information.getLongitude()));
        TextView tvAddress = view.findViewById(R.id.tv_address);
        tvAddress.setText(information.getAddressLine());
        TextView tvTimeOfVisit = view.findViewById(R.id.tv_time_of_visit);
        tvTimeOfVisit.setText(information.getVisitTime().toString());
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void changeTrackerSetting(boolean flag){
        SharedPreferences sharedPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(AppConstants.KEY_SETTING_ON, flag);
    }

    public boolean getCurrentTrackerSettigs(){
            SharedPreferences sharedPrefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            return sharedPrefs.getBoolean(AppConstants.KEY_SETTING_ON,false);
    }

}
