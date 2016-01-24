package com.andela.standingstill.Utility;

/**
 * Created by andeladev on 16/01/2016.
 */
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

        return (hour <= 0) ? "" : (hour + "hours:");
    }

    private String minuteToString() {
        if (minute <= 0) {
            return "";
        }

        return (minute <= 1) ? (minute + "minute") : (minute + "minutes");
    }

    public String timeToString() {
        if (hour <= 0 && minute <= 0) {
            return "No time set";
        }
        return hourToString() + minuteToString();
    }

    @Override
    public String toString() {
        return (hour <= 0 && minute <= 0) ? "No Value set" : hour + ":" + minute;
    }


}
