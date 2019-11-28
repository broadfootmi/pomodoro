package org.androidcourse.pomodorotimer;

interface TimerDisplayListener {
    void onTimerTick(long minutes, long seconds);

    void onTimerFinish();
}
