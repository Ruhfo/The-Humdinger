package com.humdinger.humdinger;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Button {
    float x, y;
    Bitmap normalBitmap, pressedBitmap, currentlyDisplayed;
    Rect buttonArea;
    boolean pressed = false;

    public Button(float x, float y, Bitmap normalBitmap, Bitmap pressedBitmap){
        this.x = x;
        this.y = y;
        this.normalBitmap = normalBitmap;
        this.pressedBitmap = pressedBitmap;
        this.currentlyDisplayed = normalBitmap;
        setRectangleOnButton(x,y);
    }

    public void setRectangleOnButton(float x,float y){
        int xx = Math.round(x);
        int yy = Math.round(y);
        this.buttonArea = new Rect(xx,yy,xx+currentlyDisplayed.getWidth(),yy+currentlyDisplayed.getHeight());
    }

    public boolean isItTouched(float x, float y){
        if(buttonArea.contains((int) x,(int) y)){
            return true;
        }
        else{
            return false;
        }
    }

    public void sendMessage(){

    }
}

