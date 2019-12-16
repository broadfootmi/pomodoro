package org.androidcourse.pomodorotimer;

enum TimerType {
    WORK("pref_workTime", R.string.work),
    SHORT_BREAK("pref_shortBreakTime", R.string.short_break),
    LONG_BREAK("pref_longBreakTime", R.string.long_break);

    private final String preferenceKey;
    private final int stringResourceId;

    TimerType(String preferenceKey, int stringResrouceId) {
        this.preferenceKey = preferenceKey;
        this.stringResourceId = stringResrouceId;
    }

    public String getPreferenceKey() {
        return preferenceKey;
    }

    public int getStringResource() {
        return stringResourceId;
    }
}
