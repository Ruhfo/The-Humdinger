package com.humdinger.humdinger.ControllerView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.humdinger.humdinger.ControllerView.Button;

public class DirectionalPad extends Button {

    Rect buttonAreaUp, buttonAreaRight, buttonAreaDown, buttonAreaLeft;
    char messageUp, messageRight, messageDown, messageLeft;
    private int rectangleNr = 5;

    public DirectionalPad(float x, float y, Bitmap normalBitmap, Bitmap pressedBitmap, char messageUp, char messageRight, char messageDown, char messageLeft) {
        super(x, y, normalBitmap, pressedBitmap, 'm');
        this.messageUp = messageUp;
        this.messageRight = messageRight;
        this.messageDown = messageDown;
        this.messageLeft = messageLeft;
    }

    @Override
    //Rectangle is specified Left, Top, Right, Bottom
    public void setRectangleOnButton(float x, float y) {
        super.setRectangleOnButton(x, y);
        int xx = Math.round(x);
        int yy = Math.round(y);
        this.buttonAreaUp = new Rect(xx + this.currentlyDisplayed.getWidth() / 3, yy, xx + Math.round(this.currentlyDisplayed.getWidth() * 0.68f), yy + Math.round(this.currentlyDisplayed.getHeight() * 0.35f));
        this.buttonAreaRight = new Rect(xx + Math.round(this.currentlyDisplayed.getWidth() * 0.68f), yy + Math.round(this.currentlyDisplayed.getHeight() * 0.35f), xx + this.currentlyDisplayed.getWidth(), yy + Math.round(this.currentlyDisplayed.getHeight() * 0.68f));
        this.buttonAreaDown = new Rect(xx + this.currentlyDisplayed.getWidth() / 3, yy + Math.round(this.currentlyDisplayed.getHeight() * 0.68f), xx + Math.round(this.currentlyDisplayed.getWidth() * 0.68f), yy + this.currentlyDisplayed.getHeight());
        this.buttonAreaLeft = new Rect(xx, yy + Math.round(this.currentlyDisplayed.getHeight() * 0.35f), xx + this.currentlyDisplayed.getWidth() / 3, yy + Math.round(this.currentlyDisplayed.getHeight() * 0.68f));
    }

    @Override
    public void drawRectangle(Canvas canvas, Paint paint) {
        canvas.drawRect(buttonAreaUp, paint);
        canvas.drawRect(buttonAreaRight, paint);
        canvas.drawRect(buttonAreaDown, paint);
        canvas.drawRect(buttonAreaLeft, paint);
    }

    @Override
    public boolean isItTouched(float x, float y) {
        if (buttonAreaUp.contains((int) x, (int) y)) {
            this.rectangleNr = 8;
            return true;
        } else if (buttonAreaRight.contains((int) x, (int) y)) {
            this.rectangleNr = 6;
            return true;
        } else if (buttonAreaDown.contains((int) x, (int) y)) {
            this.rectangleNr = 2;
            return true;
        } else if (buttonAreaLeft.contains((int) x, (int) y)) {
            this.rectangleNr = 4;
            return true;
        } else {
            this.rectangleNr = 5;
            return false;
        }
    }

    @Override
    public void update() {
        if (this.buttonPressed) {
            this.currentlyDisplayed = this.pressedBitmap;
            switch (rectangleNr) {
                case 8:
                    sendMessage(this.messageUp);
                    break;
                case 6:
                    sendMessage(this.messageRight);
                    break;
                case 2:
                    sendMessage(this.messageDown);
                    break;
                case 4:
                    sendMessage(this.messageLeft);
                    break;
            }
        } else this.currentlyDisplayed = this.normalBitmap;
    }
}
