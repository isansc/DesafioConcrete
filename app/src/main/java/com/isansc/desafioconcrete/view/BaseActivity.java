package com.isansc.desafioconcrete.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.isansc.desafioconcrete.R;

/**
 * Created by Isan on 01-Nov-17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Setup transition type
     * For Slide_V:
     *      On Layout, put:
     *          android:layout_marginTop="@dimen/status_bar_height"
     *      On Manifest, put:
     *          android:theme="@style/Theme.AppCompat.Translucent.NoStatusBar"
     */
    public enum TransitionType{
        SLIDE_H, SLIDE_V, FADE, NONE;
    }

    protected abstract TransitionType getTransitionType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterAnimation();
    }

    private void setEnterAnimation(){
        switch (getTransitionType()) {
            case SLIDE_H:
                overridePendingTransition(R.anim.slide_horizontal_in, R.anim.slide_horizontal_out);
                break;
            case SLIDE_V:
                overridePendingTransition(R.anim.slide_vertical_in, 0);
                break;
            case FADE:
                overridePendingTransition(android.R.anim.fade_in, 0);
                break;
            case NONE:
                break;
        }
    }

    private void setExitAnimation(){
        switch (getTransitionType()) {
            case SLIDE_H:
                overridePendingTransition(R.anim.slide_horizontal_back_in, R.anim.slide_horizontal_back_out);
                break;
            case SLIDE_V:
                overridePendingTransition(R.anim.hold, R.anim.slide_vertical_back_out);
                break;
            case FADE:
                overridePendingTransition(0, android.R.anim.fade_out);
                break;
            case NONE:
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        setExitAnimation();
    }

}
