package com.andela.standingstill.Utility;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.andela.standingstill.R;

import org.joda.time.DateTime;

public class Settings {

    private static final String TAG = "Settings";
    private Context context;

    public Settings(Context context) {
        this.context = context;
    }

    /**
     * Parse String to time Duration
     * @param durationString
     * @return
     */
    public TimeDuration parseTimeDuration(String durationString) {
        String[] args = durationString.split(":");
        TimeDuration duration = null;
        if (args.length < 2){
            return new TimeDuration(0, 5);
        }
        try {
            return new TimeDuration(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } catch (NumberFormatException e) {
            Log.d(TAG, e.getMessage());

        }
        return new TimeDuration(0, 5);
    }

    public String retreiveSettings() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString("delay_key", "0:9");

    }

    /**
     * Check if Device Is connected to the Internet
     * @return
     */
    public boolean isConnected() {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Check if GPS is enabled
     * @return
     */
    public boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Request permission to turn on GPS
     */
    public void requestGPSSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.gps_disabled)
                .setPositiveButton(R.string.enable_gps, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);

                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    /**
     * Request permission to turn on Connectivity
     */
    public void requestConnectivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.no_internet_connection)
                .setPositiveButton(R.string.check_internet_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                        context.startActivity(intent);

                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    /**
     * Request permission to enable or update Google play services
     */
    public void requestGooglePlayServices() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.common_google_play_services_enable_title)
                .setPositiveButton(R.string.common_google_play_services_enable_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        //intent.setData(Uri.parse("market://details?id=com.example.android"));
                        context.startActivity(intent);

                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.show();

    }

    /**
     * Checks if the date is today or not.
     * @param dateTime the time to check
     * @param today today's date
     * @return bolean
     */
    public static boolean isToday(DateTime dateTime, DateTime today){

        return dateTime.getYear() == today.getYear() && dateTime.getMonthOfYear() == today.getMonthOfYear() && dateTime.getDayOfMonth() == today.getDayOfMonth();

    }




}
