package org.androidcourse.pomodorotimer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TimerDisplayListener {

    private PauseButton pauseButton;
    private Button endButton;
    private TextView timerTextView;

    private CountDownTimerManager timerManager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            int timerOrdinal = data.getExtras().getBundle("result").getInt("timer_ordinal");
            timerManager.startTimer(TimerType.values()[timerOrdinal], false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

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

        timerManager = new CountDownTimerManager(this, this);
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

        startActivityForResult(
                new Intent(
                        this,
                        ChooseNextTimerActivity.class
                ),
                ChooseNextTimerActivity.REQUEST_NEXT_TIMER
        );
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

    @Override
    public void onTimerStart(long minutes, long seconds, boolean paused) {
        endButton.setEnabled(true);
        pauseButton.setEnabled(true);
        onTimerTick(minutes, seconds);

        pauseButton.setStateResume(paused);
    }
}
