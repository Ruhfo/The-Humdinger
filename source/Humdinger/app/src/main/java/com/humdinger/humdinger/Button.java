package com.humdinger.humdinger;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

public class Button {
    float x, y;
    Bitmap normalBitmap, pressedBitmap, currentlyDisplayed;
    Rect buttonArea;
    boolean buttonPressed = false;
    char message;

    public Button(float x, float y, Bitmap normalBitmap, Bitmap pressedBitmap, char message){
        this.x = x;
        this.y = y;
        this.normalBitmap = normalBitmap;
        this.pressedBitmap = pressedBitmap;
        this.currentlyDisplayed = normalBitmap;
        setRectangleOnButton(x,y);
        this.message = message;
    }

    private void setRectangleOnButton(float x,float y){
        int xx = Math.round(x);
        int yy = Math.round(y);
        this.buttonArea = new Rect(xx,yy,xx+currentlyDisplayed.getWidth(),yy+currentlyDisplayed.getHeight());
    }

    public boolean isItTouched(float x, float y){
        return buttonArea.contains((int) x, (int) y);
    }

    public void update(){
        if(buttonPressed){
            this.currentlyDisplayed = this.pressedBitmap;
            sendMessage(this.message);
        }else this.currentlyDisplayed = this.normalBitmap;
    }

    private void sendMessage(char message){
        Log.v("Message","I am sending "+message);
    }
}

