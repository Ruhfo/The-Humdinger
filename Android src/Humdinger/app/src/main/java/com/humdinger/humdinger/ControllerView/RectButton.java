package com.humdinger.humdinger.ControllerView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class RectButton extends Button {
    float x,y,width,height;
    char message;
    int normalColor, pressedColor;
    String buttonText;
    Paint buttonPaint, textPaint;
    Rect buttonArea;

    //2x radius is length of button, radius is the width
    public RectButton(float x, float y, float width, float height, char message, int color, String buttonText) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.message = message;
        this.normalColor = color;
        this.buttonText = buttonText;

        this.pressedColor = Color.LTGRAY;
        this.buttonPaint = new Paint(normalColor);
        this.buttonPaint.setStyle(Paint.Style.FILL);

        this.textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        int textSize = 1;
        this.textPaint.setTextSize(textSize);

        //Todo: Redo the code below to make it better
        //To make sure the text will be inside the button
        while(textPaint.measureText(buttonText) < height-20){
            textSize += 1;
            textPaint.setTextSize(textSize);
        }
        setRectangleOnButton();
    }

    @Override
    void update() {
        if (buttonPressed) {
            this.buttonPaint.setColor(pressedColor);
            this.sendMessage(this.message);
        } else this.buttonPaint.setColor(normalColor);
    }

    @Override
    void setRectangleOnButton() {
        this.buttonArea = new Rect(Math.round(x),Math.round(y),Math.round(x+width),Math.round(y+height));
    }

    @Override
    boolean isItTouched(float x, float y) {
        return buttonArea.contains((int) x, (int) y);
    }

    @Override
    void drawButton(Canvas canvas) {
        canvas.drawRect(x,y,x+width,y+height,buttonPaint);
        canvas.save();
        canvas.rotate(-90,x+width,y+height);
        canvas.drawText(buttonText,x+width*1.15f,y+height*0.9f,textPaint);
        canvas.restore();
    }

    @Override
    public void drawRectangle(Canvas canvas, Paint paint) {
        canvas.drawRect(this.buttonArea, paint);
    }

    @Override
    public String toString() {
        Log.v("But",buttonText);
        return  buttonText;
    }
}