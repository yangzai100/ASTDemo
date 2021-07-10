package com.example.sdk;

import android.app.Application;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


@Keep
public class SensorsDataAPI {
    private final String TAG = this.getClass().getSimpleName();
    public static final String SDK_VERSION = "v1.0.0";
    private static SensorsDataAPI INSTANCE;
    private static final Object mLock = new Object();
    private static Map<String,Object> mDeviceInfo;
    private String mDeviceId;
    @Keep
    public static SensorsDataAPI init(Application application){
        synchronized (mLock){
            if (null == INSTANCE){
                INSTANCE = new SensorsDataAPI(application);
            }
            return INSTANCE;
        }
    }

    @Keep
    public static SensorsDataAPI getInstance(){
        return INSTANCE;
    }


    private SensorsDataAPI(Application application){
        mDeviceId = SensorsDataPrivate.getAndroidID(application);
        mDeviceInfo = SensorsDataPrivate.getDeviceInfo(application);
    }

    @Keep
    public void track(@NonNull final String eventName, @Nullable JSONObject properties){

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("event",eventName);
            jsonObject.put("device_id",mDeviceId);

            JSONObject sendProperties = new JSONObject(mDeviceInfo);
            if (properties != null){
                SensorsDataPrivate.mergeJSONObject(properties,sendProperties);
            }
            jsonObject.put("properties",sendProperties);
            jsonObject.put("time",System.currentTimeMillis());
            Log.i(TAG,SensorsDataPrivate.formatJson(jsonObject.toString()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
