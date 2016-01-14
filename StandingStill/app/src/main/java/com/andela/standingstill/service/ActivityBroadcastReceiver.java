package com.andela.standingstill.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.andela.standingstill.R;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

import com.andela.standingstill.activity.Constants;

/**
 * Created by andeladev on 13/01/2016.
 */
public class ActivityBroadcastReceiver extends BroadcastReceiver {

    private String userActivity;
    private Context context;
    private ActivityChangeListener changeListener;

    public void setListener(ActivityChangeListener listener) {
        changeListener = listener;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        ArrayList<DetectedActivity> detectedActivities =
                intent.getParcelableArrayListExtra(Constants.ACTIVITY_EXTRA);
        ArrayList<DetectedActivity> mostprobableActivity = intent.getParcelableArrayListExtra(Constants.MOST_PROBABLE_ACTIVITY);
        userActivity = detectedActivityToString(mostprobableActivity.get(0).getType());
        String activity = "";

        for (DetectedActivity detectedActivity: detectedActivities){
            activity += detectedActivityToString(detectedActivity.getType()) + " "+ detectedActivity.getConfidence();


        }

    }

    public String getUserActivity() {
        return userActivity;
    }

    public String detectedActivityToString(int activityType) {
        Resources resources = context.getResources();

        switch (activityType){
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.in_vehicle);

            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);

            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.on_bicycle);

            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.on_foot);

            case DetectedActivity.UNKNOWN:
                return resources.getString(R.string.unknown);

            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);

            default:
                return resources.getString(R.string.unidentifiable_activity);
        }

    }
}
