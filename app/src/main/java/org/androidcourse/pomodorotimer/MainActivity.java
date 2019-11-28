package org.androidcourse.pomodorotimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements TimerDisplayListener {

    private ImageButton pauseButton;
    private Button endButton;
    private TextView timerTextView;

    private CountDownTimerManager timerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        timerManager.startTimer(25, 0);

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
    }
}
