package com.humdinger.humdinger;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.humdinger.humdinger.ControllerView.ControllerView;

public class ControllerActivity extends AppCompatActivity {

    ControllerView controllerView;
    private String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controllerView = new ControllerView(this);
        //                                Hides the status bar           "Locks" the drawn UI to prevent it moving   Makes the layout extend under default UI
        controllerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(controllerView);

        //ipAddress = getIntent().getExtras().getString("key");
        //Log.v("Vastus: ",ipAddress);
    }

    //When app goes into background
    @Override
    protected void onPause() {
        super.onPause();
        controllerView.pause();
    }

    //When app goes to focus
    @Override
    protected void onResume() {
        super.onResume();
        controllerView.resume();
    }
}
