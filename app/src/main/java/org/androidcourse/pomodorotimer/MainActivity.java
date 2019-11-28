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

public class MainActivity extends AppCompatActivity {
    private boolean timerRunning = true;
    private long timerMillisecondsRemaining;

    private ImageButton pauseButton;
    private Button endButton;
    private TextView timerTextView;


    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);

        pauseButton = findViewById(R.id.pauseTimerImageButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerRunning){
                    pauseTimer();
                }
                else {
                    if(timerMillisecondsRemaining > 0){
                        startTimer(timerMillisecondsRemaining);
                    }
                }
            }
        });

        endButton = findViewById(R.id.endTimerButton);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.onFinish();
                timerTextView.setText(
                        getString(R.string.time, 0, 0)
                );
            }
        });

        startTimer(25, 0);

    }

    private void pauseTimer() {
        timerRunning = false;

        timer.cancel();

    }

    private void startTimer(long milliseconds){

        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(minutes));
        startTimer(minutes, seconds);
    }

    private void startTimer(int minutes, int seconds){
        long timeMilliseconds = TimeUnit.MINUTES.toMillis(minutes) +
                TimeUnit.SECONDS.toMillis(seconds);
        long countDownInterval = 100; //ms

         timer = new CountDownTimer(timeMilliseconds, countDownInterval){

            @Override
            public void onTick(long millisecondsRemaining) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisecondsRemaining);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisecondsRemaining) -
                        TimeUnit.MINUTES.toSeconds(minutes);

                timerTextView.setText(
                        getString(
                                R.string.time,
                                minutes,
                                seconds
                        )
                );
                timerMillisecondsRemaining = millisecondsRemaining;
            }

            @Override
            public void onFinish() {
                timerMillisecondsRemaining = 0;
                timerRunning = false;
                endButton.setEnabled(false);
                pauseButton.setEnabled(false);
            }

        }.start();

        timerRunning = true;

    }
}
