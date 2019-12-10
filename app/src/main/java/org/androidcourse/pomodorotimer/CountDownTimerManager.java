package org.androidcourse.pomodorotimer;

import android.os.CountDownTimer;

import java.util.concurrent.TimeUnit;

public class CountDownTimerManager {

    private TimerDisplayListener displayListener;
    private CountDownTimer timer;
    private boolean timerRunning;
    private long timerMillisecondsRemaining;

    public CountDownTimerManager(TimerDisplayListener displayListener){
        this.displayListener = displayListener;
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }

    public void pauseTimer() {
        timerRunning = false;
        timer.cancel();

        displayListener.onTimerPause();
    }

    public void startTimer(int minutes){
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
}
