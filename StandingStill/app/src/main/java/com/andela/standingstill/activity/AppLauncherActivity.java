package com.andela.standingstill.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andela.standingstill.R;
import com.andela.standingstill.Utility.Settings;
import com.andela.standingstill.Utility.TimeDuration;
import com.andela.standingstill.dal.DataAccess;
import com.andela.standingstill.dal.SqliteDBHelper;
import com.andela.standingstill.model.Movement;
import com.andela.standingstill.service.ActivityChangeListener;
import com.andela.standingstill.service.DetectedActivities;
import com.andela.standingstill.service.GoogleLocationService;
import com.andela.standingstill.timer.StopWatch;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * The Laucher Activity of the apllication
 */

public class AppLauncherActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ResultCallback<Status> {

    /**
     * App TAG
     */
    protected static final String TAG = "Main Activity";
    /**
     * Display for timer
     */
    private TextView hourDisplay;
    /**
     * Display for timer
     */
    private TextView minuteDisplay;
    /**
     * Display for timer
     */
    private TextView secondsDisplay;
    private RelativeLayout parent;
    private boolean isRecording;
    private FloatingActionButton fab;
    private GoogleLocationService googleLocationService;
    private ActivityBroadcastReceiver broadcastReceiver;
    private ActivityChangeListener activityChangeListener;

    private StopWatch watch;

    private LocationTimer timer;

    private String userActivity;
    private String initialActivity;
    private DetectedActivity detectedActivity;
    private ActivityChangeListener listener;
    private Settings settings;
    private Movement movement;
    private String address;
    private long elapsedtime;

