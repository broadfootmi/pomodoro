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


    private CountDownTimer timer;
    private ImageButton pauseButton;
    private Button endButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView timerTextView = findViewById(R.id.timerTextView);

        pauseButton = findViewById(R.id.pauseTimerImageButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
            }
        });

        endButton = findViewById(R.id.endTimerButton);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                timerTextView.setText(
                        getString(R.string.time, 0, 0)
                );
            }
        });

        timer = new CountDownTimer(30000, 500){
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
            }

            @Override
            public void onFinish() {
            }
        }.start();


    }
}
