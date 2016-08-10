package com.humdinger.humdinger.ControllerView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class RectButton extends Button {
    float x, y, width, height;
    char message;
    int normalColor, pressedColor;
    boolean orientation;
    String buttonText;
    Paint buttonPaint, textPaint;
    Rect buttonArea;

    public RectButton(float x, float y, float width, float height, char message, int color, String buttonText) {
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

        setRectangleOnButton();
        if(!buttonText.isEmpty()){
            setTextSize();
        }

    }

    @Override
    void update() {
        if (buttonPressed) {
            this.buttonPaint.setColor(pressedColor);
            this.sendMessage(this.message);
        } else {
            this.buttonPaint.setColor(normalColor);
        }
    }

    @Override
    void setRectangleOnButton() {
        this.buttonArea = new Rect(Math.round(x), Math.round(y), Math.round(x + width), Math.round(y + height));
    }

    @Override
    boolean isItTouched(float x, float y) {
        return buttonArea.contains((int) x, (int) y);
    }

    //ToDo: Make the below more generic - do something with the rotation code
    @Override
    void drawButton(Canvas canvas) {
        canvas.drawRect(x, y, x + width, y + height, buttonPaint);
        //To make sure the text will be inside the button
        //Vertical button
        if (orientation){
            canvas.save();
            canvas.rotate(-90,x+width,y+height);
            canvas.drawText(buttonText,x+width*1.15f,y+height*0.9f,textPaint);
            canvas.restore();
        }//Landscape button
        else canvas.drawText(buttonText,x,y+height,textPaint);
    }

    @Override
    public void drawRectangle(Canvas canvas, Paint paint) {
        canvas.drawRect(this.buttonArea, paint);
    }

    void setTextSize(){
        int textSize = 1;
        float sideToCompareWith;
        this.textPaint.setTextSize(textSize);
        //Check the dimensions of the button
        //False = landscape; True = vertical
        this.orientation = this.width < this.height;

        if(orientation) {
            sideToCompareWith = this.height;
        }
        else {
            sideToCompareWith = this.width;
        }

        while(textPaint.measureText(buttonText) < sideToCompareWith-20){
            textSize += 1;
            textPaint.setTextSize(textSize);
        }

    }
}

