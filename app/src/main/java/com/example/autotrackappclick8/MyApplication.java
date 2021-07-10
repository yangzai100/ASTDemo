package com.example.autotrackappclick8;

import android.app.Application;

import com.example.sdk.SensorsDataAPI;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initSensorsDataAPI(this);
    }

    private void initSensorsDataAPI(Application application){
        SensorsDataAPI.init(application);
    }
}
