package com.andela.standingstill.activity;

import com.google.android.gms.location.DetectedActivity;

/**
 * Created by andeladev on 11/01/2016.
 */
public final class Constants {

    public static final String PACKAGE_NAME = "com.andela.standingstill";

    public static final String BROADCAST_ACTION = PACKAGE_NAME + ".broadcast action";

    public static final String ACTIVITY_EXTRA = PACKAGE_NAME + ".activity extra";

    public static final String SHARED_PREFERENCES = PACKAGE_NAME + ".shared preferences";

    public static final String MONITORED_ACTIVITIES = PACKAGE_NAME + ".monitored avtivities";

    public static final String ACTIVITY_UPDATES = PACKAGE_NAME + ".activity updates";

    public static final int[] DETECTED_ACTIVITIES = {
            DetectedActivity.STILL,
            DetectedActivity.IN_VEHICLE,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.TILTING,
            DetectedActivity.WALKING,
            DetectedActivity.UNKNOWN,
            DetectedActivity.ON_FOOT
    };

    public static final long DETECTION_INTERVAL_IN_MILLISECONDS = 0;

    public static final String MOST_PROBABLE_ACTIVITY = PACKAGE_NAME + ".most probable Activity";
    public static final String STILL = "Standing Still";
    public static final String IN_VEHICLE = "Transport";
    public static final String WALKING = "Walking";
    public static final String ON_FOOT = "On foot";
    public static final String RUNNING = "Running";
    public static final String TILTING = "Tilting";
    public static final String UKNOWN = "Unknown Activity";
    public static final String ON_BICYCLE = "On a bycycle";
    public static final String UNIDENTIFIABLE_ACTIVITIES = "Unidentifiable activities";
}
