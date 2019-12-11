package org.androidcourse.pomodorotimer;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;

import androidx.preference.PreferenceManager;

public class CountDownTimerManager {

    private Context context;
    private TimerDisplayListener displayListener;
    private CountDownTimer timer;
    private boolean timerRunning;
    private long timerMillisecondsRemaining;

    public CountDownTimerManager(TimerDisplayListener displayListener, Context context){
        this.displayListener = displayListener;
        this.context = context;

        startTimerPaused(TimerType.WORK);
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }

    public void pauseTimer() {
        timerRunning = false;
        timer.cancel();

        displayListener.onTimerPause();
    }

    private void startTimer(int minutes){
        startTimer(minutes, 0);
    }

    private void startTimer(long milliseconds){

        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                TimeUnit.MINUTES.toSeconds(minutes));

        startTimer(minutes, seconds);
    }

    private void startTimer(int minutes, int seconds){
        long timeMilliseconds = TimeUnit.MINUTES.toMillis(minutes) +
                TimeUnit.SECONDS.toMillis(seconds);
        long countDownInterval = 100; //ms
        timerMillisecondsRemaining = timeMilliseconds;

        timer = new CountDownTimer(timeMilliseconds, countDownInterval){

            @Override
            public void onTick(long millisecondsRemaining) {
                timerMillisecondsRemaining = millisecondsRemaining;

                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisecondsRemaining);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisecondsRemaining) -
                        TimeUnit.MINUTES.toSeconds(minutes);

                displayListener.onTimerTick(minutes, seconds);
            }

            @Override
            public void onFinish() {
                timerMillisecondsRemaining = 0;
                timerRunning = false;

                displayListener.onTimerFinish();
            }

        }.start();

        timerRunning = true;

    }

    public void resumeTimer() {
        if(timerMillisecondsRemaining > 0){
            startTimer(timerMillisecondsRemaining);
            displayListener.onTimerResume();
        }
    }

    public void endTimer() {
        pauseTimer();
        timer.onFinish();
    }

    public void startTimer(TimerType type) {
        String preferenceKey = type.getPreferenceKey();

        String timerPreferenceText = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(
                        preferenceKey,
                        null
                );

        int timerMinutes = 0;
        if (timerPreferenceText != null) {
            if(!timerPreferenceText.isEmpty()) {
                timerMinutes = Integer.parseInt(timerPreferenceText);
            }
        } else{
            Log.e("Settings", "Null timer preference detected. Using 0.");
        }

        startTimer(timerMinutes);
    }

    public void startTimerPaused(TimerType type){
        startTimer(type);
        timer.onTick(timerMillisecondsRemaining);
        pauseTimer();

    }

}
