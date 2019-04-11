package com.ishita.locationupdater.ui.controller;

import android.view.View;
import android.widget.ToggleButton;

import com.ishita.locationupdater.R;
import com.ishita.locationupdater.ui.LocationActivity;

public class LocationController implements View.OnClickListener{

    private LocationActivity activity;

    public LocationController(LocationActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tb_on_off:
            if(v instanceof ToggleButton){
                boolean isChecked = ((ToggleButton) v).isChecked();
                if(isChecked){
                    activity.startJob();
                }else{
                    activity.cancelJob();
                }
            }
            break;
        }
    }
}
