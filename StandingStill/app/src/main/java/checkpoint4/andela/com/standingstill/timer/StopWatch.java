package checkpoint4.andela.com.standingstill.timer;

import android.os.Handler;

/**
 * Created by andeladev on 07/01/2016.
 */
public class StopWatch {

    private long hr;
    private long min;
    private long sec;
    private long milliSeconds;
    private Handler handler;
    private long startTime, elapsedTime;

    private int refreshRate = 100;

    private String seconds, minute, hour;
    private boolean stopped;

//    private Runnable startTimer = new Runnable() {
//        @Override
//        public void run() {
//            elapsedTime = System.currentTimeMillis() - startTime;
//            updateTimer();
//            handler.postDelayed(this, refreshRate);
//        }
//    };

    public StopWatch() {
    }

    public void updateTimer(float time) {
        sec = (long) time / 1000;
        min = sec / 60;
        hr = min / 60;

    }

    public String getSeconds(float time) {
        updateTimer(time);
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

    public String getMinute() {
        return minute;
    }

    public String getHour() {
        return hour;
    }
}
