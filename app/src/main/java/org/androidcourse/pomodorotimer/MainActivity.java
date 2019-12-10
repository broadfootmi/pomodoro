package org.androidcourse.pomodorotimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements TimerDisplayListener {

    private PauseButton pauseButton;
    private Button endButton;
    private TextView timerTextView;

    private CountDownTimerManager timerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        timerManager = new CountDownTimerManager(this);

        timerTextView = findViewById(R.id.timerTextView);

        pauseButton = findViewById(R.id.pauseTimerImageButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerManager.isTimerRunning()){
                    timerManager.pauseTimer();
                }
                else {
                    timerManager.resumeTimer();
                }
            }
        });

        endButton = findViewById(R.id.endTimerButton);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerManager.endTimer();
            }
        });

        int timerMinutes = Integer.parseInt(
                PreferenceManager.getDefaultSharedPreferences(this)
                        .getString(
                        "pref_workTime",
                        null
                        )
        );
        timerManager.startTimer(timerMinutes);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        startActivity(new Intent(this, SettingsActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimerTick(long minutes, long seconds) {
        timerTextView.setText(
                getString(
                        R.string.time,
                        minutes,
                        seconds
                )
        );
    }

    @Override
    public void onTimerFinish() {
        timerTextView.setText(
                getString(R.string.time, 0, 0)
        );

        endButton.setEnabled(false);
        pauseButton.setEnabled(false);

        Toast.makeText(
                this,
                getString(
                        R.string.timer_over,
                        getString(R.string.work)
                        ),
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onTimerPause() {
        pauseButton.setStateResume(true);
        pauseButton.refreshDrawableState();
    }

    @Override
    public void onTimerResume() {
        pauseButton.setStateResume(false);
        pauseButton.refreshDrawableState();
    }
}
