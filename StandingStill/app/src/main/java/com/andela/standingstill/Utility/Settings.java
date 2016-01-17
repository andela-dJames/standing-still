package com.andela.standingstill.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by andeladev on 15/01/2016.
 */
public class Settings {

    private Context context;

    private SharedPreferences sharedPreferences;

    public Settings(Context context) {
        this.context = context;
    }

    public void saveSettings() {

    }

    public TimeDuration parseTimeDuration(String durationString) {
        String[] args = durationString.split(":");
        TimeDuration duration = null;
        if (args.length < 2){
            return new TimeDuration(0, 5);
        }
        try {
            return new TimeDuration(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } catch (NumberFormatException e) {

        }
        return new TimeDuration(0, 5);
    }

    public String retreiveSettings() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString("delay_key", "0:9");

    }
}
