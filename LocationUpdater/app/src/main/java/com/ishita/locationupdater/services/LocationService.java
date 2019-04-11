package com.ishita.locationupdater.services;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.ishita.locationupdater.R;
import com.ishita.locationupdater.receivers.LocationChangeReceiver;
import com.ishita.locationupdater.utility.Category;
import com.ishita.locationupdater.utility.JSONUtils;

import java.util.ArrayList;

public class LocationService extends JobService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static String TAG = LocationService.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();
    private static final long LOCATION_INTERVAL = 10 * 1000;
    private static final long FASTEST_LOCATION_INTERVAL = LOCATION_INTERVAL / 2;

    public LocationService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(Category.CATEGORY_SERVICES, "LocationService onStartJob ");
        if (!JSONUtils.isFilePresent(this)) {
            JSONUtils.create(this);
        }
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest.setInterval(LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_LOCATION_INTERVAL);


        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        mLocationRequest.setPriority(priority);
        mLocationClient.connect();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(Category.CATEGORY_SERVICES,"LocationService onStopJob  ");
        Intent intent = new Intent(this, LocationChangeReceiver.class);
        intent.setAction(LocationChangeReceiver.ACTION_PROCESS_UPDATES);
        LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient,
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(Category.CATEGORY_SERVICES, "GoogleApiClient connected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, R.string.lbl_wrng_location_permission, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, LocationChangeReceiver.class);
        intent.setAction(LocationChangeReceiver.ACTION_PROCESS_UPDATES);
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, R.string.msg_connection_failed,Toast.LENGTH_SHORT).show();
    }
}
