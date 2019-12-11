package org.androidcourse.pomodorotimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

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

            String[] timerPreferenceKeys = new String[]{
                    CountDownTimerManager.WORK_PREF_KEY,
                    CountDownTimerManager.SHORT_BREAK_PREF_KEY,
                    CountDownTimerManager.LONG_BREAK_PREF_KEY
            };

            for(String key : timerPreferenceKeys){
                EditTextPreference timerPreference = findPreference(key);

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
                } else{
                    Log.e("Settings", "Null Pref in SettingsAct.onCreatePref()");
                }


            }
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