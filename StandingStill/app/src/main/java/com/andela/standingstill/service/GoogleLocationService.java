package com.andela.standingstill.service;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.andela.standingstill.model.Address;

public class GoogleLocationService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        ResultCallback<Status>{

    private final static String TAG = "Location Activity";

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Activity activity;

    private double longitude;
    private double latitude;
    private ActivityChangeListener listener;
    private Address userAddress;
    private String address;

    public GoogleLocationService(Activity activity) {
        this.activity = activity;
        buildApiClient();
        longitude = 0;
        latitude = 0;
        userAddress = new Address(activity);
        address = "";

    }
    public void setListener(ActivityChangeListener listener) {
        this.listener = listener;
    }
    private void buildApiClient() {
        googleApiClient = new GoogleApiClient.Builder(activity)
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
    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public String getCoordinates() {
        return longitude + ":" +latitude;
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
        address = userAddress.getCountryname(latitude, longitude);
        if (!address.equals(null)) {
            listener.onActivityChange(address);
        }

    }

    public String getAddress() {
        return address;
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
