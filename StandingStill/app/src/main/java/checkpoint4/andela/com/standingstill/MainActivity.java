package checkpoint4.andela.com.standingstill;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.service.carrier.CarrierMessagingService;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

import checkpoint4.andela.com.standingstill.timer.StopWatch;

public class MainActivity extends AppCompatActivity implements ResultCallback<Status>{

    protected static final String TAG = "Main Activity";

    private TextView hourDisplay;
    private TextView minuteDisplay;
    private TextView secondsDisplay;
    private RelativeLayout parent;
    private boolean isRecording;
    private FloatingActionButton fab;
    private GoogleLocationService googleLocationService;
    private double userLongitude, userLatitude;
    private ActivityBroadcastReceiver broadcastReceiver;

    private StopWatch watch;

    private LocationTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeComponents();
        googleLocationService = new GoogleLocationService(this);
        broadcastReceiver = new ActivityBroadcastReceiver();

    }

    private void initializeComponents() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        parent = (RelativeLayout) findViewById(R.id.parent_view);
        hourDisplay = (TextView) findViewById(R.id.hourText);
        minuteDisplay = (TextView) findViewById(R.id.minuteText);
        secondsDisplay = (TextView) findViewById(R.id.secondText);

        isRecording = false;
        watch = new StopWatch();
    }

    public void record(View v) {
        if (!isRecording){
        startRecording(v);
        }
        else {
            stopRecording(v);
        }

    }

    public void requestUpdates() {
        ActivityRecognition.ActivityRecognitionApi
                .requestActivityUpdates(googleLocationService,
                        Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                        getActivityPendingIntent()).setResultCallback(this);
    }

    public void startRecording(View v) {
        changeIcon();
        isRecording = true;
        Snackbar.make(v, "Recording has started", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        watch.start();
        timer = new LocationTimer(watch.getStartTime(), 100);
        timer.start();

    }
    @Override
    protected void onStart() {
        super.onStart();
        googleLocationService.connect();
    }

    @Override
    protected void onStop() {
        if (googleLocationService.isConnected()){
            googleLocationService.disconnect();
        }
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

    public void stopRecording(View v) {
        changeIcon();
        isRecording = false;
        Snackbar.make(v, "Recording has stopped", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        timer.cancel();
        long time = watch.getElapsedTime();
        String message = watch.timeSpent(time);
        Intent intent = new Intent(this, LocationFragment.class);
        intent.putExtra("TIMESPENT", message);
        intent.putExtra("LONGITUDE", String.valueOf(googleLocationService.getLongitude()));
        intent.putExtra("LATITUDE", String.valueOf(googleLocationService.getLatitude()));
        startActivity(intent);
        Log.d(TAG, String.valueOf(googleLocationService.getLatitude()));


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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResult(Status status) {
        
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

            String userActivity = intent.getStringExtra(Constants.MOST_PROBABLE_ACTIVITY);
            Log.d(TAG, userActivity);
            String activity = "";

            for (DetectedActivity detectedActivity: detectedActivities){
                activity += detectedActivityToString(detectedActivity.getType()) + " "+detectedActivity.getConfidence();

            }

        }

        public void registerReceiver() {

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
