package com.andela.standingstill.timer;

import android.os.Handler;

/**
 * A StopWatch
 */

public class StopWatch {

    private long hr;
    private long min;
    private long sec;
    private long milliSeconds;
    private Handler handler;
    private long startTime, elapsedTime;


    private int refreshRate = 1000;

    private String seconds, minute, hour;
    private boolean stopped;

    private Runnable runnableTimer = new Runnable() {
        @Override
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(getElapsedTime());
            handler.postDelayed(this, refreshRate);

        }
    };

    /**
     * Start the stopwatch
     */
    public void start() {
        startTime = System.currentTimeMillis();

        handler.removeCallbacks(runnableTimer);
        handler.postDelayed(runnableTimer, 0);

    }

    /**
     * Stop the stopwatch
     */
    public void stop() {
        startTime = System.currentTimeMillis();
        stopped = true;

    }

    public StopWatch() {

        handler = new Handler();
    }

    /**
     * Update the timer
     * @param time
     */
    private void updateTimer(float time) {
        sec = (long) time / 1000;
        min = sec / 60;
        hr = min / 60;

    }

    /**
     *
     * @return string value of seconds
     */
    public String secondsToString() {
        sec = sec % 60;
        seconds=String.valueOf(sec);
        if(sec == 0){
            seconds = "00";
        }
        if(sec <10 && sec > 0){
            seconds = "0"+seconds;
        }
        return seconds;

    }

    public String minuteToString() {
        min = min % 60;
        minute =String.valueOf(min);
        if(min == 0){
            minute = "00";
        }
        if(min <10 && min > 0){
            minute = "0" + minute;
        }

        return minute;
    }

    public String hourToString() {
        hour = String.valueOf(hr);
        if(hr == 0){
            hour = "00";
        }
        if(hr < 10 && hr > 0){
            hour = "0" + hour;
        }

        return hour;
    }

    /**
     * Get the stime spent
     * @param time
     * @return timeStent
     */
    public String timeSpent(long time) {
        String initial = "";
        String message = "";
        initial = hourToString();
        if (initial.equals("00")){
            message +="";
        }
        else {
            message += initial + " hrs, ";
        }

        initial = minuteToString();
        if (initial.equals("00")){
            message +="";
        }
        else {
            message += initial + " min, ";
        }

        initial = secondsToString();

        if (initial.equals("00")){
            message += "";
        }
        else {
            message += initial + " sec";
        }

        return message;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }


    /**
     * get the start time
     * @return
     */
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * get the elapsed time
     * @return
     */
    public long getElapsedTime() {
        return elapsedTime;
    }

}
