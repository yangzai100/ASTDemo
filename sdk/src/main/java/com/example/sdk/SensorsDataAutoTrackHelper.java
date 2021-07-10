package com.example.sdk;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.annotation.Keep;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 主要是给plugin使用的，现新增一个方法trackViewOnCLick，用来给Button做埋点
 */
public class SensorsDataAutoTrackHelper {
    /***
     * View被点击，自动埋点
     */
    @Keep
    public static void trackViewOnClick(View view){
        Log.d("test","我调用了埋点方法");

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("$elemeng_type",SensorsDataPrivate.getElementType(view));
            jsonObject.put("$element_id",SensorsDataPrivate.getViewId(view));
            jsonObject.put("element_content",SensorsDataPrivate.getElementContent(view));
            Activity activity = SensorsDataPrivate.getActivityFromView(view);
            if (activity != null){
                jsonObject.put("$activity",activity.getClass().getCanonicalName());
            }
            Log.d("test","我调用了埋点方法1");
            SensorsDataAPI.getInstance().track("$AppClick",jsonObject);
            Log.d("test","我调用了埋点方法2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
