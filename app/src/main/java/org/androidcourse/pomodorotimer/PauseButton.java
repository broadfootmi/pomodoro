package org.androidcourse.pomodorotimer;


import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageButton;

public class PauseButton extends AppCompatImageButton {
    private static final int[] STATE_RESUME = {R.attr.state_resume};

    private boolean isStateResume = false;

    public PauseButton(Context context, AttributeSet attributes){
        super(context, attributes);
    }

    public boolean isStateResume() {
        return isStateResume;
    }

    public void setStateResume(boolean stateResume) {
        isStateResume = stateResume;
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if(isStateResume){
            mergeDrawableStates(drawableState, STATE_RESUME);
        }
        return drawableState;
    }
}
