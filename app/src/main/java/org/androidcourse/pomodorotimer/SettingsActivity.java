package org.androidcourse.pomodorotimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {
    public static final String RESULT_TIMER_CHANGES = "result";
    public static final String CHANGED_TIMERS = "timers";

    private boolean[] changedTimerOrdinals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        changedTimerOrdinals = new boolean[TimerType.values().length];
    }

    private void updateResult() {
        Bundle changedTimersBundle = new Bundle();
        changedTimersBundle.putBooleanArray(CHANGED_TIMERS, changedTimerOrdinals);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT_TIMER_CHANGES, changedTimersBundle);

        setResult(MainActivity.REQUEST_TIMER_CHANGES, resultIntent);
    }

    private void setPreferenceChanged(TimerType timerChanged) {
        Log.d("SettingsActivity", String.valueOf(timerChanged.ordinal()));
        changedTimerOrdinals[timerChanged.ordinal()] = true;
        updateResult();
    }

    public static class SettingsFragment
            extends PreferenceFragmentCompat
            implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceScreen()
                    .getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            for(TimerType timer : TimerType.values()) {
                String preferenceKey = timer.getPreferenceKey();
                EditTextPreference timerPreference = findPreference(preferenceKey);

                if (timerPreference != null) {
                    timerPreference.setOnBindEditTextListener(
                            new EditTextPreference.OnBindEditTextListener() {
                                @Override
                                public void onBindEditText(@NonNull EditText editText) {
                                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    editText.setHint(
                                            getString(R.string.timer_preference_hint)
                                    );

                                    editText.setSelectAllOnFocus(true);
                                    editText.clearFocus();
                                    editText.requestFocus();
                                }
                            }
                    );

                    setEditTextPreferenceSummaryToItsValue(timerPreference);

                } else {
                    Log.e("Settings", "Null Pref in SettingsAct.onCreatePref()");
                }
            }
        }

        @Override
        public void onStop() {
            super.onStop();

            getPreferenceScreen()
                    .getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference changedPreference = findPreference(key);

            if(changedPreference instanceof EditTextPreference){
                setEditTextPreferenceSummaryToItsValue((EditTextPreference) changedPreference);
            }

            Toast.makeText(
                    getContext(),
                    getString(R.string.saved_changes),
                    Toast.LENGTH_SHORT
            ).show();

            TimerType timerChanged = null;
            for(TimerType type : TimerType.values()){
                if(type.getPreferenceKey().equals(key)){
                    timerChanged = type;
                }
            }

            if(timerChanged != null) {
                SettingsActivity settingsActivity = (SettingsActivity) getActivity();
                settingsActivity.setPreferenceChanged(timerChanged);
            }
        }

        @Override
        public void onPause() {
            super.onPause();

            getPreferenceScreen()
                    .getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onResume() {
            super.onResume();

            getPreferenceScreen()
                    .getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        private void setEditTextPreferenceSummaryToItsValue(EditTextPreference preference){
            String preferenceText = preference.getText();

            int value = 0;
            if(!preferenceText.isEmpty()){
                value = Integer.parseInt(preferenceText);
            }

            preference.setSummary(
                    getString(
                            R.string.time_minutes,
                            value
                    )
            );
        }
    }

}