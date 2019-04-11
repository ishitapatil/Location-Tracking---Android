package com.ishita.locationupdater;

import android.app.Application;
import android.util.Log;

public class MainApplication extends Application {

    public void onCreate() {
        Log.d("MainApplication","onCreate" );
        super.onCreate();
    }
}
