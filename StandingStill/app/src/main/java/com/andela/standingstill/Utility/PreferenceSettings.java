package com.andela.standingstill.Utility;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.andela.standingstill.R;


public class PreferenceSettings extends DialogPreference implements Preference.OnPreferenceChangeListener {
    private static final String DEFAULT_VALUE = "0:5";
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
            String savedVal = duration.timeToString();

            if (callChangeListener(savedVal)) {
                timeDuration = duration;
                persistString(savedVal);
            }

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
            persistString(timeDuration.timeToString());
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
        return true;
    }

    public TimeDuration parseTimeDuration(String durationString) {
        String[] args = durationString.split(":");

        return new TimeDuration(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

    }


    public class TimeDuration {

        protected int hour;

        protected int minute;

        protected int seconds;

        public TimeDuration(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public TimeDuration(int hour, int minute, int seconds) {
            this.hour = hour;
            this.minute = minute;
            this.seconds = seconds;
        }

        public TimeDuration() {

        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }

        public long convertToMills() {
            return (((hour * 3600) + (minute * 60)) * 1000);
        }

        private String hourToString() {
            if (hour <= 0) {
                return "";
            }
            return hour + "hours";
        }

        private String minuteToString() {
            if (minute <= 0) {
                return "";
            }

            return minute + "minutes";
        }

        public String timeToString() {
            if (hour <= 0 && minute <= 0) {
                return "No time set";
            }
            return hourToString() + ":" + minuteToString();
        }

    }

}

