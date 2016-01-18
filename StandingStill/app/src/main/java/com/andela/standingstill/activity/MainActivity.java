package com.andela.standingstill.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andela.standingstill.R;
import com.andela.standingstill.Utility.Settings;
import com.andela.standingstill.Utility.SinificantActivity;
import com.andela.standingstill.Utility.TimeDuration;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

import com.andela.standingstill.timer.StopWatch;

import com.andela.standingstill.fragment.LocationFragment;
import com.andela.standingstill.service.ActivityChangeListener;
import com.andela.standingstill.service.DetectedActivities;
import com.andela.standingstill.service.GoogleLocationService;

public class MainActivity extends AppCompatActivity implements ResultCallback<Status>, NavigationView.OnNavigationItemSelectedListener {

    protected static final String TAG = "Main Activity";
    private TextView hourDisplay;
    private TextView minuteDisplay;
    private TextView secondsDisplay;
    private RelativeLayout parent;
    private boolean isRecording;
    private FloatingActionButton fab;
    private double longitude, latitude;
    private GoogleLocationService googleLocationService;
    private ActivityBroadcastReceiver broadcastReceiver;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private StopWatch watch;

    private LocationTimer timer;

    private String userActivity;
    private String detectedActivity;
    //private Address userAddress;
    private ActivityChangeListener listener;
    private String address;
    private Settings settings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addActionbar();
        initializeComponents();
        broadcastReceiver = new ActivityBroadcastReceiver();
        retreiveSettings();

    }

    public void retreiveSettings() {
        Settings settings = new Settings(this);
        String val = settings.retreiveSettings();
        TimeDuration duration = settings.parseTimeDuration(val);
        System.out.print(duration.convertToMills());
        Log.d(TAG, String.valueOf(duration.convertToMills()));
    }

    public void checkInternetConnectivity(){
        if (!settings.isConnected()){
           settings.requestConnectivity();
        }
    }

    private void addActionbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }


    private void initializeComponents() {
        drawerLayout = (DrawerLayout) findViewById(R.id.main_activity_drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.main_activity_navigation_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_closed);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View drawerHeader = LayoutInflater.from(this).inflate(R.layout.main_activity_navigation_view_header, null);
        navigationView.addHeaderView(drawerHeader);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        parent = (RelativeLayout) findViewById(R.id.parent_view);
        hourDisplay = (TextView) findViewById(R.id.hourText);
        minuteDisplay = (TextView) findViewById(R.id.minuteText);
        secondsDisplay = (TextView) findViewById(R.id.secondText);
        userActivity = "";

        isRecording = false;
        watch = new StopWatch();
        googleLocationService = new GoogleLocationService(MainActivity.this);
        address = "";
        getAddreess();
        settings = new Settings(this);

    }

    public void record(View v) {
        if (!isRecording){
        startRecording(v);
        }
        else {
            stopRecording(v);
        }

    }
    public void getAddreess() {
        listener = new ActivityChangeListener() {
            @Override
            public void onActivityChange(String name) {
                address = name;
            }
        };
        googleLocationService.setListener(listener);
    }
    public void requestUpdates() {

        ActivityRecognition.ActivityRecognitionApi
                .requestActivityUpdates(googleLocationService.getGoogleApiClient(),
                        Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                        getActivityPendingIntent()).setResultCallback(this);

    }

    private PendingIntent getActivityPendingIntent() {
        Intent intent = new Intent(this, DetectedActivities.class);
        return PendingIntent.getService(this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    public void startRecording(View v) {
        if (!settings.isConnected()){
            settings.requestConnectivity();
            return;
        }
        if (!settings.isGpsEnabled()){
            settings.requestGPSSettings();
            return;
        }
//        if (!googleLocationService.isConnected()){
//            settings.requestGooglePlayServices();
//            return;
//        }
        changeIcon();
        isRecording = true;
        Snackbar.make(v, "Recording has started", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        watch.start();
        timer = new LocationTimer(watch.getStartTime(), 100);
        timer.start();
        requestUpdates();
//        Log.d(TAG, address);
        //getAddreess();
        detectedActivity = userActivity;
        latitude = googleLocationService.getLatitude();
        longitude = googleLocationService.getLongitude();
        retreiveSettings();
//        String name  = userAddress.getCountryname(googleLocationService.getLatitude(), googleLocationService.getLongitude());
        //Log.d(TAG, name);
//        Log.d(TAG, googleLocationService.getUserActivity() + " " + googleLocationService.getLatitude());

    }
    @Override
    protected void onStart() {
        super.onStart();
        googleLocationService.connect();
    }

    @Override
    protected void onStop() {
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    public void stopRecording(View v) {
        changeIcon();
        isRecording = false;
        Snackbar.make(v, "Recording has stopped", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        timer.cancel();
        long time = watch.getElapsedTime();
        Log.d(TAG, address);

        String message = watch.timeSpent(time);
        Intent intent = new Intent(this, LocationFragment.class);
        intent.putExtra("TIMESPENT", message);
        intent.putExtra("LONGITUDE", String.valueOf(longitude));
        intent.putExtra("LATITUDE", String.valueOf(latitude));
        intent.putExtra("ADDRESS", address);
        intent.putExtra("DO", userActivity);
        startActivity(intent);


    }


    private void changeIcon() {
        if (!isRecording){
            fab.setImageResource(R.drawable.ic_stop_button);
            isRecording = true;
        }
        else {
            fab.setImageResource(R.drawable.ic_stat_name);
        }
    }

    public void createFragment() {
        setContentView(R.layout.activity_location_fragment);
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, PreferenceSettingsActivity.class);
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResult(Status status) {
        Log.d(TAG, status.toString());

    }

    public void resetTimer() {
        if (isRecording){
            timer.cancel();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

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
            if (detectedActivity.equals(userActivity)){
                Log.d(TAG, "SameActivity");
            }
            else {
                Log.d(TAG, "Changed Activity");
            }

        }

        @Override
        public void onFinish() {

        }
    }

    public class ActivityBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<DetectedActivity> detectedActivities =
                    intent.getParcelableArrayListExtra(Constants.ACTIVITY_EXTRA);
            ArrayList<DetectedActivity> mostprobableActivity = intent.getParcelableArrayListExtra(Constants.MOST_PROBABLE_ACTIVITY);

            userActivity = detectedActivityToString(mostprobableActivity.get(0).getType());

        }

        public boolean isSignificant(String userActivity){

            return userActivity.equals(SinificantActivity.ActivityType.STILL.getActivity())
                    || userActivity.equals(SinificantActivity.ActivityType.IN_VEHICLE.getActivity())
                    || userActivity.equals(SinificantActivity.ActivityType.ON_BICYCLE.getActivity())
                    || userActivity.equals(SinificantActivity.ActivityType.WALKING.getActivity());

        }

    }

    public String detectedActivityToString(int activityType) {
        Resources resources = this.getResources();

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
