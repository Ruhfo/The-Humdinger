package com.humdinger.humdinger.ControllerView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.humdinger.humdinger.Networker.Key;
import static com.humdinger.humdinger.ControllerView.ControllerView.gamePad;

public abstract class Button {
    boolean buttonPressed = false;
    private final String LOG_TAG = Button.class.getSimpleName();


    public Button(){
    }

    abstract void setRectangleOnButton();
    abstract boolean isItTouched(float x, float y);
    abstract void drawRectangle(Canvas canvas, Paint paint);
    abstract void drawButton(Canvas c);
    abstract void update();

    public void sendMessage(char message) {
        Log.v(getClass().getSimpleName(), "I am sending " + message);
/*        try {
            gamePad.put(new Key(message,'0'));
        } catch (InterruptedException e) {
            Log.e("Message", "Interrupted gamePad put");
       }*/
    }
}





