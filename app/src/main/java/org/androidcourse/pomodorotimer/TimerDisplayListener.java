package org.androidcourse.pomodorotimer;

interface TimerDisplayListener {
    void onTimerTick(long minutes, long seconds);

    void onTimerFinish();

    void onTimerPause();

    void onTimerResume();

    void onTimerStart(long minutes, long seconds, boolean paused);
}
