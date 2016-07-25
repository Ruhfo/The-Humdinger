package com.humdinger.humdinger.ControllerView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Button {
    float x, y;
    Bitmap normalBitmap, pressedBitmap, currentlyDisplayed;
    private Rect buttonArea;
    boolean buttonPressed = false;
    private char message;
    private final String LOG_TAG = Button.class.getSimpleName();

    public Button(float x, float y, Bitmap normalBitmap, Bitmap pressedBitmap, char message) {
        this.x = x;
        this.y = y;
        this.normalBitmap = normalBitmap;
        this.pressedBitmap = pressedBitmap;
        this.currentlyDisplayed = normalBitmap;
        this.message = message;
        setRectangleOnButton(x, y);
    }

    public void setRectangleOnButton(float x, float y) {
        int xx = Math.round(x);
        int yy = Math.round(y);
        this.buttonArea = new Rect(xx, yy, xx + currentlyDisplayed.getWidth(), yy + currentlyDisplayed.getHeight());
    }

    public boolean isItTouched(float x, float y) {
        return buttonArea.contains((int) x, (int) y);
    }

    public void drawRectangle(Canvas canvas, Paint paint) {
        canvas.drawRect(this.buttonArea, paint);
    }

    public void update() {
        if (buttonPressed) {
            this.currentlyDisplayed = this.pressedBitmap;
            sendMessage(this.message);
        } else this.currentlyDisplayed = this.normalBitmap;
    }

    public void sendMessage(char message) {
        Log.v(LOG_TAG, "I am sending " + message);
    }
}

