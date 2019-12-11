package org.androidcourse.pomodorotimer;

enum TimerType {
    WORK("pref_workTime"),
    SHORT_BREAK("pref_shortBreakTime"),
    LONG_BREAK("pref_longBreakTime");

    private final String preferenceKey;

    TimerType(String preferenceKey) {
        this.preferenceKey = preferenceKey;
    }

    public String getPreferenceKey() {
        return preferenceKey;
    }
}
