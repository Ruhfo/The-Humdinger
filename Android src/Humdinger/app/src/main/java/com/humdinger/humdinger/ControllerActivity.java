package com.humdinger.humdinger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.humdinger.humdinger.ControllerView.ControllerView;

public class ControllerActivity extends AppCompatActivity {

    ControllerView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawingView = new ControllerView(this);
        //                                Hides the status bar           "Locks" the drawn UI to prevent it moving   Makes the layout extend under default UI
        drawingView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(drawingView);
    }

    //When app goes into background
    @Override
    protected void onPause() {
        super.onPause();
        drawingView.pause();
    }

    //When app goes to focus
    @Override
    protected void onResume() {
        super.onResume();
        drawingView.resume();
    }
}