    public final static String ELAPSED_TIME = "elapsed_time";
    public final static String INITIAL_ACTIVITY = "initial_activity";
    public final static String USER_ACTIVITY = "user_activity";
    public final static String USER_MOVEMENT = "user_movement";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_launcher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState != null){
            elapsedtime = savedInstanceState.getLong(ELAPSED_TIME);
            initialActivity = savedInstanceState.getString(INITIAL_ACTIVITY);
            userActivity = savedInstanceState.getString(USER_ACTIVITY);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initializeComponents();
        broadcastReceiver = new ActivityBroadcastReceiver();
        movement = new Movement();
    }

    /**
     * Initialization of components
     */

    public void initializeComponents(){

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor(Color.parseColor("magenta"));
        hourDisplay = (TextView) findViewById(R.id.hourText);
        minuteDisplay = (TextView) findViewById(R.id.minuteText);
        secondsDisplay = (TextView) findViewById(R.id.secondText);
        isRecording = false;
        watch = new StopWatch();
        googleLocationService = new GoogleLocationService(AppLauncherActivity.this);
        getAddreess();
        settings = new Settings(this);

    }

    /**
     * Start/Stop record toggle Button
     * @param v
     */
    public void record(View v) {
        if (!isRecording){
            startRecording(v);
        }
        else {
            stopRecording(v);
        }

    }

    /**
     * Start Recording
     * @param v
     */
    public void startRecording(View v) {
        if (!settings.isConnected()){
            settings.requestConnectivity();
            return;
        }
        if (!settings.isGpsEnabled()){
            settings.requestGPSSettings();
            return;
        }
        if (!googleLocationService.isConnected()){
            settings.requestGooglePlayServices();
            return;
        }
        requestUpdates();
        changeIcon();
        isRecording = true;
        makeSnacks(v, getResources().getString(R.string.start_tracker));
        watch.start();
        timer = new LocationTimer(watch.getStartTime(), 100);
        timer.start();

    }

    /**
     * Stop Recording
     * @param v
     */

    public void stopRecording(View v) {
        googleLocationService.disconnect();
        changeIcon();
        isRecording = false;
        makeSnacks(v, getResources().getString(R.string.stop_tracker));
        timer.cancel();
        elapsedtime = watch.getElapsedTime();
        if (shouldSave()){
            saveRecord(getUserMovement());
        }
        googleLocationService.disconnect();
        Intent intent = new Intent(this, RecordActivity.class);
        //Bundle bundle = new Bundle();
        //bundle.putParcelable(USER_MOVEMENT, getUserMovement());
        //intent.putExtras(bundle);
        startActivity(intent);

    }

    /**
     * Toggle Start/stop tracking icon
     */
    private void changeIcon() {
        if (!isRecording){
            fab.setImageResource(R.drawable.ic_stop_button);
            isRecording = true;
        }
        else {
            fab.setImageResource(R.drawable.ic_stat_name);
        }
    }

    /**
     * Request for user Address
     */
    public void getAddreess() {
        listener = new ActivityChangeListener() {
            @Override
            public void onActivityChange(String name) {
                address = name;
            }
        };
        googleLocationService.setListener(listener);
    }

    /**
     * Request for Activity Updates
     */
    public void requestUpdates() {

        ActivityRecognition.ActivityRecognitionApi
                .requestActivityUpdates(googleLocationService.getGoogleApiClient(),
                        Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                        getActivityPendingIntent()).setResultCallback(this);

    }

    /**
     * Get pending Intent
     * @return
     */
    private PendingIntent getActivityPendingIntent() {
        Intent intent = new Intent(this, DetectedActivities.class);
        return PendingIntent.getService(this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    /**
     * Get User set time delay duration
     * @return
     */
    public long getSetDuration() {
        Settings settings = new Settings(this);
        String val = settings.retreiveSettings();
        TimeDuration duration =  settings.parseTimeDuration(val);
        return duration.convertToMills();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleLocationService.connect();
        broadcastReceiver = new ActivityBroadcastReceiver();

    }

    @Override
    protected void onStop() {
//        if (googleLocationService.isConnected()){
//            googleLocationService.disconnect();
//        }
        super.onStop();
    }

    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Constants.BROADCAST_ACTION));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_launcher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, PreferenceSettingsActivity.class);
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Display Snackbar messages
     * @param v
     * @param message
     */
    public void makeSnacks(View v, String message){

        Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_location) {
            Intent i = new Intent(this, DisplayRecordActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_timeline) {
            launch(RecordActivity.class);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tracker) {
            if (getApplication() == null){
                launch(AppLauncherActivity.class);
            }

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onResult(Status status) {
        Log.d(TAG, userActivity + " Result");

    }

    /**
     * Class that Keeps the timer Running
     */
    public class LocationTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public LocationTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            float time = millisUntilFinished;
            String sec =  watch.secondsToString();
            String min = watch.minuteToString();
            String hour = watch.hourToString();
            secondsDisplay.setText(sec);
            minuteDisplay.setText(min);
            hourDisplay.setText(hour);
            elapsedtime = watch.getElapsedTime();
//            if (detectedActivity.equals(userActivity)){
//                Log.d(TAG, "SameActivity");
//            }
//            else {
//                Log.d(TAG, "Changed Activity");
//            }

        }

        @Override
        public void onFinish() {

        }
    }

    /**
     * Activity Broadcast Receiver
     */
    public class ActivityBroadcastReceiver extends BroadcastReceiver {

        ActivityChangeListener changeListener;

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<DetectedActivity> mostprobableActivity = intent.getParcelableArrayListExtra(Constants.MOST_PROBABLE_ACTIVITY);
            detectedActivity = mostprobableActivity.get(0);
            userActivity = detectedActivityToString(detectedActivity.getType());
            if (isSignificant(detectedActivity)) {
                if (initialActivity == null) {
                    initialActivity = userActivity;
                }
                if (hasChanged(initialActivity, userActivity)) {
                    if (shouldSave())
                        saveRecord(getUserMovement());
                    resetTimer();

                }

            }
            else {
                return;
            }

        }

        /**
         * Check if Activity has changed
         * @param old
         * @param newVal
         * @return
         */
        public boolean hasChanged(String old, String newVal){
            return !old.equalsIgnoreCase(newVal);

        }

        /**
         * Check if new Activity is significant
         * @param detectedActivity
         * @return
         */
        private boolean isSignificant(DetectedActivity detectedActivity) {
            int type  = detectedActivity.getType();
            switch (type){
                case DetectedActivity.STILL:
                    return true;
                case DetectedActivity.IN_VEHICLE:
                    return true;
                case DetectedActivity.WALKING:
                    return true;

                case DetectedActivity.ON_BICYCLE:
                    return true;

                case DetectedActivity.ON_FOOT:
                    return false;

                case DetectedActivity.UNKNOWN:
                    return false;

                case DetectedActivity.RUNNING:
                    return true;

                default:
                    return false;
            }

        }

        public void setChangeListener(ActivityChangeListener changeListener) {
            this.changeListener = changeListener;
        }

    }

    /**
     * get the String value of Detected Activity
     * @param activityType
     * @return
     */
    public String detectedActivityToString(int activityType) {


        switch (activityType){
            case DetectedActivity.STILL:
                return Constants.STILL;
            case DetectedActivity.IN_VEHICLE:
                return Constants.IN_VEHICLE;

            case DetectedActivity.WALKING:
                return Constants.WALKING;

            case DetectedActivity.ON_BICYCLE:
                return Constants.ON_BICYCLE;

            case DetectedActivity.ON_FOOT:
                return Constants.ON_FOOT;

            case DetectedActivity.UNKNOWN:
                return Constants.UKNOWN;

            case DetectedActivity.RUNNING:
                return Constants.RUNNING;

            default:
                return Constants.UNIDENTIFIABLE_ACTIVITIES;
        }

    }

    /**
     * Get Activity type from string
     * @param activity
     * @return
     */
    public Movement.Type stringToActivity(String activity) {
        switch (activity){
            case Constants.STILL:
                return Movement.Type.STILL;
            case Constants.IN_VEHICLE:
                return Movement.Type.IN_VEHICLE;
            case Constants.WALKING:
                return Movement.Type.WALKING;
            case Constants.ON_FOOT:
                return Movement.Type.ON_FOOT;
            case Constants.RUNNING:
                return Movement.Type.RUNNING;
            case Constants.TILTING:
                return Movement.Type.TILTING;
            case Constants.UKNOWN:
                return Movement.Type.UNKNOWN;
            default:
                return null;
        }
    }

    /**
     * Reset Timer
     */
    public void resetTimer() {
        watch.stop();
        try {
            Thread.sleep(1000);
            watch.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save a record
     * @param movement
     */
    public void saveRecord(Movement movement){
        SqliteDBHelper helper = new SqliteDBHelper(this);
        DataAccess dataAccess = new DataAccess(helper);
        dataAccess.save(movement);

    }

    /**
     * Should An activirty be saved
     * @return
     */
    private boolean shouldSave() {
        return elapsedtime >= getSetDuration();
    }

    /**
     * Builds A movement object
     * @return
     */
    public Movement getUserMovement(){
        movement = new Movement();
        movement.setMovementType(stringToActivity(userActivity));
        movement.setAddress(address);
        movement.setDate(DateTime.now());
        movement.setTimeSpent(elapsedtime);
        movement.setCoordinates(googleLocationService.getCoordinates());
        return movement;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(ELAPSED_TIME, elapsedtime);
        outState.putString(INITIAL_ACTIVITY, initialActivity);
        outState.putString(USER_ACTIVITY, userActivity);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        elapsedtime = savedInstanceState.getLong(ELAPSED_TIME);
        initialActivity = savedInstanceState.getString(INITIAL_ACTIVITY);
        userActivity = savedInstanceState.getString(USER_ACTIVITY);
    }

    private void launch(Class type){
        Intent intent = new Intent(this, type);
        startActivity(intent);

    }
}
