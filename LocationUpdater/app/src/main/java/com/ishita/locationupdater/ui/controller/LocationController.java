package com.ishita.locationupdater.ui.controller;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ToggleButton;

import com.ishita.locationupdater.R;
import com.ishita.locationupdater.model.LocationInformation;
import com.ishita.locationupdater.ui.LocationActivity;
import com.ishita.locationupdater.utility.EventDispatcher;
import com.ishita.locationupdater.utility.EventModel;
import com.ishita.locationupdater.utility.EventNotifierConstants;
import com.ishita.locationupdater.utility.EventTypes;
import com.ishita.locationupdater.utility.IEventHandler;

public class LocationController implements View.OnClickListener,IEventHandler, AdapterView.OnItemClickListener {

    private LocationActivity activity;

    public void addListeners() {
        EventDispatcher.getInstance().addEventListener(EventNotifierConstants.NOTIFIER_TYPE_LOCATION, this);
    }
    public void removeListeners() {
        EventDispatcher.getInstance().removeEventListener(EventNotifierConstants.NOTIFIER_TYPE_LOCATION);
    }

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

    @Override
    public void callback(int eventType, EventModel event) {
        switch (eventType){
            case EventTypes.NEW_LOCATION_LOGGED:
                activity.refreshAdapter();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            LocationInformation information = (LocationInformation) parent.getItemAtPosition(position);
            activity.showDialog(information);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
