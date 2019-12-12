package org.androidcourse.pomodorotimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseNextTimerActivity extends AppCompatActivity {

    public static final int REQUEST_NEXT_TIMER = 1;

    public static final String RESULT_TIMER_TYPE = "result";
    public static final String TIMER_TYPE_ORDINAL = "timer_ordinal";

    private Button workButton;
    private Button shortBreakButton;
    private Button longBreakButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_next_timer);

        workButton = findViewById(R.id.workButton);
        shortBreakButton = findViewById(R.id.shortBreakButton);
        longBreakButton = findViewById(R.id.longBreakButton);

        workButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWithResult(TimerType.WORK);
            }
        });

        shortBreakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWithResult(TimerType.SHORT_BREAK);
            }
        });

        longBreakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWithResult(TimerType.LONG_BREAK);
            }
        });

    }

    private void finishWithResult(TimerType work) {
        Bundle timerTypeBundle = new Bundle();
        timerTypeBundle.putInt(TIMER_TYPE_ORDINAL, work.ordinal());

        Intent resultIntent = new Intent();
        resultIntent.putExtra(RESULT_TIMER_TYPE, timerTypeBundle);

        setResult(REQUEST_NEXT_TIMER, resultIntent);
        finish();
    }
}
