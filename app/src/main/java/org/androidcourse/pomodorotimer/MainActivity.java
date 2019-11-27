package org.androidcourse.pomodorotimer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView timerTextView = findViewById(R.id.timerTextView);


        timer = new CountDownTimer(3000, 500){

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
