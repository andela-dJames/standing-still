package checkpoint4.andela.com.standingstill;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class GoogleLocationService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        ResultCallback<Status>{

    private final static String TAG = "Location Activity";

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location userLocation;
    private Context context;

    private double longitude;
    private double latitude;
    private ActivityBroadcastReceiver activityBroadcastReceiver;
    private String userActivity;
    private ActivityChangeListener listener;

    public GoogleLocationService(Context context) {
        this.context = context;
        buildApiClient();
        longitude = 0;
        latitude = 0;
        activityBroadcastReceiver = new ActivityBroadcastReceiver();

    }

    private void buildApiClient() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);


    }

        public void requestUpdates() {

        ActivityRecognition.ActivityRecognitionApi
                .requestActivityUpdates(googleApiClient,
                        Constants.DETECTION_INTERVAL_IN_MILLISECONDS,
                        getActivityPendingIntent()).setResultCallback(this);

    }

    private PendingIntent getActivityPendingIntent() {
        Intent intent = new Intent(context, DetectedActivities.class);
        return PendingIntent.getService(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private int checkSelfPermission(Object accessFineLocation) {
        return 0;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onLocationChanged(Location location) {


        latitude = location.getLatitude();
        longitude = location.getLongitude();
        requestUpdates();
        listener = new ActivityChangeListener() {
            @Override
            public void onActivityChange() {
                userActivity = activityBroadcastReceiver.getUserActivity();

            }
        };
        activityBroadcastReceiver.setListener(listener);

        Log.d(TAG, userActivity);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {


    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public void connect() {
        googleApiClient.connect();
    }

    public void disconnect() {
        googleApiClient.disconnect();
    }

    public boolean isConnected() {
        return googleApiClient.isConnected();
    }


    @Override
    public void onResult(Status status) {

    }
}
