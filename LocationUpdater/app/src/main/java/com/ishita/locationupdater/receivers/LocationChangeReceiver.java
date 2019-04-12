package com.ishita.locationupdater.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.LocationResult;
import com.ishita.locationupdater.model.LocationInformation;
import com.ishita.locationupdater.utility.AppConstants;
import com.ishita.locationupdater.utility.Category;
import com.ishita.locationupdater.utility.JSONUtils;

import java.util.ArrayList;

public class LocationChangeReceiver extends BroadcastReceiver {

    private static Location lastLoggedLocation;
    public static final String ACTION_PROCESS_UPDATES =
            "com.ishita.locationchangenotifier.receivers.action" +
                    ".PROCESS_UPDATES";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Category.CATEGORY_RECEIVER, "LocationChangeReceiver onReceive ");
        if (intent != null) {
            String actionName = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(actionName)) {
                LocationResult locationResult = LocationResult.extractResult(intent);
                if (locationResult != null) {
                    Location lastLocation = locationResult.getLastLocation();
                    Log.d(Category.CATEGORY_RECEIVER, "LocationChangeReceiver onReceive latest location is " + lastLocation.toString());
                    LocationInformation information = new LocationInformation();
                    information.setAddressParams(lastLocation.getLatitude(), lastLocation.getLongitude(), context);
                    ArrayList<LocationInformation> locationInformation = null;
                    locationInformation = JSONUtils.getArrayListFromJSONData(context);

                    if (locationInformation == null) {
                        locationInformation = new ArrayList<>();
                    }

                    if (lastLoggedLocation == null) {
                        lastLoggedLocation = lastLocation;
                        locationInformation.add(information);
                        Log.d(Category.CATEGORY_RECEIVER, "LocationChangeReceiver onReceive adding first location!");
                        JSONUtils.saveData(context, locationInformation);
                    } else {
                        Log.d(Category.CATEGORY_RECEIVER, "LocationChangeReceiver onReceive distance between " +
                                "current and last logged location is " + lastLoggedLocation.distanceTo(lastLocation) + " meters!");
                        if (lastLoggedLocation.distanceTo(lastLocation) > AppConstants.LOCATION_DISTANCE_THRESHOLD) {
                            lastLoggedLocation = lastLocation;
                            locationInformation.add(information);
                            Log.d(Category.CATEGORY_RECEIVER, "LocationChangeReceiver onReceive adding next location!");
                            JSONUtils.saveData(context, locationInformation);
                        } else {
                            Log.d(Category.CATEGORY_RECEIVER, "LocationChangeReceiver onReceive current location and last " +
                                    "logged location are within 100 m");
                        }
                    }
                }
            }
        }
    }
}
