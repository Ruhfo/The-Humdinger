package com.humdinger.humdinger.ControllerView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class CircleButton extends Button {
    float x, y, radius;
    char message;
    int normalColor, pressedColor;
    String buttonText;
    Paint buttonPaint, textPaint;
    Rect buttonArea;
    private final String LOG_TAG = CircleButton.class.getSimpleName();

    public CircleButton(float x, float y, float radius, char message, int color, String buttonText) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.message = message;
        this.normalColor = color;
        this.pressedColor = Color.WHITE;
        this.buttonText = buttonText;

        this.buttonPaint = new Paint();
        this.buttonPaint.setStyle(Paint.Style.FILL);
        this.textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        this.textPaint.setTextSize(radius);
        setRectangleOnButton();
    }

    @Override
    public void setRectangleOnButton() {
        this.buttonArea = new Rect(Math.round(this.x-radius),Math.round(this.y-radius),Math.round(this.x + radius),Math.round(this.y + radius));
    }

    @Override
    public boolean isItTouched(float x, float y) {
        return buttonArea.contains((int) x, (int) y);
    }

    @Override
    public void drawRectangle(Canvas canvas, Paint paint) {
        canvas.drawRect(this.buttonArea, paint);
    }

    @Override
    void drawButton(Canvas c){
        c.drawCircle(x,y,radius, buttonPaint);
        c.drawText(buttonText,x-radius/4,y+radius/4,textPaint);
    }

    @Override
    public void update() {
        if (buttonPressed) {
            this.buttonPaint.setColor(pressedColor);
            this.sendMessage(this.message);
        } else this.buttonPaint.setColor(normalColor);
    }
}

