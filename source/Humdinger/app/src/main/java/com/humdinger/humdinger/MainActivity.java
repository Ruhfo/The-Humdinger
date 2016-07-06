package com.humdinger.humdinger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    ControllerView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawingView = new ControllerView(this);
        //drawingView.setOnTouchListener(this);
        //                                Hides the status bar           "Locks" the UI from moving        Makes the layout extend under default UI
        drawingView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
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
    protected void onResume(){
        super.onResume();
        drawingView.resume();
    }

    public void buttonClick(View view){
        Toast.makeText(this,"Button clicked",Toast.LENGTH_SHORT).show();
    }
}
