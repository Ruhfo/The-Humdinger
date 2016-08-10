package com.humdinger.humdinger.ControllerView;

import java.util.ArrayList;

public class DirectionalPad{

    float x,y,sideLength;
    char messageUp, messageRight, messageDown, messageLeft;
    int normalColor;
    ArrayList<Button> DPadButtons;

    public DirectionalPad(float x, float y, float sideLength, char messageUp, char messageRight, char messageDown, char messageLeft, int color, ArrayList<Button> buttons){
        this.x = x;
        this.y = y;
        this.sideLength = sideLength;
        this.messageUp = messageUp;
        this.messageRight = messageRight;
        this.messageDown = messageDown;
        this.messageLeft = messageLeft;
        this.normalColor = color;

        this.DPadButtons = buttons;
        setRectangleOnButton();
    }

    void setRectangleOnButton() {
        //Up
        DPadButtons.add(new RectButton(x+sideLength/3,y,sideLength/3, sideLength/3,this.messageUp, normalColor,"W"));
        //Right
        DPadButtons.add(new RectButton(x+(sideLength/3)*2,y+sideLength/3,sideLength/3, sideLength/3,this.messageRight, normalColor,"D"));
        //Down
        DPadButtons.add(new RectButton(x+sideLength/3,y+(sideLength/3)*2,sideLength/3, sideLength/3,this.messageDown, normalColor,"S"));
        //Left
        DPadButtons.add(new RectButton(x,y+sideLength/3,sideLength/3, sideLength/3,this.messageLeft, normalColor,"A"));
    }
}
