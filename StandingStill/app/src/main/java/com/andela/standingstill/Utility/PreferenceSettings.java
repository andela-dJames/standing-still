package com.andela.standingstill.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.andela.standingstill.R;


public class PreferenceSettings extends DialogPreference implements Preference.OnPreferenceChangeListener {
    public static final String DEFAULT_VALUE = "0:5";
    private TimePicker timePicker;
    protected TimeDuration timeDuration;

    public PreferenceSettings(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.time_picker);

        setOnPreferenceChangeListener(this);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        if (positiveResult) {
            TimeDuration duration = new TimeDuration(hour, minute);
            String savedVal = duration.toString();

            if (callChangeListener(savedVal)) {
                timeDuration = duration;
                persistString(savedVal);
            }
            Log.d("DELAY SET TO", savedVal);
        }


    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            timeDuration = parseTimeDuration(getPersistedString(DEFAULT_VALUE));
        } else {
            // Set default state from the XML attribute
            timeDuration = parseTimeDuration((String) defaultValue);
            Log.d("TAG", timeDuration.toString());
            persistString(timeDuration.toString());
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);

    }

    @Override
    protected View onCreateDialogView() {
        View v = super.onCreateDialogView();
        timePicker = (TimePicker) v.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        return v;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        timePicker.setCurrentHour(timeDuration.getHour());
        timePicker.setCurrentMinute(timeDuration.getMinute());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        setSummary(parseTimeDuration(newValue.toString()).timeToString());
        retreiveSettings();
        return true;
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

    public void retreiveSettings() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String val = pref.getString(DEFAULT_VALUE, "0:5");
        Log.d("SHARED PREF", val);
    }


}

