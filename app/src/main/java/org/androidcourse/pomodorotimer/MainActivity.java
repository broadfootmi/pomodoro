package org.androidcourse.pomodorotimer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AlarmManagerCompat;
import androidx.preference.PreferenceManager;

import android.app.AlarmManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TimerDisplayListener {

    private PauseButton pauseButton;
    private ImageButton endButton;
    private TextView timerTextView;

    private CountDownTimerManager timerManager;

    public static final int REQUEST_NEXT_TIMER = 1;
    public static final int REQUEST_TIMER_CHANGES = 2;

    private boolean[] timersChanged;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras;
        if(data != null){
            extras = data.getExtras();
            if(extras == null){
                Log.e("MainActivity", "Started activity which returned null extras.");
                return;
            }
        } else{
            Log.e("MainActivity", "Started activity which returned null data result.");
            return;
        }

        if(resultCode == REQUEST_NEXT_TIMER){
            int timerOrdinal = -1;
            Bundle result = extras.getBundle(ChooseNextTimerActivity.RESULT_TIMER_TYPE);

            if(result != null){
                timerOrdinal = result.getInt(ChooseNextTimerActivity.TIMER_TYPE_ORDINAL);
                timerManager.startTimer(TimerType.values()[timerOrdinal], false);
            }

            if(timerOrdinal == -1){
                Log.e(
                        "MainActivity",
                        "ChooseNextTimerActivity returned invalid result."
                );
            }

        } else if(resultCode == REQUEST_TIMER_CHANGES){
            Bundle result = extras.getBundle(SettingsActivity.RESULT_TIMER_CHANGES);

            if(result != null){
                timersChanged = result.getBooleanArray(
                        SettingsActivity.CHANGED_TIMERS
                );

                if(timersChanged != null){
                    timersChanged.clone();
                } else{
                    Log.e("MainActivity", "Timer Changes request returned null.");
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        timerTextView = findViewById(R.id.timerTextView);

        pauseButton = findViewById(R.id.pauseTimerImageButton);
        pauseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(timerManager.isTimerRunning()){
                            timerManager.pauseTimer();
                        }
                        else {
                            timerManager.resumeTimer();
                        }
                    }
                }
        );

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
    protected void onResume() {
        super.onResume();

        if(timersChanged == null){
            Log.e("MainActivity", "Did not notice timer setting change.");
        }

        else if(timersChanged[timerManager.getActiveTimerOrdinal()]){
            timersChanged = null;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.reset_timer_prompt);

            alertDialogBuilder.setPositiveButton(
                    android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            timerManager.pauseTimer();
                            timerManager.startTimer(timerManager.getActiveTimerType(), true);
                        }
                    }
            );

            alertDialogBuilder.setNegativeButton(
                    android.R.string.no,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }
            );

            alertDialogBuilder.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        startActivityForResult(
                new Intent(this, SettingsActivity.class),
                REQUEST_TIMER_CHANGES
        );

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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String timerName = null;
        switch(timerManager.getActiveTimerType()){
            case WORK:
                timerName = getString(R.string.work);
                break;
            case LONG_BREAK:
                timerName = getString(R.string.long_break);
                break;
            case SHORT_BREAK:
                timerName = getString(R.string.short_break);
                break;
            default:
                break;
        }

        if(timerName == null){
            Log.e("MainActivity", "Timer name is null");
        }

        builder.setTitle(
                getString(
                        R.string.timer_over,
                        timerName
                )
        );

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startChooseNextTimerActivity();
                        timerManager.stopAlarm();
                    }
                }
        );
        builder.show();

    }

    private void startChooseNextTimerActivity(){
        startActivityForResult(
                new Intent(
                        this,
                        ChooseNextTimerActivity.class
                ),
                REQUEST_NEXT_TIMER
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
